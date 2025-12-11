import { useEffect, useState } from 'react';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import * as z from 'zod';
import { Button } from './ui/Button';
import { Input } from './ui/Input';
import { Select } from './ui/Select';
import { Checkbox } from './ui/Checkbox';
import type {
  ProjectDetailDTO,
  ProjectFormData,
  ProjectReferenceData,
} from '../types/project';
import { fetchProjectReferenceData } from '../api/projectApi';
import './NewDocumentModal.css'; // Reuse same styles

// Validation schema matching JSP form requirements
const projectSchema = z.object({
  name: z.string().min(3, 'Název musí mít alespoň 3 znaky').max(100, 'Název může mít maximálně 100 znaků'),
  oldProjectNumber: z.string().max(6, 'Staré číslo může mít maximálně 6 znaků').optional(),
  idStatus: z.number().min(1, 'Status je povinný'),
  idProposedBy: z.number().optional(),
  idProjectManager: z.number().min(1, 'Projektový manažer je povinný'),
  idCategory: z.number().optional(),
  idManagementSegment: z.number().optional(),
  currencyCode: z.string().min(1, 'Měna je povinná'),
  valuationStartDate: z.string().min(1, 'Datum zahájení přecenění je povinné'),
  idFrequency: z.number().optional(),
  description: z.string().max(250, 'Popis může mít maximálně 250 znaků').optional(),
  idProjectProposal: z.number().optional(),
  nextProjectCardReport: z.string().optional(),
  reportPeriodMonths: z.number().min(1).max(99).optional(),
  idBalanceType: z.number().optional(),
  budgetTrackingFlag: z.boolean(),
  idBudgetType: z.number().optional(),
});

type ProjectFormDataWithValidation = z.infer<typeof projectSchema>;

interface EditProjectModalProps {
  isOpen: boolean;
  project: ProjectDetailDTO | null;
  onClose: () => void;
  onSubmit: (id: number, data: ProjectFormData) => Promise<void>;
}

