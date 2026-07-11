package com.fiap.fase2.infra.config;

import com.fiap.fase2.application.menuitem.*;
import com.fiap.fase2.domain.menuitem.MenuItemGateway;
import com.fiap.fase2.domain.restaurant.RestaurantGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MenuItemBeanConfig {

    @Bean
    public CreateMenuItemUseCase createMenuItemUseCase(MenuItemGateway menuItemGateway, RestaurantGateway restaurantGateway) {
        return new CreateMenuItemUseCase(menuItemGateway, restaurantGateway);
    }

    @Bean
    public UpdateMenuItemUseCase updateMenuItemUseCase(MenuItemGateway menuItemGateway, RestaurantGateway restaurantGateway) {
        return new UpdateMenuItemUseCase(menuItemGateway, restaurantGateway);
    }

    @Bean
    public DeleteMenuItemUseCase deleteMenuItemUseCase(MenuItemGateway menuItemGateway, RestaurantGateway restaurantGateway) {
        return new DeleteMenuItemUseCase(menuItemGateway, restaurantGateway);
    }

    @Bean
    public FindMenuItemByIdUseCase findMenuItemByIdUseCase(MenuItemGateway menuItemGateway) {
        return new FindMenuItemByIdUseCase(menuItemGateway);
    }

    @Bean
    public FindMenuItemsByRestaurantUseCase findMenuItemsByRestaurantUseCase(MenuItemGateway menuItemGateway) {
        return new FindMenuItemsByRestaurantUseCase(menuItemGateway);
    }
}
