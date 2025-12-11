/**
 * AssetOverviewFilters Component
 *
 * Filter panel for Asset Overview page
 * Provides date and company filters
 */

import { useState, useEffect } from 'react';
import * as referenceApi from '../../../api/referenceApi';

interface FilterParams {
  asOfDate?: string;
  companyId?: number;
}

interface AssetOverviewFiltersProps {
  filters: FilterParams;
  onChange: (filters: FilterParams) => void;
}

export const AssetOverviewFilters: React.FC<AssetOverviewFiltersProps> = ({
  filters,
  onChange,
}) => {
  const [localFilters, setLocalFilters] = useState<FilterParams>(filters);
  const [companies, setCompanies] = useState<Array<{ id: number; name: string }>>([]);
  const [isLoadingCompanies, setIsLoadingCompanies] = useState(false);

  useEffect(() => {
    loadCompanies();
  }, []);

  useEffect(() => {
    setLocalFilters(filters);
  }, [filters]);

  const loadCompanies = async () => {
    setIsLoadingCompanies(true);
    try {
      const data = await referenceApi.getCompanies();
      setCompanies(data);
    } catch (err) {
      console.error('Chyba při načítání společností:', err);
    } finally {
      setIsLoadingCompanies(false);
    }
  };

  const handleFieldChange = (field: keyof FilterParams, value: any) => {
    const newFilters = { ...localFilters, [field]: value };
    setLocalFilters(newFilters);
  };

  const handleApply = () => {
    onChange(localFilters);
  };

  const handleReset = () => {
    const defaultFilters: FilterParams = {
      asOfDate: new Date().toISOString().split('T')[0],
      companyId: undefined,
    };
    setLocalFilters(defaultFilters);
    onChange(defaultFilters);
  };

  return (
    <div className="bg-white rounded-lg shadow mb-6 p-6">
      <h2 className="text-lg font-semibold text-gray-900 mb-4">Filtry</h2>

      <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
        {/* As of Date Filter */}
        <div>
          <label className="block text-sm font-medium text-gray-700 mb-2">
            Stav k datu
          </label>
          <input
            type="date"
            value={localFilters.asOfDate || ''}
            onChange={(e) => handleFieldChange('asOfDate', e.target.value)}
            className="w-full px-3 py-2 border border-gray-300 rounded-md focus:ring-2 focus:ring-blue-500"
          />
          <p className="mt-1 text-xs text-gray-500">
            Zobrazit stav majetkových účastí k tomuto datu
          </p>
        </div>

        {/* Company Filter */}
        <div>
          <label className="block text-sm font-medium text-gray-700 mb-2">
            Společnost (volitelné)
          </label>
          <select
            value={localFilters.companyId || 0}
            onChange={(e) => handleFieldChange('companyId', parseInt(e.target.value) || undefined)}
            className="w-full px-3 py-2 border border-gray-300 rounded-md focus:ring-2 focus:ring-blue-500"
            disabled={isLoadingCompanies}
          >
            <option value={0}>-- Všechny společnosti --</option>
            {companies.map(company => (
              <option key={company.id} value={company.id}>
                {company.name}
              </option>
            ))}
          </select>
          <p className="mt-1 text-xs text-gray-500">
            Filtrovat podle účetní společnosti
          </p>
        </div>

        {/* Action Buttons */}
        <div className="flex items-end gap-2">
          <button
            onClick={handleApply}
            className="flex-1 px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700"
          >
            Použít
          </button>
          <button
            onClick={handleReset}
            className="flex-1 px-4 py-2 bg-gray-600 text-white rounded-md hover:bg-gray-700"
          >
            Reset
          </button>
        </div>
      </div>

      {/* Current Filters Display */}
      <div className="mt-4 pt-4 border-t border-gray-200">
        <h3 className="text-sm font-semibold text-gray-700 mb-2">Aktivní filtry:</h3>
        <div className="flex flex-wrap gap-2">
          {localFilters.asOfDate && (
            <span className="px-3 py-1 text-sm bg-blue-100 text-blue-800 rounded-full">
              Stav k: {new Date(localFilters.asOfDate).toLocaleDateString('cs-CZ')}
            </span>
          )}
          {localFilters.companyId && (
            <span className="px-3 py-1 text-sm bg-green-100 text-green-800 rounded-full flex items-center gap-2">
              Společnost: {companies.find(c => c.id === localFilters.companyId)?.name || localFilters.companyId}
              <button
                onClick={() => handleFieldChange('companyId', undefined)}
                className="text-green-600 hover:text-green-800"
              >
                ×
              </button>
            </span>
          )}
          {!localFilters.asOfDate && !localFilters.companyId && (
            <span className="text-sm text-gray-500">Žádné filtry nejsou aktivní</span>
          )}
        </div>
      </div>
    </div>
  );
};
