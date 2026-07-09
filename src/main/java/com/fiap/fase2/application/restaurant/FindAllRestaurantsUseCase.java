package com.fiap.fase2.application.restaurant;

import com.fiap.fase2.domain.restaurant.Restaurant;
import com.fiap.fase2.domain.restaurant.RestaurantGateway;

import java.util.List;

public class FindAllRestaurantsUseCase {
    private final RestaurantGateway  restaurantGateway;

    public FindAllRestaurantsUseCase(RestaurantGateway restaurantGateway) {
        this.restaurantGateway = restaurantGateway;
    }

    public List<Restaurant> execute() {
        return restaurantGateway.findAll();
    }
}
