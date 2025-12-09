import { useEffect } from 'react';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import * as z from 'zod';
import { Button } from './ui/Button';
import { Input } from './ui/Input';
import './NewDocumentModal.css';

const documentSchema = z.object({
  type: z.string().min(1, 'Typ dokumentu je povinný'),
  amount: z.string().min(1, 'Částka je povinná').refine(
    (val) => !isNaN(Number(val)) && Number(val) > 0,
    'Částka musí být kladné číslo'
  ),
  dueDate: z.string().min(1, 'Datum splatnosti je povinné'),
  companyName: z.string().min(3, 'Název společnosti musí mít alespoň 3 znaky'),
  description: z.string().optional(),
});

type DocumentFormData = z.infer<typeof documentSchema>;

interface NewDocumentModalProps {
  isOpen: boolean;
  onClose: () => void;
  onSubmit: (data: DocumentFormData) => Promise<void>;
}

export default function NewDocumentModal({ isOpen, onClose, onSubmit }: NewDocumentModalProps) {
  const {
    register,
    handleSubmit,
    formState: { errors, isSubmitting },
    reset,
  } = useForm<DocumentFormData>({
    resolver: zodResolver(documentSchema),
    mode: 'onSubmit', // Changed from 'onChange' to 'onSubmit' for proper validation
  });

  // Reset form when modal opens
  useEffect(() => {
    if (isOpen) {
      reset();
    }
  }, [isOpen, reset]);

  const handleFormSubmit = async (data: DocumentFormData) => {
    try {
      await onSubmit(data);
      reset();
      onClose();
    } catch (error) {
      console.error('Failed to create document:', error);
    }
  };

  const handleClose = () => {
    reset();
    onClose();
  };

  if (!isOpen) return null;

  return (
    <div className="modal-overlay" onClick={handleClose}>
      <div className="modal-content" onClick={(e) => e.stopPropagation()}>
        <div className="modal-header">
          <h2>📄 Nový dokument</h2>
          <button className="modal-close" onClick={handleClose}>
            ✕
          </button>
        </div>

        <form onSubmit={handleSubmit(handleFormSubmit)} className="modal-form">
          <div className="form-group">
            <label htmlFor="type">Typ dokumentu *</label>
            <select
              id="type"
              {...register('type')}
              className={errors.type ? 'input-error' : ''}
            >
              <option value="">Vyberte typ...</option>
              <option value="INVOICE">Faktura</option>
              <option value="PAYMENT_ORDER">Platební příkaz</option>
              <option value="CONTRACT">Smlouva</option>
              <option value="RECEIPT">Příjemka</option>
            </select>
            {errors.type && (
              <span className="error-message">{errors.type.message}</span>
            )}
          </div>

          <div className="form-group">
            <label htmlFor="companyName">Název společnosti *</label>
            <Input
              id="companyName"
              type="text"
              placeholder="např. ABC s.r.o."
              {...register('companyName')}
              className={errors.companyName ? 'input-error' : ''}
            />
            {errors.companyName && (
              <span className="error-message">{errors.companyName.message}</span>
            )}
          </div>

          <div className="form-row">
            <div className="form-group">
              <label htmlFor="amount">Částka (CZK) *</label>
              <Input
                id="amount"
                type="text"
                placeholder="např. 50000"
                {...register('amount')}
                className={errors.amount ? 'input-error' : ''}
              />
              {errors.amount && (
                <span className="error-message">{errors.amount.message}</span>
              )}
            </div>

            <div className="form-group">
              <label htmlFor="dueDate">Datum splatnosti *</label>
              <Input
                id="dueDate"
                type="date"
                {...register('dueDate')}
                className={errors.dueDate ? 'input-error' : ''}
              />
              {errors.dueDate && (
                <span className="error-message">{errors.dueDate.message}</span>
              )}
            </div>
          </div>

          <div className="form-group">
            <label htmlFor="description">Poznámka</label>
            <textarea
              id="description"
              rows={3}
              placeholder="Nepovinná poznámka k dokumentu..."
              {...register('description')}
              className="textarea"
            />
          </div>

          <div className="modal-footer">
            <Button
              type="button"
              variant="secondary"
              onClick={handleClose}
              disabled={isSubmitting}
            >
              Zrušit
            </Button>
            <Button type="submit" variant="primary" disabled={isSubmitting}>
              {isSubmitting ? 'Vytvářím...' : '✓ Vytvořit dokument'}
            </Button>
          </div>
        </form>
      </div>
    </div>
  );
}
