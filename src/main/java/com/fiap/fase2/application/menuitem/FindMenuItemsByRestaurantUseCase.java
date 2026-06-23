package com.fiap.fase2.application.menuitem;

import com.fiap.fase2.domain.menuitem.MenuItem;
import com.fiap.fase2.domain.menuitem.MenuItemGateway;

import java.util.List;
import java.util.UUID;

public class FindMenuItemsByRestaurantUseCase {

    private final MenuItemGateway menuItemGateway;

    public FindMenuItemsByRestaurantUseCase(MenuItemGateway menuItemGateway) {
        this.menuItemGateway = menuItemGateway;
    }

    public List<MenuItem> execute(UUID restaurantId) {
        return menuItemGateway.findByRestaurantId(restaurantId);
    }
}
