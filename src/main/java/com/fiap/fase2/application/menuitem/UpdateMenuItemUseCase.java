package com.fiap.fase2.application.menuitem;

import com.fiap.fase2.domain.menuitem.MenuItem;
import com.fiap.fase2.domain.menuitem.MenuItemGateway;
import com.fiap.fase2.domain.shared.BusinessException;
import com.fiap.fase2.domain.shared.EntityNotFoundException;

import java.math.BigDecimal;
import java.util.UUID;

public class UpdateMenuItemUseCase {

    private final MenuItemGateway menuItemGateway;

    public UpdateMenuItemUseCase(MenuItemGateway menuItemGateway) {
        this.menuItemGateway = menuItemGateway;
    }

    public MenuItem execute(UUID id, String name, String description, BigDecimal price,
                            boolean dineInOnly, String photoPath) {
        MenuItem existing = menuItemGateway.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Item do cardápio não encontrado com id: " + id));

        if (price != null && price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("O preço deve ser maior que zero");
        }

        existing.setName(name);
        existing.setDescription(description);
        existing.setPrice(price);
        existing.setDineInOnly(dineInOnly);
        existing.setPhotoPath(photoPath);

        return menuItemGateway.update(existing);
    }
}
