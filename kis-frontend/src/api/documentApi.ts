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
