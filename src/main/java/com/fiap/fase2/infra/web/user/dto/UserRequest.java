package com.fiap.fase2.infra.web.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.util.UUID;

@Schema(description = "Dados para criação de usuário")
public record UserRequest(
        @NotBlank(message = "O nome é obrigatório")
        @Size(min = 2, max = 100, message = "O nome deve ter entre 2 e 100 caracteres")
        @Schema(description = "Nome completo", example = "João Silva")
        String name,

        @NotBlank(message = "O email é obrigatório")
        @Email(message = "O email deve ser válido")
        @Schema(description = "Email (único)", example = "joao@email.com")
        String email,

        @NotBlank(message = "O login é obrigatório")
        @Size(min = 3, max = 50, message = "O login deve ter entre 3 e 50 caracteres")
        @Schema(description = "Login (único)", example = "joaosilva")
        String login,

        @NotBlank(message = "A senha é obrigatória")
        @Size(min = 8, message = "A senha deve ter no mínimo 8 caracteres")
        @Schema(description = "Senha", example = "Senha123")
        String password,

        @NotBlank(message = "O endereço é obrigatório")
        @Size(max = 255, message = "O endereço deve ter no máximo 255 caracteres")
        @Schema(description = "Endereço", example = "Rua das Flores, 123")
        String address,

        @NotNull(message = "O tipo de usuário é obrigatório")
        @Schema(description = "ID do tipo de usuário", example = "550e8400-e29b-41d4-a716-446655440000")
        UUID userTypeId
) {}
