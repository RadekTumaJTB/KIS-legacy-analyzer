package cz.jtbank.kis.bff.repository;

import cz.jtbank.kis.bff.entity.DokumentTypEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DokumentTypRepository extends JpaRepository<DokumentTypEntity, Long> {
}
