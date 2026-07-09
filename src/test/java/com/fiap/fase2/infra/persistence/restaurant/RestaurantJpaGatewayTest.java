package com.fiap.fase2.infra.persistence.restaurant;

import com.fiap.fase2.domain.restaurant.Restaurant;
import com.fiap.fase2.infra.persistence.user.UserJpaEntity;
import com.fiap.fase2.infra.persistence.user.UserRepository;
import com.fiap.fase2.infra.persistence.usertype.UserTypeJpaEntity;
import com.fiap.fase2.infra.persistence.usertype.UserTypeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@Import(RestaurantJpaGateway.class)
class RestaurantJpaGatewayTest {

    @Autowired
    private RestaurantJpaGateway gateway;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserTypeRepository userTypeRepository;

    private UserJpaEntity owner;

    @BeforeEach
    void setUp() {
        // Create a default user type
        UserTypeJpaEntity userType = new UserTypeJpaEntity(UUID.randomUUID(), "ADMIN");
        userTypeRepository.save(userType);

        // Create a default owner user
        owner = new UserJpaEntity(
                UUID.randomUUID(),
                "Owner Name",
                "owner@example.com",
                "ownerlogin",
                "password123",
                "Owner Address",
                LocalDateTime.now(),
                userType
        );
        userRepository.save(owner);
    }

    private Restaurant buildRestaurant(UserJpaEntity owner) {
        return new Restaurant(
                UUID.randomUUID(),
                "Pizzaria",
                "Rua A, 123",
                "ITALIANA",
                LocalDateTime.of(2024, 1, 1, 11, 0),
                LocalDateTime.of(2024, 1, 1, 23, 0),
                owner.getId()
        );
    }

    @Test
    @DisplayName("Deve criar restaurante")
    void shouldCreate() {
        Restaurant restaurant = buildRestaurant(owner);
        Restaurant saved = gateway.create(restaurant);
        assertNotNull(saved);
        assertEquals("Pizzaria", saved.getName());
        assertEquals(owner.getId(), saved.getOwnerId());
    }

    @Test
    @DisplayName("Deve buscar por ID")
    void shouldFindById() {
        Restaurant restaurant = gateway.create(buildRestaurant(owner));
        Optional<Restaurant> result = gateway.findById(restaurant.getId());
        assertTrue(result.isPresent());
    }

    @Test
    @DisplayName("Deve retornar vazio para ID inexistente")
    void shouldReturnEmptyForNonExistentId() {
        assertTrue(gateway.findById(UUID.randomUUID()).isEmpty());
    }

    @Test
    @DisplayName("Deve listar todos")
    void shouldFindAll() {
        gateway.create(buildRestaurant(owner));
        gateway.create(buildRestaurant(owner));
        List<Restaurant> result = gateway.findAll();
        assertEquals(2, result.size());
    }

    @Test
    @DisplayName("Deve buscar por ownerId")
    void shouldFindByOwnerId() {
        // Create another owner
        UserTypeJpaEntity userType = new UserTypeJpaEntity(UUID.randomUUID(), "USER");
        userTypeRepository.save(userType);

        UserJpaEntity otherOwner = new UserJpaEntity(
                UUID.randomUUID(),
                "Other Owner",
                "other@example.com",
                "otherlogin",
                "password123",
                "Other Address",
                LocalDateTime.now(),
                userType
        );
        userRepository.save(otherOwner);

        // Create restaurants for both owners
        gateway.create(buildRestaurant(owner));
        gateway.create(buildRestaurant(owner));
        gateway.create(buildRestaurant(otherOwner));

        // Find by owner ID
        List<Restaurant> result = gateway.findByOwnerId(owner.getId());
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(r -> r.getOwnerId().equals(owner.getId())));
    }

    @Test
    @DisplayName("Deve deletar")
    void shouldDelete() {
        Restaurant restaurant = gateway.create(buildRestaurant(owner));
        gateway.delete(restaurant.getId());
        assertTrue(gateway.findById(restaurant.getId()).isEmpty());
    }
}
