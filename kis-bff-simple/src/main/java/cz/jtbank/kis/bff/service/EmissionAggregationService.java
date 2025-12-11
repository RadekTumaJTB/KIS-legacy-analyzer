package cz.jtbank.kis.bff.service;

import cz.jtbank.kis.bff.dto.emission.*;
import cz.jtbank.kis.bff.entity.*;
import cz.jtbank.kis.bff.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Emission Aggregation Service
 *
 * Handles Financial Investments and Emissions (Finanční Investice a Emise).
 * Aggregates data from Oracle database and provides unified CRUD operations.
 *
 * IMPORTANT: This service will eventually call Oracle PL/SQL procedures
 * for CRUD operations. Current implementation uses JPA for development.
 */
@Service
public class EmissionAggregationService {

    private static final Logger logger = Logger.getLogger(EmissionAggregationService.class.getName());

    private final FinancialInvestmentRepository financialInvestmentRepository;
    private final FinancialInvestmentEmissionRepository emissionRepository;
    private final SpolecnostRepository spolecnostRepository;

    public EmissionAggregationService(
            FinancialInvestmentRepository financialInvestmentRepository,
            FinancialInvestmentEmissionRepository emissionRepository,
            SpolecnostRepository spolecnostRepository) {
        this.financialInvestmentRepository = financialInvestmentRepository;
        this.emissionRepository = emissionRepository;
        this.spolecnostRepository = spolecnostRepository;
    }

    /**
     * Get list of all financial investments with their current emissions
     */
    public List<EmissionWithItemsDTO> getAllEmissions() {
        logger.info("Fetching all financial investments with emissions");

        List<FinancialInvestmentEntity> investments = financialInvestmentRepository.findAll();
        List<EmissionWithItemsDTO> result = new ArrayList<>();

        for (FinancialInvestmentEntity investment : investments) {
            // Get company name
            String companyName = "N/A";
            try {
                SpolecnostEntity company = investment.getCompany();
                if (company != null) {
                    companyName = company.getNazev();
                }
            } catch (Exception e) {
                logger.warning("Could not fetch company for investment " + investment.getId());
            }

            // Build Financial Investment DTO
            FinancialInvestmentDTO finInvDTO = FinancialInvestmentDTO.builder()
                    .id(investment.getId())
                    .companyId(investment.getCompanyId())
                    .companyName(companyName)
                    .currency(investment.getCurrencyCode())
                    .isinCode(investment.getIsinCode())
                    .lastModified(investment.getLastModified())
                    .modifiedByUser(investment.getModifiedByUser())
                    .build();

            // Get current emissions for this investment
            List<FinancialInvestmentEmissionEntity> emissions =
                    emissionRepository.findCurrentByFinancialInvestmentId(investment.getId());

            List<EmissionItemDTO> emissionItems = new ArrayList<>();
            for (FinancialInvestmentEmissionEntity emission : emissions) {
                // Calculate volume: numberOfShares × nominalValue
                BigDecimal volume = null;
                if (emission.getNumberOfShares() != null && emission.getNominalValue() != null) {
                    volume = BigDecimal.valueOf(emission.getNumberOfShares()).multiply(emission.getNominalValue());
                }

                EmissionItemDTO itemDTO = EmissionItemDTO.builder()
                        .id(emission.getId())
                        .financialInvestmentId(investment.getId())
                        .action("U")  // Default to Update
                        .validFrom(emission.getValidFrom())
                        .validTo(emission.getValidTo())
                        .numberOfShares(emission.getNumberOfShares() != null ? BigDecimal.valueOf(emission.getNumberOfShares()) : null)
                        .nominalValue(emission.getNominalValue())
                        .registeredCapital(emission.getRegisteredCapital())
                        .volume(volume)
                        .investmentFlag(emission.getInvestmentFlag() != null && emission.getInvestmentFlag().equals("1"))
                        .isExpanded(false)
                        .isDirty(false)
                        .build();
                emissionItems.add(itemDTO);
            }

            EmissionWithItemsDTO emissionWithItems = EmissionWithItemsDTO.builder()
                    .financialInvestment(finInvDTO)
                    .emissionItems(emissionItems)
                    .build();

            result.add(emissionWithItems);
        }

        logger.info("Found " + result.size() + " financial investments");
        return result;
    }

