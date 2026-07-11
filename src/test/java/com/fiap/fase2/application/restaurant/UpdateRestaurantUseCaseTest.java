package com.fiap.fase2.application.restaurant;

import com.fiap.fase2.domain.restaurant.Restaurant;
import com.fiap.fase2.domain.restaurant.RestaurantGateway;
import com.fiap.fase2.domain.shared.BusinessException;
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

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UpdateRestaurantUseCaseTest {

    @Mock
    private RestaurantGateway restaurantGateway;

    @Mock
    private UserGateway userGateway;

    private UpdateRestaurantUseCase useCase;

    private UUID restaurantId;
    private UUID ownerId;
    private Restaurant existingRestaurant;
    private User ownerUser;

    @BeforeEach
    public void setUp() {
        useCase = new UpdateRestaurantUseCase(restaurantGateway, userGateway);
        restaurantId = UUID.randomUUID();
        ownerId = UUID.randomUUID();

        UserType ownerType = new UserType(UUID.randomUUID(), "RESTAURANT_OWNER");
        ownerUser = new User(ownerId, "Owner Name", "owner@example.com", "ownerlogin", "password", "Address", LocalDateTime.now(), ownerType);

        existingRestaurant = new Restaurant(restaurantId, "Pizzaria do João", "Rua A, 123", "ITALIANA",
                LocalDateTime.of(2024, 1, 1, 11, 0), LocalDateTime.of(2024, 1, 1, 23, 0), ownerId);
    }

    @Test
    @DisplayName("Deve atualizar restaurante com sucesso quando todos os dados são válidos e muda proprietário")
    void shouldUpdateRestaurantSuccessfully() {
        // Arrange
        UUID newOwnerId = UUID.randomUUID();
        UserType ownerType = new UserType(UUID.randomUUID(), "RESTAURANT_OWNER");
        User newOwner = new User(newOwnerId, "New Owner", "newowner@example.com", "newownerlogin", "password", "New Address", LocalDateTime.now(), ownerType);

        Restaurant updateData = new Restaurant(restaurantId, "Novo Nome", "Novo Endereço", "FRANCESA",
                LocalDateTime.of(2024, 1, 1, 12, 0), LocalDateTime.of(2024, 1, 2, 0, 0), newOwnerId);

        when(restaurantGateway.findById(restaurantId)).thenReturn(Optional.of(existingRestaurant));
        when(userGateway.findById(newOwnerId)).thenReturn(Optional.of(newOwner));
        when(restaurantGateway.update(any(Restaurant.class))).thenAnswer(inv -> inv.getArgument(0));

        // Act
        Restaurant result = useCase.execute(updateData);

        // Assert
        assertNotNull(result);
        assertEquals("Novo Nome", result.getName());
        assertEquals("Novo Endereço", result.getAddress());
        assertEquals("FRANCESA", result.getCuisineType());
        assertEquals(LocalDateTime.of(2024, 1, 1, 12, 0), result.getOpeningHours());
        assertEquals(LocalDateTime.of(2024, 1, 2, 0, 0), result.getClosingTime());
        verify(restaurantGateway).findById(restaurantId);
        verify(userGateway).findById(newOwnerId);
        verify(restaurantGateway).update(any(Restaurant.class));
    }

    @Test
    @DisplayName("Deve atualizar dados sem validar proprietário quando não está mudando")
    void shouldUpdateWithoutValidatingOwnerWhenNotChanging() {
        // Arrange
        Restaurant updateData = new Restaurant(restaurantId, "Novo Nome", "Rua A, 123", "ITALIANA",
                LocalDateTime.of(2024, 1, 1, 11, 0), LocalDateTime.of(2024, 1, 1, 23, 0), ownerId);

        when(restaurantGateway.findById(restaurantId)).thenReturn(Optional.of(existingRestaurant));
        when(restaurantGateway.update(any(Restaurant.class))).thenAnswer(inv -> inv.getArgument(0));

        // Act
        Restaurant result = useCase.execute(updateData);

        // Assert
        assertNotNull(result);
        assertEquals("Novo Nome", result.getName());
        verify(restaurantGateway).findById(restaurantId);
        verify(userGateway, never()).findById(any()); // Não deve chamar userGateway
        verify(restaurantGateway).update(any(Restaurant.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando restaurante não existe")
    void shouldThrowExceptionWhenRestaurantDoesNotExist() {
        // Arrange
        Restaurant updateData = new Restaurant(restaurantId, "Novo Nome", "Novo Endereço", "FRANCESA",
                LocalDateTime.of(2024, 1, 1, 12, 0), LocalDateTime.of(2024, 1, 2, 0, 0), ownerId);

        when(restaurantGateway.findById(restaurantId)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> useCase.execute(updateData)
        );

        assertEquals("Restaurante não encontrado com id: " + restaurantId, exception.getMessage());
        verify(restaurantGateway).findById(restaurantId);
        verify(userGateway, never()).findById(any());
        verify(restaurantGateway, never()).update(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando o novo proprietário não existe")
    void shouldThrowExceptionWhenOwnerDoesNotExist() {
        // Arrange
        UUID newOwnerId = UUID.randomUUID();
        Restaurant updateData = new Restaurant(restaurantId, "Novo Nome", "Novo Endereço", "FRANCESA",
                LocalDateTime.of(2024, 1, 1, 12, 0), LocalDateTime.of(2024, 1, 2, 0, 0), newOwnerId);

        when(restaurantGateway.findById(restaurantId)).thenReturn(Optional.of(existingRestaurant));
        when(userGateway.findById(newOwnerId)).thenReturn(Optional.empty());

        // Act & Assert
        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> useCase.execute(updateData)
        );

        assertEquals("Usuário proprietário não encontrado", exception.getMessage());
        verify(restaurantGateway).findById(restaurantId);
        verify(userGateway).findById(newOwnerId);
        verify(restaurantGateway, never()).update(any());
    }

    @Test
    @DisplayName("Deve atualizar apenas o nome do restaurante")
    void shouldUpdateOnlyName() {
        // Arrange
        Restaurant updateData = new Restaurant(restaurantId, "Novo Nome Pizzaria", "Rua A, 123", "ITALIANA",
                LocalDateTime.of(2024, 1, 1, 11, 0), LocalDateTime.of(2024, 1, 1, 23, 0), ownerId);

        when(restaurantGateway.findById(restaurantId)).thenReturn(Optional.of(existingRestaurant));
        when(restaurantGateway.update(any(Restaurant.class))).thenAnswer(inv -> inv.getArgument(0));

        // Act
        Restaurant result = useCase.execute(updateData);

        // Assert
        assertEquals("Novo Nome Pizzaria", result.getName());
        assertEquals("Rua A, 123", result.getAddress());
        assertEquals("ITALIANA", result.getCuisineType());
        assertEquals(ownerId, result.getOwnerId());
        verify(restaurantGateway).update(any(Restaurant.class));
        verify(userGateway, never()).findById(any());
    }

    @Test
    @DisplayName("Deve atualizar apenas o endereço do restaurante")
    void shouldUpdateOnlyAddress() {
        // Arrange
        Restaurant updateData = new Restaurant(restaurantId, "Pizzaria do João", "Avenida Paulista, 1000", "ITALIANA",
                LocalDateTime.of(2024, 1, 1, 11, 0), LocalDateTime.of(2024, 1, 1, 23, 0), ownerId);

        when(restaurantGateway.findById(restaurantId)).thenReturn(Optional.of(existingRestaurant));
        when(restaurantGateway.update(any(Restaurant.class))).thenAnswer(inv -> inv.getArgument(0));

        // Act
        Restaurant result = useCase.execute(updateData);

        // Assert
        assertEquals("Pizzaria do João", result.getName());
        assertEquals("Avenida Paulista, 1000", result.getAddress());
        assertEquals("ITALIANA", result.getCuisineType());
        verify(restaurantGateway).update(any(Restaurant.class));
        verify(userGateway, never()).findById(any());
    }

    @Test
    @DisplayName("Deve atualizar apenas os horários de funcionamento")
    void shouldUpdateOnlyBusinessHours() {
        // Arrange
        LocalDateTime newOpeningHours = LocalDateTime.of(2024, 1, 1, 10, 0);
        LocalDateTime newClosingTime = LocalDateTime.of(2024, 1, 2, 1, 0);
        Restaurant updateData = new Restaurant(restaurantId, "Pizzaria do João", "Rua A, 123", "ITALIANA",
                newOpeningHours, newClosingTime, ownerId);

        when(restaurantGateway.findById(restaurantId)).thenReturn(Optional.of(existingRestaurant));
        when(restaurantGateway.update(any(Restaurant.class))).thenAnswer(inv -> inv.getArgument(0));

        // Act
        Restaurant result = useCase.execute(updateData);

        // Assert
        assertEquals("Pizzaria do João", result.getName());
        assertEquals("Rua A, 123", result.getAddress());
        assertEquals(newOpeningHours, result.getOpeningHours());
        assertEquals(newClosingTime, result.getClosingTime());
        verify(restaurantGateway).update(any(Restaurant.class));
        verify(userGateway, never()).findById(any());
    }

    @Test
    @DisplayName("Deve transferir restaurante para novo proprietário com validação")
    void shouldTransferRestaurantToNewOwner() {
        // Arrange
        UUID newOwnerId = UUID.randomUUID();
        UserType ownerType = new UserType(UUID.randomUUID(), "RESTAURANT_OWNER");
        User newOwner = new User(newOwnerId, "New Owner", "newowner@example.com", "newownerlogin", "password", "New Address", LocalDateTime.now(), ownerType);

        Restaurant updateData = new Restaurant(restaurantId, "Pizzaria do João", "Rua A, 123", "ITALIANA",
                LocalDateTime.of(2024, 1, 1, 11, 0), LocalDateTime.of(2024, 1, 1, 23, 0), newOwnerId);

        when(restaurantGateway.findById(restaurantId)).thenReturn(Optional.of(existingRestaurant));
        when(userGateway.findById(newOwnerId)).thenReturn(Optional.of(newOwner));
        when(restaurantGateway.update(any(Restaurant.class))).thenAnswer(inv -> inv.getArgument(0));

        // Act
        Restaurant result = useCase.execute(updateData);

        // Assert
        assertEquals(newOwnerId, result.getOwnerId());
        assertEquals("Pizzaria do João", result.getName());
        verify(restaurantGateway).update(any(Restaurant.class));
        verify(userGateway).findById(newOwnerId); // Deve validar novo proprietário
    }

    @Test
    @DisplayName("Deve lançar exceção quando novo proprietário não existe")
    void shouldThrowExceptionWhenNewOwnerDoesNotExist() {
        // Arrange
        UUID newOwnerId = UUID.randomUUID();
        Restaurant updateData = new Restaurant(restaurantId, "Pizzaria do João", "Rua A, 123", "ITALIANA",
                LocalDateTime.of(2024, 1, 1, 11, 0), LocalDateTime.of(2024, 1, 1, 23, 0), newOwnerId);

        when(restaurantGateway.findById(restaurantId)).thenReturn(Optional.of(existingRestaurant));
        when(userGateway.findById(newOwnerId)).thenReturn(Optional.empty());

        // Act & Assert
        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> useCase.execute(updateData)
        );

        assertEquals("Usuário proprietário não encontrado", exception.getMessage());
        verify(userGateway).findById(newOwnerId);
        verify(restaurantGateway, never()).update(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando novo proprietário não tem permissão RESTAURANT_OWNER")
    void shouldThrowExceptionWhenNewOwnerHasNoPermission() {
        // Arrange
        UUID newOwnerId = UUID.randomUUID();
        UserType customerType = new UserType(UUID.randomUUID(), "CUSTOMER");
        User customerUser = new User(newOwnerId, "Customer", "customer@example.com", "customerlogin", "password", "Address", LocalDateTime.now(), customerType);

        Restaurant updateData = new Restaurant(restaurantId, "Pizzaria do João", "Rua A, 123", "ITALIANA",
                LocalDateTime.of(2024, 1, 1, 11, 0), LocalDateTime.of(2024, 1, 1, 23, 0), newOwnerId);

        when(restaurantGateway.findById(restaurantId)).thenReturn(Optional.of(existingRestaurant));
        when(userGateway.findById(newOwnerId)).thenReturn(Optional.of(customerUser));

        // Act & Assert
        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> useCase.execute(updateData)
        );

        assertEquals("Usuário não tem permissão para ser proprietário (deve ser RESTAURANT_OWNER)", exception.getMessage());
        verify(userGateway).findById(newOwnerId);
        verify(restaurantGateway, never()).update(any());
    }
    @Test
    @DisplayName("validateOwnership deve lançar exceção quando userId não é o proprietário")
    void shouldThrowOnValidateOwnershipWhenNotOwner() {
        UUID otherUser = UUID.randomUUID();
        when(restaurantGateway.findById(restaurantId)).thenReturn(java.util.Optional.of(existingRestaurant));
        assertThrows(com.fiap.fase2.domain.shared.BusinessException.class, () -> useCase.validateOwnership(restaurantId, otherUser));
    }

    @Test
    @DisplayName("validateOwnership deve passar quando userId é o proprietário")
    void shouldPassOnValidateOwnershipWhenOwner() {
        when(restaurantGateway.findById(restaurantId)).thenReturn(java.util.Optional.of(existingRestaurant));
        assertDoesNotThrow(() -> useCase.validateOwnership(restaurantId, ownerId));
    }
}
