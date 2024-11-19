package edu.opengroup.crc.entity.dto;

import edu.opengroup.crc.entity.Condominio;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import org.hibernate.validator.constraints.br.CPF;

@Builder
public record MoradorRequestUpdate(
        @NotBlank String nome,
        @NotNull Integer qtdMoradores,
        @Pattern(regexp = "(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*_=+-]).{8,16}", message = "A senha deve seguir o padrão: ")
        @NotBlank String senha
) {
}
