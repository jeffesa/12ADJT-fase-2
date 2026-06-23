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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateMenuItemUseCaseTest {

    @Mock private MenuItemGateway menuItemGateway;
    private UpdateMenuItemUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new UpdateMenuItemUseCase(menuItemGateway);
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
}
