import { useState, useEffect } from 'react';
import { Button } from './ui/Button';
import { Input } from './ui/Input';
import { formatCurrency, formatDate } from '../lib/utils';
import type { DocumentDetailDTO } from '../types/document';
import { fetchDocumentDetail } from '../api/documentApi';
import './DocumentDetailEnhanced.css';

interface Comment {
  id: number;
  author: string;
  authorRole: string;
  timestamp: string;
  text: string;
  type: 'comment' | 'approval' | 'rejection';
}

interface TimelineEvent {
  id: number;
  timestamp: string;
  user: string;
  action: string;
  status: 'completed' | 'current' | 'pending';
}

interface DocumentDetailEnhancedProps {
  documentId: number;
}

export default function DocumentDetailEnhanced({ documentId }: DocumentDetailEnhancedProps) {
  const [documentDetail, setDocumentDetail] = useState<DocumentDetailDTO | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [isEditing, setIsEditing] = useState(false);
  const [editedValues, setEditedValues] = useState<any>({});
  const [newComment, setNewComment] = useState('');
  const [comments, setComments] = useState<Comment[]>([
    {
      id: 1,
      author: 'Martin Novák',
      authorRole: 'Účetní',
      timestamp: '2025-01-05T10:30:00',
      text: 'Prosím o schválení této faktury do konce týdne.',
      type: 'comment',
    },
    {
      id: 2,
      author: 'Eva Černá',
      authorRole: 'CFO',
      timestamp: '2025-01-05T14:15:00',
      text: 'Schváleno - částka odpovídá smlouvě.',
      type: 'approval',
    },
  ]);

  const [timeline] = useState<TimelineEvent[]>([
    {
      id: 1,
      timestamp: '2025-01-05T09:00:00',
      user: 'Martin Novák',
      action: 'Dokument vytvořen',
      status: 'completed',
    },
    {
      id: 2,
      timestamp: '2025-01-05T10:30:00',
      user: 'Martin Novák',
      action: 'Odesláno ke schválení',
      status: 'completed',
    },
    {
      id: 3,
      timestamp: '2025-01-05T14:15:00',
      user: 'Eva Černá',
      action: 'Schváleno CFO',
      status: 'current',
    },
    {
      id: 4,
      timestamp: '',
      user: 'Petr Dvořák',
      action: 'Čeká na schválení CEO',
      status: 'pending',
    },
  ]);

  useEffect(() => {
    const loadDocument = async () => {
      try {
        setLoading(true);
        const result = await fetchDocumentDetail(documentId);
        setDocumentDetail(result);
        setEditedValues(result.document);
        setError(null);
      } catch (err) {
        setError(err instanceof Error ? err.message : 'Failed to load document');
      } finally {
        setLoading(false);
      }
    };

    loadDocument();
  }, [documentId]);

  const document = documentDetail?.document;

  const handleEdit = () => {
    setIsEditing(true);
  };

  const handleSave = async () => {
    console.log('Saving changes:', editedValues);
    // TODO: Implement API call to save changes
    // await updateDocument(documentId, editedValues);

    if (documentDetail) {
      setDocumentDetail({
        ...documentDetail,
        document: { ...documentDetail.document, ...editedValues }
      });
    }
    setIsEditing(false);
  };

  const handleCancel = () => {
    setEditedValues(document!);
    setIsEditing(false);
  };

  const handleFieldChange = (field: string, value: any) => {
    setEditedValues((prev: any) => ({ ...prev, [field]: value }));
  };

  const handleAddComment = () => {
    if (!newComment.trim()) return;

    const comment: Comment = {
      id: comments.length + 1,
      author: 'Aktuální uživatel',
      authorRole: 'CFO',
      timestamp: new Date().toISOString(),
      text: newComment,
      type: 'comment',
    };

    setComments([...comments, comment]);
    setNewComment('');
  };

  if (loading) {
    return (
      <div className="document-detail-enhanced loading">
        <div className="spinner"></div>
        <p>Načítám dokument...</p>
      </div>
    );
  }

  if (error || !document) {
    return (
      <div className="document-detail-enhanced error">
        <h2>❌ Chyba</h2>
        <p>{error || 'Dokument nenalezen'}</p>
      </div>
    );
  }

  return (
    <div className="document-detail-enhanced">
      {/* Header with Actions */}
      <div className="detail-header">
        <div className="header-info">
          <h1>📄 {document.number}</h1>
          <span className={`status-badge ${document.status.toLowerCase()}`}>
            {document.status}
          </span>
        </div>
        <div className="header-actions">
          {!isEditing ? (
            <>
              <Button variant="secondary" onClick={handleEdit}>
                ✏️ Upravit
              </Button>
              <Button variant="success">✓ Schválit</Button>
              <Button variant="danger">✗ Zamítnout</Button>
            </>
          ) : (
            <>
              <Button variant="secondary" onClick={handleCancel}>
                Zrušit
              </Button>
              <Button variant="primary" onClick={handleSave}>
                💾 Uložit změny
              </Button>
            </>
          )}
        </div>
      </div>

      <div className="detail-content">
        {/* Left Column - Document Details */}
        <div className="detail-main">
          {/* Basic Information */}
          <section className="detail-section">
            <h2>Základní informace</h2>
            <div className="detail-grid">
              <div className="detail-field">
                <label>Číslo dokumentu</label>
                <div className="field-value">{document.number}</div>
              </div>
              <div className="detail-field">
                <label>Typ</label>
                {isEditing ? (
                  <Input
                    value={editedValues.type || ''}
                    onChange={(e) => handleFieldChange('type', e.target.value)}
                  />
                ) : (
                  <div className="field-value">{document.type}</div>
                )}
              </div>
              <div className="detail-field">
                <label>Částka</label>
                {isEditing ? (
                  <Input
                    type="number"
                    value={editedValues.amount || ''}
                    onChange={(e) => handleFieldChange('amount', parseFloat(e.target.value))}
                  />
                ) : (
                  <div className="field-value amount">
                    {formatCurrency(document.amount, document.currency)}
                  </div>
                )}
              </div>
              <div className="detail-field">
                <label>Měna</label>
                <div className="field-value">{document.currency}</div>
              </div>
              <div className="detail-field">
                <label>Datum splatnosti</label>
                {isEditing ? (
                  <Input
                    type="date"
                    value={editedValues.dueDate || ''}
                    onChange={(e) => handleFieldChange('dueDate', e.target.value)}
                  />
                ) : (
                  <div className="field-value">{formatDate(document.dueDate)}</div>
                )}
              </div>
              <div className="detail-field">
                <label>Status</label>
                <div className="field-value">
                  <span className={`status-tag ${document.status.toLowerCase()}`}>
                    {document.status}
                  </span>
                </div>
              </div>
            </div>
          </section>

          {/* Company Information */}
          <section className="detail-section">
            <h2>Informace o společnosti</h2>
            <div className="detail-grid">
              <div className="detail-field">
                <label>Název společnosti</label>
                {isEditing ? (
                  <Input
                    value={editedValues.company?.name || ''}
                    onChange={(e) => handleFieldChange('company', {
                      ...editedValues.company!,
                      name: e.target.value
                    })}
                  />
                ) : (
                  <div className="field-value">{document.company.name}</div>
                )}
              </div>
              <div className="detail-field">
                <label>IČO</label>
                <div className="field-value">{document.company.registrationNumber}</div>
              </div>
              <div className="detail-field">
                <label>Adresa</label>
                <div className="field-value">{document.company.address}</div>
              </div>
            </div>
          </section>

          {/* Line Items */}
          {documentDetail?.lineItems && documentDetail.lineItems.length > 0 && (
            <section className="detail-section">
              <h2>Položky</h2>
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
                  {documentDetail.lineItems.map((item, index) => (
                    <tr key={index}>
                      <td>{item.description}</td>
                      <td>{item.quantity} {item.unit}</td>
                      <td>{formatCurrency(item.unitPrice, document!.currency)}</td>
                      <td className="amount">
                        {formatCurrency(item.quantity * item.unitPrice, document!.currency)}
                      </td>
                    </tr>
                  ))}
                </tbody>
                <tfoot>
                  <tr>
                    <td colSpan={3} className="total-label">Celkem:</td>
                    <td className="total-amount">
                      {formatCurrency(document!.amount, document!.currency)}
                    </td>
                  </tr>
                </tfoot>
              </table>
            </section>
          )}

          {/* Comments Thread */}
          <section className="detail-section">
            <h2>💬 Komentáře ({comments.length})</h2>
            <div className="comments-thread">
              {comments.map(comment => (
                <div key={comment.id} className={`comment ${comment.type}`}>
                  <div className="comment-header">
                    <div className="comment-author">
                      <strong>{comment.author}</strong>
                      <span className="author-role">{comment.authorRole}</span>
                    </div>
                    <div className="comment-time">
                      {formatDate(comment.timestamp, 'long')}
                    </div>
                  </div>
                  <div className="comment-body">{comment.text}</div>
                  {comment.type === 'approval' && (
                    <div className="comment-badge approval">✓ Schváleno</div>
                  )}
                  {comment.type === 'rejection' && (
                    <div className="comment-badge rejection">✗ Zamítnuto</div>
                  )}
                </div>
              ))}
            </div>

            {/* Add Comment */}
            <div className="add-comment">
              <textarea
                className="comment-input"
                placeholder="Přidat komentář..."
                value={newComment}
                onChange={(e) => setNewComment(e.target.value)}
                rows={3}
              />
              <Button
                variant="primary"
                size="sm"
                onClick={handleAddComment}
                disabled={!newComment.trim()}
              >
                Přidat komentář
              </Button>
            </div>
          </section>
        </div>

        {/* Right Column - Timeline & Metadata */}
        <div className="detail-sidebar">
          {/* Approval Timeline */}
          <section className="sidebar-section">
            <h3>📋 Schvalovací proces</h3>
            <div className="timeline">
              {timeline.map((event, index) => (
                <div key={event.id} className={`timeline-item ${event.status}`}>
                  <div className="timeline-marker"></div>
                  <div className="timeline-content">
                    <div className="timeline-action">{event.action}</div>
                    <div className="timeline-user">{event.user}</div>
                    {event.timestamp && (
                      <div className="timeline-time">
                        {formatDate(event.timestamp, 'long')}
                      </div>
                    )}
                  </div>
                  {index < timeline.length - 1 && <div className="timeline-line"></div>}
                </div>
              ))}
            </div>
          </section>

          {/* Metadata */}
          <section className="sidebar-section">
            <h3>📊 Metadata</h3>
            <div className="metadata-list">
              <div className="metadata-item">
                <span className="metadata-label">Vytvořil:</span>
                <span className="metadata-value">{document?.createdBy.name}</span>
              </div>
              <div className="metadata-item">
                <span className="metadata-label">Vytvořeno:</span>
                <span className="metadata-value">
                  {formatDate(documentDetail?.metadata.createdAt || '', 'long')}
                </span>
              </div>
              <div className="metadata-item">
                <span className="metadata-label">Naposledy upravil:</span>
                <span className="metadata-value">{documentDetail?.metadata.modifiedBy}</span>
              </div>
              <div className="metadata-item">
                <span className="metadata-label">Upraveno:</span>
                <span className="metadata-value">
                  {formatDate(documentDetail?.metadata.modifiedAt || '', 'long')}
                </span>
              </div>
              <div className="metadata-item">
                <span className="metadata-label">Verze:</span>
                <span className="metadata-value">v{documentDetail?.metadata.version}</span>
              </div>
            </div>
          </section>
        </div>
      </div>
    </div>
  );
}
