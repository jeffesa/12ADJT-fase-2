package com.fiap.fase2.application.restaurant;

import com.fiap.fase2.domain.restaurant.Restaurant;
import com.fiap.fase2.domain.restaurant.RestaurantGateway;
import com.fiap.fase2.domain.shared.BusinessException;
import com.fiap.fase2.domain.shared.EntityNotFoundException;
import com.fiap.fase2.domain.user.User;
import com.fiap.fase2.domain.user.UserGateway;

import java.time.LocalDateTime;
import java.util.UUID;

public class UpdateRestaurantUseCase {
    private final RestaurantGateway restaurantGateway;
    private final UserGateway userGateway;

    public UpdateRestaurantUseCase(RestaurantGateway restaurantGateway, UserGateway userGateway) {
        this.restaurantGateway = restaurantGateway;
        this.userGateway = userGateway;
    }

    public Restaurant execute(UUID id, String name, String address, String cuisineType,
                              LocalDateTime openingHours, LocalDateTime closingTime, UUID ownerId) {
        return executeWithOwnerCheck(id, name, address, cuisineType, openingHours, closingTime, ownerId, null);
    }

    public Restaurant executeWithOwnerCheck(UUID id, String name, String address, String cuisineType,
                              LocalDateTime openingHours, LocalDateTime closingTime, UUID ownerId, UUID userId) {
        Restaurant existing = restaurantGateway.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Restaurante não encontrado com id: " + id));

        if (userId != null && !userId.equals(existing.getOwnerId())) {
            throw new BusinessException("Somente o proprietário pode alterar este restaurante");
        }

        if (ownerId != null && !ownerId.equals(existing.getOwnerId())) {
            User newOwner = userGateway.findById(ownerId)
                    .orElseThrow(() -> new BusinessException("Usuário proprietário não encontrado"));
            if (newOwner.getUserType() == null || !"RESTAURANT_OWNER".equals(newOwner.getUserType().getName())) {
                throw new BusinessException("Usuário não tem permissão para ser proprietário (deve ser RESTAURANT_OWNER)");
            }
        }

        if (name != null) existing.setName(name);
        if (address != null) existing.setAddress(address);
        if (cuisineType != null) existing.setCuisineType(cuisineType);
        if (openingHours != null) existing.setOpeningHours(openingHours);
        if (closingTime != null) existing.setClosingTime(closingTime);
        if (ownerId != null) existing.setOwnerId(ownerId);

        return restaurantGateway.update(existing);
    }

    public Restaurant execute(Restaurant restaurant) {
        return execute(restaurant.getId(), restaurant.getName(), restaurant.getAddress(),
                restaurant.getCuisineType(), restaurant.getOpeningHours(),
                restaurant.getClosingTime(), restaurant.getOwnerId());
    }
}
