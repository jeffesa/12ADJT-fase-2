package com.fiap.fase2.domain.usertype;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserTypeTest {

    @Test
    @DisplayName("Deve criar UserType com id e nome válidos")
    void shouldCreateUserTypeWithValidData() {
        UUID id = UUID.randomUUID();
        UserType userType = new UserType(id, "CUSTOMER");

        assertEquals(id, userType.getId());
        assertEquals("CUSTOMER", userType.getName());
    }

    @Test
    @DisplayName("Deve criar UserType com construtor padrão")
    void shouldCreateWithDefaultConstructor() {
        UserType userType = new UserType();
        assertNotNull(userType);
        assertNull(userType.getId());
        assertNull(userType.getName());
    }

    @Test
    @DisplayName("Deve alterar nome com setter")
    void shouldSetName() {
        UserType userType = new UserType(UUID.randomUUID(), "CUSTOMER");
        userType.setName("RESTAURANT_OWNER");
        assertEquals("RESTAURANT_OWNER", userType.getName());
    }

    @Test
    @DisplayName("Deve alterar id com setter")
    void shouldSetId() {
        UserType userType = new UserType();
        UUID id = UUID.randomUUID();
        userType.setId(id);
        assertEquals(id, userType.getId());
    }

    @Test
    @DisplayName("Deve lançar exceção ao criar com nome nulo")
    void shouldThrowExceptionWhenNameIsNull() {
        assertThrows(IllegalArgumentException.class, () -> new UserType(UUID.randomUUID(), null));
    }

    @Test
    @DisplayName("Deve lançar exceção ao criar com nome vazio")
    void shouldThrowExceptionWhenNameIsBlank() {
        assertThrows(IllegalArgumentException.class, () -> new UserType(UUID.randomUUID(), "   "));
    }

    @Test
    @DisplayName("Deve lançar exceção ao setar nome nulo")
    void shouldThrowExceptionWhenSetNameNull() {
        UserType userType = new UserType(UUID.randomUUID(), "CUSTOMER");
        assertThrows(IllegalArgumentException.class, () -> userType.setName(null));
    }

    @Test
    @DisplayName("Deve lançar exceção ao setar nome vazio")
    void shouldThrowExceptionWhenSetNameBlank() {
        UserType userType = new UserType(UUID.randomUUID(), "CUSTOMER");
        assertThrows(IllegalArgumentException.class, () -> userType.setName(""));
    }
}
