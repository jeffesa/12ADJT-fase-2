package com.fiap.fase2.application.restaurant;

import com.fiap.fase2.domain.restaurant.Restaurant;
import com.fiap.fase2.domain.restaurant.RestaurantGateway;
import com.fiap.fase2.domain.shared.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DeleteRestaurantUseCaseTest {
    @Mock
    private RestaurantGateway restaurantGateway;

    private DeleteRestaurantUseCase useCase;

    private UUID restaurantId;
    private Restaurant restaurant;

    @BeforeEach
    public void setUp() {
        useCase = new DeleteRestaurantUseCase(restaurantGateway);
        restaurantId = UUID.randomUUID();
        restaurant = new Restaurant(restaurantId, "Pizzaria do João", "Rua A, 123", "ITALIANA",
                LocalDateTime.of(2024, 1, 1, 11, 0), LocalDateTime.of(2024, 1, 1, 23, 0), UUID.randomUUID());
    }

    @Test
    @DisplayName("Deve deletar restaurante com sucesso quando o restaurante existe")
    void shouldDeleteRestaurantSuccessfully() {
        when(restaurantGateway.findById(restaurantId)).thenReturn(Optional.of(restaurant));

        useCase.execute(restaurantId);

        verify(restaurantGateway).findById(restaurantId);
        verify(restaurantGateway).delete(restaurantId);
    }

    @Test
    @DisplayName("Deve lançar exceção quando restaurante não existe")
    void shouldThrowExceptionWhenRestaurantDoesNotExist() {
        when(restaurantGateway.findById(restaurantId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> useCase.execute(restaurantId)
        );

        assertEquals("Restaurante não encontrado com id: " + restaurantId, exception.getMessage());
        verify(restaurantGateway).findById(restaurantId);
        verify(restaurantGateway, never()).delete(restaurantId);
    }
}
