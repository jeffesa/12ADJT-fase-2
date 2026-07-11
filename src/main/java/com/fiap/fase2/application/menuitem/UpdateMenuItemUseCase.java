package com.fiap.fase2.application.menuitem;

import com.fiap.fase2.domain.menuitem.MenuItem;
import com.fiap.fase2.domain.menuitem.MenuItemGateway;
import com.fiap.fase2.domain.restaurant.Restaurant;
import com.fiap.fase2.domain.restaurant.RestaurantGateway;
import com.fiap.fase2.domain.shared.BusinessException;
import com.fiap.fase2.domain.shared.EntityNotFoundException;

import java.math.BigDecimal;
import java.util.UUID;

public class UpdateMenuItemUseCase {

    private final MenuItemGateway menuItemGateway;
    private final RestaurantGateway restaurantGateway;

    public UpdateMenuItemUseCase(MenuItemGateway menuItemGateway, RestaurantGateway restaurantGateway) {
        this.menuItemGateway = menuItemGateway;
        this.restaurantGateway = restaurantGateway;
    }

    public MenuItem execute(UUID id, String name, String description, BigDecimal price,
                            boolean dineInOnly, String photoPath, UUID userId) {
        MenuItem existing = menuItemGateway.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Item do cardápio não encontrado com id: " + id));

        if (price != null && price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("O preço deve ser maior que zero");
        }

        if (userId != null) {
            Restaurant restaurant = restaurantGateway.findById(existing.getRestaurantId())
                    .orElseThrow(() -> new EntityNotFoundException("Restaurante não encontrado"));
            if (!userId.equals(restaurant.getOwnerId())) {
                throw new BusinessException("Somente o proprietário do restaurante pode alterar itens do cardápio");
            }
        }

        existing.setName(name);
        existing.setDescription(description);
        existing.setPrice(price);
        existing.setDineInOnly(dineInOnly);
        existing.setPhotoPath(photoPath);

        return menuItemGateway.update(existing);
    }

    public MenuItem execute(UUID id, String name, String description, BigDecimal price,
                            boolean dineInOnly, String photoPath) {
        return execute(id, name, description, price, dineInOnly, photoPath, null);
    }
}
