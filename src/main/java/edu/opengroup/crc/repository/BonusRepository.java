package edu.opengroup.crc.repository;

import edu.opengroup.crc.entity.Bonus;
import edu.opengroup.crc.entity.Condominio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BonusRepository extends JpaRepository<Bonus, Long> {
    List<Bonus> findAllByCondominio(Condominio condominio);
}
