/**
 * E2E Tests for Assets Module - Participations
 *
 * Tests for AssetCompaniesPage, AssetParticipationPage, and related modals
 * Covers role-based access, CRUD operations, and dual currency support
 */

import { test, expect } from '@playwright/test';

test.describe('Assets - Companies Selection', () => {
  test.beforeEach(async ({ page }) => {
    // Navigate to companies page
    await page.goto('http://localhost:5173/assets/companies');
    await page.waitForLoadState('networkidle');
  });

  test('should display companies selection page', async ({ page }) => {
    // Check page title
    await expect(page.getByRole('heading', { name: /Správa Majetkových Účastí/i })).toBeVisible();

    // Check for description
    await expect(page.getByText(/Výběr společnosti pro správu majetkových účastí/i)).toBeVisible();
  });

  test('should load and display companies with permissions', async ({ page }) => {
    // Mock API call
    await page.route('**/bff/assets/companies', async (route) => {
      await route.fulfill({
        status: 200,
        contentType: 'application/json',
        body: JSON.stringify([
          { id: 1, name: 'J&T BANKA, a.s.', ico: '47115378', canView: true, canEdit: true },
          { id: 2, name: 'J&T INVESTIČNÍ SPOLEČNOST, a.s.', ico: '12345678', canView: true, canEdit: false }
        ])
      });
    });

    await page.reload();
    await page.waitForLoadState('networkidle');

    // Check if companies are displayed
    await expect(page.getByText('J&T BANKA, a.s.')).toBeVisible();
    await expect(page.getByText('J&T INVESTIČNÍ SPOLEČNOST, a.s.')).toBeVisible();

    // Check permission badges
    await expect(page.getByText('Zobrazit').first()).toBeVisible();
    await expect(page.getByText('Upravit').first()).toBeVisible();
  });

  test('should display statistics correctly', async ({ page }) => {
    // Mock API call with specific permissions
    await page.route('**/bff/assets/companies', async (route) => {
      await route.fulfill({
        status: 200,
        contentType: 'application/json',
        body: JSON.stringify([
          { id: 1, name: 'Company A', ico: '111', canView: true, canEdit: true },
          { id: 2, name: 'Company B', ico: '222', canView: true, canEdit: true },
          { id: 3, name: 'Company C', ico: '333', canView: true, canEdit: false }
        ])
      });
    });

    await page.reload();
    await page.waitForLoadState('networkidle');

    // Check statistics
    await expect(page.getByText(/Celkem společností:/)).toBeVisible();
    await expect(page.getByText('3', { exact: true })).toBeVisible(); // Total
    await expect(page.getByText('2', { exact: true })).toBeVisible(); // With edit rights
  });

  test('should filter companies by search term', async ({ page }) => {
    // Mock API call
    await page.route('**/bff/assets/companies', async (route) => {
      await route.fulfill({
        status: 200,
        contentType: 'application/json',
        body: JSON.stringify([
          { id: 1, name: 'J&T BANKA', ico: '47115378', canView: true, canEdit: true },
          { id: 2, name: 'ČSOB', ico: '12345678', canView: true, canEdit: true }
        ])
      });
    });

    await page.reload();
    await page.waitForLoadState('networkidle');

    // Type in search
    const searchInput = page.getByPlaceholder(/Hledat podle názvu nebo IČO/i);
    await searchInput.fill('J&T');

    // Check filtered results
    await expect(page.getByText('J&T BANKA')).toBeVisible();
    await expect(page.getByText('ČSOB')).not.toBeVisible();
  });

  test('should navigate to participation page on company click', async ({ page }) => {
    // Mock API call
    await page.route('**/bff/assets/companies', async (route) => {
      await route.fulfill({
        status: 200,
        contentType: 'application/json',
        body: JSON.stringify([
          { id: 1, name: 'J&T BANKA', ico: '47115378', canView: true, canEdit: true }
        ])
      });
    });

    await page.reload();
    await page.waitForLoadState('networkidle');

    // Click on "Otevřít" button
    await page.getByRole('button', { name: /Otevřít/i }).click();

    // Check navigation
    await page.waitForURL('**/assets/participations/**');
    expect(page.url()).toContain('/assets/participations/');
  });
});

