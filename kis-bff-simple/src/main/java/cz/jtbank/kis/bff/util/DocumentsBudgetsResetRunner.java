package cz.jtbank.kis.bff.util;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.Statement;

/**
 * Documents and Budgets Database Reset and Population Runner
 *
 * This runner performs a complete database reset for documents and budgets modules:
 * 1. Drops all existing documents and budgets tables
 * 2. Creates all 8 tables from scratch with proper constraints
 * 3. Populates all tables with realistic Czech test data
 *
 * IMPORTANT: Enable this component when you need to reset and populate documents/budgets data
 */
@Component  // ENABLED - Run to populate documents and budgets data
@Order(0)
public class DocumentsBudgetsResetRunner implements CommandLineRunner {

    private final JdbcTemplate jdbcTemplate;

    public DocumentsBudgetsResetRunner(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("============================================================================");
        System.out.println("KIS Documents & Budgets Module - COMPLETE DATABASE RESET");
        System.out.println("============================================================================");
        System.out.println("WARNING: This will DROP all documents and budgets tables and recreate them!");
        System.out.println("============================================================================");
        System.out.println("");

        try {
            // Read complete reset SQL file
            ClassPathResource resource = new ClassPathResource("db/migration/documents_budgets_reset_and_populate.sql");
            BufferedReader reader = new BufferedReader(
                new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8)
            );

            // Read entire file as string
            StringBuilder sqlContent = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                // Skip comment-only lines
                if (!line.trim().startsWith("--") && !line.trim().isEmpty()) {
                    sqlContent.append(line).append("\n");
                }
            }
            reader.close();

            // Execute entire SQL script using native JDBC (to handle Oracle-specific syntax)
            jdbcTemplate.execute((Connection connection) -> {
                Statement statement = connection.createStatement();

                // Split by semicolon and execute each statement
                String[] statements = sqlContent.toString().split(";");
                int successCount = 0;
                int errorCount = 0;

                for (String sql : statements) {
                    String trimmed = sql.trim();

                    if (trimmed.isEmpty()) {
                        continue;
                    }

                    try {
                        // Execute statement
                        statement.execute(trimmed);

                        // Log progress for CREATE TABLE and INSERT statements
                        if (trimmed.toUpperCase().startsWith("DROP TABLE")) {
                            String tableName = extractTableName(trimmed, "DROP TABLE");
                            if (tableName != null) {
                                System.out.println("  Dropped: " + tableName);
                            }
                        } else if (trimmed.toUpperCase().startsWith("CREATE TABLE")) {
                            String tableName = extractTableName(trimmed, "CREATE TABLE");
                            if (tableName != null) {
                                System.out.println("✓ Created table: " + tableName);
                            }
                        } else if (trimmed.toUpperCase().startsWith("INSERT INTO")) {
                            // Don't log every INSERT (too verbose)
                        } else if (trimmed.toUpperCase().startsWith("CREATE INDEX")) {
                            // Suppress index creation messages
                        }

                        successCount++;
                    } catch (Exception e) {
                        // Ignore DROP TABLE errors (table might not exist)
                        if (!trimmed.toUpperCase().startsWith("DROP TABLE")) {
                            System.err.println("✗ Error: " + trimmed.substring(0, Math.min(60, trimmed.length())) + "...");
                            System.err.println("  " + e.getMessage());
                            errorCount++;
                        }
                    }
                }

                System.out.println("");
                System.out.println("Execution Summary:");
                System.out.println("  Successful statements: " + successCount);
                System.out.println("  Failed statements: " + errorCount);

                statement.close();
                return null;
            });

            // Verify results
            System.out.println("");
            System.out.println("============================================================================");
            System.out.println("Verifying database state...");
            System.out.println("============================================================================");

            Long tableCount = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM user_tables WHERE table_name IN ('KP_DAT_DOKUMENT', 'KP_DAT_BUDGET', 'KP_DAT_BUDGETPOLOZKA', 'KP_CIS_DOKUMENT', 'KP_CIS_DOKUMENTSTATUS', 'KP_KTG_SPOLECNOST', 'KP_KTG_APPUSER', 'KP_KTG_ODBOR')", Long.class
            );
            System.out.println("Tables created: " + tableCount);

            Long dokumentCount = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM DB_JT.KP_DAT_DOKUMENT", Long.class
            );
            System.out.println("Documents inserted: " + dokumentCount);

            Long budgetCount = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM DB_JT.KP_DAT_BUDGET", Long.class
            );
            System.out.println("Budgets inserted: " + budgetCount);

            Long budgetPolozkaCount = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM DB_JT.KP_DAT_BUDGETPOLOZKA", Long.class
            );
            System.out.println("Budget line items inserted: " + budgetPolozkaCount);

            Long spolecnostCount = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM DB_JT.KP_KTG_SPOLECNOST", Long.class
            );
            System.out.println("Companies inserted: " + spolecnostCount);

            Long userCount = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM DB_JT.KP_KTG_APPUSER", Long.class
            );
            System.out.println("Users inserted: " + userCount);

            System.out.println("");
            System.out.println("============================================================================");
            System.out.println("✓ DOCUMENTS & BUDGETS DATABASE RESET AND POPULATION COMPLETED SUCCESSFULLY!");
            System.out.println("============================================================================");
            System.out.println("");
            System.out.println("IMPORTANT: Disable DocumentsBudgetsResetRunner by commenting out @Component");
            System.out.println("           annotation to prevent re-running on next startup.");
            System.out.println("");
            System.out.println("Next steps:");
            System.out.println("  1. Restart server");
            System.out.println("  2. Test endpoints: GET /bff/documents and GET /bff/budgets");
            System.out.println("  3. Verify frontend displays documents and budgets correctly");
            System.out.println("");

        } catch (Exception e) {
            System.err.println("");
            System.err.println("============================================================================");
            System.err.println("ERROR: Database reset failed!");
            System.err.println("============================================================================");
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    private String extractTableName(String sql, String prefix) {
        try {
            int startIndex = sql.toUpperCase().indexOf(prefix) + prefix.length();
            String remaining = sql.substring(startIndex).trim();

            // Handle "DROP TABLE ... CASCADE CONSTRAINTS"
            if (remaining.contains(" ")) {
                remaining = remaining.substring(0, remaining.indexOf(" "));
            }

            // Handle "CREATE TABLE ... ("
            if (remaining.contains("(")) {
                remaining = remaining.substring(0, remaining.indexOf("(")).trim();
            }

            return remaining;
        } catch (Exception e) {
            return null;
        }
    }
}
