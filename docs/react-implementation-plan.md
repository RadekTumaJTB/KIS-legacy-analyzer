# KIS React Frontend - Implementation Plan & Progress

**Date:** December 5, 2025
**Status:** Ready for Implementation
**Version:** 1.0

---

## Executive Summary

Complete React frontend migration plan based on:
- ✅ Oracle ADF Analysis (24 modules, 1,045 files analyzed)
- ✅ UX Research Report (4 personas, 3 journey maps)
- ✅ Frontend Design Specification (50+ pages, production-ready)
- ✅ BFF Architecture (Backend For Frontend layer designed)

**Project initialized:**
- Location: `/Users/radektuma/DEV/KIS/kis-frontend/`
- Framework: Vite + React 18 + TypeScript
- Base dependencies: Installed ✅

---

## Implementation Status

### Phase 1: Foundation & Setup (Week 1-2)

**Completed:**
- [x] Project initialization (Vite + React + TypeScript)
- [x] Base npm dependencies installed

**In Progress:**
- [ ] Tailwind CSS configuration
- [ ] shadcn/ui setup

**Pending:**
- [ ] Redux Toolkit + React Query
- [ ] React Router v6
- [ ] Axios/Fetch wrapper
- [ ] Environment configuration
- [ ] ESLint + Prettier
- [ ] Git setup

### Phase 2: Core Components (Week 3-4)

**Pending:**
- [ ] Design System Implementation
  - [ ] Color palette (Tailwind config)
  - [ ] Typography scale
  - [ ] Spacing system
  - [ ] Component tokens

- [ ] Layout Components
  - [ ] Sidebar Navigation
  - [ ] Header
  - [ ] Main Layout
  - [ ] Mobile Navigation

- [ ] Common Components
  - [ ] DataTable (TanStack Table)
  - [ ] FilterPanel
  - [ ] DateRangePicker
  - [ ] ExcelExportWizard
  - [ ] LoadingSpinner
  - [ ] Toast Notifications

### Phase 3: Document Module (Week 5-6)

**Priority 1 - Martin's workflow (100+ docs/day):**
- [ ] Document List
  - [ ] TanStack Table integration
  - [ ] Filters & Search
  - [ ] Bulk actions
  - [ ] Infinite scroll
  - [ ] Sort & pagination

- [ ] Document Detail
  - [ ] Form with React Hook Form
  - [ ] Inline editing
  - [ ] Auto-save (every 10s)
  - [ ] Line items grid

- [ ] Approval Timeline
  - [ ] Visual stepper
  - [ ] Status badges
  - [ ] Comment thread
  - [ ] Approval actions

- [ ] Bulk Actions
  - [ ] Select all/none
  - [ ] Approve selected
  - [ ] Reject selected
  - [ ] Archive selected

### Phase 4: Budget Module (Week 7-8)

**Priority 2 - Eva's workflow (6-8h/day):**
- [ ] Budget Dashboard
  - [ ] KPI cards
  - [ ] Budget utilization charts (Recharts)
  - [ ] Alert notifications
  - [ ] Quick actions

- [ ] Budget List
  - [ ] Filterable table
  - [ ] Status indicators
  - [ ] Export button

- [ ] Budget Line Items
  - [ ] Editable grid (AG-Grid)
  - [ ] Inline editing
  - [ ] Validation
  - [ ] Auto-calculation

- [ ] Budget Comparison
  - [ ] Actual vs Planned chart
  - [ ] Variance analysis
  - [ ] Drill-down

### Phase 5: Consolidation Module (Week 9-10)

**Priority 3 - Eva + Tomáš workflow:**
- [ ] Consolidation List
- [ ] Visual Formula Builder
  - [ ] Autocomplete
  - [ ] Syntax highlighting
  - [ ] Validation

- [ ] Company Grouping
  - [ ] Tree view
  - [ ] Drag & drop
  - [ ] Multi-select

- [ ] Consolidation Results
  - [ ] Pivot table
  - [ ] Export

