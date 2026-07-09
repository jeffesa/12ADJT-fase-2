package com.fiap.fase2.application.restaurant;

import com.fiap.fase2.domain.restaurant.Restaurant;
import com.fiap.fase2.domain.restaurant.RestaurantGateway;
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

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateRestaurantUseCaseTest {

    @Mock
    private RestaurantGateway restaurantGateway;

    @Mock
    private UserGateway userGateway;

    private CreateRestaurantUseCase useCase;

    private UUID ownerId;
    private User ownerUser;
    private UserType restaurantOwnerType;

    @BeforeEach
    void setUp() {
        useCase = new CreateRestaurantUseCase(restaurantGateway, userGateway);
        ownerId = UUID.randomUUID();
        restaurantOwnerType = new UserType(UUID.randomUUID(), "RESTAURANT_OWNER");
        ownerUser = new User(ownerId, "Owner Name", "owner@example.com", "ownerlogin", "password", "Address", LocalDateTime.now(), restaurantOwnerType);
    }

    @Test
    @DisplayName("Deve criar restaurante com sucesso quando todos os dados são válidos")
    void shouldCreateRestaurantSuccessfully() {
        String name = "Pizzaria do João";
        String address = "Rua A, 123";
        String cuisineType = "ITALIANA";
        LocalDateTime openingHours = LocalDateTime.of(2024, 1, 1, 11, 0);
        LocalDateTime closingTime = LocalDateTime.of(2024, 1, 1, 23, 0);

        when(userGateway.findById(ownerId)).thenReturn(Optional.of(ownerUser));
        when(restaurantGateway.create(any(Restaurant.class))).thenAnswer(inv -> inv.getArgument(0));

        Restaurant result = useCase.execute(name, address, cuisineType, openingHours, closingTime, ownerId);

        assertNotNull(result);
        assertEquals(name, result.getName());
        assertEquals(address, result.getAddress());
        assertEquals(cuisineType, result.getCuisineType());
        assertEquals(openingHours, result.getOpeningHours());
        assertEquals(closingTime, result.getClosingTime());
        assertEquals(ownerId, result.getOwnerId());
        verify(userGateway).findById(ownerId);
        verify(restaurantGateway).create(any(Restaurant.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando owner não existe")
    void shouldThrowExceptionWhenOwnerDoesNotExist() {
        when(userGateway.findById(ownerId)).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> useCase.execute("Pizzaria", "Rua A", "ITALIANA", LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(2), ownerId)
        );

        assertEquals("Usuário proprietário não encontrado", exception.getMessage());
        verify(userGateway).findById(ownerId);
        verify(restaurantGateway, never()).create(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando owner não tem userType")
    void shouldThrowExceptionWhenOwnerHasNoUserType() {
        User ownerWithoutType = new User(ownerId, "Owner Name", "owner@example.com", "ownerlogin", "password", "Address", LocalDateTime.now(), null);
        when(userGateway.findById(ownerId)).thenReturn(Optional.of(ownerWithoutType));

        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> useCase.execute("Pizzaria", "Rua A", "ITALIANA", LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(2), ownerId)
        );

        assertEquals("Usuário não tem permissão para ser proprietário (deve ser RESTAURANT_OWNER)", exception.getMessage());
        verify(userGateway).findById(ownerId);
        verify(restaurantGateway, never()).create(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando owner não é RESTAURANT_OWNER")
    void shouldThrowExceptionWhenOwnerIsNotRestaurantOwner() {
        UserType customerType = new UserType(UUID.randomUUID(), "CUSTOMER");
        User customerUser = new User(ownerId, "Customer Name", "customer@example.com", "customerlogin", "password", "Address", LocalDateTime.now(), customerType);
        when(userGateway.findById(ownerId)).thenReturn(Optional.of(customerUser));

        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> useCase.execute("Pizzaria", "Rua A", "ITALIANA", LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(2), ownerId)
        );

        assertEquals("Usuário não tem permissão para ser proprietário (deve ser RESTAURANT_OWNER)", exception.getMessage());
        verify(userGateway).findById(ownerId);
        verify(restaurantGateway, never()).create(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando nome do restaurante está vazio")
    void shouldThrowExceptionWhenNameIsEmpty() {
        when(userGateway.findById(ownerId)).thenReturn(Optional.of(ownerUser));

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> useCase.execute("", "Rua A", "ITALIANA", LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(2), ownerId)
        );

        assertEquals("Nome do restaurante é obrigatório", exception.getMessage());
        verify(restaurantGateway, never()).create(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando endereço está vazio")
    void shouldThrowExceptionWhenAddressIsEmpty() {
        when(userGateway.findById(ownerId)).thenReturn(Optional.of(ownerUser));

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> useCase.execute("Pizzaria", "", "ITALIANA", LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(2), ownerId)
        );

        assertEquals("Endereço do restaurante é obrigatório", exception.getMessage());
        verify(restaurantGateway, never()).create(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando tipo de culinária está vazio")
    void shouldThrowExceptionWhenCuisineTypeIsEmpty() {
        when(userGateway.findById(ownerId)).thenReturn(Optional.of(ownerUser));

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> useCase.execute("Pizzaria", "Rua A", "", LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(2), ownerId)
        );

        assertEquals("Tipo de cozinha é obrigatório", exception.getMessage());
        verify(restaurantGateway, never()).create(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando horário de fechamento é igual ao de abertura")
    void shouldThrowExceptionWhenClosingTimeEqualsOpeningHours() {
        LocalDateTime sameHours = LocalDateTime.of(2024, 1, 1, 11, 0);
        when(userGateway.findById(ownerId)).thenReturn(Optional.of(ownerUser));

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> useCase.execute("Pizzaria", "Rua A", "ITALIANA", sameHours, sameHours, ownerId)
        );

        assertEquals("Horário de fechamento deve ser após o horário de abertura", exception.getMessage());
        verify(restaurantGateway, never()).create(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando horário de fechamento é anterior ao de abertura")
    void shouldThrowExceptionWhenClosingTimeIsBeforeOpeningHours() {
        LocalDateTime openingHours = LocalDateTime.of(2024, 1, 1, 23, 0);
        LocalDateTime closingTime = LocalDateTime.of(2024, 1, 1, 11, 0);
        when(userGateway.findById(ownerId)).thenReturn(Optional.of(ownerUser));

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> useCase.execute("Pizzaria", "Rua A", "ITALIANA", openingHours, closingTime, ownerId)
        );

        assertEquals("Horário de fechamento deve ser após o horário de abertura", exception.getMessage());
        verify(restaurantGateway, never()).create(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando ownerId é nulo")
    void shouldThrowExceptionWhenOwnerIdIsNull() {
        when(userGateway.findById(null)).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> useCase.execute("Pizzaria", "Rua A", "ITALIANA", LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(2), null)
        );

        assertEquals("Usuário proprietário não encontrado", exception.getMessage());
        verify(restaurantGateway, never()).create(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando horário de funcionamento é nulo")
    void shouldThrowExceptionWhenOpeningHoursIsNull() {
        when(userGateway.findById(ownerId)).thenReturn(Optional.of(ownerUser));

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> useCase.execute("Pizzaria", "Rua A", "ITALIANA", null, LocalDateTime.now().plusHours(2), ownerId)
        );

        assertEquals("Horário de funcionamento é obrigatório", exception.getMessage());
        verify(restaurantGateway, never()).create(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando horário de fechamento é nulo")
    void shouldThrowExceptionWhenClosingTimeIsNull() {
        when(userGateway.findById(ownerId)).thenReturn(Optional.of(ownerUser));

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> useCase.execute("Pizzaria", "Rua A", "ITALIANA", LocalDateTime.now().plusHours(1), null, ownerId)
        );

        assertEquals("Horário de fechamento é obrigatório", exception.getMessage());
        verify(restaurantGateway, never()).create(any());
    }
}