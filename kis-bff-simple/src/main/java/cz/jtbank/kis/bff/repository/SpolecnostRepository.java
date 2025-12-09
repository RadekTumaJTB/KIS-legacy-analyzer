package cz.jtbank.kis.bff.repository;

import cz.jtbank.kis.bff.entity.SpolecnostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpolecnostRepository extends JpaRepository<SpolecnostEntity, Long> {
}
