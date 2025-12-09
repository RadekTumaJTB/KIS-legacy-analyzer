import type { ProjectDetailDTO, ProjectSummaryDTO } from '../types/project';

const BFF_BASE_URL = '/bff';

/**
 * Fetch all projects (list view)
 */
export async function fetchProjects(): Promise<ProjectSummaryDTO[]> {
  const response = await fetch(`${BFF_BASE_URL}/projects`);

  if (!response.ok) {
    throw new Error(`Failed to fetch projects: ${response.statusText}`);
  }

  return response.json();
}

/**
 * Fetch project detail with cash flow list
 */
export async function fetchProjectDetail(id: number): Promise<ProjectDetailDTO> {
  const response = await fetch(`${BFF_BASE_URL}/projects/${id}`);

  if (!response.ok) {
    throw new Error(`Failed to fetch project ${id}: ${response.statusText}`);
  }

  return response.json();
}

/**
 * Create new project
 */
export async function createProject(data: {
  name: string;
  projectNumber: string;
  description?: string;
  startDate: string;
}): Promise<ProjectDetailDTO> {
  const response = await fetch(`${BFF_BASE_URL}/projects`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify({
      name: data.name,
      projectNumber: data.projectNumber,
      description: data.description || '',
      startDate: data.startDate,
    }),
  });

  if (!response.ok) {
    throw new Error(`Failed to create project: ${response.statusText}`);
  }

  return response.json();
}

/**
 * Update project
 */
export async function updateProject(
  id: number,
  data: {
    name: string;
    description?: string;
    startDate: string;
  }
): Promise<ProjectDetailDTO> {
  const response = await fetch(`${BFF_BASE_URL}/projects/${id}`, {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify({
      name: data.name,
      description: data.description || '',
      startDate: data.startDate,
    }),
  });

  if (!response.ok) {
    throw new Error(`Failed to update project ${id}: ${response.statusText}`);
  }

  return response.json();
}
