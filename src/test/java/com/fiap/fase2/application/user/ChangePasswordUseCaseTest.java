package com.fiap.fase2.application.user;

import com.fiap.fase2.domain.shared.EntityNotFoundException;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChangePasswordUseCaseTest {

    @Mock private UserGateway userGateway;
    @Mock private PasswordEncoder passwordEncoder;

    private ChangePasswordUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new ChangePasswordUseCase(userGateway, passwordEncoder);
    }

    @Test
    @DisplayName("Deve trocar senha com sucesso")
    void shouldChangePassword() {
        UUID id = UUID.randomUUID();
        User user = new User(id, "João", "joao@email.com", "joao",
                "oldHash", "Rua A", LocalDateTime.now(), new UserType(UUID.randomUUID(), "CUSTOMER"));
        when(userGateway.findById(id)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("Senha123", "oldHash")).thenReturn(true);
        when(passwordEncoder.encode("NovaSenha456")).thenReturn("newHash");

        useCase.execute(id, "Senha123", "NovaSenha456");

        assertEquals("newHash", user.getPassword());
        verify(userGateway).update(user);
    }

    @Test
    @DisplayName("Deve lançar exceção com senha atual incorreta")
    void shouldThrowWhenCurrentPasswordWrong() {
        UUID id = UUID.randomUUID();
        User user = new User(id, "João", "joao@email.com", "joao",
                "hash", "Rua A", LocalDateTime.now(), new UserType(UUID.randomUUID(), "CUSTOMER"));
        when(userGateway.findById(id)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("errada", "hash")).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> useCase.execute(id, "errada", "Nova123"));
        verify(userGateway, never()).update(any());
    }

    @Test
    @DisplayName("Deve lançar exceção com ID inexistente")
    void shouldThrowWhenUserNotFound() {
        UUID id = UUID.randomUUID();
        when(userGateway.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> useCase.execute(id, "Senha123", "Nova123"));
    }
}
