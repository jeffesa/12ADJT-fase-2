package com.fiap.fase2.application.usertype;

import com.fiap.fase2.domain.usertype.UserType;
import com.fiap.fase2.domain.usertype.UserTypeGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateUserTypeUseCaseTest {

    @Mock
    private UserTypeGateway gateway;

    private CreateUserTypeUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new CreateUserTypeUseCase(gateway);
    }

    @Test
    @DisplayName("Deve criar tipo de usuário com sucesso")
    void shouldCreateUserType() {
        when(gateway.create(any(UserType.class))).thenAnswer(inv -> inv.getArgument(0));

        UserType result = useCase.execute("CUSTOMER");

        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals("CUSTOMER", result.getName());
        verify(gateway).create(any(UserType.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao criar com nome vazio")
    void shouldThrowExceptionWhenNameIsBlank() {
        assertThrows(IllegalArgumentException.class, () -> useCase.execute(""));
        verify(gateway, never()).create(any());
    }

    @Test
    @DisplayName("Deve lançar exceção ao criar com nome nulo")
    void shouldThrowExceptionWhenNameIsNull() {
        assertThrows(IllegalArgumentException.class, () -> useCase.execute(null));
        verify(gateway, never()).create(any());
    }
}
