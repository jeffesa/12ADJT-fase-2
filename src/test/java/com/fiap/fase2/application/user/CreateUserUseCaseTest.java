package com.fiap.fase2.application.user;

import com.fiap.fase2.domain.shared.BusinessException;
import com.fiap.fase2.domain.shared.EntityNotFoundException;
import com.fiap.fase2.domain.user.User;
import com.fiap.fase2.domain.user.UserGateway;
import com.fiap.fase2.domain.usertype.UserType;
import com.fiap.fase2.domain.usertype.UserTypeGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.fiap.fase2.domain.user.PasswordHasher;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateUserUseCaseTest {

    @Mock private UserGateway userGateway;
    @Mock private UserTypeGateway userTypeGateway;
    @Mock private PasswordHasher passwordHasher;

    private CreateUserUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new CreateUserUseCase(userGateway, userTypeGateway, passwordHasher);
    }

    @Test
    @DisplayName("Deve criar usuário com sucesso")
    void shouldCreateUser() {
        UUID typeId = UUID.randomUUID();
        when(userGateway.findByEmail("joao@email.com")).thenReturn(Optional.empty());
        when(userGateway.findByLogin("joao")).thenReturn(Optional.empty());
        when(userTypeGateway.findById(typeId)).thenReturn(Optional.of(new UserType(typeId, "CUSTOMER")));
        when(passwordHasher.encode("Senha123")).thenReturn("hash");
        when(userGateway.create(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        User result = useCase.execute("João", "joao@email.com", "joao", "Senha123", "Rua A", typeId);

        assertNotNull(result);
        assertEquals("João", result.getName());
        assertEquals("hash", result.getPassword());
        verify(userGateway).create(any(User.class));
    }

    @Test
    @DisplayName("Deve lançar exceção com email duplicado")
    void shouldThrowWhenEmailDuplicate() {
        when(userGateway.findByEmail("joao@email.com")).thenReturn(Optional.of(new User()));

        assertThrows(BusinessException.class, () ->
                useCase.execute("João", "joao@email.com", "joao", "Senha123", "Rua A", UUID.randomUUID()));
    }

    @Test
    @DisplayName("Deve lançar exceção com login duplicado")
    void shouldThrowWhenLoginDuplicate() {
        when(userGateway.findByEmail("joao@email.com")).thenReturn(Optional.empty());
        when(userGateway.findByLogin("joao")).thenReturn(Optional.of(new User()));

        assertThrows(BusinessException.class, () ->
                useCase.execute("João", "joao@email.com", "joao", "Senha123", "Rua A", UUID.randomUUID()));
    }

    @Test
    @DisplayName("Deve lançar exceção com userType inexistente")
    void shouldThrowWhenUserTypeNotFound() {
        UUID typeId = UUID.randomUUID();
        when(userGateway.findByEmail("joao@email.com")).thenReturn(Optional.empty());
        when(userGateway.findByLogin("joao")).thenReturn(Optional.empty());
        when(userTypeGateway.findById(typeId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () ->
                useCase.execute("João", "joao@email.com", "joao", "Senha123", "Rua A", typeId));
    }
}
