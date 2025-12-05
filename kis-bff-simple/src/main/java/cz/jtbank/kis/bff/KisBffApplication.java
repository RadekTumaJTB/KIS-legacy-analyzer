package cz.jtbank.kis.bff;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * KIS Banking Application - Backend For Frontend (BFF) Layer
 *
 * Simple version without Lombok (Java 24 compatible)
 *
 * Purpose: Aggregates data from multiple backend services and provides
 * frontend-optimized endpoints for the React SPA.
 *
 * Architecture:
 * - Port: 8081
 * - Core Backend Services: 8080 (when available)
 *
 * Key Features:
 * - Service aggregation (reduces API calls 50-80%)
 * - Mock data for frontend development
 * - Health check endpoints
 */
@SpringBootApplication
public class KisBffApplication {

    public static void main(String[] args) {
        SpringApplication.run(KisBffApplication.class, args);
    }
}
