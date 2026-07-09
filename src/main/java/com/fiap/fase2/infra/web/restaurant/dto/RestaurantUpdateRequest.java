package com.fiap.fase2.infra.web.restaurant.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "Dados para atualização de restaurante")
public record RestaurantUpdateRequest(
        @Size(min = 2, max = 100)
        @Schema(description = "Nome do restaurante", example = "Restaurante Saboroso")
        String name,

        @Size(min = 2, max = 100)
        @Schema(description = "Endereço do restaurante", example = "Rua das Flores, 123")
        String address,

        @Schema(description = "Tipo de cozinha", example = "Brasileira")
        @Size(max = 100)
        String cuisineType,

        @Schema(description = "Horário de abertura", example = "08:00")
        LocalDateTime openingHours,

        @Schema(description = "Horário de fechamento", example = "22:00")
        LocalDateTime closingTime,

        @Schema(description = "ID do proprietário", example = "123e4567-e89b-12d3-a456-426614174000")
        UUID ownerId
) {
}
