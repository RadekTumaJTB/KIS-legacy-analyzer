import { useState } from 'react';
import { Button } from './ui/Button';
import { Input } from './ui/Input';
import './DocumentFiltersPanel.css';

export interface DocumentFilters {
  status: string[];
  type: string[];
  dateFrom: string;
  dateTo: string;
  amountMin: string;
  amountMax: string;
  companyName: string;
  createdBy: string;
}

interface DocumentFiltersPanelProps {
  filters: DocumentFilters;
  onFiltersChange: (filters: DocumentFilters) => void;
  onClearFilters: () => void;
}

const statusOptions = [
  { value: 'PENDING_APPROVAL', label: 'Čeká na schválení' },
  { value: 'APPROVED', label: 'Schváleno' },
  { value: 'OVERDUE', label: 'Po splatnosti' },
  { value: 'DRAFT', label: 'Koncept' },
];

const typeOptions = [
  { value: 'INVOICE', label: 'Faktura' },
  { value: 'CREDIT_NOTE', label: 'Dobropis' },
  { value: 'PAYMENT', label: 'Platba' },
  { value: 'CONTRACT', label: 'Smlouva' },
];

export default function DocumentFiltersPanel({
  filters,
  onFiltersChange,
  onClearFilters,
}: DocumentFiltersPanelProps) {
  const [isExpanded, setIsExpanded] = useState(false);

  const handleStatusChange = (status: string) => {
    const newStatuses = filters.status.includes(status)
      ? filters.status.filter(s => s !== status)
      : [...filters.status, status];

    onFiltersChange({ ...filters, status: newStatuses });
  };

  const handleTypeChange = (type: string) => {
    const newTypes = filters.type.includes(type)
      ? filters.type.filter(t => t !== type)
      : [...filters.type, type];

    onFiltersChange({ ...filters, type: newTypes });
  };

  const handleInputChange = (field: keyof DocumentFilters, value: string) => {
    onFiltersChange({ ...filters, [field]: value });
  };

  const activeFiltersCount =
    filters.status.length +
    filters.type.length +
    (filters.dateFrom ? 1 : 0) +
    (filters.dateTo ? 1 : 0) +
    (filters.amountMin ? 1 : 0) +
    (filters.amountMax ? 1 : 0) +
    (filters.companyName ? 1 : 0) +
    (filters.createdBy ? 1 : 0);

  return (
    <div className="filters-panel">
      <div className="filters-header">
        <button
          className="filters-toggle"
          onClick={() => setIsExpanded(!isExpanded)}
        >
          <span className="filters-icon">{isExpanded ? '▼' : '▶'}</span>
          <span className="filters-title">
            🔍 Pokročilé filtry
            {activeFiltersCount > 0 && (
              <span className="filters-badge">{activeFiltersCount}</span>
            )}
          </span>
        </button>
        {activeFiltersCount > 0 && (
          <Button
            variant="ghost"
            size="sm"
            onClick={onClearFilters}
          >
            ✗ Vymazat všechny filtry
          </Button>
        )}
      </div>

      {isExpanded && (
        <div className="filters-content">
          {/* Status Filter */}
          <div className="filter-group">
            <label className="filter-label">Status</label>
            <div className="filter-checkboxes">
              {statusOptions.map(option => (
                <label key={option.value} className="checkbox-label">
                  <input
                    type="checkbox"
                    checked={filters.status.includes(option.value)}
                    onChange={() => handleStatusChange(option.value)}
                    className="checkbox-input"
                  />
                  <span className="checkbox-text">{option.label}</span>
                </label>
              ))}
            </div>
          </div>

          {/* Type Filter */}
          <div className="filter-group">
            <label className="filter-label">Typ dokumentu</label>
            <div className="filter-checkboxes">
              {typeOptions.map(option => (
                <label key={option.value} className="checkbox-label">
                  <input
                    type="checkbox"
                    checked={filters.type.includes(option.value)}
                    onChange={() => handleTypeChange(option.value)}
                    className="checkbox-input"
                  />
                  <span className="checkbox-text">{option.label}</span>
                </label>
              ))}
            </div>
          </div>

          {/* Date Range Filter */}
          <div className="filter-group">
            <label className="filter-label">Datum splatnosti</label>
            <div className="filter-row">
              <div className="filter-input-group">
                <label className="input-label">Od</label>
                <Input
                  type="date"
                  value={filters.dateFrom}
                  onChange={(e) => handleInputChange('dateFrom', e.target.value)}
                />
              </div>
              <div className="filter-input-group">
                <label className="input-label">Do</label>
                <Input
                  type="date"
                  value={filters.dateTo}
                  onChange={(e) => handleInputChange('dateTo', e.target.value)}
                />
              </div>
            </div>
          </div>

          {/* Amount Range Filter */}
          <div className="filter-group">
            <label className="filter-label">Částka (CZK)</label>
            <div className="filter-row">
              <div className="filter-input-group">
                <label className="input-label">Min</label>
                <Input
                  type="number"
                  placeholder="0"
                  value={filters.amountMin}
                  onChange={(e) => handleInputChange('amountMin', e.target.value)}
                />
              </div>
              <div className="filter-input-group">
                <label className="input-label">Max</label>
                <Input
                  type="number"
                  placeholder="∞"
                  value={filters.amountMax}
                  onChange={(e) => handleInputChange('amountMax', e.target.value)}
                />
              </div>
            </div>
          </div>

          {/* Company Name Filter */}
          <div className="filter-group">
            <label className="filter-label">Společnost</label>
            <Input
              type="text"
              placeholder="Název společnosti..."
              value={filters.companyName}
              onChange={(e) => handleInputChange('companyName', e.target.value)}
            />
          </div>

          {/* Created By Filter */}
          <div className="filter-group">
            <label className="filter-label">Vytvořil</label>
            <Input
              type="text"
              placeholder="Jméno uživatele..."
              value={filters.createdBy}
              onChange={(e) => handleInputChange('createdBy', e.target.value)}
            />
          </div>

          {/* Active Filters Summary */}
          {activeFiltersCount > 0 && (
            <div className="active-filters-summary">
              <div className="summary-header">
                <span className="summary-title">Aktivní filtry:</span>
                <Button variant="ghost" size="sm" onClick={onClearFilters}>
                  Vymazat vše
                </Button>
              </div>
              <div className="filter-chips">
                {filters.status.map(status => (
                  <div key={status} className="filter-chip">
                    <span>{statusOptions.find(o => o.value === status)?.label}</span>
                    <button
                      className="chip-remove"
                      onClick={() => handleStatusChange(status)}
                    >
                      ×
                    </button>
                  </div>
                ))}
                {filters.type.map(type => (
                  <div key={type} className="filter-chip">
                    <span>{typeOptions.find(o => o.value === type)?.label}</span>
                    <button
                      className="chip-remove"
                      onClick={() => handleTypeChange(type)}
                    >
                      ×
                    </button>
                  </div>
                ))}
                {filters.dateFrom && (
                  <div className="filter-chip">
                    <span>Od: {filters.dateFrom}</span>
                    <button
                      className="chip-remove"
                      onClick={() => handleInputChange('dateFrom', '')}
                    >
                      ×
                    </button>
                  </div>
                )}
                {filters.dateTo && (
                  <div className="filter-chip">
                    <span>Do: {filters.dateTo}</span>
                    <button
                      className="chip-remove"
                      onClick={() => handleInputChange('dateTo', '')}
                    >
                      ×
                    </button>
                  </div>
                )}
                {filters.amountMin && (
                  <div className="filter-chip">
                    <span>Min: {filters.amountMin} CZK</span>
                    <button
                      className="chip-remove"
                      onClick={() => handleInputChange('amountMin', '')}
                    >
                      ×
                    </button>
                  </div>
                )}
                {filters.amountMax && (
                  <div className="filter-chip">
                    <span>Max: {filters.amountMax} CZK</span>
                    <button
                      className="chip-remove"
                      onClick={() => handleInputChange('amountMax', '')}
                    >
                      ×
                    </button>
                  </div>
                )}
                {filters.companyName && (
                  <div className="filter-chip">
                    <span>Společnost: {filters.companyName}</span>
                    <button
                      className="chip-remove"
                      onClick={() => handleInputChange('companyName', '')}
                    >
                      ×
                    </button>
                  </div>
                )}
                {filters.createdBy && (
                  <div className="filter-chip">
                    <span>Vytvořil: {filters.createdBy}</span>
                    <button
                      className="chip-remove"
                      onClick={() => handleInputChange('createdBy', '')}
                    >
                      ×
                    </button>
                  </div>
                )}
              </div>
            </div>
          )}
        </div>
      )}
    </div>
  );
}
