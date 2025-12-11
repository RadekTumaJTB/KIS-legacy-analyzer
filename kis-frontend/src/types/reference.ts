/**
 * TypeScript types for Reference Data (Číselníky)
 * Used for dropdowns, filters, and form selects
 * Matches backend ReferenceDataController.ReferenceDTO
 */

// Generic reference data type (used by all dropdowns)
export interface ReferenceData {
  id: number;
  code: string;
  description: string;
}

// Specific reference types (for better type safety)
export type ProjectStatus = ReferenceData;
export type User = ReferenceData;
export type ProjectCategory = ReferenceData;
export type ManagementSegment = ReferenceData;
export type Currency = ReferenceData;
export type Frequency = ReferenceData;
export type ProjectProposal = ReferenceData;
export type BalanceType = ReferenceData;
export type BudgetType = ReferenceData;
export type Company = ReferenceData;
export type Department = ReferenceData;

// Transaction types for equity stakes
export type TransactionType = ReferenceData;  // Nákup, Prodej, Transfer

// Methods for equity stakes
export type EquityStakeMethod = ReferenceData;  // Přímá účast, Nepřímá účast

// Helper function to convert ReferenceData to select options
export const toSelectOptions = (items: ReferenceData[]) =>
  items.map(item => ({
    value: item.id,
    label: item.description || item.code,
  }));

// Helper function to find reference by code
export const findByCode = (items: ReferenceData[], code: string): ReferenceData | undefined =>
  items.find(item => item.code === code);

// Helper function to find reference by ID
export const findById = (items: ReferenceData[], id: number): ReferenceData | undefined =>
  items.find(item => item.id === id);
