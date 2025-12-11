# Module 2: Assets (Emise a Majetkové Účasti) - UI Design Specification

**Date**: 2025-12-09
**Source**: UI Designer Agent Analysis + JSP Source Files
**Technology**: React 19 + TypeScript + Tailwind CSS

---

## Overview

Based on detailed JSP analysis, the Assets module requires **12 distinct React components** across 5 main pages, not the simplified 3-4 component structure originally planned.

---

## Pages & Components (From JSP Analysis)

### Page 1: Emissions Management (`FininvInvestice.jsp` → `EmissionsPage.tsx`)

**Purpose**: Manage financial investment emissions with inline editing grid

**Components Required** (5):
1. **EmissionListPage.tsx** - Main container
2. **EmissionItemRow.tsx** - Individual editable emission row
3. **NewEmissionModal.tsx** - Add new financial investment
4. **EmissionHistoryModal.tsx** - View version history
5. **EmissionExportModal.tsx** - Excel export configuration

**Key Features Identified**:
- ✅ Inline editing grid (not separate edit modal!)
- ✅ Dynamic row addition (`addLine()` JavaScript → React state arrays)
- ✅ Hidden action fields (I/U/D tracking) → Controlled components
- ✅ Collapsible/expandable rows (`switchMe()` → useState for expanded IDs)
- ✅ Auto-calculations (Volume = Quantity × Nominal)
- ✅ Currency handling with exchange rates

**JSP Pattern Found**:
```jsp
<!-- Dynamic row generation -->
<tr id="line_<%= i %>">
  <td><input type="hidden" name="action_<%= i %>" value="U"></td>
  <td><input name="shares_<%= i %>" onchange="calculate(<%= i %>)"></td>
</tr>
<script>
function addLine() {
  // Add new row dynamically
}
</script>
```

**React Equivalent Structure**:
```typescript
interface EmissionRow {
  id: string;
  action: 'I' | 'U' | 'D'; // Insert, Update, Delete
  shares: number;
  nominal: BigDecimal;
  volume: BigDecimal;  // Auto-calculated
  isExpanded: boolean;
}

const [rows, setRows] = useState<EmissionRow[]>([]);
const addRow = () => setRows([...rows, newEmissionRow()]);
```

---

### Page 2: Asset Companies Selection (`Majetek.jsp` → `AssetCompaniesPage.tsx`)

**Purpose**: Landing page for company selection with role-based filtering

**Components Required** (1):
1. **AssetCompaniesPage.tsx** - Company selection with permission filtering

**Key Features**:
- ✅ Role-based ViewObject selection (4 permission levels)
- ✅ Company grid display
- ✅ Navigation to participation management

**JSP Pattern**:
```jsp
<%
boolean admin = request.isUserInRole("Admin_MU");
boolean jednotlive = request.isUserInRole("MU_jednotlive");
String viewObjectName = jednotlive ?
    "VwMajetekUserPravaView1" :
    "VwMajetekOverviewView1";
%>
```

**React Equivalent**:
- Use `usePermissions()` hook
- BFF middleware returns filtered data based on user role
- No client-side filtering needed

---

### Page 3: Asset Participation Management (`MajetekPrehled.jsp` → `AssetParticipationPage.tsx`)

**Purpose**: Main CRUD for equity stakes

**Components Required** (3):
1. **AssetParticipationPage.tsx** - Main container
2. **NewParticipationModal.tsx** - Create equity stake
3. **EditParticipationModal.tsx** - Edit existing stake

**Key Features**:
- ✅ Transaction type selection (Nákup, Prodej, Transfer)
- ✅ Dual currency handling (transaction + accounting)
- ✅ Exchange rate calculations
- ✅ Percentage of ownership calculations
- ✅ Temporal validity (valid from/to dates)

---

### Page 4: Asset Overview (`MajetekPrehledFiltr.jsp` → `AssetOverviewPage.tsx`)

**Purpose**: Snapshot view with complex calculations

**Components Required** (2):
1. **AssetOverviewPage.tsx** - Overview dashboard
2. **AssetOverviewFilters.tsx** - Date range filters

**Key Features**:
- ✅ Date range filtering (snapshot at specific date)
- ✅ Percentage calculations:
  - Share of total emission
  - Share of company ownership
- ✅ Aggregated volume calculations
- ✅ Multi-currency display

