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

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateUserUseCaseTest {

    @Mock private UserGateway userGateway;
    @Mock private UserTypeGateway userTypeGateway;
    private UpdateUserUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new UpdateUserUseCase(userGateway, userTypeGateway);
    }

    @Test
    @DisplayName("Deve atualizar com sucesso")
    void shouldUpdate() {
        UUID id = UUID.randomUUID();
        UUID typeId = UUID.randomUUID();
        User existing = new User(id, "João", "joao@email.com", "joao",
                "hash", "Rua A", LocalDateTime.now(), new UserType(typeId, "CUSTOMER"));

        when(userGateway.findById(id)).thenReturn(Optional.of(existing));
        when(userGateway.findByEmail("novo@email.com")).thenReturn(Optional.empty());
        when(userGateway.findByLogin("novologin")).thenReturn(Optional.empty());
        when(userTypeGateway.findById(typeId)).thenReturn(Optional.of(new UserType(typeId, "CUSTOMER")));
        when(userGateway.update(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        User result = useCase.execute(id, "Novo Nome", "novo@email.com", "novologin", "Rua B", typeId);

        assertEquals("Novo Nome", result.getName());
        assertEquals("novo@email.com", result.getEmail());
    }

    @Test
    @DisplayName("Deve lançar exceção com email de outro usuário")
    void shouldThrowWhenEmailBelongsToAnother() {
        UUID id = UUID.randomUUID();
        UUID otherId = UUID.randomUUID();
        User existing = new User(id, "João", "joao@email.com", "joao",
                "hash", "Rua A", LocalDateTime.now(), null);
        User other = new User();
        other.setId(otherId);

        when(userGateway.findById(id)).thenReturn(Optional.of(existing));
        when(userGateway.findByEmail("outro@email.com")).thenReturn(Optional.of(other));

        assertThrows(BusinessException.class, () ->
                useCase.execute(id, "Nome", "outro@email.com", "joao", "Rua A", UUID.randomUUID()));
    }

    @Test
    @DisplayName("Deve lançar exceção com ID inexistente")
    void shouldThrowWhenNotFound() {
        UUID id = UUID.randomUUID();
        when(userGateway.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () ->
                useCase.execute(id, "Nome", "e@e.com", "login", "Rua", UUID.randomUUID()));
    }
}
