package com.fiap.fase2.application.restaurant;

import com.fiap.fase2.domain.restaurant.RestaurantGateway;
import com.fiap.fase2.domain.shared.EntityNotFoundException;

import java.util.UUID;

public class DeleteRestaurantUseCase {
    private final RestaurantGateway restaurantGateway;

    public DeleteRestaurantUseCase(RestaurantGateway restaurantGateway){
        this.restaurantGateway = restaurantGateway;
    }

    public void execute(UUID id){
        restaurantGateway.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Restaurante não encontrado com id: " + id));

        restaurantGateway.delete(id);
    }
}
