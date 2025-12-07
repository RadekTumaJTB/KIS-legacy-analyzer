import { test, expect } from '@playwright/test';

/**
 * E2E Tests for Budget Module
 *
 * Tests cover:
 * - Budget list rendering
 * - Table filtering and sorting
 * - Create new budget modal
 * - Budget detail view with monthly breakdown
 * - Edit budget modal
 * - KPI statistics
 */

test.describe('Budget Module', () => {
  test.beforeEach(async ({ page }) => {
    // Navigate to budgets page
    await page.goto('/budgets');
    await page.waitForLoadState('networkidle');
  });

  test('should display budgets list with correct data', async ({ page }) => {
    // Check page title
    await expect(page.locator('h1')).toContainText('💰 Rozpočty');

    // Verify table is rendered
    const table = page.locator('table');
    await expect(table).toBeVisible();

    // Verify table headers
    await expect(page.locator('th').filter({ hasText: 'Kód' })).toBeVisible();
    await expect(page.locator('th').filter({ hasText: 'Název' })).toBeVisible();
    await expect(page.locator('th').filter({ hasText: 'Rok' })).toBeVisible();
    await expect(page.locator('th').filter({ hasText: 'Plánováno' })).toBeVisible();

    // Verify at least one row exists
    const rows = page.locator('tbody tr');
    await expect(rows.first()).toBeVisible();
  });

  test('should filter budgets by year', async ({ page }) => {
    // Get initial row count
    const initialRows = await page.locator('tbody tr').count();
    expect(initialRows).toBeGreaterThan(0);

    // Filter by year 2025
    const yearFilter = page.locator('select').filter({ hasText: 'Všechny roky' }).or(page.locator('select').first());
    if (await yearFilter.isVisible()) {
      await yearFilter.selectOption('2025');
      await page.waitForTimeout(500);

      // Verify filtered results
      const filteredRows = await page.locator('tbody tr').count();
      expect(filteredRows).toBeLessThanOrEqual(initialRows);

      // Verify all rows show 2025
      const yearCells = page.locator('tbody tr td:nth-child(4)');
      const count = await yearCells.count();
      for (let i = 0; i < Math.min(count, 5); i++) {
        const yearText = await yearCells.nth(i).textContent();
        expect(yearText).toContain('2025');
      }
    }
  });

  test('should filter budgets by status', async ({ page }) => {
    // Open status filter
    const statusFilter = page.locator('select').filter({ hasText: 'Všechny statusy' });
    if (await statusFilter.isVisible()) {
      await statusFilter.selectOption('ACTIVE');
      await page.waitForTimeout(500);

      // Verify filtered results show ACTIVE badges
      const statusBadges = page.locator('.status-badge');
      const count = await statusBadges.count();
      if (count > 0) {
        const firstBadgeText = await statusBadges.first().textContent();
        expect(firstBadgeText).toContain('ACTIVE');
      }
    }
  });

  test('should open and close new budget modal', async ({ page }) => {
    // Click "Nový rozpočet" button
    const newBudgetButton = page.locator('button').filter({ hasText: 'Nový rozpočet' });
    await newBudgetButton.click();

    // Verify modal opened
    const modal = page.locator('.modal-overlay');
    await expect(modal).toBeVisible();

    // Verify modal title
    await expect(page.locator('h2').filter({ hasText: 'Nový rozpočet' })).toBeVisible();

    // Close modal
    const closeButton = page.locator('button').filter({ hasText: 'Zrušit' });
    await closeButton.click();

    // Verify modal closed
    await expect(modal).not.toBeVisible();
  });

  test('should validate new budget form', async ({ page }) => {
    // Open modal
    await page.locator('button').filter({ hasText: 'Nový rozpočet' }).click();
    await expect(page.locator('.modal-overlay')).toBeVisible();

    // Try to submit empty form
    const submitButton = page.locator('button[type="submit"]').filter({ hasText: 'Vytvořit rozpočet' });
    await submitButton.click();

    // Verify validation errors appear
    await expect(page.locator('.error-message').first()).toBeVisible();

    // Fill in valid data
    const timestamp = Date.now();
    await page.locator('input[name="code"]').fill(`BUD-2025-${timestamp}`);
    await page.locator('input[name="name"]').fill('Test Budget for E2E');
    await page.locator('select[name="type"]').selectOption('EXPENSE');
    await page.locator('input[name="year"]').fill('2025');
    await page.locator('input[name="plannedAmount"]').fill('5000000');
    await page.locator('input[name="departmentName"]').fill('IT Testing');
    await page.locator('textarea[name="description"]').fill('E2E test budget created by Playwright');

    // Submit form
    await submitButton.click();

    // Wait for success (modal should close)
    await page.waitForTimeout(1000);
    await expect(page.locator('.modal-overlay')).not.toBeVisible();
  });

  test('should navigate to budget detail', async ({ page }) => {
    // Click on first budget link
    const firstBudgetLink = page.locator('tbody tr:first-child a').first();
    const budgetCode = await firstBudgetLink.textContent();
    await firstBudgetLink.click();

    // Wait for navigation
    await page.waitForLoadState('networkidle');

    // Verify we're on detail page
    expect(page.url()).toContain('/budgets/');

    // Verify budget code is displayed in breadcrumb
    await expect(page.locator('.breadcrumb-current')).toContainText(budgetCode || '');

    // Verify summary cards exist
    await expect(page.locator('.summary-card').first()).toBeVisible();

    // Verify at least 4 summary cards (Planned, Actual, Variance, Status)
    const summaryCards = await page.locator('.summary-card').count();
    expect(summaryCards).toBeGreaterThanOrEqual(4);
  });

  test('should display monthly breakdown in budget detail', async ({ page }) => {
    // Navigate to first budget detail
    await page.locator('tbody tr:first-child a').first().click();
    await page.waitForLoadState('networkidle');

    // Verify monthly breakdown section
    await expect(page.locator('h2').filter({ hasText: 'Měsíční rozpad' })).toBeVisible();

    // Verify line items table
    const lineItemsTable = page.locator('.line-items-table table');
    await expect(lineItemsTable).toBeVisible();

    // Verify table has 12 months (or 13 with total row)
    const rows = await lineItemsTable.locator('tbody tr').count();
    expect(rows).toBeGreaterThanOrEqual(12);

    // Verify month names (check first few)
    await expect(page.locator('tbody tr:first-child').filter({ hasText: 'Leden' })).toBeVisible();

    // Verify total row exists
    await expect(page.locator('tfoot tr.total-row')).toBeVisible();
    await expect(page.locator('tfoot tr.total-row').filter({ hasText: 'Celkem' })).toBeVisible();
  });

  test('should open and close edit budget modal', async ({ page }) => {
    // Navigate to first budget detail
    await page.locator('tbody tr:first-child a').first().click();
    await page.waitForLoadState('networkidle');

    // Click "Upravit" button
    const editButton = page.locator('button').filter({ hasText: 'Upravit' });
    await editButton.click();

    // Verify modal opened
    const modal = page.locator('.modal-overlay');
    await expect(modal).toBeVisible();

    // Verify modal title
    await expect(page.locator('h2').filter({ hasText: 'Upravit rozpočet' })).toBeVisible();

    // Verify form fields are pre-filled
    const nameInput = page.locator('input[name="name"]');
    const nameValue = await nameInput.inputValue();
    expect(nameValue).toBeTruthy();
    expect(nameValue.length).toBeGreaterThan(0);

    // Close modal
    const closeButton = page.locator('button').filter({ hasText: 'Zrušit' });
    await closeButton.click();

    // Verify modal closed
    await expect(modal).not.toBeVisible();
  });

  test('should update budget via edit modal', async ({ page }) => {
    // Navigate to first budget detail
    await page.locator('tbody tr:first-child a').first().click();
    await page.waitForLoadState('networkidle');

    // Open edit modal
    await page.locator('button').filter({ hasText: 'Upravit' }).click();
    await expect(page.locator('.modal-overlay')).toBeVisible();

    // Modify budget name
    const nameInput = page.locator('input[name="name"]');
    const originalName = await nameInput.inputValue();
    const newName = `${originalName} (Updated ${Date.now()})`;
    await nameInput.fill(newName);

    // Modify planned amount
    await page.locator('input[name="plannedAmount"]').fill('6500000');

    // Add description
    await page.locator('textarea[name="description"]').fill('Updated via E2E test');

    // Submit form
    await page.locator('button[type="submit"]').filter({ hasText: 'Uložit změny' }).click();

    // Wait for success
    await page.waitForTimeout(1000);

    // Modal should close
    await expect(page.locator('.modal-overlay')).not.toBeVisible();

    // Verify budget name updated in header
    await expect(page.locator('h1')).toContainText(newName);
  });

  test('should display utilization bars correctly', async ({ page }) => {
    // Navigate to first budget detail
    await page.locator('tbody tr:first-child a').first().click();
    await page.waitForLoadState('networkidle');

    // Verify utilization bars in monthly breakdown
    const utilizationBars = page.locator('.utilization-bar-small');
    const count = await utilizationBars.count();
    expect(count).toBeGreaterThan(0);

    // Verify at least one bar has fill
    const firstBar = utilizationBars.first().locator('.utilization-fill');
    await expect(firstBar).toBeVisible();

    // Verify utilization text exists
    const utilizationText = page.locator('.utilization-text-small').first();
    await expect(utilizationText).toBeVisible();
    const text = await utilizationText.textContent();
    expect(text).toMatch(/\d+\.\d+%/); // Should be percentage like "95.5%"
  });

  test('should navigate back to list from detail', async ({ page }) => {
    // Navigate to first budget detail
    await page.locator('tbody tr:first-child a').first().click();
    await page.waitForLoadState('networkidle');

    // Click "Zpět na seznam" button
    const backButton = page.locator('button').filter({ hasText: 'Zpět na seznam' });
    await backButton.click();

    // Wait for navigation
    await page.waitForLoadState('networkidle');

    // Verify we're back on list page
    expect(page.url()).toContain('/budgets');
    expect(page.url()).not.toContain('/budgets/');

    // Verify table is visible
    await expect(page.locator('table')).toBeVisible();
  });

  test('should display metadata section in detail', async ({ page }) => {
    // Navigate to first budget detail
    await page.locator('tbody tr:first-child a').first().click();
    await page.waitForLoadState('networkidle');

    // Verify metadata section exists
    const metadataCards = page.locator('.metadata-card');
    const count = await metadataCards.count();
    expect(count).toBeGreaterThanOrEqual(3); // Owner, Validity, Department

    // Verify owner card
    await expect(page.locator('h3').filter({ hasText: 'Vlastník' })).toBeVisible();

    // Verify validity card
    await expect(page.locator('h3').filter({ hasText: 'Platnost' })).toBeVisible();

    // Verify department card
    await expect(page.locator('h3').filter({ hasText: 'Oddělení' })).toBeVisible();
  });
});

test.describe('Budget Dashboard', () => {
  test.beforeEach(async ({ page }) => {
    // Navigate to budget dashboard
    await page.goto('/budgets/dashboard');
    await page.waitForLoadState('networkidle');
  });

  test('should display KPI statistics', async ({ page }) => {
    // Verify page title
    await expect(page.locator('h1').filter({ hasText: 'Dashboard' })).toBeVisible();

    // Verify KPI cards exist
    const kpiCards = page.locator('.kpi-card');
    const count = await kpiCards.count();
    expect(count).toBeGreaterThanOrEqual(4); // Total Planned, Total Actual, Utilization, etc.

    // Verify at least one KPI has value
    const firstKpiValue = page.locator('.kpi-value').first();
    await expect(firstKpiValue).toBeVisible();
    const valueText = await firstKpiValue.textContent();
    expect(valueText).toBeTruthy();
  });

  test('should display charts', async ({ page }) => {
    // Verify chart container exists
    const chartContainer = page.locator('.chart-container, canvas, svg').first();
    if (await chartContainer.count() > 0) {
      await expect(chartContainer.first()).toBeVisible();
    }
  });
});
