package edu.opengroup.crc.entity.dto;

import jakarta.validation.constraints.*;
import lombok.Builder;

@Builder
public record CondominioRequest(
        @NotBlank
        String nome,
        @NotBlank
        String logradouro,
        @NotBlank
        String numero,
        @NotBlank
        String bairro,
        @NotBlank
        String cidade,
        @NotBlank
        String uf,
        @NotBlank
        String cep

) {
}
