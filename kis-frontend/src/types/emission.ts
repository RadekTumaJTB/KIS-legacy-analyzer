/**
 * TypeScript types for Emission module (Finanční Investice a Emise)
 * Matches backend DTOs from cz.jtbank.kis.bff.dto.emission
 */

// Financial Investment (top-level investment metadata)
export interface FinancialInvestment {
  id: number;
  companyId: number;
  companyName: string;
  currency: string;
  isinCode: string;
  lastModified: string;
  modifiedByUser: string;
}

// Emission Item with inline editing support
export interface EmissionItem {
  id?: number;
  financialInvestmentId: number;

  // Action tracking for batch updates: 'I' = Insert, 'U' = Update, 'D' = Delete
  action: 'I' | 'U' | 'D';

  // Temporal validity
  validFrom: string;
  validTo?: string;

  // Emission details
  numberOfShares: number;
  nominalValue: number;
  registeredCapital: number;
  volume: number;  // Auto-calculated: numberOfShares × nominalValue

  // Flags
  investmentFlag: boolean;

  // UI state (not persisted to database)
  isExpanded?: boolean;  // For collapsible rows in UI
  isDirty?: boolean;     // Track unsaved changes in UI
}

// Emission with Items for batch update operations
export interface EmissionWithItems {
  financialInvestment: FinancialInvestment;
  emissionItems: EmissionItem[];
}

// Helper functions for EmissionItem
export const createNewEmissionItem = (financialInvestmentId: number): EmissionItem => ({
  financialInvestmentId,
  action: 'I',
  validFrom: new Date().toISOString().split('T')[0],
  numberOfShares: 0,
  nominalValue: 0,
  registeredCapital: 0,
  volume: 0,
  investmentFlag: false,
  isExpanded: false,
  isDirty: false,
});

export const calculateVolume = (item: EmissionItem): number => {
  return item.numberOfShares * item.nominalValue;
};

export const markItemForUpdate = (item: EmissionItem): EmissionItem => ({
  ...item,
  action: item.id ? 'U' : 'I',
  isDirty: true,
});

export const markItemForDelete = (item: EmissionItem): EmissionItem => ({
  ...item,
  action: 'D',
  isDirty: true,
});

// Filter functions for batch operations
export const getItemsToInsert = (items: EmissionItem[]): EmissionItem[] =>
  items.filter(item => item.action === 'I');

export const getItemsToUpdate = (items: EmissionItem[]): EmissionItem[] =>
  items.filter(item => item.action === 'U');

export const getItemsToDelete = (items: EmissionItem[]): EmissionItem[] =>
  items.filter(item => item.action === 'D');

export const getDirtyItems = (items: EmissionItem[]): EmissionItem[] =>
  items.filter(item => item.isDirty);
