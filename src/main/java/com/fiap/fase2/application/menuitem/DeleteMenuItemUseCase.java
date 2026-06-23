package com.fiap.fase2.application.menuitem;

import com.fiap.fase2.domain.menuitem.MenuItemGateway;
import com.fiap.fase2.domain.shared.EntityNotFoundException;

import java.util.UUID;

public class DeleteMenuItemUseCase {

    private final MenuItemGateway menuItemGateway;

    public DeleteMenuItemUseCase(MenuItemGateway menuItemGateway) {
        this.menuItemGateway = menuItemGateway;
    }

    public void execute(UUID id) {
        menuItemGateway.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Item do cardápio não encontrado com id: " + id));

        menuItemGateway.delete(id);
    }
}
