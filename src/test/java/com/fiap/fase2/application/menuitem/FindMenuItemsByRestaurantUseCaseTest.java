package com.fiap.fase2.application.menuitem;

import com.fiap.fase2.domain.menuitem.MenuItem;
import com.fiap.fase2.domain.menuitem.MenuItemGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FindMenuItemsByRestaurantUseCaseTest {

    @Mock private MenuItemGateway menuItemGateway;
    private FindMenuItemsByRestaurantUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new FindMenuItemsByRestaurantUseCase(menuItemGateway);
    }

    @Test
    @DisplayName("Deve listar itens do restaurante")
    void shouldFindByRestaurant() {
        UUID restaurantId = UUID.randomUUID();
        List<MenuItem> items = List.of(
                new MenuItem(UUID.randomUUID(), "Pizza", "desc", BigDecimal.TEN, false, null, restaurantId),
                new MenuItem(UUID.randomUUID(), "Sushi", "desc", new BigDecimal("45"), true, null, restaurantId)
        );
        when(menuItemGateway.findByRestaurantId(restaurantId)).thenReturn(items);

        List<MenuItem> result = useCase.execute(restaurantId);

        assertEquals(2, result.size());
        verify(menuItemGateway).findByRestaurantId(restaurantId);
    }

    @Test
    @DisplayName("Deve retornar lista vazia")
    void shouldReturnEmpty() {
        UUID restaurantId = UUID.randomUUID();
        when(menuItemGateway.findByRestaurantId(restaurantId)).thenReturn(List.of());

        assertTrue(useCase.execute(restaurantId).isEmpty());
    }
}
