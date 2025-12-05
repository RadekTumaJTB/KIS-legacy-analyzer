/**
 * TypeScript types matching BFF DTOs
 */

export interface UserSummaryDTO {
  id: number;
  name: string;
  email: string;
  position: string;
}

export interface CompanySummaryDTO {
  id: number;
  name: string;
  ico: string;
}

export interface DocumentDTO {
  id: number;
  number: string;
  type: string;
  amount: number;
  currency: string;
  dueDate: string;
  status: string;
  createdBy: UserSummaryDTO;
  company: CompanySummaryDTO;
  createdAt: string;
  updatedAt: string;
}

export interface DocumentSummaryDTO {
  id: number;
  number: string;
  type: string;
  amount: number;
  currency: string;
  dueDate: string;
  status: string;
  companyName: string;
  createdByName: string;
}

export interface ApprovalChainItemDTO {
  level: number;
  approver: UserSummaryDTO;
  status: 'PENDING' | 'APPROVED' | 'REJECTED';
  approvedAt?: string;
  comment?: string;
}

export interface RelatedTransactionDTO {
  id: number;
  type: string;
  amount: number;
  date: string;
}

export interface DocumentLineItemDTO {
  id: number;
  description: string;
  quantity: number;
  unitPrice: number;
  total: number;
}

export interface DocumentMetadataDTO {
  canEdit: boolean;
  canApprove: boolean;
  canReject: boolean;
  pendingApproverName: string;
}

export interface DocumentDetailDTO {
  document: DocumentDTO;
  approvalChain: ApprovalChainItemDTO[];
  relatedTransactions: RelatedTransactionDTO[];
  lineItems: DocumentLineItemDTO[];
  metadata: DocumentMetadataDTO;
}
