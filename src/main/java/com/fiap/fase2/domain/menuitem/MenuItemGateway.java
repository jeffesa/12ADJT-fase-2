package com.fiap.fase2.domain.menuitem;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MenuItemGateway {

    MenuItem create(MenuItem menuItem);

    MenuItem update(MenuItem menuItem);

    void delete(UUID id);

    Optional<MenuItem> findById(UUID id);

    List<MenuItem> findByRestaurantId(UUID restaurantId);
}
