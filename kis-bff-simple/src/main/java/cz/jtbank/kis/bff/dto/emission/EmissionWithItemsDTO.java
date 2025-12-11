package cz.jtbank.kis.bff.dto.emission;

import java.util.ArrayList;
import java.util.List;

/**
 * DTO for Emission with Items for batch update operations.
 * Combines Financial Investment metadata with its emission items.
 * Used for inline editing grid where multiple items can be modified at once.
 */
public class EmissionWithItemsDTO {

    private FinancialInvestmentDTO financialInvestment;
    private List<EmissionItemDTO> emissionItems;

    // Constructors
    public EmissionWithItemsDTO() {
        this.emissionItems = new ArrayList<>();
    }

    public EmissionWithItemsDTO(FinancialInvestmentDTO financialInvestment, List<EmissionItemDTO> emissionItems) {
        this.financialInvestment = financialInvestment;
        this.emissionItems = emissionItems != null ? emissionItems : new ArrayList<>();
    }

    // Builder pattern
    public static class Builder {
        private EmissionWithItemsDTO dto = new EmissionWithItemsDTO();

        public Builder financialInvestment(FinancialInvestmentDTO financialInvestment) {
            dto.financialInvestment = financialInvestment;
            return this;
        }

        public Builder emissionItems(List<EmissionItemDTO> emissionItems) {
            dto.emissionItems = emissionItems;
            return this;
        }

        public Builder addEmissionItem(EmissionItemDTO item) {
            if (dto.emissionItems == null) {
                dto.emissionItems = new ArrayList<>();
            }
            dto.emissionItems.add(item);
            return this;
        }

        public EmissionWithItemsDTO build() {
            return dto;
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    /**
     * Get only items marked for insert
     */
    public List<EmissionItemDTO> getItemsToInsert() {
        return emissionItems.stream()
                .filter(item -> "I".equals(item.getAction()))
                .toList();
    }

    /**
     * Get only items marked for update
     */
    public List<EmissionItemDTO> getItemsToUpdate() {
        return emissionItems.stream()
                .filter(item -> "U".equals(item.getAction()))
                .toList();
    }

    /**
     * Get only items marked for delete
     */
    public List<EmissionItemDTO> getItemsToDelete() {
        return emissionItems.stream()
                .filter(item -> "D".equals(item.getAction()))
                .toList();
    }

    // Getters and Setters
    public FinancialInvestmentDTO getFinancialInvestment() {
        return financialInvestment;
    }

    public void setFinancialInvestment(FinancialInvestmentDTO financialInvestment) {
        this.financialInvestment = financialInvestment;
    }

    public List<EmissionItemDTO> getEmissionItems() {
        return emissionItems;
    }

    public void setEmissionItems(List<EmissionItemDTO> emissionItems) {
        this.emissionItems = emissionItems;
    }
}
