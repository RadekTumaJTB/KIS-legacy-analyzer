/**
 * AssetOverviewPage Component
 *
 * Dashboard displaying asset overview with calculations
 * Replaces legacy MajetekPrehledFiltr.jsp
 *
 * Features:
 * - Ownership percentage calculations
 * - Market value vs Book value comparison
 * - Unrealized gain/loss indicators
 * - Date-based filtering (as of date)
 * - Company-based filtering
 * - Aggregated totals
 */

import { useState, useEffect } from 'react';
import type { AssetOverview } from '../../../types/asset';
import * as assetApi from '../../../api/assetApi';
import { AssetOverviewFilters } from './AssetOverviewFilters';

interface FilterParams {
  asOfDate?: string;
  companyId?: number;
}

export const AssetOverviewPage: React.FC = () => {
  const [overview, setOverview] = useState<AssetOverview[]>([]);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const [filters, setFilters] = useState<FilterParams>({
    asOfDate: new Date().toISOString().split('T')[0],
    companyId: undefined,
  });

  useEffect(() => {
    loadOverview();
  }, [filters]);

  const loadOverview = async () => {
    setIsLoading(true);
    setError(null);
    try {
      const data = await assetApi.getAssetOverview(filters);
      setOverview(data);
    } catch (err) {
      setError('Chyba při načítání přehledu: ' + (err as Error).message);
    } finally {
      setIsLoading(false);
    }
  };

  const handleFiltersChange = (newFilters: FilterParams) => {
    setFilters(newFilters);
  };

  const handleRefresh = () => {
    loadOverview();
  };

  // Calculate totals
  const totals = overview.reduce(
    (acc, item) => ({
      totalShares: acc.totalShares + item.sharesOwned,
      totalMarketValue: acc.totalMarketValue + item.marketValue,
      totalBookValue: acc.totalBookValue + item.bookValue,
      totalUnrealizedGainLoss: acc.totalUnrealizedGainLoss + item.unrealizedGainLoss,
    }),
    { totalShares: 0, totalMarketValue: 0, totalBookValue: 0, totalUnrealizedGainLoss: 0 }
  );

  const formatCurrency = (amount: number, currency: string): string => {
    return `${amount.toLocaleString('cs-CZ', {
      minimumFractionDigits: 2,
      maximumFractionDigits: 2,
    })} ${currency}`;
  };

  const formatPercentage = (percentage: number): string => {
    return `${percentage.toLocaleString('cs-CZ', {
      minimumFractionDigits: 2,
      maximumFractionDigits: 2,
    })} %`;
  };

  const getGainLossColor = (gainLoss: number): string => {
    if (gainLoss > 0) return 'text-green-600';
    if (gainLoss < 0) return 'text-red-600';
    return 'text-gray-600';
  };

  const getGainLossBackground = (gainLoss: number): string => {
    if (gainLoss > 0) return 'bg-green-50';
    if (gainLoss < 0) return 'bg-red-50';
    return 'bg-gray-50';
  };

  return (
    <div className="container mx-auto px-4 py-8">
      {/* Header */}
      <div className="mb-6">
        <div className="flex items-center justify-between">
          <div>
            <h1 className="text-3xl font-bold text-gray-900">
              Přehled Majetkových Účastí
            </h1>
            <p className="text-gray-600 mt-2">
              Dashboard s výpočty vlastnických podílů a zhodnocení
            </p>
          </div>
          <button
            onClick={handleRefresh}
            className="px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 flex items-center gap-2"
            disabled={isLoading}
          >
            <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M4 4v5h.582m15.356 2A8.001 8.001 0 004.582 9m0 0H9m11 11v-5h-.581m0 0a8.003 8.003 0 01-15.357-2m15.357 2H15" />
            </svg>
            Obnovit
          </button>
        </div>
      </div>

      {/* Error Message */}
      {error && (
        <div className="mb-4 p-4 bg-red-100 border border-red-400 text-red-700 rounded">
          {error}
        </div>
      )}

      {/* Filters */}
      <AssetOverviewFilters
        filters={filters}
        onChange={handleFiltersChange}
      />

      {/* Summary Cards */}
      <div className="grid grid-cols-1 md:grid-cols-4 gap-4 mb-6">
        <div className="bg-white rounded-lg shadow p-6">
          <div className="text-sm text-gray-600 mb-1">Celkový počet účastí</div>
          <div className="text-2xl font-bold text-gray-900">{overview.length}</div>
        </div>

        <div className="bg-white rounded-lg shadow p-6">
          <div className="text-sm text-gray-600 mb-1">Tržní hodnota</div>
          <div className="text-2xl font-bold text-blue-600">
            {totals.totalMarketValue.toLocaleString('cs-CZ', { maximumFractionDigits: 0 })}
          </div>
        </div>

        <div className="bg-white rounded-lg shadow p-6">
          <div className="text-sm text-gray-600 mb-1">Účetní hodnota</div>
          <div className="text-2xl font-bold text-gray-600">
            {totals.totalBookValue.toLocaleString('cs-CZ', { maximumFractionDigits: 0 })}
          </div>
        </div>

        <div className={`bg-white rounded-lg shadow p-6 ${getGainLossBackground(totals.totalUnrealizedGainLoss)}`}>
          <div className="text-sm text-gray-600 mb-1">Nerealizovaný zisk/ztráta</div>
          <div className={`text-2xl font-bold ${getGainLossColor(totals.totalUnrealizedGainLoss)}`}>
            {totals.totalUnrealizedGainLoss > 0 ? '+' : ''}
            {totals.totalUnrealizedGainLoss.toLocaleString('cs-CZ', { maximumFractionDigits: 0 })}
          </div>
        </div>
      </div>

      {/* Overview Table */}
      <div className="bg-white rounded-lg shadow overflow-hidden">
        {isLoading ? (
          <div className="text-center py-12">
            <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600 mx-auto"></div>
            <p className="mt-4 text-gray-600">Načítání přehledu...</p>
          </div>
        ) : overview.length === 0 ? (
          <div className="text-center py-12 text-gray-500">
            <p className="text-lg">Žádné majetkové účasti</p>
            <p className="text-sm mt-2">Zkuste změnit filtr nebo přidat nové účasti</p>
          </div>
        ) : (
          <div className="overflow-x-auto">
            <table className="min-w-full divide-y divide-gray-200">
              <thead className="bg-gray-50">
                <tr>
                  <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Společnost
                  </th>
                  <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    ISIN
                  </th>
                  <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Účetní Společnost
                  </th>
                  <th className="px-4 py-3 text-right text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Vlastněné Akcie
                  </th>
                  <th className="px-4 py-3 text-right text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Celkem Akcií
                  </th>
                  <th className="px-4 py-3 text-right text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Vlastnický %
                  </th>
                  <th className="px-4 py-3 text-right text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Tržní Hodnota
                  </th>
                  <th className="px-4 py-3 text-right text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Účetní Hodnota
                  </th>
                  <th className="px-4 py-3 text-right text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Nerealizovaný Z/Z
                  </th>
                </tr>
              </thead>
              <tbody className="bg-white divide-y divide-gray-200">
                {overview.map((item) => (
                  <tr
                    key={item.emissionId}
                    className={`hover:bg-gray-50 ${getGainLossBackground(item.unrealizedGainLoss)}`}
                  >
                    <td className="px-4 py-3 text-sm font-medium text-gray-900">
                      {item.companyName}
                    </td>
                    <td className="px-4 py-3 text-sm font-mono text-gray-600">
                      {item.isinCode}
                    </td>
                    <td className="px-4 py-3 text-sm text-gray-600">
                      {item.accountingCompanyName}
                    </td>
                    <td className="px-4 py-3 text-sm text-right text-gray-900">
                      {item.sharesOwned.toLocaleString('cs-CZ')}
                    </td>
                    <td className="px-4 py-3 text-sm text-right text-gray-600">
                      {item.totalEmissionShares.toLocaleString('cs-CZ')}
                    </td>
                    <td className="px-4 py-3 text-sm text-right">
                      <span className="font-semibold text-blue-600">
                        {formatPercentage(item.ownershipPercentage)}
                      </span>
                    </td>
                    <td className="px-4 py-3 text-sm text-right text-gray-900">
                      {formatCurrency(item.marketValue, item.currency)}
                    </td>
                    <td className="px-4 py-3 text-sm text-right text-gray-600">
                      {formatCurrency(item.bookValue, item.currency)}
                    </td>
                    <td className="px-4 py-3 text-sm text-right">
                      <span className={`font-semibold ${getGainLossColor(item.unrealizedGainLoss)}`}>
                        {item.unrealizedGainLoss > 0 ? '+' : ''}
                        {formatCurrency(item.unrealizedGainLoss, item.currency)}
                      </span>
                    </td>
                  </tr>
                ))}
              </tbody>

              {/* Totals Footer */}
              {overview.length > 0 && (
                <tfoot className="bg-gray-50 border-t-2 border-gray-300">
                  <tr>
                    <td colSpan={3} className="px-4 py-3 text-sm font-semibold text-gray-900">
                      Celkem
                    </td>
                    <td className="px-4 py-3 text-sm text-right font-bold text-gray-900">
                      {totals.totalShares.toLocaleString('cs-CZ')}
                    </td>
                    <td colSpan={2}></td>
                    <td className="px-4 py-3 text-sm text-right font-bold text-blue-600">
                      {totals.totalMarketValue.toLocaleString('cs-CZ', {
                        minimumFractionDigits: 2,
                        maximumFractionDigits: 2,
                      })}
                    </td>
                    <td className="px-4 py-3 text-sm text-right font-bold text-gray-600">
                      {totals.totalBookValue.toLocaleString('cs-CZ', {
                        minimumFractionDigits: 2,
                        maximumFractionDigits: 2,
                      })}
                    </td>
                    <td className={`px-4 py-3 text-sm text-right font-bold ${getGainLossColor(totals.totalUnrealizedGainLoss)}`}>
                      {totals.totalUnrealizedGainLoss > 0 ? '+' : ''}
                      {totals.totalUnrealizedGainLoss.toLocaleString('cs-CZ', {
                        minimumFractionDigits: 2,
                        maximumFractionDigits: 2,
                      })}
                    </td>
                  </tr>
                </tfoot>
              )}
            </table>
          </div>
        )}
      </div>

      {/* Legend */}
      {overview.length > 0 && (
        <div className="mt-6 bg-blue-50 border border-blue-200 rounded-lg p-4">
          <h3 className="text-sm font-semibold text-blue-800 mb-2">Legenda:</h3>
          <div className="grid grid-cols-3 gap-4 text-sm text-blue-700">
            <div className="flex items-center gap-2">
              <div className="w-4 h-4 bg-green-50 border border-green-200 rounded"></div>
              <span>Nerealizovaný zisk (kladná hodnota)</span>
            </div>
            <div className="flex items-center gap-2">
              <div className="w-4 h-4 bg-red-50 border border-red-200 rounded"></div>
              <span>Nerealizovaná ztráta (záporná hodnota)</span>
            </div>
            <div className="flex items-center gap-2">
              <div className="w-4 h-4 bg-gray-50 border border-gray-200 rounded"></div>
              <span>Bez změny (nulová hodnota)</span>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};
