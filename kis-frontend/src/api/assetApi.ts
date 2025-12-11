/**
 * API client for Asset module (Majetkové Účasti)
 * Communicates with BFF endpoints at /bff/assets
 */

import axios from 'axios';
import type {
  EquityStake,
  AssetOverview,
  AssetControlRule,
  CompanyWithPermissions,
} from '../types/asset';

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8081';

// ============================================================================
// Companies
// ============================================================================

/**
 * Get companies with role-based filtering
 * Returns companies based on user permissions (Admin_MU, MU_jednotlive, etc.)
 */
export const getCompaniesWithRoleFiltering = async (): Promise<CompanyWithPermissions[]> => {
  const response = await axios.get<CompanyWithPermissions[]>(
    `${API_BASE_URL}/bff/assets/companies`
  );
  return response.data;
};

// ============================================================================
// Equity Stakes (Majetkové Účasti)
// ============================================================================

/**
 * Get equity stakes for a specific company
 */
export const getEquityStakesForCompany = async (companyId: number): Promise<EquityStake[]> => {
  const response = await axios.get<EquityStake[]>(
    `${API_BASE_URL}/bff/assets/companies/${companyId}/participations`
  );
  return response.data;
};

/**
 * Get single equity stake by ID
 */
export const getEquityStakeById = async (id: number): Promise<EquityStake> => {
  const response = await axios.get<EquityStake>(
    `${API_BASE_URL}/bff/assets/participations/${id}`
  );
  return response.data;
};

/**
 * Create new equity stake
 */
export const createEquityStake = async (
  data: Partial<EquityStake>
): Promise<EquityStake> => {
  const response = await axios.post<EquityStake>(
    `${API_BASE_URL}/bff/assets/participations`,
    data
  );
  return response.data;
};

/**
 * Update existing equity stake
 */
export const updateEquityStake = async (
  id: number,
  data: Partial<EquityStake>
): Promise<EquityStake> => {
  const response = await axios.put<EquityStake>(
    `${API_BASE_URL}/bff/assets/participations/${id}`,
    data
  );
  return response.data;
};

/**
 * Delete equity stake
 */
export const deleteEquityStake = async (id: number): Promise<void> => {
  await axios.delete(`${API_BASE_URL}/bff/assets/participations/${id}`);
};

// ============================================================================
// Asset Overview
// ============================================================================

/**
 * Get asset overview with calculations
 * @param asOfDate - Snapshot date (YYYY-MM-DD), defaults to today
 * @param companyId - Filter by company (optional)
 */
export const getAssetOverview = async (params?: {
  asOfDate?: string;
  companyId?: number;
}): Promise<AssetOverview[]> => {
  const response = await axios.get<AssetOverview[]>(
    `${API_BASE_URL}/bff/assets/overview`,
    { params }
  );
  return response.data;
};

// ============================================================================
// Control Rules
// ============================================================================

/**
 * Get all control rules
 */
export const getControlRules = async (): Promise<AssetControlRule[]> => {
  const response = await axios.get<AssetControlRule[]>(
    `${API_BASE_URL}/bff/assets/controls`
  );
  return response.data;
};

/**
 * Get single control rule by ID
 */
export const getControlRuleById = async (id: number): Promise<AssetControlRule> => {
  const response = await axios.get<AssetControlRule>(
    `${API_BASE_URL}/bff/assets/controls/${id}`
  );
  return response.data;
};

/**
 * Create new control rule
 */
export const createControlRule = async (
  data: Partial<AssetControlRule>
): Promise<AssetControlRule> => {
  const response = await axios.post<AssetControlRule>(
    `${API_BASE_URL}/bff/assets/controls`,
    data
  );
  return response.data;
};

/**
 * Update control rule
 */
export const updateControlRule = async (
  id: number,
  data: Partial<AssetControlRule>
): Promise<AssetControlRule> => {
  const response = await axios.put<AssetControlRule>(
    `${API_BASE_URL}/bff/assets/controls/${id}`,
    data
  );
  return response.data;
};

/**
 * Delete control rule
 */
export const deleteControlRule = async (id: number): Promise<void> => {
  await axios.delete(`${API_BASE_URL}/bff/assets/controls/${id}`);
};

// ============================================================================
// Export
// ============================================================================

/**
 * Export assets to Excel
 */
export const exportAssetsToExcel = async (filters?: {
  companyId?: number;
  asOfDate?: string;
}): Promise<Blob> => {
  const response = await axios.get(`${API_BASE_URL}/bff/assets/export`, {
    params: filters,
    responseType: 'blob',
  });
  return response.data;
};

/**
 * Helper function to download Excel file
 */
export const downloadAssetsExcel = async (filters?: {
  companyId?: number;
  asOfDate?: string;
}): Promise<void> => {
  const blob = await exportAssetsToExcel(filters);
  const url = window.URL.createObjectURL(blob);
  const link = document.createElement('a');
  link.href = url;
  link.setAttribute('download', `assets-${new Date().toISOString()}.xlsx`);
  document.body.appendChild(link);
  link.click();
  link.remove();
  window.URL.revokeObjectURL(url);
};
