import { useEffect } from 'react';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import * as z from 'zod';
import { Button } from './ui/Button';
import { Input } from './ui/Input';
import './NewDocumentModal.css'; // Reuse same styles

const projectSchema = z.object({
  projectNumber: z.string().min(3, 'Číslo projektu musí mít alespoň 3 znaky'),
  name: z.string().min(3, 'Název musí mít alespoň 3 znaky'),
  startDate: z.string().min(1, 'Datum zahájení je povinné'),
  description: z.string().optional(),
});

type ProjectFormData = z.infer<typeof projectSchema>;

interface NewProjectModalProps {
  isOpen: boolean;
  onClose: () => void;
  onSubmit: (data: ProjectFormData) => Promise<void>;
}

export default function NewProjectModal({ isOpen, onClose, onSubmit }: NewProjectModalProps) {
  const {
    register,
    handleSubmit,
    formState: { errors, isSubmitting },
    reset,
  } = useForm<ProjectFormData>({
    resolver: zodResolver(projectSchema),
    mode: 'onSubmit',
  });

  // Reset form when modal opens
  useEffect(() => {
    if (isOpen) {
      reset({
        projectNumber: '',
        name: '',
        startDate: new Date().toISOString().split('T')[0], // Today's date in YYYY-MM-DD
        description: '',
      });
    }
  }, [isOpen, reset]);

  const handleFormSubmit = async (data: ProjectFormData) => {
    try {
      await onSubmit(data);
      reset();
      onClose();
    } catch (error) {
      console.error('Failed to create project:', error);
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
          <h2>📋 Nový projekt</h2>
          <button className="modal-close" onClick={handleClose}>
            ✕
          </button>
        </div>

        <form onSubmit={handleSubmit(handleFormSubmit)} className="modal-form">
          <div className="form-group">
            <label htmlFor="projectNumber">Číslo projektu *</label>
            <Input
              id="projectNumber"
              type="text"
              placeholder="např. PRJ-2025-001"
              {...register('projectNumber')}
              className={errors.projectNumber ? 'input-error' : ''}
            />
            {errors.projectNumber && (
              <span className="error-message">{errors.projectNumber.message}</span>
            )}
          </div>

          <div className="form-group">
            <label htmlFor="name">Název projektu *</label>
            <Input
              id="name"
              type="text"
              placeholder="např. Implementace nového CRM systému"
              {...register('name')}
              className={errors.name ? 'input-error' : ''}
            />
            {errors.name && (
              <span className="error-message">{errors.name.message}</span>
            )}
          </div>

          <div className="form-group">
            <label htmlFor="startDate">Datum zahájení *</label>
            <Input
              id="startDate"
              type="date"
              {...register('startDate')}
              className={errors.startDate ? 'input-error' : ''}
            />
            {errors.startDate && (
              <span className="error-message">{errors.startDate.message}</span>
            )}
          </div>

          <div className="form-group">
            <label htmlFor="description">Popis</label>
            <textarea
              id="description"
              rows={4}
              placeholder="Nepovinný popis projektu..."
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
              {isSubmitting ? 'Vytvářím...' : '✓ Vytvořit projekt'}
            </Button>
          </div>
        </form>
      </div>
    </div>
  );
}
