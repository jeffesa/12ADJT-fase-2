package com.fiap.fase2.infra.web.usertype;

import com.fiap.fase2.application.usertype.*;
import com.fiap.fase2.domain.usertype.UserType;
import com.fiap.fase2.infra.web.usertype.dto.UserTypeRequest;
import com.fiap.fase2.infra.web.usertype.dto.UserTypeResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/user-types")
@Tag(name = "Tipos de Usuário", description = "Gerenciamento dos tipos de usuário do sistema (ex: CUSTOMER, RESTAURANT_OWNER)")
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

    @Operation(
            summary = "Criar tipo de usuário",
            description = "Cadastra um novo tipo de usuário no sistema. O nome deve ser único — não podem existir dois tipos com o mesmo nome."
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = UserTypeRequest.class),
                    examples = {
                            @ExampleObject(
                                    name = "Cliente",
                                    summary = "Tipo para clientes",
                                    value = """
                                            {"name": "CUSTOMER"}
                                            """
                            ),
                            @ExampleObject(
                                    name = "Dono de Restaurante",
                                    summary = "Tipo para proprietários",
                                    value = """
                                            {"name": "RESTAURANT_OWNER"}
                                            """
                            )
                    }
            )
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Tipo criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos — nome vazio ou fora do limite de caracteres"),
            @ApiResponse(responseCode = "422", description = "Nome já cadastrado no sistema")
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

    @Operation(
            summary = "Listar todos os tipos de usuário",
            description = "Retorna todos os tipos de usuário cadastrados no sistema."
    )
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    @GetMapping
    public ResponseEntity<List<UserTypeResponse>> findAll() {
        List<UserTypeResponse> responses = findAllUseCase.execute().stream()
                .map(UserTypeResponse::fromDomain)
                .toList();
        return ResponseEntity.ok(responses);
    }

    @Operation(
            summary = "Buscar tipo de usuário por ID",
            description = "Retorna os dados de um tipo de usuário específico pelo seu UUID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tipo encontrado"),
            @ApiResponse(responseCode = "404", description = "Tipo não encontrado para o ID informado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<UserTypeResponse> findById(
            @Parameter(description = "ID do tipo de usuário (UUID)", example = "550e8400-e29b-41d4-a716-446655440000", required = true)
            @PathVariable UUID id) {
        UserType userType = findByIdUseCase.execute(id);
        return ResponseEntity.ok(UserTypeResponse.fromDomain(userType));
    }

    @Operation(
            summary = "Atualizar tipo de usuário",
            description = "Atualiza o nome de um tipo de usuário existente. O novo nome deve ser único no sistema."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tipo atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Tipo não encontrado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "422", description = "Nome já cadastrado por outro tipo")
    })
    @PutMapping("/{id}")
    public ResponseEntity<UserTypeResponse> update(
            @Parameter(description = "ID do tipo de usuário (UUID)", required = true)
            @PathVariable UUID id,
            @Valid @RequestBody UserTypeRequest request) {
        UserType updated = updateUseCase.execute(id, request.name());
        return ResponseEntity.ok(UserTypeResponse.fromDomain(updated));
    }

    @Operation(
            summary = "Remover tipo de usuário",
            description = "Remove um tipo de usuário pelo ID. Não é possível remover um tipo que possua usuários vinculados (retorna 409 Conflict)."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Tipo removido com sucesso"),
            @ApiResponse(responseCode = "404", description = "Tipo não encontrado"),
            @ApiResponse(responseCode = "409", description = "Tipo possui usuários vinculados — remova-os primeiro")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID do tipo de usuário (UUID)", required = true)
            @PathVariable UUID id) {
        deleteUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }
}
