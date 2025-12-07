import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import * as z from 'zod';
import { Button } from './ui/Button';
import { Input } from './ui/Input';
import './NewDocumentModal.css'; // Reuse same styles

const budgetSchema = z.object({
  code: z.string().min(3, 'Kód musí mít alespoň 3 znaky'),
  name: z.string().min(3, 'Název musí mít alespoň 3 znaky'),
  type: z.string().min(1, 'Typ rozpočtu je povinný'),
  year: z.string().min(1, 'Rok je povinný').refine(
    (val) => !isNaN(Number(val)) && Number(val) >= 2020 && Number(val) <= 2030,
    'Rok musí být mezi 2020 a 2030'
  ),
  plannedAmount: z.string().min(1, 'Částka je povinná').refine(
    (val) => !isNaN(Number(val)) && Number(val) > 0,
    'Částka musí být kladné číslo'
  ),
  departmentName: z.string().min(3, 'Název oddělení musí mít alespoň 3 znaky'),
  description: z.string().optional(),
});

type BudgetFormData = z.infer<typeof budgetSchema>;

interface NewBudgetModalProps {
  isOpen: boolean;
  onClose: () => void;
  onSubmit: (data: BudgetFormData) => Promise<void>;
}

export default function NewBudgetModal({ isOpen, onClose, onSubmit }: NewBudgetModalProps) {
  const {
    register,
    handleSubmit,
    formState: { errors, isSubmitting },
    reset,
  } = useForm<BudgetFormData>({
    resolver: zodResolver(budgetSchema),
    mode: 'onChange',
  });

  const handleFormSubmit = async (data: BudgetFormData) => {
    try {
      await onSubmit(data);
      reset();
      onClose();
    } catch (error) {
      console.error('Failed to create budget:', error);
    }
  };

  const handleClose = () => {
    reset();
    onClose();
  };

  if (!isOpen) return null;

  const currentYear = new Date().getFullYear();

  return (
    <div className="modal-overlay" onClick={handleClose}>
      <div className="modal-content" onClick={(e) => e.stopPropagation()}>
        <div className="modal-header">
          <h2>💰 Nový rozpočet</h2>
          <button className="modal-close" onClick={handleClose}>
            ✕
          </button>
        </div>

        <form onSubmit={handleSubmit(handleFormSubmit)} className="modal-form">
          <div className="form-row">
            <div className="form-group">
              <label htmlFor="code">Kód rozpočtu *</label>
              <Input
                id="code"
                type="text"
                placeholder="např. BUD-2025-001"
                {...register('code')}
                className={errors.code ? 'input-error' : ''}
              />
              {errors.code && (
                <span className="error-message">{errors.code.message}</span>
              )}
            </div>

            <div className="form-group">
              <label htmlFor="year">Rok *</label>
              <Input
                id="year"
                type="text"
                placeholder={currentYear.toString()}
                {...register('year')}
                className={errors.year ? 'input-error' : ''}
              />
              {errors.year && (
                <span className="error-message">{errors.year.message}</span>
              )}
            </div>
          </div>

          <div className="form-group">
            <label htmlFor="name">Název rozpočtu *</label>
            <Input
              id="name"
              type="text"
              placeholder="např. IT Oddělení - Provozní náklady"
              {...register('name')}
              className={errors.name ? 'input-error' : ''}
            />
            {errors.name && (
              <span className="error-message">{errors.name.message}</span>
            )}
          </div>

          <div className="form-row">
            <div className="form-group">
              <label htmlFor="type">Typ rozpočtu *</label>
              <select
                id="type"
                {...register('type')}
                className={errors.type ? 'input-error' : ''}
              >
                <option value="">Vyberte typ...</option>
                <option value="REVENUE">Příjmy</option>
                <option value="EXPENSE">Náklady</option>
                <option value="CAPEX">Kapitálové výdaje</option>
              </select>
              {errors.type && (
                <span className="error-message">{errors.type.message}</span>
              )}
            </div>

            <div className="form-group">
              <label htmlFor="plannedAmount">Plánovaná částka (CZK) *</label>
              <Input
                id="plannedAmount"
                type="text"
                placeholder="např. 5000000"
                {...register('plannedAmount')}
                className={errors.plannedAmount ? 'input-error' : ''}
              />
              {errors.plannedAmount && (
                <span className="error-message">{errors.plannedAmount.message}</span>
              )}
            </div>
          </div>

          <div className="form-group">
            <label htmlFor="departmentName">Oddělení *</label>
            <Input
              id="departmentName"
              type="text"
              placeholder="např. IT"
              {...register('departmentName')}
              className={errors.departmentName ? 'input-error' : ''}
            />
            {errors.departmentName && (
              <span className="error-message">{errors.departmentName.message}</span>
            )}
          </div>

          <div className="form-group">
            <label htmlFor="description">Popis</label>
            <textarea
              id="description"
              rows={3}
              placeholder="Nepovinný popis rozpočtu..."
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
              {isSubmitting ? 'Vytvářím...' : '✓ Vytvořit rozpočet'}
            </Button>
          </div>
        </form>
      </div>
    </div>
  );
}
