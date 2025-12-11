/**
 * TypeScript types for Asset module (Majetkové Účasti)
 * Matches backend DTOs from cz.jtbank.kis.bff.dto.asset
 */

// Equity Stake (Majetková Účast)
export interface EquityStake {
  id: number;
  emissionId: number;
  accountingCompanyId: number;
  accountingCompanyName: string;
  accountNumber: string;

  // Temporal validity
  validFrom: string;
  validTo?: string;

  // Transaction type and method
  transactionTypeId: number;
  transactionTypeName: string;  // e.g., "Nákup", "Prodej", "Transfer"
  methodId: number;
  methodName: string;  // e.g., "Přímá účast", "Nepřímá účast"

  numberOfShares: number;

  // Transaction currency details
  transactionCurrency: string;
  pricePerShareTransaction: number;
  totalTransactionAmount: number;

  // Exchange rate
  exchangeRate: number;

  // Accounting currency details
  accountingCurrency: string;
  pricePerShareAccounting: number;
  totalAccountingAmount: number;

  // Additional fields
  purchasedFromCompanyId?: number;
  purchasedFromCompanyName?: string;
  ignoreFlag: boolean;

  // Audit fields
  lastModified: string;
  modifiedByUser: string;
}

// Asset Overview with calculations
export interface AssetOverview {
  emissionId: number;
  companyName: string;
  isinCode: string;

  // Share calculations
  totalEmissionShares: number;
  sharesOwned: number;
  ownershipPercentage: number;  // Calculated: (sharesOwned / totalEmissionShares) × 100

  // Value calculations
  marketValue: number;
  bookValue: number;
  unrealizedGainLoss: number;  // Calculated: marketValue - bookValue

  // Additional context
  currency: string;
  accountingCompanyName: string;
}

// Asset Control Rule
export interface AssetControlRule {
  id: number;
  accountPattern: string;  // e.g., "061*", "062*"
  equityStakeTypeId: number;
  equityStakeTypeName: string;
  isActive: boolean;
  validationMessage?: string;
  description?: string;
}

// Company with role-based permissions
export interface CompanyWithPermissions {
  id: number;
  name: string;
  ico: string;
  canView: boolean;
  canEdit: boolean;
}

// Helper functions for EquityStake
export const createNewEquityStake = (
  emissionId: number,
  accountingCompanyId: number
): Partial<EquityStake> => ({
  emissionId,
  accountingCompanyId,
  validFrom: new Date().toISOString().split('T')[0],
  numberOfShares: 0,
  transactionCurrency: 'CZK',
  pricePerShareTransaction: 0,
  totalTransactionAmount: 0,
  exchangeRate: 1,
  accountingCurrency: 'CZK',
  pricePerShareAccounting: 0,
  totalAccountingAmount: 0,
  ignoreFlag: false,
});

export const calculateAccountingAmounts = (stake: Partial<EquityStake>): Partial<EquityStake> => {
  if (!stake.pricePerShareTransaction || !stake.exchangeRate) {
    return stake;
  }

  return {
    ...stake,
    pricePerShareAccounting: stake.pricePerShareTransaction * stake.exchangeRate,
    totalAccountingAmount: stake.totalTransactionAmount
      ? stake.totalTransactionAmount * stake.exchangeRate
      : 0,
  };
};

export const calculateTotalAmount = (stake: Partial<EquityStake>): number => {
  if (!stake.numberOfShares || !stake.pricePerShareTransaction) {
    return 0;
  }
  return stake.numberOfShares * stake.pricePerShareTransaction;
};

// Helper functions for AssetOverview
export const calculateOwnershipPercentage = (
  sharesOwned: number,
  totalShares: number
): number => {
  if (!totalShares || totalShares === 0) return 0;
  return (sharesOwned / totalShares) * 100;
};

export const calculateUnrealizedGainLoss = (
  marketValue: number,
  bookValue: number
): number => {
  return marketValue - bookValue;
};

// Helper function for AssetControlRule
export const matchesAccountPattern = (
  accountNumber: string,
  pattern: string
): boolean => {
  // Convert pattern to regex (e.g., "061*" → "^061.*")
  const regex = new RegExp('^' + pattern.replace(/\*/g, '.*'));
  return regex.test(accountNumber);
};
