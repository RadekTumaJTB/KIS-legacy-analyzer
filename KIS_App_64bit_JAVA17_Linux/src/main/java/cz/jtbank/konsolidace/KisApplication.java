package cz.jtbank.konsolidace;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/**
 * KIS Banking Application - Main Spring Boot Application
 *
 * Migration: Oracle ADF → Spring Boot 3.2.1
 * Java: 17 LTS
 * Platform: Linux UBI-base10 (64-bit)
 *
 * @version 2.0.0-SNAPSHOT
 * @since 2025-12-05
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@ComponentScan(basePackages = {
    "cz.jtbank.konsolidace"
    // Note: Oracle ADF classes will be gradually migrated to Spring components
    // Excluded for now: excel, doklady, budget, etc. (Oracle ADF ViewObjects)
})
public class KisApplication {

    public static void main(String[] args) {
        // Log startup information
        System.out.println("═══════════════════════════════════════════════════════════");
        System.out.println("  KIS Banking Application - Starting");
        System.out.println("  Version: 2.0.0-SNAPSHOT");
        System.out.println("  Java: " + System.getProperty("java.version"));
        System.out.println("  OS: " + System.getProperty("os.name"));
        System.out.println("  Platform: Linux UBI-base10 (64-bit)");
        System.out.println("  Framework: Spring Boot 3.2.1");
        System.out.println("═══════════════════════════════════════════════════════════");

        SpringApplication.run(KisApplication.class, args);
    }
}
