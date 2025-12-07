import type { DocumentDetailDTO, DocumentSummaryDTO } from '../types/document';

const BFF_BASE_URL = '/bff';

/**
 * Fetch all documents (list view)
 */
export async function fetchDocuments(): Promise<DocumentSummaryDTO[]> {
  const response = await fetch(`${BFF_BASE_URL}/documents`);

  if (!response.ok) {
    throw new Error(`Failed to fetch documents: ${response.statusText}`);
  }

  return response.json();
}

/**
 * Fetch document detail from BFF
 *
 * Performance: 1 API call instead of 5 (80% faster)
 */
export async function fetchDocumentDetail(id: number): Promise<DocumentDetailDTO> {
  const response = await fetch(`${BFF_BASE_URL}/documents/${id}/detail`);

  if (!response.ok) {
    throw new Error(`Failed to fetch document ${id}: ${response.statusText}`);
  }

  return response.json();
}

/**
 * Create new document
 *
 * @param data Document data
 */
export async function createDocument(data: {
  type: string;
  amount: string;
  dueDate: string;
  companyName: string;
  description?: string;
}): Promise<DocumentDetailDTO> {
  const response = await fetch(`${BFF_BASE_URL}/documents`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify({
      type: data.type,
      amount: parseFloat(data.amount),
      dueDate: data.dueDate,
      companyName: data.companyName,
      description: data.description,
    }),
  });

  if (!response.ok) {
    throw new Error(`Failed to create document: ${response.statusText}`);
  }

  return response.json();
}

/**
 * Update document
 *
 * @param id Document ID
 * @param updates Partial document updates
 */
export async function updateDocument(id: number, updates: {
  type?: string;
  amount?: number;
  dueDate?: string;
  companyName?: string;
}): Promise<DocumentDetailDTO> {
  const response = await fetch(`${BFF_BASE_URL}/documents/${id}`, {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(updates),
  });

  if (!response.ok) {
    throw new Error(`Failed to update document ${id}: ${response.statusText}`);
  }

  return response.json();
}

/**
 * Add comment to document
 *
 * @param id Document ID
 * @param comment Comment text
 * @param type Comment type ('comment' | 'approval' | 'rejection')
 */
export async function addComment(id: number, comment: string, type: string = 'comment'): Promise<DocumentDetailDTO> {
  const response = await fetch(`${BFF_BASE_URL}/documents/${id}/comments`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify({ text: comment, type }),
  });

  if (!response.ok) {
    throw new Error(`Failed to add comment to document ${id}: ${response.statusText}`);
  }

  return response.json();
}

/**
 * Approve or reject document
 *
 * @param id Document ID
 * @param action 'approve' or 'reject'
 * @param comment Optional comment
 */
export async function performApprovalAction(
  id: number,
  action: 'approve' | 'reject',
  comment?: string
): Promise<DocumentDetailDTO> {
  const response = await fetch(`${BFF_BASE_URL}/documents/${id}/approval-action`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify({ action, comment, documentIds: [id] }),
  });

  if (!response.ok) {
    throw new Error(`Failed to ${action} document ${id}: ${response.statusText}`);
  }

  return response.json();
}

/**
 * Bulk approval action for multiple documents
 *
 * @param documentIds Array of document IDs
 * @param action 'approve' or 'reject'
 * @param comment Optional comment
 */
export async function bulkApprovalAction(
  documentIds: number[],
  action: 'approve' | 'reject',
  comment?: string
): Promise<string> {
  const response = await fetch(`${BFF_BASE_URL}/documents/bulk-approval-action`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify({ action, comment, documentIds }),
  });

  if (!response.ok) {
    throw new Error(`Failed to perform bulk ${action}: ${response.statusText}`);
  }

  return response.text();
}
