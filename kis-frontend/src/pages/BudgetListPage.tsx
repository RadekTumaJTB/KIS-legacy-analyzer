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
import type { BudgetSummaryDTO } from '../types/budget';
import { fetchBudgets, createBudget } from '../api/budgetApi';
import { Button } from '../components/ui/Button';
import { Input } from '../components/ui/Input';
import NewBudgetModal from '../components/NewBudgetModal';
import { formatCurrency, cn } from '../lib/utils';
import './BudgetListPage.css';

export default function BudgetListPage() {
  const [budgets, setBudgets] = useState<BudgetSummaryDTO[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  // Table state
  const [sorting, setSorting] = useState<SortingState>([]);
  const [columnFilters, setColumnFilters] = useState<ColumnFiltersState>([]);
  const [globalFilter, setGlobalFilter] = useState('');
  const [isNewBudgetModalOpen, setIsNewBudgetModalOpen] = useState(false);

  useEffect(() => {
    const loadBudgets = async () => {
      try {
        setLoading(true);
        const result = await fetchBudgets();
        setBudgets(result);
        setError(null);
      } catch (err) {
        setError(err instanceof Error ? err.message : 'Failed to load budgets');
      } finally {
        setLoading(false);
      }
    };

    loadBudgets();
  }, []);

  const handleCreateBudget = async (data: {
    code: string;
    name: string;
    type: string;
    year: string;
    plannedAmount: string;
    departmentName: string;
    description?: string;
  }) => {
    try {
      const created = await createBudget(data);

      const newSummary: BudgetSummaryDTO = {
        id: created.id,
        code: created.code,
        name: created.name,
        type: created.type as 'REVENUE' | 'EXPENSE' | 'CAPEX',
        year: created.year,
        status: created.status as 'DRAFT' | 'ACTIVE' | 'LOCKED' | 'ARCHIVED',
        plannedAmount: created.totalPlanned,
        actualAmount: created.totalActual,
        variance: created.totalVariance,
        utilizationPercent: created.utilizationPercent,
        departmentName: created.departmentName,
        ownerName: created.owner.name,
        validFrom: created.validFrom.toString(),
        validTo: created.validTo.toString(),
      };

      setBudgets(prev => [newSummary, ...prev]);
      alert('✓ Rozpočet byl úspěšně vytvořen');
    } catch (error) {
      console.error('Failed to create budget:', error);
      alert('Nepodařilo se vytvořit rozpočet');
      throw error;
    }
  };

  const columns = useMemo<ColumnDef<BudgetSummaryDTO>[]>(
    () => [
      {
        accessorKey: 'code',
        header: 'Kód',
        cell: ({ row }) => (
          <Link
            to={`/budgets/${row.original.id}`}
            className="text-blue-600 hover:underline font-semibold"
          >
            {row.getValue('code')}
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
        accessorKey: 'departmentName',
        header: 'Oddělení',
        cell: ({ row }) => (
          <span className="text-sm text-gray-700">{row.getValue('departmentName')}</span>
        ),
      },
      {
        accessorKey: 'type',
        header: 'Typ',
        cell: ({ row }) => {
          const type = row.getValue('type') as string;
          return (
            <span
              className={cn(
                'inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium',
                {
                  'bg-blue-100 text-blue-800': type === 'REVENUE',
                  'bg-red-100 text-red-800': type === 'EXPENSE',
                  'bg-purple-100 text-purple-800': type === 'CAPEX',
                }
              )}
            >
              {type}
            </span>
          );
        },
      },
      {
        accessorKey: 'year',
        header: 'Rok',
        cell: ({ row }) => (
          <span className="text-sm text-gray-700">{row.getValue('year')}</span>
        ),
      },
      {
        accessorKey: 'plannedAmount',
        header: 'Plánováno',
        cell: ({ row }) => (
          <span className="text-sm font-semibold text-gray-900">
            {formatCurrency(row.getValue('plannedAmount'), 'CZK')}
          </span>
        ),
      },
      {
        accessorKey: 'actualAmount',
        header: 'Skutečné',
        cell: ({ row }) => (
          <span className="text-sm font-semibold text-green-700">
            {formatCurrency(row.getValue('actualAmount'), 'CZK')}
          </span>
        ),
      },
      {
        accessorKey: 'utilizationPercent',
        header: 'Využití',
        cell: ({ row }) => {
          const utilization = row.getValue('utilizationPercent') as number;
          return (
            <div className="utilization-cell">
              <div className="utilization-bar">
                <div
                  className={cn('utilization-fill', {
                    'on-track': utilization <= 95,
                    'warning': utilization > 95 && utilization <= 105,
                    'over-budget': utilization > 105,
                  })}
                  style={{ width: `${Math.min(utilization, 100)}%` }}
                ></div>
              </div>
              <span className="utilization-text">{utilization.toFixed(1)}%</span>
            </div>
          );
        },
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
                  'bg-green-100 text-green-800': status === 'ACTIVE',
                  'bg-gray-100 text-gray-800': status === 'DRAFT',
                  'bg-yellow-100 text-yellow-800': status === 'LOCKED',
                  'bg-red-100 text-red-800': status === 'ARCHIVED',
                }
              )}
            >
              {status}
            </span>
          );
        },
      },
      {
        id: 'actions',
        header: 'Akce',
        cell: ({ row }) => (
          <Link to={`/budgets/${row.original.id}`}>
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
    data: budgets,
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
      <div className="budget-list loading">
        <div className="spinner"></div>
        <p>Načítám rozpočty...</p>
      </div>
    );
  }

  if (error) {
    return (
      <div className="budget-list error">
        <h2>❌ Chyba</h2>
        <p>{error}</p>
      </div>
    );
  }

  return (
    <div className="budget-list">
      {/* Header */}
      <div className="list-header">
        <div>
          <h1>💰 Rozpočty</h1>
          <p className="text-gray-600">
            {table.getFilteredRowModel().rows.length} rozpočtů
          </p>
        </div>
        <div className="header-actions">
          <Link to="/budgets/dashboard">
            <Button variant="secondary">📊 Dashboard</Button>
          </Link>
          <Button variant="primary" onClick={() => setIsNewBudgetModalOpen(true)}>
            + Nový rozpočet
          </Button>
        </div>
      </div>

      {/* Search & Filters */}
      <div className="search-filters">
        <div className="search-box">
          <Input
            type="search"
            placeholder="Hledat v rozpočtech..."
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
            <option value="ACTIVE">Aktivní</option>
            <option value="DRAFT">Koncept</option>
            <option value="LOCKED">Uzamčeno</option>
            <option value="ARCHIVED">Archivováno</option>
          </select>
          <select
            className="filter-select"
            value={(table.getColumn('type')?.getFilterValue() as string) ?? ''}
            onChange={(e) =>
              table.getColumn('type')?.setFilterValue(e.target.value || undefined)
            }
          >
            <option value="">Všechny typy</option>
            <option value="REVENUE">Příjmy</option>
            <option value="EXPENSE">Náklady</option>
            <option value="CAPEX">Kapitálové výdaje</option>
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

      {/* New Budget Modal */}
      <NewBudgetModal
        isOpen={isNewBudgetModalOpen}
        onClose={() => setIsNewBudgetModalOpen(false)}
        onSubmit={handleCreateBudget}
      />
    </div>
  );
}
