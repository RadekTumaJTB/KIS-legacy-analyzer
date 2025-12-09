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
 * Database Table Creator - Creates missing database tables
 *
 * This component runs BEFORE DatabaseTableVerifier to create missing tables.
 * Disable this component by commenting out @Component after tables are created.
 */
//@Component  // DISABLED - Using CompleteResetRunner instead
@Order(0) // Run before DatabaseTableVerifier (Order 1)
public class DatabaseTableCreator implements CommandLineRunner {

    private final JdbcTemplate jdbcTemplate;

    public DatabaseTableCreator(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("============================================================================");
        System.out.println("KIS Project Module - Database Table Creation");
        System.out.println("============================================================================");
        System.out.println("Creating missing database tables...");
        System.out.println("============================================================================");
        System.out.println("");

        try {
            // Read DDL file from classpath
            ClassPathResource resource = new ClassPathResource("db/migration/create_missing_tables.sql");
            BufferedReader reader = new BufferedReader(
                new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8)
            );

            // Parse SQL statements
            List<String> statements = parseSqlStatements(reader);
            reader.close();

            // Execute each statement
            int successCount = 0;
            int errorCount = 0;
            int skipCount = 0;

            for (String statement : statements) {
                String trimmed = statement.trim();

                // Skip empty statements and comments
                if (trimmed.isEmpty() || trimmed.startsWith("--")) {
                    continue;
                }

                // Skip SELECT and COMMENT statements (informational only)
                if (trimmed.toUpperCase().startsWith("SELECT") ||
                    trimmed.toUpperCase().startsWith("COMMENT")) {
                    skipCount++;
                    continue;
                }

                try {
                    jdbcTemplate.execute(trimmed);

                    // Extract table/index name for user feedback
                    String objectName = extractObjectName(trimmed);
                    if (objectName != null) {
                        System.out.println("✓ Created: " + objectName);
                    }

                    successCount++;
                } catch (Exception e) {
                    // Check if error is "table already exists"
                    if (e.getMessage() != null &&
                        (e.getMessage().contains("ORA-00955") || // table already exists
                         e.getMessage().contains("name is already used"))) {
                        String objectName = extractObjectName(trimmed);
                        System.out.println("  Skipped (already exists): " + objectName);
                        skipCount++;
                    } else {
                        System.err.println("✗ Error: " + trimmed.substring(0, Math.min(80, trimmed.length())));
                        System.err.println("  " + e.getMessage());
                        errorCount++;
                    }
                }
            }

            System.out.println("");
            System.out.println("============================================================================");
            System.out.println("Table Creation Summary:");
            System.out.println("  Successful: " + successCount);
            System.out.println("  Skipped: " + skipCount);
            System.out.println("  Failed: " + errorCount);
            System.out.println("============================================================================");

            if (errorCount == 0) {
                System.out.println("");
                System.out.println("All tables created successfully!");
                System.out.println("");
                System.out.println("IMPORTANT: Disable DatabaseTableCreator by commenting out @Component");
                System.out.println("           to prevent re-running on next startup.");
                System.out.println("");
            } else {
                System.err.println("");
                System.err.println("ERROR: Some tables failed to create!");
                System.err.println("       Check error messages above.");
                System.err.println("");
            }

        } catch (Exception e) {
            System.err.println("");
            System.err.println("============================================================================");
            System.err.println("ERROR: Table creation failed!");
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

    private String extractObjectName(String sql) {
        String upperSql = sql.toUpperCase();

        if (upperSql.startsWith("CREATE TABLE")) {
            int start = upperSql.indexOf("TABLE") + 5;
            int end = upperSql.indexOf("(", start);
            if (end > start) {
                return sql.substring(start, end).trim();
            }
        } else if (upperSql.startsWith("CREATE INDEX")) {
            int start = upperSql.indexOf("INDEX") + 5;
            int end = upperSql.indexOf("ON", start);
            if (end > start) {
                return sql.substring(start, end).trim();
            }
        } else if (upperSql.startsWith("ALTER TABLE")) {
            int start = upperSql.indexOf("TABLE") + 5;
            String[] parts = sql.substring(start).split("\\s+");
            if (parts.length > 0) {
                return parts[0].trim();
            }
        }

        return null;
    }
}
