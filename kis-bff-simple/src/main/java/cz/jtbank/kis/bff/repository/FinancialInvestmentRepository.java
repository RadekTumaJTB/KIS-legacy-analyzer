package cz.jtbank.kis.bff.repository;

import cz.jtbank.kis.bff.entity.FinancialInvestmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for Financial Investment entity.
 * Provides CRUD and custom query methods for financial investments.
 */
@Repository
public interface FinancialInvestmentRepository extends JpaRepository<FinancialInvestmentEntity, Long> {

    /**
     * Find all financial investments for a specific company
     */
    List<FinancialInvestmentEntity> findByCompanyId(Long companyId);

    /**
     * Find financial investment by ISIN code
     */
    FinancialInvestmentEntity findByIsinCode(String isinCode);

    /**
     * Find all investments in a specific currency
     */
    List<FinancialInvestmentEntity> findByCurrencyCode(String currencyCode);

    /**
     * Find all investments for a company with specific currency
     */
    List<FinancialInvestmentEntity> findByCompanyIdAndCurrencyCode(Long companyId, String currencyCode);

    /**
     * Custom query to get investments with their latest emissions
     */
    @Query("SELECT DISTINCT fi FROM FinancialInvestmentEntity fi " +
           "LEFT JOIN FETCH fi.company " +
           "WHERE fi.companyId = :companyId")
    List<FinancialInvestmentEntity> findByCompanyIdWithCompanyDetails(@Param("companyId") Long companyId);
}
