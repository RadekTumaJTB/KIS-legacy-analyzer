import { useEffect, useState, useMemo } from 'react';
import { Link } from 'react-router-dom';
import {
  useReactTable,
  getCoreRowModel,
  getSortedRowModel,
  getFilteredRowModel,
  getPaginationRowModel,
  flexRender,
} from '@tanstack/react-table';
import type {
  ColumnDef,
  SortingState,
  ColumnFiltersState,
} from '@tanstack/react-table';
import type { ProjectSummaryDTO } from '../types/project';
import { fetchProjects, createProject } from '../api/projectApi';
import { Button } from '../components/ui/Button';
import { Input } from '../components/ui/Input';
import NewProjectModal from '../components/NewProjectModal';
import { formatDate, cn } from '../lib/utils';
import './ProjectListPage.css';

export default function ProjectListPage() {
  const [projects, setProjects] = useState<ProjectSummaryDTO[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  // Table state
  const [sorting, setSorting] = useState<SortingState>([]);
  const [columnFilters, setColumnFilters] = useState<ColumnFiltersState>([]);
  const [globalFilter, setGlobalFilter] = useState('');
  const [isNewProjectModalOpen, setIsNewProjectModalOpen] = useState(false);

  useEffect(() => {
    const loadProjects = async () => {
      try {
        setLoading(true);
        const result = await fetchProjects();
        setProjects(result);
        setError(null);
      } catch (err) {
        setError(err instanceof Error ? err.message : 'Failed to load projects');
      } finally {
        setLoading(false);
      }
    };

    loadProjects();
  }, []);

  const handleCreateProject = async (data: {
    name: string;
    projectNumber: string;
    description?: string;
    startDate: string;
  }) => {
    try {
      const created = await createProject(data);

      const newSummary: ProjectSummaryDTO = {
        id: created.id,
        name: created.name,
        projectNumber: created.projectNumber,
        status: created.status,
        statusCode: created.statusCode,
        projectManagerName: created.projectManager.name,
        managementSegmentName: created.managementSegment.name,
        startDate: created.startDate,
        description: created.description,
      };

      setProjects(prev => [newSummary, ...prev]);
      alert('✓ Projekt byl úspěšně vytvořen');
    } catch (error) {
      console.error('Failed to create project:', error);
      alert('Nepodařilo se vytvořit projekt');
      throw error;
    }
  };

  const columns = useMemo<ColumnDef<ProjectSummaryDTO>[]>(
    () => [
      {
        accessorKey: 'projectNumber',
        header: 'Číslo projektu',
        cell: ({ row }) => (
          <Link
            to={`/projects/${row.original.id}`}
            className="text-blue-600 hover:underline font-semibold"
          >
            {row.getValue('projectNumber')}
          </Link>
        ),
      },
      {
        accessorKey: 'name',
        header: 'Název',
        cell: ({ row }) => (
          <span className="text-sm text-gray-900">{row.getValue('name')}</span>
        ),
      },
      {
        accessorKey: 'status',
        header: 'Status',
        cell: ({ row }) => {
          const status = row.getValue('status') as string;
          return (
            <span
              className={cn(
                'inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium',
                {
                  'bg-green-100 text-green-800': status === 'Aktivní',
                  'bg-yellow-100 text-yellow-800': status === 'V přípravě',
                  'bg-blue-100 text-blue-800': status === 'Probíhá',
                  'bg-gray-100 text-gray-800': status === 'Pozastaveno',
                  'bg-red-100 text-red-800': status === 'Ukončeno',
                }
              )}
            >
              {status}
            </span>
          );
        },
      },
      {
        accessorKey: 'projectManagerName',
        header: 'Projektový manažer',
        cell: ({ row }) => (
          <span className="text-sm text-gray-700">{row.getValue('projectManagerName')}</span>
        ),
      },
      {
        accessorKey: 'managementSegmentName',
        header: 'Oddělení',
        cell: ({ row }) => (
          <span className="text-sm text-gray-700">{row.getValue('managementSegmentName')}</span>
        ),
      },
      {
        accessorKey: 'startDate',
        header: 'Datum zahájení',
        cell: ({ row }) => (
          <span className="text-sm text-gray-700">
            {formatDate(row.getValue('startDate'))}
          </span>
        ),
      },
      {
        id: 'actions',
        header: 'Akce',
        cell: ({ row }) => (
          <Link to={`/projects/${row.original.id}`}>
            <Button variant="ghost" size="sm">
              Detail
            </Button>
          </Link>
        ),
        enableSorting: false,
        enableColumnFilter: false,
      },
    ],
    []
  );

  const table = useReactTable({
    data: projects,
    columns,
    state: {
      sorting,
      columnFilters,
      globalFilter,
    },
    onSortingChange: setSorting,
    onColumnFiltersChange: setColumnFilters,
    onGlobalFilterChange: setGlobalFilter,
    getCoreRowModel: getCoreRowModel(),
    getSortedRowModel: getSortedRowModel(),
    getFilteredRowModel: getFilteredRowModel(),
    getPaginationRowModel: getPaginationRowModel(),
    initialState: {
      pagination: {
        pageSize: 10,
      },
    },
  });

  if (loading) {
    return (
      <div className="project-list loading">
        <div className="spinner"></div>
        <p>Načítám projekty...</p>
      </div>
    );
  }

  if (error) {
    return (
      <div className="project-list error">
        <h2>❌ Chyba</h2>
        <p>{error}</p>
      </div>
    );
  }

  return (
    <div className="project-list">
      {/* Header */}
      <div className="list-header">
        <div>
          <h1>📋 Projekty</h1>
          <p className="text-gray-600">
            {table.getFilteredRowModel().rows.length} projektů
          </p>
        </div>
        <div className="header-actions">
          <Button variant="primary" onClick={() => setIsNewProjectModalOpen(true)}>
            + Nový projekt
          </Button>
        </div>
      </div>

      {/* Search & Filters */}
      <div className="search-filters">
        <div className="search-box">
          <Input
            type="search"
            placeholder="Hledat v projektech..."
            value={globalFilter}
            onChange={(e) => setGlobalFilter(e.target.value)}
            className="w-full max-w-sm"
          />
        </div>
        <div className="filters">
          <select
            className="filter-select"
            value={(table.getColumn('status')?.getFilterValue() as string) ?? ''}
            onChange={(e) =>
              table.getColumn('status')?.setFilterValue(e.target.value || undefined)
            }
          >
            <option value="">Všechny statusy</option>
            <option value="Aktivní">Aktivní</option>
            <option value="V přípravě">V přípravě</option>
            <option value="Probíhá">Probíhá</option>
            <option value="Pozastaveno">Pozastaveno</option>
            <option value="Ukončeno">Ukončeno</option>
          </select>
        </div>
      </div>

      {/* Table */}
      <div className="table-container">
        <table className="data-table">
          <thead>
            {table.getHeaderGroups().map((headerGroup) => (
              <tr key={headerGroup.id}>
                {headerGroup.headers.map((header) => (
                  <th key={header.id}>
                    {header.isPlaceholder ? null : (
                      <div
                        className={cn(
                          'flex items-center gap-2',
                          header.column.getCanSort() && 'cursor-pointer select-none'
                        )}
                        onClick={header.column.getToggleSortingHandler()}
                      >
                        {flexRender(
                          header.column.columnDef.header,
                          header.getContext()
                        )}
                        {header.column.getCanSort() && (
                          <span className="sort-indicator">
                            {{
                              asc: '↑',
                              desc: '↓',
                            }[header.column.getIsSorted() as string] ?? '↕'}
                          </span>
                        )}
                      </div>
                    )}
                  </th>
                ))}
              </tr>
            ))}
          </thead>
          <tbody>
            {table.getRowModel().rows.map((row) => (
              <tr key={row.id}>
                {row.getVisibleCells().map((cell) => (
                  <td key={cell.id}>
                    {flexRender(cell.column.columnDef.cell, cell.getContext())}
                  </td>
                ))}
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      {/* Pagination */}
      <div className="pagination">
        <div className="pagination-info">
          Stránka {table.getState().pagination.pageIndex + 1} z{' '}
          {table.getPageCount()}
        </div>
        <div className="pagination-controls">
          <Button
            variant="secondary"
            size="sm"
            onClick={() => table.setPageIndex(0)}
            disabled={!table.getCanPreviousPage()}
          >
            {'<<'}
          </Button>
          <Button
            variant="secondary"
            size="sm"
            onClick={() => table.previousPage()}
            disabled={!table.getCanPreviousPage()}
          >
            {'<'}
          </Button>
          <Button
            variant="secondary"
            size="sm"
            onClick={() => table.nextPage()}
            disabled={!table.getCanNextPage()}
          >
            {'>'}
          </Button>
          <Button
            variant="secondary"
            size="sm"
            onClick={() => table.setPageIndex(table.getPageCount() - 1)}
            disabled={!table.getCanNextPage()}
          >
            {'>>'}
          </Button>
        </div>
        <select
          className="page-size-select"
          value={table.getState().pagination.pageSize}
          onChange={(e) => table.setPageSize(Number(e.target.value))}
        >
          {[5, 10, 20, 50].map((pageSize) => (
            <option key={pageSize} value={pageSize}>
              Zobrazit {pageSize}
            </option>
          ))}
        </select>
      </div>

      {/* New Project Modal */}
      <NewProjectModal
        isOpen={isNewProjectModalOpen}
        onClose={() => setIsNewProjectModalOpen(false)}
        onSubmit={handleCreateProject}
      />
    </div>
  );
}
