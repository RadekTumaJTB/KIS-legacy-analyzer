package cz.jtbank.kis.bff.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

/**
 * Feign client for Core Backend Service
 *
 * For now, this is a mock client that returns sample data
 * because the core backend is still being migrated from Oracle ADF
 *
 * When real endpoints are available, replace mock data with actual API calls
 */
@FeignClient(
    name = "core-backend",
    url = "${services.core-backend.url}"
)
public interface CoreBackendClient {

    /**
     * Get document by ID
     * TODO: Replace with actual endpoint when available
     */
    @GetMapping("/api/documents/{id}")
    Map<String, Object> getDocument(@PathVariable Long id);

    /**
     * Get approvals for document
     * TODO: Replace with actual endpoint when available
     */
    @GetMapping("/api/approvals")
    Map<String, Object> getApprovals(@PathVariable Long documentId);

    /**
     * Get user by ID
     * TODO: Replace with actual endpoint when available
     */
    @GetMapping("/api/users/{id}")
    Map<String, Object> getUser(@PathVariable Long id);

    /**
     * Get company by ID
     * TODO: Replace with actual endpoint when available
     */
    @GetMapping("/api/companies/{id}")
    Map<String, Object> getCompany(@PathVariable Long id);

    /**
     * Get transactions for document
     * TODO: Replace with actual endpoint when available
     */
    @GetMapping("/api/transactions")
    Map<String, Object> getTransactions(@PathVariable Long documentId);
}
