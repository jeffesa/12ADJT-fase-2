package com.fiap.fase2.application.restaurant;

import com.fiap.fase2.domain.restaurant.Restaurant;
import com.fiap.fase2.domain.restaurant.RestaurantGateway;
import com.fiap.fase2.domain.shared.BusinessException;
import com.fiap.fase2.domain.shared.EntityNotFoundException;
import com.fiap.fase2.domain.user.User;
import com.fiap.fase2.domain.user.UserGateway;

public class UpdateRestaurantUseCase {
    private final RestaurantGateway restaurantGateway;
    private final UserGateway userGateway;

    public UpdateRestaurantUseCase(RestaurantGateway restaurantGateway, UserGateway userGateway) {
        this.restaurantGateway = restaurantGateway;
        this.userGateway = userGateway;
    }

    public Restaurant execute(Restaurant restaurant) {
        Restaurant existing = restaurantGateway.findById(restaurant.getId())
                .orElseThrow(() -> new EntityNotFoundException("Restaurante não encontrado com id: " + restaurant.getId()));

        if (restaurant.getOwnerId() != null && !restaurant.getOwnerId().equals(existing.getOwnerId())) {
            User newOwner = userGateway.findById(restaurant.getOwnerId())
                    .orElseThrow(() -> new BusinessException("Usuário proprietário não encontrado"));

            if (newOwner.getUserType() == null || !"RESTAURANT_OWNER".equals(newOwner.getUserType().getName())) {
                throw new BusinessException("Usuário não tem permissão para ser proprietário (deve ser RESTAURANT_OWNER)");
            }
        }
        
        if (restaurant.getName() != null) {
            existing.setName(restaurant.getName());
        }
        if (restaurant.getAddress() != null) {
            existing.setAddress(restaurant.getAddress());
        }
        if (restaurant.getCuisineType() != null) {
            existing.setCuisineType(restaurant.getCuisineType());
        }
        if (restaurant.getOpeningHours() != null) {
            existing.setOpeningHours(restaurant.getOpeningHours());
        }
        if (restaurant.getClosingTime() != null) {
            existing.setClosingTime(restaurant.getClosingTime());
        }
        if (restaurant.getOwnerId() != null) {
            existing.setOwnerId(restaurant.getOwnerId());
        }

        return restaurantGateway.update(existing);
    }
}