### Phase 6: Excel Export & Reports (Week 11-12)

**Critical for all users:**
- [ ] Excel Export Wizard
  - [ ] Step 1: Select columns
  - [ ] Step 2: Apply filters
  - [ ] Step 3: Preview data
  - [ ] Step 4: Download

- [ ] Report Library
  - [ ] Predefined reports
  - [ ] Custom report builder
  - [ ] Schedule reports

### Phase 7: Mobile Optimization (Week 13-14)

**Critical for Petra (Manager approvals):**
- [ ] Bottom tab navigation
- [ ] Touch-friendly buttons
- [ ] Mobile approval flow
- [ ] Offline capability (PWA)

### Phase 8: Testing & Polish (Week 15-16)

- [ ] Unit tests (Vitest)
- [ ] Integration tests
- [ ] E2E tests (Playwright)
- [ ] Accessibility audit
- [ ] Performance optimization
- [ ] Production build

---

## Technical Stack - Confirmed

### Core Framework
```json
{
  "react": "^18.2.0",
  "typescript": "^5.0.0",
  "vite": "^5.0.0"
}
```

### Styling
```json
{
  "tailwindcss": "^3.4.0",
  "@tailwindcss/forms": "^0.5.7",
  "@tailwindcss/typography": "^0.5.10"
}
```

### UI Components (shadcn/ui)
```json
{
  "@radix-ui/react-dialog": "^1.0.5",
  "@radix-ui/react-dropdown-menu": "^2.0.6",
  "@radix-ui/react-select": "^2.0.0",
  "@radix-ui/react-toast": "^1.1.5",
  "class-variance-authority": "^0.7.0",
  "clsx": "^2.1.0",
  "tailwind-merge": "^2.2.0"
}
```

### State Management
```json
{
  "@reduxjs/toolkit": "^2.0.0",
  "react-redux": "^9.0.0",
  "@tanstack/react-query": "^5.17.0"
}
```

### Routing
```json
{
  "react-router-dom": "^6.21.0"
}
```

### Forms
```json
{
  "react-hook-form": "^7.49.0",
  "@hookform/resolvers": "^3.3.0",
  "zod": "^3.22.0"
}
```

### Data Tables
```json
{
  "@tanstack/react-table": "^8.11.0",
  "ag-grid-react": "^31.0.0",
  "ag-grid-community": "^31.0.0"
}
```

### Charts & Visualization
```json
{
  "recharts": "^2.10.0",
  "date-fns": "^3.0.0"
}
```

### API & HTTP
```json
{
  "axios": "^1.6.0"
}
```

### Utilities
```json
{
  "lodash": "^4.17.21",
  "uuid": "^9.0.1"
}
```

### Development & Testing
```json
{
  "vitest": "^1.1.0",
  "@testing-library/react": "^14.1.0",
  "@testing-library/jest-dom": "^6.1.0",
  "playwright": "^1.40.0",
  "eslint": "^8.56.0",
  "prettier": "^3.1.0"
}
```

---

## Project Structure

