package com.fiap.fase2.infra.web.user;

import com.fiap.fase2.application.user.*;
import com.fiap.fase2.domain.user.User;
import com.fiap.fase2.infra.web.user.dto.*;
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
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "Usuários", description = "Cadastro, consulta, atualização e exclusão de usuários")
public class UserController {

    private final CreateUserUseCase createUseCase;
    private final UpdateUserUseCase updateUseCase;
    private final DeleteUserUseCase deleteUseCase;
    private final FindUserByIdUseCase findByIdUseCase;
    private final FindAllUsersUseCase findAllUseCase;
    private final ChangePasswordUseCase changePasswordUseCase;
    private final LoginUseCase loginUseCase;

    public UserController(CreateUserUseCase createUseCase, UpdateUserUseCase updateUseCase,
                          DeleteUserUseCase deleteUseCase, FindUserByIdUseCase findByIdUseCase,
                          FindAllUsersUseCase findAllUseCase, ChangePasswordUseCase changePasswordUseCase,
                          LoginUseCase loginUseCase) {
        this.createUseCase = createUseCase;
        this.updateUseCase = updateUseCase;
        this.deleteUseCase = deleteUseCase;
        this.findByIdUseCase = findByIdUseCase;
        this.findAllUseCase = findAllUseCase;
        this.changePasswordUseCase = changePasswordUseCase;
        this.loginUseCase = loginUseCase;
    }

    @Operation(summary = "Criar usuário")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Usuário criado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "422", description = "Email ou login já cadastrado")
    })
    @PostMapping
    public ResponseEntity<UserResponse> create(@Valid @RequestBody UserRequest request) {
        User created = createUseCase.execute(
                request.name(), request.email(), request.login(),
                request.password(), request.address(), request.userTypeId()
        );
        UserResponse response = UserResponse.fromDomain(created);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(response.id()).toUri();
        return ResponseEntity.created(location).body(response);
    }

    @Operation(summary = "Listar usuários ou buscar por nome")
    @ApiResponse(responseCode = "200", description = "Lista retornada")
    @GetMapping
    public ResponseEntity<List<UserResponse>> findAll(@RequestParam(required = false) String name) {
        List<UserResponse> responses = findAllUseCase.execute(name).stream()
                .map(UserResponse::fromDomain).toList();
        return ResponseEntity.ok(responses);
    }

    @Operation(summary = "Buscar usuário por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuário encontrado"),
            @ApiResponse(responseCode = "404", description = "Não encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(UserResponse.fromDomain(findByIdUseCase.execute(id)));
    }

    @Operation(summary = "Atualizar usuário")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Atualizado"),
            @ApiResponse(responseCode = "404", description = "Não encontrado"),
            @ApiResponse(responseCode = "422", description = "Email ou login já cadastrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> update(@PathVariable UUID id, @Valid @RequestBody UserUpdateRequest request) {
        User updated = updateUseCase.execute(
                id, request.name(), request.email(), request.login(), request.address(), request.userTypeId()
        );
        return ResponseEntity.ok(UserResponse.fromDomain(updated));
    }

    @Operation(summary = "Deletar usuário")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Removido"),
            @ApiResponse(responseCode = "404", description = "Não encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        deleteUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Trocar senha", tags = {"Autenticação"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Senha alterada"),
            @ApiResponse(responseCode = "400", description = "Senha atual incorreta"),
            @ApiResponse(responseCode = "404", description = "Não encontrado")
    })
    @PatchMapping("/{id}/password")
    public ResponseEntity<Map<String, String>> changePassword(@PathVariable UUID id,
                                                               @Valid @RequestBody ChangePasswordRequest request) {
        changePasswordUseCase.execute(id, request.currentPassword(), request.newPassword());
        return ResponseEntity.ok(Map.of("mensagem", "Senha alterada com sucesso"));
    }

    @Operation(summary = "Login", tags = {"Autenticação"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login realizado"),
            @ApiResponse(responseCode = "422", description = "Credenciais inválidas")
    })
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        User user = loginUseCase.execute(request.login(), request.password());
        return ResponseEntity.ok(LoginResponse.fromDomain(user));
    }
}
