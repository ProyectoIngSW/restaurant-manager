package co.edu.javeriana.restaurant.manager;
 
import org.junit.jupiter.api.BeforeEach; 
import org.junit.jupiter.api.Test; 
import org.junit.jupiter.api.DisplayName; 
import org.junit.jupiter.api.Tag; 
import static org.assertj.core.api.Assertions.assertThat; 
 
/** 
 * Tests de integración que prueban flujos completos. 
 */ 
@Tag("integration") 
@DisplayName("Tests de Integración de Restaurant") 
public class RestaurantIntegrationTest { 
 
    private Restaurant restaurant; 
 
    @BeforeEach 
    public void setUp() { 
        restaurant = new Restaurant("Integration Test Restaurant"); 
    } 
 
    @Test 
    @DisplayName("Flujo completo: agregar menú, procesar órdenes, crear reservas") 
    public void testCompleteRestaurantFlow() { 
        // Paso 1: Crear menú 
        restaurant.addMenuItem("Pizza Margherita", 12.99); 
        restaurant.addMenuItem("Pasta Carbonara", 10.50); 
        restaurant.addMenuItem("Tiramisu", 6.99); 
        restaurant.addMenuItem("Vino", 8.50); 
 
        assertThat(restaurant.getMenuSize()).isEqualTo(4); 
 
        // Paso 2: Procesar múltiples órdenes 
        restaurant.processOrder("Pizza Margherita", 12.99); 
        restaurant.processOrder("Pasta Carbonara", 10.50); 
        restaurant.processOrder("Vino", 8.50); 
        restaurant.processOrder("Tiramisu", 6.99); 
 
        double expectedRevenue = 12.99 + 10.50 + 8.50 + 6.99; 
        assertThat(restaurant.getTotalRevenue()).isEqualTo(expectedRevenue); 
 
        // Paso 3: Crear reservas 
        restaurant.makeReservation("Alice Smith", 2, "2024-12-24 18:00"); 
        restaurant.makeReservation("Bob Johnson", 4, "2024-12-24 19:30"); 
        restaurant.makeReservation("Carol White", 6, "2024-12-25 20:00"); 
 
        assertThat(restaurant.getReservations()).hasSize(3); 
 
        // Paso 4: Verificar estadísticas completas 
        String stats = restaurant.getStatistics(); 
        assertThat(stats) 
            .contains("Integration Test Restaurant") 
            .contains("Items en menú: 4") 
            .contains("Reservas: 3") 
            .contains(String.format("%.2f", expectedRevenue)); 
    } 
 
    @Test 
    @DisplayName("Escenario: Día ocupado del restaurante") 
    public void testBusyDayScenario() { 
        // Preparar menú del día 
        restaurant.addMenuItem("Menú del día - Entrada", 5.00); 
        restaurant.addMenuItem("Menú del día - Principal", 12.00); 
        restaurant.addMenuItem("Menú del día - Postre", 4.00); 
 
        // Simular 10 clientes comprando el menú completo 
        for (int i = 0; i < 10; i++) { 
            restaurant.processOrder("Menú completo", 21.00); 
        } 
 
        // Verificar ingresos totales 
        assertThat(restaurant.getTotalRevenue()).isEqualTo(210.00); 
 
        // Simular reservas para la noche 
        for (int i = 1; i <= 5; i++) { 
            restaurant.makeReservation( 
                "Customer " + i,  
                2 + (i % 3),  
                "2024-12-25 " + (18 + i) + ":00" 
            ); 
        } 
 
        assertThat(restaurant.getReservations()).hasSize(5); 
    } 
 
    @Test 
    @DisplayName("Escenario: Actualización del menú durante el día") 
    public void testMenuUpdateScenario() { 
        // Menú inicial 
        restaurant.addMenuItem("Pizza", 12.99); 
        restaurant.addMenuItem("Pasta", 10.50); 
 
        int initialSize = restaurant.getMenuSize(); 
 
        // Agregar especiales del día 
        restaurant.addMenuItem("Especial: Risotto", 14.99); 
        restaurant.addMenuItem("Especial: Salmón", 18.50); 
 
        assertThat(restaurant.getMenuSize()).isEqualTo(initialSize + 2); 
 
        // Remover item que se agotó 
        boolean removed = restaurant.removeMenuItem("Pasta"); 
        assertThat(removed).isTrue(); 
        assertThat(restaurant.getMenuSize()).isEqualTo(initialSize + 1); 
    } 
 
    @Test 
    @DisplayName("Escenario: Cancelaciones y cambios de reservas") 
    public void testReservationChangesScenario() { 
        // Crear varias reservas 
restaurant.makeReservation("Customer A", 2, "2024-12-25 18:00"); 
restaurant.makeReservation("Customer B", 4, "2024-12-25 19:00"); 
restaurant.makeReservation("Customer C", 3, "2024-12-25 20:00"); 
int initialReservations = restaurant.getReservations().size(); 
assertThat(initialReservations).isEqualTo(3); 
// En un escenario real, aquí podríamos tener métodos para 
// cancelar o modificar reservas 
// Por ahora, solo verificamos que las reservas existen 
assertThat(restaurant.getReservations()) 
.anyMatch(r -> r.contains("Customer A")) 
.anyMatch(r -> r.contains("Customer B")) 
.anyMatch(r -> r.contains("Customer C")); 
} 
} 