```
kis-frontend/
├── public/
│   └── logo.svg
│
├── src/
│   ├── assets/
│   │   ├── images/
│   │   └── fonts/
│   │
│   ├── components/
│   │   ├── budget/
│   │   │   ├── BudgetList.tsx
│   │   │   ├── BudgetDashboard.tsx
│   │   │   ├── BudgetLineItems.tsx
│   │   │   ├── BudgetApproval.tsx
│   │   │   └── BudgetComparison.tsx
│   │   │
│   │   ├── documents/
│   │   │   ├── DocumentList.tsx
│   │   │   ├── DocumentDetail.tsx
│   │   │   ├── ApprovalTimeline.tsx
│   │   │   ├── BulkActionsBar.tsx
│   │   │   └── DocumentLinking.tsx
│   │   │
│   │   ├── consolidation/
│   │   │   ├── ConsolidationList.tsx
│   │   │   ├── FormulaBuilder.tsx
│   │   │   ├── CompanyGrouping.tsx
│   │   │   └── ConsolidationResults.tsx
│   │   │
│   │   ├── common/
│   │   │   ├── DataTable.tsx
│   │   │   ├── FilterPanel.tsx
│   │   │   ├── ExcelExportWizard.tsx
│   │   │   ├── DateRangePicker.tsx
│   │   │   ├── CurrencyInput.tsx
│   │   │   ├── LoadingSpinner.tsx
│   │   │   └── Toast.tsx
│   │   │
│   │   ├── layout/
│   │   │   ├── Sidebar.tsx
│   │   │   ├── Header.tsx
│   │   │   ├── MainLayout.tsx
│   │   │   └── MobileNav.tsx
│   │   │
│   │   └── ui/ (shadcn/ui components)
│   │       ├── button.tsx
│   │       ├── dialog.tsx
│   │       ├── dropdown-menu.tsx
│   │       ├── input.tsx
│   │       ├── select.tsx
│   │       └── toast.tsx
│   │
│   ├── pages/
│   │   ├── DashboardPage.tsx
│   │   ├── DocumentsPage.tsx
│   │   ├── BudgetPage.tsx
│   │   ├── ConsolidationPage.tsx
│   │   ├── ReportsPage.tsx
│   │   └── AdminPage.tsx
│   │
│   ├── services/
│   │   ├── api/
│   │   │   ├── client.ts          # Axios instance
│   │   │   ├── budgetService.ts
│   │   │   ├── documentService.ts
│   │   │   ├── consolidationService.ts
│   │   │   └── exportService.ts
│   │   │
│   │   └── hooks/
│   │       ├── useBudgetData.ts
│   │       ├── useDocuments.ts
│   │       ├── useApprovalFlow.ts
│   │       └── useExport.ts
│   │
│   ├── store/
│   │   ├── index.ts               # Redux store config
│   │   ├── slices/
│   │   │   ├── budgetSlice.ts
│   │   │   ├── documentSlice.ts
│   │   │   ├── consolidationSlice.ts
│   │   │   └── uiSlice.ts
│   │   └── api/
│   │       └── kisApi.ts          # RTK Query API
│   │
│   ├── types/
│   │   ├── budget.ts
│   │   ├── document.ts
│   │   ├── consolidation.ts
│   │   ├── user.ts
│   │   └── common.ts
│   │
│   ├── utils/
│   │   ├── formatters.ts          # Currency, date formatters
│   │   ├── validators.ts
│   │   ├── dateHelpers.ts
│   │   └── constants.ts
│   │
│   ├── lib/
│   │   └── utils.ts               # shadcn/ui utils (cn function)
│   │
│   ├── App.tsx
│   ├── main.tsx
│   ├── index.css                  # Tailwind imports
│   └── vite-env.d.ts
│
├── tests/
│   ├── unit/
│   ├── integration/
│   └── e2e/
│
├── .env.development
├── .env.production
├── .eslintrc.json
├── .prettierrc
├── components.json               # shadcn/ui config
├── tailwind.config.js
├── postcss.config.js
├── tsconfig.json
├── vite.config.ts
└── package.json
```

---

## Environment Configuration

### `.env.development`
```env
VITE_API_URL=http://localhost:8081
VITE_BFF_URL=http://localhost:8081/bff
VITE_ENABLE_MOCK=false
VITE_LOG_LEVEL=debug
```

### `.env.production`
```env
VITE_API_URL=https://api.jtbank.cz
VITE_BFF_URL=https://api.jtbank.cz/bff
VITE_ENABLE_MOCK=false
VITE_LOG_LEVEL=error
```

---

## Tailwind Configuration

