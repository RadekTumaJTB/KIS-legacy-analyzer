import { useEffect } from 'react';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import * as z from 'zod';
import { Button } from './ui/Button';
import { Input } from './ui/Input';
import type { BudgetDetailDTO } from '../types/budget';
import './NewDocumentModal.css'; // Reuse same styles

const budgetSchema = z.object({
  name: z.string().min(3, 'Název musí mít alespoň 3 znaky'),
  plannedAmount: z.string().min(1, 'Částka je povinná').refine(
    (val) => !isNaN(Number(val)) && Number(val) > 0,
    'Částka musí být kladné číslo'
  ),
  description: z.string().optional(),
});

type BudgetFormData = z.infer<typeof budgetSchema>;

interface EditBudgetModalProps {
  isOpen: boolean;
  budget: BudgetDetailDTO | null;
  onClose: () => void;
  onSubmit: (id: number, data: BudgetFormData) => Promise<void>;
}

export default function EditBudgetModal({ isOpen, budget, onClose, onSubmit }: EditBudgetModalProps) {
  const {
    register,
    handleSubmit,
    formState: { errors, isSubmitting },
    reset,
  } = useForm<BudgetFormData>({
    resolver: zodResolver(budgetSchema),
    mode: 'onSubmit', // Changed from 'onChange' to 'onSubmit'
  });

  // Reset form with budget values when modal opens or budget changes
  useEffect(() => {
    if (isOpen && budget) {
      reset({
        name: budget.name,
        plannedAmount: budget.totalPlanned.toString(),
        description: budget.description || '',
      });
    }
  }, [isOpen, budget, reset]);

  const handleFormSubmit = async (data: BudgetFormData) => {
    if (!budget) return;

    try {
      await onSubmit(budget.id, data);
      onClose();
    } catch (error) {
      console.error('Failed to update budget:', error);
    }
  };

  const handleClose = () => {
    onClose();
  };

  if (!isOpen || !budget) return null;

  return (
    <div className="modal-overlay" onClick={handleClose}>
      <div className="modal-content" onClick={(e) => e.stopPropagation()}>
        <div className="modal-header">
          <h2>✏️ Upravit rozpočet</h2>
          <button className="modal-close" onClick={handleClose}>
            ✕
          </button>
        </div>

        <form onSubmit={handleSubmit(handleFormSubmit)} className="modal-form">
          <div className="form-group">
            <label>Kód rozpočtu</label>
            <Input
              type="text"
              value={budget.code}
              disabled
              className="bg-gray-100"
            />
            <span className="text-xs text-gray-500">Kód nelze měnit</span>
          </div>

          <div className="form-group">
            <label>Rok</label>
            <Input
              type="text"
              value={budget.year}
              disabled
              className="bg-gray-100"
            />
            <span className="text-xs text-gray-500">Rok nelze měnit</span>
          </div>

          <div className="form-group">
            <label htmlFor="name">Název rozpočtu *</label>
            <Input
              id="name"
              type="text"
              {...register('name')}
              className={errors.name ? 'input-error' : ''}
            />
            {errors.name && (
              <span className="error-message">{errors.name.message}</span>
            )}
          </div>

          <div className="form-group">
            <label htmlFor="plannedAmount">Celková plánovaná částka pro celý rok (CZK) *</label>
            <Input
              id="plannedAmount"
              type="text"
              {...register('plannedAmount')}
              className={errors.plannedAmount ? 'input-error' : ''}
              placeholder="např. 12000000"
            />
            <span className="text-xs text-gray-500">
              Částka bude automaticky rozdělena na 12 měsíců
            </span>
            {errors.plannedAmount && (
              <span className="error-message">{errors.plannedAmount.message}</span>
            )}
          </div>

          <div className="form-group">
            <label>Oddělení</label>
            <Input
              type="text"
              value={budget.departmentName}
              disabled
              className="bg-gray-100"
            />
            <span className="text-xs text-gray-500">Oddělení nelze měnit</span>
          </div>

          <div className="form-group">
            <label htmlFor="description">Popis</label>
            <textarea
              id="description"
              rows={3}
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
              {isSubmitting ? 'Ukládám...' : '✓ Uložit změny'}
            </Button>
          </div>
        </form>
      </div>
    </div>
  );
}
