/**
 * E2E Tests for Assets Module - Overview and Controls
 *
 * Tests for AssetOverviewPage and AssetControlPage
 * Covers calculations, filtering, and control rule management
 */

import { test, expect } from '@playwright/test';

test.describe('Assets - Overview Dashboard', () => {
  test.beforeEach(async ({ page }) => {
    // Navigate to overview page
    await page.goto('http://localhost:5173/assets/overview');
    await page.waitForLoadState('networkidle');
  });

  test('should display overview dashboard', async ({ page }) => {
    // Check page title
    await expect(page.getByRole('heading', { name: /Přehled Majetkových Účastí/i })).toBeVisible();

    // Check for description
    await expect(page.getByText(/Dashboard s výpočty vlastnických podílů/i)).toBeVisible();

    // Check for refresh button
    await expect(page.getByRole('button', { name: /Obnovit/i })).toBeVisible();
  });

  test('should display summary cards', async ({ page }) => {
    // Mock API call
    await page.route('**/bff/assets/overview**', async (route) => {
      await route.fulfill({
        status: 200,
        contentType: 'application/json',
        body: JSON.stringify([
          {
            emissionId: 1,
            companyName: 'Test Company',
            isinCode: 'CZ0001234567',
            totalEmissionShares: 10000,
            sharesOwned: 2500,
            ownershipPercentage: 25,
            marketValue: 1000000,
            bookValue: 800000,
            unrealizedGainLoss: 200000,
            currency: 'CZK',
            accountingCompanyName: 'J&T BANKA'
          }
        ])
      });
    });

    await page.reload();
    await page.waitForLoadState('networkidle');

    // Check summary cards
    await expect(page.getByText(/Celkový počet účastí/i)).toBeVisible();
    await expect(page.getByText(/Tržní hodnota/i)).toBeVisible();
    await expect(page.getByText(/Účetní hodnota/i)).toBeVisible();
    await expect(page.getByText(/Nerealizovaný zisk\/ztráta/i)).toBeVisible();

    // Check values
    await expect(page.getByText('1').and(page.locator('.text-2xl'))).toBeVisible(); // Count
    await expect(page.getByText('1 000 000').and(page.locator('.text-blue-600'))).toBeVisible(); // Market value
    await expect(page.getByText('800 000').and(page.locator('.text-gray-600'))).toBeVisible(); // Book value
    await expect(page.getByText('+200 000').and(page.locator('.text-green-600'))).toBeVisible(); // Gain
  });

  test('should display overview table with calculations', async ({ page }) => {
    // Mock API call
    await page.route('**/bff/assets/overview**', async (route) => {
      await route.fulfill({
        status: 200,
        contentType: 'application/json',
        body: JSON.stringify([
          {
            emissionId: 1,
            companyName: 'Test Company A',
            isinCode: 'CZ0001111111',
            totalEmissionShares: 10000,
            sharesOwned: 2500,
            ownershipPercentage: 25.00,
            marketValue: 1000000,
            bookValue: 800000,
            unrealizedGainLoss: 200000,
            currency: 'CZK',
            accountingCompanyName: 'J&T BANKA'
          },
          {
            emissionId: 2,
            companyName: 'Test Company B',
            isinCode: 'CZ0002222222',
            totalEmissionShares: 5000,
            sharesOwned: 1000,
            ownershipPercentage: 20.00,
            marketValue: 500000,
            bookValue: 600000,
            unrealizedGainLoss: -100000,
            currency: 'CZK',
            accountingCompanyName: 'J&T BANKA'
          }
        ])
      });
    });

    await page.reload();
    await page.waitForLoadState('networkidle');

    // Check table headers
    await expect(page.getByText('Společnost').and(page.locator('th'))).toBeVisible();
    await expect(page.getByText('ISIN').and(page.locator('th'))).toBeVisible();
    await expect(page.getByText('Vlastnický %').and(page.locator('th'))).toBeVisible();

    // Check company names
    await expect(page.getByText('Test Company A')).toBeVisible();
    await expect(page.getByText('Test Company B')).toBeVisible();

    // Check ownership percentages
    await expect(page.getByText('25,00 %')).toBeVisible();
    await expect(page.getByText('20,00 %')).toBeVisible();

    // Check positive gain (green)
    const gainRow = page.locator('tr:has-text("Test Company A")');
    await expect(gainRow.locator('.text-green-600')).toBeVisible();

    // Check negative loss (red)
    const lossRow = page.locator('tr:has-text("Test Company B")');
    await expect(lossRow.locator('.text-red-600')).toBeVisible();
  });

  test('should display totals footer', async ({ page }) => {
    // Mock API call
    await page.route('**/bff/assets/overview**', async (route) => {
      await route.fulfill({
        status: 200,
        contentType: 'application/json',
        body: JSON.stringify([
          {
            emissionId: 1,
            sharesOwned: 2500,
            marketValue: 1000000,
            bookValue: 800000,
            unrealizedGainLoss: 200000,
            companyName: 'A',
            isinCode: 'CZ1',
            totalEmissionShares: 10000,
            ownershipPercentage: 25,
            currency: 'CZK',
            accountingCompanyName: 'Test'
          },
          {
            emissionId: 2,
            sharesOwned: 1500,
            marketValue: 500000,
            bookValue: 400000,
            unrealizedGainLoss: 100000,
            companyName: 'B',
            isinCode: 'CZ2',
            totalEmissionShares: 5000,
            ownershipPercentage: 30,
            currency: 'CZK',
            accountingCompanyName: 'Test'
          }
        ])
      });
    });

    await page.reload();
    await page.waitForLoadState('networkidle');

    // Check totals footer
    const footer = page.locator('tfoot');
    await expect(footer.getByText('Celkem')).toBeVisible();

    // Total shares: 2500 + 1500 = 4000
    await expect(footer.getByText('4 000')).toBeVisible();

    // Total market value: 1000000 + 500000 = 1500000
    await expect(footer.getByText('1 500 000,00')).toBeVisible();

    // Total book value: 800000 + 400000 = 1200000
    await expect(footer.getByText('1 200 000,00')).toBeVisible();

    // Total unrealized gain/loss: 200000 + 100000 = 300000
    await expect(footer.getByText('+300 000,00').and(page.locator('.text-green-600'))).toBeVisible();
  });

  test('should apply date filter', async ({ page }) => {
    // Mock API call
    await page.route('**/bff/assets/overview**', async (route) => {
      const url = new URL(route.request().url());
      const asOfDate = url.searchParams.get('asOfDate');

      // Return different data based on date
      const data = asOfDate === '2024-06-01'
        ? [{ emissionId: 1, companyName: 'Historical Company', sharesOwned: 1000 }]
        : [{ emissionId: 2, companyName: 'Current Company', sharesOwned: 2000 }];

      await route.fulfill({
        status: 200,
        contentType: 'application/json',
        body: JSON.stringify(data.map(d => ({
          ...d,
          isinCode: 'CZ123',
          totalEmissionShares: 10000,
          ownershipPercentage: 10,
          marketValue: 100000,
          bookValue: 100000,
          unrealizedGainLoss: 0,
          currency: 'CZK',
          accountingCompanyName: 'Test'
        })))
      });
    });

    await page.reload();
    await page.waitForLoadState('networkidle');

    // Change date filter
    const dateInput = page.locator('input[type="date"]').first();
    await dateInput.fill('2024-06-01');

    // Click "Použít" button
    await page.getByRole('button', { name: /Použít/i }).click();

    // Wait for API call with new date
    await page.waitForLoadState('networkidle');

    // Check active filters display
    await expect(page.getByText(/Stav k: 1. 6. 2024/i)).toBeVisible();
  });

  test('should reset filters', async ({ page }) => {
    // Change date filter first
    const dateInput = page.locator('input[type="date"]').first();
    await dateInput.fill('2024-06-01');
    await page.getByRole('button', { name: /Použít/i }).click();

    // Click "Reset" button
    await page.getByRole('button', { name: /Reset/i }).click();

    // Check that date is reset to today
    const today = new Date().toISOString().split('T')[0];
    await expect(dateInput).toHaveValue(today);
  });

  test('should display legend', async ({ page }) => {
    // Mock API with data
    await page.route('**/bff/assets/overview**', async (route) => {
      await route.fulfill({
        status: 200,
        contentType: 'application/json',
        body: JSON.stringify([
          {
            emissionId: 1,
            companyName: 'Test',
            isinCode: 'CZ123',
            sharesOwned: 1000,
            totalEmissionShares: 10000,
            ownershipPercentage: 10,
            marketValue: 100000,
            bookValue: 100000,
            unrealizedGainLoss: 0,
            currency: 'CZK',
            accountingCompanyName: 'Test'
          }
        ])
      });
    });

    await page.reload();
    await page.waitForLoadState('networkidle');

    // Check legend
    await expect(page.getByText(/Legenda:/i)).toBeVisible();
    await expect(page.getByText(/Nerealizovaný zisk \(kladná hodnota\)/i)).toBeVisible();
    await expect(page.getByText(/Nerealizovaná ztráta \(záporná hodnota\)/i)).toBeVisible();
  });
});

