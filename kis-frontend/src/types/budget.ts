/**
 * Budget types matching BFF DTOs
 */

export interface BudgetSummaryDTO {
  id: number;
  code: string;
  name: string;
  type: 'REVENUE' | 'EXPENSE' | 'CAPEX';
  year: number;
  status: 'DRAFT' | 'ACTIVE' | 'LOCKED' | 'ARCHIVED';
  plannedAmount: number;
  actualAmount: number;
  variance: number;
  utilizationPercent: number;
  departmentName: string;
  ownerName: string;
  validFrom: string; // ISO date string
  validTo: string;   // ISO date string
}

export interface BudgetLineItemDTO {
  id: number;
  month: number; // 1-12
  monthName: string; // "Leden", "Únor", etc.
  plannedAmount: number;
  actualAmount: number;
  variance: number;
  utilizationPercent: number;
  status: 'ON_TRACK' | 'OVER_BUDGET' | 'UNDER_BUDGET' | 'WARNING' | 'PENDING';
  notes: string;
}

export interface BudgetDetailDTO {
  id: number;
  code: string;
  name: string;
  description: string;
  type: 'REVENUE' | 'EXPENSE' | 'CAPEX';
  year: number;
  status: 'DRAFT' | 'ACTIVE' | 'LOCKED' | 'ARCHIVED';
  totalPlanned: number;
  totalActual: number;
  totalVariance: number;
  utilizationPercent: number;
  departmentName: string;
  owner: {
    id: number;
    name: string;
    email: string;
    position: string;
  };
  validFrom: string;
  validTo: string;
  createdAt: string;
  updatedAt: string;
  lineItems: BudgetLineItemDTO[];
  notes: string;
}

export interface BudgetKPIDTO {
  totalPlanned: number;
  totalActual: number;
  totalVariance: number;
  averageUtilization: number;
  totalBudgets: number;
  activeBudgets: number;
  overBudgetCount: number;
  underBudgetCount: number;
  largestVariance: number;
  largestVarianceBudgetName: string;
}