test.describe('Assets - Participations CRUD', () => {
  test.beforeEach(async ({ page }) => {
    // Navigate to participations page for company 1
    await page.goto('http://localhost:5173/assets/participations/1');
    await page.waitForLoadState('networkidle');
  });

  test('should display participations list page', async ({ page }) => {
    // Check page title
    await expect(page.getByRole('heading', { name: /Majetkové Účasti/i })).toBeVisible();

    // Check for back button
    await expect(page.getByRole('button', { name: /Zpět na výběr společnosti/i })).toBeVisible();

    // Check for filters
    await expect(page.getByText(/Vyhledávání/i)).toBeVisible();
    await expect(page.getByText(/Stav k datu/i)).toBeVisible();
  });

  test('should load and display equity stakes', async ({ page }) => {
    // Mock API call
    await page.route('**/bff/assets/companies/1/participations', async (route) => {
      await route.fulfill({
        status: 200,
        contentType: 'application/json',
        body: JSON.stringify([
          {
            id: 1,
            emissionId: 1,
            accountingCompanyId: 1,
            accountingCompanyName: 'J&T BANKA',
            accountNumber: '0611234567',
            validFrom: '2024-01-01',
            validTo: null,
            transactionTypeId: 1,
            transactionTypeName: 'Nákup',
            methodId: 1,
            methodName: 'Přímá účast',
            numberOfShares: 1000,
            transactionCurrency: 'EUR',
            pricePerShareTransaction: 50,
            totalTransactionAmount: 50000,
            exchangeRate: 25,
            accountingCurrency: 'CZK',
            pricePerShareAccounting: 1250,
            totalAccountingAmount: 1250000,
            ignoreFlag: false,
            lastModified: '2024-12-01T10:00:00',
            modifiedByUser: 'test.user'
          }
        ])
      });
    });

    await page.reload();
    await page.waitForLoadState('networkidle');

    // Check if stake is displayed
    await expect(page.getByText('0611234567')).toBeVisible();
    await expect(page.getByText('Nákup')).toBeVisible();
    await expect(page.getByText('Přímá účast')).toBeVisible();

    // Check dual currency display
    await expect(page.getByText(/50 000,00 EUR/)).toBeVisible();
    await expect(page.getByText(/1 250 000,00 CZK/)).toBeVisible();
  });

  test('should filter by active only status', async ({ page }) => {
    // Mock API with active and inactive stakes
    await page.route('**/bff/assets/companies/1/participations', async (route) => {
      await route.fulfill({
        status: 200,
        contentType: 'application/json',
        body: JSON.stringify([
          {
            id: 1,
            accountNumber: '0611111111',
            validFrom: '2024-01-01',
            validTo: null, // Active
            transactionTypeName: 'Nákup',
            methodName: 'Přímá účast',
            numberOfShares: 1000,
            transactionCurrency: 'CZK',
            pricePerShareTransaction: 100,
            totalTransactionAmount: 100000,
            exchangeRate: 1,
            accountingCurrency: 'CZK',
            pricePerShareAccounting: 100,
            totalAccountingAmount: 100000
          },
          {
            id: 2,
            accountNumber: '0622222222',
            validFrom: '2023-01-01',
            validTo: '2023-12-31', // Inactive
            transactionTypeName: 'Prodej',
            methodName: 'Nepřímá účast',
            numberOfShares: 500,
            transactionCurrency: 'CZK',
            pricePerShareTransaction: 100,
            totalTransactionAmount: 50000,
            exchangeRate: 1,
            accountingCurrency: 'CZK',
            pricePerShareAccounting: 100,
            totalAccountingAmount: 50000
          }
        ])
      });
    });

    await page.reload();
    await page.waitForLoadState('networkidle');

    // Initially "Pouze aktivní" should be checked
    const activeOnlyCheckbox = page.getByRole('checkbox', { name: /Pouze aktivní/i });
    await expect(activeOnlyCheckbox).toBeChecked();

    // Should show only active stake
    await expect(page.getByText('0611111111')).toBeVisible();
    await expect(page.getByText('0622222222')).not.toBeVisible();

    // Uncheck "Pouze aktivní"
    await activeOnlyCheckbox.uncheck();

    // Should now show both
    await expect(page.getByText('0611111111')).toBeVisible();
    await expect(page.getByText('0622222222')).toBeVisible();
  });

  test('should display totals footer', async ({ page }) => {
    // Mock API call
    await page.route('**/bff/assets/companies/1/participations', async (route) => {
      await route.fulfill({
        status: 200,
        contentType: 'application/json',
        body: JSON.stringify([
          {
            id: 1,
            accountNumber: '061111',
            numberOfShares: 1000,
            totalTransactionAmount: 50000,
            totalAccountingAmount: 1250000,
            transactionCurrency: 'EUR',
            accountingCurrency: 'CZK',
            validFrom: '2024-01-01',
            transactionTypeName: 'Nákup',
            methodName: 'Přímá',
            pricePerShareTransaction: 50,
            exchangeRate: 25,
            pricePerShareAccounting: 1250
          },
          {
            id: 2,
            accountNumber: '062222',
            numberOfShares: 500,
            totalTransactionAmount: 25000,
            totalAccountingAmount: 625000,
            transactionCurrency: 'EUR',
            accountingCurrency: 'CZK',
            validFrom: '2024-01-01',
            transactionTypeName: 'Nákup',
            methodName: 'Přímá',
            pricePerShareTransaction: 50,
            exchangeRate: 25,
            pricePerShareAccounting: 1250
          }
        ])
      });
    });

    await page.reload();
    await page.waitForLoadState('networkidle');

    // Check totals footer
    await expect(page.getByText('Celkem')).toBeVisible();

    // Total shares: 1000 + 500 = 1500
    const footer = page.locator('tfoot');
    await expect(footer.getByText('1 500')).toBeVisible();

    // Total transaction amount: 50000 + 25000 = 75000
    await expect(footer.getByText('75 000,00')).toBeVisible();

    // Total accounting amount: 1250000 + 625000 = 1875000
    await expect(footer.getByText('1 875 000,00')).toBeVisible();
  });

  test('should open new participation modal', async ({ page }) => {
    // Click "Nová účast" button
    await page.getByRole('button', { name: /Nová účast/i }).click();

    // Check modal is visible
    await expect(page.getByRole('heading', { name: /Nová Majetková Účast/i })).toBeVisible();

    // Check required fields
    await expect(page.getByText(/Emise \/ Finanční Investice \*/i)).toBeVisible();
    await expect(page.getByText(/Číslo Účtu \*/i)).toBeVisible();
    await expect(page.getByText(/Typ Transakce \*/i)).toBeVisible();
  });

  test('should open edit participation modal', async ({ page }) => {
    // Mock API call
    await page.route('**/bff/assets/companies/1/participations', async (route) => {
      await route.fulfill({
        status: 200,
        contentType: 'application/json',
        body: JSON.stringify([
          {
            id: 1,
            accountNumber: '0611234567',
            validFrom: '2024-01-01',
            numberOfShares: 1000,
            transactionTypeName: 'Nákup',
            methodName: 'Přímá účast',
            transactionCurrency: 'EUR',
            pricePerShareTransaction: 50,
            totalTransactionAmount: 50000,
            exchangeRate: 25,
            accountingCurrency: 'CZK',
            pricePerShareAccounting: 1250,
            totalAccountingAmount: 1250000,
            lastModified: '2024-12-01T10:00:00',
            modifiedByUser: 'test.user'
          }
        ])
      });
    });

    await page.reload();
    await page.waitForLoadState('networkidle');

    // Click "Upravit" button
    await page.getByRole('button', { name: /Upravit/i }).first().click();

    // Check modal is visible
    await expect(page.getByRole('heading', { name: /Upravit Majetkovou Účast/i })).toBeVisible();

    // Check audit information is displayed
    await expect(page.getByText(/Poslední úprava:/i)).toBeVisible();
    await expect(page.getByText(/Upravil:/i)).toBeVisible();
  });

  test('should delete participation stake', async ({ page }) => {
    // Mock API calls
    await page.route('**/bff/assets/companies/1/participations', async (route) => {
      await route.fulfill({
        status: 200,
        contentType: 'application/json',
        body: JSON.stringify([
          {
            id: 1,
            accountNumber: '0611234567',
            validFrom: '2024-01-01',
            numberOfShares: 1000,
            transactionTypeName: 'Nákup',
            methodName: 'Přímá účast',
            transactionCurrency: 'CZK',
            pricePerShareTransaction: 100,
            totalTransactionAmount: 100000,
            exchangeRate: 1,
            accountingCurrency: 'CZK',
            pricePerShareAccounting: 100,
            totalAccountingAmount: 100000
          }
        ])
      });
    });

    await page.reload();
    await page.waitForLoadState('networkidle');

    // Mock delete API
    await page.route('**/bff/assets/participations/1', async (route) => {
      if (route.request().method() === 'DELETE') {
        await route.fulfill({ status: 204 });
      }
    });

    // Setup dialog handler
    page.on('dialog', dialog => dialog.accept());

    // Click "Smazat" button
    await page.getByRole('button', { name: /Smazat/i }).first().click();

    // Check success message
    await expect(page.getByText(/byla úspěšně smazána/i)).toBeVisible({ timeout: 5000 });
  });
});

