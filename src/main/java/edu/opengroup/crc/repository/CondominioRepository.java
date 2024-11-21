package edu.opengroup.crc.repository;

import edu.opengroup.crc.entity.Condominio;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CondominioRepository extends JpaRepository<Condominio, Long> {
    Condominio findByNome(@NotBlank String nome);
}