    /**
     * Get single emission with items by financial investment ID
     */
    public EmissionWithItemsDTO getEmissionById(Long financialInvestmentId) {
        logger.info("Fetching emission detail for financial investment ID: " + financialInvestmentId);

        FinancialInvestmentEntity investment = financialInvestmentRepository
                .findById(financialInvestmentId)
                .orElseThrow(() -> new EntityNotFoundException("Financial investment not found: " + financialInvestmentId));

        // Get company name
        String companyName = "N/A";
        try {
            SpolecnostEntity company = investment.getCompany();
            if (company != null) {
                companyName = company.getNazev();
            }
        } catch (Exception e) {
            logger.warning("Could not fetch company for investment " + investment.getId());
        }

        // Build Financial Investment DTO
        FinancialInvestmentDTO finInvDTO = FinancialInvestmentDTO.builder()
                .id(investment.getId())
                .companyId(investment.getCompanyId())
                .companyName(companyName)
                .currency(investment.getCurrencyCode())
                .isinCode(investment.getIsinCode())
                .lastModified(investment.getLastModified())
                .modifiedByUser(investment.getModifiedByUser())
                .build();

        // Get ALL emissions (including history) for this investment
        List<FinancialInvestmentEmissionEntity> emissions =
                emissionRepository.findHistoryByFinancialInvestmentId(investment.getId());

        List<EmissionItemDTO> emissionItems = new ArrayList<>();
        for (FinancialInvestmentEmissionEntity emission : emissions) {
            // Calculate volume: numberOfShares × nominalValue
            BigDecimal volume = null;
            if (emission.getNumberOfShares() != null && emission.getNominalValue() != null) {
                volume = BigDecimal.valueOf(emission.getNumberOfShares()).multiply(emission.getNominalValue());
            }

            EmissionItemDTO itemDTO = EmissionItemDTO.builder()
                    .id(emission.getId())
                    .financialInvestmentId(investment.getId())
                    .action("U")
                    .validFrom(emission.getValidFrom())
                    .validTo(emission.getValidTo())
                    .numberOfShares(emission.getNumberOfShares() != null ? BigDecimal.valueOf(emission.getNumberOfShares()) : null)
                    .nominalValue(emission.getNominalValue())
                    .registeredCapital(emission.getRegisteredCapital())
                    .volume(volume)
                    .investmentFlag(emission.getInvestmentFlag() != null && emission.getInvestmentFlag().equals("1"))
                    .isExpanded(false)
                    .isDirty(false)
                    .build();
            emissionItems.add(itemDTO);
        }

        return EmissionWithItemsDTO.builder()
                .financialInvestment(finInvDTO)
                .emissionItems(emissionItems)
                .build();
    }

    /**
     * Get emission history for a financial investment
     * Returns all historical versions sorted by validFrom desc
     */
    public List<EmissionItemDTO> getEmissionHistory(Long financialInvestmentId) {
        logger.info("Fetching emission history for financial investment ID: " + financialInvestmentId);

        FinancialInvestmentEntity investment = financialInvestmentRepository
                .findById(financialInvestmentId)
                .orElseThrow(() -> new EntityNotFoundException("Financial investment not found: " + financialInvestmentId));

        // Get ALL emissions (including history) for this investment
        List<FinancialInvestmentEmissionEntity> emissions =
                emissionRepository.findHistoryByFinancialInvestmentId(financialInvestmentId);

        List<EmissionItemDTO> emissionItems = new ArrayList<>();
        for (FinancialInvestmentEmissionEntity emission : emissions) {
            // Calculate volume: numberOfShares × nominalValue
            BigDecimal volume = null;
            if (emission.getNumberOfShares() != null && emission.getNominalValue() != null) {
                volume = BigDecimal.valueOf(emission.getNumberOfShares()).multiply(emission.getNominalValue());
            }

            EmissionItemDTO itemDTO = EmissionItemDTO.builder()
                    .id(emission.getId())
                    .financialInvestmentId(financialInvestmentId)
                    .action("U")
                    .validFrom(emission.getValidFrom())
                    .validTo(emission.getValidTo())
                    .numberOfShares(emission.getNumberOfShares() != null ? BigDecimal.valueOf(emission.getNumberOfShares()) : null)
                    .nominalValue(emission.getNominalValue())
                    .registeredCapital(emission.getRegisteredCapital())
                    .volume(volume)
                    .investmentFlag(emission.getInvestmentFlag() != null && emission.getInvestmentFlag().equals("1"))
                    .isExpanded(false)
                    .isDirty(false)
                    .build();
            emissionItems.add(itemDTO);
        }

        logger.info("Found " + emissionItems.size() + " emission history records");
        return emissionItems;
    }

