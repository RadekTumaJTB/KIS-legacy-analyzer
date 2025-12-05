package cz.jtbank.kis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * KIS Docker Test Application
 *
 * Minimal Spring Boot application for testing Docker infrastructure
 * Platform: Linux UBI-base10 (64-bit)
 * Java: 17 LTS
 * Framework: Spring Boot 3.2.1
 */
@SpringBootApplication
@RestController
public class KisDockerTestApplication {

    private static final DateTimeFormatter formatter =
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static void main(String[] args) {
        System.out.println("═══════════════════════════════════════════════════════════");
        System.out.println("  KIS Docker Test Application - Starting");
        System.out.println("  Version: 1.0.0");
        System.out.println("  Java: " + System.getProperty("java.version"));
        System.out.println("  OS: " + System.getProperty("os.name"));
        System.out.println("  Framework: Spring Boot 3.2.1");
        System.out.println("═══════════════════════════════════════════════════════════");

        SpringApplication.run(KisDockerTestApplication.class, args);
    }

    @GetMapping("/")
    public Map<String, String> root() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "KIS Docker Test - Running Successfully!");
        response.put("health", "/health");
        response.put("actuator", "/actuator/health");
        response.put("version", "1.0.0");
        return response;
    }

    @GetMapping("/health")
    public Map<String, Object> health() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("application", "KIS Docker Test");
        response.put("version", "1.0.0");
        response.put("platform", "Linux UBI-base10 (64-bit)");
        response.put("java", System.getProperty("java.version"));
        response.put("timestamp", LocalDateTime.now().format(formatter));
        return response;
    }
}
