/**
 * EmissionHistoryModal Component
 *
 * Display version history for emission items
 * Shows all historical changes with temporal validity
 */

import { useState, useEffect } from 'react';
import type { EmissionItem } from '../../../types/emission';
import * as emissionApi from '../../../api/emissionApi';

interface EmissionHistoryModalProps {
  isOpen: boolean;
  onClose: () => void;
  financialInvestmentId: number | null;
  companyName?: string;
}

export const EmissionHistoryModal: React.FC<EmissionHistoryModalProps> = ({
  isOpen,
  onClose,
  financialInvestmentId,
  companyName,
}) => {
  const [history, setHistory] = useState<EmissionItem[]>([]);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    if (isOpen && financialInvestmentId) {
      loadHistory();
    }
  }, [isOpen, financialInvestmentId]);

  const loadHistory = async () => {
    if (!financialInvestmentId) return;

    setIsLoading(true);
    setError(null);

    try {
      const data = await emissionApi.getEmissionHistory(financialInvestmentId);
      setHistory(data);
    } catch (err) {
      setError('Chyba při načítání historie: ' + (err as Error).message);
    } finally {
      setIsLoading(false);
    }
  };

  if (!isOpen) return null;

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
      <div className="bg-white rounded-lg shadow-xl max-w-6xl w-full mx-4 max-h-[90vh] flex flex-col">
        {/* Header */}
        <div className="flex items-center justify-between p-6 border-b">
          <div>
            <h2 className="text-2xl font-bold text-gray-900">
              Historie Emisí
            </h2>
            {companyName && (
              <p className="text-sm text-gray-600 mt-1">{companyName}</p>
            )}
          </div>
          <button
            onClick={onClose}
            className="text-gray-400 hover:text-gray-600"
          >
            <svg className="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M6 18L18 6M6 6l12 12" />
            </svg>
          </button>
        </div>

        {/* Content */}
        <div className="flex-1 overflow-auto p-6">
          {error && (
            <div className="mb-4 p-4 bg-red-100 border border-red-400 text-red-700 rounded">
              {error}
            </div>
          )}

          {isLoading ? (
            <div className="text-center py-12">
              <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600 mx-auto"></div>
              <p className="mt-4 text-gray-600">Načítání historie...</p>
            </div>
          ) : history.length === 0 ? (
            <div className="text-center py-12 text-gray-500">
              <p className="text-lg">Žádná historie</p>
            </div>
          ) : (
            <div className="overflow-x-auto">
              <table className="min-w-full divide-y divide-gray-200">
                <thead className="bg-gray-50">
                  <tr>
                    <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      ID
                    </th>
                    <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Platnost Od
                    </th>
                    <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Platnost Do
                    </th>
                    <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Počet Kusů
                    </th>
                    <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Nominální Hodnota
                    </th>
                    <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Objem
                    </th>
                    <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Základní Jmění
                    </th>
                    <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Investice
                    </th>
                    <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Status
                    </th>
                  </tr>
                </thead>
                <tbody className="bg-white divide-y divide-gray-200">
                  {history.map((item, index) => {
                    const isActive = !item.validTo || new Date(item.validTo) > new Date();
                    const isFuture = new Date(item.validFrom) > new Date();

                    return (
                      <tr
                        key={item.id}
                        className={`
                          ${isActive && !isFuture ? 'bg-green-50' : ''}
                          ${isFuture ? 'bg-blue-50' : ''}
                          ${!isActive && !isFuture ? 'bg-gray-50 opacity-75' : ''}
                        `}
                      >
                        <td className="px-4 py-3 text-sm text-gray-900">
                          {item.id}
                        </td>
                        <td className="px-4 py-3 text-sm text-gray-900">
                          {new Date(item.validFrom).toLocaleDateString('cs-CZ')}
                        </td>
                        <td className="px-4 py-3 text-sm text-gray-900">
                          {item.validTo ? new Date(item.validTo).toLocaleDateString('cs-CZ') : '-'}
                        </td>
                        <td className="px-4 py-3 text-sm text-right text-gray-900">
                          {item.numberOfShares.toLocaleString('cs-CZ')}
                        </td>
                        <td className="px-4 py-3 text-sm text-right text-gray-900">
                          {item.nominalValue.toLocaleString('cs-CZ', {
                            minimumFractionDigits: 2,
                            maximumFractionDigits: 2,
                          })}
                        </td>
                        <td className="px-4 py-3 text-sm text-right font-mono text-gray-900">
                          {item.volume.toLocaleString('cs-CZ', {
                            minimumFractionDigits: 2,
                            maximumFractionDigits: 2,
                          })}
                        </td>
                        <td className="px-4 py-3 text-sm text-right text-gray-900">
                          {item.registeredCapital.toLocaleString('cs-CZ', {
                            minimumFractionDigits: 2,
                            maximumFractionDigits: 2,
                          })}
                        </td>
                        <td className="px-4 py-3 text-sm text-center">
                          {item.investmentFlag ? (
                            <span className="text-green-600">✓</span>
                          ) : (
                            <span className="text-gray-400">-</span>
                          )}
                        </td>
                        <td className="px-4 py-3 text-sm">
                          {isActive && !isFuture && (
                            <span className="px-2 py-1 text-xs font-semibold text-green-800 bg-green-100 rounded">
                              Aktivní
                            </span>
                          )}
                          {isFuture && (
                            <span className="px-2 py-1 text-xs font-semibold text-blue-800 bg-blue-100 rounded">
                              Budoucí
                            </span>
                          )}
                          {!isActive && !isFuture && (
                            <span className="px-2 py-1 text-xs font-semibold text-gray-600 bg-gray-200 rounded">
                              Historické
                            </span>
                          )}
                        </td>
                      </tr>
                    );
                  })}
                </tbody>
              </table>
            </div>
          )}

          {/* Legend */}
          {history.length > 0 && (
            <div className="mt-6 p-4 bg-gray-50 rounded-lg">
              <h3 className="text-sm font-semibold text-gray-700 mb-2">Legenda:</h3>
              <div className="grid grid-cols-3 gap-4 text-sm">
                <div className="flex items-center gap-2">
                  <div className="w-4 h-4 bg-green-50 border border-green-200 rounded"></div>
                  <span>Aktivní (platné dnes)</span>
                </div>
                <div className="flex items-center gap-2">
                  <div className="w-4 h-4 bg-blue-50 border border-blue-200 rounded"></div>
                  <span>Budoucí (platné od budoucna)</span>
                </div>
                <div className="flex items-center gap-2">
                  <div className="w-4 h-4 bg-gray-50 border border-gray-200 rounded"></div>
                  <span>Historické (již neplatné)</span>
                </div>
              </div>
            </div>
          )}
        </div>

        {/* Footer */}
        <div className="flex justify-end gap-3 px-6 py-4 bg-gray-50 border-t rounded-b-lg">
          <button
            onClick={onClose}
            className="px-4 py-2 text-white bg-blue-600 rounded-md hover:bg-blue-700"
          >
            Zavřít
          </button>
        </div>
      </div>
    </div>
  );
};
