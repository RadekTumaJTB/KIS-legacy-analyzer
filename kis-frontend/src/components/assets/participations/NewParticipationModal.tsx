/**
 * NewParticipationModal Component
 *
 * Modal for creating new Equity Stake (Majetková Účast)
 * Part of legacy MajetekPrehled.jsp functionality
 *
 * Features:
 * - Dual currency support (transaction + accounting)
 * - Auto-calculation of accounting amounts via exchange rate
 * - Auto-calculation of total amounts
 * - Reference data integration
 */

import { useState, useEffect } from 'react';
import type { EquityStake } from '../../../types/asset';
import { createNewEquityStake, calculateAccountingAmounts, calculateTotalAmount } from '../../../types/asset';
import * as assetApi from '../../../api/assetApi';
import * as referenceApi from '../../../api/referenceApi';

interface NewParticipationModalProps {
  isOpen: boolean;
  onClose: () => void;
  onSuccess: (newStake: EquityStake) => void;
  companyId: number;
}

export const NewParticipationModal: React.FC<NewParticipationModalProps> = ({
  isOpen,
  onClose,
  onSuccess,
  companyId,
}) => {
  const [formData, setFormData] = useState<Partial<EquityStake>>(
    createNewEquityStake(0, companyId)
  );

  const [isSubmitting, setIsSubmitting] = useState(false);
  const [error, setError] = useState<string | null>(null);

  // Reference data
  const [emissions, setEmissions] = useState<Array<{ id: number; name: string; isin: string }>>([]);
  const [transactionTypes, setTransactionTypes] = useState<Array<{ id: number; name: string }>>([]);
  const [methods, setMethods] = useState<Array<{ id: number; name: string }>>([]);
  const [companies, setCompanies] = useState<Array<{ id: number; name: string }>>([]);
  const [currencies, setCurrencies] = useState<Array<{ code: string; name: string }>>([]);

  useEffect(() => {
    if (isOpen) {
      loadReferenceData();
    }
  }, [isOpen]);

  // Auto-calculate accounting amounts when transaction amounts or exchange rate change
  useEffect(() => {
    if (formData.pricePerShareTransaction && formData.exchangeRate) {
      const updated = calculateAccountingAmounts(formData);
      setFormData(prev => ({
        ...prev,
        pricePerShareAccounting: updated.pricePerShareAccounting,
        totalAccountingAmount: updated.totalAccountingAmount,
      }));
    }
  }, [formData.pricePerShareTransaction, formData.totalTransactionAmount, formData.exchangeRate]);

  // Auto-calculate total transaction amount when shares or price change
  useEffect(() => {
    if (formData.numberOfShares && formData.pricePerShareTransaction) {
      const totalAmount = calculateTotalAmount(formData);
      setFormData(prev => ({
        ...prev,
        totalTransactionAmount: totalAmount,
      }));
    }
  }, [formData.numberOfShares, formData.pricePerShareTransaction]);

  const loadReferenceData = async () => {
    try {
      // Load all reference data in parallel
      const [emissionsData, transTypesData, methodsData, companiesData, currenciesData] = await Promise.all([
        referenceApi.getEmissions(),
        referenceApi.getEquityStakeTransactionTypes(),
        referenceApi.getEquityStakeMethods(),
        referenceApi.getCompanies(),
        referenceApi.getCurrencies(),
      ]);

      setEmissions(emissionsData);
      setTransactionTypes(transTypesData);
      setMethods(methodsData);
      setCompanies(companiesData);
      setCurrencies(currenciesData);
    } catch (err) {
      setError('Chyba při načítání referenčních dat: ' + (err as Error).message);
    }
  };

  if (!isOpen) return null;

  const handleFieldChange = (field: keyof EquityStake, value: any) => {
    setFormData(prev => ({ ...prev, [field]: value }));
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError(null);

    // Validation
    if (!formData.emissionId || formData.emissionId === 0) {
      setError('Vyberte emisi');
      return;
    }
    if (!formData.accountNumber || formData.accountNumber.trim() === '') {
      setError('Zadejte číslo účtu');
      return;
    }
    if (!formData.validFrom) {
      setError('Zadejte datum platnosti od');
      return;
    }
    if (!formData.transactionTypeId) {
      setError('Vyberte typ transakce');
      return;
    }
    if (!formData.methodId) {
      setError('Vyberte metodu');
      return;
    }
    if (!formData.numberOfShares || formData.numberOfShares <= 0) {
      setError('Počet akcií musí být větší než 0');
      return;
    }

    setIsSubmitting(true);

    try {
      const result = await assetApi.createEquityStake(formData);
      onSuccess(result);
      handleClose();
    } catch (err) {
      setError('Chyba při vytváření: ' + (err as Error).message);
    } finally {
      setIsSubmitting(false);
    }
  };

  const handleClose = () => {
    setFormData(createNewEquityStake(0, companyId));
    setError(null);
    onClose();
  };

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
      <div className="bg-white rounded-lg shadow-xl max-w-4xl w-full mx-4 max-h-[90vh] overflow-y-auto">
        {/* Header */}
        <div className="flex items-center justify-between p-6 border-b sticky top-0 bg-white">
          <h2 className="text-2xl font-bold text-gray-900">
            Nová Majetková Účast
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
        <form onSubmit={handleSubmit}>
          <div className="p-6 space-y-6">
            {error && (
              <div className="p-4 bg-red-100 border border-red-400 text-red-700 rounded">
                {error}
              </div>
            )}

            {/* Basic Information */}
            <div className="border-b pb-4">
              <h3 className="text-lg font-semibold text-gray-900 mb-4">Základní informace</h3>
              <div className="grid grid-cols-2 gap-4">
                {/* Emission Selection */}
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-2">
                    Emise / Finanční Investice *
                  </label>
                  <select
                    value={formData.emissionId || 0}
                    onChange={(e) => handleFieldChange('emissionId', parseInt(e.target.value))}
                    className="w-full px-3 py-2 border border-gray-300 rounded-md focus:ring-2 focus:ring-blue-500"
                    required
                  >
                    <option value={0}>-- Vyberte emisi --</option>
                    {emissions.map(emission => (
                      <option key={emission.id} value={emission.id}>
                        {emission.name} - {emission.isin}
                      </option>
                    ))}
                  </select>
                </div>

                {/* Account Number */}
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-2">
                    Číslo Účtu *
                  </label>
                  <input
                    type="text"
                    value={formData.accountNumber || ''}
                    onChange={(e) => handleFieldChange('accountNumber', e.target.value)}
                    className="w-full px-3 py-2 border border-gray-300 rounded-md focus:ring-2 focus:ring-blue-500"
                    placeholder="např. 0611234567"
                    required
                  />
                </div>

                {/* Valid From */}
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-2">
                    Platnost Od *
                  </label>
                  <input
                    type="date"
                    value={formData.validFrom || ''}
                    onChange={(e) => handleFieldChange('validFrom', e.target.value)}
                    className="w-full px-3 py-2 border border-gray-300 rounded-md focus:ring-2 focus:ring-blue-500"
                    required
                  />
                </div>

                {/* Valid To */}
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-2">
                    Platnost Do
                  </label>
                  <input
                    type="date"
                    value={formData.validTo || ''}
                    onChange={(e) => handleFieldChange('validTo', e.target.value || undefined)}
                    className="w-full px-3 py-2 border border-gray-300 rounded-md focus:ring-2 focus:ring-blue-500"
                  />
                </div>

                {/* Transaction Type */}
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-2">
                    Typ Transakce *
                  </label>
                  <select
                    value={formData.transactionTypeId || 0}
                    onChange={(e) => handleFieldChange('transactionTypeId', parseInt(e.target.value))}
                    className="w-full px-3 py-2 border border-gray-300 rounded-md focus:ring-2 focus:ring-blue-500"
                    required
                  >
                    <option value={0}>-- Vyberte typ --</option>
                    {transactionTypes.map(type => (
                      <option key={type.id} value={type.id}>
                        {type.name}
                      </option>
                    ))}
                  </select>
                </div>

                {/* Method */}
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-2">
                    Metoda *
                  </label>
                  <select
                    value={formData.methodId || 0}
                    onChange={(e) => handleFieldChange('methodId', parseInt(e.target.value))}
                    className="w-full px-3 py-2 border border-gray-300 rounded-md focus:ring-2 focus:ring-blue-500"
                    required
                  >
                    <option value={0}>-- Vyberte metodu --</option>
                    {methods.map(method => (
                      <option key={method.id} value={method.id}>
                        {method.name}
                      </option>
                    ))}
                  </select>
                </div>
              </div>
            </div>

            {/* Share Information */}
            <div className="border-b pb-4">
              <h3 className="text-lg font-semibold text-gray-900 mb-4">Informace o akciích</h3>
              <div className="grid grid-cols-3 gap-4">
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-2">
                    Počet Akcií *
                  </label>
                  <input
                    type="number"
                    value={formData.numberOfShares || 0}
                    onChange={(e) => handleFieldChange('numberOfShares', parseFloat(e.target.value) || 0)}
                    className="w-full px-3 py-2 border border-gray-300 rounded-md focus:ring-2 focus:ring-blue-500"
                    min="0"
                    step="1"
                    required
                  />
                </div>

                {/* Purchased From Company */}
                <div className="col-span-2">
                  <label className="block text-sm font-medium text-gray-700 mb-2">
                    Nakoupeno Od (volitelné)
                  </label>
                  <select
                    value={formData.purchasedFromCompanyId || 0}
                    onChange={(e) => handleFieldChange('purchasedFromCompanyId', parseInt(e.target.value) || undefined)}
                    className="w-full px-3 py-2 border border-gray-300 rounded-md focus:ring-2 focus:ring-blue-500"
                  >
                    <option value={0}>-- Nevybráno --</option>
                    {companies.map(company => (
                      <option key={company.id} value={company.id}>
                        {company.name}
                      </option>
                    ))}
                  </select>
                </div>
              </div>
            </div>

            {/* Transaction Currency */}
            <div className="border-b pb-4">
              <h3 className="text-lg font-semibold text-gray-900 mb-4">Transakční Měna</h3>
              <div className="grid grid-cols-3 gap-4">
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-2">
                    Měna *
                  </label>
                  <select
                    value={formData.transactionCurrency || 'CZK'}
                    onChange={(e) => handleFieldChange('transactionCurrency', e.target.value)}
                    className="w-full px-3 py-2 border border-gray-300 rounded-md focus:ring-2 focus:ring-blue-500"
                    required
                  >
                    {currencies.map(currency => (
                      <option key={currency.code} value={currency.code}>
                        {currency.code} - {currency.name}
                      </option>
                    ))}
                  </select>
                </div>

                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-2">
                    Cena za Akcii *
                  </label>
                  <input
                    type="number"
                    value={formData.pricePerShareTransaction || 0}
                    onChange={(e) => handleFieldChange('pricePerShareTransaction', parseFloat(e.target.value) || 0)}
                    className="w-full px-3 py-2 border border-gray-300 rounded-md focus:ring-2 focus:ring-blue-500"
                    min="0"
                    step="0.01"
                    required
                  />
                </div>

                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-2">
                    Celková Částka (auto)
                  </label>
                  <input
                    type="text"
                    value={formData.totalTransactionAmount?.toLocaleString('cs-CZ', {
                      minimumFractionDigits: 2,
                      maximumFractionDigits: 2,
                    }) || '0,00'}
                    readOnly
                    className="w-full px-3 py-2 border border-gray-300 rounded-md bg-gray-100 text-right"
                  />
                </div>
              </div>
            </div>

            {/* Exchange Rate and Accounting Currency */}
            <div className="border-b pb-4">
              <h3 className="text-lg font-semibold text-gray-900 mb-4">Kurz a Účetní Měna</h3>
              <div className="grid grid-cols-4 gap-4">
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-2">
                    Kurz *
                  </label>
                  <input
                    type="number"
                    value={formData.exchangeRate || 1}
                    onChange={(e) => handleFieldChange('exchangeRate', parseFloat(e.target.value) || 1)}
                    className="w-full px-3 py-2 border border-gray-300 rounded-md focus:ring-2 focus:ring-blue-500"
                    min="0"
                    step="0.0001"
                    required
                  />
                </div>

                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-2">
                    Účetní Měna *
                  </label>
                  <select
                    value={formData.accountingCurrency || 'CZK'}
                    onChange={(e) => handleFieldChange('accountingCurrency', e.target.value)}
                    className="w-full px-3 py-2 border border-gray-300 rounded-md focus:ring-2 focus:ring-blue-500"
                    required
                  >
                    {currencies.map(currency => (
                      <option key={currency.code} value={currency.code}>
                        {currency.code}
                      </option>
                    ))}
                  </select>
                </div>

                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-2">
                    Cena za Akcii (auto)
                  </label>
                  <input
                    type="text"
                    value={formData.pricePerShareAccounting?.toLocaleString('cs-CZ', {
                      minimumFractionDigits: 2,
                      maximumFractionDigits: 2,
                    }) || '0,00'}
                    readOnly
                    className="w-full px-3 py-2 border border-gray-300 rounded-md bg-gray-100 text-right"
                  />
                </div>

                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-2">
                    Celková Částka (auto)
                  </label>
                  <input
                    type="text"
                    value={formData.totalAccountingAmount?.toLocaleString('cs-CZ', {
                      minimumFractionDigits: 2,
                      maximumFractionDigits: 2,
                    }) || '0,00'}
                    readOnly
                    className="w-full px-3 py-2 border border-gray-300 rounded-md bg-gray-100 text-right"
                  />
                </div>
              </div>
            </div>

            {/* Additional Options */}
            <div>
              <div className="flex items-center gap-2">
                <input
                  type="checkbox"
                  checked={formData.ignoreFlag || false}
                  onChange={(e) => handleFieldChange('ignoreFlag', e.target.checked)}
                  className="w-4 h-4 text-blue-600 border-gray-300 rounded focus:ring-blue-500"
                />
                <label className="text-sm text-gray-700">
                  Ignorovat při výpočtech (Ignore Flag)
                </label>
              </div>
            </div>
          </div>

          {/* Footer */}
          <div className="flex justify-end gap-3 px-6 py-4 bg-gray-50 border-t rounded-b-lg sticky bottom-0">
            <button
              type="button"
              onClick={handleClose}
              className="px-4 py-2 text-gray-700 bg-white border border-gray-300 rounded-md hover:bg-gray-50"
              disabled={isSubmitting}
            >
              Zrušit
            </button>
            <button
              type="submit"
              className="px-4 py-2 text-white bg-blue-600 rounded-md hover:bg-blue-700 disabled:bg-gray-400 flex items-center gap-2"
              disabled={isSubmitting}
            >
              {isSubmitting && (
                <div className="animate-spin rounded-full h-4 w-4 border-b-2 border-white"></div>
              )}
              {isSubmitting ? 'Vytvářím...' : 'Vytvořit'}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};
