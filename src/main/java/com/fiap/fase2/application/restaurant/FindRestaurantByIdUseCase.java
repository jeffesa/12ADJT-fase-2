package com.fiap.fase2.application.restaurant;

import com.fiap.fase2.domain.restaurant.Restaurant;
import com.fiap.fase2.domain.restaurant.RestaurantGateway;
import com.fiap.fase2.domain.shared.EntityNotFoundException;

import java.util.Optional;
import java.util.UUID;

public class FindRestaurantByIdUseCase {
    private final RestaurantGateway restaurantGateway;

    public FindRestaurantByIdUseCase(RestaurantGateway restaurantGateway) {
        this.restaurantGateway = restaurantGateway;
    }

    public Restaurant execute(UUID id) {

        return restaurantGateway.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Restaurante não encontrado com id: " + id));
    }
}
