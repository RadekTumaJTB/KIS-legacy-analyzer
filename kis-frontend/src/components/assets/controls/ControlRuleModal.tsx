/**
 * ControlRuleModal Component
 *
 * Modal for creating/editing Asset Control Rules
 * Part of legacy MajetekKontrola.jsp functionality
 *
 * Features:
 * - Account pattern configuration (e.g., "061*")
 * - Equity stake type assignment
 * - Validation message configuration
 * - Active/inactive toggle
 */

import { useState, useEffect } from 'react';
import type { AssetControlRule } from '../../../types/asset';
import * as assetApi from '../../../api/assetApi';
import * as referenceApi from '../../../api/referenceApi';

interface ControlRuleModalProps {
  isOpen: boolean;
  onClose: () => void;
  onSuccess: () => void;
  rule: AssetControlRule | null;
}

export const ControlRuleModal: React.FC<ControlRuleModalProps> = ({
  isOpen,
  onClose,
  onSuccess,
  rule,
}) => {
  const [formData, setFormData] = useState<Partial<AssetControlRule>>({
    accountPattern: '',
    equityStakeTypeId: 0,
    isActive: true,
    description: '',
    validationMessage: '',
  });

  const [isSubmitting, setIsSubmitting] = useState(false);
  const [error, setError] = useState<string | null>(null);

  // Reference data
  const [equityStakeTypes, setEquityStakeTypes] = useState<Array<{ id: number; name: string }>>([]);

  useEffect(() => {
    if (isOpen) {
      loadReferenceData();
      if (rule) {
        setFormData(rule);
      } else {
        setFormData({
          accountPattern: '',
          equityStakeTypeId: 0,
          isActive: true,
          description: '',
          validationMessage: '',
        });
      }
    }
  }, [isOpen, rule]);

  const loadReferenceData = async () => {
    try {
      const types = await referenceApi.getEquityStakeTypes();
      setEquityStakeTypes(types);
    } catch (err) {
      setError('Chyba při načítání referenčních dat: ' + (err as Error).message);
    }
  };

  if (!isOpen) return null;

  const handleFieldChange = (field: keyof AssetControlRule, value: any) => {
    setFormData(prev => ({ ...prev, [field]: value }));
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError(null);

    // Validation
    if (!formData.accountPattern || formData.accountPattern.trim() === '') {
      setError('Zadejte account pattern');
      return;
    }
    if (!formData.equityStakeTypeId || formData.equityStakeTypeId === 0) {
      setError('Vyberte typ majetkové účasti');
      return;
    }

    // Validate account pattern format
    const patternRegex = /^[0-9*]+$/;
    if (!patternRegex.test(formData.accountPattern)) {
      setError('Account pattern může obsahovat pouze číslice a *');
      return;
    }

    setIsSubmitting(true);

    try {
      if (rule) {
        // Update existing rule
        await assetApi.updateControlRule(rule.id, formData);
      } else {
        // Create new rule
        await assetApi.createControlRule(formData);
      }
      onSuccess();
      handleClose();
    } catch (err) {
      setError('Chyba při ukládání: ' + (err as Error).message);
    } finally {
      setIsSubmitting(false);
    }
  };

  const handleClose = () => {
    setFormData({
      accountPattern: '',
      equityStakeTypeId: 0,
      isActive: true,
      description: '',
      validationMessage: '',
    });
    setError(null);
    onClose();
  };

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
      <div className="bg-white rounded-lg shadow-xl max-w-2xl w-full mx-4">
        {/* Header */}
        <div className="flex items-center justify-between p-6 border-b">
          <div>
            <h2 className="text-2xl font-bold text-gray-900">
              {rule ? 'Upravit Kontrolní Pravidlo' : 'Nové Kontrolní Pravidlo'}
            </h2>
            {rule && <p className="text-sm text-gray-600 mt-1">ID: {rule.id}</p>}
          </div>
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

            {/* Info Panel */}
            <div className="bg-blue-50 border border-blue-200 rounded-lg p-4">
              <h3 className="text-sm font-semibold text-blue-800 mb-2">
                Pattern Examples:
              </h3>
              <ul className="text-sm text-blue-700 space-y-1">
                <li>• <code className="bg-blue-100 px-2 py-0.5 rounded">061*</code> - všechny účty začínající 061</li>
                <li>• <code className="bg-blue-100 px-2 py-0.5 rounded">062*</code> - všechny účty začínající 062</li>
                <li>• <code className="bg-blue-100 px-2 py-0.5 rounded">0611234567</code> - konkrétní účet</li>
                <li>• Použijte * jako wildcard pro shodu s jakýmkoli počtem číslic</li>
              </ul>
            </div>

            {/* Account Pattern */}
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">
                Account Pattern *
              </label>
              <input
                type="text"
                value={formData.accountPattern || ''}
                onChange={(e) => handleFieldChange('accountPattern', e.target.value)}
                className="w-full px-3 py-2 border border-gray-300 rounded-md focus:ring-2 focus:ring-blue-500 font-mono"
                placeholder="např. 061*"
                pattern="[0-9*]+"
                title="Pouze číslice a *"
                required
              />
              <p className="mt-1 text-sm text-gray-500">
                Pattern pro čísla účtů (pouze číslice a *)
              </p>
            </div>

            {/* Equity Stake Type */}
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">
                Typ Majetkové Účasti *
              </label>
              <select
                value={formData.equityStakeTypeId || 0}
                onChange={(e) => handleFieldChange('equityStakeTypeId', parseInt(e.target.value))}
                className="w-full px-3 py-2 border border-gray-300 rounded-md focus:ring-2 focus:ring-blue-500"
                required
              >
                <option value={0}>-- Vyberte typ --</option>
                {equityStakeTypes.map(type => (
                  <option key={type.id} value={type.id}>
                    {type.name}
                  </option>
                ))}
              </select>
              <p className="mt-1 text-sm text-gray-500">
                Typ, který se automaticky přiřadí k účtům odpovídajícím patternu
              </p>
            </div>

            {/* Description */}
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">
                Popis (volitelné)
              </label>
              <textarea
                value={formData.description || ''}
                onChange={(e) => handleFieldChange('description', e.target.value)}
                className="w-full px-3 py-2 border border-gray-300 rounded-md focus:ring-2 focus:ring-blue-500"
                rows={2}
                placeholder="Krátký popis pravidla..."
              />
            </div>

            {/* Validation Message */}
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">
                Validační Zpráva (volitelné)
              </label>
              <textarea
                value={formData.validationMessage || ''}
                onChange={(e) => handleFieldChange('validationMessage', e.target.value)}
                className="w-full px-3 py-2 border border-gray-300 rounded-md focus:ring-2 focus:ring-blue-500"
                rows={2}
                placeholder="Chybová zpráva zobrazená při neshodě s pravidlem..."
              />
              <p className="mt-1 text-sm text-gray-500">
                Zpráva zobrazená uživateli, pokud účet neodpovídá pravidlu
              </p>
            </div>

            {/* Active Status */}
            <div className="flex items-center gap-2 pt-4 border-t">
              <input
                type="checkbox"
                checked={formData.isActive ?? true}
                onChange={(e) => handleFieldChange('isActive', e.target.checked)}
                className="w-4 h-4 text-blue-600 border-gray-300 rounded focus:ring-blue-500"
              />
              <label className="text-sm font-medium text-gray-700">
                Aktivní pravidlo
              </label>
              <span className="text-sm text-gray-500">
                (Pouze aktivní pravidla se používají při validaci)
              </span>
            </div>
          </div>

          {/* Footer */}
          <div className="flex justify-end gap-3 px-6 py-4 bg-gray-50 border-t rounded-b-lg">
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
              {isSubmitting ? (rule ? 'Ukládám...' : 'Vytvářím...') : (rule ? 'Uložit změny' : 'Vytvořit')}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};
