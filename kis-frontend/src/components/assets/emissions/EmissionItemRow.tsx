/**
 * EmissionItemRow Component
 *
 * CRITICAL COMPONENT: Inline editable row for emission items
 * Supports I/U/D action tracking for batch operations
 *
 * Based on JSP analysis of FininvInvestice.jsp
 */

import { useState, useEffect } from 'react';
import type { EmissionItem } from '../../../types/emission';
import { calculateVolume } from '../../../types/emission';

interface EmissionItemRowProps {
  item: EmissionItem;
  onUpdate: (updatedItem: EmissionItem) => void;
  onDelete: (item: EmissionItem) => void;
  isNew?: boolean;
}

export const EmissionItemRow: React.FC<EmissionItemRowProps> = ({
  item,
  onUpdate,
  onDelete,
  isNew = false,
}) => {
  const [localItem, setLocalItem] = useState<EmissionItem>(item);
  const [isExpanded, setIsExpanded] = useState(item.isExpanded || false);
  const [errors, setErrors] = useState<Record<string, string>>({});

  // Sync with parent when item changes
  useEffect(() => {
    setLocalItem(item);
  }, [item]);

  // Auto-calculate volume when shares or nominal changes
  useEffect(() => {
    if (localItem.numberOfShares && localItem.nominalValue) {
      const calculatedVolume = calculateVolume(localItem);
      if (calculatedVolume !== localItem.volume) {
        setLocalItem(prev => ({
          ...prev,
          volume: calculatedVolume,
        }));
      }
    }
  }, [localItem.numberOfShares, localItem.nominalValue]);

  const handleFieldChange = (field: keyof EmissionItem, value: any) => {
    const updatedItem: EmissionItem = {
      ...localItem,
      [field]: value,
      action: localItem.id ? 'U' : 'I', // Mark as Update if has ID, else Insert
      isDirty: true,
    };

    setLocalItem(updatedItem);
    onUpdate(updatedItem);

    // Clear error for this field
    if (errors[field]) {
      setErrors(prev => {
        const newErrors = { ...prev };
        delete newErrors[field];
        return newErrors;
      });
    }
  };

  const handleDeleteClick = () => {
    if (localItem.id) {
      // Existing item - mark for deletion
      const deletedItem: EmissionItem = {
        ...localItem,
        action: 'D',
        isDirty: true,
      };
      onDelete(deletedItem);
    } else {
      // New unsaved item - just remove from UI
      onDelete(localItem);
    }
  };

  const toggleExpanded = () => {
    setIsExpanded(!isExpanded);
    const updatedItem: EmissionItem = {
      ...localItem,
      isExpanded: !isExpanded,
    };
    setLocalItem(updatedItem);
    onUpdate(updatedItem);
  };

  const validateField = (field: keyof EmissionItem): boolean => {
    let error = '';

    switch (field) {
      case 'validFrom':
        if (!localItem.validFrom) {
          error = 'Platnost od je povinná';
        }
        break;
      case 'numberOfShares':
        if (!localItem.numberOfShares || localItem.numberOfShares <= 0) {
          error = 'Počet kusů musí být větší než 0';
        }
        break;
      case 'nominalValue':
        if (!localItem.nominalValue || localItem.nominalValue <= 0) {
          error = 'Nominální hodnota musí být větší než 0';
        }
        break;
    }

    if (error) {
      setErrors(prev => ({ ...prev, [field]: error }));
      return false;
    }
    return true;
  };

  // Style for deleted rows
  const rowClassName = localItem.action === 'D'
    ? 'bg-red-50 opacity-50 line-through'
    : localItem.isDirty
    ? 'bg-yellow-50'
    : isNew
    ? 'bg-green-50'
    : '';

  return (
    <>
      <tr className={`border-b hover:bg-gray-50 ${rowClassName}`}>
        {/* Hidden action field (I/U/D tracking) */}
        <input type="hidden" value={localItem.action} />

        {/* Expand/Collapse button */}
        <td className="px-4 py-2 text-center">
          <button
            type="button"
            onClick={toggleExpanded}
            className="text-blue-600 hover:text-blue-800"
            disabled={localItem.action === 'D'}
          >
            {isExpanded ? '▼' : '▶'}
          </button>
        </td>

        {/* Valid From */}
        <td className="px-4 py-2">
          <input
            type="date"
            value={localItem.validFrom}
            onChange={(e) => handleFieldChange('validFrom', e.target.value)}
            onBlur={() => validateField('validFrom')}
            disabled={localItem.action === 'D'}
            className={`w-full px-2 py-1 border rounded focus:ring-2 focus:ring-blue-500 ${
              errors.validFrom ? 'border-red-500' : 'border-gray-300'
            }`}
            required
          />
          {errors.validFrom && (
            <span className="text-xs text-red-600">{errors.validFrom}</span>
          )}
        </td>

        {/* Valid To */}
        <td className="px-4 py-2">
          <input
            type="date"
            value={localItem.validTo || ''}
            onChange={(e) => handleFieldChange('validTo', e.target.value || undefined)}
            disabled={localItem.action === 'D'}
            className="w-full px-2 py-1 border border-gray-300 rounded focus:ring-2 focus:ring-blue-500"
          />
        </td>

        {/* Number of Shares */}
        <td className="px-4 py-2">
          <input
            type="number"
            value={localItem.numberOfShares}
            onChange={(e) => handleFieldChange('numberOfShares', parseFloat(e.target.value) || 0)}
            onBlur={() => validateField('numberOfShares')}
            disabled={localItem.action === 'D'}
            className={`w-full px-2 py-1 border rounded text-right focus:ring-2 focus:ring-blue-500 ${
              errors.numberOfShares ? 'border-red-500' : 'border-gray-300'
            }`}
            min="0"
            step="1"
            required
          />
          {errors.numberOfShares && (
            <span className="text-xs text-red-600">{errors.numberOfShares}</span>
          )}
        </td>

        {/* Nominal Value */}
        <td className="px-4 py-2">
          <input
            type="number"
            value={localItem.nominalValue}
            onChange={(e) => handleFieldChange('nominalValue', parseFloat(e.target.value) || 0)}
            onBlur={() => validateField('nominalValue')}
            disabled={localItem.action === 'D'}
            className={`w-full px-2 py-1 border rounded text-right focus:ring-2 focus:ring-blue-500 ${
              errors.nominalValue ? 'border-red-500' : 'border-gray-300'
            }`}
            min="0"
            step="0.01"
            required
          />
          {errors.nominalValue && (
            <span className="text-xs text-red-600">{errors.nominalValue}</span>
          )}
        </td>

        {/* Volume (calculated, read-only) */}
        <td className="px-4 py-2 text-right bg-gray-100">
          <span className="font-mono">
            {localItem.volume.toLocaleString('cs-CZ', {
              minimumFractionDigits: 2,
              maximumFractionDigits: 2,
            })}
          </span>
        </td>

        {/* Registered Capital */}
        <td className="px-4 py-2">
          <input
            type="number"
            value={localItem.registeredCapital}
            onChange={(e) => handleFieldChange('registeredCapital', parseFloat(e.target.value) || 0)}
            disabled={localItem.action === 'D'}
            className="w-full px-2 py-1 border border-gray-300 rounded text-right focus:ring-2 focus:ring-blue-500"
            min="0"
            step="0.01"
          />
        </td>

        {/* Investment Flag */}
        <td className="px-4 py-2 text-center">
          <input
            type="checkbox"
            checked={localItem.investmentFlag}
            onChange={(e) => handleFieldChange('investmentFlag', e.target.checked)}
            disabled={localItem.action === 'D'}
            className="w-4 h-4 text-blue-600 border-gray-300 rounded focus:ring-blue-500"
          />
        </td>

        {/* Status indicator */}
        <td className="px-4 py-2 text-center">
          {localItem.action === 'I' && (
            <span className="px-2 py-1 text-xs font-semibold text-green-800 bg-green-100 rounded">
              Nový
            </span>
          )}
          {localItem.action === 'U' && localItem.isDirty && (
            <span className="px-2 py-1 text-xs font-semibold text-yellow-800 bg-yellow-100 rounded">
              Změněno
            </span>
          )}
          {localItem.action === 'D' && (
            <span className="px-2 py-1 text-xs font-semibold text-red-800 bg-red-100 rounded">
              Smazáno
            </span>
          )}
        </td>

        {/* Actions */}
        <td className="px-4 py-2 text-center">
          <button
            type="button"
            onClick={handleDeleteClick}
            className="px-3 py-1 text-sm text-white bg-red-600 rounded hover:bg-red-700 focus:ring-2 focus:ring-red-500"
            disabled={localItem.action === 'D'}
          >
            {localItem.id ? 'Smazat' : 'Odstranit'}
          </button>
        </td>
      </tr>

      {/* Expanded detail row */}
      {isExpanded && localItem.action !== 'D' && (
        <tr className="bg-blue-50">
          <td colSpan={10} className="px-8 py-4">
            <div className="grid grid-cols-2 gap-4">
              <div>
                <label className="block text-sm font-medium text-gray-700">
                  ID: <span className="font-mono">{localItem.id || 'Nový'}</span>
                </label>
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700">
                  Akce: <span className="font-mono">{localItem.action}</span>
                </label>
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700">
                  Objem (vypočtený): <span className="font-mono">{localItem.volume.toFixed(2)}</span>
                </label>
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700">
                  Změněno: <span className="font-mono">{localItem.isDirty ? 'Ano' : 'Ne'}</span>
                </label>
              </div>
            </div>
          </td>
        </tr>
      )}
    </>
  );
};
