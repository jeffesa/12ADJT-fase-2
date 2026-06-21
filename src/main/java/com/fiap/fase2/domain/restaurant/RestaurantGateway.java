package com.fiap.fase2.domain.restaurant;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RestaurantGateway {
    Restaurant create(Restaurant restaurant);

    Restaurant update(Restaurant restaurant);

    void delete(String id);

    Optional<Restaurant> findById(String id);

    List<Restaurant> findAll();

    List<Restaurant> findByOwnerId(UUID ownerId);
}
