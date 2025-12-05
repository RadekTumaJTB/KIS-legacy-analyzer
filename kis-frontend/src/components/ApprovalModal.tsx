import { useState } from 'react';
import { Button } from './ui/Button';
import './ApprovalModal.css';

interface ApprovalModalProps {
  isOpen: boolean;
  onClose: () => void;
  onSubmit: (action: 'approve' | 'reject', comment: string) => void;
  selectedCount: number;
  action: 'approve' | 'reject';
}

export default function ApprovalModal({
  isOpen,
  onClose,
  onSubmit,
  selectedCount,
  action,
}: ApprovalModalProps) {
  const [comment, setComment] = useState('');
  const [isSubmitting, setIsSubmitting] = useState(false);

  if (!isOpen) return null;

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setIsSubmitting(true);

    try {
      await onSubmit(action, comment);
      setComment('');
      onClose();
    } catch (error) {
      console.error('Failed to submit approval:', error);
    } finally {
      setIsSubmitting(false);
    }
  };

  const handleBackdropClick = (e: React.MouseEvent) => {
    if (e.target === e.currentTarget) {
      onClose();
    }
  };

  const isApprove = action === 'approve';
  const title = isApprove ? 'Schválit dokumenty' : 'Zamítnout dokumenty';
  const icon = isApprove ? '✓' : '✗';
  const actionColor = isApprove ? 'success' : 'danger';

  return (
    <div className="modal-backdrop" onClick={handleBackdropClick}>
      <div className="modal-container">
        <div className="modal-header">
          <h2 className={`modal-title ${actionColor}`}>
            <span className="modal-icon">{icon}</span>
            {title}
          </h2>
          <button
            className="modal-close-btn"
            onClick={onClose}
            aria-label="Zavřít"
          >
            ×
          </button>
        </div>

        <form onSubmit={handleSubmit}>
          <div className="modal-body">
            <div className="modal-info">
              <p className="modal-count">
                Počet vybraných dokumentů: <strong>{selectedCount}</strong>
              </p>
              <p className="modal-description">
                {isApprove
                  ? 'Vybrané dokumenty budou schváleny a postoupeny k dalšímu zpracování.'
                  : 'Vybrané dokumenty budou zamítnuty a vráceny k přepracování.'}
              </p>
            </div>

            <div className="form-group">
              <label htmlFor="approval-comment" className="form-label">
                Komentář {!isApprove && <span className="required">*</span>}
              </label>
              <textarea
                id="approval-comment"
                className="form-textarea"
                rows={4}
                value={comment}
                onChange={(e) => setComment(e.target.value)}
                placeholder={
                  isApprove
                    ? 'Volitelný komentář ke schválení...'
                    : 'Důvod zamítnutí (povinné)...'
                }
                required={!isApprove}
              />
              <p className="form-hint">
                {isApprove
                  ? 'Můžete přidat poznámku pro další uživatele.'
                  : 'Při zamítnutí je nutné uvést důvod.'}
              </p>
            </div>

            {!isApprove && comment.length > 0 && comment.length < 10 && (
              <div className="form-warning">
                ⚠️ Důvod zamítnutí by měl být podrobnější (minimálně 10 znaků)
              </div>
            )}
          </div>

          <div className="modal-footer">
            <Button
              type="button"
              variant="secondary"
              onClick={onClose}
              disabled={isSubmitting}
            >
              Zrušit
            </Button>
            <Button
              type="submit"
              variant={actionColor}
              disabled={isSubmitting || (!isApprove && comment.length < 10)}
            >
              {isSubmitting ? (
                <>
                  <span className="btn-spinner"></span>
                  Zpracovávám...
                </>
              ) : (
                <>
                  {icon} {isApprove ? 'Schválit' : 'Zamítnout'} ({selectedCount})
                </>
              )}
            </Button>
          </div>
        </form>
      </div>
    </div>
  );
}
