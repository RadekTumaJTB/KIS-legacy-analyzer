/**
 * EmissionExportModal Component
 *
 * Configure and download Excel export for emissions
 */

import { useState } from 'react';
import { downloadEmissionsExcel } from '../../../api/emissionApi';

interface EmissionExportModalProps {
  isOpen: boolean;
  onClose: () => void;
}

export const EmissionExportModal: React.FC<EmissionExportModalProps> = ({
  isOpen,
  onClose,
}) => {
  const [filters, setFilters] = useState({
    companyId: undefined as number | undefined,
    currency: undefined as string | undefined,
    dateFrom: '',
    dateTo: '',
  });

  const [isExporting, setIsExporting] = useState(false);
  const [error, setError] = useState<string | null>(null);

  if (!isOpen) return null;

  const handleExport = async () => {
    setError(null);
    setIsExporting(true);

    try {
      // Clean up filters (remove empty values)
      const cleanFilters: any = {};
      if (filters.companyId) cleanFilters.companyId = filters.companyId;
      if (filters.currency) cleanFilters.currency = filters.currency;
      if (filters.dateFrom) cleanFilters.dateFrom = filters.dateFrom;
      if (filters.dateTo) cleanFilters.dateTo = filters.dateTo;

      await downloadEmissionsExcel(cleanFilters);

      // Close modal after successful export
      setTimeout(() => {
        handleClose();
      }, 1000);
    } catch (err) {
      setError('Chyba při exportu: ' + (err as Error).message);
    } finally {
      setIsExporting(false);
    }
  };

  const handleClose = () => {
    setFilters({
      companyId: undefined,
      currency: undefined,
      dateFrom: '',
      dateTo: '',
    });
    setError(null);
    onClose();
  };

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
      <div className="bg-white rounded-lg shadow-xl max-w-2xl w-full mx-4">
        {/* Header */}
        <div className="flex items-center justify-between p-6 border-b">
          <h2 className="text-2xl font-bold text-gray-900">
            Export Emisí do Excelu
          </h2>
          <button
            onClick={handleClose}
            className="text-gray-400 hover:text-gray-600"
          >
            <svg className="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M6 18L18 6M6 6l12 12" />
            </svg>
          </button>
        </div>

        {/* Form */}
        <div className="p-6 space-y-4">
          {error && (
            <div className="p-4 bg-red-100 border border-red-400 text-red-700 rounded">
              {error}
            </div>
          )}

          <div className="bg-blue-50 border border-blue-200 rounded-lg p-4">
            <p className="text-sm text-blue-800">
              <strong>Volitelné filtry:</strong> Můžete exportovat všechny emise nebo filtrovat podle společnosti, měny a data.
              Pokud nevyberete žádné filtry, exportují se všechny záznamy.
            </p>
          </div>

          {/* Company Filter */}
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-2">
              Společnost (volitelné)
            </label>
            <select
              value={filters.companyId || ''}
              onChange={(e) => setFilters({ ...filters, companyId: e.target.value ? parseInt(e.target.value) : undefined })}
              className="w-full px-3 py-2 border border-gray-300 rounded-md focus:ring-2 focus:ring-blue-500"
            >
              <option value="">-- Všechny společnosti --</option>
              {/* TODO: Load companies from reference API */}
              <option value={1}>J&T BANKA, a.s.</option>
              <option value={2}>J&T INVESTIČNÍ SPOLEČNOST, a.s.</option>
              <option value={3}>J&T ASSET MANAGEMENT, INVESTIČNÍ SPOLEČNOST, a.s.</option>
            </select>
          </div>

          {/* Currency Filter */}
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-2">
              Měna (volitelné)
            </label>
            <select
              value={filters.currency || ''}
              onChange={(e) => setFilters({ ...filters, currency: e.target.value || undefined })}
              className="w-full px-3 py-2 border border-gray-300 rounded-md focus:ring-2 focus:ring-blue-500"
            >
              <option value="">-- Všechny měny --</option>
              <option value="CZK">CZK - Česká koruna</option>
              <option value="EUR">EUR - Euro</option>
              <option value="USD">USD - Americký dolar</option>
              <option value="GBP">GBP - Britská libra</option>
            </select>
          </div>

          {/* Date Range Filter */}
          <div className="grid grid-cols-2 gap-4">
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">
                Platnost od (volitelné)
              </label>
              <input
                type="date"
                value={filters.dateFrom}
                onChange={(e) => setFilters({ ...filters, dateFrom: e.target.value })}
                className="w-full px-3 py-2 border border-gray-300 rounded-md focus:ring-2 focus:ring-blue-500"
              />
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">
                Platnost do (volitelné)
              </label>
              <input
                type="date"
                value={filters.dateTo}
                onChange={(e) => setFilters({ ...filters, dateTo: e.target.value })}
                className="w-full px-3 py-2 border border-gray-300 rounded-md focus:ring-2 focus:ring-blue-500"
              />
            </div>
          </div>

          {/* Export info */}
          <div className="bg-gray-50 border border-gray-200 rounded-lg p-4">
            <h3 className="text-sm font-semibold text-gray-700 mb-2">
              Export bude obsahovat:
            </h3>
            <ul className="text-sm text-gray-600 space-y-1">
              <li>• Finanční investice (společnost, měna, ISIN)</li>
              <li>• Emise (počet kusů, nominální hodnota, objem)</li>
              <li>• Platnost (od, do)</li>
              <li>• Základní jmění</li>
              <li>• Příznak investice</li>
            </ul>
          </div>
        </div>

        {/* Footer */}
        <div className="flex justify-end gap-3 px-6 py-4 bg-gray-50 rounded-b-lg">
          <button
            onClick={handleClose}
            className="px-4 py-2 text-gray-700 bg-white border border-gray-300 rounded-md hover:bg-gray-50"
            disabled={isExporting}
          >
            Zrušit
          </button>
          <button
            onClick={handleExport}
            className="px-4 py-2 text-white bg-green-600 rounded-md hover:bg-green-700 disabled:bg-gray-400 flex items-center gap-2"
            disabled={isExporting}
          >
            {isExporting && (
              <div className="animate-spin rounded-full h-4 w-4 border-b-2 border-white"></div>
            )}
            <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 10v6m0 0l-3-3m3 3l3-3m2 8H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z" />
            </svg>
            {isExporting ? 'Exportuji...' : 'Exportovat do Excelu'}
          </button>
        </div>
      </div>
    </div>
  );
};
