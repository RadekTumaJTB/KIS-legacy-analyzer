import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import type { BudgetKPIDTO, BudgetSummaryDTO } from '../types/budget';
import { fetchBudgetKPIs, fetchBudgets } from '../api/budgetApi';
import { Button } from '../components/ui/Button';
import { formatCurrency } from '../lib/utils';
import './BudgetDashboard.css';

export default function BudgetDashboard() {
  const [kpis, setKpis] = useState<BudgetKPIDTO | null>(null);
  const [recentBudgets, setRecentBudgets] = useState<BudgetSummaryDTO[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const loadDashboardData = async () => {
      try {
        setLoading(true);
        const [kpisData, budgetsData] = await Promise.all([
          fetchBudgetKPIs(),
          fetchBudgets()
        ]);

        setKpis(kpisData);
        // Show only first 5 budgets on dashboard
        setRecentBudgets(budgetsData.slice(0, 5));
        setError(null);
      } catch (err) {
        setError(err instanceof Error ? err.message : 'Failed to load dashboard data');
      } finally {
        setLoading(false);
      }
    };

    loadDashboardData();
  }, []);

  if (loading) {
    return (
      <div className="budget-dashboard loading">
        <div className="spinner"></div>
        <p>Načítám dashboard...</p>
      </div>
    );
  }

  if (error || !kpis) {
    return (
      <div className="budget-dashboard error">
        <h2>❌ Chyba</h2>
        <p>{error || 'Data nenalezena'}</p>
      </div>
    );
  }

  return (
    <div className="budget-dashboard">
      {/* Header */}
      <div className="dashboard-header">
        <div>
          <h1>💰 Rozpočty - Dashboard</h1>
          <p className="text-gray-600">
            Přehled rozpočtů a klíčových metrik
          </p>
        </div>
        <div className="header-actions">
          <Link to="/budgets">
            <Button variant="secondary">📋 Seznam rozpočtů</Button>
          </Link>
          <Button variant="primary">+ Nový rozpočet</Button>
        </div>
      </div>

      {/* KPI Cards */}
      <div className="kpi-grid">
        {/* Total Planned */}
        <div className="kpi-card">
          <div className="kpi-header">
            <h3>Plánováno celkem</h3>
            <span className="kpi-icon">📊</span>
          </div>
          <div className="kpi-value">{formatCurrency(kpis.totalPlanned, 'CZK')}</div>
          <div className="kpi-subtitle">
            {kpis.totalBudgets} rozpočtů ({kpis.activeBudgets} aktivních)
          </div>
        </div>

        {/* Total Actual */}
        <div className="kpi-card">
          <div className="kpi-header">
            <h3>Skutečné náklady</h3>
            <span className="kpi-icon">💸</span>
          </div>
          <div className="kpi-value">{formatCurrency(kpis.totalActual, 'CZK')}</div>
          <div className="kpi-subtitle">
            {kpis.averageUtilization.toFixed(1)}% průměrné využití
          </div>
        </div>

        {/* Total Variance */}
        <div className={`kpi-card ${kpis.totalVariance > 0 ? 'warning' : 'success'}`}>
          <div className="kpi-header">
            <h3>Celková odchylka</h3>
            <span className="kpi-icon">{kpis.totalVariance > 0 ? '⚠️' : '✅'}</span>
          </div>
          <div className={`kpi-value ${kpis.totalVariance > 0 ? 'text-red-600' : 'text-green-600'}`}>
            {formatCurrency(Math.abs(kpis.totalVariance), 'CZK')}
          </div>
          <div className="kpi-subtitle">
            {kpis.totalVariance > 0 ? 'Překročení rozpočtu' : 'Pod rozpočtem'}
          </div>
        </div>

        {/* Budget Status */}
        <div className="kpi-card">
          <div className="kpi-header">
            <h3>Stav rozpočtů</h3>
            <span className="kpi-icon">📈</span>
          </div>
          <div className="kpi-stats">
            <div className="stat-item">
              <span className="stat-label">Nad rozpočtem:</span>
              <span className="stat-value text-red-600">{kpis.overBudgetCount}</span>
            </div>
            <div className="stat-item">
              <span className="stat-label">Pod rozpočtem:</span>
              <span className="stat-value text-green-600">{kpis.underBudgetCount}</span>
            </div>
          </div>
        </div>
      </div>

      {/* Largest Variance Alert */}
      {kpis.largestVariance !== 0 && (
        <div className={`alert ${kpis.largestVariance > 0 ? 'alert-warning' : 'alert-info'}`}>
          <span className="alert-icon">{kpis.largestVariance > 0 ? '⚠️' : 'ℹ️'}</span>
          <div className="alert-content">
            <strong>Největší odchylka:</strong> {kpis.largestVarianceBudgetName} -{' '}
            {formatCurrency(Math.abs(kpis.largestVariance), 'CZK')}
            {kpis.largestVariance > 0 ? ' nad rozpočtem' : ' pod rozpočtem'}
          </div>
        </div>
      )}

      {/* Recent Budgets */}
      <div className="recent-budgets-section">
        <div className="section-header">
          <h2>📋 Nedávné rozpočty</h2>
          <Link to="/budgets">
            <Button variant="ghost" size="sm">Zobrazit všechny →</Button>
          </Link>
        </div>

        <div className="budgets-table">
          <table>
            <thead>
              <tr>
                <th>Kód</th>
                <th>Název</th>
                <th>Oddělení</th>
                <th>Plánováno</th>
                <th>Skutečné</th>
                <th>Využití</th>
                <th>Status</th>
              </tr>
            </thead>
            <tbody>
              {recentBudgets.map(budget => (
                <tr key={budget.id}>
                  <td>
                    <Link to={`/budgets/${budget.id}`} className="text-blue-600 hover:underline font-semibold">
                      {budget.code}
                    </Link>
                  </td>
                  <td>{budget.name}</td>
                  <td>{budget.departmentName}</td>
                  <td>{formatCurrency(budget.plannedAmount, 'CZK')}</td>
                  <td>{formatCurrency(budget.actualAmount, 'CZK')}</td>
                  <td>
                    <div className="utilization-bar">
                      <div
                        className={`utilization-fill ${
                          budget.utilizationPercent > 100 ? 'over-budget' :
                          budget.utilizationPercent > 90 ? 'warning' :
                          'on-track'
                        }`}
                        style={{ width: `${Math.min(budget.utilizationPercent, 100)}%` }}
                      ></div>
                      <span className="utilization-text">
                        {budget.utilizationPercent.toFixed(1)}%
                      </span>
                    </div>
                  </td>
                  <td>
                    <span className={`status-badge ${budget.status.toLowerCase()}`}>
                      {budget.status}
                    </span>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
}
