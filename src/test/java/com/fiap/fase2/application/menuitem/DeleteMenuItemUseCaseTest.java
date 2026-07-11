package com.fiap.fase2.application.menuitem;

import com.fiap.fase2.domain.menuitem.MenuItem;
import com.fiap.fase2.domain.menuitem.MenuItemGateway;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteMenuItemUseCaseTest {

    @Mock private MenuItemGateway menuItemGateway;
    @Mock private com.fiap.fase2.domain.restaurant.RestaurantGateway restaurantGateway;
    private DeleteMenuItemUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new DeleteMenuItemUseCase(menuItemGateway, restaurantGateway);
    }

    @Test
    @DisplayName("Deve deletar com sucesso")
    void shouldDelete() {
        UUID id = UUID.randomUUID();
        when(menuItemGateway.findById(id)).thenReturn(Optional.of(
                new MenuItem(id, "Pizza", "desc", BigDecimal.TEN, false, null, UUID.randomUUID())));

        useCase.execute(id);
        verify(menuItemGateway).delete(id);
    }

    @Test
    @DisplayName("Deve lançar exceção com ID inexistente")
    void shouldThrowWhenNotFound() {
        UUID id = UUID.randomUUID();
        when(menuItemGateway.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> useCase.execute(id));
        verify(menuItemGateway, never()).delete(any());
    }
}
