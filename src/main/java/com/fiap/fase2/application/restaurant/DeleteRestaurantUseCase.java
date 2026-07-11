package com.fiap.fase2.application.restaurant;

import com.fiap.fase2.domain.restaurant.Restaurant;
import com.fiap.fase2.domain.restaurant.RestaurantGateway;
import com.fiap.fase2.domain.shared.BusinessException;
import com.fiap.fase2.domain.shared.EntityNotFoundException;

import java.util.UUID;

public class DeleteRestaurantUseCase {
    private final RestaurantGateway restaurantGateway;

    public DeleteRestaurantUseCase(RestaurantGateway restaurantGateway) {
        this.restaurantGateway = restaurantGateway;
    }

    public void execute(UUID id, UUID userId) {
        Restaurant restaurant = restaurantGateway.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Restaurante não encontrado com id: " + id));

        if (userId != null && !userId.equals(restaurant.getOwnerId())) {
            throw new BusinessException("Somente o proprietário pode deletar este restaurante");
        }

        restaurantGateway.delete(id);
    }

    public void execute(UUID id) {
        execute(id, null);
    }
}
