package edu.opengroup.crc.repository;

import edu.opengroup.crc.entity.Auth;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthRepository extends JpaRepository<Auth,Long> {
    Optional<Auth> findByEmail(String email);
}