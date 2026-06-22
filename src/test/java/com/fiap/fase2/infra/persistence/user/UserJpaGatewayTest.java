package com.fiap.fase2.infra.persistence.user;

import com.fiap.fase2.domain.user.User;
import com.fiap.fase2.domain.usertype.UserType;
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
@Import(UserJpaGateway.class)
class UserJpaGatewayTest {

    @Autowired
    private UserJpaGateway gateway;

    @Autowired
    private UserTypeRepository userTypeRepository;

    private UserType customerType;

    @BeforeEach
    void setUp() {
        UserTypeJpaEntity typeEntity = userTypeRepository.save(new UserTypeJpaEntity(UUID.randomUUID(), "CUSTOMER"));
        customerType = new UserType(typeEntity.getId(), typeEntity.getName());
    }

    private User buildUser(String name, String email, String login) {
        return new User(UUID.randomUUID(), name, email, login, "hash", "Rua A", LocalDateTime.now(), customerType);
    }

    @Test
    @DisplayName("Deve criar usuário")
    void shouldCreate() {
        User user = buildUser("João", "joao@email.com", "joao");
        User saved = gateway.create(user);
        assertNotNull(saved);
        assertEquals("João", saved.getName());
    }

    @Test
    @DisplayName("Deve buscar por ID")
    void shouldFindById() {
        User user = gateway.create(buildUser("Maria", "maria@email.com", "maria"));
        Optional<User> result = gateway.findById(user.getId());
        assertTrue(result.isPresent());
        assertEquals("Maria", result.get().getName());
    }

    @Test
    @DisplayName("Deve buscar por email")
    void shouldFindByEmail() {
        gateway.create(buildUser("João", "joao@email.com", "joao"));
        assertTrue(gateway.findByEmail("joao@email.com").isPresent());
        assertTrue(gateway.findByEmail("naoexiste@email.com").isEmpty());
    }

    @Test
    @DisplayName("Deve buscar por login")
    void shouldFindByLogin() {
        gateway.create(buildUser("João", "joao@email.com", "joao"));
        assertTrue(gateway.findByLogin("joao").isPresent());
        assertTrue(gateway.findByLogin("naoexiste").isEmpty());
    }

    @Test
    @DisplayName("Deve buscar por nome parcial")
    void shouldFindByNameContaining() {
        gateway.create(buildUser("João Silva", "joao@email.com", "joao"));
        gateway.create(buildUser("Maria Oliveira", "maria@email.com", "maria"));
        List<User> result = gateway.findByNameContaining("João");
        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("Deve deletar")
    void shouldDelete() {
        User user = gateway.create(buildUser("João", "joao@email.com", "joao"));
        gateway.delete(user.getId());
        assertTrue(gateway.findById(user.getId()).isEmpty());
    }
}
