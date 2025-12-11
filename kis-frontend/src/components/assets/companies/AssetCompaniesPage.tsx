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
import type { CompanyWithPermissions } from '../../../types/asset';
import * as assetApi from '../../../api/assetApi';

export const AssetCompaniesPage: React.FC = () => {
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

  return (
    <div className="container mx-auto px-4 py-8">
      <div className="mb-6">
        <h1 className="text-3xl font-bold text-gray-900">
          Správa Majetkových Účastí
        </h1>
        <p className="text-gray-600 mt-2">
          Výběr společnosti pro správu majetkových účastí
        </p>
      </div>

      {/* Error Message */}
      {error && (
        <div className="mb-4 p-4 bg-red-100 border border-red-400 text-red-700 rounded">
          {error}
        </div>
      )}

      {/* Statistics and Search */}
      <div className="bg-white rounded-lg shadow mb-6">
        <div className="p-6">
          <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
            {/* Statistics */}
            <div>
              <h2 className="text-lg font-semibold mb-4">Přehled oprávnění</h2>
              <div className="space-y-2">
                <div className="flex items-center justify-between">
                  <span className="text-gray-600">Celkem společností:</span>
                  <span className="font-bold text-gray-900">{companies.length}</span>
                </div>
                <div className="flex items-center justify-between">
                  <span className="text-gray-600">S právem úprav:</span>
                  <span className="font-bold text-green-600">{editableCount}</span>
                </div>
                <div className="flex items-center justify-between">
                  <span className="text-gray-600">Pouze prohlížení:</span>
                  <span className="font-bold text-blue-600">{viewOnlyCount}</span>
                </div>
              </div>
            </div>

            {/* Search */}
            <div>
              <h2 className="text-lg font-semibold mb-4">Vyhledávání</h2>
              <input
                type="text"
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
                placeholder="Hledat podle názvu nebo IČO..."
                className="w-full px-4 py-2 border border-gray-300 rounded-md focus:ring-2 focus:ring-blue-500"
              />
              <p className="mt-2 text-sm text-gray-500">
                Zobrazeno: {filteredCompanies.length} z {companies.length} společností
              </p>
            </div>
          </div>
        </div>
      </div>

      {/* Companies Grid */}
      <div className="bg-white rounded-lg shadow">
        {isLoading ? (
          <div className="text-center py-12">
            <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600 mx-auto"></div>
            <p className="mt-4 text-gray-600">Načítání společností...</p>
          </div>
        ) : filteredCompanies.length === 0 ? (
          <div className="text-center py-12 text-gray-500">
            <p className="text-lg">
              {searchTerm
                ? 'Žádné společnosti neodpovídají vyhledávání'
                : 'Nemáte přístup k žádným společnostem'}
            </p>
          </div>
        ) : (
          <div className="overflow-x-auto">
            <table className="min-w-full divide-y divide-gray-200">
              <thead className="bg-gray-50">
                <tr>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Název společnosti
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    IČO
                  </th>
                  <th className="px-6 py-3 text-center text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Oprávnění
                  </th>
                  <th className="px-6 py-3 text-center text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Akce
                  </th>
                </tr>
              </thead>
              <tbody className="bg-white divide-y divide-gray-200">
                {filteredCompanies.map((company) => (
                  <tr
                    key={company.id}
                    className="hover:bg-gray-50 transition-colors cursor-pointer"
                    onClick={() => handleCompanyClick(company)}
                  >
                    <td className="px-6 py-4">
                      <div className="text-sm font-medium text-gray-900">
                        {company.name}
                      </div>
                    </td>
                    <td className="px-6 py-4">
                      <div className="text-sm text-gray-600 font-mono">
                        {company.ico}
                      </div>
                    </td>
                    <td className="px-6 py-4 text-center">
                      <div className="flex items-center justify-center gap-2">
                        {company.canView && (
                          <span className="px-2 py-1 text-xs font-semibold text-blue-800 bg-blue-100 rounded">
                            Zobrazit
                          </span>
                        )}
                        {company.canEdit && (
                          <span className="px-2 py-1 text-xs font-semibold text-green-800 bg-green-100 rounded">
                            Upravit
                          </span>
                        )}
                      </div>
                    </td>
                    <td className="px-6 py-4 text-center">
                      <button
                        onClick={(e) => {
                          e.stopPropagation();
                          handleCompanyClick(company);
                        }}
                        disabled={!company.canView}
                        className="px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 disabled:bg-gray-400 disabled:cursor-not-allowed"
                      >
                        Otevřít
                      </button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </div>

      {/* Info Panel */}
      <div className="mt-6 bg-blue-50 border border-blue-200 rounded-lg p-4">
        <h3 className="text-sm font-semibold text-blue-800 mb-2">
          Informace o oprávněních:
        </h3>
        <ul className="text-sm text-blue-700 space-y-1">
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
};
