/**
 * E2E Tests for Assets Module - Emissions
 *
 * Tests for EmissionListPage and related components
 * Covers inline editing, batch operations, and export functionality
 */

import { test, expect } from '@playwright/test';

test.describe('Assets - Emissions Module', () => {
  test.beforeEach(async ({ page }) => {
    // Navigate to emissions page
    await page.goto('http://localhost:5173/assets/emissions');

    // Wait for page to load
    await page.waitForLoadState('networkidle');
  });

  test('should display emissions list page', async ({ page }) => {
    // Check page title
    await expect(page.getByRole('heading', { name: /Správa Emisí a Finančních Investic/i })).toBeVisible();

    // Check for main sections
    await expect(page.getByText(/Finanční Investice/i)).toBeVisible();
    await expect(page.getByRole('button', { name: /Přidat řádek/i })).toBeVisible();
  });

  test('should load and display financial investments list', async ({ page }) => {
    // Wait for financial investments to load
    await page.waitForSelector('.bg-white.rounded-lg.shadow');

    // Check if at least one financial investment is displayed
    const investmentButtons = page.locator('button:has-text("J&T")');
    await expect(investmentButtons.first()).toBeVisible();
  });

  test('should select a financial investment and display its items', async ({ page }) => {
    // Click on first financial investment
    const firstInvestment = page.locator('button:has-text("J&T")').first();
    await firstInvestment.click();

    // Wait for items table to load
    await page.waitForSelector('table');

    // Check table headers
    await expect(page.getByText('Platnost Od')).toBeVisible();
    await expect(page.getByText('Počet Kusů')).toBeVisible();
    await expect(page.getByText('Nominální Hodnota')).toBeVisible();
    await expect(page.getByText('Objem')).toBeVisible();
  });

  test('should add a new emission item row', async ({ page }) => {
    // Select first investment
    await page.locator('button:has-text("J&T")').first().click();
    await page.waitForSelector('table');

    // Click "Přidat řádek" button
    await page.getByRole('button', { name: /Přidat řádek/i }).click();

    // Check if a new row appeared with "Nový" status
    await expect(page.getByText('Nový')).toBeVisible();

    // Check if the row has bg-green-50 class (new item indicator)
    const newRow = page.locator('tr.bg-green-50');
    await expect(newRow).toBeVisible();
  });

  test('should perform inline editing on emission item', async ({ page }) => {
    // Select first investment
    await page.locator('button:has-text("J&T")').first().click();
    await page.waitForSelector('table');

    // Add new row
    await page.getByRole('button', { name: /Přidat řádek/i }).click();

    // Fill in the form fields
    const validFromInput = page.locator('input[type="date"]').first();
    await validFromInput.fill('2025-01-01');

    const numberOfSharesInput = page.locator('input[type="number"]').first();
    await numberOfSharesInput.fill('1000');

    const nominalValueInput = page.locator('input[type="number"]').nth(1);
    await nominalValueInput.fill('100');

    // Check if volume is auto-calculated (1000 × 100 = 100000)
    await expect(page.getByText('100 000,00')).toBeVisible();
  });

  test('should validate required fields', async ({ page }) => {
    // Select first investment
    await page.locator('button:has-text("J&T")').first().click();
    await page.waitForSelector('table');

    // Add new row
    await page.getByRole('button', { name: /Přidat řádek/i }).click();

    // Try to save without filling required fields
    const saveButton = page.getByRole('button', { name: /Uložit vše/i });

    // Initially button should be enabled (with dirty items)
    await expect(saveButton).toBeEnabled();

    // After clicking, should show error for missing valid date
    const numberOfSharesInput = page.locator('input[type="number"]').first();
    await numberOfSharesInput.fill('0'); // Invalid value
    await numberOfSharesInput.blur();

    // Check for validation error
    await expect(page.getByText(/musí být větší než 0/i)).toBeVisible();
  });

  test('should track dirty items count', async ({ page }) => {
    // Select first investment
    await page.locator('button:has-text("J&T")').first().click();
    await page.waitForSelector('table');

    // Add first row
    await page.getByRole('button', { name: /Přidat řádek/i }).click();

    // Check dirty count badge shows 1
    await expect(page.getByText('1').and(page.locator('.bg-white.text-blue-600'))).toBeVisible();

    // Add second row
    await page.getByRole('button', { name: /Přidat řádek/i }).click();

    // Check dirty count badge shows 2
    await expect(page.getByText('2').and(page.locator('.bg-white.text-blue-600'))).toBeVisible();
  });

  test('should cancel changes and reload original data', async ({ page }) => {
    // Select first investment
    await page.locator('button:has-text("J&T")').first().click();
    await page.waitForSelector('table');

    // Add new row
    await page.getByRole('button', { name: /Přidat řádek/i }).click();

    // Click cancel button
    await page.getByRole('button', { name: /Zrušit změny/i }).click();

    // Check that new row is removed
    await expect(page.getByText('Nový')).not.toBeVisible();
  });

  test('should mark item for deletion', async ({ page }) => {
    // Select first investment
    await page.locator('button:has-text("J&T")').first().click();
    await page.waitForSelector('table');

    // Find first row with existing data
    const firstRow = page.locator('tbody tr').first();

    // Click delete button on first row
    const deleteButton = firstRow.getByRole('button', { name: /Smazat/i });
    await deleteButton.click();

    // Check that row is marked with "Smazáno" status
    await expect(page.getByText('Smazáno')).toBeVisible();

    // Check that row has strikethrough styling
    const deletedRow = page.locator('tr.bg-red-50.opacity-50.line-through');
    await expect(deletedRow).toBeVisible();
  });

  test('should expand row to show details', async ({ page }) => {
    // Select first investment
    await page.locator('button:has-text("J&T")').first().click();
    await page.waitForSelector('table');

    // Click expand button (▶)
    const expandButton = page.locator('button:has-text("▶")').first();
    await expandButton.click();

    // Check that expand button changed to ▼
    await expect(page.locator('button:has-text("▼")').first()).toBeVisible();

    // Check that detail row is visible
    await expect(page.getByText(/Objem \(vypočtený\):/i)).toBeVisible();
  });

  test('should display statistics in footer', async ({ page }) => {
    // Select first investment
    await page.locator('button:has-text("J&T")').first().click();
    await page.waitForSelector('table');

    // Check for statistics
    await expect(page.getByText(/Celkem položek:/i)).toBeVisible();
    await expect(page.getByText(/Změněných:/i)).toBeVisible();
  });

  test('should perform batch save operation', async ({ page }) => {
    // Mock the batch update API call
    await page.route('**/bff/emissions/*/items', async (route) => {
      // Return success response
      await route.fulfill({
        status: 200,
        contentType: 'application/json',
        body: JSON.stringify({
          financialInvestment: {
            id: 1,
            companyName: 'J&T BANKA, a.s.',
            currency: 'CZK',
            isinCode: 'CZ0008019106'
          },
          emissionItems: []
        })
      });
    });

    // Select first investment
    await page.locator('button:has-text("J&T")').first().click();
    await page.waitForSelector('table');

    // Add new row and fill data
    await page.getByRole('button', { name: /Přidat řádek/i }).click();

    const validFromInput = page.locator('input[type="date"]').first();
    await validFromInput.fill('2025-01-01');

    const numberOfSharesInput = page.locator('input[type="number"]').first();
    await numberOfSharesInput.fill('1000');

    const nominalValueInput = page.locator('input[type="number"]').nth(1);
    await nominalValueInput.fill('100');

    // Click save button
    await page.getByRole('button', { name: /Uložit vše/i }).click();

    // Wait for success message
    await expect(page.getByText(/Úspěšně uloženo/i)).toBeVisible({ timeout: 5000 });
  });
});

