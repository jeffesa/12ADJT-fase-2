package com.fiap.fase2.application.menuitem;

import com.fiap.fase2.domain.menuitem.MenuItem;
import com.fiap.fase2.domain.menuitem.MenuItemGateway;
import com.fiap.fase2.domain.shared.EntityNotFoundException;

import java.util.UUID;

public class FindMenuItemByIdUseCase {

    private final MenuItemGateway menuItemGateway;

    public FindMenuItemByIdUseCase(MenuItemGateway menuItemGateway) {
        this.menuItemGateway = menuItemGateway;
    }

    public MenuItem execute(UUID id) {
        return menuItemGateway.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Item do cardápio não encontrado com id: " + id));
    }
}
