package com.fiap.fase2.application.usertype;

import com.fiap.fase2.domain.usertype.UserType;
import com.fiap.fase2.domain.usertype.UserTypeGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FindAllUserTypesUseCaseTest {

    @Mock
    private UserTypeGateway gateway;

    private FindAllUserTypesUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new FindAllUserTypesUseCase(gateway);
    }

    @Test
    @DisplayName("Deve listar todos os tipos de usuário")
    void shouldFindAll() {
        List<UserType> types = List.of(
                new UserType(UUID.randomUUID(), "CUSTOMER"),
                new UserType(UUID.randomUUID(), "RESTAURANT_OWNER")
        );
        when(gateway.findAll()).thenReturn(types);

        List<UserType> result = useCase.execute();

        assertEquals(2, result.size());
        verify(gateway).findAll();
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando não houver tipos")
    void shouldReturnEmptyList() {
        when(gateway.findAll()).thenReturn(List.of());

        List<UserType> result = useCase.execute();

        assertTrue(result.isEmpty());
    }
}
