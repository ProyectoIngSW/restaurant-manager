package co.edu.javeriana.restaurant.manager; 
/** 
* Clase principal de la aplicación. 
*/ 
public class Main { 
public static void main(String[] args) { 
System.out.println("=== Restaurant Manager ==="); 
System.out.println("Versión: 1.0.1");
Restaurant restaurant = new Restaurant("La Pizzeria"); 
restaurant.addMenuItem("Pizza Margherita", 12.99); 
restaurant.addMenuItem("Pasta Carbonara", 10.50); 
System.out.println("\n" + restaurant.getStatistics()); 
} 
} 