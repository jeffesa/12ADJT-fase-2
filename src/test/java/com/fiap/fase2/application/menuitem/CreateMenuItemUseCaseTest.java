package com.fiap.fase2.application.menuitem;

import com.fiap.fase2.domain.menuitem.MenuItem;
import com.fiap.fase2.domain.menuitem.MenuItemGateway;
import com.fiap.fase2.domain.restaurant.Restaurant;
import com.fiap.fase2.domain.restaurant.RestaurantGateway;
import com.fiap.fase2.domain.shared.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateMenuItemUseCaseTest {

    @Mock private MenuItemGateway menuItemGateway;
    @Mock private RestaurantGateway restaurantGateway;
    private CreateMenuItemUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new CreateMenuItemUseCase(menuItemGateway, restaurantGateway);
    }

    @Test
    @DisplayName("Deve criar item com sucesso")
    void shouldCreate() {
        UUID restaurantId = UUID.randomUUID();
        when(restaurantGateway.findById(restaurantId)).thenReturn(Optional.of(new Restaurant()));
        when(menuItemGateway.create(any(MenuItem.class))).thenAnswer(inv -> inv.getArgument(0));

        MenuItem result = useCase.execute("Pizza", "Margherita", new BigDecimal("39.90"), false, "/img/pizza.jpg", restaurantId);

        assertNotNull(result);
        assertEquals("Pizza", result.getName());
        verify(menuItemGateway).create(any(MenuItem.class));
    }

    @Test
    @DisplayName("Deve lançar exceção com restaurante inexistente")
    void shouldThrowWhenRestaurantNotFound() {
        UUID restaurantId = UUID.randomUUID();
        when(restaurantGateway.findById(restaurantId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () ->
                useCase.execute("Pizza", "desc", BigDecimal.TEN, false, null, restaurantId));
        verify(menuItemGateway, never()).create(any());
    }

    @Test
    @DisplayName("Deve criar item quando userId é o dono do restaurante")
    void shouldCreateWhenUserIsOwner() {
        UUID restaurantId = UUID.randomUUID();
        UUID ownerId = UUID.randomUUID();
        Restaurant restaurant = new Restaurant(restaurantId, "Test", "Addr", "ITALIANA",
                java.time.LocalDateTime.now(), java.time.LocalDateTime.now(), ownerId);
        when(restaurantGateway.findById(restaurantId)).thenReturn(Optional.of(restaurant));
        when(menuItemGateway.create(any(MenuItem.class))).thenAnswer(inv -> inv.getArgument(0));

        MenuItem result = useCase.execute("Pizza", "desc", BigDecimal.TEN, false, null, restaurantId, ownerId);
        assertNotNull(result);
    }

    @Test
    @DisplayName("Deve lançar exceção quando userId não é o dono do restaurante")
    void shouldThrowWhenUserIsNotOwner() {
        UUID restaurantId = UUID.randomUUID();
        UUID ownerId = UUID.randomUUID();
        UUID otherUser = UUID.randomUUID();
        Restaurant restaurant = new Restaurant(restaurantId, "Test", "Addr", "ITALIANA",
                java.time.LocalDateTime.now(), java.time.LocalDateTime.now(), ownerId);
        when(restaurantGateway.findById(restaurantId)).thenReturn(Optional.of(restaurant));

        assertThrows(com.fiap.fase2.domain.shared.BusinessException.class, () -> {
            useCase.execute("Pizza", "desc", BigDecimal.TEN, false, null, restaurantId, otherUser);
        });
        verify(menuItemGateway, never()).create(any());
    }
}
