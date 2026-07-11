package com.fiap.fase2.infra.web.menuitem;

import com.fiap.fase2.application.menuitem.*;
import com.fiap.fase2.domain.menuitem.MenuItem;
import com.fiap.fase2.infra.web.menuitem.dto.MenuItemRequest;
import com.fiap.fase2.infra.web.menuitem.dto.MenuItemResponse;
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
@Tag(name = "Cardápio", description = "CRUD de itens do cardápio")
public class MenuItemController {

    private final CreateMenuItemUseCase createUseCase;
    private final UpdateMenuItemUseCase updateUseCase;
    private final DeleteMenuItemUseCase deleteUseCase;
    private final FindMenuItemByIdUseCase findByIdUseCase;
    private final FindMenuItemsByRestaurantUseCase findByRestaurantUseCase;

    public MenuItemController(CreateMenuItemUseCase createUseCase, UpdateMenuItemUseCase updateUseCase,
                              DeleteMenuItemUseCase deleteUseCase, FindMenuItemByIdUseCase findByIdUseCase,
                              FindMenuItemsByRestaurantUseCase findByRestaurantUseCase) {
        this.createUseCase = createUseCase;
        this.updateUseCase = updateUseCase;
        this.deleteUseCase = deleteUseCase;
        this.findByIdUseCase = findByIdUseCase;
        this.findByRestaurantUseCase = findByRestaurantUseCase;
    }

    @Operation(summary = "Criar item do cardápio",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Item criado"),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos"),
                    @ApiResponse(responseCode = "404", description = "Restaurante não encontrado")
            })
    @PostMapping("/api/v1/restaurants/{restaurantId}/menu-items")
    public ResponseEntity<MenuItemResponse> create(
            @PathVariable UUID restaurantId,
            @RequestHeader(value = "X-User-Id", required = false) UUID userId,
            @Valid @RequestBody MenuItemRequest request) {
        MenuItem created = createUseCase.execute(
                request.name(), request.description(), request.price(),
                request.dineInOnly(), request.photoPath(), restaurantId, userId
        );
        MenuItemResponse response = MenuItemResponse.fromDomain(created);
        URI location = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/v1/menu-items/{id}").buildAndExpand(response.id()).toUri();
        return ResponseEntity.created(location).body(response);
    }

    @Operation(summary = "Listar itens do restaurante",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista retornada")
            })
    @GetMapping("/api/v1/restaurants/{restaurantId}/menu-items")
    public ResponseEntity<List<MenuItemResponse>> findByRestaurant(@PathVariable UUID restaurantId) {
        return ResponseEntity.ok(findByRestaurantUseCase.execute(restaurantId).stream()
                .map(MenuItemResponse::fromDomain).toList());
    }

    @Operation(summary = "Buscar item por ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Item encontrado"),
                    @ApiResponse(responseCode = "404", description = "Item não encontrado")
            })
    @GetMapping("/api/v1/menu-items/{id}")
    public ResponseEntity<MenuItemResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(MenuItemResponse.fromDomain(findByIdUseCase.execute(id)));
    }

    @Operation(summary = "Atualizar item do cardápio",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Item atualizado"),
                    @ApiResponse(responseCode = "404", description = "Item não encontrado"),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos")
            })
    @PutMapping("/api/v1/menu-items/{id}")
    public ResponseEntity<MenuItemResponse> update(
            @PathVariable UUID id,
            @RequestHeader(value = "X-User-Id", required = false) UUID userId,
            @Valid @RequestBody MenuItemRequest request) {
        MenuItem updated = updateUseCase.execute(
                id, request.name(), request.description(), request.price(),
                request.dineInOnly(), request.photoPath(), userId
        );
        return ResponseEntity.ok(MenuItemResponse.fromDomain(updated));
    }

    @Operation(summary = "Remover item do cardápio",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Item removido"),
                    @ApiResponse(responseCode = "404", description = "Item não encontrado")
            })
    @DeleteMapping("/api/v1/menu-items/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable UUID id,
            @RequestHeader(value = "X-User-Id", required = false) UUID userId) {
        deleteUseCase.execute(id, userId);
        return ResponseEntity.noContent().build();
    }
}
