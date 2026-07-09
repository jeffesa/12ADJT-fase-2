package com.fiap.fase2.application.restaurant;

import com.fiap.fase2.domain.restaurant.Restaurant;
import com.fiap.fase2.domain.restaurant.RestaurantGateway;

import java.util.List;
import java.util.UUID;

public class FindRestaurantsByOwnerUseCase {
    private final RestaurantGateway restaurantGateway;

    public FindRestaurantsByOwnerUseCase(RestaurantGateway restaurantGateway) {
        this.restaurantGateway = restaurantGateway;
    }

    public List<Restaurant> execute(UUID ownerId) {
        return restaurantGateway.findByOwnerId(ownerId);
    }
}
