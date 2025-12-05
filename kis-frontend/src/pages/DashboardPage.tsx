import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import type { DocumentSummaryDTO } from '../types/document';
import { fetchDocuments } from '../api/documentApi';
import './DashboardPage.css';

export default function DashboardPage() {
  const [documents, setDocuments] = useState<DocumentSummaryDTO[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const loadDocuments = async () => {
      try {
        const result = await fetchDocuments();
        setDocuments(result);
      } catch (err) {
        console.error('Failed to load documents', err);
      } finally {
        setLoading(false);
      }
    };

    loadDocuments();
  }, []);

  const pendingCount = documents.filter(d => d.status === 'PENDING_APPROVAL').length;
  const approvedCount = documents.filter(d => d.status === 'APPROVED').length;
  const overdueCount = documents.filter(d => d.status === 'OVERDUE').length;
  const totalAmount = documents.reduce((sum, d) => sum + d.amount, 0);

  return (
    <div className="dashboard">
      <h1>📊 Dashboard</h1>

      {loading ? (
        <div className="loading">Načítám data...</div>
      ) : (
        <>
          <div className="stats-grid">
            <div className="stat-card">
              <div className="stat-icon">📄</div>
              <div className="stat-content">
                <div className="stat-value">{documents.length}</div>
                <div className="stat-label">Celkem dokumentů</div>
              </div>
            </div>

            <div className="stat-card pending">
              <div className="stat-icon">⏳</div>
              <div className="stat-content">
                <div className="stat-value">{pendingCount}</div>
                <div className="stat-label">Čeká na schválení</div>
              </div>
            </div>

            <div className="stat-card success">
              <div className="stat-icon">✓</div>
              <div className="stat-content">
                <div className="stat-value">{approvedCount}</div>
                <div className="stat-label">Schváleno</div>
              </div>
            </div>

            <div className="stat-card danger">
              <div className="stat-icon">⚠️</div>
              <div className="stat-content">
                <div className="stat-value">{overdueCount}</div>
                <div className="stat-label">Po splatnosti</div>
              </div>
            </div>

            <div className="stat-card amount">
              <div className="stat-icon">💰</div>
              <div className="stat-content">
                <div className="stat-value">
                  {totalAmount.toLocaleString('cs-CZ')} CZK
                </div>
                <div className="stat-label">Celková částka</div>
              </div>
            </div>
          </div>

          <div className="recent-section">
            <h2>Poslední dokumenty</h2>
            <div className="recent-documents">
              {documents.slice(0, 5).map((doc) => (
                <Link key={doc.id} to={`/documents/${doc.id}`} className="recent-doc-card">
                  <div className="doc-header-row">
                    <span className="doc-number">{doc.number}</span>
                    <span className={`status-badge ${doc.status.toLowerCase()}`}>
                      {doc.status}
                    </span>
                  </div>
                  <div className="doc-company">{doc.companyName}</div>
                  <div className="doc-footer">
                    <span className="doc-amount">
                      {doc.amount.toLocaleString('cs-CZ')} {doc.currency}
                    </span>
                    <span className="doc-date">
                      {new Date(doc.dueDate).toLocaleDateString('cs-CZ')}
                    </span>
                  </div>
                </Link>
              ))}
            </div>
          </div>

          <div className="quick-actions">
            <Link to="/documents" className="action-button">
              📄 Všechny dokumenty
            </Link>
            <button className="action-button secondary" disabled>
              ➕ Nový dokument
            </button>
          </div>
        </>
      )}
    </div>
  );
}
