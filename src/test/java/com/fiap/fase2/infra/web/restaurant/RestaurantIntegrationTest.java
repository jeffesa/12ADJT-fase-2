package com.fiap.fase2.infra.web.restaurant;

import com.fiap.fase2.application.restaurant.CreateRestaurantUseCase;
import com.fiap.fase2.application.restaurant.FindRestaurantByIdUseCase;
import com.fiap.fase2.domain.restaurant.Restaurant;
import com.fiap.fase2.domain.shared.EntityNotFoundException;
import com.fiap.fase2.infra.web.restaurant.dto.RestaurantRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.boot.test.web.client.TestRestTemplate;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RestaurantIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CreateRestaurantUseCase createRestaurantUseCase;

    @MockBean
    private FindRestaurantByIdUseCase findRestaurantByIdUseCase;

    @Test
    void createRestaurant_integration_shouldReturn201AndLocation() throws Exception {
        UUID ownerId = UUID.randomUUID();
        RestaurantRequest request = new RestaurantRequest("Nome", "Addr", "ITALIANA",
                LocalDateTime.of(2024,1,1,11,0), LocalDateTime.of(2024,1,1,23,0), ownerId);

        Restaurant created = new Restaurant(UUID.randomUUID(), request.name(), request.address(), request.cuisineType(),
                request.openingHours(), request.closingTime(), ownerId);

        Mockito.when(createRestaurantUseCase.execute(
                Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(created);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(objectMapper.writeValueAsString(request), headers);

        ResponseEntity<String> response = restTemplate.postForEntity("/api/v1/restaurants", entity, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getHeaders().getLocation()).isNotNull();
    }

    @Test
    void findRestaurantById_integration_shouldReturn404WhenNotFound() {
        UUID id = UUID.randomUUID();
        Mockito.when(findRestaurantByIdUseCase.execute(id)).thenThrow(new EntityNotFoundException("Restaurante não encontrado com id: " + id));

        ResponseEntity<String> response = restTemplate.getForEntity("/api/v1/restaurants/" + id, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
