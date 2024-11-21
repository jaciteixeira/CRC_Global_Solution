package edu.opengroup.crc.entity.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record BonusRequest(
        @NotBlank String nome,
        @NotBlank String descricao,
        @NotNull Integer custo,
        @NotNull Integer quantidade

) {
}
