package com.fiap.fase2.infra.web.usertype;

import com.fiap.fase2.application.usertype.*;
import com.fiap.fase2.domain.usertype.UserType;
import com.fiap.fase2.infra.web.usertype.dto.UserTypeRequest;
import com.fiap.fase2.infra.web.usertype.dto.UserTypeResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/user-types")
@Tag(name = "Tipos de Usuário", description = "CRUD de tipos de usuário")
public class UserTypeController {

    private final CreateUserTypeUseCase createUseCase;
    private final UpdateUserTypeUseCase updateUseCase;
    private final DeleteUserTypeUseCase deleteUseCase;
    private final FindUserTypeByIdUseCase findByIdUseCase;
    private final FindAllUserTypesUseCase findAllUseCase;

    public UserTypeController(CreateUserTypeUseCase createUseCase,
                              UpdateUserTypeUseCase updateUseCase,
                              DeleteUserTypeUseCase deleteUseCase,
                              FindUserTypeByIdUseCase findByIdUseCase,
                              FindAllUserTypesUseCase findAllUseCase) {
        this.createUseCase = createUseCase;
        this.updateUseCase = updateUseCase;
        this.deleteUseCase = deleteUseCase;
        this.findByIdUseCase = findByIdUseCase;
        this.findAllUseCase = findAllUseCase;
    }

    @Operation(summary = "Criar tipo de usuário")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Tipo criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    @PostMapping
    public ResponseEntity<UserTypeResponse> create(@Valid @RequestBody UserTypeRequest request) {
        UserType created = createUseCase.execute(request.name());
        UserTypeResponse response = UserTypeResponse.fromDomain(created);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();
        return ResponseEntity.created(location).body(response);
    }

    @Operation(summary = "Listar todos os tipos de usuário")
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    @GetMapping
    public ResponseEntity<List<UserTypeResponse>> findAll() {
        List<UserTypeResponse> responses = findAllUseCase.execute().stream()
                .map(UserTypeResponse::fromDomain)
                .toList();
        return ResponseEntity.ok(responses);
    }

    @Operation(summary = "Buscar tipo de usuário por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tipo encontrado"),
            @ApiResponse(responseCode = "404", description = "Tipo não encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<UserTypeResponse> findById(@PathVariable UUID id) {
        UserType userType = findByIdUseCase.execute(id);
        return ResponseEntity.ok(UserTypeResponse.fromDomain(userType));
    }

    @Operation(summary = "Atualizar tipo de usuário")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tipo atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Tipo não encontrado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    @PutMapping("/{id}")
    public ResponseEntity<UserTypeResponse> update(@PathVariable UUID id, @Valid @RequestBody UserTypeRequest request) {
        UserType updated = updateUseCase.execute(id, request.name());
        return ResponseEntity.ok(UserTypeResponse.fromDomain(updated));
    }

    @Operation(summary = "Remover tipo de usuário")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Tipo removido com sucesso"),
            @ApiResponse(responseCode = "404", description = "Tipo não encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        deleteUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }
}
