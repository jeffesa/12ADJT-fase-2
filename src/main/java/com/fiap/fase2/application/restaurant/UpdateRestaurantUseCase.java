package com.fiap.fase2.application.restaurant;

import com.fiap.fase2.domain.restaurant.Restaurant;
import com.fiap.fase2.domain.restaurant.RestaurantGateway;
import com.fiap.fase2.domain.shared.BusinessException;
import com.fiap.fase2.domain.shared.EntityNotFoundException;
import com.fiap.fase2.domain.user.User;
import com.fiap.fase2.domain.user.UserGateway;
import com.fiap.fase2.infra.web.restaurant.dto.RestaurantUpdateRequest;

import java.util.UUID;

public class UpdateRestaurantUseCase {
    private final RestaurantGateway restaurantGateway;
    private final UserGateway userGateway;

    public UpdateRestaurantUseCase(RestaurantGateway restaurantGateway, UserGateway userGateway) {
        this.restaurantGateway = restaurantGateway;
        this.userGateway = userGateway;
    }

    public Restaurant execute(UUID id, RestaurantUpdateRequest request) {
        Restaurant existing = restaurantGateway.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Restaurante não encontrado com id: " + id));

        if (request.ownerId() != null && !request.ownerId().equals(existing.getOwnerId())) {
            User newOwner = userGateway.findById(request.ownerId())
                    .orElseThrow(() -> new BusinessException("Usuário proprietário não encontrado"));

            if (newOwner.getUserType() == null || !"RESTAURANT_OWNER".equals(newOwner.getUserType().getName())) {
                throw new BusinessException("Usuário não tem permissão para ser proprietário (deve ser RESTAURANT_OWNER)");
            }
        }
        
        if (request.name() != null) {
            existing.setName(request.name());
        }
        if (request.address() != null) {
            existing.setAddress(request.address());
        }
        if (request.cuisineType() != null) {
            existing.setCuisineType(request.cuisineType());
        }
        if (request.openingHours() != null) {
            existing.setOpeningHours(request.openingHours());
        }
        if (request.closingTime() != null) {
            existing.setClosingTime(request.closingTime());
        }
        if (request.ownerId() != null) {
            existing.setOwnerId(request.ownerId());
        }

        return restaurantGateway.update(existing);
    }

    // Compatibility overload used by older tests that pass a domain Restaurant as update DTO
    public Restaurant execute(Restaurant restaurant) {
        // Build a RestaurantUpdateRequest from the domain object and delegate to the main method
        RestaurantUpdateRequest request = new RestaurantUpdateRequest(
                restaurant.getName(),
                restaurant.getAddress(),
                restaurant.getCuisineType(),
                restaurant.getOpeningHours(),
                restaurant.getClosingTime(),
                restaurant.getOwnerId()
        );
        return execute(restaurant.getId(), request);
    }
}
