# 🐳 Docker Setup - Restaurant Manager
## Prerequisitos
- Docker Desktop instalado
- Docker Compose V2
## Quick Start
### Opción 1: Docker Compose (Recomendado)
```bash
# Levantar todos los servicios
docker-compose up -d
# Ver logs
docker-compose logs -f
# Detener
docker-compose down
 ```
### Opción 2: Solo Backend
```bash
# Construir imagen
docker build -f Dockerfile.multistage -t restaurant-manager:latest .
# Ejecutar
docker run -p 8080:8080 restaurant-manager:latest
 ```
## 🏗️ Arquitectura
```mermaid
flowchart LR
subgraph Docker_Compose_Stack [Docker Compose Stack]
A[Backend - Java 17\nPort: 8080] --> B[(PostgreSQL Database\nPort: 5432)]
end

subgraph Configuracion [Configuracion]
C[Network: restaurant-network - bridge]
D[Volume: postgres-data - persistencia]
end

Docker_Compose_Stack --> C
Docker_Compose_Stack --> D

 ```
## Servicios
### Backend
- **Image:** restaurant-manager:compose 
- **Puerto:** 8080
- **Variables de entorno:**
  - DB_HOST: Hostname de PostgreSQL 
  - DB_PORT: Puerto de PostgreSQL 
  - DB_NAME: Nombre de la base de datos 
  - DB_USER: Usuario de PostgreSQL 
  - DB_PASSWORD: Password de PostgreSQL 
### Database
- **Image:** postgres:15-alpine 
- **Puerto:** 5432
- **Variables de entorno:**
  - POSTGRES_DB: restaurant 
  - POSTGRES_USER: postgres 
  - POSTGRES_PASSWORD: restaurant123
## Comandos Útiles
### Gestión de Servicios
```bash
# Levantar servicios
docker-compose up -d
# Ver estado
docker-compose ps
# Ver logs
docker-compose logs -f [servicio]
# Reiniciar un servicio
docker-compose restart [servicio]
# Detener servicios
docker-compose down
# Detener y borrar volúmenes (CUIDADO)
docker-compose down -v
```
### Debugging
```bash
# Entrar al contenedor backend
docker-compose exec backend /bin/sh
# Entrar a PostgreSQL
docker-compose exec database psql -U postgres -d restaurant
# Ver logs de un servicio específico
docker-compose logs backend
docker-compose logs database
# Ver recursos utilizados
docker stats restaurant-backend restaurant-db
```
### Database
```bash
# Conectarse a PostgreSQL
docker-compose exec database psql -U postgres -d restaurant
# Ejecutar SQL desde archivo
docker-compose exec -T database psql -U postgres -d restaurant < script.sql
# Backup de la base de datos
docker-compose exec -T database pg_dump -U postgres restaurant > backup.sql
# Restore de backup
docker-compose exec -T database psql -U postgres -d restaurant < backup.sql
```
## Configuración
### Cambiar Puerto del Backend
Editar docker-compose.yml:
```bash
backend:
 ports:
 - "9000:8080" # Cambia 9000 al puerto deseado
 ```
### Cambiar Password de PostgreSQL
Editar docker-compose.yml:
```bash
database:
 environment:
 POSTGRES_PASSWORD: tu-nuevo-password
backend:
 environment:
 DB_PASSWORD: tu-nuevo-password
 ```
⚠ Si cambias la password después de la primera vez, debes borrar el volumen:
docker-compose down -v
docker-compose up -d
## Troubleshooting
### El backend no se conecta a la base de datos
1. Verificar que database está healthy:
docker-compose ps
2. Ver logs de database:
docker-compose logs database
3. Verificar variables de entorno:
docker-compose exec backend env | grep DB_
### Puerto ya en uso
Si el puerto 8080 o 5432 ya están en uso:
1. Cambiar en docker-compose.yml
2. O detener el proceso que usa el puerto
### Datos no persisten
Verificar que el volume existe:
```bash
docker volume ls | grep postgres
```
Si no existe, docker-compose lo creará automáticamente.
### Performance Issues
Aumentar recursos en Docker Desktop: - Settings → Resources → CPU/Memory
O ajustar JAVA_OPTS en docker-compose.yml:
```bash
backend:
 environment:
 JAVA_OPTS: "-Xmx512m -Xms256m" # Aumentar memoria
 ```
## Cleanup
### Limpiar contenedores detenidos
```bash
docker container prune
```
### Limpiar imágenes no usadas
```bash
docker image prune -a
```
### Limpiar volúmenes no usados
```bash
docker volume prune
```
### Limpiar todo (CUIDADO)
```bash
docker system prune -a --volumes
```
## Production Considerations
⚠ Este setup es para **desarrollo/testing**. Para producción:
1. **NO usar passwords en plain text**
   - Usar Docker Secrets o variables de entorno encriptadas
2. **Usar imágenes específicas, no :latest**

   image: restaurant-manager:1.0.3
3. **Configurar limits de recursos**
```bash
deploy:
 resources:
 limits:
 cpus: '0.50'
 memory: 512M
 ```
4. **Configurar restart policies**

   restart: unless-stopped
5. **Usar reverse proxy (nginx)**
6. **Configurar SSL/TLS**
7. **Implementar backups automáticos**
8. **Monitoreo y logging centralizado**
### Links
- [📘 Docker Documentation](https://docs.docker.com/)
- [🐘 PostgreSQL Docker Image](https://hub.docker.com/_/postgres)
