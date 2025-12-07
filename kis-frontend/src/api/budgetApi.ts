import type { BudgetDetailDTO, BudgetKPIDTO, BudgetSummaryDTO } from '../types/budget';

const BFF_BASE_URL = '/bff';

/**
 * Fetch budget KPIs for dashboard
 */
export async function fetchBudgetKPIs(): Promise<BudgetKPIDTO> {
  const response = await fetch(`${BFF_BASE_URL}/budgets/kpis`);

  if (!response.ok) {
    throw new Error(`Failed to fetch budget KPIs: ${response.statusText}`);
  }

  return response.json();
}

/**
 * Fetch all budgets (list view)
 */
export async function fetchBudgets(): Promise<BudgetSummaryDTO[]> {
  const response = await fetch(`${BFF_BASE_URL}/budgets`);

  if (!response.ok) {
    throw new Error(`Failed to fetch budgets: ${response.statusText}`);
  }

  return response.json();
}

/**
 * Fetch budget detail with line items
 */
export async function fetchBudgetDetail(id: number): Promise<BudgetDetailDTO> {
  const response = await fetch(`${BFF_BASE_URL}/budgets/${id}`);

  if (!response.ok) {
    throw new Error(`Failed to fetch budget ${id}: ${response.statusText}`);
  }

  return response.json();
}

/**
 * Create new budget
 */
export async function createBudget(data: {
  code: string;
  name: string;
  type: string;
  year: string;
  plannedAmount: string;
  departmentName: string;
  description?: string;
}): Promise<BudgetDetailDTO> {
  const response = await fetch(`${BFF_BASE_URL}/budgets`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify({
      code: data.code,
      name: data.name,
      type: data.type,
      year: parseInt(data.year),
      plannedAmount: parseFloat(data.plannedAmount),
      departmentName: data.departmentName,
      description: data.description,
    }),
  });

  if (!response.ok) {
    throw new Error(`Failed to create budget: ${response.statusText}`);
  }

  return response.json();
}

/**
 * Update budget
 */
export async function updateBudget(
  id: number,
  data: {
    name: string;
    plannedAmount: string;
    description?: string;
  }
): Promise<BudgetDetailDTO> {
  const response = await fetch(`${BFF_BASE_URL}/budgets/${id}`, {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify({
      name: data.name,
      plannedAmount: parseFloat(data.plannedAmount),
      description: data.description,
    }),
  });

  if (!response.ok) {
    throw new Error(`Failed to update budget ${id}: ${response.statusText}`);
  }

  return response.json();
}
