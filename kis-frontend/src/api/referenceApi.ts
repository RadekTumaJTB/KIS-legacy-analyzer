/**
 * API client for Reference Data (Číselníky)
 * Communicates with BFF endpoints at /bff/reference
 */

import axios from 'axios';
import type {
  ReferenceData,
  ProjectStatus,
  User,
  ProjectCategory,
  ManagementSegment,
  Currency,
  Frequency,
  ProjectProposal,
  BalanceType,
  BudgetType,
  Company,
  Department,
} from '../types/reference';

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8081';

/**
 * Get all project statuses (Nový, Schválený, Uzavřený, etc.)
 */
export const getProjectStatuses = async (): Promise<ProjectStatus[]> => {
  const response = await axios.get<ReferenceData[]>(
    `${API_BASE_URL}/bff/reference/project-statuses`
  );
  return response.data;
};

/**
 * Get all active users for assignment dropdowns
 */
export const getUsers = async (): Promise<User[]> => {
  const response = await axios.get<ReferenceData[]>(
    `${API_BASE_URL}/bff/reference/users`
  );
  return response.data;
};

/**
 * Get all project categories (Investiční, Provozní, etc.)
 */
export const getCategories = async (): Promise<ProjectCategory[]> => {
  const response = await axios.get<ReferenceData[]>(
    `${API_BASE_URL}/bff/reference/categories`
  );
  return response.data;
};

/**
 * Get all management segments (holdingová struktura)
 */
export const getManagementSegments = async (): Promise<ManagementSegment[]> => {
  const response = await axios.get<ReferenceData[]>(
    `${API_BASE_URL}/bff/reference/management-segments`
  );
  return response.data;
};

/**
 * Get all currencies (CZK, EUR, USD, etc.)
 */
export const getCurrencies = async (): Promise<Currency[]> => {
  const response = await axios.get<ReferenceData[]>(
    `${API_BASE_URL}/bff/reference/currencies`
  );
  return response.data;
};

/**
 * Get all frequencies (Měsíčně, Čtvrtletně, Ročně, etc.)
 */
export const getFrequencies = async (): Promise<Frequency[]> => {
  const response = await axios.get<ReferenceData[]>(
    `${API_BASE_URL}/bff/reference/frequencies`
  );
  return response.data;
};

/**
 * Get all project proposal statuses (Koncept, Předloženo, etc.)
 */
export const getProjectProposals = async (): Promise<ProjectProposal[]> => {
  const response = await axios.get<ReferenceData[]>(
    `${API_BASE_URL}/bff/reference/project-proposals`
  );
  return response.data;
};

/**
 * Get all balance types for project accounting
 */
export const getBalanceTypes = async (): Promise<BalanceType[]> => {
  const response = await axios.get<ReferenceData[]>(
    `${API_BASE_URL}/bff/reference/balance-types`
  );
  return response.data;
};

/**
 * Get all budget types (Provozní, Investiční, Projektový)
 */
export const getBudgetTypes = async (): Promise<BudgetType[]> => {
  const response = await axios.get<ReferenceData[]>(
    `${API_BASE_URL}/bff/reference/budget-types`
  );
  return response.data;
};

/**
 * Get all companies (Společnosti) for selection
 */
export const getCompanies = async (): Promise<Company[]> => {
  const response = await axios.get<ReferenceData[]>(
    `${API_BASE_URL}/bff/reference/companies`
  );
  return response.data;
};

/**
 * Get all departments (Odbory) for budget assignment
 */
export const getDepartments = async (): Promise<Department[]> => {
  const response = await axios.get<ReferenceData[]>(
    `${API_BASE_URL}/bff/reference/departments`
  );
  return response.data;
};

/**
 * Load all reference data at once (for initialization)
 * Returns a map of reference data by type
 */
export const loadAllReferenceData = async () => {
  const [
    projectStatuses,
    users,
    categories,
    managementSegments,
    currencies,
    frequencies,
    projectProposals,
    balanceTypes,
    budgetTypes,
    companies,
    departments,
  ] = await Promise.all([
    getProjectStatuses(),
    getUsers(),
    getCategories(),
    getManagementSegments(),
    getCurrencies(),
    getFrequencies(),
    getProjectProposals(),
    getBalanceTypes(),
    getBudgetTypes(),
    getCompanies(),
    getDepartments(),
  ]);

  return {
    projectStatuses,
    users,
    categories,
    managementSegments,
    currencies,
    frequencies,
    projectProposals,
    balanceTypes,
    budgetTypes,
    companies,
    departments,
  };
};
