package cz.jtbank.kis.bff.repository;

import cz.jtbank.kis.bff.entity.EquityStakeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Repository for Equity Stake entity (Majetková Účast).
 * Provides CRUD and custom query methods for equity stakes.
 */
@Repository
public interface EquityStakeRepository extends JpaRepository<EquityStakeEntity, Long> {

    /**
     * Find all equity stakes for a specific company (owner)
     */
    List<EquityStakeEntity> findByAccountingCompanyId(Long accountingCompanyId);

    /**
     * Find all equity stakes for a specific emission
     */
    List<EquityStakeEntity> findByEmissionId(Long emissionId);

    /**
     * Find equity stakes valid at a specific date
     */
    @Query("SELECT es FROM EquityStakeEntity es " +
           "WHERE :date BETWEEN es.validFrom AND COALESCE(es.validTo, :date) " +
           "AND es.ignoreFlag = '0'")
    List<EquityStakeEntity> findValidAtDate(@Param("date") LocalDate date);

    /**
     * Find current equity stakes for a company
     */
    @Query("SELECT es FROM EquityStakeEntity es " +
           "WHERE es.accountingCompanyId = :companyId " +
           "AND CURRENT_DATE BETWEEN es.validFrom AND COALESCE(es.validTo, CURRENT_DATE) " +
           "AND es.ignoreFlag = '0'")
    List<EquityStakeEntity> findCurrentByCompanyId(@Param("companyId") Long companyId);

    /**
     * Find equity stakes purchased from a specific company
     */
    List<EquityStakeEntity> findByPurchasedFromCompanyId(Long purchasedFromCompanyId);

    /**
     * Find all stake changes within a date range
     */
    @Query("SELECT es FROM EquityStakeEntity es " +
           "WHERE es.validFrom BETWEEN :fromDate AND :toDate " +
           "AND es.ignoreFlag = '0' " +
           "ORDER BY es.validFrom DESC")
    List<EquityStakeEntity> findChangesBetweenDates(@Param("fromDate") LocalDate fromDate,
                                                      @Param("toDate") LocalDate toDate);

    /**
     * Get historical chain (all versions) for a specific stake
     */
    @Query("SELECT es FROM EquityStakeEntity es " +
           "WHERE es.id = :id OR es.parentId = :id " +
           "ORDER BY es.validFrom DESC")
    List<EquityStakeEntity> findHistoryChain(@Param("id") Long id);

    /**
     * Count equity stakes for an emission (used in views)
     */
    @Query("SELECT COUNT(es) FROM EquityStakeEntity es " +
           "WHERE es.emissionId = :emissionId AND es.ignoreFlag = '0'")
    Long countByEmissionId(@Param("emissionId") Long emissionId);
}
