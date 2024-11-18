package edu.opengroup.crc.entity.dto;

import edu.opengroup.crc.entity.Condominio;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
public record MoradorRequest(
        @NotBlank(message = "Nome é obrigatório!")
        String nome,
        @NotBlank(message = "O email é obrigatório!")
        @Email(message = "Email inválido!")
        String email,
        @NotBlank
        String cpf,
        @NotBlank
        String identificadorRes,
        @Pattern(regexp = "(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*_=+-]).{8,16}", message = "A senha deve seguir o padrão: ")
        @NotEmpty(message = "Senha é obrigatório!")
        String senha,
        @NotBlank
        Condominio condominio
) {
}
