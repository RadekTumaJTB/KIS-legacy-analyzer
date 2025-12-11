import { test, expect, type Page } from '@playwright/test';

/**
 * Comprehensive Analysis Test for Project Detail Page
 *
 * This test analyzes the project detail page at http://localhost:5173/projects/1
 * to understand its structure, functionality, and identify any issues.
 *
 * Test Steps:
 * 1. Navigate to the project detail page
 * 2. Take a screenshot of the page
 * 3. Capture all visible text content
 * 4. Check for error messages or console errors
 * 5. Attempt to click the "Upravit" (Edit) button
 * 6. Capture the edit modal/form if it opens
 * 7. Analyze available form fields
 * 8. Report any issues found
 */

interface AnalysisReport {
  pageLoaded: boolean;
  pageTitle: string;
  visibleSections: string[];
  textContent: string[];
  consoleErrors: string[];
  editButtonFound: boolean;
  editModalOpened: boolean;
  formFields: string[];
  issues: string[];
}

// Helper function to wait for project detail to load
async function waitForProjectDetailToLoad(page: Page) {
  try {
    await Promise.race([
      page.waitForSelector('.project-detail.loading', { state: 'detached', timeout: 10000 }),
      page.waitForSelector('.detail-header h1', { timeout: 10000 }),
      page.waitForSelector('h1', { timeout: 10000 }),
    ]);
    return true;
  } catch (error) {
    return false;
  }
}

