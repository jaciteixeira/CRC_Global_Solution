package edu.opengroup.crc.repository;

import edu.opengroup.crc.entity.Auth;
import edu.opengroup.crc.entity.Morador;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MoradorRepository extends JpaRepository<Morador, Long> {
    Morador findByAuthUser(Auth resgatado);
}
