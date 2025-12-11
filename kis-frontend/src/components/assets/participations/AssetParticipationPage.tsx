/**
 * AssetParticipationPage Component
 *
 * Main CRUD container for managing Equity Stakes (Majetkové Účasti)
 * Replaces legacy MajetekPrehled.jsp
 *
 * Features:
 * - List all equity stakes for a company
 * - Create new participations
 * - Edit existing participations
 * - Delete participations
 * - Dual currency display (transaction + accounting)
 * - Temporal validity filtering
 */

import { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import type { EquityStake } from '../../../types/asset';
import * as assetApi from '../../../api/assetApi';
import { NewParticipationModal } from './NewParticipationModal';
import { EditParticipationModal } from './EditParticipationModal';

export const AssetParticipationPage: React.FC = () => {
  const { companyId } = useParams<{ companyId: string }>();
  const navigate = useNavigate();

  const [stakes, setStakes] = useState<EquityStake[]>([]);
  const [filteredStakes, setFilteredStakes] = useState<EquityStake[]>([]);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [successMessage, setSuccessMessage] = useState<string | null>(null);

  // Modals
  const [isNewModalOpen, setIsNewModalOpen] = useState(false);
  const [isEditModalOpen, setIsEditModalOpen] = useState(false);
  const [selectedStake, setSelectedStake] = useState<EquityStake | null>(null);

  // Filters
  const [showActiveOnly, setShowActiveOnly] = useState(true);
  const [dateFilter, setDateFilter] = useState<string>(new Date().toISOString().split('T')[0]);
  const [searchTerm, setSearchTerm] = useState('');

  useEffect(() => {
    if (companyId) {
      loadStakes();
    }
  }, [companyId]);

  useEffect(() => {
    applyFilters();
  }, [stakes, showActiveOnly, dateFilter, searchTerm]);

  const loadStakes = async () => {
    if (!companyId) return;

    setIsLoading(true);
    setError(null);
    try {
      const data = await assetApi.getEquityStakesForCompany(parseInt(companyId));
      setStakes(data);
    } catch (err) {
      setError('Chyba při načítání majetkových účastí: ' + (err as Error).message);
    } finally {
      setIsLoading(false);
    }
  };

  const applyFilters = () => {
    let filtered = [...stakes];

    // Filter by active status (based on validFrom/validTo)
    if (showActiveOnly) {
      const filterDate = new Date(dateFilter);
      filtered = filtered.filter(stake => {
        const validFrom = new Date(stake.validFrom);
        const validTo = stake.validTo ? new Date(stake.validTo) : null;

        return validFrom <= filterDate && (!validTo || validTo >= filterDate);
      });
    }

    // Search filter
    if (searchTerm) {
      filtered = filtered.filter(stake =>
        stake.accountNumber.toLowerCase().includes(searchTerm.toLowerCase()) ||
        stake.transactionTypeName.toLowerCase().includes(searchTerm.toLowerCase()) ||
        stake.methodName.toLowerCase().includes(searchTerm.toLowerCase())
      );
    }

    setFilteredStakes(filtered);
  };

  const handleCreateSuccess = async (newStake: EquityStake) => {
    setSuccessMessage('Majetková účast byla úspěšně vytvořena');
    await loadStakes();
    setTimeout(() => setSuccessMessage(null), 3000);
  };

  const handleEditClick = (stake: EquityStake) => {
    setSelectedStake(stake);
    setIsEditModalOpen(true);
  };

  const handleEditSuccess = async (updatedStake: EquityStake) => {
    setSuccessMessage('Majetková účast byla úspěšně aktualizována');
    await loadStakes();
    setTimeout(() => setSuccessMessage(null), 3000);
  };

  const handleDeleteClick = async (stake: EquityStake) => {
    if (!confirm(`Opravdu chcete smazat majetkovou účast pro účet ${stake.accountNumber}?`)) {
      return;
    }

    try {
      await assetApi.deleteEquityStake(stake.id);
      setSuccessMessage('Majetková účast byla úspěšně smazána');
      await loadStakes();
      setTimeout(() => setSuccessMessage(null), 3000);
    } catch (err) {
      setError('Chyba při mazání: ' + (err as Error).message);
    }
  };

  const formatCurrency = (amount: number, currency: string): string => {
    return `${amount.toLocaleString('cs-CZ', {
      minimumFractionDigits: 2,
      maximumFractionDigits: 2,
    })} ${currency}`;
  };

  const formatDate = (dateString: string): string => {
    return new Date(dateString).toLocaleDateString('cs-CZ');
  };

  // Calculate totals
  const totalTransactionAmount = filteredStakes.reduce(
    (sum, stake) => sum + stake.totalTransactionAmount,
    0
  );
  const totalAccountingAmount = filteredStakes.reduce(
    (sum, stake) => sum + stake.totalAccountingAmount,
    0
  );
  const totalShares = filteredStakes.reduce(
    (sum, stake) => sum + stake.numberOfShares,
    0
  );

  return (
    <div className="container mx-auto px-4 py-8">
      {/* Header */}
      <div className="mb-6">
        <div className="flex items-center gap-4">
          <button
            onClick={() => navigate('/assets/companies')}
            className="px-4 py-2 text-gray-700 bg-white border border-gray-300 rounded-md hover:bg-gray-50"
          >
            ← Zpět na výběr společnosti
          </button>
          <div>
            <h1 className="text-3xl font-bold text-gray-900">
              Majetkové Účasti
            </h1>
            <p className="text-gray-600 mt-1">
              Správa majetkových účastí pro společnost
            </p>
          </div>
        </div>
      </div>

      {/* Messages */}
      {error && (
        <div className="mb-4 p-4 bg-red-100 border border-red-400 text-red-700 rounded">
          {error}
        </div>
      )}
      {successMessage && (
        <div className="mb-4 p-4 bg-green-100 border border-green-400 text-green-700 rounded">
          {successMessage}
        </div>
      )}

      {/* Filters and Actions */}
      <div className="bg-white rounded-lg shadow mb-6 p-6">
        <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
          {/* Search */}
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-2">
              Vyhledávání
            </label>
            <input
              type="text"
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              placeholder="Účet, typ, metoda..."
              className="w-full px-3 py-2 border border-gray-300 rounded-md focus:ring-2 focus:ring-blue-500"
            />
          </div>

          {/* Date Filter */}
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-2">
              Stav k datu
            </label>
            <input
              type="date"
              value={dateFilter}
              onChange={(e) => setDateFilter(e.target.value)}
              className="w-full px-3 py-2 border border-gray-300 rounded-md focus:ring-2 focus:ring-blue-500"
            />
          </div>

          {/* Active Only Checkbox */}
          <div className="flex items-end">
            <label className="flex items-center gap-2">
              <input
                type="checkbox"
                checked={showActiveOnly}
                onChange={(e) => setShowActiveOnly(e.target.checked)}
                className="w-4 h-4 text-blue-600 border-gray-300 rounded focus:ring-blue-500"
              />
              <span className="text-sm text-gray-700">Pouze aktivní</span>
            </label>
          </div>

          {/* New Button */}
          <div className="flex items-end">
            <button
              onClick={() => setIsNewModalOpen(true)}
              className="w-full px-4 py-2 bg-green-600 text-white rounded-md hover:bg-green-700"
            >
              + Nová účast
            </button>
          </div>
        </div>

        {/* Results count */}
        <div className="mt-4 text-sm text-gray-600">
          Zobrazeno: <strong>{filteredStakes.length}</strong> z <strong>{stakes.length}</strong> účastí
        </div>
      </div>

      {/* Stakes Table */}
      <div className="bg-white rounded-lg shadow overflow-hidden">
        {isLoading ? (
          <div className="text-center py-12">
            <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600 mx-auto"></div>
            <p className="mt-4 text-gray-600">Načítání majetkových účastí...</p>
          </div>
        ) : filteredStakes.length === 0 ? (
          <div className="text-center py-12 text-gray-500">
            <p className="text-lg">
              {stakes.length === 0 ? 'Žádné majetkové účasti' : 'Žádné účasti neodpovídají filtrům'}
            </p>
            {stakes.length === 0 && (
              <button
                onClick={() => setIsNewModalOpen(true)}
                className="mt-4 px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700"
              >
                Přidat první účast
              </button>
            )}
          </div>
        ) : (
          <div className="overflow-x-auto">
            <table className="min-w-full divide-y divide-gray-200">
              <thead className="bg-gray-50">
                <tr>
                  <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">
                    Účet
                  </th>
                  <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">
                    Platnost
                  </th>
                  <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">
                    Typ / Metoda
                  </th>
                  <th className="px-4 py-3 text-right text-xs font-medium text-gray-500 uppercase">
                    Počet Akcií
                  </th>
                  <th className="px-4 py-3 text-right text-xs font-medium text-gray-500 uppercase">
                    Transakční Měna
                  </th>
                  <th className="px-4 py-3 text-right text-xs font-medium text-gray-500 uppercase">
                    Účetní Měna
                  </th>
                  <th className="px-4 py-3 text-center text-xs font-medium text-gray-500 uppercase">
                    Kurz
                  </th>
                  <th className="px-4 py-3 text-center text-xs font-medium text-gray-500 uppercase">
                    Akce
                  </th>
                </tr>
              </thead>
              <tbody className="bg-white divide-y divide-gray-200">
                {filteredStakes.map((stake) => {
                  const isActive = !stake.validTo || new Date(stake.validTo) >= new Date();

                  return (
                    <tr
                      key={stake.id}
                      className={`hover:bg-gray-50 ${!isActive ? 'bg-gray-50 opacity-75' : ''}`}
                    >
                      <td className="px-4 py-3 text-sm font-mono text-gray-900">
                        {stake.accountNumber}
                      </td>
                      <td className="px-4 py-3 text-sm text-gray-600">
                        <div>{formatDate(stake.validFrom)}</div>
                        {stake.validTo && (
                          <div className="text-xs text-gray-500">
                            do {formatDate(stake.validTo)}
                          </div>
                        )}
                      </td>
                      <td className="px-4 py-3 text-sm text-gray-900">
                        <div className="font-medium">{stake.transactionTypeName}</div>
                        <div className="text-xs text-gray-500">{stake.methodName}</div>
                      </td>
                      <td className="px-4 py-3 text-sm text-right text-gray-900">
                        {stake.numberOfShares.toLocaleString('cs-CZ')}
                      </td>
                      <td className="px-4 py-3 text-sm text-right">
                        <div className="font-medium text-gray-900">
                          {formatCurrency(stake.totalTransactionAmount, stake.transactionCurrency)}
                        </div>
                        <div className="text-xs text-gray-500">
                          {stake.pricePerShareTransaction.toLocaleString('cs-CZ', {
                            minimumFractionDigits: 2,
                            maximumFractionDigits: 2,
                          })} / akcie
                        </div>
                      </td>
                      <td className="px-4 py-3 text-sm text-right">
                        <div className="font-medium text-gray-900">
                          {formatCurrency(stake.totalAccountingAmount, stake.accountingCurrency)}
                        </div>
                        <div className="text-xs text-gray-500">
                          {stake.pricePerShareAccounting.toLocaleString('cs-CZ', {
                            minimumFractionDigits: 2,
                            maximumFractionDigits: 2,
                          })} / akcie
                        </div>
                      </td>
                      <td className="px-4 py-3 text-sm text-center text-gray-900">
                        {stake.exchangeRate.toLocaleString('cs-CZ', {
                          minimumFractionDigits: 4,
                          maximumFractionDigits: 4,
                        })}
                      </td>
                      <td className="px-4 py-3 text-center">
                        <div className="flex items-center justify-center gap-2">
                          <button
                            onClick={() => handleEditClick(stake)}
                            className="px-3 py-1 text-sm text-white bg-blue-600 rounded hover:bg-blue-700"
                          >
                            Upravit
                          </button>
                          <button
                            onClick={() => handleDeleteClick(stake)}
                            className="px-3 py-1 text-sm text-white bg-red-600 rounded hover:bg-red-700"
                          >
                            Smazat
                          </button>
                        </div>
                      </td>
                    </tr>
                  );
                })}
              </tbody>

              {/* Totals Footer */}
              <tfoot className="bg-gray-50 border-t-2 border-gray-300">
                <tr>
                  <td colSpan={3} className="px-4 py-3 text-sm font-semibold text-gray-900">
                    Celkem
                  </td>
                  <td className="px-4 py-3 text-sm text-right font-bold text-gray-900">
                    {totalShares.toLocaleString('cs-CZ')}
                  </td>
                  <td className="px-4 py-3 text-sm text-right font-bold text-gray-900">
                    {totalTransactionAmount.toLocaleString('cs-CZ', {
                      minimumFractionDigits: 2,
                      maximumFractionDigits: 2,
                    })}
                  </td>
                  <td className="px-4 py-3 text-sm text-right font-bold text-gray-900">
                    {totalAccountingAmount.toLocaleString('cs-CZ', {
                      minimumFractionDigits: 2,
                      maximumFractionDigits: 2,
                    })}
                  </td>
                  <td colSpan={2}></td>
                </tr>
              </tfoot>
            </table>
          </div>
        )}
      </div>

      {/* Modals */}
      {companyId && (
        <>
          <NewParticipationModal
            isOpen={isNewModalOpen}
            onClose={() => setIsNewModalOpen(false)}
            onSuccess={handleCreateSuccess}
            companyId={parseInt(companyId)}
          />

          {selectedStake && (
            <EditParticipationModal
              isOpen={isEditModalOpen}
              onClose={() => {
                setIsEditModalOpen(false);
                setSelectedStake(null);
              }}
              onSuccess={handleEditSuccess}
              stake={selectedStake}
            />
          )}
        </>
      )}
    </div>
  );
};
