package com.fiap.fase2.domain.user;

import com.fiap.fase2.domain.usertype.UserType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    @DisplayName("Deve criar User com todos os campos")
    void shouldCreateWithAllFields() {
        UUID id = UUID.randomUUID();
        UserType userType = new UserType(UUID.randomUUID(), "CUSTOMER");
        LocalDateTime now = LocalDateTime.now();

        User user = new User(id, "João", "joao@email.com", "joaosilva",
                "hash", "Rua A, 123", now, userType);

        assertEquals(id, user.getId());
        assertEquals("João", user.getName());
        assertEquals("joao@email.com", user.getEmail());
        assertEquals("joaosilva", user.getLogin());
        assertEquals("hash", user.getPassword());
        assertEquals("Rua A, 123", user.getAddress());
        assertEquals(now, user.getLastModifiedDate());
        assertEquals(userType, user.getUserType());
    }

    @Test
    @DisplayName("Deve criar User com construtor padrão")
    void shouldCreateWithDefaultConstructor() {
        User user = new User();
        assertNotNull(user);
        assertNull(user.getId());
    }

    @Test
    @DisplayName("Deve alterar campos via setters")
    void shouldSetFields() {
        User user = new User();
        UUID id = UUID.randomUUID();
        UserType userType = new UserType(UUID.randomUUID(), "RESTAURANT_OWNER");
        LocalDateTime now = LocalDateTime.now();

        user.setId(id);
        user.setName("Maria");
        user.setEmail("maria@email.com");
        user.setLogin("maria");
        user.setPassword("newHash");
        user.setAddress("Rua B, 456");
        user.setLastModifiedDate(now);
        user.setUserType(userType);

        assertEquals(id, user.getId());
        assertEquals("Maria", user.getName());
        assertEquals("maria@email.com", user.getEmail());
        assertEquals("maria", user.getLogin());
        assertEquals("newHash", user.getPassword());
        assertEquals("Rua B, 456", user.getAddress());
        assertEquals(now, user.getLastModifiedDate());
        assertEquals(userType, user.getUserType());
    }
}
