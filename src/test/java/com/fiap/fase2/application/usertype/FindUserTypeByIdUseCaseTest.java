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
class FindUserTypeByIdUseCaseTest {

    @Mock
    private UserTypeGateway gateway;

    private FindUserTypeByIdUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new FindUserTypeByIdUseCase(gateway);
    }

    @Test
    @DisplayName("Deve buscar tipo de usuário por ID com sucesso")
    void shouldFindById() {
        UUID id = UUID.randomUUID();
        UserType userType = new UserType(id, "CUSTOMER");
        when(gateway.findById(id)).thenReturn(Optional.of(userType));

        UserType result = useCase.execute(id);

        assertEquals(id, result.getId());
        assertEquals("CUSTOMER", result.getName());
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar ID inexistente")
    void shouldThrowExceptionWhenNotFound() {
        UUID id = UUID.randomUUID();
        when(gateway.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> useCase.execute(id));
    }
}
