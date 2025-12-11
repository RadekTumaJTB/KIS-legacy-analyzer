package cz.jtbank.kis.bff.service;

import cz.jtbank.kis.bff.dto.asset.*;
import cz.jtbank.kis.bff.entity.*;
import cz.jtbank.kis.bff.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Asset Aggregation Service
 *
 * Handles Equity Stakes (Majetkové Účasti) and Asset Overview.
 * Provides role-based filtering, dual currency handling, and ownership calculations.
 *
 * IMPORTANT: This service will eventually call Oracle PL/SQL procedures
 * from packages KAP_MAJETEK and KAP_FININV. Current implementation uses JPA for development.
 */
@Service
public class AssetAggregationService {

    private static final Logger logger = Logger.getLogger(AssetAggregationService.class.getName());

    private final EquityStakeRepository equityStakeRepository;
    private final FinancialInvestmentEmissionRepository emissionRepository;
    private final FinancialInvestmentRepository financialInvestmentRepository;
    private final SpolecnostRepository spolecnostRepository;
    private final EquityStakeTransactionTypeRepository transactionTypeRepository;
    private final EquityStakeMethodRepository methodRepository;

    public AssetAggregationService(
            EquityStakeRepository equityStakeRepository,
            FinancialInvestmentEmissionRepository emissionRepository,
            FinancialInvestmentRepository financialInvestmentRepository,
            SpolecnostRepository spolecnostRepository,
            EquityStakeTransactionTypeRepository transactionTypeRepository,
            EquityStakeMethodRepository methodRepository) {
        this.equityStakeRepository = equityStakeRepository;
        this.emissionRepository = emissionRepository;
        this.financialInvestmentRepository = financialInvestmentRepository;
        this.spolecnostRepository = spolecnostRepository;
        this.transactionTypeRepository = transactionTypeRepository;
        this.methodRepository = methodRepository;
    }

    /**
     * Get companies with role-based filtering
     *
     * TODO: Implement role-based filtering using Spring Security context
     * Roles: Admin_MU, MU_jednotlive, MU_konsolidovane, MU_view_only
     *
     * For now, returns all companies
     */
    public List<CompanyWithPermissionsDTO> getCompaniesWithRoleFiltering() {
        logger.info("Fetching companies with role-based filtering");

        List<SpolecnostEntity> companies = spolecnostRepository.findAll();
        List<CompanyWithPermissionsDTO> result = new ArrayList<>();

        for (SpolecnostEntity company : companies) {
            // TODO: Add permission check here based on user role
            // For now, all companies are visible
            CompanyWithPermissionsDTO dto = new CompanyWithPermissionsDTO(
                    company.getId(),
                    company.getNazev(),
                    company.getIco(),
                    true,  // canView
                    true   // canEdit
            );
            result.add(dto);
        }

        logger.info("Found " + result.size() + " companies");
        return result;
    }

    /**
     * Get equity stakes for a specific company
     */
    public List<EquityStakeDTO> getEquityStakesForCompany(Long companyId) {
        logger.info("Fetching equity stakes for company ID: " + companyId);

        List<EquityStakeEntity> stakes = equityStakeRepository.findByAccountingCompanyId(companyId);
        List<EquityStakeDTO> result = new ArrayList<>();

        for (EquityStakeEntity stake : stakes) {
            result.add(convertToDTO(stake));
        }

        logger.info("Found " + result.size() + " equity stakes");
        return result;
    }

    /**
     * Get single equity stake by ID
     */
    public EquityStakeDTO getEquityStakeById(Long id) {
        logger.info("Fetching equity stake ID: " + id);

        EquityStakeEntity stake = equityStakeRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Equity stake not found: " + id));

        return convertToDTO(stake);
    }

    /**
     * Create new equity stake
     *
     * TODO: Replace with Oracle procedure call: KAP_MAJETEK.majetkovaUcast()
     */
    @Transactional
    public EquityStakeDTO createEquityStake(EquityStakeDTO dto) {
        logger.info("Creating new equity stake for company ID: " + dto.getAccountingCompanyId());

        EquityStakeEntity entity = EquityStakeEntity.builder()
                .accountingCompanyId(dto.getAccountingCompanyId())
                .emissionId(dto.getEmissionId())
                .accountNumber(dto.getAccountNumber())
                .validFrom(dto.getValidFrom())
                .validTo(dto.getValidTo())
                .transactionTypeId(dto.getTransactionTypeId())
                .methodId(dto.getMethodId())
                .numberOfShares(dto.getNumberOfShares())
                .transactionCurrency(dto.getTransactionCurrency())
                .pricePerShareTransaction(dto.getPricePerShareTransaction())
                .totalTransactionAmount(dto.getTotalTransactionAmount())
                .exchangeRate(dto.getExchangeRate())
                .accountingCurrency(dto.getAccountingCurrency())
                .pricePerShareAccounting(dto.getPricePerShareAccounting())
                .totalAccountingAmount(dto.getTotalAccountingAmount())
                .purchasedFromCompanyId(dto.getPurchasedFromCompanyId())
                .ignoreFlag(dto.getIgnoreFlag() != null && dto.getIgnoreFlag() ? "1" : "0")
                .modifiedByUser("SYSTEM")  // TODO: Get from security context
                .build();

        EquityStakeEntity saved = equityStakeRepository.save(entity);
        return convertToDTO(saved);
    }