**Complex Calculations**:
```typescript
// Example from JSP analysis:
interface OwnershipCalculation {
  sharesOwned: BigDecimal;
  totalEmissionShares: BigDecimal;
  ownershipPercentage: BigDecimal;  // (sharesOwned / totalEmissionShares) * 100

  shareOfInvestment: BigDecimal;    // From parent company perspective
  consolidatedPercentage: BigDecimal; // Recursive calculation through holding structure
}
```

---

### Page 5: Asset Control Rules (`MajetekKontrola.jsp` → `AssetControlPage.tsx`)

**Purpose**: Define and validate account control rules

**Components Required** (2):
1. **AssetControlPage.tsx** - Rules management
2. **ControlRuleModal.tsx** - Add/edit rules

**Key Features**:
- ✅ Account number validation rules
- ✅ Equity stake type mapping
- ✅ Exception handling
- ✅ Validation result display

---

## Complete Component List (12 Total)

### Emissions Module (5 components):
1. ✅ `EmissionListPage.tsx`
2. ✅ `EmissionItemRow.tsx` - **CRITICAL**: Inline editable row
3. ✅ `NewEmissionModal.tsx`
4. ✅ `EmissionHistoryModal.tsx`
5. ✅ `EmissionExportModal.tsx`

### Asset Participations Module (7 components):
6. ✅ `AssetCompaniesPage.tsx`
7. ✅ `AssetParticipationPage.tsx`
8. ✅ `NewParticipationModal.tsx`
9. ✅ `EditParticipationModal.tsx`
10. ✅ `AssetOverviewPage.tsx`
11. ✅ `AssetOverviewFilters.tsx`
12. ✅ `AssetControlPage.tsx`

---

## TypeScript Types (From JSP Analysis)

```typescript
// Financial Investment
export interface FinancialInvestmentDTO {
  id: number;
  companyId: number;
  companyName: string;
  currency: string;
  isinCode: string;
  lastModified: string;
  modifiedByUser: string;
}

// Emission with inline editing support
export interface EmissionItemDTO {
  id?: number;
  financialInvestmentId: number;
  action: 'I' | 'U' | 'D';  // Track insert/update/delete
  validFrom: string;
  validTo?: string;
  numberOfShares: number;
  nominalValue: number;
  registeredCapital: number;
  volume: number;  // Auto-calculated: shares × nominal
  investmentFlag: boolean;
  isExpanded?: boolean;  // For collapsible rows
  isDirty?: boolean;     // Track unsaved changes
}

// Emission with items (for batch updates)
export interface EmissionWithItemsDTO {
  financialInvestment: FinancialInvestmentDTO;
  emissionItems: EmissionItemDTO[];
}

// Equity Stake
export interface EquityStakeDTO {
  id: number;
  emissionId: number;
  accountingCompanyId: number;
  accountNumber: string;
  validFrom: string;
  validTo?: string;
  transactionTypeId: number;
  methodId: number;
  numberOfShares: number;

  // Transaction currency
  transactionCurrency: string;
  pricePerShareTransaction: number;
  totalTransactionAmount: number;

  // Exchange rate
  exchangeRate: number;

  // Accounting currency
  accountingCurrency: string;
  pricePerShareAccounting: number;
  totalAccountingAmount: number;

  purchasedFromCompanyId?: number;
  ignoreFlag: boolean;
}

// Asset Overview (with calculations)
export interface AssetOverviewDTO {
  emissionId: number;
  companyName: string;
  isinCode: string;
  totalEmissionShares: number;
  sharesOwned: number;
  ownershipPercentage: number;  // Calculated
  marketValue: number;
  bookValue: number;
  unrealizedGainLoss: number;   // Calculated
}

// Control Rule
export interface AssetControlRuleDTO {
  id: number;
  accountPattern: string;
  equityStakeTypeId: number;
  isActive: boolean;
  validationMessage?: string;
}
```

---

## API Endpoints (From JSP Analysis)

### Emissions:
```
GET    /bff/emissions                    # List all emissions
POST   /bff/emissions                    # Create new financial investment
GET    /bff/emissions/:id                # Get emission detail
PUT    /bff/emissions/:id                # Update financial investment metadata
DELETE /bff/emissions/:id                # Delete financial investment

POST   /bff/emissions/:id/items          # Batch update emission items (I/U/D)
GET    /bff/emissions/:id/history        # Get version history
GET    /bff/emissions/export             # Excel export
```

