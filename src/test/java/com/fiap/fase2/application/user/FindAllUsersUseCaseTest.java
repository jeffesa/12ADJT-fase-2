package com.fiap.fase2.application.user;

import com.fiap.fase2.domain.user.User;
import com.fiap.fase2.domain.user.UserGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FindAllUsersUseCaseTest {

    @Mock private UserGateway userGateway;
    private FindAllUsersUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new FindAllUsersUseCase(userGateway);
    }

    @Test
    @DisplayName("Deve listar todos quando nome for nulo")
    void shouldFindAllWhenNameNull() {
        when(userGateway.findAll()).thenReturn(List.of(new User()));
        List<User> result = useCase.execute(null);
        assertEquals(1, result.size());
        verify(userGateway).findAll();
    }

    @Test
    @DisplayName("Deve listar todos quando nome for vazio")
    void shouldFindAllWhenNameBlank() {
        when(userGateway.findAll()).thenReturn(List.of());
        List<User> result = useCase.execute("   ");
        assertTrue(result.isEmpty());
        verify(userGateway).findAll();
    }

    @Test
    @DisplayName("Deve buscar por nome quando informado")
    void shouldFindByName() {
        when(userGateway.findByNameContaining("João")).thenReturn(List.of(new User()));
        List<User> result = useCase.execute("João");
        assertEquals(1, result.size());
        verify(userGateway).findByNameContaining("João");
    }
}