export default function EditProjectModal({
  isOpen,
  project,
  onClose,
  onSubmit,
}: EditProjectModalProps) {
  const [referenceData, setReferenceData] = useState<ProjectReferenceData | null>(null);
  const [loadingReferenceData, setLoadingReferenceData] = useState(false);

  const {
    register,
    handleSubmit,
    formState: { errors, isSubmitting },
    reset,
    watch,
  } = useForm<ProjectFormDataWithValidation>({
    resolver: zodResolver(projectSchema),
    mode: 'onSubmit',
  });

  // Load reference data when modal opens
  useEffect(() => {
    if (isOpen && !referenceData) {
      const loadReferenceData = async () => {
        try {
          setLoadingReferenceData(true);
          const data = await fetchProjectReferenceData();
          setReferenceData(data);
        } catch (error) {
          console.error('Failed to load reference data:', error);
          alert('Nepodařilo se načíst справочná data. Zkuste to prosím znovu.');
        } finally {
          setLoadingReferenceData(false);
        }
      };

      loadReferenceData();
    }
  }, [isOpen, referenceData]);

  // Reset form with project values when modal opens or project changes
  useEffect(() => {
    if (isOpen && project && referenceData) {
      reset({
        name: project.name,
        oldProjectNumber: project.oldProjectNumber || '',
        idStatus: project.idStatus,
        idProposedBy: project.idProposedBy || undefined,
        idProjectManager: project.idProjectManager,
        idCategory: project.idCategory || undefined,
        idManagementSegment: project.idManagementSegment || undefined,
        currencyCode: project.currencyCode || 'CZK',
        valuationStartDate: project.valuationStartDate.split('T')[0], // Convert ISO to YYYY-MM-DD
        idFrequency: project.idFrequency || undefined,
        description: project.description || '',
        idProjectProposal: project.idProjectProposal || undefined,
        nextProjectCardReport: project.nextProjectCardReport
          ? project.nextProjectCardReport.split('T')[0]
          : '',
        reportPeriodMonths: project.reportPeriodMonths || 3,
        idBalanceType: project.idBalanceType || undefined,
        budgetTrackingFlag: project.budgetTrackingFlag === 'A' || project.budgetTrackingFlag === '1',
        idBudgetType: project.idBudgetType || undefined,
      });
    }
  }, [isOpen, project, referenceData, reset]);

  const handleFormSubmit = async (data: ProjectFormDataWithValidation) => {
    if (!project) return;

    try {
      const formData: ProjectFormData = {
        ...data,
        budgetTrackingFlag: data.budgetTrackingFlag,
      };
      await onSubmit(project.id, formData);
      onClose();
    } catch (error) {
      console.error('Failed to update project:', error);
    }
  };

  const handleClose = () => {
    onClose();
  };

  if (!isOpen || !project) return null;

  if (loadingReferenceData || !referenceData) {
    return (
      <div className="modal-overlay">
        <div className="modal-content">
          <div className="modal-header">
            <h2>Načítám data...</h2>
          </div>
          <div className="modal-form" style={{ textAlign: 'center', padding: '2rem' }}>
            <div className="spinner"></div>
            <p>Načítám справочná data formuláře...</p>
          </div>
        </div>
      </div>
    );
  }

  return (
    <div className="modal-overlay" onClick={handleClose}>
      <div
        className="modal-content"
        onClick={(e) => e.stopPropagation()}
        style={{ maxWidth: '800px' }}
      >
        <div className="modal-header">
          <h2>Upravit projekt</h2>
          <button className="modal-close" onClick={handleClose}>
            ✕
          </button>
        </div>

        <form onSubmit={handleSubmit(handleFormSubmit)} className="modal-form">
          {/* ID - Read Only */}
          <div className="form-group">
            <label>ID</label>
            <Input type="text" value={project.id} disabled className="bg-gray-100" />
            <span className="text-xs text-gray-500">ID projektu nelze měnit</span>
          </div>

          {/* Name - Required */}
          <div className="form-group">
            <label htmlFor="name">Název *</label>
            <Input
              id="name"
              type="text"
              {...register('name')}
              className={errors.name ? 'input-error' : ''}
              maxLength={100}
            />
            {errors.name && <span className="error-message">{errors.name.message}</span>}
          </div>

          {/* Old Project Number */}
          <div className="form-group">
            <label htmlFor="oldProjectNumber">Staré číslo projektu</label>
            <Input
              id="oldProjectNumber"
              type="text"
              {...register('oldProjectNumber')}
              className={errors.oldProjectNumber ? 'input-error' : ''}
              maxLength={6}
            />
            {errors.oldProjectNumber && (
              <span className="error-message">{errors.oldProjectNumber.message}</span>
            )}
          </div>

          {/* Two column layout for related fields */}
          <div className="form-row">
            {/* Status - Required */}
            <div className="form-group">
              <label htmlFor="idStatus">Status *</label>
              <Select
                id="idStatus"
                {...register('idStatus', { valueAsNumber: true })}
                className={errors.idStatus ? 'input-error' : ''}
              >
                <option value="">Vyberte status</option>
                {referenceData.statuses.map((status) => (
                  <option key={status.id} value={status.id}>
                    {status.description}
                  </option>
                ))}
              </Select>
              {errors.idStatus && (
                <span className="error-message">{errors.idStatus.message}</span>
              )}
            </div>

            {/* Proposed By */}
            <div className="form-group">
              <label htmlFor="idProposedBy">Navrhuje</label>
              <Select
                id="idProposedBy"
                {...register('idProposedBy', { valueAsNumber: true })}
              >
                <option value="">Vyberte uživatele</option>
                {referenceData.users.map((user) => (
                  <option key={user.id} value={user.id}>
                    {user.fullName}
                  </option>
                ))}
              </Select>
            </div>
          </div>

          {/* Project Manager - Required */}
          <div className="form-group">
            <label htmlFor="idProjectManager">Projektový manažer *</label>
            <Select
              id="idProjectManager"
              {...register('idProjectManager', { valueAsNumber: true })}
              className={errors.idProjectManager ? 'input-error' : ''}
            >
              <option value="">Vyberte projektového manažera</option>
              {referenceData.users.map((user) => (
                <option key={user.id} value={user.id}>
                  {user.fullName}
                </option>
              ))}
            </Select>
            {errors.idProjectManager && (
              <span className="error-message">{errors.idProjectManager.message}</span>
            )}
          </div>

          <div className="form-row">
            {/* Category */}
            <div className="form-group">
              <label htmlFor="idCategory">Kategorie</label>
              <Select id="idCategory" {...register('idCategory', { valueAsNumber: true })}>
                <option value="">Vyberte kategorii</option>
                {referenceData.categories.map((category) => (
                  <option key={category.id} value={category.id}>
                    {category.description}
                  </option>
                ))}
              </Select>
            </div>

            {/* Management Segment / Holding */}
            <div className="form-group">
              <label htmlFor="idManagementSegment">Holding</label>
              <Select
                id="idManagementSegment"
                {...register('idManagementSegment', { valueAsNumber: true })}
              >
                <option value="">Vyberte holding</option>
                {referenceData.managementSegments.map((segment) => (
                  <option key={segment.id} value={segment.id}>
                    {segment.name}
                  </option>
                ))}
              </Select>
            </div>
          </div>

          {/* Currency - Required, Default CZK */}
          <div className="form-group">
            <label htmlFor="currencyCode">Měna projektu *</label>
            <Select
              id="currencyCode"
              {...register('currencyCode')}
              className={errors.currencyCode ? 'input-error' : ''}
            >
              {referenceData.currencies.map((currency) => (
                <option key={currency.code} value={currency.code}>
                  {currency.name} ({currency.code})
                </option>
              ))}
            </Select>
            {errors.currencyCode && (
              <span className="error-message">{errors.currencyCode.message}</span>
            )}
          </div>

          <div className="form-row">
            {/* Revaluation Start Date - Required */}
            <div className="form-group">
              <label htmlFor="valuationStartDate">Start přecenění *</label>
              <Input
                id="valuationStartDate"
                type="date"
                {...register('valuationStartDate')}
                className={errors.valuationStartDate ? 'input-error' : ''}
              />
              {errors.valuationStartDate && (
                <span className="error-message">{errors.valuationStartDate.message}</span>
              )}
            </div>

            {/* Revaluation Frequency */}
            <div className="form-group">
              <label htmlFor="idFrequency">Frekvence přecenění</label>
              <Select id="idFrequency" {...register('idFrequency', { valueAsNumber: true })}>
                <option value="">Vyberte frekvenci</option>
                {referenceData.frequencies.map((frequency) => (
                  <option key={frequency.id} value={frequency.id}>
                    {frequency.description}
                  </option>
                ))}
              </Select>
            </div>
          </div>

          {/* Description */}
          <div className="form-group">
            <label htmlFor="description">Popis</label>
            <textarea
              id="description"
              rows={3}
              {...register('description')}
              className={`textarea ${errors.description ? 'input-error' : ''}`}
              maxLength={250}
            />
            {errors.description && (
              <span className="error-message">{errors.description.message}</span>
            )}
            <span className="text-xs text-gray-500">
              Maximálně 250 znaků. Zbývá: {250 - (watch('description')?.length || 0)}
            </span>
          </div>

          {/* Project Proposal */}
          <div className="form-group">
            <label htmlFor="idProjectProposal">Projektový návrh</label>
            <Select
              id="idProjectProposal"
              {...register('idProjectProposal', { valueAsNumber: true })}
            >
              <option value="">Vyberte projektový návrh</option>
              {referenceData.projectProposals.map((proposal) => (
                <option key={proposal.id} value={proposal.id}>
                  {proposal.name}
                </option>
              ))}
            </Select>
          </div>

          {/* Validity Dates - Read Only */}
          <div className="form-row">
            <div className="form-group">
              <label>Platnost od</label>
              <Input
                type="text"
                value={project.validityFrom || '-'}
                disabled
                className="bg-gray-100"
              />
              <span className="text-xs text-gray-500">Pouze pro čtení</span>
            </div>

            <div className="form-group">
              <label>Platnost do</label>
              <Input
                type="text"
                value={project.validityTo || '-'}
                disabled
                className="bg-gray-100"
              />
              <span className="text-xs text-gray-500">Pouze pro čtení</span>
            </div>
          </div>

          {/* Project Card Report Settings */}
          <div className="form-row">
            <div className="form-group">
              <label htmlFor="nextProjectCardReport">Karta projektu - další zpráva</label>
              <Input
                id="nextProjectCardReport"
                type="date"
                {...register('nextProjectCardReport')}
              />
            </div>

            <div className="form-group">
              <label htmlFor="reportPeriodMonths">Karta projektu - perioda zpráv</label>
              <div style={{ display: 'flex', alignItems: 'center', gap: '0.5rem' }}>
                <Input
                  id="reportPeriodMonths"
                  type="number"
                  {...register('reportPeriodMonths', { valueAsNumber: true })}
                  min={1}
                  max={99}
                  style={{ width: '80px' }}
                />
                <span className="text-sm text-gray-600">(v měsících)</span>
              </div>
              {errors.reportPeriodMonths && (
                <span className="error-message">{errors.reportPeriodMonths.message}</span>
              )}
            </div>
          </div>

          {/* Balance Type */}
          <div className="form-group">
            <label htmlFor="idBalanceType">Typ projektové bilance</label>
            <Select id="idBalanceType" {...register('idBalanceType', { valueAsNumber: true })}>
              <option value="">Vyberte typ bilance</option>
              {referenceData.balanceTypes.map((balanceType) => (
                <option key={balanceType.id} value={balanceType.id}>
                  {balanceType.description}
                </option>
              ))}
            </Select>
          </div>

          {/* Budget Tracking Flag - Checkbox */}
          <div className="form-group">
            <label className="flex items-center gap-2">
              <Checkbox id="budgetTrackingFlag" {...register('budgetTrackingFlag')} />
              <span>Sleduje budget</span>
            </label>
          </div>

          {/* Budget Type */}
          <div className="form-group">
            <label htmlFor="idBudgetType">Typ budgetu</label>
            <Select id="idBudgetType" {...register('idBudgetType', { valueAsNumber: true })}>
              <option value="">Vyberte typ budgetu</option>
              {referenceData.budgetTypes.map((budgetType) => (
                <option key={budgetType.id} value={budgetType.id}>
                  {budgetType.description}
                </option>
              ))}
            </Select>
          </div>

          {/* Submit Buttons */}
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
              {isSubmitting ? 'Ukládám...' : 'Uložit změny'}
            </Button>
          </div>
        </form>
      </div>
    </div>
  );
}
