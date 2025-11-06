#!/usr/bin/env bash
set -euo pipefail

echo "🧪 SMOKE TESTS - Restaurant Manager"
echo "===================================="
echo ""

# 🔧 Ajusta estos nombres a tus contenedores/DB reales:
BACKEND="restaurant-backend"
DB="restaurant-db"
DB_USER="postgres"
DB_NAME="restaurant"

# Helper para obtener estado Health (o 'none' si no existe):
health_of () {
  docker inspect "$1" --format='{{if .State.Health}}{{.State.Health.Status}}{{else}}none{{end}}' 2>/dev/null || echo "unknown"
}

# Test 1: Contenedores corriendo
echo "Test 1: Verificar contenedores..."
if docker ps | grep -q "Up"; then
  echo "✅ Contenedores están corriendo"
else
  echo "❌ Contenedores no están corriendo"
  exit 1
fi

# Test 2: Backend healthy (o al menos Up)
echo ""
echo "Test 2: Health check backend..."
BACKEND_HEALTH="$(health_of "$BACKEND")"
if [[ "$BACKEND_HEALTH" == "healthy" ]]; then
  echo "✅ Backend está healthy"
else
  # Si no hay HEALTHCHECK, al menos verifica que esté Up
  if docker ps --format '{{.Names}} {{.Status}}' | grep -q "^$BACKEND .*Up"; then
    echo "⚠️  Backend sin HEALTHCHECK (estado: $BACKEND_HEALTH), pero el contenedor está Up"
  else
    echo "❌ Backend no está corriendo (estado: $BACKEND_HEALTH)"
    exit 1
  fi
fi

# Test 3: Database healthy (o al menos Up)
echo ""
echo "Test 3: Health check database..."
DB_HEALTH="$(health_of "$DB")"
if [[ "$DB_HEALTH" == "healthy" ]]; then
  echo "✅ Database está healthy"
else
  if docker ps --format '{{.Names}} {{.Status}}' | grep -q "^$DB .*Up"; then
    echo "⚠️  Database sin HEALTHCHECK (estado: $DB_HEALTH), pero el contenedor está Up"
  else
    echo "❌ Database no está corriendo (estado: $DB_HEALTH)"
    exit 1
  fi
fi

# Test 4: Conectividad a base de datos
echo ""
echo "Test 4: Conectividad a base de datos..."
if docker exec -i "$DB" psql -U "$DB_USER" -d "$DB_NAME" -c "SELECT 1;" >/dev/null 2>&1; then
  echo "✅ Conexión a DB exitosa"
else
  echo "❌ No se pudo conectar a DB (SELECT 1 falló)"
  exit 1
fi

# Test 5: Verificar tablas (ejemplo: 'restaurants')
echo ""
echo "Test 5: Verificar tablas..."
if docker exec -i "$DB" psql -U "$DB_USER" -d "$DB_NAME" -c "\dt" | grep -q "restaurants"; then
  echo "✅ Tablas existen (encontrado 'restaurants')"
else
  echo "❌ Tablas no encontradas (no aparece 'restaurants' en \\dt)"
  exit 1
fi

echo ""
echo "===================================="
echo "✅ TODOS LOS TESTS PASARON"
echo "===================================="