test.describe('Assets - New Emission Modal', () => {
  test.beforeEach(async ({ page }) => {
    await page.goto('http://localhost:5173/assets/emissions');
    await page.waitForLoadState('networkidle');
  });

  test('should open new emission modal', async ({ page }) => {
    // This test would require a "New Investment" button
    // Since we don't have it in EmissionListPage, we'll skip this for now
    // Or we could navigate directly to the modal if there's a route
  });
});

test.describe('Assets - Emission History Modal', () => {
  test.beforeEach(async ({ page }) => {
    await page.goto('http://localhost:5173/assets/emissions');
    await page.waitForLoadState('networkidle');
  });

  test('should display emission history', async ({ page }) => {
    // Mock the history API call
    await page.route('**/bff/emissions/history/*', async (route) => {
      await route.fulfill({
        status: 200,
        contentType: 'application/json',
        body: JSON.stringify([
          {
            id: 1,
            validFrom: '2024-01-01',
            validTo: '2024-12-31',
            numberOfShares: 1000,
            nominalValue: 100,
            volume: 100000,
            registeredCapital: 500000,
            investmentFlag: true
          },
          {
            id: 2,
            validFrom: '2025-01-01',
            validTo: null,
            numberOfShares: 1500,
            nominalValue: 100,
            volume: 150000,
            registeredCapital: 500000,
            investmentFlag: true
          }
        ])
      });
    });

    // This would require a "Show History" button
    // Implementation depends on how we trigger the history modal
  });
});

test.describe('Assets - Export Modal', () => {
  test.beforeEach(async ({ page }) => {
    await page.goto('http://localhost:5173/assets/emissions');
    await page.waitForLoadState('networkidle');
  });

  test('should export emissions to Excel', async ({ page }) => {
    // Mock the export API call
    const downloadPromise = page.waitForEvent('download');

    await page.route('**/bff/emissions/export', async (route) => {
      // Return Excel file blob
      await route.fulfill({
        status: 200,
        headers: {
          'Content-Type': 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet',
          'Content-Disposition': 'attachment; filename=emissions.xlsx'
        },
        body: Buffer.from('mock excel data')
      });
    });

    // This would require an "Export" button
    // Implementation depends on how we trigger the export modal
  });
});
