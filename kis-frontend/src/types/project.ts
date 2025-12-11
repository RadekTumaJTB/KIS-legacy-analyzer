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
  oldProjectNumber?: string;
  status: string;
  statusCode: string;
  idStatus: number;
  projectManagerName: string;
  idProjectManager: number;
  proposedByName?: string;
  idProposedBy?: number;
  managementSegmentName: string;
  idManagementSegment: number;
  currencyCode: string;
  idCurrency: number;
  valuationStartDate: string;
  valuationEndDate?: string;
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
  projectProposalName?: string;
  idProjectProposal?: number;
  validityFrom?: string;
  validityTo?: string;
  nextProjectCardReport?: string;
  reportPeriodMonths?: number;
  createdAt: string;
  createdBy: string;
  updatedAt: string;
  cashFlowList: ProjectCashFlowDTO[];
}

/**
 * Reference data types for dropdowns
 */

export interface ProjectStatusDTO {
  id: number;
  code: string;
  description: string;
}

export interface UserDTO {
  id: number;
  firstName: string;
  lastName: string;
  fullName: string;
  email?: string;
}

export interface ProjectCategoryDTO {
  id: number;
  code: string;
  description: string;
}

export interface FrequencyDTO {
  id: number;
  code: string;
  description: string;
}

export interface ProjectProposalDTO {
  id: number;
  name: string;
  description?: string;
}

export interface BalanceTypeDTO {
  id: number;
  code: string;
  description: string;
}

export interface BudgetTypeDTO {
  id: number;
  code: string;
  description: string;
}

/**
 * Form data for creating/updating projects
 */

export interface ProjectFormData {
  name: string;
  oldProjectNumber?: string;
  idStatus: number;
  idProposedBy?: number;
  idProjectManager: number;
  idCategory?: number;
  idManagementSegment?: number;
  currencyCode: string;
  valuationStartDate: string;
  idFrequency?: number;
  description?: string;
  idProjectProposal?: number;
  nextProjectCardReport?: string;
  reportPeriodMonths?: number;
  idBalanceType?: number;
  budgetTrackingFlag: boolean;
  idBudgetType?: number;
}

/**
 * Reference data collection for form dropdowns
 */
export interface ProjectReferenceData {
  statuses: ProjectStatusDTO[];
  users: UserDTO[];
  categories: ProjectCategoryDTO[];
  managementSegments: ManagementSegmentDTO[];
  currencies: CurrencyDTO[];
  frequencies: FrequencyDTO[];
  projectProposals: ProjectProposalDTO[];
  balanceTypes: BalanceTypeDTO[];
  budgetTypes: BudgetTypeDTO[];
}
