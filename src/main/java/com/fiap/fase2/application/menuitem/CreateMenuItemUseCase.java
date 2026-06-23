package com.fiap.fase2.application.menuitem;

import com.fiap.fase2.domain.menuitem.MenuItem;
import com.fiap.fase2.domain.menuitem.MenuItemGateway;
import com.fiap.fase2.domain.restaurant.RestaurantGateway;
import com.fiap.fase2.domain.shared.EntityNotFoundException;

import java.math.BigDecimal;
import java.util.UUID;

public class CreateMenuItemUseCase {

    private final MenuItemGateway menuItemGateway;
    private final RestaurantGateway restaurantGateway;

    public CreateMenuItemUseCase(MenuItemGateway menuItemGateway, RestaurantGateway restaurantGateway) {
        this.menuItemGateway = menuItemGateway;
        this.restaurantGateway = restaurantGateway;
    }

    public MenuItem execute(String name, String description, BigDecimal price,
                            boolean dineInOnly, String photoPath, UUID restaurantId) {
        restaurantGateway.findById(restaurantId)
                .orElseThrow(() -> new EntityNotFoundException("Restaurante não encontrado com id: " + restaurantId));

        MenuItem menuItem = new MenuItem(UUID.randomUUID(), name, description, price, dineInOnly, photoPath, restaurantId);
        return menuItemGateway.create(menuItem);
    }
}
