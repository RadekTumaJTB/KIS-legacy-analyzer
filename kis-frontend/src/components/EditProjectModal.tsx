import { useEffect } from 'react';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import * as z from 'zod';
import { Button } from './ui/Button';
import { Input } from './ui/Input';
import type { ProjectDetailDTO } from '../types/project';
import './NewDocumentModal.css'; // Reuse same styles

const projectSchema = z.object({
  name: z.string().min(3, 'Název musí mít alespoň 3 znaky'),
  startDate: z.string().min(1, 'Datum zahájení je povinné'),
  description: z.string().optional(),
});

type ProjectFormData = z.infer<typeof projectSchema>;

interface EditProjectModalProps {
  isOpen: boolean;
  project: ProjectDetailDTO | null;
  onClose: () => void;
  onSubmit: (id: number, data: ProjectFormData) => Promise<void>;
}

export default function EditProjectModal({ isOpen, project, onClose, onSubmit }: EditProjectModalProps) {
  const {
    register,
    handleSubmit,
    formState: { errors, isSubmitting },
    reset,
  } = useForm<ProjectFormData>({
    resolver: zodResolver(projectSchema),
    mode: 'onSubmit',
  });

  // Reset form with project values when modal opens or project changes
  useEffect(() => {
    if (isOpen && project) {
      reset({
        name: project.name,
        startDate: project.valuationStartDate.split('T')[0], // Convert ISO to YYYY-MM-DD
        description: project.description || '',
      });
    }
  }, [isOpen, project, reset]);

  const handleFormSubmit = async (data: ProjectFormData) => {
    if (!project) return;

    try {
      await onSubmit(project.id, data);
      onClose();
    } catch (error) {
      console.error('Failed to update project:', error);
    }
  };

  const handleClose = () => {
    onClose();
  };

  if (!isOpen || !project) return null;

  return (
    <div className="modal-overlay" onClick={handleClose}>
      <div className="modal-content" onClick={(e) => e.stopPropagation()}>
        <div className="modal-header">
          <h2>✏️ Upravit projekt</h2>
          <button className="modal-close" onClick={handleClose}>
            ✕
          </button>
        </div>

        <form onSubmit={handleSubmit(handleFormSubmit)} className="modal-form">
          <div className="form-group">
            <label>Číslo projektu</label>
            <Input
              type="text"
              value={project.projectNumber}
              disabled
              className="bg-gray-100"
            />
            <span className="text-xs text-gray-500">Číslo projektu nelze měnit</span>
          </div>

          <div className="form-group">
            <label>Status</label>
            <Input
              type="text"
              value={project.status}
              disabled
              className="bg-gray-100"
            />
            <span className="text-xs text-gray-500">Status nelze měnit</span>
          </div>

          <div className="form-group">
            <label htmlFor="name">Název projektu *</label>
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
