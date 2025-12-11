package cz.jtbank.kis.bff.repository;

import cz.jtbank.kis.bff.entity.FinancialInvestmentEmissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Repository for Financial Investment Emission entity.
 * Provides CRUD and custom query methods for emissions.
 */
@Repository
public interface FinancialInvestmentEmissionRepository extends JpaRepository<FinancialInvestmentEmissionEntity, Long> {

    /**
     * Find all emissions for a specific financial investment
     */
    List<FinancialInvestmentEmissionEntity> findByFinancialInvestmentId(Long financialInvestmentId);

    /**
     * Find all emissions valid at a specific date
     */
    @Query("SELECT e FROM FinancialInvestmentEmissionEntity e " +
           "WHERE :date BETWEEN e.validFrom AND COALESCE(e.validTo, :date)")
    List<FinancialInvestmentEmissionEntity> findValidAtDate(@Param("date") LocalDate date);

    /**
     * Find current (active) emissions for a financial investment
     */
    @Query("SELECT e FROM FinancialInvestmentEmissionEntity e " +
           "WHERE e.financialInvestmentId = :finInvId " +
           "AND CURRENT_DATE BETWEEN e.validFrom AND COALESCE(e.validTo, CURRENT_DATE)")
    List<FinancialInvestmentEmissionEntity> findCurrentByFinancialInvestmentId(@Param("finInvId") Long financialInvestmentId);

    /**
     * Find historical emissions (all versions) for tracking changes
     */
    @Query("SELECT e FROM FinancialInvestmentEmissionEntity e " +
           "WHERE e.financialInvestmentId = :finInvId " +
           "ORDER BY e.validFrom DESC")
    List<FinancialInvestmentEmissionEntity> findHistoryByFinancialInvestmentId(@Param("finInvId") Long financialInvestmentId);

    /**
     * Find parent emission (previous version in history chain)
     */
    FinancialInvestmentEmissionEntity findByParentId(Long parentId);
}
