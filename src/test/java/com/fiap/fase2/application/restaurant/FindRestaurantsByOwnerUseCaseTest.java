package com.fiap.fase2.application.restaurant;

import com.fiap.fase2.domain.restaurant.Restaurant;
import com.fiap.fase2.domain.restaurant.RestaurantGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FindRestaurantsByOwnerUseCaseTest {

    @Mock
    private RestaurantGateway restaurantGateway;
    private FindRestaurantsByOwnerUseCase useCase;

    @BeforeEach
    public void setUp() {
        useCase = new FindRestaurantsByOwnerUseCase(restaurantGateway);
    }

    @Test
    @DisplayName("Deve retornar lista de restaurantes do proprietário com sucesso")
    void shouldReturnRestaurantsByOwnerSuccessfully() {
        UUID ownerId = UUID.randomUUID();
        UUID restaurantId1 = UUID.randomUUID();
        UUID restaurantId2 = UUID.randomUUID();

        Restaurant restaurant1 = new Restaurant(restaurantId1, "Pizzaria do João", "Rua A, 123", "ITALIANA",
                LocalDateTime.of(2024, 1, 1, 11, 0), LocalDateTime.of(2024, 1, 1, 23, 0), ownerId);
        Restaurant restaurant2 = new Restaurant(restaurantId2, "Churrascaria do Sul", "Rua B, 456", "BRASILEIRA",
                LocalDateTime.of(2024, 1, 1, 12, 0), LocalDateTime.of(2024, 1, 2, 0, 0), ownerId);

        List<Restaurant> restaurants = new ArrayList<>();
        restaurants.add(restaurant1);
        restaurants.add(restaurant2);

        when(restaurantGateway.findByOwnerId(ownerId)).thenReturn(restaurants);

        List<Restaurant> result = useCase.execute(ownerId);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(restaurant1, result.get(0));
        assertEquals(restaurant2, result.get(1));
        verify(restaurantGateway).findByOwnerId(ownerId);
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando proprietário não tem restaurantes")
    void shouldReturnEmptyListWhenOwnerHasNoRestaurants() {
        UUID ownerId = UUID.randomUUID();
        List<Restaurant> emptyList = new ArrayList<>();
        when(restaurantGateway.findByOwnerId(ownerId)).thenReturn(emptyList);

        List<Restaurant> result = useCase.execute(ownerId);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        assertEquals(0, result.size());
        verify(restaurantGateway).findByOwnerId(ownerId);
    }

    @Test
    @DisplayName("Deve chamar findByOwnerId do gateway exatamente uma vez")
    void shouldCallFindByOwnerIdOnceFromGateway() {
        UUID ownerId = UUID.randomUUID();
        List<Restaurant> restaurants = new ArrayList<>();
        when(restaurantGateway.findByOwnerId(ownerId)).thenReturn(restaurants);

        useCase.execute(ownerId);

        verify(restaurantGateway, times(1)).findByOwnerId(ownerId);
    }

    @Test
    @DisplayName("Deve retornar restaurante do proprietário específico")
    void shouldReturnOnlyRestaurantsFromSpecificOwner() {
        UUID ownerId = UUID.randomUUID();
        UUID restaurantId = UUID.randomUUID();

        Restaurant restaurant = new Restaurant(restaurantId, "Restaurante do Dono", "Rua C, 789", "CHINESA",
                LocalDateTime.of(2024, 1, 1, 10, 0), LocalDateTime.of(2024, 1, 1, 22, 0), ownerId);

        List<Restaurant> restaurants = new ArrayList<>();
        restaurants.add(restaurant);

        when(restaurantGateway.findByOwnerId(ownerId)).thenReturn(restaurants);

        List<Restaurant> result = useCase.execute(ownerId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(ownerId, result.get(0).getOwnerId());
        verify(restaurantGateway).findByOwnerId(ownerId);
    }
}
