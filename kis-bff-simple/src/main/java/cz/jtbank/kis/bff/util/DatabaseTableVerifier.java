package cz.jtbank.kis.bff.util;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * Database Table Verifier - Checks if required tables exist
 */
@Component
@Order(1) // Run after CompleteResetRunner (Order 0)
public class DatabaseTableVerifier implements CommandLineRunner {

    private final JdbcTemplate jdbcTemplate;

    private static final String[] REQUIRED_TABLES = {
        "KP_KTG_PROJEKT",
        "KP_DAT_PROJEKTCASHFLOW",
        "KP_CIS_PROJEKTSTATUS",
        "KP_CIS_PROJEKTKATEGORIE",
        "KP_CIS_PROJEKTFREKVENCE",
        "KP_CIS_MNGSEGMENT",
        "KP_CIS_MENA",
        "KP_CIS_TYPBUDGETUPROJEKTU",
        "KP_CIS_TYPPROJEKTOVEBILANCE",
        "KP_CIS_PROJEKTCASHFLOWTYP",
        "KP_CIS_PROJEKTINOOUTTYP"
    };

    public DatabaseTableVerifier(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("============================================================================");
        System.out.println("Database Table Verification");
        System.out.println("============================================================================");

        try {
            // Get database metadata
            jdbcTemplate.execute((java.sql.Connection connection) -> {
                DatabaseMetaData metaData = connection.getMetaData();

                System.out.println("Database: " + metaData.getDatabaseProductName() + " " + metaData.getDatabaseProductVersion());
                System.out.println("Connection URL: " + metaData.getURL());
                System.out.println("Current user: " + metaData.getUserName());
                System.out.println("");

                // Check each table
                List<String> existingTables = new ArrayList<>();
                List<String> missingTables = new ArrayList<>();

                for (String tableName : REQUIRED_TABLES) {
                    // Try DB_JT schema
                    ResultSet rs = metaData.getTables(null, "DB_JT", tableName, new String[]{"TABLE"});

                    if (rs.next()) {
                        existingTables.add(tableName);
                        System.out.println("✓ Table exists: DB_JT." + tableName);
                    } else {
                        missingTables.add(tableName);
                        System.out.println("✗ Table missing: DB_JT." + tableName);
                    }
                    rs.close();
                }

                System.out.println("");
                System.out.println("Summary:");
                System.out.println("  Existing tables: " + existingTables.size() + "/" + REQUIRED_TABLES.length);
                System.out.println("  Missing tables: " + missingTables.size());

                if (!missingTables.isEmpty()) {
                    System.out.println("");
                    System.out.println("ERROR: Some required tables are missing!");
                    System.out.println("Please create the missing tables using DDL scripts.");
                }

                System.out.println("============================================================================");
                System.out.println("");

                return null;
            });

        } catch (Exception e) {
            System.err.println("ERROR: Failed to verify database tables!");
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