test.describe('Project Detail Page Analysis', () => {
  let consoleErrors: string[] = [];
  let report: AnalysisReport;

  test.beforeEach(async ({ page }) => {
    // Reset report
    report = {
      pageLoaded: false,
      pageTitle: '',
      visibleSections: [],
      textContent: [],
      consoleErrors: [],
      editButtonFound: false,
      editModalOpened: false,
      formFields: [],
      issues: [],
    };

    // Capture console errors
    consoleErrors = [];
    page.on('console', (msg) => {
      if (msg.type() === 'error') {
        consoleErrors.push(msg.text());
      }
    });

    // Capture page errors
    page.on('pageerror', (error) => {
      consoleErrors.push(`Page Error: ${error.message}`);
    });
  });

  test('should analyze project detail page comprehensively', async ({ page }) => {
    console.log('\n========================================');
    console.log('PROJECT DETAIL PAGE ANALYSIS REPORT');
    console.log('========================================\n');

    // Step 1: Navigate to the project detail page
    console.log('Step 1: Navigating to /projects/1...');
    try {
      await page.goto('/projects/1', { waitUntil: 'networkidle', timeout: 30000 });
      report.pageLoaded = true;
      console.log('✓ Page navigation successful');
    } catch (error) {
      report.pageLoaded = false;
      report.issues.push(`Navigation failed: ${error}`);
      console.log(`✗ Page navigation failed: ${error}`);
    }

    // Wait a moment for content to render
    await page.waitForTimeout(2000);

    // Step 2: Take a screenshot
    console.log('\nStep 2: Taking screenshot...');
    await page.screenshot({
      path: '/Users/radektuma/DEV/KIS/kis-frontend/test-results/project-detail-page.png',
      fullPage: true
    });
    console.log('✓ Screenshot saved to test-results/project-detail-page.png');

    // Step 3: Capture page title
    console.log('\nStep 3: Analyzing page structure...');
    try {
      const title = await page.title();
      report.pageTitle = title;
      console.log(`Page Title: "${title}"`);
    } catch (error) {
      report.issues.push(`Failed to get page title: ${error}`);
    }

    // Check for main heading
    try {
      const h1Elements = await page.locator('h1').all();
      for (const h1 of h1Elements) {
        const text = await h1.textContent();
        if (text) {
          console.log(`Main Heading: "${text.trim()}"`);
          report.visibleSections.push(`Heading: ${text.trim()}`);
        }
      }
    } catch (error) {
      report.issues.push(`Failed to get headings: ${error}`);
    }

    // Step 4: Check for error messages
    console.log('\nStep 4: Checking for error messages...');
    try {
      // Look for common error indicators
      const errorSelectors = [
        '.error',
        '.error-message',
        '[role="alert"]',
        '.alert-error',
        '.text-red-500',
        '.text-red-600',
        '.bg-red-100'
      ];

      for (const selector of errorSelectors) {
        const errorElements = await page.locator(selector).all();
        for (const element of errorElements) {
          const isVisible = await element.isVisible();
          if (isVisible) {
            const text = await element.textContent();
            if (text) {
              report.issues.push(`Error message found: ${text.trim()}`);
              console.log(`✗ Error message: "${text.trim()}"`);
            }
          }
        }
      }

      if (report.issues.length === 0) {
        console.log('✓ No error messages found on page');
      }
    } catch (error) {
      console.log(`Warning: Error check failed: ${error}`);
    }

    // Check console errors
    if (consoleErrors.length > 0) {
      report.consoleErrors = consoleErrors;
      console.log('\nConsole Errors Found:');
      consoleErrors.forEach((error, index) => {
        console.log(`  ${index + 1}. ${error}`);
      });
    } else {
      console.log('✓ No console errors detected');
    }

    // Step 5: Capture all visible text content
    console.log('\nStep 5: Capturing visible content...');
    try {
      const bodyText = await page.locator('body').textContent();
      if (bodyText) {
        const lines = bodyText.split('\n')
          .map(line => line.trim())
          .filter(line => line.length > 0);
        report.textContent = lines;
        console.log(`✓ Captured ${lines.length} lines of text content`);
      }
    } catch (error) {
      report.issues.push(`Failed to capture text content: ${error}`);
    }

    // Analyze visible sections
    console.log('\nVisible Sections:');
    try {
      const sections = await page.locator('section, .section, [class*="section"]').all();
      for (const section of sections) {
        const isVisible = await section.isVisible();
        if (isVisible) {
          const className = await section.getAttribute('class');
          console.log(`  - Section: ${className || 'unnamed'}`);
        }
      }
    } catch (error) {
      console.log(`  Warning: Section analysis failed: ${error}`);
    }

    // Analyze cards or detail containers
    console.log('\nDetail Cards/Containers:');
    try {
      const cards = await page.locator('.card, [class*="card"], .detail, [class*="detail"]').all();
      for (const card of cards) {
        const isVisible = await card.isVisible();
        if (isVisible) {
          const className = await card.getAttribute('class');
          console.log(`  - Card: ${className || 'unnamed'}`);
        }
      }
    } catch (error) {
      console.log(`  Warning: Card analysis failed: ${error}`);
    }

    // Step 6: Look for "Upravit" (Edit) button
    console.log('\nStep 6: Looking for Edit button...');
    try {
      // Try multiple ways to find the edit button
      const editButtonSelectors = [
        'button:has-text("Upravit")',
        'button:has-text("Edit")',
        '[aria-label*="Upravit"]',
        '[aria-label*="Edit"]',
        '.edit-button',
        'button.edit',
      ];

      let editButton = null;
      for (const selector of editButtonSelectors) {
        const buttons = await page.locator(selector).all();
        if (buttons.length > 0) {
          editButton = buttons[0];
          report.editButtonFound = true;
          console.log(`✓ Edit button found with selector: ${selector}`);
          break;
        }
      }

      if (!editButton) {
        // Try to find any button and list them
        console.log('\nAvailable buttons on page:');
        const allButtons = await page.locator('button').all();
        for (const button of allButtons) {
          const text = await button.textContent();
          const isVisible = await button.isVisible();
          if (isVisible && text) {
            console.log(`  - Button: "${text.trim()}"`);
          }
        }
        report.issues.push('Edit button not found');
        console.log('✗ Edit button not found');
      } else {
        // Step 7: Try to click the edit button
        console.log('\nStep 7: Attempting to click Edit button...');
        try {
          // Take screenshot before clicking
          await page.screenshot({
            path: '/Users/radektuma/DEV/KIS/kis-frontend/test-results/before-edit-click.png',
            fullPage: true
          });

          await editButton.click();
          console.log('✓ Edit button clicked');

          // Wait for modal to appear
          await page.waitForTimeout(1000);

          // Look for modal
          const modalSelectors = [
            '[role="dialog"]',
            '.modal',
            '[class*="modal"]',
            '.dialog',
            '[class*="dialog"]',
          ];

          let modalFound = false;
          for (const selector of modalSelectors) {
            const modals = await page.locator(selector).all();
            for (const modal of modals) {
              const isVisible = await modal.isVisible();
              if (isVisible) {
                report.editModalOpened = true;
                modalFound = true;
                console.log(`✓ Edit modal opened (selector: ${selector})`);

                // Take screenshot of modal
                await page.screenshot({
                  path: '/Users/radektuma/DEV/KIS/kis-frontend/test-results/edit-modal.png',
                  fullPage: true
                });
                console.log('✓ Modal screenshot saved');

                // Step 8: Analyze form fields
                console.log('\nStep 8: Analyzing form fields...');
                try {
                  // Get all input fields
                  const inputs = await modal.locator('input, textarea, select').all();
                  console.log(`\nFound ${inputs.length} form fields:`);

                  for (const input of inputs) {
                    const type = await input.getAttribute('type');
                    const name = await input.getAttribute('name');
                    const id = await input.getAttribute('id');
                    const placeholder = await input.getAttribute('placeholder');
                    const value = await input.inputValue().catch(() => '');

                    const fieldInfo = {
                      type: type || 'text',
                      name: name || '',
                      id: id || '',
                      placeholder: placeholder || '',
                      value: value || '',
                    };

                    report.formFields.push(JSON.stringify(fieldInfo));
                    console.log(`  - Field: ${fieldInfo.type} | name="${fieldInfo.name}" | id="${fieldInfo.id}" | placeholder="${fieldInfo.placeholder}"`);
                  }

                  // Get all labels
                  const labels = await modal.locator('label').all();
                  console.log(`\nFound ${labels.length} labels:`);
                  for (const label of labels) {
                    const text = await label.textContent();
                    const forAttr = await label.getAttribute('for');
                    if (text) {
                      console.log(`  - Label: "${text.trim()}" (for="${forAttr || 'none'}")`);
                    }
                  }

                  // Get all buttons in modal
                  const modalButtons = await modal.locator('button').all();
                  console.log(`\nFound ${modalButtons.length} buttons in modal:`);
                  for (const button of modalButtons) {
                    const text = await button.textContent();
                    if (text) {
                      console.log(`  - Button: "${text.trim()}"`);
                    }
                  }
                } catch (error) {
                  report.issues.push(`Failed to analyze form fields: ${error}`);
                  console.log(`✗ Failed to analyze form fields: ${error}`);
                }

                break;
              }
            }
            if (modalFound) break;
          }

          if (!modalFound) {
            report.issues.push('Edit modal did not open after clicking edit button');
            console.log('✗ Edit modal did not open');

            // Take screenshot anyway
            await page.screenshot({
              path: '/Users/radektuma/DEV/KIS/kis-frontend/test-results/after-edit-click.png',
              fullPage: true
            });
          }
        } catch (error) {
          report.issues.push(`Failed to click edit button: ${error}`);
          console.log(`✗ Failed to click edit button: ${error}`);
        }
      }
    } catch (error) {
      report.issues.push(`Failed to find edit button: ${error}`);
      console.log(`✗ Failed to find edit button: ${error}`);
    }

    // Final report summary
    console.log('\n========================================');
    console.log('ANALYSIS SUMMARY');
    console.log('========================================\n');
    console.log(`Page Loaded: ${report.pageLoaded ? '✓ YES' : '✗ NO'}`);
    console.log(`Page Title: ${report.pageTitle}`);
    console.log(`Edit Button Found: ${report.editButtonFound ? '✓ YES' : '✗ NO'}`);
    console.log(`Edit Modal Opened: ${report.editModalOpened ? '✓ YES' : '✗ NO'}`);
    console.log(`Form Fields Found: ${report.formFields.length}`);
    console.log(`Console Errors: ${report.consoleErrors.length}`);
    console.log(`Issues Found: ${report.issues.length}`);

    if (report.issues.length > 0) {
      console.log('\nIssues:');
      report.issues.forEach((issue, index) => {
        console.log(`  ${index + 1}. ${issue}`);
      });
    }

    console.log('\n========================================\n');

    // Save report to file
    const reportPath = '/Users/radektuma/DEV/KIS/kis-frontend/test-results/project-detail-analysis-report.json';
    await page.evaluate((reportData) => {
      return reportData;
    }, report).then(async () => {
      const fs = require('fs');
      fs.writeFileSync(reportPath, JSON.stringify(report, null, 2));
      console.log(`Report saved to: ${reportPath}`);
    }).catch(() => {
      console.log('Note: Report could not be saved to file (not critical)');
    });

    // Assert that the page loaded successfully
    expect(report.pageLoaded).toBe(true);
  });
});
