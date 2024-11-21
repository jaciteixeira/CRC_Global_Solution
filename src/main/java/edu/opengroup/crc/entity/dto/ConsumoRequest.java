package edu.opengroup.crc.entity.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record ConsumoRequest(
        @NotNull
        Double quantidadeConsumida,
        @NotNull
        LocalDate dataConsumo
) {
}
