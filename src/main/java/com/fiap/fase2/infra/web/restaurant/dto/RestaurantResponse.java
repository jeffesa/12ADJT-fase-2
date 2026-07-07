package com.fiap.fase2.infra.web.restaurant.dto;

import com.fiap.fase2.domain.restaurant.Restaurant;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "Dados de retorno do restaurante")
public record RestaurantResponse(
        UUID id,
        String name,
        String address,
        String cuisineType,
        LocalDateTime openingHours,
        LocalDateTime closingTime,
        UUID ownerId
) {

    public static RestaurantResponse fromDomain(Restaurant restaurant) {
        return new RestaurantResponse(
                restaurant.getId(),
                restaurant.getName(),
                restaurant.getAddress(),
                restaurant.getCuisineType(),
                restaurant.getOpeningHours(),
                restaurant.getClosingTime(),
                restaurant.getOwnerId()
        );
    }
}