### `tailwind.config.js`
```javascript
/** @type {import('tailwindcss').Config} */
export default {
  darkMode: ["class"],
  content: [
    './pages/**/*.{ts,tsx}',
    './components/**/*.{ts,tsx}',
    './app/**/*.{ts,tsx}',
    './src/**/*.{ts,tsx}',
  ],
  prefix: "",
  theme: {
    container: {
      center: true,
      padding: "2rem",
      screens: {
        "2xl": "1400px",
      },
    },
    extend: {
      colors: {
        // Banking Blue Palette
        primary: {
          50: '#eff6ff',
          100: '#dbeafe',
          200: '#bfdbfe',
          300: '#93c5fd',
          400: '#60a5fa',
          500: '#3b82f6',  // Primary blue
          600: '#2563eb',
          700: '#1d4ed8',
          800: '#1e40af',
          900: '#1e3a8a',
          950: '#172554',
        },
        // Semantic Colors
        success: {
          50: '#f0fdf4',
          500: '#22c55e',
          700: '#15803d',
        },
        warning: {
          50: '#fffbeb',
          500: '#f59e0b',
          700: '#b45309',
        },
        error: {
          50: '#fef2f2',
          500: '#ef4444',
          700: '#b91c1c',
        },
        // Neutral Grays (8-level)
        neutral: {
          50: '#fafafa',
          100: '#f4f4f5',
          200: '#e4e4e7',
          300: '#d4d4d8',
          400: '#a1a1aa',
          500: '#71717a',
          600: '#52525b',
          700: '#3f3f46',
          800: '#27272a',
          900: '#18181b',
          950: '#09090b',
        },
      },
      fontFamily: {
        sans: ['Inter', 'sans-serif'],
        mono: ['JetBrains Mono', 'monospace'],
      },
      fontSize: {
        // Major Third Scale (1.250)
        'xs': '0.800rem',    // 12.8px
        'sm': '0.875rem',    // 14px
        'base': '1rem',      // 16px
        'lg': '1.125rem',    // 18px
        'xl': '1.250rem',    // 20px
        '2xl': '1.563rem',   // 25px
        '3xl': '1.953rem',   // 31.2px
        '4xl': '2.441rem',   // 39px
        '5xl': '3.052rem',   // 48.8px
      },
      spacing: {
        // 8px base unit
        '1': '0.25rem',  // 4px
        '2': '0.5rem',   // 8px
        '3': '0.75rem',  // 12px
        '4': '1rem',     // 16px
        '6': '1.5rem',   // 24px
        '8': '2rem',     // 32px
        '12': '3rem',    // 48px
        '16': '4rem',    // 64px
      },
      borderRadius: {
        sm: '4px',
        DEFAULT: '8px',
        md: '8px',
        lg: '16px',
        full: '9999px',
      },
      boxShadow: {
        sm: '0 1px 2px 0 rgba(0, 0, 0, 0.05)',
        DEFAULT: '0 4px 6px -1px rgba(0, 0, 0, 0.1)',
        lg: '0 10px 15px -3px rgba(0, 0, 0, 0.1)',
      },
      transitionDuration: {
        fast: '150ms',
        DEFAULT: '300ms',
      },
      keyframes: {
        "accordion-down": {
          from: { height: "0" },
          to: { height: "var(--radix-accordion-content-height)" },
        },
        "accordion-up": {
          from: { height: "var(--radix-accordion-content-height)" },
          to: { height: "0" },
        },
      },
      animation: {
        "accordion-down": "accordion-down 0.2s ease-out",
        "accordion-up": "accordion-up 0.2s ease-out",
      },
    },
  },
  plugins: [
    require("tailwindcss-animate"),
    require("@tailwindcss/forms"),
    require("@tailwindcss/typography"),
  ],
}
```

---

## Next Steps - Immediate Actions

### 1. Complete Dependency Installation (30 minutes)

