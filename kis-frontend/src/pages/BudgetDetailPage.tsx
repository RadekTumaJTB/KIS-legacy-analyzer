import { useEffect, useState } from 'react';
import { useParams, Link } from 'react-router-dom';
import type { BudgetDetailDTO } from '../types/budget';
import { fetchBudgetDetail, updateBudget } from '../api/budgetApi';
import { Button } from '../components/ui/Button';
import EditBudgetModal from '../components/EditBudgetModal';
import { formatCurrency, formatDate, cn } from '../lib/utils';
import './BudgetDetailPage.css';

export default function BudgetDetailPage() {
  const { id } = useParams<{ id: string }>();
  const [budget, setBudget] = useState<BudgetDetailDTO | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [isEditModalOpen, setIsEditModalOpen] = useState(false);

  useEffect(() => {
    const loadBudget = async () => {
      if (!id) return;

      try {
        setLoading(true);
        const result = await fetchBudgetDetail(parseInt(id));
        setBudget(result);
        setError(null);
      } catch (err) {
        setError(err instanceof Error ? err.message : 'Failed to load budget');
      } finally {
        setLoading(false);
      }
    };

    loadBudget();
  }, [id]);

  const handleUpdateBudget = async (budgetId: number, data: {
    name: string;
    plannedAmount: string;
    description?: string;
  }) => {
    try {
      const updated = await updateBudget(budgetId, data);
      setBudget(updated);
      alert('✓ Rozpočet byl úspěšně aktualizován');
    } catch (error) {
      console.error('Failed to update budget:', error);
      alert('Nepodařilo se aktualizovat rozpočet');
      throw error;
    }
  };

  if (loading) {
    return (
      <div className="budget-detail loading">
        <div className="spinner"></div>
        <p>Načítám rozpočet...</p>
      </div>
    );
  }

  if (error || !budget) {
    return (
      <div className="budget-detail error">
        <h2>❌ Chyba</h2>
        <p>{error || 'Rozpočet nenalezen'}</p>
      </div>
    );
  }

  return (
    <div className="budget-detail">
      {/* Header */}
      <div className="detail-header">
        <div className="header-info">
          <div className="breadcrumb">
            <Link to="/budgets" className="breadcrumb-link">Rozpočty</Link>
            <span className="breadcrumb-separator">›</span>
            <span className="breadcrumb-current">{budget.code}</span>
          </div>
          <h1>💰 {budget.name}</h1>
          <p className="text-gray-600">{budget.description}</p>
        </div>
        <div className="header-actions">
          <Link to="/budgets">
            <Button variant="secondary">← Zpět na seznam</Button>
          </Link>
          <Button variant="primary" onClick={() => setIsEditModalOpen(true)}>
            ✏️ Upravit
          </Button>
        </div>
      </div>

      {/* Summary Cards */}
      <div className="summary-grid">
        <div className="summary-card">
          <h3>Plánovaná částka</h3>
          <div className="summary-value">{formatCurrency(budget.totalPlanned, 'CZK')}</div>
          <div className="summary-meta">
            {budget.year} • {budget.type}
          </div>
        </div>

        <div className="summary-card">
          <h3>Skutečné náklady</h3>
          <div className="summary-value text-blue-600">
            {formatCurrency(budget.totalActual, 'CZK')}
          </div>
          <div className="summary-meta">
            {budget.utilizationPercent.toFixed(1)}% využití
          </div>
        </div>

        <div className={`summary-card ${budget.totalVariance > 0 ? 'warning' : 'success'}`}>
          <h3>Odchylka</h3>
          <div className={`summary-value ${budget.totalVariance > 0 ? 'text-red-600' : 'text-green-600'}`}>
            {formatCurrency(Math.abs(budget.totalVariance), 'CZK')}
          </div>
          <div className="summary-meta">
            {budget.totalVariance > 0 ? '⚠️ Nad rozpočtem' : '✅ Pod rozpočtem'}
          </div>
        </div>

        <div className="summary-card">
          <h3>Status</h3>
          <div className="summary-value">
            <span className={`status-badge ${budget.status.toLowerCase()}`}>
              {budget.status}
            </span>
          </div>
          <div className="summary-meta">
            {budget.departmentName}
          </div>
        </div>
      </div>

      {/* Monthly Breakdown */}
      <div className="monthly-breakdown-section">
        <h2>📊 Měsíční rozpad</h2>

        <div className="line-items-table">
          <table>
            <thead>
              <tr>
                <th>Měsíc</th>
                <th>Plánováno</th>
                <th>Skutečné</th>
                <th>Odchylka</th>
                <th>Využití</th>
                <th>Status</th>
                <th>Poznámky</th>
              </tr>
            </thead>
            <tbody>
              {budget.lineItems.map((item) => (
                <tr key={item.id} className={`row-${item.status.toLowerCase()}`}>
                  <td>
                    <span className="font-semibold">{item.monthName}</span>
                  </td>
                  <td>{formatCurrency(item.plannedAmount, 'CZK')}</td>
                  <td>
                    <span className={cn('font-semibold', {
                      'text-green-600': item.actualAmount < item.plannedAmount,
                      'text-red-600': item.actualAmount > item.plannedAmount,
                    })}>
                      {formatCurrency(item.actualAmount, 'CZK')}
                    </span>
                  </td>
                  <td>
                    <span className={cn('font-semibold', {
                      'text-green-600': item.variance < 0,
                      'text-red-600': item.variance > 0,
                    })}>
                      {item.variance > 0 ? '+' : ''}{formatCurrency(item.variance, 'CZK')}
                    </span>
                  </td>
                  <td>
                    <div className="utilization-cell">
                      <div className="utilization-bar-small">
                        <div
                          className={cn('utilization-fill', {
                            'on-track': item.utilizationPercent <= 95,
                            'warning': item.utilizationPercent > 95 && item.utilizationPercent <= 105,
                            'over-budget': item.utilizationPercent > 105,
                          })}
                          style={{ width: `${Math.min(item.utilizationPercent, 100)}%` }}
                        ></div>
                      </div>
                      <span className="utilization-text-small">
                        {item.utilizationPercent.toFixed(1)}%
                      </span>
                    </div>
                  </td>
                  <td>
                    <span className={cn(
                      'status-badge-small',
                      {
                        'bg-green-100 text-green-800': item.status === 'ON_TRACK',
                        'bg-yellow-100 text-yellow-800': item.status === 'WARNING',
                        'bg-red-100 text-red-800': item.status === 'OVER_BUDGET',
                        'bg-blue-100 text-blue-800': item.status === 'UNDER_BUDGET',
                        'bg-gray-100 text-gray-800': item.status === 'PENDING',
                      }
                    )}>
                      {item.status.replace('_', ' ')}
                    </span>
                  </td>
                  <td>
                    <span className="text-sm text-gray-600">{item.notes || '-'}</span>
                  </td>
                </tr>
              ))}
            </tbody>
            <tfoot>
              <tr className="total-row">
                <td><strong>Celkem</strong></td>
                <td><strong>{formatCurrency(budget.totalPlanned, 'CZK')}</strong></td>
                <td><strong>{formatCurrency(budget.totalActual, 'CZK')}</strong></td>
                <td>
                  <strong className={budget.totalVariance > 0 ? 'text-red-600' : 'text-green-600'}>
                    {budget.totalVariance > 0 ? '+' : ''}{formatCurrency(budget.totalVariance, 'CZK')}
                  </strong>
                </td>
                <td>
                  <strong>{budget.utilizationPercent.toFixed(1)}%</strong>
                </td>
                <td colSpan={2}></td>
              </tr>
            </tfoot>
          </table>
        </div>
      </div>

      {/* Metadata */}
      <div className="metadata-section">
        <div className="metadata-card">
          <h3>👤 Vlastník</h3>
          <div className="metadata-value">{budget.owner.name}</div>
          <div className="metadata-subtitle">{budget.owner.position}</div>
          <div className="metadata-subtitle text-sm text-gray-500">{budget.owner.email}</div>
        </div>

        <div className="metadata-card">
          <h3>📅 Platnost</h3>
          <div className="metadata-value">
            {formatDate(budget.validFrom)} - {formatDate(budget.validTo)}
          </div>
          <div className="metadata-subtitle">Rok {budget.year}</div>
        </div>

        <div className="metadata-card">
          <h3>🏢 Oddělení</h3>
          <div className="metadata-value">{budget.departmentName}</div>
        </div>

        <div className="metadata-card">
          <h3>📝 Naposledy upraveno</h3>
          <div className="metadata-value">{formatDate(budget.updatedAt)}</div>
          <div className="metadata-subtitle">Vytvořeno: {formatDate(budget.createdAt)}</div>
        </div>
      </div>

      {/* Notes */}
      {budget.notes && (
        <div className="notes-section">
          <h3>📌 Poznámky</h3>
          <p>{budget.notes}</p>
        </div>
      )}

      {/* Edit Budget Modal */}
      <EditBudgetModal
        isOpen={isEditModalOpen}
        budget={budget}
        onClose={() => setIsEditModalOpen(false)}
        onSubmit={handleUpdateBudget}
      />
    </div>
  );
}