    /**
     * Update existing equity stake
     */
    @Transactional
    public EquityStakeDTO updateEquityStake(Long id, EquityStakeDTO dto) {
        logger.info("Updating equity stake ID: " + id);

        EquityStakeEntity existing = equityStakeRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Equity stake not found: " + id));

        // Update fields
        existing.setAccountNumber(dto.getAccountNumber());
        existing.setValidFrom(dto.getValidFrom());
        existing.setValidTo(dto.getValidTo());
        existing.setTransactionTypeId(dto.getTransactionTypeId());
        existing.setMethodId(dto.getMethodId());
        existing.setNumberOfShares(dto.getNumberOfShares());
        existing.setTransactionCurrency(dto.getTransactionCurrency());
        existing.setPricePerShareTransaction(dto.getPricePerShareTransaction());
        existing.setTotalTransactionAmount(dto.getTotalTransactionAmount());
        existing.setExchangeRate(dto.getExchangeRate());
        existing.setAccountingCurrency(dto.getAccountingCurrency());
        existing.setPricePerShareAccounting(dto.getPricePerShareAccounting());
        existing.setTotalAccountingAmount(dto.getTotalAccountingAmount());
        existing.setPurchasedFromCompanyId(dto.getPurchasedFromCompanyId());
        existing.setIgnoreFlag(dto.getIgnoreFlag() != null && dto.getIgnoreFlag() ? "1" : "0");
        existing.setLastModified(LocalDate.now());
        existing.setModifiedByUser("SYSTEM");  // TODO: Get from security context

        EquityStakeEntity saved = equityStakeRepository.save(existing);
        return convertToDTO(saved);
    }

    /**
     * Delete equity stake
     */
    @Transactional
    public void deleteEquityStake(Long id) {
        logger.info("Deleting equity stake ID: " + id);

        if (!equityStakeRepository.existsById(id)) {
            throw new EntityNotFoundException("Equity stake not found: " + id);
        }

        equityStakeRepository.deleteById(id);
        logger.info("Equity stake deleted successfully");
    }

