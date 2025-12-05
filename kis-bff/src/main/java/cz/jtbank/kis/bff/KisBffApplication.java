package cz.jtbank.kis.bff;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * KIS Banking Application - Backend For Frontend (BFF) Layer
 *
 * Purpose: Aggregates data from multiple backend services and provides
 * frontend-optimized endpoints for the React SPA.
 *
 * Architecture:
 * - Port: 8081
 * - Core Backend Services: 8080
 * - Redis Cache: 6379
 *
 * Key Features:
 * - Service aggregation (reduces API calls 50-80%)
 * - Feign clients for backend communication
 * - Redis caching for performance
 * - JWT authentication
 * - Distributed tracing
 */
@SpringBootApplication
@EnableFeignClients
@EnableCaching
public class KisBffApplication {

    public static void main(String[] args) {
        SpringApplication.run(KisBffApplication.class, args);
    }
}
