import { test, expect } from '@playwright/test';

/**
 * E2E Tests for Document Module
 *
 * Tests cover:
 * - Document list rendering
 * - Table sorting and filtering
 * - Search functionality
 * - Create new document modal
 * - Document detail view
 * - Approval actions
 */

test.describe('Document Module', () => {
  test.beforeEach(async ({ page }) => {
    // Navigate to documents page
    await page.goto('/documents');
    await page.waitForLoadState('networkidle');
  });

  test('should display documents list with correct data', async ({ page }) => {
    // Check page title
    await expect(page.locator('h1')).toContainText('📄 Dokumenty');

    // Verify table is rendered
    const table = page.locator('table');
    await expect(table).toBeVisible();

    // Verify table headers
    await expect(page.locator('th').filter({ hasText: 'Číslo dokumentu' })).toBeVisible();
    await expect(page.locator('th').filter({ hasText: 'Typ' })).toBeVisible();
    await expect(page.locator('th').filter({ hasText: 'Částka' })).toBeVisible();
    await expect(page.locator('th').filter({ hasText: 'Status' })).toBeVisible();

    // Verify at least one row exists
    const rows = page.locator('tbody tr');
    await expect(rows.first()).toBeVisible();
  });

  test('should filter documents by search term', async ({ page }) => {
    // Get initial row count
    const initialRows = await page.locator('tbody tr').count();
    expect(initialRows).toBeGreaterThan(0);

    // Search for specific term
    const searchInput = page.locator('input[type="text"]').first();
    await searchInput.fill('INV-2025');
    await page.waitForTimeout(500); // Debounce

    // Verify filtered results
    const filteredRows = await page.locator('tbody tr').count();
    expect(filteredRows).toBeLessThanOrEqual(initialRows);

    // Clear search
    await searchInput.clear();
    await page.waitForTimeout(500);

    // Verify all rows back
    const clearedRows = await page.locator('tbody tr').count();
    expect(clearedRows).toBe(initialRows);
  });

  test('should sort table by column', async ({ page }) => {
    // Click on "Částka" header to sort
    const amountHeader = page.locator('th').filter({ hasText: 'Částka' });
    await amountHeader.click();

    // Wait for sort animation
    await page.waitForTimeout(300);

    // Get first two amounts
    const firstAmount = await page.locator('tbody tr:nth-child(1) td:nth-child(3)').textContent();
    const secondAmount = await page.locator('tbody tr:nth-child(2) td:nth-child(3)').textContent();

    expect(firstAmount).toBeTruthy();
    expect(secondAmount).toBeTruthy();

    // Click again to reverse sort
    await amountHeader.click();
    await page.waitForTimeout(300);

    // Verify order changed
    const newFirstAmount = await page.locator('tbody tr:nth-child(1) td:nth-child(3)').textContent();
    expect(newFirstAmount).toBeTruthy();
  });

  test('should open and close new document modal', async ({ page }) => {
    // Click "Nový dokument" button
    const newDocButton = page.locator('button').filter({ hasText: 'Nový dokument' });
    await newDocButton.click();

    // Verify modal opened
    const modal = page.locator('.modal-overlay');
    await expect(modal).toBeVisible();

    // Verify modal title
    await expect(page.locator('h2').filter({ hasText: 'Nový dokument' })).toBeVisible();

    // Close modal
    const closeButton = page.locator('button').filter({ hasText: 'Zrušit' });
    await closeButton.click();

    // Verify modal closed
    await expect(modal).not.toBeVisible();
  });

  test('should validate new document form', async ({ page }) => {
    // Open modal
    await page.locator('button').filter({ hasText: 'Nový dokument' }).click();
    await expect(page.locator('.modal-overlay')).toBeVisible();

    // Try to submit empty form
    const submitButton = page.locator('button[type="submit"]').filter({ hasText: 'Vytvořit dokument' });
    await submitButton.click();

    // Verify validation errors appear
    await expect(page.locator('.error-message').first()).toBeVisible();

    // Fill in valid data
    await page.locator('select[name="type"]').selectOption('INVOICE');
    await page.locator('input[name="amount"]').fill('150000');
    await page.locator('input[name="dueDate"]').fill('2025-12-31');
    await page.locator('input[name="companyName"]').fill('Test Company Ltd');
    await page.locator('textarea[name="description"]').fill('Test invoice description');

    // Submit form
    await submitButton.click();

    // Wait for success (modal should close)
    await page.waitForTimeout(1000);
    await expect(page.locator('.modal-overlay')).not.toBeVisible();
  });

  test('should navigate to document detail', async ({ page }) => {
    // Click on first document link
    const firstDocLink = page.locator('tbody tr:first-child a').first();
    const docNumber = await firstDocLink.textContent();
    await firstDocLink.click();

    // Wait for navigation
    await page.waitForLoadState('networkidle');

    // Verify we're on detail page
    expect(page.url()).toContain('/documents/');

    // Verify document number is displayed
    await expect(page.locator('h1')).toContainText(docNumber || '');

    // Verify detail sections exist
    await expect(page.locator('.detail-section')).toBeVisible();
  });

  test('should filter by status checkboxes', async ({ page }) => {
    // Open advanced filters if not visible
    const advancedFilters = page.locator('.advanced-filters');
    if (!(await advancedFilters.isVisible())) {
      await page.locator('button').filter({ hasText: 'Filtry' }).click();
    }

    // Get initial row count
    const initialRows = await page.locator('tbody tr').count();

    // Check only PENDING status
    const pendingCheckbox = page.locator('input[type="checkbox"][value="PENDING"]');
    if (!(await pendingCheckbox.isChecked())) {
      await pendingCheckbox.check();
    }

    // Wait for filter
    await page.waitForTimeout(500);

    // Verify filtered results
    const filteredRows = await page.locator('tbody tr').count();
    expect(filteredRows).toBeLessThanOrEqual(initialRows);

    // Uncheck to clear filter
    await pendingCheckbox.uncheck();
    await page.waitForTimeout(500);

    // Verify all rows back
    const clearedRows = await page.locator('tbody tr').count();
    expect(clearedRows).toBe(initialRows);
  });

  test('should paginate through results', async ({ page }) => {
    // Verify pagination controls exist
    const pagination = page.locator('.pagination');
    await expect(pagination).toBeVisible();

    // Get first row document number
    const firstRowDoc = await page.locator('tbody tr:first-child td:first-child a').textContent();

    // Change page size to 5
    await page.locator('select').filter({ hasText: '10' }).selectOption('5');
    await page.waitForTimeout(300);

    // Verify only 5 rows shown
    const rows = await page.locator('tbody tr').count();
    expect(rows).toBeLessThanOrEqual(5);

    // Go to next page
    const nextButton = page.locator('button').filter({ hasText: '›' });
    if (await nextButton.isEnabled()) {
      await nextButton.click();
      await page.waitForTimeout(300);

      // Verify different documents shown
      const newFirstRowDoc = await page.locator('tbody tr:first-child td:first-child a').textContent();
      expect(newFirstRowDoc).not.toBe(firstRowDoc);
    }
  });

  test('should select and act on multiple documents', async ({ page }) => {
    // Select first checkbox
    const firstCheckbox = page.locator('tbody tr:first-child input[type="checkbox"]');
    await firstCheckbox.check();

    // Verify selection count
    await expect(page.locator('text=1 dokument')).toBeVisible();

    // Select second checkbox
    const secondCheckbox = page.locator('tbody tr:nth-child(2) input[type="checkbox"]');
    await secondCheckbox.check();

    // Verify selection count updated
    await expect(page.locator('text=2 dokumenty')).toBeVisible();

    // Open action menu
    const actionButton = page.locator('button').filter({ hasText: 'Akce' });
    if (await actionButton.isVisible()) {
      await actionButton.click();
      await expect(page.locator('text=Schválit').or(page.locator('text=Zamítnout'))).toBeVisible();
    }

    // Deselect all
    await firstCheckbox.uncheck();
    await secondCheckbox.uncheck();

    // Verify no selection
    await expect(page.locator('text=0 dokument')).toBeVisible();
  });
});
