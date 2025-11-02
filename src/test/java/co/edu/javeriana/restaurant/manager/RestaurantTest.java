package co.edu.javeriana.restaurant.manager;
 
import org.junit.jupiter.api.BeforeEach; 
import org.junit.jupiter.api.Test; 
import org.junit.jupiter.api.DisplayName; 
import org.junit.jupiter.api.Tag; 
import static org.junit.jupiter.api.Assertions.*; 
import static org.assertj.core.api.Assertions.assertThat; 
import static org.assertj.core.api.Assertions.within;
 
/** 
 * Tests unitarios para la clase Restaurant. 
 */ 
@Tag("unit") 
@DisplayName("Tests Unitarios de Restaurant") 
public class RestaurantTest { 
 
    private Restaurant restaurant; 
 
    @BeforeEach 
    public void setUp() { 
        restaurant = new Restaurant("Test Restaurant"); 
    } 
 
    @Test 
    @DisplayName("Constructor crea restaurante correctamente") 
    public void testConstructor() { 
        assertThat(restaurant.getName()).isEqualTo("Test Restaurant"); 
        assertThat(restaurant.getMenuSize()).isZero(); 
        assertThat(restaurant.getTotalRevenue()).isZero(); 
    } 
 
    @Test 
    @DisplayName("Constructor lanza excepción con nombre vacío") 
    public void testConstructorEmptyName() { 
        assertThrows(IllegalArgumentException.class,  
            () -> new Restaurant("")); 
        assertThrows(IllegalArgumentException.class,  
            () -> new Restaurant(null)); 
    } 
 
    @Test 
    @DisplayName("Agregar item al menú aumenta el tamaño") 
    public void testAddMenuItem() { 
        restaurant.addMenuItem("Pizza", 12.99); 
         
        assertThat(restaurant.getMenuSize()).isEqualTo(1); 
        assertThat(restaurant.getMenu()).hasSize(1); 
        assertThat(restaurant.getMenu().get(0)).contains("Pizza"); 
    } 
 
    @Test 
    @DisplayName("Agregar múltiples items funciona correctamente") 
    public void testAddMultipleMenuItems() { 
        restaurant.addMenuItem("Pizza", 12.99); 
        restaurant.addMenuItem("Pasta", 10.50); 
        restaurant.addMenuItem("Salad", 8.00); 
         
        assertThat(restaurant.getMenuSize()).isEqualTo(3); 
    } 
 
    @Test 
    @DisplayName("Agregar item con precio negativo lanza excepción") 
    public void testAddMenuItemNegativePrice() { 
        Exception exception = assertThrows(IllegalArgumentException.class, 
            () -> restaurant.addMenuItem("Pizza", -5.0)); 
         
        assertThat(exception.getMessage()).contains("negativo"); 
    } 
 
    @Test 
    @DisplayName("Remover item existente retorna true") 
    public void testRemoveMenuItem() { 
        restaurant.addMenuItem("Pizza", 12.99); 
        restaurant.addMenuItem("Pasta", 10.50); 
         
        boolean removed = restaurant.removeMenuItem("Pizza"); 
         
        assertThat(removed).isTrue(); 
        assertThat(restaurant.getMenuSize()).isEqualTo(1); 
    } 
 
    @Test 
    @DisplayName("Remover item inexistente retorna false") 
    public void testRemoveNonExistentItem() { 
        restaurant.addMenuItem("Pizza", 12.99); 
         
        boolean removed = restaurant.removeMenuItem("Burger"); 
         
        assertThat(removed).isFalse(); 
        assertThat(restaurant.getMenuSize()).isEqualTo(1); 
    } 
 
    @Test 
    @DisplayName("Procesar orden incrementa ingresos") 
    public void testProcessOrder() { 
        restaurant.processOrder("Pizza", 12.99); 
         
        assertThat(restaurant.getTotalRevenue()).isEqualTo(12.99); 
    } 
    
    @Test
    @DisplayName("Procesar múltiples órdenes suma ingresos")
    public void testProcessMultipleOrders() {
        restaurant.processOrder("Pizza", 12.99);
        restaurant.processOrder("Pasta", 10.50);
        restaurant.processOrder("Drink", 3.50);
        
        // Se usa tolerancia para evitar errores de precisión en double
        assertThat(restaurant.getTotalRevenue()).isCloseTo(26.99, within(0.001));
}

    @Test 
    @DisplayName("Procesar orden con precio inválido lanza excepción") 
    public void testProcessOrderInvalidPrice() { 
        assertThrows(IllegalArgumentException.class, 
            () -> restaurant.processOrder("Pizza", 0)); 
        assertThrows(IllegalArgumentException.class, 
            () -> restaurant.processOrder("Pizza", -10)); 
    } 
 
    @Test 
    @DisplayName("Crear reserva válida funciona correctamente") 
    public void testMakeReservation() { 
        restaurant.makeReservation("John Doe", 4, "2024-12-25 19:00"); 
         
        assertThat(restaurant.getReservations()).hasSize(1); 
        assertThat(restaurant.getReservations().get(0)) 
            .contains("John Doe") 
            .contains("4 personas"); 
    } 
 
    @Test 
    @DisplayName("Crear reserva con datos inválidos lanza excepción") 
    public void testMakeReservationInvalidData() { 
        assertThrows(IllegalArgumentException.class, 
            () -> restaurant.makeReservation("", 4, "2024-12-25")); 
        assertThrows(IllegalArgumentException.class, 
            () -> restaurant.makeReservation("John", 0, "2024-12-25")); 
        assertThrows(IllegalArgumentException.class, 
            () -> restaurant.makeReservation("John", 4, "")); 
    } 
 
    @Test 
    @DisplayName("Obtener estadísticas retorna información completa") 
    public void testGetStatistics() { 
        restaurant.addMenuItem("Pizza", 12.99); 
        restaurant.processOrder("Pizza", 12.99); 
        restaurant.makeReservation("John", 4, "2024-12-25 19:00"); 
         
        String stats = restaurant.getStatistics(); 
         
        assertThat(stats) 
            .contains("Test Restaurant") 
            .contains("Items en menú: 1") 
            .contains("Reservas: 1") 
            .contains("12.99"); 
    } 
} 