### Assets (Equity Stakes):
```
GET    /bff/assets/companies             # Get companies with role filtering
GET    /bff/assets/companies/:id/participations  # Get equity stakes for company

POST   /bff/assets/participations        # Create equity stake
PUT    /bff/assets/participations/:id    # Update equity stake
DELETE /bff/assets/participations/:id    # Delete equity stake

GET    /bff/assets/overview              # Get overview with date filters
  ?asOfDate=2025-01-01                   # Snapshot at specific date
  &companyId=123                         # Filter by company

POST   /bff/assets/controls              # Create control rule
GET    /bff/assets/controls              # List control rules
PUT    /bff/assets/controls/:id          # Update control rule
DELETE /bff/assets/controls/:id          # Delete control rule

GET    /bff/assets/export                # Excel export
```

---

## Oracle Integration (Procedures to Call from BFF)

**Package**: `FININV_MODULE` (or similar)

```sql
-- Emission CRUD
PROCEDURE investiceEmise(
  p_id IN NUMBER,
  p_action IN VARCHAR2,  -- 'I', 'U', 'D'
  p_fin_inv_id IN NUMBER,
  p_shares IN NUMBER,
  p_nominal IN NUMBER,
  ...
);

-- Financial Investment metadata
PROCEDURE updateFinInv(
  p_id IN NUMBER,
  p_company_id IN NUMBER,
  p_currency IN VARCHAR2,
  p_isin IN VARCHAR2
);
```

**Package**: `MAJETEK_MODULE`

```sql
-- Equity Stake CRUD
PROCEDURE majetkovaUcast(
  p_id IN NUMBER,
  p_action IN VARCHAR2,
  p_emission_id IN NUMBER,
  ...
);

-- Ownership percentage calculation
FUNCTION getPodilInvestice(
  p_emission_id IN NUMBER,
  p_as_of_date IN DATE
) RETURN NUMBER;

-- Emission volume at date
FUNCTION getEmiseMap(
  p_emission_id IN NUMBER,
  p_as_of_date IN DATE
) RETURN SYS_REFCURSOR;
```

---

## Implementation Priority (Corrected)

### Week 1-2: Foundation
- ✅ Backend entities (DONE)
- ✅ Backend repositories (DONE)
- ⏳ BFF Service layer with Oracle procedure calls
- ⏳ Frontend TypeScript types
- ⏳ API client (`assetApi.ts`)

### Week 3-4: **Emissions Module** (5 components)
- `EmissionListPage.tsx` with inline editing grid
- `EmissionItemRow.tsx` - Key component with complex state
- `NewEmissionModal.tsx`
- `EmissionHistoryModal.tsx`
- `EmissionExportModal.tsx`

### Week 5: **Asset Companies Selection**
- `AssetCompaniesPage.tsx` with role-based filtering

### Week 6-7: **Asset Participations**
- `AssetParticipationPage.tsx`
- `NewParticipationModal.tsx`
- `EditParticipationModal.tsx`

### Week 8-9: **Overview & Analytics**
- `AssetOverviewPage.tsx` with percentage calculations
- `AssetOverviewFilters.tsx`

### Week 10: **Control Rules**
- `AssetControlPage.tsx`
- `ControlRuleModal.tsx`

### Week 11: **Excel Export & History**
- Export functionality
- Version history tracking

### Week 12: **Testing & Refinement**
- E2E tests (Playwright)
- Bug fixes
- Performance optimization

---

## Critical Differences from Original Plan

### ❌ WRONG (Original):
```
components/assets/
├── AssetTable.tsx          # Generic table
├── AssetEditModal.tsx      # Single edit modal
└── AssetControlPanel.tsx   # Control panel
```

### ✅ CORRECT (From JSP Analysis):
```
components/assets/
├── emissions/
│   ├── EmissionListPage.tsx
│   ├── EmissionItemRow.tsx        # INLINE EDITING!
│   ├── NewEmissionModal.tsx
│   ├── EmissionHistoryModal.tsx
│   └── EmissionExportModal.tsx
├── participations/
│   ├── AssetCompaniesPage.tsx
│   ├── AssetParticipationPage.tsx
│   ├── NewParticipationModal.tsx
│   └── EditParticipationModal.tsx
├── overview/
│   ├── AssetOverviewPage.tsx
│   └── AssetOverviewFilters.tsx
└── control/
    ├── AssetControlPage.tsx
    └── ControlRuleModal.tsx
```

---

## JSP to React Component Mapping Table

This table shows exactly which React components replace which JSP files from the legacy system.