```bash
cd /Users/radektuma/DEV/KIS/kis-frontend

# Install all dependencies
npm install \
  react-router-dom \
  @reduxjs/toolkit react-redux \
  @tanstack/react-query \
  @tanstack/react-table \
  react-hook-form @hookform/resolvers zod \
  axios \
  recharts \
  date-fns \
  lodash uuid \
  clsx tailwind-merge class-variance-authority \
  lucide-react

# Install Radix UI components (shadcn/ui base)
npm install \
  @radix-ui/react-dialog \
  @radix-ui/react-dropdown-menu \
  @radix-ui/react-select \
  @radix-ui/react-toast \
  @radix-ui/react-tabs \
  @radix-ui/react-accordion

# Install dev dependencies
npm install -D \
  vitest \
  @testing-library/react \
  @testing-library/jest-dom \
  @testing-library/user-event \
  playwright \
  eslint-plugin-react-hooks \
  eslint-plugin-react-refresh \
  prettier \
  prettier-plugin-tailwindcss
```

### 2. Setup shadcn/ui (10 minutes)

```bash
npx shadcn@latest init
# Select:
# - TypeScript: Yes
# - Style: Default
# - Base color: Blue
# - Global CSS: src/index.css
# - CSS variables: Yes
# - Tailwind config: tailwind.config.js
# - Components: src/components/ui
# - Utils: src/lib/utils.ts
```

### 3. Add shadcn/ui Components (20 minutes)

```bash
npx shadcn@latest add button
npx shadcn@latest add dialog
npx shadcn@latest add dropdown-menu
npx shadcn@latest add input
npx shadcn@latest add select
npx shadcn@latest add toast
npx shadcn@latest add table
npx shadcn@latest add tabs
npx shadcn@latest add card
npx shadcn@latest add badge
```

### 4. Configure TypeScript Paths (5 minutes)

Update `tsconfig.json`:
```json
{
  "compilerOptions": {
    "baseUrl": ".",
    "paths": {
      "@/*": ["./src/*"],
      "@/components/*": ["./src/components/*"],
      "@/lib/*": ["./src/lib/*"],
      "@/utils/*": ["./src/utils/*"]
    }
  }
}
```

Update `vite.config.ts`:
```typescript
import path from "path"
import react from "@vitejs/plugin-react"
import { defineConfig } from "vite"

export default defineConfig({
  plugins: [react()],
  resolve: {
    alias: {
      "@": path.resolve(__dirname, "./src"),
    },
  },
})
```

### 5. Create Basic Layout (1 hour)

Implement:
- `src/components/layout/Sidebar.tsx`
- `src/components/layout/Header.tsx`
- `src/components/layout/MainLayout.tsx`
- `src/App.tsx` (routing)

### 6. Test Development Server (5 minutes)

```bash
npm run dev
```

Open http://localhost:5173

---

## Success Criteria

### Sprint 1-2 (Foundation)
- [ ] All dependencies installed
- [ ] Tailwind CSS working
- [ ] shadcn/ui components available
- [ ] Routing configured
- [ ] Basic layout renders
- [ ] Dev server runs without errors

### Sprint 3-4 (Document Module)
- [ ] Document list loads from BFF API
- [ ] Filters work
- [ ] Bulk actions functional
- [ ] Detail page editable
- [ ] Approval timeline shows correctly

### Sprint 5-6 (Budget Module)
- [ ] Dashboard KPIs render
- [ ] Charts display data
- [ ] Line items editable
- [ ] Comparison view works

### Sprint 7+ (Remaining Modules)
- [ ] Consolidation formula builder
- [ ] Excel export wizard
- [ ] Mobile responsive
- [ ] All tests passing

---

## Documentation References

- **UX Research:** `/Users/radektuma/DEV/KIS/docs/ux-research-report.md`
- **Design Spec:** `/Users/radektuma/DEV/KIS/docs/frontend-design-spec.md`
- **BFF Architecture:** `/Users/radektuma/DEV/KIS/docs/bff-architecture.md`
- **Oracle ADF Analysis:** (via Explore agent output)

---

## Contact & Support

**Project Owner:** KIS Development Team
**Last Updated:** December 5, 2025
**Status:** Implementation Ready - Awaiting Developer Assignment

**Next Action:** Assign frontend developer(s) to begin Sprint 1 implementation.
