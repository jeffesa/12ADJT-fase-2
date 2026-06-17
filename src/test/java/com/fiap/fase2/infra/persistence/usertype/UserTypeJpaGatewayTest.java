package com.fiap.fase2.infra.persistence.usertype;

import com.fiap.fase2.domain.usertype.UserType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@Import(UserTypeJpaGateway.class)
class UserTypeJpaGatewayTest {

    @Autowired
    private UserTypeJpaGateway gateway;

    @Test
    @DisplayName("Deve criar tipo de usuário no banco")
    void shouldCreate() {
        UserType userType = new UserType(UUID.randomUUID(), "CUSTOMER");

        UserType saved = gateway.create(userType);

        assertNotNull(saved);
        assertEquals(userType.getId(), saved.getId());
        assertEquals("CUSTOMER", saved.getName());
    }

    @Test
    @DisplayName("Deve buscar tipo de usuário por ID")
    void shouldFindById() {
        UUID id = UUID.randomUUID();
        gateway.create(new UserType(id, "RESTAURANT_OWNER"));

        Optional<UserType> result = gateway.findById(id);

        assertTrue(result.isPresent());
        assertEquals("RESTAURANT_OWNER", result.get().getName());
    }

    @Test
    @DisplayName("Deve retornar vazio para ID inexistente")
    void shouldReturnEmptyForNonExistentId() {
        Optional<UserType> result = gateway.findById(UUID.randomUUID());
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Deve listar todos os tipos")
    void shouldFindAll() {
        gateway.create(new UserType(UUID.randomUUID(), "CUSTOMER"));
        gateway.create(new UserType(UUID.randomUUID(), "RESTAURANT_OWNER"));

        List<UserType> result = gateway.findAll();

        assertEquals(2, result.size());
    }

    @Test
    @DisplayName("Deve atualizar tipo de usuário")
    void shouldUpdate() {
        UUID id = UUID.randomUUID();
        gateway.create(new UserType(id, "CUSTOMER"));

        UserType updated = gateway.update(new UserType(id, "ADMIN"));

        assertEquals("ADMIN", updated.getName());
    }

    @Test
    @DisplayName("Deve deletar tipo de usuário")
    void shouldDelete() {
        UUID id = UUID.randomUUID();
        gateway.create(new UserType(id, "CUSTOMER"));

        gateway.delete(id);

        assertTrue(gateway.findById(id).isEmpty());
    }
}