test.describe('Assets - New Participation Modal', () => {
  test.beforeEach(async ({ page }) => {
    await page.goto('http://localhost:5173/assets/participations/1');
    await page.waitForLoadState('networkidle');

    // Open modal
    await page.getByRole('button', { name: /Nová účast/i }).click();
  });

  test('should auto-calculate accounting amounts', async ({ page }) => {
    // Mock reference data
    await page.route('**/bff/reference/**', async (route) => {
      await route.fulfill({
        status: 200,
        contentType: 'application/json',
        body: JSON.stringify([
          { id: 1, name: 'Test', code: 'TST' }
        ])
      });
    });

    // Fill price per share transaction
    const priceInput = page.locator('input[type="number"]').filter({ hasText: /Cena za Akcii/ }).first();
    await priceInput.fill('100');

    // Fill exchange rate
    const rateInput = page.locator('input[type="number"]').filter({ hasText: /Kurz/ }).first();
    await rateInput.fill('25');

    // Check auto-calculated accounting price (100 × 25 = 2500)
    const accountingPriceField = page.getByText('2 500,00').first();
    await expect(accountingPriceField).toBeVisible();
  });

  test('should auto-calculate total amounts', async ({ page }) => {
    // Fill number of shares
    const sharesInput = page.getByLabel(/Počet Akcií/i);
    await sharesInput.fill('1000');

    // Fill price per share
    const priceInput = page.locator('input[type="number"]').nth(1); // Price per share field
    await priceInput.fill('50');

    // Check auto-calculated total (1000 × 50 = 50000)
    await expect(page.getByText('50 000,00')).toBeVisible();
  });

  test('should validate required fields on submit', async ({ page }) => {
    // Try to submit without filling required fields
    await page.getByRole('button', { name: /Vytvořit/i }).click();

    // Should show validation errors
    await expect(page.getByText(/Vyberte emisi/i)).toBeVisible();
  });
});
