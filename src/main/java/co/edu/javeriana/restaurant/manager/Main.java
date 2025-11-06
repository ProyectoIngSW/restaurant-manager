package co.edu.javeriana.restaurant.manager;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println("=== Restaurant Manager ===");
        System.out.println("Versión: 1.0.0");
        System.out.println();

        // 1) Prueba de conexión a la base de datos (tu clase existente)
        DatabaseConnection.testConnection();
        System.out.println();

        // 2) Demo de la aplicación (tus clases existentes)
        final Restaurant restaurant = new Restaurant("La Pizzeria");
        restaurant.addMenuItem("Pizza Margherita", 12.99);
        restaurant.addMenuItem("Pasta Carbonara", 10.50);

        System.out.println(restaurant.getStatistics());

        // 3) Servidor HTTP mínimo para exponer estado/health
        int port = Integer.parseInt(System.getenv().getOrDefault("PORT", "8080"));
        HttpServer server = HttpServer.create(new InetSocketAddress("0.0.0.0", port), 0);

        // GET /health -> "OK"
        server.createContext("/health", (HttpExchange exchange) -> {
            byte[] resp = "OK".getBytes(StandardCharsets.UTF_8);
            try {
                exchange.sendResponseHeaders(200, resp.length);
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(resp);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                exchange.close();
            }
        });

        // GET / -> texto simple
        server.createContext("/", (HttpExchange exchange) -> {
            byte[] resp = "Restaurant Manager running".getBytes(StandardCharsets.UTF_8);
            try {
                exchange.sendResponseHeaders(200, resp.length);
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(resp);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                exchange.close();
            }
        });

        // GET /stats -> estadísticas actuales del restaurant
        server.createContext("/stats", (HttpExchange exchange) -> {
            byte[] resp = restaurant.getStatistics().getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().add("Content-Type", "text/plain; charset=utf-8");
            try {
                exchange.sendResponseHeaders(200, resp.length);
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(resp);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                exchange.close();
            }
        });

        server.setExecutor(null);
        server.start();
        System.out.println("HTTP server listening on 0.0.0.0:" + port);

        // Mantener el proceso vivo
        Thread.currentThread().join();
    }
}
