import type {
  ProjectDetailDTO,
  ProjectSummaryDTO,
  ProjectFormData,
  ProjectStatusDTO,
  UserDTO,
  ProjectCategoryDTO,
  ManagementSegmentDTO,
  CurrencyDTO,
  FrequencyDTO,
  ProjectProposalDTO,
  BalanceTypeDTO,
  BudgetTypeDTO,
  ProjectReferenceData,
} from '../types/project';

const BFF_BASE_URL = '/bff';

/**
 * Fetch all projects (list view)
 */
export async function fetchProjects(): Promise<ProjectSummaryDTO[]> {
  const response = await fetch(`${BFF_BASE_URL}/projects`);

  if (!response.ok) {
    throw new Error(`Failed to fetch projects: ${response.statusText}`);
  }

  return response.json();
}

/**
 * Fetch project detail with cash flow list
 */
export async function fetchProjectDetail(id: number): Promise<ProjectDetailDTO> {
  const response = await fetch(`${BFF_BASE_URL}/projects/${id}`);

  if (!response.ok) {
    throw new Error(`Failed to fetch project ${id}: ${response.statusText}`);
  }

  return response.json();
}

/**
 * Create new project
 */
export async function createProject(data: ProjectFormData): Promise<ProjectDetailDTO> {
  const response = await fetch(`${BFF_BASE_URL}/projects`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(data),
  });

  if (!response.ok) {
    throw new Error(`Failed to create project: ${response.statusText}`);
  }

  return response.json();
}

/**
 * Update project
 */
export async function updateProject(
  id: number,
  data: ProjectFormData
): Promise<ProjectDetailDTO> {
  const response = await fetch(`${BFF_BASE_URL}/projects/${id}`, {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(data),
  });

  if (!response.ok) {
    throw new Error(`Failed to update project ${id}: ${response.statusText}`);
  }

  return response.json();
}

/**
 * ===== Reference Data API =====
 * Fetch dropdown/select options for project forms
 */

/**
 * Fetch all project statuses
 */
export async function fetchProjectStatuses(): Promise<ProjectStatusDTO[]> {
  const response = await fetch(`${BFF_BASE_URL}/reference/project-statuses`);

  if (!response.ok) {
    throw new Error(`Failed to fetch project statuses: ${response.statusText}`);
  }

  return response.json();
}

/**
 * Fetch all users (for project managers and proposers)
 */
export async function fetchUsers(): Promise<UserDTO[]> {
  const response = await fetch(`${BFF_BASE_URL}/reference/users`);

  if (!response.ok) {
    throw new Error(`Failed to fetch users: ${response.statusText}`);
  }

  return response.json();
}

/**
 * Fetch all project categories
 */
export async function fetchProjectCategories(): Promise<ProjectCategoryDTO[]> {
  const response = await fetch(`${BFF_BASE_URL}/reference/categories`);

  if (!response.ok) {
    throw new Error(`Failed to fetch categories: ${response.statusText}`);
  }

  return response.json();
}

/**
 * Fetch all management segments (holdings)
 */
export async function fetchManagementSegments(): Promise<ManagementSegmentDTO[]> {
  const response = await fetch(`${BFF_BASE_URL}/reference/management-segments`);

  if (!response.ok) {
    throw new Error(`Failed to fetch management segments: ${response.statusText}`);
  }

  return response.json();
}

/**
 * Fetch all currencies
 */
export async function fetchCurrencies(): Promise<CurrencyDTO[]> {
  const response = await fetch(`${BFF_BASE_URL}/reference/currencies`);

  if (!response.ok) {
    throw new Error(`Failed to fetch currencies: ${response.statusText}`);
  }

  return response.json();
}

/**
 * Fetch all revaluation frequencies
 */
export async function fetchFrequencies(): Promise<FrequencyDTO[]> {
  const response = await fetch(`${BFF_BASE_URL}/reference/frequencies`);

  if (!response.ok) {
    throw new Error(`Failed to fetch frequencies: ${response.statusText}`);
  }

  return response.json();
}

/**
 * Fetch all project proposals
 */
export async function fetchProjectProposals(): Promise<ProjectProposalDTO[]> {
  const response = await fetch(`${BFF_BASE_URL}/reference/project-proposals`);

  if (!response.ok) {
    throw new Error(`Failed to fetch project proposals: ${response.statusText}`);
  }

  return response.json();
}

/**
 * Fetch all balance types
 */
export async function fetchBalanceTypes(): Promise<BalanceTypeDTO[]> {
  const response = await fetch(`${BFF_BASE_URL}/reference/balance-types`);

  if (!response.ok) {
    throw new Error(`Failed to fetch balance types: ${response.statusText}`);
  }

  return response.json();
}

/**
 * Fetch all budget types
 */
export async function fetchBudgetTypes(): Promise<BudgetTypeDTO[]> {
  const response = await fetch(`${BFF_BASE_URL}/reference/budget-types`);

  if (!response.ok) {
    throw new Error(`Failed to fetch budget types: ${response.statusText}`);
  }

  return response.json();
}

/**
 * Fetch all reference data at once (optimized for form initialization)
 */
export async function fetchProjectReferenceData(): Promise<ProjectReferenceData> {
  const [
    statuses,
    users,
    categories,
    managementSegments,
    currencies,
    frequencies,
    projectProposals,
    balanceTypes,
    budgetTypes,
  ] = await Promise.all([
    fetchProjectStatuses(),
    fetchUsers(),
    fetchProjectCategories(),
    fetchManagementSegments(),
    fetchCurrencies(),
    fetchFrequencies(),
    fetchProjectProposals(),
    fetchBalanceTypes(),
    fetchBudgetTypes(),
  ]);

  return {
    statuses,
    users,
    categories,
    managementSegments,
    currencies,
    frequencies,
    projectProposals,
    balanceTypes,
    budgetTypes,
  };
}
