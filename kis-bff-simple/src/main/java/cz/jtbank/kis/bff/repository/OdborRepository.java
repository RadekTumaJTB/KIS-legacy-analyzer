package cz.jtbank.kis.bff.repository;

import cz.jtbank.kis.bff.entity.OdborEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OdborRepository extends JpaRepository<OdborEntity, Long> {
    
    Optional<OdborEntity> findByKod(String kod);
}
