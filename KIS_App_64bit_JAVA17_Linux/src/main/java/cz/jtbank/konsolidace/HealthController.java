package cz.jtbank.konsolidace;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * Health Check Controller for Docker health monitoring
 *
 * Endpoint: /health
 * Used by: Docker HEALTHCHECK
 *
 * @version 1.0
 * @since 2025-12-05
 */
@RestController
public class HealthController {

    private static final DateTimeFormatter formatter =
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @GetMapping("/health")
    public Map<String, Object> health() {
        Map<String, Object> response = new HashMap<>();

        response.put("status", "UP");
        response.put("application", "KIS Banking Application");
        response.put("version", "2.0.0-SNAPSHOT");
        response.put("platform", "Linux UBI-base10 (64-bit)");
        response.put("java", System.getProperty("java.version"));
        response.put("timestamp", LocalDateTime.now().format(formatter));

        return response;
    }

    @GetMapping("/")
    public Map<String, String> root() {
        Map<String, String> response = new HashMap<>();

        response.put("message", "KIS Banking Application - Running");
        response.put("health", "/health");
        response.put("version", "2.0.0-SNAPSHOT");

        return response;
    }
}
