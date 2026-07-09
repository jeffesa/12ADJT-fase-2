package com.fiap.fase2.application.restaurant;

import com.fiap.fase2.domain.restaurant.Restaurant;
import com.fiap.fase2.domain.restaurant.RestaurantGateway;
import com.fiap.fase2.domain.shared.BusinessException;
import com.fiap.fase2.domain.user.UserGateway;
import com.fiap.fase2.domain.user.User;

import java.time.LocalDateTime;
import java.util.UUID;

public class CreateRestaurantUseCase {
    private final RestaurantGateway restaurantGateway;
    private final UserGateway userGateway;

    public CreateRestaurantUseCase(RestaurantGateway restaurantGateway, UserGateway userGateway) {
        this.restaurantGateway = restaurantGateway;
        this.userGateway = userGateway;
    }

    public Restaurant execute(String name, String address, String cuisineType, LocalDateTime openingHours, LocalDateTime closingTime, UUID ownerId) {
        User owner = userGateway.findById(ownerId)
                .orElseThrow(() -> new BusinessException("Usuário proprietário não encontrado"));

        if (owner.getUserType() == null || !"RESTAURANT_OWNER".equals(owner.getUserType().getName())) {
            throw new BusinessException("Usuário não tem permissão para ser proprietário (deve ser RESTAURANT_OWNER)");
        }

        Restaurant restaurant = new Restaurant(UUID.randomUUID(),
                name,
                address,
                cuisineType,
                openingHours,
                closingTime,
                ownerId
        );
        return restaurantGateway.create(restaurant);
    }
}
