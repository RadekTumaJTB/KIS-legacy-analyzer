import { useEffect, useState } from 'react';
import { useParams, Link } from 'react-router-dom';
import type { ProjectDetailDTO, ProjectFormData } from '../types/project';
import { fetchProjectDetail, updateProject } from '../api/projectApi';
import { Button } from '../components/ui/Button';
import EditProjectModal from '../components/EditProjectModal';
import { formatCurrency, formatDate, cn } from '../lib/utils';
import './ProjectDetailPage.css';

export default function ProjectDetailPage() {
  const { id } = useParams<{ id: string }>();
  const [project, setProject] = useState<ProjectDetailDTO | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [isEditModalOpen, setIsEditModalOpen] = useState(false);

  useEffect(() => {
    const loadProject = async () => {
      if (!id) return;

      try {
        setLoading(true);
        const result = await fetchProjectDetail(parseInt(id));
        setProject(result);
        setError(null);
      } catch (err) {
        setError(err instanceof Error ? err.message : 'Failed to load project');
      } finally {
        setLoading(false);
      }
    };

    loadProject();
  }, [id]);

  const handleUpdateProject = async (projectId: number, data: ProjectFormData) => {
    try {
      const updated = await updateProject(projectId, data);
      setProject(updated);
      alert('Projekt byl úspěšně aktualizován');
    } catch (error) {
      console.error('Failed to update project:', error);
      alert('Nepodařilo se aktualizovat projekt');
      throw error;
    }
  };

  if (loading) {
    return (
      <div className="project-detail loading">
        <div className="spinner"></div>
        <p>Načítám projekt...</p>
      </div>
    );
  }

  if (error || !project) {
    return (
      <div className="project-detail error">
        <h2>Chyba</h2>
        <p>{error || 'Projekt nenalezen'}</p>
      </div>
    );
  }

  return (
    <div className="project-detail">
      {/* Header */}
      <div className="detail-header">
        <div className="header-info">
          <div className="breadcrumb">
            <Link to="/projects" className="breadcrumb-link">Projekty</Link>
            <span className="breadcrumb-separator">›</span>
            <span className="breadcrumb-current">{project.projectNumber}</span>
          </div>
          <h1>{project.name}</h1>
          <div className="header-status">
            <span className={cn(
              'status-badge',
              {
                'bg-green-100 text-green-800': project.status === 'Aktivní',
                'bg-yellow-100 text-yellow-800': project.status === 'V přípravě',
                'bg-blue-100 text-blue-800': project.status === 'Probíhá',
                'bg-gray-100 text-gray-800': project.status === 'Pozastaveno',
                'bg-red-100 text-red-800': project.status === 'Ukončeno',
              }
            )}>
              {project.status}
            </span>
          </div>
        </div>
        <div className="header-actions">
          <Link to="/projects">
            <Button variant="secondary">Zpět na seznam</Button>
          </Link>
          <Button variant="primary" onClick={() => setIsEditModalOpen(true)}>
            Upravit
          </Button>
        </div>
      </div>

      {/* Main Content - Two Column Layout */}
      <div className="detail-content">
        {/* Left Column - Project Information */}
        <div className="project-info-section">
          <h2>Informace o projektu</h2>

          <div className="info-grid">
            <div className="info-item">
              <label>Číslo projektu</label>
              <div className="info-value">{project.projectNumber}</div>
            </div>

            {project.oldProjectNumber && (
              <div className="info-item">
                <label>Staré číslo projektu</label>
                <div className="info-value">{project.oldProjectNumber}</div>
              </div>
            )}

            <div className="info-item">
              <label>Projektový manažer</label>
              <div className="info-value">{project.projectManagerName}</div>
            </div>

            {project.proposedByName && (
              <div className="info-item">
                <label>Navrhuje</label>
                <div className="info-value">{project.proposedByName}</div>
              </div>
            )}

            <div className="info-item">
              <label>Oddělení</label>
              <div className="info-value">{project.managementSegmentName}</div>
            </div>

            <div className="info-item">
              <label>Měna</label>
              <div className="info-value">{project.currencyCode}</div>
            </div>

            <div className="info-item">
              <label>Datum zahájení přecenění</label>
              <div className="info-value">{formatDate(project.valuationStartDate)}</div>
            </div>

            {project.valuationEndDate && (
              <div className="info-item">
                <label>Datum konce přecenění</label>
                <div className="info-value">{formatDate(project.valuationEndDate)}</div>
              </div>
            )}

            <div className="info-item">
              <label>Frekvence</label>
              <div className="info-value">{project.frequencyName}</div>
            </div>

            <div className="info-item">
              <label>Kategorie</label>
              <div className="info-value">{project.categoryName}</div>
            </div>

            <div className="info-item">
              <label>Typ rozpočtu</label>
              <div className="info-value">{project.budgetTypeName}</div>
            </div>

            <div className="info-item">
              <label>Typ zůstatku</label>
              <div className="info-value">{project.balanceTypeName}</div>
            </div>

            <div className="info-item">
              <label>Sledování rozpočtu</label>
              <div className="info-value">
                {project.budgetTrackingFlag === 'A' || project.budgetTrackingFlag === '1' ? 'Ano' : 'Ne'}
              </div>
            </div>

            {project.nextProjectCardReport && (
              <div className="info-item">
                <label>Další zpráva karty projektu</label>
                <div className="info-value">{formatDate(project.nextProjectCardReport)}</div>
              </div>
            )}

            {project.reportPeriodMonths && (
              <div className="info-item">
                <label>Perioda zpráv</label>
                <div className="info-value">{project.reportPeriodMonths} měsíců</div>
              </div>
            )}
          </div>

          {/* Approval Levels */}
          <div className="approval-levels">
            <h3>Schvalovací úrovně</h3>
            <div className="approval-grid">
              <div className="approval-item">
                <label>Úroveň 1</label>
                <div className="approval-value">
                  {formatCurrency(project.approvalLevel1Amount, project.currencyCode)}
                </div>
              </div>
              <div className="approval-item">
                <label>Úroveň 2</label>
                <div className="approval-value">
                  {formatCurrency(project.approvalLevel2Amount, project.currencyCode)}
                </div>
              </div>
              <div className="approval-item">
                <label>Úroveň 3</label>
                <div className="approval-value">
                  {formatCurrency(project.approvalLevel3Amount, project.currencyCode)}
                </div>
              </div>
            </div>
          </div>

          {/* Budget Increases */}
          <div className="budget-increases">
            <h3>Navýšení rozpočtu</h3>
            <div className="budget-grid">
              <div className="budget-item">
                <label>PM navýšení</label>
                <div className="budget-value">
                  {formatCurrency(project.budgetIncreaseAmountPM, project.currencyCode)}
                </div>
              </div>
              <div className="budget-item">
                <label>Top navýšení</label>
                <div className="budget-value">
                  {formatCurrency(project.budgetIncreaseAmountTop, project.currencyCode)}
                </div>
              </div>
            </div>
          </div>

          {/* Description */}
          {project.description && (
            <div className="description-section">
              <h3>Popis</h3>
              <p>{project.description}</p>
            </div>
          )}

          {/* Metadata */}
          <div className="metadata">
            <div className="metadata-item">
              <label>Vytvořeno</label>
              <div className="metadata-value">{formatDate(project.createdAt, 'long')}</div>
              <div className="metadata-subtitle">Autor: {project.createdBy}</div>
            </div>
            <div className="metadata-item">
              <label>Aktualizováno</label>
              <div className="metadata-value">{formatDate(project.updatedAt, 'long')}</div>
            </div>
          </div>
        </div>

        {/* Right Column - Cash Flow List */}
        <div className="cashflow-section">
          <h2>Cash Flow</h2>

          {project.cashFlowList.length === 0 ? (
            <div className="empty-state">
              <p>Žádné cash flow záznamy</p>
            </div>
          ) : (
            <div className="cashflow-table">
              <table>
                <thead>
                  <tr>
                    <th>Datum</th>
                    <th>Typ</th>
                    <th>Částka</th>
                    <th>Směr</th>
                    <th>Poznámky</th>
                  </tr>
                </thead>
                <tbody>
                  {project.cashFlowList.map((cashFlow) => (
                    <tr key={cashFlow.id}>
                      <td>
                        <span className="cashflow-date">
                          {formatDate(cashFlow.date)}
                        </span>
                      </td>
                      <td>
                        <span className="cashflow-type">{cashFlow.cashFlowTypeName}</span>
                        <div className="cashflow-position text-xs text-gray-500">
                          {cashFlow.positionTypeName}
                        </div>
                      </td>
                      <td>
                        <span className={cn('cashflow-amount', {
                          'text-green-600': cashFlow.inOutTypeName === 'Příjem',
                          'text-red-600': cashFlow.inOutTypeName === 'Výdaj',
                        })}>
                          {cashFlow.inOutTypeName === 'Příjem' ? '+' : '-'}
                          {formatCurrency(cashFlow.amount, cashFlow.currencyCode)}
                        </span>
                      </td>
                      <td>
                        <span className={cn(
                          'inline-flex items-center px-2 py-0.5 rounded-full text-xs font-medium',
                          {
                            'bg-green-100 text-green-800': cashFlow.inOutTypeName === 'Příjem',
                            'bg-red-100 text-red-800': cashFlow.inOutTypeName === 'Výdaj',
                          }
                        )}>
                          {cashFlow.inOutTypeName}
                        </span>
                      </td>
                      <td>
                        <span className="text-sm text-gray-600">{cashFlow.notes || '-'}</span>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          )}
        </div>
      </div>

      {/* Edit Project Modal */}
      <EditProjectModal
        isOpen={isEditModalOpen}
        project={project}
        onClose={() => setIsEditModalOpen(false)}
        onSubmit={handleUpdateProject}
      />
    </div>
  );
}
