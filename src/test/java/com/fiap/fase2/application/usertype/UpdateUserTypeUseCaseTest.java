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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateUserTypeUseCaseTest {

    @Mock
    private UserTypeGateway gateway;

    private UpdateUserTypeUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new UpdateUserTypeUseCase(gateway);
    }

    @Test
    @DisplayName("Deve atualizar tipo de usuário com sucesso")
    void shouldUpdateUserType() {
        UUID id = UUID.randomUUID();
        UserType existing = new UserType(id, "CUSTOMER");
        when(gateway.findById(id)).thenReturn(Optional.of(existing));
        when(gateway.findByName("RESTAURANT_OWNER")).thenReturn(Optional.empty());
        when(gateway.update(any(UserType.class))).thenAnswer(inv -> inv.getArgument(0));

        UserType result = useCase.execute(id, "RESTAURANT_OWNER");

        assertEquals("RESTAURANT_OWNER", result.getName());
        verify(gateway).update(any(UserType.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao atualizar ID inexistente")
    void shouldThrowExceptionWhenNotFound() {
        UUID id = UUID.randomUUID();
        when(gateway.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> useCase.execute(id, "NOVO"));
        verify(gateway, never()).update(any());
    }

    @Test
    @DisplayName("Deve lançar exceção ao atualizar com nome já existente de outro tipo")
    void shouldThrowExceptionWhenNameAlreadyExists() {
        UUID id = UUID.randomUUID();
        UUID otherId = UUID.randomUUID();
        UserType existing = new UserType(id, "CUSTOMER");
        UserType other = new UserType(otherId, "RESTAURANT_OWNER");

        when(gateway.findById(id)).thenReturn(Optional.of(existing));
        when(gateway.findByName("RESTAURANT_OWNER")).thenReturn(Optional.of(other));

        assertThrows(com.fiap.fase2.domain.shared.BusinessException.class, () -> useCase.execute(id, "RESTAURANT_OWNER"));
        verify(gateway, never()).update(any());
    }

    @Test
    @DisplayName("Deve permitir atualizar mantendo o próprio nome")
    void shouldAllowUpdateWithSameName() {
        UUID id = UUID.randomUUID();
        UserType existing = new UserType(id, "CUSTOMER");

        when(gateway.findById(id)).thenReturn(Optional.of(existing));
        when(gateway.findByName("CUSTOMER")).thenReturn(Optional.of(existing));
        when(gateway.update(any(UserType.class))).thenAnswer(inv -> inv.getArgument(0));

        UserType result = useCase.execute(id, "CUSTOMER");

        assertEquals("CUSTOMER", result.getName());
        verify(gateway).update(any());
    }
}
