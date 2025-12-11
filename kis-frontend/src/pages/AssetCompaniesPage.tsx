/**
 * AssetCompaniesPage Component
 *
 * Role-based company selection for Asset Management
 * Replaces legacy Majetek.jsp
 *
 * Displays companies based on user permissions:
 * - Admin_MU: All companies with full edit
 * - MU_jednotlive: Specific companies with edit
 * - MU_konsolidovane: Consolidated companies with edit
 * - MU_view_only: View-only access
 */

import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import type { CompanyWithPermissions } from '../types/asset';
import * as assetApi from '../api/assetApi';
import { Button } from '../components/ui/Button';
import { Input } from '../components/ui/Input';
import './AssetCompaniesPage.css';

export default function AssetCompaniesPage() {
  const navigate = useNavigate();
  const [companies, setCompanies] = useState<CompanyWithPermissions[]>([]);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [searchTerm, setSearchTerm] = useState('');

  useEffect(() => {
    loadCompanies();
  }, []);

  const loadCompanies = async () => {
    setIsLoading(true);
    setError(null);
    try {
      const data = await assetApi.getCompaniesWithRoleFiltering();
      setCompanies(data);
    } catch (err) {
      setError('Chyba při načítání společností: ' + (err as Error).message);
    } finally {
      setIsLoading(false);
    }
  };

  const handleCompanyClick = (company: CompanyWithPermissions) => {
    if (!company.canView) {
      setError('Nemáte oprávnění k zobrazení této společnosti');
      return;
    }
    // Navigate to participation page for this company
    navigate(`/assets/participations/${company.id}`);
  };

  // Filter companies by search term
  const filteredCompanies = companies.filter(company =>
    company.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
    company.ico.includes(searchTerm)
  );

  // Count companies by permission
  const viewOnlyCount = companies.filter(c => c.canView && !c.canEdit).length;
  const editableCount = companies.filter(c => c.canEdit).length;

  if (isLoading) {
    return (
      <div className="asset-companies-page loading">
        <div className="spinner"></div>
        <p>Načítám společnosti...</p>
      </div>
    );
  }

  if (error && companies.length === 0) {
    return (
      <div className="asset-companies-page error">
        <h2>Chyba</h2>
        <p>{error}</p>
        <Button onClick={loadCompanies}>Zkusit znovu</Button>
      </div>
    );
  }

  return (
    <div className="asset-companies-page">
      {/* Header */}
      <div className="list-header">
        <div>
          <h1>Správa Majetkových Účastí</h1>
          <p className="text-gray-600">
            Výběr společnosti pro správu majetkových účastí
          </p>
        </div>
      </div>

      {/* Error Message */}
      {error && (
        <div className="alert alert-error">
          {error}
        </div>
      )}

      {/* Statistics Panel */}
      <div className="statistics-panel">
        <div className="statistics-grid">
          <div className="stat-item">
            <span className="stat-label">Celkem společností</span>
            <span className="stat-value">{companies.length}</span>
          </div>
          <div className="stat-item">
            <span className="stat-label">S právem úprav</span>
            <span className="stat-value success">{editableCount}</span>
          </div>
          <div className="stat-item">
            <span className="stat-label">Pouze prohlížení</span>
            <span className="stat-value info">{viewOnlyCount}</span>
          </div>
        </div>
      </div>

      {/* Search & Filters */}
      <div className="search-filters">
        <div className="search-box">
          <Input
            type="search"
            placeholder="Hledat podle názvu nebo IČO..."
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
            className="w-full"
          />
        </div>
        <div className="filters">
          <span className="text-sm text-gray-600">
            Zobrazeno: {filteredCompanies.length} z {companies.length}
          </span>
        </div>
      </div>

      {/* Companies Table */}
      <div className="table-container">
        {filteredCompanies.length === 0 ? (
          <div className="empty-state">
            <p>
              {searchTerm
                ? 'Žádné společnosti neodpovídají vyhledávání'
                : 'Nemáte přístup k žádným společnostem'}
            </p>
          </div>
        ) : (
          <table className="data-table">
            <thead>
              <tr>
                <th>Název společnosti</th>
                <th>IČO</th>
                <th className="text-center">Oprávnění</th>
                <th className="text-center">Akce</th>
              </tr>
            </thead>
            <tbody>
              {filteredCompanies.map((company) => (
                <tr
                  key={company.id}
                  onClick={() => handleCompanyClick(company)}
                >
                  <td>
                    <span className="company-name">{company.name}</span>
                  </td>
                  <td>
                    <span className="company-ico">{company.ico}</span>
                  </td>
                  <td className="text-center">
                    <div className="permission-badges">
                      {company.canView && (
                        <span className="permission-badge view">
                          Zobrazit
                        </span>
                      )}
                      {company.canEdit && (
                        <span className="permission-badge edit">
                          Upravit
                        </span>
                      )}
                    </div>
                  </td>
                  <td className="text-center">
                    <Button
                      onClick={(e) => {
                        e.stopPropagation();
                        handleCompanyClick(company);
                      }}
                      disabled={!company.canView}
                      variant="primary"
                      size="sm"
                    >
                      Otevřít
                    </Button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </div>

      {/* Info Panel */}
      <div className="info-panel">
        <h3>Informace o oprávněních:</h3>
        <ul>
          <li>
            • <strong>Admin_MU:</strong> Přístup ke všem společnostem s možností úprav
          </li>
          <li>
            • <strong>MU_jednotlive:</strong> Přístup k vybraným jednotlivým společnostem
          </li>
          <li>
            • <strong>MU_konsolidovane:</strong> Přístup ke konsolidovaným společnostem
          </li>
          <li>
            • <strong>MU_view_only:</strong> Pouze prohlížení bez možnosti úprav
          </li>
        </ul>
      </div>
    </div>
  );
}
