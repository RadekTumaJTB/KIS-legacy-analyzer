package cz.jtbank.kis.bff.util;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Database Migration Runner - Executes SQL scripts on application startup
 *
 * This component runs once at application startup to populate the database with test data.
 * After successful migration, disable this component by commenting out @Component annotation.
 */
//@Component  // DISABLED - Using CompleteResetRunner instead
@Order(2) // Run after DatabaseTableCreator (0) and DatabaseTableVerifier (1)
public class DatabaseMigrationRunner implements CommandLineRunner {

    private final JdbcTemplate jdbcTemplate;

    public DatabaseMigrationRunner(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("============================================================================");
        System.out.println("KIS Project Module - Database Migration");
        System.out.println("============================================================================");
        System.out.println("Executing migration script: db/migration/insert_test_data.sql");
        System.out.println("============================================================================");
        System.out.println("");

        try {
            // Read SQL file from classpath
            ClassPathResource resource = new ClassPathResource("db/migration/insert_test_data.sql");
            BufferedReader reader = new BufferedReader(
                new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8)
            );

            // Parse SQL statements
            List<String> statements = parseSqlStatements(reader);
            reader.close();

            // Execute each statement
            int successCount = 0;
            int errorCount = 0;

            for (String statement : statements) {
                String trimmed = statement.trim();

                // Skip empty statements and comments
                if (trimmed.isEmpty() || trimmed.startsWith("--")) {
                    continue;
                }

                // Skip verification SELECT statements (they would fail with jdbcTemplate.update)
                if (trimmed.toUpperCase().startsWith("SELECT")) {
                    System.out.println("Skipping verification query: " + trimmed.substring(0, Math.min(50, trimmed.length())) + "...");
                    continue;
                }

                try {
                    jdbcTemplate.execute(trimmed);
                    successCount++;
                } catch (Exception e) {
                    // Ignore errors for DELETE statements (tables might be empty)
                    if (trimmed.toUpperCase().startsWith("DELETE")) {
                        System.out.println("Ignored DELETE error (table might be empty): " + e.getMessage());
                    } else if (e.getMessage() != null && e.getMessage().contains("ORA-00001")) {
                        // Unique constraint violation - data already exists
                        System.out.println("Data already exists, skipping: " + e.getMessage());
                    } else {
                        System.err.println("Error executing statement: " + trimmed.substring(0, Math.min(100, trimmed.length())));
                        System.err.println("Error: " + e.getMessage());
                        errorCount++;
                    }
                }
            }

            System.out.println("");
            System.out.println("============================================================================");
            System.out.println("Migration Summary:");
            System.out.println("  Successful statements: " + successCount);
            System.out.println("  Failed statements: " + errorCount);
            System.out.println("============================================================================");

            // Verify data
            verifyMigration();

            System.out.println("");
            System.out.println("============================================================================");
            System.out.println("Migration completed successfully!");
            System.out.println("============================================================================");
            System.out.println("");
            System.out.println("IMPORTANT: Disable DatabaseMigrationRunner by commenting out @Component");
            System.out.println("           to prevent re-running migration on next startup.");
            System.out.println("");

        } catch (Exception e) {
            System.err.println("");
            System.err.println("============================================================================");
            System.err.println("ERROR: Migration failed!");
            System.err.println("============================================================================");
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    private List<String> parseSqlStatements(BufferedReader reader) throws Exception {
        List<String> statements = new ArrayList<>();
        StringBuilder currentStatement = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            line = line.trim();

            // Skip comment lines
            if (line.startsWith("--") || line.isEmpty()) {
                continue;
            }

            // Append line to current statement
            currentStatement.append(line).append(" ");

            // If line ends with semicolon, it's the end of a statement
            if (line.endsWith(";")) {
                String statement = currentStatement.toString().trim();
                // Remove trailing semicolon
                if (statement.endsWith(";")) {
                    statement = statement.substring(0, statement.length() - 1);
                }
                statements.add(statement);
                currentStatement = new StringBuilder();
            }
        }

        return statements;
    }

    private void verifyMigration() {
        System.out.println("");
        System.out.println("Verifying migration:");

        try {
            Long projectCount = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM DB_JT.KP_KTG_PROJEKT", Long.class
            );
            System.out.println("  Projects count: " + projectCount);

            Long cashFlowCount = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM DB_JT.KP_DAT_PROJEKTCASHFLOW", Long.class
            );
            System.out.println("  Cash Flow count: " + cashFlowCount);

            Long statusCount = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM DB_JT.KP_CIS_PROJEKTSTATUS", Long.class
            );
            System.out.println("  Statuses count: " + statusCount);

            Long categoryCount = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM DB_JT.KP_CIS_PROJEKTKATEGORIE", Long.class
            );
            System.out.println("  Categories count: " + categoryCount);

        } catch (Exception e) {
            System.err.println("  Verification failed: " + e.getMessage());
        }
    }
}
