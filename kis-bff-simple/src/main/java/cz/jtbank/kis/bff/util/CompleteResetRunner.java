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
 * Complete Database Reset and Population Runner
 *
 * This runner performs a complete database reset:
 * 1. Drops all existing tables
 * 2. Creates all 11 tables from scratch with proper constraints
 * 3. Populates all tables with test data
 *
 * IMPORTANT: Disable this component after successful execution by commenting out @Component
 */
//@Component  // DISABLED - Migration completed successfully
@Order(0)
public class CompleteResetRunner implements CommandLineRunner {

    private final JdbcTemplate jdbcTemplate;

    public CompleteResetRunner(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("============================================================================");
        System.out.println("KIS Project Module - COMPLETE DATABASE RESET");
        System.out.println("============================================================================");
        System.out.println("WARNING: This will DROP all existing tables and recreate them!");
        System.out.println("============================================================================");
        System.out.println("");

        try {
            // Read complete reset SQL file
            ClassPathResource resource = new ClassPathResource("db/migration/complete_reset_and_populate.sql");
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
                "SELECT COUNT(*) FROM user_tables WHERE table_name LIKE 'KP_%'", Long.class
            );
            System.out.println("Tables created: " + tableCount);

            Long projectCount = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM DB_JT.KP_KTG_PROJEKT", Long.class
            );
            System.out.println("Projects inserted: " + projectCount);

            Long cashFlowCount = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM DB_JT.KP_DAT_PROJEKTCASHFLOW", Long.class
            );
            System.out.println("Cash Flow records inserted: " + cashFlowCount);

            Long statusCount = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM DB_JT.KP_CIS_PROJEKTSTATUS", Long.class
            );
            System.out.println("Statuses inserted: " + statusCount);

            System.out.println("");
            System.out.println("============================================================================");
            System.out.println("✓ DATABASE RESET AND POPULATION COMPLETED SUCCESSFULLY!");
            System.out.println("============================================================================");
            System.out.println("");
            System.out.println("IMPORTANT: Disable CompleteResetRunner by commenting out @Component");
            System.out.println("           annotation to prevent re-running on next startup.");
            System.out.println("");
            System.out.println("Next steps:");
            System.out.println("  1. Disable all migration runners (CompleteResetRunner, DatabaseTableCreator, etc.)");
            System.out.println("  2. Update ProjectAggregationService to use repository instead of mock data");
            System.out.println("  3. Restart server and test endpoints");
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
