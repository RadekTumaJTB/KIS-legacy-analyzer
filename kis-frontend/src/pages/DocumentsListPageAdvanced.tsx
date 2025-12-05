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
import type { DocumentSummaryDTO } from '../types/document';
import { fetchDocuments } from '../api/documentApi';
import { Button } from '../components/ui/Button';
import { Input } from '../components/ui/Input';
import { Checkbox } from '../components/ui/Checkbox';
import ApprovalModal from '../components/ApprovalModal';
import DocumentFiltersPanel from '../components/DocumentFiltersPanel';
import type { DocumentFilters } from '../components/DocumentFiltersPanel';
import { formatCurrency, formatDate, cn } from '../lib/utils';
import './DocumentsListPageAdvanced.css';

export default function DocumentsListPageAdvanced() {
  const [documents, setDocuments] = useState<DocumentSummaryDTO[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  // Table state
  const [sorting, setSorting] = useState<SortingState>([]);
  const [columnFilters, setColumnFilters] = useState<ColumnFiltersState>([]);
  const [rowSelection, setRowSelection] = useState({});
  const [globalFilter, setGlobalFilter] = useState('');

  // Modal state
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [modalAction, setModalAction] = useState<'approve' | 'reject'>('approve');

  // Advanced filters state
  const [advancedFilters, setAdvancedFilters] = useState<DocumentFilters>({
    status: [],
    type: [],
    dateFrom: '',
    dateTo: '',
    amountMin: '',
    amountMax: '',
    companyName: '',
    createdBy: '',
  });

  useEffect(() => {
    const loadDocuments = async () => {
      try {
        setLoading(true);
        const result = await fetchDocuments();
        setDocuments(result);
        setError(null);
      } catch (err) {
        setError(err instanceof Error ? err.message : 'Failed to load documents');
      } finally {
        setLoading(false);
      }
    };

    loadDocuments();
  }, []);

  const columns = useMemo<ColumnDef<DocumentSummaryDTO>[]>(
    () => [
      {
        id: 'select',
        header: ({ table }) => (
          <Checkbox
            checked={table.getIsAllPageRowsSelected()}
            indeterminate={table.getIsSomePageRowsSelected()}
            onChange={table.getToggleAllPageRowsSelectedHandler()}
            aria-label="Select all"
          />
        ),
        cell: ({ row }) => (
          <Checkbox
            checked={row.getIsSelected()}
            disabled={!row.getCanSelect()}
            onChange={row.getToggleSelectedHandler()}
            aria-label="Select row"
          />
        ),
        enableSorting: false,
        enableColumnFilter: false,
      },
      {
        accessorKey: 'number',
        header: 'Číslo dokumentu',
        cell: ({ row }) => (
          <Link
            to={`/documents/${row.original.id}`}
            className="text-blue-600 hover:underline font-semibold"
          >
            {row.getValue('number')}
          </Link>
        ),
      },
      {
        accessorKey: 'type',
        header: 'Typ',
        cell: ({ row }) => (
          <span className="text-sm text-gray-700">{row.getValue('type')}</span>
        ),
      },
      {
        accessorKey: 'companyName',
        header: 'Společnost',
        cell: ({ row }) => (
          <span className="text-sm text-gray-900">{row.getValue('companyName')}</span>
        ),
      },
      {
        accessorKey: 'amount',
        header: 'Částka',
        cell: ({ row }) => (
          <span className="text-sm font-semibold text-green-700">
            {formatCurrency(row.getValue('amount'), row.original.currency)}
          </span>
        ),
      },
      {
        accessorKey: 'dueDate',
        header: 'Splatnost',
        cell: ({ row }) => (
          <span className="text-sm text-gray-600">
            {formatDate(row.getValue('dueDate'))}
          </span>
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
                  'bg-yellow-100 text-yellow-800': status === 'PENDING_APPROVAL',
                  'bg-green-100 text-green-800': status === 'APPROVED',
                  'bg-red-100 text-red-800': status === 'OVERDUE',
                  'bg-gray-100 text-gray-800': status === 'DRAFT',
                }
              )}
            >
              {status}
            </span>
          );
        },
      },
      {
        accessorKey: 'createdByName',
        header: 'Vytvořil',
        cell: ({ row }) => (
          <span className="text-sm text-gray-700">{row.getValue('createdByName')}</span>
        ),
      },
      {
        id: 'actions',
        header: 'Akce',
        cell: ({ row }) => (
          <div className="flex gap-2">
            <Link to={`/documents/${row.original.id}`}>
              <Button variant="ghost" size="sm">
                Detail
              </Button>
            </Link>
          </div>
        ),
        enableSorting: false,
        enableColumnFilter: false,
      },
    ],
    []
  );

  // Apply advanced filters to documents
  const filteredDocuments = useMemo(() => {
    let filtered = documents;

    // Status filter
    if (advancedFilters.status.length > 0) {
      filtered = filtered.filter(doc => advancedFilters.status.includes(doc.status));
    }

    // Type filter
    if (advancedFilters.type.length > 0) {
      filtered = filtered.filter(doc => advancedFilters.type.includes(doc.type));
    }

    // Date range filter
    if (advancedFilters.dateFrom) {
      const fromDate = new Date(advancedFilters.dateFrom);
      filtered = filtered.filter(doc => new Date(doc.dueDate) >= fromDate);
    }
    if (advancedFilters.dateTo) {
      const toDate = new Date(advancedFilters.dateTo);
      filtered = filtered.filter(doc => new Date(doc.dueDate) <= toDate);
    }

    // Amount range filter
    if (advancedFilters.amountMin) {
      const minAmount = parseFloat(advancedFilters.amountMin);
      filtered = filtered.filter(doc => doc.amount >= minAmount);
    }
    if (advancedFilters.amountMax) {
      const maxAmount = parseFloat(advancedFilters.amountMax);
      filtered = filtered.filter(doc => doc.amount <= maxAmount);
    }

    // Company name filter
    if (advancedFilters.companyName) {
      const searchTerm = advancedFilters.companyName.toLowerCase();
      filtered = filtered.filter(doc =>
        doc.companyName.toLowerCase().includes(searchTerm)
      );
    }

    // Created by filter
    if (advancedFilters.createdBy) {
      const searchTerm = advancedFilters.createdBy.toLowerCase();
      filtered = filtered.filter(doc =>
        doc.createdByName.toLowerCase().includes(searchTerm)
      );
    }

    return filtered;
  }, [documents, advancedFilters]);

  const table = useReactTable({
    data: filteredDocuments,
    columns,
    state: {
      sorting,
      columnFilters,
      rowSelection,
      globalFilter,
    },
    onSortingChange: setSorting,
    onColumnFiltersChange: setColumnFilters,
    onRowSelectionChange: setRowSelection,
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

  const selectedCount = table.getFilteredSelectedRowModel().rows.length;

  const handleOpenModal = (action: 'approve' | 'reject') => {
    setModalAction(action);
    setIsModalOpen(true);
  };

  const handleCloseModal = () => {
    setIsModalOpen(false);
  };

  const handleSubmitApproval = async (action: 'approve' | 'reject', comment: string) => {
    const selectedRows = table.getFilteredSelectedRowModel().rows;
    const selectedIds = selectedRows.map(row => row.original.id);

    console.log(`${action} documents:`, selectedIds, 'with comment:', comment);

    // TODO: Implement actual API call to BFF
    // await approveDocuments(selectedIds, comment);
    // await rejectDocuments(selectedIds, comment);

    // Simulate API call
    await new Promise(resolve => setTimeout(resolve, 1000));

    // Clear selection after successful operation
    table.resetRowSelection();

    // Optionally reload documents
    // loadDocuments();
  };

  const handleFiltersChange = (filters: DocumentFilters) => {
    setAdvancedFilters(filters);
  };

  const handleClearFilters = () => {
    setAdvancedFilters({
      status: [],
      type: [],
      dateFrom: '',
      dateTo: '',
      amountMin: '',
      amountMax: '',
      companyName: '',
      createdBy: '',
    });
    // Clear table column filters as well
    table.resetColumnFilters();
    setGlobalFilter('');
  };

  if (loading) {
    return (
      <div className="documents-list-advanced loading">
        <div className="spinner"></div>
        <p>Načítám dokumenty...</p>
      </div>
    );
  }

  if (error) {
    return (
      <div className="documents-list-advanced error">
        <h2>❌ Chyba</h2>
        <p>{error}</p>
      </div>
    );
  }

  return (
    <div className="documents-list-advanced">
      {/* Header */}
      <div className="list-header">
        <div>
          <h1>📄 Dokumenty</h1>
          <p className="text-gray-600">
            {table.getFilteredRowModel().rows.length} dokumentů
            {selectedCount > 0 && ` (${selectedCount} vybráno)`}
          </p>
        </div>
        <div className="header-actions">
          <Button variant="primary">+ Nový dokument</Button>
        </div>
      </div>

      {/* Bulk Actions Bar */}
      {selectedCount > 0 && (
        <div className="bulk-actions-bar">
          <span className="text-sm font-medium text-gray-700">
            {selectedCount} položek vybráno
          </span>
          <div className="flex gap-2">
            <Button
              variant="success"
              size="sm"
              onClick={() => handleOpenModal('approve')}
            >
              ✓ Schválit ({selectedCount})
            </Button>
            <Button
              variant="danger"
              size="sm"
              onClick={() => handleOpenModal('reject')}
            >
              ✗ Zamítnout ({selectedCount})
            </Button>
            <Button variant="secondary" size="sm">
              📧 Odeslat email
            </Button>
            <Button
              variant="ghost"
              size="sm"
              onClick={() => table.resetRowSelection()}
            >
              Zrušit výběr
            </Button>
          </div>
        </div>
      )}

      {/* Advanced Filters Panel */}
      <DocumentFiltersPanel
        filters={advancedFilters}
        onFiltersChange={handleFiltersChange}
        onClearFilters={handleClearFilters}
      />

      {/* Search & Filters */}
      <div className="search-filters">
        <div className="search-box">
          <Input
            type="search"
            placeholder="Hledat v dokumentech..."
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
            <option value="PENDING_APPROVAL">Čeká na schválení</option>
            <option value="APPROVED">Schváleno</option>
            <option value="OVERDUE">Po splatnosti</option>
            <option value="DRAFT">Koncept</option>
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

      {/* Approval Modal */}
      <ApprovalModal
        isOpen={isModalOpen}
        onClose={handleCloseModal}
        onSubmit={handleSubmitApproval}
        selectedCount={selectedCount}
        action={modalAction}
      />
    </div>
  );
}
