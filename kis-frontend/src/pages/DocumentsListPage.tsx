import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import type { DocumentSummaryDTO } from '../types/document';
import { fetchDocuments } from '../api/documentApi';
import './DocumentsListPage.css';

export default function DocumentsListPage() {
  const [documents, setDocuments] = useState<DocumentSummaryDTO[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const loadDocuments = async () => {
      try {
        setLoading(true);
        const result = await fetchDocuments();
        setDocuments(result);
        setError(null);
      } catch (err) {
        setError(err instanceof Error ? err.message : 'Failed to load documents');
      } finally {
        setLoading(false);
      }
    };

    loadDocuments();
  }, []);

  if (loading) {
    return (
      <div className="documents-list loading">
        <div className="spinner"></div>
        <p>Načítám dokumenty...</p>
      </div>
    );
  }

  if (error) {
    return (
      <div className="documents-list error">
        <h2>❌ Chyba</h2>
        <p>{error}</p>
      </div>
    );
  }

  return (
    <div className="documents-list">
      <div className="list-header">
        <h1>📄 Dokumenty</h1>
        <p className="document-count">{documents.length} dokumentů</p>
      </div>

      <div className="documents-table-container">
        <table className="documents-table">
          <thead>
            <tr>
              <th>Číslo</th>
              <th>Typ</th>
              <th>Společnost</th>
              <th>Částka</th>
              <th>Splatnost</th>
              <th>Status</th>
              <th>Vytvořil</th>
              <th>Akce</th>
            </tr>
          </thead>
          <tbody>
            {documents.map((doc) => (
              <tr key={doc.id}>
                <td className="doc-number">
                  <Link to={`/documents/${doc.id}`}>{doc.number}</Link>
                </td>
                <td>{doc.type}</td>
                <td>{doc.companyName}</td>
                <td className="amount">
                  {doc.amount.toLocaleString('cs-CZ')} {doc.currency}
                </td>
                <td>{new Date(doc.dueDate).toLocaleDateString('cs-CZ')}</td>
                <td>
                  <span className={`status-badge ${doc.status.toLowerCase()}`}>
                    {doc.status}
                  </span>
                </td>
                <td>{doc.createdByName}</td>
                <td>
                  <Link to={`/documents/${doc.id}`} className="btn btn-small">
                    Detail
                  </Link>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}
