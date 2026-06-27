package com.fiap.fase2.infra.persistence.restaurant;

import com.fiap.fase2.domain.restaurant.Restaurant;
import com.fiap.fase2.domain.restaurant.RestaurantGateway;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class RestaurantJpaGateway implements RestaurantGateway {

    private final RestaurantRepository repository;

    public RestaurantJpaGateway(RestaurantRepository repository) {
        this.repository = repository;
    }

    @Override
    public Restaurant create(Restaurant restaurant) {
        return toDomain(repository.save(toJpaEntity(restaurant)));
    }

    @Override
    public Restaurant update(Restaurant restaurant) {
        return toDomain(repository.save(toJpaEntity(restaurant)));
    }

    @Override
    public void delete(UUID id) {
        repository.deleteById(id);
    }

    @Override
    public Optional<Restaurant> findById(UUID id) {
        return repository.findById(id).map(this::toDomain);
    }

    @Override
    public List<Restaurant> findAll() {
        return repository.findAll().stream().map(this::toDomain).toList();
    }

    @Override
    public List<Restaurant> findByOwnerId(UUID ownerId) {
        return repository.findByOwnerId(ownerId).stream().map(this::toDomain).toList();
    }

    private RestaurantJpaEntity toJpaEntity(Restaurant restaurant) {
        return new RestaurantJpaEntity(
                restaurant.getId(), restaurant.getName(), restaurant.getAddress(),
                restaurant.getCuisineType(), restaurant.getOpeningHours(), restaurant.getClosingTime(), restaurant.getOwnerId()
        );
    }

    private Restaurant toDomain(RestaurantJpaEntity entity) {
        return new Restaurant(
                entity.getId(), entity.getName(), entity.getAddress(),
                entity.getCuisineType(), entity.getOpeningHours(), entity.getClosingTime(), entity.getOwnerId()
        );
    }
}
