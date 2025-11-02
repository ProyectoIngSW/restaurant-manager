package co.edu.javeriana.restaurant.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Clase principal para gestionar un restaurante.
 * Maneja menú, órdenes, reservas y estadísticas.
 */
public class Restaurant {
    private final String name;
    private final List<String> menu;
    private final List<String> reservations;
    private double totalRevenue;

    /**
     * Constructor del restaurante.
     * @param name Nombre del restaurante
     */
    public Restaurant(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }
        this.name = name;
        this.menu = new ArrayList<>();
        this.reservations = new ArrayList<>();
        this.totalRevenue = 0.0;
    }

    public String getName() {
        return name;
    }

    public List<String> getMenu() {
        return new ArrayList<>(menu);
    }

    public double getTotalRevenue() {
        return totalRevenue;
    }

    public int getMenuSize() {
        return menu.size();
    }

    /** 
     * Agrega un item al menú con su precio. 
     */
    public void addMenuItem(String item, double price) {
        if (item == null || item.trim().isEmpty()) {
            throw new IllegalArgumentException("El item no puede estar vacío");
        }
        if (price < 0) {
            throw new IllegalArgumentException("El precio no puede ser negativo");
        }
        menu.add(String.format("%s - $%.2f", item, price));
    }

    /** 
     * Remueve un item del menú. 
     */
    public boolean removeMenuItem(String item) {
        if (item == null || item.trim().isEmpty()) {
            return false;
        }
        return menu.removeIf(menuItem -> menuItem.startsWith(item.trim()));
    }

    /** 
     * Procesa una orden y actualiza los ingresos. 
     */
    public void processOrder(String item, double price) {
        if (price <= 0) {
            throw new IllegalArgumentException("El precio debe ser positivo");
        }
        if (item == null || item.trim().isEmpty()) {
            throw new IllegalArgumentException("El item no puede estar vacío");
        }
        totalRevenue += price;
    }

    /** 
     * Crea una nueva reserva. 
     */
    public void makeReservation(String customerName, int partySize, String dateTime) {
        if (customerName == null || customerName.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre es requerido");
        }
        if (partySize <= 0) {
            throw new IllegalArgumentException("El tamaño debe ser positivo");
        }
        if (dateTime == null || dateTime.trim().isEmpty()) {
            throw new IllegalArgumentException("La fecha es requerida");
        }
        String reservation = String.format("%s - %d personas - %s",
                customerName, partySize, dateTime);
        reservations.add(reservation);
    }

    /** 
     * Obtiene todas las reservas. 
     */
    public List<String> getReservations() {
        return new ArrayList<>(reservations);
    }

    /** 
     * Obtiene estadísticas del restaurante.
     * 🔧 Se fuerza el uso del punto decimal (Locale.US) para coincidir con los tests.
     */
    public String getStatistics() {
        return String.format(Locale.US,
            "Restaurant: %s%n" +
            "Items en menú: %d%n" +
            "Reservas: %d%n" +
            "Ingresos totales: $%.2f",
            name, menu.size(), reservations.size(), totalRevenue
        );
    }
}