    /**
     * Batch update emissions - processes Insert/Update/Delete operations
     *
     * TODO: Replace with Oracle procedure call: KAP_FININV.investiceEmise()
     * For now, uses JPA for development.
     */
    @Transactional
    public EmissionWithItemsDTO batchUpdateEmissionItems(Long financialInvestmentId, List<EmissionItemDTO> items) {
        logger.info("Batch updating emission items for financial investment ID: " + financialInvestmentId);
        logger.info("Total items to process: " + items.size());

        FinancialInvestmentEntity investment = financialInvestmentRepository
                .findById(financialInvestmentId)
                .orElseThrow(() -> new EntityNotFoundException("Financial investment not found: " + financialInvestmentId));

        int insertCount = 0;
        int updateCount = 0;
        int deleteCount = 0;

        for (EmissionItemDTO itemDTO : items) {
            String action = itemDTO.getAction() != null ? itemDTO.getAction() : "U";

            switch (action) {
                case "I": // Insert
                    FinancialInvestmentEmissionEntity newEmission = FinancialInvestmentEmissionEntity.builder()
                            .financialInvestmentId(financialInvestmentId)
                            .validFrom(itemDTO.getValidFrom())
                            .validTo(itemDTO.getValidTo())
                            .numberOfShares(itemDTO.getNumberOfShares() != null ? itemDTO.getNumberOfShares().longValue() : null)
                            .nominalValue(itemDTO.getNominalValue())
                            .registeredCapital(itemDTO.getRegisteredCapital())
                            .investmentFlag(itemDTO.getInvestmentFlag() != null && itemDTO.getInvestmentFlag() ? "1" : "0")
                            .modifiedByUser("SYSTEM")  // TODO: Get from security context
                            .build();
                    emissionRepository.save(newEmission);
                    insertCount++;
                    break;

                case "U": // Update
                    if (itemDTO.getId() != null) {
                        FinancialInvestmentEmissionEntity existingEmission = emissionRepository
                                .findById(itemDTO.getId())
                                .orElseThrow(() -> new EntityNotFoundException("Emission not found: " + itemDTO.getId()));

                        existingEmission.setValidFrom(itemDTO.getValidFrom());
                        existingEmission.setValidTo(itemDTO.getValidTo());
                        existingEmission.setNumberOfShares(itemDTO.getNumberOfShares() != null ? itemDTO.getNumberOfShares().longValue() : null);
                        existingEmission.setNominalValue(itemDTO.getNominalValue());
                        existingEmission.setRegisteredCapital(itemDTO.getRegisteredCapital());
                        existingEmission.setInvestmentFlag(itemDTO.getInvestmentFlag() != null && itemDTO.getInvestmentFlag() ? "1" : "0");
                        existingEmission.setLastModified(LocalDate.now());
                        existingEmission.setModifiedByUser("SYSTEM");  // TODO: Get from security context

                        emissionRepository.save(existingEmission);
                        updateCount++;
                    }
                    break;

                case "D": // Delete
                    if (itemDTO.getId() != null) {
                        emissionRepository.deleteById(itemDTO.getId());
                        deleteCount++;
                    }
                    break;

                default:
                    logger.warning("Unknown action: " + action + " for emission item ID: " + itemDTO.getId());
            }
        }

        logger.info("Batch update complete. Inserted: " + insertCount + ", Updated: " + updateCount + ", Deleted: " + deleteCount);

        // Return updated emission with items
        return getEmissionById(financialInvestmentId);
    }

    /**
     * Create new financial investment
     *
     * TODO: Replace with Oracle procedure call: KAP_FININV.updateFinInv()
     */
    @Transactional
    public FinancialInvestmentDTO createFinancialInvestment(FinancialInvestmentDTO dto) {
        logger.info("Creating new financial investment for company ID: " + dto.getCompanyId());

        FinancialInvestmentEntity entity = FinancialInvestmentEntity.builder()
                .companyId(dto.getCompanyId())
                .currencyCode(dto.getCurrency())
                .isinCode(dto.getIsinCode())
                .modifiedByUser("SYSTEM")  // TODO: Get from security context
                .build();

        FinancialInvestmentEntity saved = financialInvestmentRepository.save(entity);

        return FinancialInvestmentDTO.builder()
                .id(saved.getId())
                .companyId(saved.getCompanyId())
                .companyName(dto.getCompanyName())
                .currency(saved.getCurrencyCode())
                .isinCode(saved.getIsinCode())
                .lastModified(saved.getLastModified())
                .modifiedByUser(saved.getModifiedByUser())
                .build();
    }

    /**
     * Delete financial investment (and all its emissions via cascade)
     */
    @Transactional
    public void deleteFinancialInvestment(Long id) {
        logger.info("Deleting financial investment ID: " + id);

        if (!financialInvestmentRepository.existsById(id)) {
            throw new EntityNotFoundException("Financial investment not found: " + id);
        }

        financialInvestmentRepository.deleteById(id);
        logger.info("Financial investment deleted successfully");
    }
}
