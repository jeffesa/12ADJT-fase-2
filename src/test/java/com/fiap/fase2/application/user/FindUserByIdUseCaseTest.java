package com.fiap.fase2.application.user;

import com.fiap.fase2.domain.shared.EntityNotFoundException;
import com.fiap.fase2.domain.user.User;
import com.fiap.fase2.domain.user.UserGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FindUserByIdUseCaseTest {

    @Mock private UserGateway userGateway;
    private FindUserByIdUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new FindUserByIdUseCase(userGateway);
    }

    @Test
    @DisplayName("Deve buscar por ID com sucesso")
    void shouldFindById() {
        UUID id = UUID.randomUUID();
        User user = new User();
        user.setId(id);
        when(userGateway.findById(id)).thenReturn(Optional.of(user));

        User result = useCase.execute(id);
        assertEquals(id, result.getId());
    }

    @Test
    @DisplayName("Deve lançar exceção com ID inexistente")
    void shouldThrowWhenNotFound() {
        UUID id = UUID.randomUUID();
        when(userGateway.findById(id)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> useCase.execute(id));
    }
}
