package com.fiap.fase2.domain.restaurant;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RestaurantGateway {
    Restaurant create(Restaurant restaurant);

    Restaurant update(Restaurant restaurant);

    void delete(UUID id);

    Optional<Restaurant> findById(UUID id);

    List<Restaurant> findAll();

    List<Restaurant> findByOwnerId(UUID ownerId);
}
