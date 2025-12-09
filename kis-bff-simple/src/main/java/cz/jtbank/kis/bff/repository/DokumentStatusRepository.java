package cz.jtbank.kis.bff.repository;

import cz.jtbank.kis.bff.entity.DokumentStatusEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DokumentStatusRepository extends JpaRepository<DokumentStatusEntity, Long> {
}
