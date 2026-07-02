package com.fiap.fase2.infra.persistence.restaurant;

import com.fiap.fase2.domain.restaurant.Restaurant;
import com.fiap.fase2.domain.restaurant.RestaurantGateway;
import com.fiap.fase2.infra.persistence.user.UserRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class RestaurantJpaGateway implements RestaurantGateway {

    private final RestaurantRepository repository;
    private final UserRepository userRepository;

    public RestaurantJpaGateway(RestaurantRepository repository, UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
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
        var owner = userRepository.findById(restaurant.getOwnerId())
                .orElseThrow(() -> new IllegalArgumentException("User not found for ID: " + restaurant.getOwnerId()));

        return new RestaurantJpaEntity(
                restaurant.getId(), restaurant.getName(), restaurant.getAddress(),
                restaurant.getCuisineType(), restaurant.getOpeningHours(), restaurant.getClosingTime(), owner
        );
    }

    private Restaurant toDomain(RestaurantJpaEntity entity) {
        return new Restaurant(
                entity.getId(), entity.getName(), entity.getAddress(),
                entity.getCuisineType(), entity.getOpeningHours(), entity.getClosingTime(), entity.getOwner().getId()
        );
    }
}
