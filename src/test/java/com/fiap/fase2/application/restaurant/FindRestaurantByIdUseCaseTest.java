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

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FindRestaurantByIdUseCaseTest {

    @Mock
    private RestaurantGateway restaurantGateway;

    private FindRestaurantByIdUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new FindRestaurantByIdUseCase(restaurantGateway);
    }

    @Test
    @DisplayName("Deve buscar restaurante por ID com sucesso")
    void shouldFindById() {
        UUID id = UUID.randomUUID();
        Restaurant restaurant = new Restaurant();
        restaurant.setId(id);
        when(restaurantGateway.findById(id)).thenReturn(Optional.of(restaurant));

        Restaurant result = useCase.execute(id);
        assertEquals(id, result.getId());
    }

    @Test
    @DisplayName("Deve lançar exceção quando não encontrado")
    void shouldThrowWhenNotFound() {
        UUID id = UUID.randomUUID();
        when(restaurantGateway.findById(id)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> useCase.execute(id));
    }
}
