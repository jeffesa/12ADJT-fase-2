package com.fiap.fase2.application.usertype;

import com.fiap.fase2.domain.shared.EntityNotFoundException;
import com.fiap.fase2.domain.usertype.UserType;
import com.fiap.fase2.domain.usertype.UserTypeGateway;
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
class DeleteUserTypeUseCaseTest {

    @Mock
    private UserTypeGateway gateway;

    private DeleteUserTypeUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new DeleteUserTypeUseCase(gateway);
    }

    @Test
    @DisplayName("Deve deletar tipo de usuário com sucesso")
    void shouldDeleteUserType() {
        UUID id = UUID.randomUUID();
        when(gateway.findById(id)).thenReturn(Optional.of(new UserType(id, "CUSTOMER")));

        useCase.execute(id);

        verify(gateway).delete(id);
    }

    @Test
    @DisplayName("Deve lançar exceção ao deletar ID inexistente")
    void shouldThrowExceptionWhenNotFound() {
        UUID id = UUID.randomUUID();
        when(gateway.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> useCase.execute(id));
        verify(gateway, never()).delete(any());
    }
}
