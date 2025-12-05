package cz.jtbank.kis.bff.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Health check controller for BFF
 */
@Slf4j
@RestController
@CrossOrigin(origins = "*")
public class HealthController {

    @Value("${spring.application.name}")
    private String applicationName;

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> health = new HashMap<>();
        health.put("status", "UP");
        health.put("application", applicationName);
        health.put("version", "1.0.0-SNAPSHOT");
        health.put("java", System.getProperty("java.version"));
        health.put("description", "Backend For Frontend layer for KIS React application");

        return ResponseEntity.ok(health);
    }

    @GetMapping("/")
    public ResponseEntity<Map<String, String>> root() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "KIS BFF Layer - Running");
        response.put("swagger", "/swagger-ui.html (TODO)");
        response.put("health", "/health");
        response.put("actuator", "/actuator");
        response.put("endpoints", "/bff/documents/{id}/detail");

        return ResponseEntity.ok(response);
    }
}
