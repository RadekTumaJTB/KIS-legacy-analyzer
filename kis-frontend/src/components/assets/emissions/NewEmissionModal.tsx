/**
 * NewEmissionModal Component
 *
 * Modal for creating new Financial Investment
 */

import { useState } from 'react';
import type { FinancialInvestment } from '../../../types/emission';
import * as emissionApi from '../../../api/emissionApi';

interface NewEmissionModalProps {
  isOpen: boolean;
  onClose: () => void;
  onSuccess: (newEmission: FinancialInvestment) => void;
}

export const NewEmissionModal: React.FC<NewEmissionModalProps> = ({
  isOpen,
  onClose,
  onSuccess,
}) => {
  const [formData, setFormData] = useState<Partial<FinancialInvestment>>({
    companyId: 0,
    companyName: '',
    currency: 'CZK',
    isinCode: '',
  });

  const [isSubmitting, setIsSubmitting] = useState(false);
  const [error, setError] = useState<string | null>(null);

  if (!isOpen) return null;

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError(null);

    // Validation
    if (!formData.companyId || formData.companyId === 0) {
      setError('Vyberte společnost');
      return;
    }
    if (!formData.currency) {
      setError('Vyberte měnu');
      return;
    }
    if (!formData.isinCode || formData.isinCode.trim() === '') {
      setError('Zadejte ISIN kód');
      return;
    }

    setIsSubmitting(true);

    try {
      const result = await emissionApi.createFinancialInvestment(formData);
      onSuccess(result);
      handleClose();
    } catch (err) {
      setError('Chyba při vytváření: ' + (err as Error).message);
    } finally {
      setIsSubmitting(false);
    }
  };

  const handleClose = () => {
    setFormData({
      companyId: 0,
      companyName: '',
      currency: 'CZK',
      isinCode: '',
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
            Nová Finanční Investice
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
          <div className="p-6 space-y-4">
            {error && (
              <div className="p-4 bg-red-100 border border-red-400 text-red-700 rounded">
                {error}
              </div>
            )}

            {/* Company Selection */}
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">
                Společnost *
              </label>
              <select
                value={formData.companyId}
                onChange={(e) => setFormData({ ...formData, companyId: parseInt(e.target.value) })}
                className="w-full px-3 py-2 border border-gray-300 rounded-md focus:ring-2 focus:ring-blue-500"
                required
              >
                <option value={0}>-- Vyberte společnost --</option>
                {/* TODO: Load companies from reference API */}
                <option value={1}>J&T BANKA, a.s.</option>
                <option value={2}>J&T INVESTIČNÍ SPOLEČNOST, a.s.</option>
                <option value={3}>J&T ASSET MANAGEMENT, INVESTIČNÍ SPOLEČNOST, a.s.</option>
              </select>
            </div>

            {/* Currency Selection */}
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">
                Měna *
              </label>
              <select
                value={formData.currency}
                onChange={(e) => setFormData({ ...formData, currency: e.target.value })}
                className="w-full px-3 py-2 border border-gray-300 rounded-md focus:ring-2 focus:ring-blue-500"
                required
              >
                <option value="CZK">CZK - Česká koruna</option>
                <option value="EUR">EUR - Euro</option>
                <option value="USD">USD - Americký dolar</option>
                <option value="GBP">GBP - Britská libra</option>
              </select>
            </div>

            {/* ISIN Code */}
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">
                ISIN Kód *
              </label>
              <input
                type="text"
                value={formData.isinCode}
                onChange={(e) => setFormData({ ...formData, isinCode: e.target.value.toUpperCase() })}
                className="w-full px-3 py-2 border border-gray-300 rounded-md focus:ring-2 focus:ring-blue-500"
                placeholder="např. CZ0008019106"
                maxLength={12}
                pattern="[A-Z]{2}[A-Z0-9]{10}"
                required
              />
              <p className="mt-1 text-sm text-gray-500">
                Formát: 2 písmena země + 10 znaků (písmena/číslice)
              </p>
            </div>
          </div>

          {/* Footer */}
          <div className="flex justify-end gap-3 px-6 py-4 bg-gray-50 rounded-b-lg">
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