test.describe('Assets - Control Rules', () => {
  test.beforeEach(async ({ page }) => {
    // Navigate to controls page
    await page.goto('http://localhost:5173/assets/controls');
    await page.waitForLoadState('networkidle');
  });

  test('should display control rules page', async ({ page }) => {
    // Check page title
    await expect(page.getByRole('heading', { name: /Kontrolní Pravidla Majetkových Účastí/i })).toBeVisible();

    // Check for description
    await expect(page.getByText(/Správa pravidel pro validaci účtů/i)).toBeVisible();

    // Check for new rule button
    await expect(page.getByRole('button', { name: /Nové pravidlo/i })).toBeVisible();
  });

  test('should display info panel', async ({ page }) => {
    // Check info panel
    await expect(page.getByText(/Jak fungují kontrolní pravidla:/i)).toBeVisible();
    await expect(page.getByText(/Account Pattern:/i)).toBeVisible();
    await expect(page.getByText(/Equity Stake Type:/i)).toBeVisible();
  });

  test('should load and display control rules', async ({ page }) => {
    // Mock API call
    await page.route('**/bff/assets/controls', async (route) => {
      await route.fulfill({
        status: 200,
        contentType: 'application/json',
        body: JSON.stringify([
          {
            id: 1,
            accountPattern: '061*',
            equityStakeTypeId: 1,
            equityStakeTypeName: 'Přímá účast',
            isActive: true,
            description: 'Pravidlo pro účty 061',
            validationMessage: 'Účet musí začínat 061'
          },
          {
            id: 2,
            accountPattern: '062*',
            equityStakeTypeId: 2,
            equityStakeTypeName: 'Nepřímá účast',
            isActive: false,
            description: 'Pravidlo pro účty 062',
            validationMessage: null
          }
        ])
      });
    });

    await page.reload();
    await page.waitForLoadState('networkidle');

    // Check statistics
    await expect(page.getByText(/Celkem pravidel/i)).toBeVisible();
    await expect(page.getByText('2').and(page.locator('.text-2xl'))).toBeVisible();

    // Check active rules
    await expect(page.getByText(/Aktivní Pravidla \(1\)/i)).toBeVisible();
    await expect(page.getByText('061*').and(page.locator('.font-mono'))).toBeVisible();
    await expect(page.getByText('Přímá účast')).toBeVisible();

    // Check inactive rules
    await expect(page.getByText(/Neaktivní Pravidla \(1\)/i)).toBeVisible();
    await expect(page.getByText('062*').and(page.locator('.font-mono'))).toBeVisible();
  });

  test('should open new rule modal', async ({ page }) => {
    // Click "Nové pravidlo" button
    await page.getByRole('button', { name: /Nové pravidlo/i }).click();

    // Check modal is visible
    await expect(page.getByRole('heading', { name: /Nové Kontrolní Pravidlo/i })).toBeVisible();

    // Check required fields
    await expect(page.getByText(/Account Pattern \*/i)).toBeVisible();
    await expect(page.getByText(/Typ Majetkové Účasti \*/i)).toBeVisible();

    // Check pattern examples
    await expect(page.getByText(/Pattern Examples:/i)).toBeVisible();
    await expect(page.getByText(/061\*/)).toBeVisible();
  });

  test('should edit existing rule', async ({ page }) => {
    // Mock API call
    await page.route('**/bff/assets/controls', async (route) => {
      await route.fulfill({
        status: 200,
        contentType: 'application/json',
        body: JSON.stringify([
          {
            id: 1,
            accountPattern: '061*',
            equityStakeTypeId: 1,
            equityStakeTypeName: 'Přímá účast',
            isActive: true,
            description: 'Test description',
            validationMessage: 'Test message'
          }
        ])
      });
    });

    await page.reload();
    await page.waitForLoadState('networkidle');

    // Click "Upravit" button
    await page.getByRole('button', { name: /Upravit/i }).first().click();

    // Check edit modal is visible
    await expect(page.getByRole('heading', { name: /Upravit Kontrolní Pravidlo/i })).toBeVisible();

    // Check ID is displayed
    await expect(page.getByText(/ID: 1/i)).toBeVisible();
  });

  test('should delete control rule', async ({ page }) => {
    // Mock API calls
    await page.route('**/bff/assets/controls', async (route) => {
      await route.fulfill({
        status: 200,
        contentType: 'application/json',
        body: JSON.stringify([
          {
            id: 1,
            accountPattern: '061*',
            equityStakeTypeId: 1,
            equityStakeTypeName: 'Přímá účast',
            isActive: true
          }
        ])
      });
    });

    await page.reload();
    await page.waitForLoadState('networkidle');

    // Mock delete API
    await page.route('**/bff/assets/controls/1', async (route) => {
      if (route.request().method() === 'DELETE') {
        await route.fulfill({ status: 204 });
      }
    });

    // Setup dialog handler
    page.on('dialog', dialog => dialog.accept());

    // Click "Smazat" button
    await page.getByRole('button', { name: /Smazat/i }).first().click();

    // Check success message
    await expect(page.getByText(/bylo úspěšně smazáno/i)).toBeVisible({ timeout: 5000 });
  });
});

