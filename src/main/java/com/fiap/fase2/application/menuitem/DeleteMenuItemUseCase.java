package com.fiap.fase2.application.menuitem;

import com.fiap.fase2.domain.menuitem.MenuItem;
import com.fiap.fase2.domain.menuitem.MenuItemGateway;
import com.fiap.fase2.domain.restaurant.Restaurant;
import com.fiap.fase2.domain.restaurant.RestaurantGateway;
import com.fiap.fase2.domain.shared.BusinessException;
import com.fiap.fase2.domain.shared.EntityNotFoundException;

import java.util.UUID;

public class DeleteMenuItemUseCase {

    private final MenuItemGateway menuItemGateway;
    private final RestaurantGateway restaurantGateway;

    public DeleteMenuItemUseCase(MenuItemGateway menuItemGateway, RestaurantGateway restaurantGateway) {
        this.menuItemGateway = menuItemGateway;
        this.restaurantGateway = restaurantGateway;
    }

    public void execute(UUID id, UUID userId) {
        MenuItem menuItem = menuItemGateway.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Item do cardápio não encontrado com id: " + id));

        if (userId != null) {
            Restaurant restaurant = restaurantGateway.findById(menuItem.getRestaurantId())
                    .orElseThrow(() -> new EntityNotFoundException("Restaurante não encontrado"));
            if (!userId.equals(restaurant.getOwnerId())) {
                throw new BusinessException("Somente o proprietário do restaurante pode remover itens do cardápio");
            }
        }

        menuItemGateway.delete(id);
    }

    public void execute(UUID id) {
        execute(id, null);
    }
}
