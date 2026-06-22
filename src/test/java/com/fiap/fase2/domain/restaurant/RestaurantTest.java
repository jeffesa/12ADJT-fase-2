package com.fiap.fase2.domain.restaurant;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class RestaurantTest {

    @Test
    @DisplayName("Deve criar Restaurant com todos os campos válidos")
    void shouldCreateRestaurantWithValidData() {
        UUID id = UUID.randomUUID();
        UUID ownerId = UUID.randomUUID();
        String openingHours = "Seg-Sex 11:00-23:00";

        Restaurant restaurant = new Restaurant(id, "Pizzaria Italia", "Rua A, 123", "ITALIANO", openingHours, ownerId);
        restaurant.setId(id);

        assertEquals(id, restaurant.getId());
        assertEquals("Pizzaria Italia", restaurant.getName());
        assertEquals("Rua A, 123", restaurant.getAddress());
        assertEquals("ITALIANO", restaurant.getCuisineType());
        assertEquals(openingHours, restaurant.getOpeningHours());
        assertEquals(ownerId, restaurant.getOwnerId());
    }

    @Test
    @DisplayName("Deve criar Restaurant com construtor padrão")
    void shouldCreateWithDefaultConstructor() {
        Restaurant restaurant = new Restaurant();
        assertNotNull(restaurant);
        assertNull(restaurant.getId());
        assertNull(restaurant.getName());
    }

    @Test
    @DisplayName("Deve alterar campos via setters")
    void shouldSetFields() {
        Restaurant restaurant = new Restaurant();
        UUID id = UUID.randomUUID();
        UUID ownerId = UUID.randomUUID();
        String openingHours = "Seg-Sex 11:00-23:00";

        restaurant.setId(id);
        restaurant.setName("Sushi House");
        restaurant.setAddress("Rua B, 456");
        restaurant.setCuisineType("JAPANESE");
        restaurant.setOpeningHours(openingHours);
        restaurant.setOwnerId(ownerId);

        assertEquals(id, restaurant.getId());
        assertEquals("Sushi House", restaurant.getName());
        assertEquals("Rua B, 456", restaurant.getAddress());
        assertEquals("JAPANESE", restaurant.getCuisineType());
        assertEquals(openingHours, restaurant.getOpeningHours());
        assertEquals(ownerId, restaurant.getOwnerId());
    }

    @Test
    @DisplayName("Deve lançar exceção ao criar com nome nulo")
    void shouldThrowExceptionWhenNameIsNull() {
        UUID id = UUID.randomUUID();
        UUID ownerId = UUID.randomUUID();
        String openingHours = "Seg-Sex 11:00-23:00";

        assertThrows(IllegalArgumentException.class, () -> new Restaurant(id, null, "Rua A, 123", "ITALIANO", openingHours, ownerId));
    }

    @Test
    @DisplayName("Deve lançar exceção ao criar com nome vazio")
    void shouldThrowExceptionWhenNameIsBlank() {
        UUID id = UUID.randomUUID();
        UUID ownerId = UUID.randomUUID();
        String openingHours = "Seg-Sex 11:00-23:00";

        assertThrows(IllegalArgumentException.class, () -> new Restaurant(id, "   ", "Rua A, 123", "ITALIANO", openingHours, ownerId));
    }

    @Test
    @DisplayName("Deve lançar exceção ao setar nome nulo")
    void shouldThrowExceptionWhenSetNameNull() {
        Restaurant restaurant = createValidRestaurant();
        assertThrows(IllegalArgumentException.class, () -> restaurant.setName(null));
    }

    @Test
    @DisplayName("Deve lançar exceção ao criar com endereço nulo")
    void shouldThrowExceptionWhenAddressIsNull() {
        UUID id = UUID.randomUUID();
        UUID ownerId = UUID.randomUUID();
        String openingHours = "Seg-Sex 11:00-23:00";

        assertThrows(IllegalArgumentException.class, () -> new Restaurant(id, "Pizzaria", null, "ITALIANO", openingHours, ownerId));
    }

    @Test
    @DisplayName("Deve lançar exceção ao criar com endereço vazio")
    void shouldThrowExceptionWhenAddressIsBlank() {
        UUID id = UUID.randomUUID();
        UUID ownerId = UUID.randomUUID();
        String openingHours = "Seg-Sex 11:00-23:00";

        assertThrows(IllegalArgumentException.class, () -> new Restaurant(id, "Pizzaria", "   ", "ITALIANO", openingHours, ownerId));
    }

    @Test
    @DisplayName("Deve lançar exceção ao setar endereço nulo")
    void shouldThrowExceptionWhenSetAddressNull() {
        Restaurant restaurant = createValidRestaurant();
        assertThrows(IllegalArgumentException.class, () -> restaurant.setAddress(null));
    }

    @Test
    @DisplayName("Deve lançar exceção ao setar endereço vazio")
    void shouldThrowExceptionWhenSetAddressBlank() {
        Restaurant restaurant = createValidRestaurant();
        assertThrows(IllegalArgumentException.class, () -> restaurant.setAddress(""));
    }

    @Test
    @DisplayName("Deve lançar exceção ao criar com tipo de cozinha nulo")
    void shouldThrowExceptionWhenCuisineTypeIsNull() {
        UUID id = UUID.randomUUID();
        UUID ownerId = UUID.randomUUID();
        String openingHours = "Seg-Sex 11:00-23:00";

        assertThrows(IllegalArgumentException.class, () -> new Restaurant(id, "Pizzaria", "Rua A, 123", null, openingHours, ownerId));
    }

    @Test
    @DisplayName("Deve lançar exceção ao setar tipo de cozinha nulo")
    void shouldThrowExceptionWhenSetCuisineTypeNull() {
        Restaurant restaurant = createValidRestaurant();
        assertThrows(IllegalArgumentException.class, () -> restaurant.setCuisineType(null));
    }

    @Test
    @DisplayName("Deve lançar exceção ao criar com horário de abertura nulo")
    void shouldThrowExceptionWhenOpeningHoursIsNull() {
        UUID id = UUID.randomUUID();
        UUID ownerId = UUID.randomUUID();

        assertThrows(IllegalArgumentException.class, () -> new Restaurant(id, "Pizzaria", "Rua A, 123", "ITALIANO", null, ownerId));
    }

    @Test
    @DisplayName("Deve lançar exceção ao setar horário de abertura nulo")
    void shouldThrowExceptionWhenSetOpeningHoursNull() {
        Restaurant restaurant = createValidRestaurant();
        assertThrows(IllegalArgumentException.class, () -> restaurant.setOpeningHours(null));
    }

    @Test
    @DisplayName("Deve lançar exceção ao criar com ID do proprietário nulo")
    void shouldThrowExceptionWhenOwnerIdIsNull() {
        UUID id = UUID.randomUUID();
        String openingHours = "Seg-Sex 11:00-23:00";

        assertThrows(IllegalArgumentException.class, () -> new Restaurant(id, "Pizzaria", "Rua A, 123", "ITALIANO", openingHours, null));
    }

    @Test
    @DisplayName("Deve lançar exceção ao setar ID do proprietário nulo")
    void shouldThrowExceptionWhenSetOwnerIdNull() {
        Restaurant restaurant = createValidRestaurant();
        assertThrows(IllegalArgumentException.class, () -> restaurant.setOwnerId(null));
    }

    private Restaurant createValidRestaurant() {
        return new Restaurant(UUID.randomUUID(), "Restaurante Teste", "Rua Teste, 123", "ITALIANA", "Seg-Sex 11:00-23:00", UUID.randomUUID());
    }
}
