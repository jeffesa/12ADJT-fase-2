package com.fiap.fase2.infra.web.restaurant.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "Dados para criação de restaurante")
public record RestaurantRequest(
        @NotBlank(message = "O nome é obrigatório")
        @Size(min = 2, max = 100, message = "O nome deve ter entre 2 e 100 caracteres")
        @Schema(description = "Nome do restaurante", example = "Restaurante Saboroso")
        String name,

        @Schema(description = "Endereço do restaurante", example = "Av. Paulista, 1000")
        String address,

        @Schema(description = "Tipo de cozinha", example = "Brasileira")
        String cuisineType,

        @Schema(description = "Horário de abertura", example = "08:00")
        LocalDateTime openingHours,

        @Schema(description = "Horário de fechamento", example = "22:00")
        LocalDateTime closingTime,

        @Schema(description = "ID do proprietário", example = "123e4567-e89b-12d3-a456-426614174000")
        UUID ownerId
) {
}
