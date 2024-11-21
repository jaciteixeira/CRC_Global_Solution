package edu.opengroup.crc.repository;

import edu.opengroup.crc.entity.ConsumoMensal;
import edu.opengroup.crc.entity.Morador;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ConsumoMensalRepository extends JpaRepository<ConsumoMensal, Long> {
    List<ConsumoMensal> findByMorador(Morador morador);
    Optional<ConsumoMensal> findTopByMoradorOrderByDataUploadDesc(Morador morador);
}
