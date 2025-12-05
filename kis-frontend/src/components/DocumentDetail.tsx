import { useEffect, useState } from 'react';
import type { DocumentDetailDTO } from '../types/document';
import { fetchDocumentDetail } from '../api/documentApi';
import './DocumentDetail.css';

interface Props {
  documentId: number;
}

export default function DocumentDetail({ documentId }: Props) {
  const [data, setData] = useState<DocumentDetailDTO | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const loadDocument = async () => {
      try {
        setLoading(true);
        const result = await fetchDocumentDetail(documentId);
        setData(result);
        setError(null);
      } catch (err) {
        setError(err instanceof Error ? err.message : 'Failed to load document');
      } finally {
        setLoading(false);
      }
    };

    loadDocument();
  }, [documentId]);

  if (loading) {
    return (
      <div className="document-detail loading">
        <div className="spinner"></div>
        <p>Načítám dokument...</p>
      </div>
    );
  }

  if (error) {
    return (
      <div className="document-detail error">
        <h2>❌ Chyba</h2>
        <p>{error}</p>
      </div>
    );
  }

  if (!data) {
    return null;
  }

  const { document, approvalChain, relatedTransactions, lineItems, metadata } = data;

  return (
    <div className="document-detail">
      {/* Header */}
      <div className="document-header">
        <div>
          <h1>{document.number}</h1>
          <p className="document-type">{document.type}</p>
        </div>
        <div className="document-amount">
          <span className="amount">{document.amount.toLocaleString('cs-CZ')}</span>
          <span className="currency">{document.currency}</span>
        </div>
      </div>

      {/* Status */}
      <div className="document-status">
        <span className={`status-badge ${document.status.toLowerCase()}`}>
          {document.status}
        </span>
        <span className="due-date">Splatnost: {new Date(document.dueDate).toLocaleDateString('cs-CZ')}</span>
      </div>

      {/* Company & Creator */}
      <div className="document-info-grid">
        <div className="info-card">
          <h3>🏢 Společnost</h3>
          <p className="company-name">{document.company.name}</p>
          <p className="company-ico">IČO: {document.company.registrationNumber}</p>
        </div>

        <div className="info-card">
          <h3>👤 Vytvořil</h3>
          <p className="user-name">{document.createdBy.name}</p>
          <p className="user-position">{document.createdBy.position}</p>
          <p className="user-email">{document.createdBy.email}</p>
        </div>
      </div>

      {/* Approval Chain */}
      <div className="section">
        <h2>✓ Schvalovací řetězec</h2>
        <div className="approval-chain">
          {approvalChain.map((item) => (
            <div key={item.level} className={`approval-item ${item.status.toLowerCase()}`}>
              <div className="approval-level">Level {item.level}</div>
              <div className="approval-content">
                <div className="approver-info">
                  <strong>{item.approver.name}</strong>
                  <span className="approver-position">{item.approver.position}</span>
                </div>
                <div className={`approval-status ${item.status.toLowerCase()}`}>
                  {item.status}
                </div>
                {item.approvedAt && (
                  <div className="approval-date">
                    {new Date(item.approvedAt).toLocaleString('cs-CZ')}
                  </div>
                )}
                {item.comment && (
                  <div className="approval-comment">💬 {item.comment}</div>
                )}
              </div>
            </div>
          ))}
        </div>
      </div>

      {/* Line Items */}
      <div className="section">
        <h2>📋 Položky dokumentu</h2>
        <table className="line-items-table">
          <thead>
            <tr>
              <th>Popis</th>
              <th>Množství</th>
              <th>Jednotková cena</th>
              <th>Celkem</th>
            </tr>
          </thead>
          <tbody>
            {lineItems.map((item) => (
              <tr key={item.id}>
                <td>{item.description}</td>
                <td>{item.quantity}</td>
                <td>{item.unitPrice.toLocaleString('cs-CZ')} {document.currency}</td>
                <td><strong>{item.total.toLocaleString('cs-CZ')} {document.currency}</strong></td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      {/* Related Transactions */}
      {relatedTransactions.length > 0 && (
        <div className="section">
          <h2>💳 Související transakce</h2>
          <div className="transactions">
            {relatedTransactions.map((tx) => (
              <div key={tx.id} className="transaction-item">
                <span className="tx-type">{tx.type}</span>
                <span className="tx-amount">{tx.amount.toLocaleString('cs-CZ')} {document.currency}</span>
                <span className="tx-date">{new Date(tx.date).toLocaleDateString('cs-CZ')}</span>
              </div>
            ))}
          </div>
        </div>
      )}

      {/* Metadata Actions */}
      <div className="document-actions">
        <button disabled={!metadata.canEdit} className="btn btn-secondary">
          ✏️ Upravit
        </button>
        <button disabled={!metadata.canApprove} className="btn btn-success">
          ✓ Schválit
        </button>
        <button disabled={!metadata.canReject} className="btn btn-danger">
          ✗ Zamítnout
        </button>
        {metadata.pendingApproverName && (
          <p className="pending-approver">
            Čeká na schválení: <strong>{metadata.pendingApproverName}</strong>
          </p>
        )}
      </div>

      {/* Footer */}
      <div className="document-footer">
        <small>
          Vytvořeno: {new Date(document.createdAt).toLocaleString('cs-CZ')} |
          Upraveno: {new Date(document.updatedAt).toLocaleString('cs-CZ')}
        </small>
      </div>
    </div>
  );
}
