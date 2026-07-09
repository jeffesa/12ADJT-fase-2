package com.fiap.fase2.infra.web.restaurant;

import com.fiap.fase2.application.restaurant.*;
import com.fiap.fase2.domain.restaurant.Restaurant;
import com.fiap.fase2.infra.web.restaurant.dto.RestaurantRequest;
import com.fiap.fase2.infra.web.restaurant.dto.RestaurantResponse;
import com.fiap.fase2.infra.web.restaurant.dto.RestaurantUpdateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/restaurants")
@Tag(name = "Restaurantes", description = "Cadastro, consulta, atualização e exclusão de restaurantes")
public class RestaurantController {

    private final CreateRestaurantUseCase createRestaurantUseCase;
    private final DeleteRestaurantUseCase deleteRestaurantUseCase;
    private final FindAllRestaurantsUseCase findAllRestaurantsUseCase;
    private final FindRestaurantByIdUseCase findRestaurantByIdUseCase;
    private final FindRestaurantsByOwnerUseCase findRestaurantsByOwnerUseCase;
    private final UpdateRestaurantUseCase updateRestaurantUseCase;

    public RestaurantController(CreateRestaurantUseCase createRestaurantUseCase,
                                DeleteRestaurantUseCase deleteRestaurantUseCase,
                                FindAllRestaurantsUseCase findAllRestaurantsUseCase,
                                FindRestaurantByIdUseCase findRestaurantByIdUseCase,
                                FindRestaurantsByOwnerUseCase findRestaurantsByOwnerUseCase,
                                UpdateRestaurantUseCase updateRestaurantUseCase) {
        this.createRestaurantUseCase = createRestaurantUseCase;
        this.deleteRestaurantUseCase = deleteRestaurantUseCase;
        this.findAllRestaurantsUseCase = findAllRestaurantsUseCase;
        this.findRestaurantByIdUseCase = findRestaurantByIdUseCase;
        this.findRestaurantsByOwnerUseCase = findRestaurantsByOwnerUseCase;
        this.updateRestaurantUseCase = updateRestaurantUseCase;
    }

    @Operation(summary = "Criar restaurante",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Restaurante criado"),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos"),
                    @ApiResponse(responseCode = "422", description = "Erro de validação")
            })
    @PostMapping
    public ResponseEntity<RestaurantResponse> createRestaurant(@Valid @RequestBody RestaurantRequest request) {
        Restaurant created = createRestaurantUseCase.execute(
                request.name(), request.address(), request.cuisineType(),
                request.openingHours(), request.closingTime(), request.ownerId()
        );
        RestaurantResponse response = RestaurantResponse.fromDomain(created);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(response.id()).toUri();
        return ResponseEntity.status(201).headers(h -> h.setLocation(location)).body(response);
    }

    @Operation(summary = "Deletar restaurante",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Restaurante deletado"),
                    @ApiResponse(responseCode = "404", description = "Restaurante não encontrado")
            })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRestaurant(@PathVariable UUID id) {
        deleteRestaurantUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Listar todos os restaurantes",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de restaurantes retornada")
            })
    @GetMapping
    public ResponseEntity<List<RestaurantResponse>> findAllRestaurants(
            @RequestParam(required = false) UUID ownerId) {
        List<Restaurant> restaurants = ownerId != null
                ? findRestaurantsByOwnerUseCase.execute(ownerId)
                : findAllRestaurantsUseCase.execute();
        return ResponseEntity.ok(restaurants.stream().map(RestaurantResponse::fromDomain).toList());
    }

    @Operation(summary = "Buscar restaurante por ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Restaurante encontrado"),
                    @ApiResponse(responseCode = "404", description = "Restaurante não encontrado")
            })
    @GetMapping("/{id}")
    public ResponseEntity<RestaurantResponse> findRestaurantById(@PathVariable UUID id) {
        return ResponseEntity.ok(RestaurantResponse.fromDomain(findRestaurantByIdUseCase.execute(id)));
    }

    @Operation(summary = "Atualizar restaurante",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Restaurante atualizado"),
                    @ApiResponse(responseCode = "404", description = "Restaurante não encontrado"),
                    @ApiResponse(responseCode = "422", description = "Erro de validação")
            })
    @PutMapping("/{id}")
    public ResponseEntity<RestaurantResponse> updateRestaurant(@PathVariable UUID id,
                                                               @Valid @RequestBody RestaurantUpdateRequest request) {
        Restaurant updated = updateRestaurantUseCase.execute(id, request.name(), request.address(),
                request.cuisineType(), request.openingHours(), request.closingTime(), request.ownerId());
        return ResponseEntity.ok(RestaurantResponse.fromDomain(updated));
    }
}