test.describe('Assets - Control Rule Modal', () => {
  test.beforeEach(async ({ page }) => {
    await page.goto('http://localhost:5173/assets/controls');
    await page.waitForLoadState('networkidle');

    // Open modal
    await page.getByRole('button', { name: /Nové pravidlo/i }).click();
  });

  test('should validate account pattern format', async ({ page }) => {
    // Try to enter invalid pattern
    const patternInput = page.locator('input[type="text"]').first();
    await patternInput.fill('ABC*'); // Invalid - contains letters

    // Try to submit
    await page.getByRole('button', { name: /Vytvořit/i }).click();

    // Should show validation error
    await expect(page.getByText(/může obsahovat pouze číslice a \*/i)).toBeVisible();
  });

  test('should validate required fields', async ({ page }) => {
    // Try to submit without filling required fields
    await page.getByRole('button', { name: /Vytvořit/i }).click();

    // Should show validation errors
    await expect(page.getByText(/Zadejte account pattern/i)).toBeVisible();
  });

  test('should toggle active status', async ({ page }) => {
    // Check "Aktivní pravidlo" checkbox
    const activeCheckbox = page.getByRole('checkbox', { name: /Aktivní pravidlo/i });

    // Should be checked by default
    await expect(activeCheckbox).toBeChecked();

    // Uncheck it
    await activeCheckbox.uncheck();
    await expect(activeCheckbox).not.toBeChecked();

    // Check again
    await activeCheckbox.check();
    await expect(activeCheckbox).toBeChecked();
  });
});
