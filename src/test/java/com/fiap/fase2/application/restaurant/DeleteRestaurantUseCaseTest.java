package com.fiap.fase2.application.restaurant;

import com.fiap.fase2.domain.restaurant.Restaurant;
import com.fiap.fase2.domain.restaurant.RestaurantGateway;
import com.fiap.fase2.domain.shared.BusinessException;
import com.fiap.fase2.domain.shared.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteRestaurantUseCaseTest {
    @Mock
    private RestaurantGateway restaurantGateway;

    private DeleteRestaurantUseCase useCase;
    private UUID restaurantId;
    private UUID ownerId;
    private Restaurant restaurant;

    @BeforeEach
    public void setUp() {
        useCase = new DeleteRestaurantUseCase(restaurantGateway);
        restaurantId = UUID.randomUUID();
        ownerId = UUID.randomUUID();
        restaurant = new Restaurant(restaurantId, "Pizzaria do João", "Rua A, 123", "ITALIANA",
                LocalDateTime.of(2024, Month.JANUARY, 1, 11, 0), LocalDateTime.of(2024, Month.JANUARY, 1, 23, 0), ownerId);
    }

    @Test
    @DisplayName("Deve deletar restaurante com sucesso")
    void shouldDeleteRestaurantSuccessfully() {
        when(restaurantGateway.findById(restaurantId)).thenReturn(Optional.of(restaurant));
        useCase.execute(restaurantId);
        verify(restaurantGateway).delete(restaurantId);
    }

    @Test
    @DisplayName("Deve lançar exceção quando restaurante não existe")
    void shouldThrowExceptionWhenRestaurantDoesNotExist() {
        when(restaurantGateway.findById(restaurantId)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> useCase.execute(restaurantId));
        verify(restaurantGateway, never()).delete(any());
    }

    @Test
    @DisplayName("Deve deletar quando userId é o proprietário")
    void shouldDeleteWhenUserIsOwner() {
        when(restaurantGateway.findById(restaurantId)).thenReturn(Optional.of(restaurant));
        useCase.execute(restaurantId, ownerId);
        verify(restaurantGateway).delete(restaurantId);
    }

    @Test
    @DisplayName("Deve lançar exceção quando userId não é o proprietário")
    void shouldThrowWhenUserIsNotOwner() {
        UUID otherUserId = UUID.randomUUID();
        when(restaurantGateway.findById(restaurantId)).thenReturn(Optional.of(restaurant));
        assertThrows(BusinessException.class, () -> {
            useCase.execute(restaurantId, otherUserId);
        });
        verify(restaurantGateway, never()).delete(any());
    }
}