| **Legacy JSP File** | **React Components** | **Count** | **Component Type** | **Purpose** |
|---------------------|---------------------|-----------|-------------------|------------|
| `FininvInvestice.jsp` | `EmissionListPage.tsx` | 1 | Page | Main container with inline editing grid |
| | `EmissionItemRow.tsx` | 1 | Row Component | Individual editable emission row |
| | `NewEmissionModal.tsx` | 1 | Modal | Add new financial investment |
| | `EmissionHistoryModal.tsx` | 1 | Modal | View version history |
| | `EmissionExportModal.tsx` | 1 | Modal | Excel export configuration |
| | **Subtotal** | **5** | | **Emissions Management** |
| `Majetek.jsp` | `AssetCompaniesPage.tsx` | 1 | Page | Company selection with role-based filtering |
| | **Subtotal** | **1** | | **Company Selection** |
| `MajetekPrehled.jsp` | `AssetParticipationPage.tsx` | 1 | Page | Main equity stakes CRUD container |
| | `NewParticipationModal.tsx` | 1 | Modal | Create new equity stake |
| | `EditParticipationModal.tsx` | 1 | Modal | Edit existing equity stake |
| | **Subtotal** | **3** | | **Equity Stakes CRUD** |
| `MajetekPrehledFiltr.jsp` | `AssetOverviewPage.tsx` | 1 | Page | Overview dashboard with calculations |
| | `AssetOverviewFilters.tsx` | 1 | Component | Date range and filter controls |
| | **Subtotal** | **2** | | **Overview & Analytics** |
| `MajetekKontrola.jsp` | `AssetControlPage.tsx` | 1 | Page | Rules management interface |
| | `ControlRuleModal.tsx` | 1 | Modal | Add/edit control rules |
| | **Subtotal** | **2** | | **Control Rules** |
| | | | | |
| **TOTAL** | **13 React Components** | **13** | **5 JSP files → 13 components** | |

### Key Observations:

1. **1 JSP → 5 React Components** (FininvInvestice.jsp)
   - Most complex page due to inline editing grid
   - Requires specialized row component (`EmissionItemRow.tsx`)
   - 3 supporting modals for different operations

2. **1 JSP → 1 React Component** (Majetek.jsp)
   - Simplest page - just company selection
   - Role-based filtering handled by BFF

3. **1 JSP → 3 React Components** (MajetekPrehled.jsp)
   - Standard CRUD pattern
   - Separate modals for Create vs. Edit operations

4. **1 JSP → 2 React Components** (MajetekPrehledFiltr.jsp)
   - Main page + separate filter component
   - Filters complex enough to warrant own component

5. **1 JSP → 2 React Components** (MajetekKontrola.jsp)
   - Main page + modal for rule editing
   - Similar pattern to participations

### Component Organization (Directory Structure):

```
kis-frontend/src/components/assets/
├── emissions/               # FininvInvestice.jsp (5 components)
│   ├── EmissionListPage.tsx
│   ├── EmissionItemRow.tsx
│   ├── NewEmissionModal.tsx
│   ├── EmissionHistoryModal.tsx
│   └── EmissionExportModal.tsx
│
├── participations/          # Majetek.jsp + MajetekPrehled.jsp (4 components)
│   ├── AssetCompaniesPage.tsx
│   ├── AssetParticipationPage.tsx
│   ├── NewParticipationModal.tsx
│   └── EditParticipationModal.tsx
│
├── overview/               # MajetekPrehledFiltr.jsp (2 components)
│   ├── AssetOverviewPage.tsx
│   └── AssetOverviewFilters.tsx
│
└── control/               # MajetekKontrola.jsp (2 components)
    ├── AssetControlPage.tsx
    └── ControlRuleModal.tsx
```

### Migration Ratio:

- **Legacy**: 5 JSP files (monolithic, mixed concerns)
- **React**: 13 focused components (separation of concerns)
- **Ratio**: 1 JSP → average 2.6 React components
- **Benefit**: Better testability, reusability, maintainability

---

## Key Takeaways

1. **Inline Editing Grid**: Not a simple table - requires complex state management
2. **13 Components**: Not 3-4, but 13 distinct specialized components
3. **Temporal Data**: Valid from/to dates everywhere
4. **Calculations**: Auto-calculations, percentages, consolidation
5. **Role-Based**: Different views per user permissions
6. **Oracle Procedures**: Must call procedures, not just CRUD
7. **Batch Updates**: Emissions support I/U/D tracking for batch operations
8. **Separation of Concerns**: Each component has single, focused responsibility

---

This specification is based on actual JSP source code analysis, not assumptions.
