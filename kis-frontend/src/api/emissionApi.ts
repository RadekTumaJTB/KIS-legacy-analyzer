/**
 * API client for Emission module (Finanční Investice a Emise)
 * Communicates with BFF endpoints at /bff/emissions
 */

import axios from 'axios';
import type { FinancialInvestment, EmissionWithItems, EmissionItem } from '../types/emission';

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8081';

/**
 * Get list of all financial investments with their current emissions
 */
export const getAllEmissions = async (): Promise<EmissionWithItems[]> => {
  const response = await axios.get<EmissionWithItems[]>(`${API_BASE_URL}/bff/emissions`);
  return response.data;
};

/**
 * Get single emission with items by financial investment ID
 * Includes all history (all versions)
 */
export const getEmissionById = async (id: number): Promise<EmissionWithItems> => {
  const response = await axios.get<EmissionWithItems>(`${API_BASE_URL}/bff/emissions/${id}`);
  return response.data;
};

/**
 * Create new financial investment
 */
export const createFinancialInvestment = async (
  data: Partial<FinancialInvestment>
): Promise<FinancialInvestment> => {
  const response = await axios.post<FinancialInvestment>(
    `${API_BASE_URL}/bff/emissions`,
    data
  );
  return response.data;
};

/**
 * Update financial investment metadata
 */
export const updateFinancialInvestment = async (
  id: number,
  data: Partial<FinancialInvestment>
): Promise<FinancialInvestment> => {
  const response = await axios.put<FinancialInvestment>(
    `${API_BASE_URL}/bff/emissions/${id}`,
    data
  );
  return response.data;
};

/**
 * Delete financial investment (and all its emissions via cascade)
 */
export const deleteFinancialInvestment = async (id: number): Promise<void> => {
  await axios.delete(`${API_BASE_URL}/bff/emissions/${id}`);
};

/**
 * Batch update emission items (Insert/Update/Delete operations)
 * This is the key method for inline editing grid
 */
export const batchUpdateEmissionItems = async (
  financialInvestmentId: number,
  items: EmissionItem[]
): Promise<EmissionWithItems> => {
  const response = await axios.post<EmissionWithItems>(
    `${API_BASE_URL}/bff/emissions/${financialInvestmentId}/items`,
    items
  );
  return response.data;
};

/**
 * Get version history for a financial investment
 */
export const getEmissionHistory = async (id: number): Promise<EmissionItem[]> => {
  const response = await axios.get<EmissionItem[]>(
    `${API_BASE_URL}/bff/emissions/${id}/history`
  );
  return response.data;
};

/**
 * Export emissions to Excel
 */
export const exportEmissionsToExcel = async (filters?: {
  companyId?: number;
  currency?: string;
  dateFrom?: string;
  dateTo?: string;
}): Promise<Blob> => {
  const response = await axios.get(`${API_BASE_URL}/bff/emissions/export`, {
    params: filters,
    responseType: 'blob',
  });
  return response.data;
};

/**
 * Helper function to download Excel file
 */
export const downloadEmissionsExcel = async (filters?: {
  companyId?: number;
  currency?: string;
  dateFrom?: string;
  dateTo?: string;
}): Promise<void> => {
  const blob = await exportEmissionsToExcel(filters);
  const url = window.URL.createObjectURL(blob);
  const link = document.createElement('a');
  link.href = url;
  link.setAttribute('download', `emissions-${new Date().toISOString()}.xlsx`);
  document.body.appendChild(link);
  link.click();
  link.remove();
  window.URL.revokeObjectURL(url);
};
