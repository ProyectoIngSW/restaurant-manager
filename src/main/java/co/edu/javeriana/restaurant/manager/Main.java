package co.edu.javeriana.restaurant.manager;
 
public class Main {
    public static void main(String[] args) { 
        System.out.println("=== Restaurant Manager ==="); 
        System.out.println("Versión: 1.0.0");
        System.out.println(); 
         
        // Test database connection 
        DatabaseConnection.testConnection(); 
        System.out.println(); 
         
        // Demo de la aplicación 
        Restaurant restaurant = new Restaurant("La Pizzeria"); 
        restaurant.addMenuItem("Pizza Margherita", 12.99); 
        restaurant.addMenuItem("Pasta Carbonara", 10.50); 
         
        System.out.println(restaurant.getStatistics()); 
    }
} 