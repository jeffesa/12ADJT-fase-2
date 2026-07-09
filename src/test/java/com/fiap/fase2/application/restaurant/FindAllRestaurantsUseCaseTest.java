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
public class FindAllRestaurantsUseCaseTest {

    @Mock
    private RestaurantGateway restaurantGateway;

    private FindAllRestaurantsUseCase useCase;

    @BeforeEach
    public void setUp() {
        useCase = new FindAllRestaurantsUseCase(restaurantGateway);
    }

    @Test
    @DisplayName("Deve retornar lista com sucesso quando existem restaurantes")
    void shouldReturnRestaurantListSuccessfully() {
        UUID restaurantId1 = UUID.randomUUID();
        UUID restaurantId2 = UUID.randomUUID();
        UUID ownerId = UUID.randomUUID();
        
        Restaurant restaurant1 = new Restaurant(restaurantId1, "Pizzaria do João", "Rua A, 123", "ITALIANA",
                LocalDateTime.of(2024, 1, 1, 11, 0), LocalDateTime.of(2024, 1, 1, 23, 0), ownerId);
        Restaurant restaurant2 = new Restaurant(restaurantId2, "Churrascaria do Sul", "Rua B, 456", "BRASILEIRA",
                LocalDateTime.of(2024, 1, 1, 12, 0), LocalDateTime.of(2024, 1, 2, 0, 0), ownerId);
        
        List<Restaurant> restaurants = new ArrayList<>();
        restaurants.add(restaurant1);
        restaurants.add(restaurant2);
        
        when(restaurantGateway.findAll()).thenReturn(restaurants);

        List<Restaurant> result = useCase.execute();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(restaurant1, result.get(0));
        assertEquals(restaurant2, result.get(1));
        verify(restaurantGateway).findAll();
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando não há restaurantes")
    void shouldReturnEmptyListWhenNoRestaurants() {
        List<Restaurant> emptyList = new ArrayList<>();
        when(restaurantGateway.findAll()).thenReturn(emptyList);

        List<Restaurant> result = useCase.execute();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        assertEquals(0, result.size());
        verify(restaurantGateway).findAll();
    }

    @Test
    @DisplayName("Deve chamar findAll do gateway exatamente uma vez")
    void shouldCallFindAllOnceFromGateway() {
        List<Restaurant> restaurants = new ArrayList<>();
        when(restaurantGateway.findAll()).thenReturn(restaurants);

        useCase.execute();

        verify(restaurantGateway, times(1)).findAll();
    }
}
