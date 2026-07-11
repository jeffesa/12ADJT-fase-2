package com.fiap.fase2.application.menuitem;

import com.fiap.fase2.domain.menuitem.MenuItem;
import com.fiap.fase2.domain.menuitem.MenuItemGateway;
import com.fiap.fase2.domain.shared.BusinessException;
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
class UpdateMenuItemUseCaseTest {

    @Mock private MenuItemGateway menuItemGateway;
    @Mock private com.fiap.fase2.domain.restaurant.RestaurantGateway restaurantGateway;
    private UpdateMenuItemUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new UpdateMenuItemUseCase(menuItemGateway, restaurantGateway);
    }

    @Test
    @DisplayName("Deve atualizar com sucesso")
    void shouldUpdate() {
        UUID id = UUID.randomUUID();
        MenuItem existing = new MenuItem(id, "Pizza", "desc", BigDecimal.TEN, false, null, UUID.randomUUID());
        when(menuItemGateway.findById(id)).thenReturn(Optional.of(existing));
        when(menuItemGateway.update(any(MenuItem.class))).thenAnswer(inv -> inv.getArgument(0));

        MenuItem result = useCase.execute(id, "Sushi", "Salmão", new BigDecimal("45.00"), true, "/img/sushi.jpg");

        assertEquals("Sushi", result.getName());
        assertEquals(new BigDecimal("45.00"), result.getPrice());
        verify(menuItemGateway).update(any());
    }

    @Test
    @DisplayName("Deve lançar exceção com ID inexistente")
    void shouldThrowWhenNotFound() {
        UUID id = UUID.randomUUID();
        when(menuItemGateway.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () ->
                useCase.execute(id, "Sushi", "desc", BigDecimal.TEN, false, null));
    }

    @Test
    @DisplayName("Deve lançar exceção com preço zero")
    void shouldThrowWhenPriceIsZero() {
        UUID id = UUID.randomUUID();
        MenuItem existing = new MenuItem(id, "Pizza", "desc", BigDecimal.TEN, false, null, UUID.randomUUID());
        when(menuItemGateway.findById(id)).thenReturn(Optional.of(existing));

        BusinessException ex = assertThrows(BusinessException.class, () -> {
            useCase.execute(id, "Pizza", "desc", BigDecimal.ZERO, false, null);
        });
        assertTrue(ex.getMessage().contains("maior que zero"));
    }

    @Test
    @DisplayName("Deve lançar exceção com preço negativo")
    void shouldThrowWhenPriceIsNegative() {
        UUID id = UUID.randomUUID();
        MenuItem existing = new MenuItem(id, "Pizza", "desc", BigDecimal.TEN, false, null, UUID.randomUUID());
        when(menuItemGateway.findById(id)).thenReturn(Optional.of(existing));

        BusinessException ex = assertThrows(BusinessException.class, () -> {
            useCase.execute(id, "Pizza", "desc", new BigDecimal("-5.00"), false, null);
        });
        assertTrue(ex.getMessage().contains("maior que zero"));
    }

    @Test
    @DisplayName("Deve lançar exceção quando userId não é o dono do restaurante")
    void shouldThrowWhenUserIsNotRestaurantOwner() {
        UUID id = UUID.randomUUID();
        UUID restaurantId = UUID.randomUUID();
        UUID ownerId = UUID.randomUUID();
        UUID otherUser = UUID.randomUUID();
        MenuItem existing = new MenuItem(id, "Pizza", "desc", BigDecimal.TEN, false, null, restaurantId);
        com.fiap.fase2.domain.restaurant.Restaurant restaurant = new com.fiap.fase2.domain.restaurant.Restaurant(
                restaurantId, "Test", "Addr", "ITALIANA",
                java.time.LocalDateTime.now(), java.time.LocalDateTime.now(), ownerId);
        when(menuItemGateway.findById(id)).thenReturn(Optional.of(existing));
        when(restaurantGateway.findById(restaurantId)).thenReturn(Optional.of(restaurant));

        assertThrows(BusinessException.class, () -> {
            useCase.execute(id, "Sushi", "desc", new BigDecimal("45.00"), true, null, otherUser);
        });
        verify(menuItemGateway, never()).update(any());
    }

    @Test
    @DisplayName("Deve atualizar quando userId é o dono")
    void shouldUpdateWhenUserIsOwner() {
        UUID id = UUID.randomUUID();
        UUID restaurantId = UUID.randomUUID();
        UUID ownerId = UUID.randomUUID();
        MenuItem existing = new MenuItem(id, "Pizza", "desc", BigDecimal.TEN, false, null, restaurantId);
        com.fiap.fase2.domain.restaurant.Restaurant restaurant = new com.fiap.fase2.domain.restaurant.Restaurant(
                restaurantId, "Test", "Addr", "ITALIANA",
                java.time.LocalDateTime.now(), java.time.LocalDateTime.now(), ownerId);
        when(menuItemGateway.findById(id)).thenReturn(Optional.of(existing));
        when(restaurantGateway.findById(restaurantId)).thenReturn(Optional.of(restaurant));
        when(menuItemGateway.update(any(MenuItem.class))).thenAnswer(inv -> inv.getArgument(0));

        MenuItem result = useCase.execute(id, "Sushi", "desc", new BigDecimal("45.00"), true, null, ownerId);
        assertEquals("Sushi", result.getName());
    }
}
