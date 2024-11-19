package edu.opengroup.crc.entity.dto;

import edu.opengroup.crc.entity.Condominio;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.br.CPF;

@Builder
public record MoradorRequest(
        @NotBlank String nome,
        @NotBlank @Email String email,
        @NotBlank String identificadorRes,
        @NotNull @CPF String cpf,
        @NotNull Integer qtdMoradores,
        @Pattern(regexp = "(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*_=+-]).{8,16}", message = "A senha deve seguir o padrão: ")
        @NotBlank String senha,
        @NotNull Condominio condominio
) {
}
