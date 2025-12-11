package cz.jtbank.kis.bff.controller;

import cz.jtbank.kis.bff.dto.ReferenceDataDTO;
import cz.jtbank.kis.bff.entity.*;
import cz.jtbank.kis.bff.repository.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * BFF Controller for Reference Data (Dropdowns)
 *
 * Provides lookup data for dropdowns in frontend forms:
 * - Project statuses, categories, segments
 * - Document types, statuses
 * - Budget types
 * - Currencies
 * - Transaction types, methods
 */
@RestController
@RequestMapping("/bff/reference")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"})
public class ReferenceDataBFFController {

    // Project repositories
    private final ProjectStatusRepository projectStatusRepository;
    private final ProjektKategorieRepository projektKategorieRepository;
    private final ManagementSegmentRepository managementSegmentRepository;
    private final TypProjektoveBilanceRepository typProjektoveBilanceRepository;
    private final TypBudgetuProjektuRepository typBudgetuProjektuRepository;

    // Document repositories
    private final DokumentStatusRepository dokumentStatusRepository;
    private final DokumentTypRepository dokumentTypRepository;

    // General repositories
    private final CurrencyRepository currencyRepository;

    // Asset repositories (for Majetkové účasti)
    private final EquityStakeTransactionTypeRepository equityStakeTransactionTypeRepository;

    public ReferenceDataBFFController(
            ProjectStatusRepository projectStatusRepository,
            ProjektKategorieRepository projektKategorieRepository,
            ManagementSegmentRepository managementSegmentRepository,
            TypProjektoveBilanceRepository typProjektoveBilanceRepository,
            TypBudgetuProjektuRepository typBudgetuProjektuRepository,
            DokumentStatusRepository dokumentStatusRepository,
            DokumentTypRepository dokumentTypRepository,
            CurrencyRepository currencyRepository,
            EquityStakeTransactionTypeRepository equityStakeTransactionTypeRepository) {
        this.projectStatusRepository = projectStatusRepository;
        this.projektKategorieRepository = projektKategorieRepository;
        this.managementSegmentRepository = managementSegmentRepository;
        this.typProjektoveBilanceRepository = typProjektoveBilanceRepository;
        this.typBudgetuProjektuRepository = typBudgetuProjektuRepository;
        this.dokumentStatusRepository = dokumentStatusRepository;
        this.dokumentTypRepository = dokumentTypRepository;
        this.currencyRepository = currencyRepository;
        this.equityStakeTransactionTypeRepository = equityStakeTransactionTypeRepository;
    }

    // ===== PROJECT REFERENCE DATA =====

    @GetMapping("/project-statuses")
    public List<ReferenceDataDTO> getProjectStatuses() {
        return projectStatusRepository.findAll().stream()
                .map(e -> new ReferenceDataDTO(e.getId(), e.getCode(), e.getName(), e.getDescription()))
                .collect(Collectors.toList());
    }

    @GetMapping("/project-categories")
    public List<ReferenceDataDTO> getProjectCategories() {
        return projektKategorieRepository.findAll().stream()
                .map(e -> new ReferenceDataDTO(e.getId(), e.getCode(), e.getName(), e.getDescription()))
                .collect(Collectors.toList());
    }

    @GetMapping("/management-segments")
    public List<ReferenceDataDTO> getManagementSegments() {
        return managementSegmentRepository.findAll().stream()
                .map(e -> new ReferenceDataDTO(e.getId(), e.getCode(), e.getName(), e.getDescription()))
                .collect(Collectors.toList());
    }

    @GetMapping("/project-balance-types")
    public List<ReferenceDataDTO> getProjectBalanceTypes() {
        return typProjektoveBilanceRepository.findAll().stream()
                .map(e -> new ReferenceDataDTO(e.getId(), e.getCode(), e.getName(), e.getDescription()))
                .collect(Collectors.toList());
    }

    @GetMapping("/project-budget-types")
    public List<ReferenceDataDTO> getProjectBudgetTypes() {
        return typBudgetuProjektuRepository.findAll().stream()
                .map(e -> new ReferenceDataDTO(e.getId(), e.getCode(), e.getName(), e.getDescription()))
                .collect(Collectors.toList());
    }

    // ===== DOCUMENT REFERENCE DATA =====

    @GetMapping("/document-statuses")
    public List<ReferenceDataDTO> getDocumentStatuses() {
        return dokumentStatusRepository.findAll().stream()
                .map(e -> new ReferenceDataDTO(e.getId(), e.getKod(), e.getNazev(), null))
                .collect(Collectors.toList());
    }

    @GetMapping("/document-types")
    public List<ReferenceDataDTO> getDocumentTypes() {
        return dokumentTypRepository.findAll().stream()
                .map(e -> new ReferenceDataDTO(e.getId(), e.getKod(), e.getNazev(), null))
                .collect(Collectors.toList());
    }

    // ===== GENERAL REFERENCE DATA =====

    @GetMapping("/currencies")
    public List<ReferenceDataDTO> getCurrencies() {
        return currencyRepository.findAll().stream()
                .map(e -> new ReferenceDataDTO(e.getId(), e.getCode(), e.getName(), e.getDescription()))
                .collect(Collectors.toList());
    }

    // ===== ASSET REFERENCE DATA =====

    @GetMapping("/equity-transaction-types")
    public List<ReferenceDataDTO> getEquityTransactionTypes() {
        return equityStakeTransactionTypeRepository.findAll().stream()
                .map(e -> new ReferenceDataDTO(e.getId(), null, e.getDescription(), null))
                .collect(Collectors.toList());
    }
}
