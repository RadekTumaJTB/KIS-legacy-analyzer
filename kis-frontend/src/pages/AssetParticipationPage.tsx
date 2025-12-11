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
import type { EquityStake } from '../types/asset';
import * as assetApi from '../api/assetApi';
import { Button } from '../components/ui/Button';
import { Input } from '../components/ui/Input';
import { NewParticipationModal } from '../components/assets/participations/NewParticipationModal';
import { EditParticipationModal } from '../components/assets/participations/EditParticipationModal';
import { formatCurrency, formatDate } from '../lib/utils';
import './AssetParticipationPage.css';

export default function AssetParticipationPage() {
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

  if (isLoading) {
    return (
      <div className="asset-participation-page loading">
        <div className="spinner"></div>
        <p>Načítám majetkové účasti...</p>
      </div>
    );
  }

  return (
    <div className="asset-participation-page">
      {/* Header with Back Button */}
      <div className="page-header-with-back">
        <button
          onClick={() => navigate('/assets/companies')}
          className="back-button"
        >
          ← Zpět na výběr společnosti
        </button>
        <div>
          <h1>Majetkové Účasti</h1>
          <p className="text-gray-600">
            Správa majetkových účastí pro společnost
          </p>
        </div>
      </div>

      {/* Messages */}
      {error && (
        <div className="alert alert-error">
          {error}
        </div>
      )}
      {successMessage && (
        <div className="alert alert-success">
          {successMessage}
        </div>
      )}

      {/* Filters and Actions */}
      <div className="filter-panel">
        <div className="filter-grid">
          {/* Search */}
          <div className="filter-field">
            <label>Vyhledávání</label>
            <Input
              type="text"
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              placeholder="Účet, typ, metoda..."
            />
          </div>

          {/* Date Filter */}
          <div className="filter-field">
            <label>Stav k datu</label>
            <Input
              type="date"
              value={dateFilter}
              onChange={(e) => setDateFilter(e.target.value)}
            />
          </div>

          {/* Active Only Checkbox */}
          <div className="filter-checkbox">
            <label>
              <input
                type="checkbox"
                checked={showActiveOnly}
                onChange={(e) => setShowActiveOnly(e.target.checked)}
              />
              <span>Pouze aktivní</span>
            </label>
          </div>

          {/* New Button */}
          <div className="new-button-container">
            <Button
              variant="success"
              onClick={() => setIsNewModalOpen(true)}
              className="w-full"
            >
              + Nová účast
            </Button>
          </div>
        </div>

        {/* Results count */}
        <div className="results-count">
          Zobrazeno: <strong>{filteredStakes.length}</strong> z <strong>{stakes.length}</strong> účastí
        </div>
      </div>

      {/* Stakes Table */}
      <div className="table-container">
        {filteredStakes.length === 0 ? (
          <div className="empty-state-with-action">
            <p>
              {stakes.length === 0 ? 'Žádné majetkové účasti' : 'Žádné účasti neodpovídají filtrům'}
            </p>
            {stakes.length === 0 && (
              <Button
                variant="primary"
                onClick={() => setIsNewModalOpen(true)}
              >
                Přidat první účast
              </Button>
            )}
          </div>
        ) : (
          <table className="data-table">
            <thead>
              <tr>
                <th>Účet</th>
                <th>Platnost</th>
                <th>Typ / Metoda</th>
                <th className="text-right">Počet Akcií</th>
                <th className="text-right">Transakční Měna</th>
                <th className="text-right">Účetní Měna</th>
                <th className="text-center">Kurz</th>
                <th className="text-center">Akce</th>
              </tr>
            </thead>
            <tbody>
              {filteredStakes.map((stake) => {
                const isActive = !stake.validTo || new Date(stake.validTo) >= new Date();

                return (
                  <tr
                    key={stake.id}
                    className={!isActive ? 'inactive' : ''}
                  >
                    <td>
                      <span className="account-number">{stake.accountNumber}</span>
                    </td>
                    <td>
                      <div className="validity-dates">
                        <span className="main-date">{formatDate(stake.validFrom)}</span>
                        {stake.validTo && (
                          <span className="end-date">
                            do {formatDate(stake.validTo)}
                          </span>
                        )}
                      </div>
                    </td>
                    <td>
                      <div className="transaction-info">
                        <span className="type">{stake.transactionTypeName}</span>
                        <span className="method">{stake.methodName}</span>
                      </div>
                    </td>
                    <td className="text-right">
                      {stake.numberOfShares.toLocaleString('cs-CZ')}
                    </td>
                    <td className="text-right">
                      <div className="currency-amount">
                        {formatCurrency(stake.totalTransactionAmount, stake.transactionCurrency)}
                      </div>
                      <div className="currency-rate">
                        {stake.pricePerShareTransaction.toLocaleString('cs-CZ', {
                          minimumFractionDigits: 2,
                          maximumFractionDigits: 2,
                        })} / akcie
                      </div>
                    </td>
                    <td className="text-right">
                      <div className="currency-amount">
                        {formatCurrency(stake.totalAccountingAmount, stake.accountingCurrency)}
                      </div>
                      <div className="currency-rate">
                        {stake.pricePerShareAccounting.toLocaleString('cs-CZ', {
                          minimumFractionDigits: 2,
                          maximumFractionDigits: 2,
                        })} / akcie
                      </div>
                    </td>
                    <td className="text-center">
                      {stake.exchangeRate.toLocaleString('cs-CZ', {
                        minimumFractionDigits: 4,
                        maximumFractionDigits: 4,
                      })}
                    </td>
                    <td>
                      <div className="action-buttons">
                        <button
                          onClick={() => handleEditClick(stake)}
                          className="edit"
                        >
                          Upravit
                        </button>
                        <button
                          onClick={() => handleDeleteClick(stake)}
                          className="delete"
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
            <tfoot>
              <tr>
                <td colSpan={3}>Celkem</td>
                <td className="text-right">
                  {totalShares.toLocaleString('cs-CZ')}
                </td>
                <td className="text-right">
                  {totalTransactionAmount.toLocaleString('cs-CZ', {
                    minimumFractionDigits: 2,
                    maximumFractionDigits: 2,
                  })}
                </td>
                <td className="text-right">
                  {totalAccountingAmount.toLocaleString('cs-CZ', {
                    minimumFractionDigits: 2,
                    maximumFractionDigits: 2,
                  })}
                </td>
                <td colSpan={2}></td>
              </tr>
            </tfoot>
          </table>
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
}
