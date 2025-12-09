/**
 * Project types matching BFF DTOs
 */

export interface ProjectSummaryDTO {
  id: number;
  name: string;
  projectNumber: string;
  status: string;
  statusCode: string;
  projectManagerName: string;
  managementSegmentName: string;
  startDate: string; // ISO date
  description: string;
}

export interface ProjectManagerDTO {
  id: number;
  name: string;
  email: string;
  position: string;
}

export interface ManagementSegmentDTO {
  id: number;
  name: string;
  code: string;
}

export interface CurrencyDTO {
  id: number;
  code: string;
  name: string;
}

export interface ProjectCashFlowDTO {
  id: number;
  idProject: number;
  cashFlowTypeName: string;
  date: string; // ISO date
  amount: number;
  currencyCode: string;
  inOutTypeName: string;
  positionTypeName: string;
  notes: string;
}

export interface ProjectDetailDTO {
  id: number;
  name: string;
  projectNumber: string;
  status: string;
  statusCode: string;
  projectManagerName: string;
  idProjectManager: number;
  managementSegmentName: string;
  idManagementSegment: number;
  currencyCode: string;
  idCurrency: number;
  valuationStartDate: string;
  frequencyName: string;
  idFrequency: number;
  description: string;
  approvalLevel1Amount: number;
  approvalLevel2Amount: number;
  approvalLevel3Amount: number;
  budgetTrackingFlag: string;
  budgetIncreaseAmountPM: number;
  budgetIncreaseAmountTop: number;
  balanceTypeName: string;
  idBalanceType: number;
  budgetTypeName: string;
  idBudgetType: number;
  categoryName: string;
  idCategory: number;
  createdAt: string;
  createdBy: string;
  updatedAt: string;
  cashFlowList: ProjectCashFlowDTO[];
}
