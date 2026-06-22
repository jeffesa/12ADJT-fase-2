package com.fiap.fase2.infra.web.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.util.UUID;

@Schema(description = "Dados para atualização de usuário (sem senha)")
public record UserUpdateRequest(
        @NotBlank(message = "O nome é obrigatório")
        @Size(min = 2, max = 100)
        @Schema(description = "Nome completo", example = "João Silva")
        String name,

        @NotBlank(message = "O email é obrigatório")
        @Email(message = "O email deve ser válido")
        @Schema(description = "Email (único)", example = "joao@email.com")
        String email,

        @NotBlank(message = "O login é obrigatório")
        @Size(min = 3, max = 50)
        @Schema(description = "Login (único)", example = "joaosilva")
        String login,

        @NotBlank(message = "O endereço é obrigatório")
        @Size(max = 255)
        @Schema(description = "Endereço", example = "Rua das Flores, 123")
        String address,

        @NotNull(message = "O tipo de usuário é obrigatório")
        @Schema(description = "ID do tipo de usuário")
        UUID userTypeId
) {}
