/**
 * EmissionListPage Component
 *
 * Main container for Emissions Management with inline editing grid
 * Supports batch I/U/D operations
 *
 * Based on JSP analysis of FininvInvestice.jsp
 */

import { useState, useEffect } from 'react';
import { EmissionItemRow } from './EmissionItemRow';
import type { EmissionWithItems, EmissionItem } from '../../../types/emission';
import { createNewEmissionItem, getDirtyItems } from '../../../types/emission';
import * as emissionApi from '../../../api/emissionApi';

export const EmissionListPage: React.FC = () => {
  const [emissions, setEmissions] = useState<EmissionWithItems[]>([]);
  const [selectedEmission, setSelectedEmission] = useState<EmissionWithItems | null>(null);
  const [items, setItems] = useState<EmissionItem[]>([]);
  const [isLoading, setIsLoading] = useState(false);
  const [isSaving, setIsSaving] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [successMessage, setSuccessMessage] = useState<string | null>(null);

  // Load all emissions on mount
  useEffect(() => {
    loadEmissions();
  }, []);

  // Load emission items when selection changes
  useEffect(() => {
    if (selectedEmission) {
      setItems(selectedEmission.emissionItems);
    }
  }, [selectedEmission]);

  const loadEmissions = async () => {
    setIsLoading(true);
    setError(null);
    try {
      const data = await emissionApi.getAllEmissions();
      setEmissions(data);

      // Auto-select first emission if available
      if (data.length > 0 && !selectedEmission) {
        setSelectedEmission(data[0]);
      }
    } catch (err) {
      setError('Chyba při načítání emisí: ' + (err as Error).message);
    } finally {
      setIsLoading(false);
    }
  };

  const loadEmissionDetail = async (id: number) => {
    setIsLoading(true);
    setError(null);
    try {
      const data = await emissionApi.getEmissionById(id);
      setSelectedEmission(data);
      setItems(data.emissionItems);
    } catch (err) {
      setError('Chyba při načítání detailu emise: ' + (err as Error).message);
    } finally {
      setIsLoading(false);
    }
  };

  const handleEmissionSelect = (emission: EmissionWithItems) => {
    setSelectedEmission(emission);
    loadEmissionDetail(emission.financialInvestment.id);
  };

  const handleAddRow = () => {
    if (!selectedEmission) {
      setError('Nejprve vyberte finanční investici');
      return;
    }

    const newItem = createNewEmissionItem(selectedEmission.financialInvestment.id);
    setItems(prevItems => [...prevItems, newItem]);
  };

  const handleUpdateItem = (updatedItem: EmissionItem) => {
    setItems(prevItems =>
      prevItems.map(item =>
        item === updatedItem || (item.id && item.id === updatedItem.id)
          ? updatedItem
          : item
      )
    );
  };

  const handleDeleteItem = (deletedItem: EmissionItem) => {
    if (deletedItem.id) {
      // Existing item - mark for deletion (keep in array with action='D')
      setItems(prevItems =>
        prevItems.map(item =>
          item.id === deletedItem.id ? deletedItem : item
        )
      );
    } else {
      // New unsaved item - remove from array
      setItems(prevItems => prevItems.filter(item => item !== deletedItem));
    }
  };

  const handleSaveAll = async () => {
    if (!selectedEmission) {
      setError('Není vybrána žádná finanční investice');
      return;
    }

    const dirtyItems = getDirtyItems(items);

    if (dirtyItems.length === 0) {
      setError('Žádné změny k uložení');
      return;
    }

    setIsSaving(true);
    setError(null);
    setSuccessMessage(null);

    try {
      // Batch update via API
      const result = await emissionApi.batchUpdateEmissionItems(
        selectedEmission.financialInvestment.id,
        dirtyItems
      );

      // Update local state with fresh data from server
      setSelectedEmission(result);
      setItems(result.emissionItems);

      // Show success message
      const insertCount = dirtyItems.filter(i => i.action === 'I').length;
      const updateCount = dirtyItems.filter(i => i.action === 'U').length;
      const deleteCount = dirtyItems.filter(i => i.action === 'D').length;

      setSuccessMessage(
        `Úspěšně uloženo: ${insertCount} nových, ${updateCount} změněných, ${deleteCount} smazaných`
      );

      // Reload emissions list
      await loadEmissions();
    } catch (err) {
      setError('Chyba při ukládání: ' + (err as Error).message);
    } finally {
      setIsSaving(false);
    }
  };

  const handleCancelChanges = () => {
    if (selectedEmission) {
      // Reload original data
      loadEmissionDetail(selectedEmission.financialInvestment.id);
    }
  };

  const hasDirtyItems = getDirtyItems(items).length > 0;

  return (
    <div className="container mx-auto px-4 py-8">
      <div className="mb-6">
        <h1 className="text-3xl font-bold text-gray-900">
          Správa Emisí a Finančních Investic
        </h1>
        <p className="text-gray-600 mt-2">
          Inline editace s batch update operacemi (I/U/D tracking)
        </p>
      </div>

      {/* Error/Success Messages */}
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

      <div className="grid grid-cols-12 gap-6">
        {/* Left Sidebar - Emissions List */}
        <div className="col-span-3">
          <div className="bg-white rounded-lg shadow p-4">
            <h2 className="text-lg font-semibold mb-4">Finanční Investice</h2>

            {isLoading && !selectedEmission ? (
              <div className="text-center py-8">
                <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-blue-600 mx-auto"></div>
                <p className="mt-2 text-gray-600">Načítání...</p>
              </div>
            ) : (
              <div className="space-y-2">
                {emissions.map(emission => (
                  <button
                    key={emission.financialInvestment.id}
                    onClick={() => handleEmissionSelect(emission)}
                    className={`w-full text-left px-4 py-3 rounded transition-colors ${
                      selectedEmission?.financialInvestment.id === emission.financialInvestment.id
                        ? 'bg-blue-100 border-2 border-blue-500'
                        : 'bg-gray-50 hover:bg-gray-100 border-2 border-transparent'
                    }`}
                  >
                    <div className="font-medium">{emission.financialInvestment.companyName}</div>
                    <div className="text-sm text-gray-600">
                      {emission.financialInvestment.isinCode}
                    </div>
                    <div className="text-xs text-gray-500 mt-1">
                      {emission.emissionItems.length} položek
                    </div>
                  </button>
                ))}
              </div>
            )}
          </div>
        </div>

        {/* Main Content - Emission Items Grid */}
        <div className="col-span-9">
          <div className="bg-white rounded-lg shadow">
            {/* Header with action buttons */}
            <div className="flex items-center justify-between p-4 border-b">
              <div>
                {selectedEmission && (
                  <>
                    <h2 className="text-xl font-semibold">
                      {selectedEmission.financialInvestment.companyName}
                    </h2>
                    <p className="text-sm text-gray-600">
                      ISIN: {selectedEmission.financialInvestment.isinCode} |
                      Měna: {selectedEmission.financialInvestment.currency}
                    </p>
                  </>
                )}
              </div>

              <div className="flex gap-2">
                <button
                  onClick={handleAddRow}
                  disabled={!selectedEmission || isLoading}
                  className="px-4 py-2 bg-green-600 text-white rounded hover:bg-green-700 disabled:bg-gray-400 disabled:cursor-not-allowed"
                >
                  + Přidat řádek
                </button>

                <button
                  onClick={handleCancelChanges}
                  disabled={!hasDirtyItems || isSaving}
                  className="px-4 py-2 bg-gray-600 text-white rounded hover:bg-gray-700 disabled:bg-gray-400 disabled:cursor-not-allowed"
                >
                  Zrušit změny
                </button>

                <button
                  onClick={handleSaveAll}
                  disabled={!hasDirtyItems || isSaving}
                  className="px-4 py-2 bg-blue-600 text-white rounded hover:bg-blue-700 disabled:bg-gray-400 disabled:cursor-not-allowed flex items-center gap-2"
                >
                  {isSaving && (
                    <div className="animate-spin rounded-full h-4 w-4 border-b-2 border-white"></div>
                  )}
                  {isSaving ? 'Ukládám...' : 'Uložit vše'}
                  {hasDirtyItems && (
                    <span className="bg-white text-blue-600 rounded-full px-2 py-0.5 text-xs font-bold">
                      {getDirtyItems(items).length}
                    </span>
                  )}
                </button>
              </div>
            </div>

            {/* Items Table */}
            <div className="overflow-x-auto">
              {isLoading && selectedEmission ? (
                <div className="text-center py-12">
                  <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600 mx-auto"></div>
                  <p className="mt-4 text-gray-600">Načítání položek...</p>
                </div>
              ) : !selectedEmission ? (
                <div className="text-center py-12 text-gray-500">
                  <p className="text-lg">Vyberte finanční investici z levého panelu</p>
                </div>
              ) : items.length === 0 ? (
                <div className="text-center py-12 text-gray-500">
                  <p className="text-lg">Žádné položky</p>
                  <button
                    onClick={handleAddRow}
                    className="mt-4 px-4 py-2 bg-blue-600 text-white rounded hover:bg-blue-700"
                  >
                    Přidat první položku
                  </button>
                </div>
              ) : (
                <table className="min-w-full divide-y divide-gray-200">
                  <thead className="bg-gray-50">
                    <tr>
                      <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                        Expand
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
                        Objem (calc)
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
                      <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                        Akce
                      </th>
                    </tr>
                  </thead>
                  <tbody className="bg-white divide-y divide-gray-200">
                    {items.map((item, index) => (
                      <EmissionItemRow
                        key={item.id || `new-${index}`}
                        item={item}
                        onUpdate={handleUpdateItem}
                        onDelete={handleDeleteItem}
                        isNew={!item.id}
                      />
                    ))}
                  </tbody>
                </table>
              )}
            </div>

            {/* Footer with statistics */}
            {selectedEmission && items.length > 0 && (
              <div className="p-4 border-t bg-gray-50">
                <div className="flex justify-between text-sm">
                  <div className="space-x-4">
                    <span className="text-gray-600">
                      Celkem položek: <strong>{items.length}</strong>
                    </span>
                    <span className="text-gray-600">
                      Změněných: <strong className="text-yellow-600">{getDirtyItems(items).length}</strong>
                    </span>
                  </div>
                  <div className="space-x-4">
                    <span className="text-gray-600">
                      Nových: <strong className="text-green-600">
                        {items.filter(i => i.action === 'I').length}
                      </strong>
                    </span>
                    <span className="text-gray-600">
                      Upravených: <strong className="text-yellow-600">
                        {items.filter(i => i.action === 'U' && i.isDirty).length}
                      </strong>
                    </span>
                    <span className="text-gray-600">
                      Smazaných: <strong className="text-red-600">
                        {items.filter(i => i.action === 'D').length}
                      </strong>
                    </span>
                  </div>
                </div>
              </div>
            )}
          </div>
        </div>
      </div>
    </div>
  );
};
