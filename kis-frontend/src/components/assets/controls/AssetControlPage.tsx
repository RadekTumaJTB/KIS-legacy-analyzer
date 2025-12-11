/**
 * AssetControlPage Component
 *
 * Rules management interface for Equity Stake Controls
 * Replaces legacy MajetekKontrola.jsp
 *
 * Features:
 * - Manage account pattern rules (e.g., "061*" -> "Přímá účast")
 * - Create, edit, delete control rules
 * - Toggle active/inactive status
 * - Validation message configuration
 */

import { useState, useEffect } from 'react';
import type { AssetControlRule } from '../../../types/asset';
import * as assetApi from '../../../api/assetApi';
import { ControlRuleModal } from './ControlRuleModal';

export const AssetControlPage: React.FC = () => {
  const [rules, setRules] = useState<AssetControlRule[]>([]);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [successMessage, setSuccessMessage] = useState<string | null>(null);

  // Modals
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [selectedRule, setSelectedRule] = useState<AssetControlRule | null>(null);

  useEffect(() => {
    loadRules();
  }, []);

  const loadRules = async () => {
    setIsLoading(true);
    setError(null);
    try {
      const data = await assetApi.getControlRules();
      setRules(data);
    } catch (err) {
      setError('Chyba při načítání kontrolních pravidel: ' + (err as Error).message);
    } finally {
      setIsLoading(false);
    }
  };

  const handleNewRule = () => {
    setSelectedRule(null);
    setIsModalOpen(true);
  };

  const handleEditRule = (rule: AssetControlRule) => {
    setSelectedRule(rule);
    setIsModalOpen(true);
  };

  const handleDeleteRule = async (rule: AssetControlRule) => {
    if (!confirm(`Opravdu chcete smazat pravidlo pro pattern "${rule.accountPattern}"?`)) {
      return;
    }

    try {
      await assetApi.deleteControlRule(rule.id);
      setSuccessMessage('Pravidlo bylo úspěšně smazáno');
      await loadRules();
      setTimeout(() => setSuccessMessage(null), 3000);
    } catch (err) {
      setError('Chyba při mazání: ' + (err as Error).message);
    }
  };

  const handleRuleSuccess = async () => {
    setSuccessMessage(
      selectedRule
        ? 'Pravidlo bylo úspěšně aktualizováno'
        : 'Pravidlo bylo úspěšně vytvořeno'
    );
    await loadRules();
    setTimeout(() => setSuccessMessage(null), 3000);
  };

  // Separate active and inactive rules
  const activeRules = rules.filter(r => r.isActive);
  const inactiveRules = rules.filter(r => !r.isActive);

  return (
    <div className="container mx-auto px-4 py-8">
      {/* Header */}
      <div className="mb-6">
        <div className="flex items-center justify-between">
          <div>
            <h1 className="text-3xl font-bold text-gray-900">
              Kontrolní Pravidla Majetkových Účastí
            </h1>
            <p className="text-gray-600 mt-2">
              Správa pravidel pro validaci účtů a přiřazení typů majetkových účastí
            </p>
          </div>
          <button
            onClick={handleNewRule}
            className="px-4 py-2 bg-green-600 text-white rounded-md hover:bg-green-700 flex items-center gap-2"
          >
            <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 4v16m8-8H4" />
            </svg>
            Nové pravidlo
          </button>
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

      {/* Info Panel */}
      <div className="mb-6 bg-blue-50 border border-blue-200 rounded-lg p-4">
        <h3 className="text-sm font-semibold text-blue-800 mb-2">
          Jak fungují kontrolní pravidla:
        </h3>
        <ul className="text-sm text-blue-700 space-y-1">
          <li>
            • <strong>Account Pattern:</strong> Pattern pro číslo účtu (např. "061*" = všechny účty začínající 061)
          </li>
          <li>
            • <strong>Equity Stake Type:</strong> Typ majetkové účasti, který se automaticky přiřadí
          </li>
          <li>
            • <strong>Validation Message:</strong> Chybová hláška, pokud účet neodpovídá pravidlu
          </li>
          <li>
            • Pravidla se aplikují při vytváření a úpravě majetkových účastí
          </li>
        </ul>
      </div>

      {/* Statistics */}
      <div className="grid grid-cols-1 md:grid-cols-3 gap-4 mb-6">
        <div className="bg-white rounded-lg shadow p-6">
          <div className="text-sm text-gray-600 mb-1">Celkem pravidel</div>
          <div className="text-2xl font-bold text-gray-900">{rules.length}</div>
        </div>
        <div className="bg-white rounded-lg shadow p-6">
          <div className="text-sm text-gray-600 mb-1">Aktivní pravidla</div>
          <div className="text-2xl font-bold text-green-600">{activeRules.length}</div>
        </div>
        <div className="bg-white rounded-lg shadow p-6">
          <div className="text-sm text-gray-600 mb-1">Neaktivní pravidla</div>
          <div className="text-2xl font-bold text-gray-400">{inactiveRules.length}</div>
        </div>
      </div>

      {/* Rules Tables */}
      {isLoading ? (
        <div className="bg-white rounded-lg shadow p-12 text-center">
          <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600 mx-auto"></div>
          <p className="mt-4 text-gray-600">Načítání pravidel...</p>
        </div>
      ) : (
        <>
          {/* Active Rules */}
          <div className="bg-white rounded-lg shadow mb-6 overflow-hidden">
            <div className="px-6 py-4 bg-green-50 border-b border-green-200">
              <h2 className="text-lg font-semibold text-green-900">
                Aktivní Pravidla ({activeRules.length})
              </h2>
            </div>

            {activeRules.length === 0 ? (
              <div className="text-center py-8 text-gray-500">
                <p>Žádná aktivní pravidla</p>
              </div>
            ) : (
              <div className="overflow-x-auto">
                <table className="min-w-full divide-y divide-gray-200">
                  <thead className="bg-gray-50">
                    <tr>
                      <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">
                        ID
                      </th>
                      <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">
                        Account Pattern
                      </th>
                      <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">
                        Typ Majetkové Účasti
                      </th>
                      <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">
                        Popis
                      </th>
                      <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">
                        Validační Zpráva
                      </th>
                      <th className="px-4 py-3 text-center text-xs font-medium text-gray-500 uppercase">
                        Akce
                      </th>
                    </tr>
                  </thead>
                  <tbody className="bg-white divide-y divide-gray-200">
                    {activeRules.map((rule) => (
                      <tr key={rule.id} className="hover:bg-gray-50">
                        <td className="px-4 py-3 text-sm text-gray-600">
                          {rule.id}
                        </td>
                        <td className="px-4 py-3 text-sm font-mono font-bold text-blue-600">
                          {rule.accountPattern}
                        </td>
                        <td className="px-4 py-3 text-sm text-gray-900">
                          {rule.equityStakeTypeName}
                        </td>
                        <td className="px-4 py-3 text-sm text-gray-600">
                          {rule.description || '-'}
                        </td>
                        <td className="px-4 py-3 text-sm text-gray-600">
                          {rule.validationMessage || '-'}
                        </td>
                        <td className="px-4 py-3 text-center">
                          <div className="flex items-center justify-center gap-2">
                            <button
                              onClick={() => handleEditRule(rule)}
                              className="px-3 py-1 text-sm text-white bg-blue-600 rounded hover:bg-blue-700"
                            >
                              Upravit
                            </button>
                            <button
                              onClick={() => handleDeleteRule(rule)}
                              className="px-3 py-1 text-sm text-white bg-red-600 rounded hover:bg-red-700"
                            >
                              Smazat
                            </button>
                          </div>
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            )}
          </div>

          {/* Inactive Rules */}
          {inactiveRules.length > 0 && (
            <div className="bg-white rounded-lg shadow overflow-hidden">
              <div className="px-6 py-4 bg-gray-100 border-b border-gray-300">
                <h2 className="text-lg font-semibold text-gray-700">
                  Neaktivní Pravidla ({inactiveRules.length})
                </h2>
              </div>

              <div className="overflow-x-auto">
                <table className="min-w-full divide-y divide-gray-200">
                  <thead className="bg-gray-50">
                    <tr>
                      <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">
                        ID
                      </th>
                      <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">
                        Account Pattern
                      </th>
                      <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">
                        Typ Majetkové Účasti
                      </th>
                      <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">
                        Popis
                      </th>
                      <th className="px-4 py-3 text-center text-xs font-medium text-gray-500 uppercase">
                        Akce
                      </th>
                    </tr>
                  </thead>
                  <tbody className="bg-white divide-y divide-gray-200">
                    {inactiveRules.map((rule) => (
                      <tr key={rule.id} className="hover:bg-gray-50 opacity-60">
                        <td className="px-4 py-3 text-sm text-gray-600">
                          {rule.id}
                        </td>
                        <td className="px-4 py-3 text-sm font-mono text-gray-600">
                          {rule.accountPattern}
                        </td>
                        <td className="px-4 py-3 text-sm text-gray-700">
                          {rule.equityStakeTypeName}
                        </td>
                        <td className="px-4 py-3 text-sm text-gray-600">
                          {rule.description || '-'}
                        </td>
                        <td className="px-4 py-3 text-center">
                          <div className="flex items-center justify-center gap-2">
                            <button
                              onClick={() => handleEditRule(rule)}
                              className="px-3 py-1 text-sm text-white bg-blue-600 rounded hover:bg-blue-700"
                            >
                              Upravit
                            </button>
                            <button
                              onClick={() => handleDeleteRule(rule)}
                              className="px-3 py-1 text-sm text-white bg-red-600 rounded hover:bg-red-700"
                            >
                              Smazat
                            </button>
                          </div>
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            </div>
          )}
        </>
      )}

      {/* Modal */}
      <ControlRuleModal
        isOpen={isModalOpen}
        onClose={() => {
          setIsModalOpen(false);
          setSelectedRule(null);
        }}
        onSuccess={handleRuleSuccess}
        rule={selectedRule}
      />
    </div>
  );
};
