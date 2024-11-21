package edu.opengroup.crc.repository;

import edu.opengroup.crc.entity.Auth;
import edu.opengroup.crc.entity.Morador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MoradorRepository extends JpaRepository<Morador, Long> {
    Morador findByAuthUser(Auth resgatado);
    List<Morador> findByCondominioId(Long id);
    @Query("SELECT COUNT(m) FROM Morador m WHERE m.condominio.id = :condominioId")
    Long countByCondominioId(@Param("condominioId") Long condominioId);
}
