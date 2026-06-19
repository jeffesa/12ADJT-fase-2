package com.fiap.fase2.application.user;

import com.fiap.fase2.domain.shared.BusinessException;
import com.fiap.fase2.domain.user.User;
import com.fiap.fase2.domain.user.UserGateway;
import com.fiap.fase2.domain.usertype.UserType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoginUseCaseTest {

    @Mock private UserGateway userGateway;
    @Mock private PasswordEncoder passwordEncoder;

    private LoginUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new LoginUseCase(userGateway, passwordEncoder);
    }

    @Test
    @DisplayName("Deve realizar login com sucesso")
    void shouldLogin() {
        User user = new User(UUID.randomUUID(), "João", "joao@email.com", "joao",
                "hash", "Rua A", LocalDateTime.now(), new UserType(UUID.randomUUID(), "CUSTOMER"));
        when(userGateway.findByLogin("joao")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("Senha123", "hash")).thenReturn(true);

        User result = useCase.execute("joao", "Senha123");

        assertEquals("joao", result.getLogin());
    }

    @Test
    @DisplayName("Deve lançar exceção com login inexistente")
    void shouldThrowWhenLoginNotFound() {
        when(userGateway.findByLogin("naoexiste")).thenReturn(Optional.empty());

        assertThrows(BusinessException.class, () -> useCase.execute("naoexiste", "Senha123"));
    }

    @Test
    @DisplayName("Deve lançar exceção com senha incorreta")
    void shouldThrowWhenPasswordWrong() {
        User user = new User(UUID.randomUUID(), "João", "joao@email.com", "joao",
                "hash", "Rua A", LocalDateTime.now(), new UserType(UUID.randomUUID(), "CUSTOMER"));
        when(userGateway.findByLogin("joao")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("errada", "hash")).thenReturn(false);

        assertThrows(BusinessException.class, () -> useCase.execute("joao", "errada"));
    }
}
