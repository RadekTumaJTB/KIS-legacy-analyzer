package cz.jtbank.kis.bff.repository;

import cz.jtbank.kis.bff.entity.DokumentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DokumentRepository extends JpaRepository<DokumentEntity, Long> {

    @Query("SELECT d FROM DokumentEntity d ORDER BY d.id DESC")
    List<DokumentEntity> findAllOrderByIdDesc();

    List<DokumentEntity> findByIdCisStatus(Long statusId);
}