    /**
     * Get asset overview with calculations
     *
     * TODO: Use Oracle functions:
     * - KAP_MAJETEK.getPodilInvestice() for ownership percentage
     * - KAP_FININV.getEmiseMap() for emission volume
     */
    public List<AssetOverviewDTO> getAssetOverview(LocalDate asOfDate, Long companyId) {
        logger.info("Generating asset overview as of: " + asOfDate + " for company: " + companyId);

        LocalDate snapshotDate = asOfDate != null ? asOfDate : LocalDate.now();

        // Get all equity stakes for the company valid at the snapshot date
        List<EquityStakeEntity> stakes;
        if (companyId != null) {
            stakes = equityStakeRepository.findByAccountingCompanyId(companyId).stream()
                    .filter(stake -> isValidAtDate(stake, snapshotDate))
                    .toList();
        } else {
            stakes = equityStakeRepository.findAll().stream()
                    .filter(stake -> isValidAtDate(stake, snapshotDate))
                    .toList();
        }

        // Group by emission and calculate totals
        Map<Long, AssetOverviewDTO> overviewMap = new HashMap<>();

        for (EquityStakeEntity stake : stakes) {
            Long emissionId = stake.getEmissionId();

            if (!overviewMap.containsKey(emissionId)) {
                // Get emission details
                FinancialInvestmentEmissionEntity emission = emissionRepository
                        .findById(emissionId)
                        .orElse(null);

                if (emission == null) {
                    logger.warning("Emission not found for stake: " + stake.getId());
                    continue;
                }

                // Get financial investment details
                FinancialInvestmentEntity investment = financialInvestmentRepository
                        .findById(emission.getFinancialInvestmentId())
                        .orElse(null);

                if (investment == null) {
                    logger.warning("Financial investment not found for emission: " + emissionId);
                    continue;
                }

                // Get company name
                String companyName = "N/A";
                try {
                    SpolecnostEntity company = investment.getCompany();
                    if (company != null) {
                        companyName = company.getNazev();
                    }
                } catch (Exception e) {
                    logger.warning("Could not fetch company for investment: " + investment.getId());
                }

                // Initialize overview DTO
                AssetOverviewDTO overview = AssetOverviewDTO.builder()
                        .emissionId(emissionId)
                        .companyName(companyName)
                        .isinCode(investment.getIsinCode())
                        .totalEmissionShares(emission.getNumberOfShares() != null ? BigDecimal.valueOf(emission.getNumberOfShares()) : BigDecimal.ZERO)
                        .sharesOwned(BigDecimal.ZERO)
                        .currency(investment.getCurrencyCode())
                        .build();

                overviewMap.put(emissionId, overview);
            }

            // Add shares to total
            AssetOverviewDTO overview = overviewMap.get(emissionId);
            BigDecimal currentShares = overview.getSharesOwned() != null ? overview.getSharesOwned() : BigDecimal.ZERO;
            overview.setSharesOwned(currentShares.add(stake.getNumberOfShares()));

            // Add book value (from accounting amount)
            BigDecimal currentBookValue = overview.getBookValue() != null ? overview.getBookValue() : BigDecimal.ZERO;
            BigDecimal stakeBookValue = stake.getTotalAccountingAmount() != null ? stake.getTotalAccountingAmount() : BigDecimal.ZERO;
            overview.setBookValue(currentBookValue.add(stakeBookValue));
        }

        // Calculate ownership percentages and set market values (placeholder)
        for (AssetOverviewDTO overview : overviewMap.values()) {
            // Calculate ownership percentage
            if (overview.getTotalEmissionShares() != null
                    && overview.getTotalEmissionShares().compareTo(BigDecimal.ZERO) > 0
                    && overview.getSharesOwned() != null) {
                BigDecimal ownershipPct = overview.getSharesOwned()
                        .divide(overview.getTotalEmissionShares(), 4, RoundingMode.HALF_UP)
                        .multiply(BigDecimal.valueOf(100));
                overview.setOwnershipPercentage(ownershipPct);
            }

            // TODO: Get market value from external source or Oracle function
            // For now, use book value as market value
            overview.setMarketValue(overview.getBookValue());

            // Calculate unrealized gain/loss
            if (overview.getMarketValue() != null && overview.getBookValue() != null) {
                overview.setUnrealizedGainLoss(overview.getMarketValue().subtract(overview.getBookValue()));
            }
        }

        logger.info("Generated overview for " + overviewMap.size() + " emissions");
        return new ArrayList<>(overviewMap.values());
    }

    /**
     * Helper method to convert entity to DTO
     */
    private EquityStakeDTO convertToDTO(EquityStakeEntity entity) {
        // Get accounting company name
        String accountingCompanyName = "N/A";
        try {
            SpolecnostEntity company = entity.getAccountingCompany();
            if (company != null) {
                accountingCompanyName = company.getNazev();
            }
        } catch (Exception e) {
            logger.warning("Could not fetch accounting company for stake: " + entity.getId());
        }

        // Get transaction type name
        String transactionTypeName = "N/A";
        if (entity.getTransactionTypeId() != null) {
            try {
                EquityStakeTransactionTypeEntity transactionType = transactionTypeRepository
                        .findById(entity.getTransactionTypeId())
                        .orElse(null);
                if (transactionType != null) {
                    transactionTypeName = transactionType.getDescription();
                }
            } catch (Exception e) {
                logger.warning("Could not fetch transaction type: " + entity.getTransactionTypeId());
            }
        }

        // Get method name
        String methodName = "N/A";
        if (entity.getMethodId() != null) {
            try {
                EquityStakeMethodEntity method = methodRepository
                        .findById(entity.getMethodId())
                        .orElse(null);
                if (method != null) {
                    methodName = method.getDescription();
                }
            } catch (Exception e) {
                logger.warning("Could not fetch method: " + entity.getMethodId());
            }
        }

        // Get purchased from company name
        String purchasedFromCompanyName = null;
        if (entity.getPurchasedFromCompanyId() != null) {
            try {
                SpolecnostEntity company = spolecnostRepository
                        .findById(entity.getPurchasedFromCompanyId())
                        .orElse(null);
                if (company != null) {
                    purchasedFromCompanyName = company.getNazev();
                }
            } catch (Exception e) {
                logger.warning("Could not fetch purchased from company: " + entity.getPurchasedFromCompanyId());
            }
        }

        return EquityStakeDTO.builder()
                .id(entity.getId())
                .emissionId(entity.getEmissionId())
                .accountingCompanyId(entity.getAccountingCompanyId())
                .accountingCompanyName(accountingCompanyName)
                .accountNumber(entity.getAccountNumber())
                .validFrom(entity.getValidFrom())
                .validTo(entity.getValidTo())
                .transactionTypeId(entity.getTransactionTypeId())
                .transactionTypeName(transactionTypeName)
                .methodId(entity.getMethodId())
                .methodName(methodName)
                .numberOfShares(entity.getNumberOfShares())
                .transactionCurrency(entity.getTransactionCurrency())
                .pricePerShareTransaction(entity.getPricePerShareTransaction())
                .totalTransactionAmount(entity.getTotalTransactionAmount())
                .exchangeRate(entity.getExchangeRate())
                .accountingCurrency(entity.getAccountingCurrency())
                .pricePerShareAccounting(entity.getPricePerShareAccounting())
                .totalAccountingAmount(entity.getTotalAccountingAmount())
                .purchasedFromCompanyId(entity.getPurchasedFromCompanyId())
                .purchasedFromCompanyName(purchasedFromCompanyName)
                .ignoreFlag(entity.getIgnoreFlag() != null && entity.getIgnoreFlag().equals("1"))
                .lastModified(entity.getLastModified())
                .modifiedByUser(entity.getModifiedByUser())
                .build();
    }

