package com.fiap.fase2.infra.persistence.restaurant;

import com.fiap.fase2.domain.restaurant.Restaurant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@Import(RestaurantJpaGateway.class)
class RestaurantJpaGatewayTest {

    @Autowired
    private RestaurantJpaGateway gateway;

    private Restaurant buildRestaurant(UUID ownerId) {
        return new Restaurant(UUID.randomUUID(), "Pizzaria", "Rua A, 123", "ITALIANA",
                LocalDateTime.of(2024, 1, 1, 11, 0), LocalDateTime.of(2024, 1, 1, 23, 0), ownerId);
    }

    @Test
    @DisplayName("Deve criar restaurante")
    void shouldCreate() {
        Restaurant restaurant = buildRestaurant(UUID.randomUUID());
        Restaurant saved = gateway.create(restaurant);
        assertNotNull(saved);
        assertEquals("Pizzaria", saved.getName());
    }

    @Test
    @DisplayName("Deve buscar por ID")
    void shouldFindById() {
        Restaurant restaurant = gateway.create(buildRestaurant(UUID.randomUUID()));
        Optional<Restaurant> result = gateway.findById(restaurant.getId());
        assertTrue(result.isPresent());
    }

    @Test
    @DisplayName("Deve retornar vazio para ID inexistente")
    void shouldReturnEmptyForNonExistentId() {
        assertTrue(gateway.findById(UUID.randomUUID()).isEmpty());
    }

    @Test
    @DisplayName("Deve listar todos")
    void shouldFindAll() {
        gateway.create(buildRestaurant(UUID.randomUUID()));
        gateway.create(buildRestaurant(UUID.randomUUID()));
        List<Restaurant> result = gateway.findAll();
        assertEquals(2, result.size());
    }

    @Test
    @DisplayName("Deve buscar por ownerId")
    void shouldFindByOwnerId() {
        UUID ownerId = UUID.randomUUID();
        gateway.create(buildRestaurant(ownerId));
        gateway.create(buildRestaurant(ownerId));
        gateway.create(buildRestaurant(UUID.randomUUID()));
        List<Restaurant> result = gateway.findByOwnerId(ownerId);
        assertEquals(2, result.size());
    }

    @Test
    @DisplayName("Deve deletar")
    void shouldDelete() {
        Restaurant restaurant = gateway.create(buildRestaurant(UUID.randomUUID()));
        gateway.delete(restaurant.getId());
        assertTrue(gateway.findById(restaurant.getId()).isEmpty());
    }
}
