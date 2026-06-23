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
class FindMenuItemByIdUseCaseTest {

    @Mock private MenuItemGateway menuItemGateway;
    private FindMenuItemByIdUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new FindMenuItemByIdUseCase(menuItemGateway);
    }

    @Test
    @DisplayName("Deve buscar por ID")
    void shouldFindById() {
        UUID id = UUID.randomUUID();
        MenuItem item = new MenuItem(id, "Pizza", "desc", BigDecimal.TEN, false, null, UUID.randomUUID());
        when(menuItemGateway.findById(id)).thenReturn(Optional.of(item));

        MenuItem result = useCase.execute(id);
        assertEquals("Pizza", result.getName());
    }

    @Test
    @DisplayName("Deve lançar exceção com ID inexistente")
    void shouldThrowWhenNotFound() {
        UUID id = UUID.randomUUID();
        when(menuItemGateway.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> useCase.execute(id));
    }
}