    /**
     * Helper method to check if stake is valid at a specific date
     */
    private boolean isValidAtDate(EquityStakeEntity stake, LocalDate date) {
        if (stake.getValidFrom() != null && date.isBefore(stake.getValidFrom())) {
            return false;
        }
        if (stake.getValidTo() != null && date.isAfter(stake.getValidTo())) {
            return false;
        }
        return true;
    }

    // ============================================================================
    // Control Rules Methods
    // ============================================================================

    /**
     * Get all control rules
     *
     * TODO: Implement actual persistence using Oracle database
     * For now, returns mock data for development
     */
    public List<AssetControlRuleDTO> getControlRules() {
        logger.info("Fetching all control rules");

        // Mock data for now
        List<AssetControlRuleDTO> rules = new ArrayList<>();

        AssetControlRuleDTO rule1 = AssetControlRuleDTO.builder()
                .id(1L)
                .accountPattern("061*")
                .equityStakeTypeId(1L)
                .equityStakeTypeName("Přímá účast")
                .isActive(true)
                .validationMessage("Účet musí začínat 061 pro přímou účast")
                .description("Pravidlo pro přímé účasti")
                .build();

        AssetControlRuleDTO rule2 = AssetControlRuleDTO.builder()
                .id(2L)
                .accountPattern("062*")
                .equityStakeTypeId(2L)
                .equityStakeTypeName("Nepřímá účast")
                .isActive(true)
                .validationMessage("Účet musí začínat 062 pro nepřímou účast")
                .description("Pravidlo pro nepřímé účasti")
                .build();

        rules.add(rule1);
        rules.add(rule2);

        logger.info("Found " + rules.size() + " control rules");
        return rules;
    }

    /**
     * Get single control rule by ID
     */
    public AssetControlRuleDTO getControlRuleById(Long id) {
        logger.info("Fetching control rule ID: " + id);

        // Mock data for now
        return AssetControlRuleDTO.builder()
                .id(id)
                .accountPattern("061*")
                .equityStakeTypeId(1L)
                .equityStakeTypeName("Přímá účast")
                .isActive(true)
                .validationMessage("Účet musí začínat 061 pro přímou účast")
                .description("Pravidlo pro přímé účasti")
                .build();
    }

    /**
     * Create new control rule
     */
    @Transactional
    public AssetControlRuleDTO createControlRule(AssetControlRuleDTO dto) {
        logger.info("Creating new control rule: " + dto.getAccountPattern());

        // TODO: Implement actual persistence
        // For now, return the input with a generated ID
        dto.setId(System.currentTimeMillis());

        logger.info("Control rule created with ID: " + dto.getId());
        return dto;
    }

    /**
     * Update control rule
     */
    @Transactional
    public AssetControlRuleDTO updateControlRule(Long id, AssetControlRuleDTO dto) {
        logger.info("Updating control rule ID: " + id);

        // TODO: Implement actual persistence
        // For now, return the input
        dto.setId(id);

        logger.info("Control rule updated successfully");
        return dto;
    }

    /**
     * Delete control rule
     */
    @Transactional
    public void deleteControlRule(Long id) {
        logger.info("Deleting control rule ID: " + id);

        // TODO: Implement actual persistence
        // For now, just log

        logger.info("Control rule deleted successfully");
    }
}
