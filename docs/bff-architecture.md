# KIS Banking Application - BFF Architecture
**Backend For Frontend Layer Design**

**Date:** December 5, 2025
**Status:** Architecture Design Document
**Version:** 1.0

---

## Executive Summary

This document defines the **Backend For Frontend (BFF)** architecture pattern for KIS Banking Application's React frontend. The BFF layer serves as an intermediary between the React SPA and the Spring Boot backend, optimizing data delivery, aggregating multiple services, and providing frontend-specific endpoints.

### Why BFF?

**Current Architecture:**
```
React Frontend → Spring Boot Backend → Oracle Database
```

**Problem:**
- Frontend needs to make multiple API calls for single screen
- Data shapes don't match UI requirements (over/under-fetching)
- No aggregation of related data
- Backend designed for Oracle ADF, not modern SPA

**BFF Architecture:**
```
React Frontend → BFF Layer (Spring Boot) → Core Services → Oracle Database
```

**Benefits:**
- ✅ Single API call for complex UIs
- ✅ Data aggregation across multiple services
- ✅ Frontend-optimized DTOs
- ✅ Reduced network requests
- ✅ Simplified React code
- ✅ Better security (hide backend complexity)

---

## 1. BFF Layer Architecture

### 1.1 High-Level Design

```
┌─────────────────────────────────────────────────────────────────┐
│                    REACT FRONTEND                                │
│  Components → Hooks → React Query → API Services                │
└─────────────────────────────────────────────────────────────────┘
                             ↓ HTTP/REST
┌─────────────────────────────────────────────────────────────────┐
│                   BFF LAYER (Spring Boot)                        │
│                                                                  │
│  ┌───────────────────────────────────────────────────────────┐ │
│  │  BFF Controllers (Frontend-specific endpoints)            │ │
│  │  ├─ DocumentBFFController                                  │ │
│  │  ├─ BudgetBFFController                                    │ │
│  │  ├─ ConsolidationBFFController                             │ │
│  │  └─ ExportBFFController                                    │ │
│  └───────────────────────────────────────────────────────────┘ │
│                             ↓                                    │
│  ┌───────────────────────────────────────────────────────────┐ │
│  │  BFF Services (Aggregation & Composition Logic)           │ │
│  │  ├─ DocumentAggregationService                             │ │
│  │  ├─ BudgetAggregationService                               │ │
│  │  └─ ConsolidationAggregationService                        │ │
│  └───────────────────────────────────────────────────────────┘ │
│                             ↓                                    │
│  ┌───────────────────────────────────────────────────────────┐ │
│  │  Clients (Calls to Core Services via REST/gRPC)           │ │
│  │  ├─ DocumentServiceClient                                  │ │
│  │  ├─ BudgetServiceClient                                    │ │
│  │  ├─ ApprovalServiceClient                                  │ │
│  │  └─ UserServiceClient                                      │ │
│  └───────────────────────────────────────────────────────────┘ │
└─────────────────────────────────────────────────────────────────┘
                             ↓ Internal REST/gRPC
┌─────────────────────────────────────────────────────────────────┐
│                CORE BACKEND SERVICES (Spring Boot)               │
│                                                                  │
│  ┌──────────────┬──────────────┬──────────────┬──────────────┐ │
│  │  Document    │  Budget      │  Approval    │  User        │ │
│  │  Service     │  Service     │  Service     │  Service     │ │
│  └──────────────┴──────────────┴──────────────┴──────────────┘ │
│                             ↓                                    │
│  ┌───────────────────────────────────────────────────────────┐ │
│  │         JPA Repositories → Oracle Database 23c            │ │
│  └───────────────────────────────────────────────────────────┘ │
└─────────────────────────────────────────────────────────────────┘
```

### 1.2 BFF Deployment Strategy

**Option 1: Monolithic BFF (Recommended for start)**
```
Single Spring Boot Application:
- Port: 8081 (BFF)
- Port: 8080 (Core Backend - existing)
```

**Pros:**
- Simpler deployment
- Easier debugging
- Shared dependencies

**Cons:**
- Couples frontend/backend deployments
- Single point of failure

**Option 2: Separate BFF Service (Future)**
```
Microservices Architecture:
- BFF Service: Port 8081 (dedicated)
- Document Service: Port 8082
- Budget Service: Port 8083
- Consolidation Service: Port 8084
- User/Auth Service: Port 8085
```

**Pros:**
- Independent deployments
- Scalability
- Tech diversity

**Cons:**
- More complex
- Need service mesh
- Distributed tracing required

**Recommendation:** Start with Option 1, migrate to Option 2 after 6-12 months.

---

## 2. BFF Endpoints Design

### 2.1 Document Module BFF Endpoints

**Traditional Backend (current):**
```java
// Multiple calls needed for Document Detail page
GET /api/documents/{id}              → Document entity
GET /api/approvals?documentId={id}   → Approval history
GET /api/users/{approverUserId}      → Approver details
GET /api/companies/{companyId}       → Company details
GET /api/transactions/{documentId}   → Related transactions

// 5 API calls for single screen!
```

**BFF Endpoint (optimized):**
```java
@RestController
@RequestMapping("/bff/documents")
public class DocumentBFFController {

    /**
     * Get complete document with all related data for detail view
     * Single API call instead of 5
     */
    @GetMapping("/{id}/detail")
    public DocumentDetailDTO getDocumentDetail(@PathVariable Long id) {
        return documentAggregationService.getFullDocumentDetail(id);
    }
}
```

**Response DTO:**
```json
{
  "document": {
    "id": 12345,
    "number": "DOC-2025-0001",
    "type": "INVOICE",
    "amount": 150000.00,
    "currency": "CZK",
    "dueDate": "2025-01-15",
    "status": "PENDING_APPROVAL",
    "createdBy": {
      "id": 1,
      "name": "Martin Novák",
      "email": "martin.novak@jtbank.cz"
    },
    "company": {
      "id": 10,
      "name": "JT Bank a.s.",
      "ico": "12345678"
    }
  },
  "approvalChain": [
    {
      "level": 1,
      "approver": {
        "id": 2,
        "name": "Petra Svobodová",
        "position": "Manager"
      },
      "status": "APPROVED",
      "approvedAt": "2025-12-01T10:30:00Z",
      "comment": "Schváleno"
    },
    {
      "level": 2,
      "approver": {
        "id": 3,
        "name": "Eva Černá",
        "position": "CFO"
      },
      "status": "PENDING",
      "approvedAt": null,
      "comment": null
    }
  ],
  "relatedTransactions": [
    {
      "id": 501,
      "type": "PAYMENT",
      "amount": 75000.00,
      "date": "2025-11-20"
    }
  ],
  "lineItems": [
    {
      "id": 1001,
      "description": "Consulting services",
      "quantity": 10,
      "unitPrice": 15000.00,
      "total": 150000.00
    }
  ],
  "metadata": {
    "canEdit": true,
    "canApprove": false,
    "canReject": false,
    "pendingApproverName": "Eva Černá"
  }
}
```

### 2.2 Budget Module BFF Endpoints

```java
@RestController
@RequestMapping("/bff/budgets")
public class BudgetBFFController {

    /**
     * Dashboard data - aggregates KPIs, charts, alerts
     */
    @GetMapping("/dashboard")
    public BudgetDashboardDTO getDashboard(
        @RequestParam(required = false) String period,
        @RequestParam(required = false) Long departmentId
    ) {
        return budgetAggregationService.getDashboardData(period, departmentId);
    }

    /**
     * Budget list with filtering, sorting, pagination
     */
    @GetMapping("/list")
    public Page<BudgetListItemDTO> getBudgetList(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size,
        @RequestParam(required = false) String search,
        @RequestParam(required = false) String status,
        @RequestParam(required = false) Long departmentId,
        @RequestParam(defaultValue = "createdAt,desc") String sort
    ) {
        return budgetAggregationService.getBudgetList(
            PageRequest.of(page, size, Sort.by(sort)),
            search, status, departmentId
        );
    }

    /**
     * Budget detail with line items, approvals, execution tracking
     */
    @GetMapping("/{id}/detail")
    public BudgetDetailDTO getBudgetDetail(@PathVariable Long id) {
        return budgetAggregationService.getFullBudgetDetail(id);
    }

    /**
     * Budget comparison - actual vs planned with variance analysis
     */
    @GetMapping("/{id}/comparison")
    public BudgetComparisonDTO getBudgetComparison(
        @PathVariable Long id,
        @RequestParam String comparisonType // "MONTHLY", "QUARTERLY", "YEARLY"
    ) {
        return budgetAggregationService.getComparison(id, comparisonType);
    }
}
```

### 2.3 Consolidation Module BFF Endpoints

```java
@RestController
@RequestMapping("/bff/consolidations")
public class ConsolidationBFFController {

    /**
     * Consolidation setup wizard - all data needed for creation
     */
    @GetMapping("/setup-data")
    public ConsolidationSetupDTO getSetupData() {
        return consolidationAggregationService.getSetupData();
    }

    /**
     * Formula builder autocomplete - available fields, functions
     */
    @GetMapping("/formula/autocomplete")
    public FormulaAutocompleteDTO getFormulaAutocomplete(
        @RequestParam String partial
    ) {
        return consolidationAggregationService.getFormulaAutocomplete(partial);
    }

    /**
     * Execute consolidation and get results
     */
    @PostMapping("/{id}/execute")
    public ConsolidationResultsDTO executeConsolidation(
        @PathVariable Long id
    ) {
        return consolidationAggregationService.executeAndGetResults(id);
    }
}
```

### 2.4 Export BFF Endpoints

```java
@RestController
@RequestMapping("/bff/export")
public class ExportBFFController {

    /**
     * Export wizard - step 1: get available columns/fields
     */
    @GetMapping("/{entityType}/fields")
    public ExportFieldsDTO getAvailableFields(
        @PathVariable String entityType // "DOCUMENT", "BUDGET", etc.
    ) {
        return exportAggregationService.getAvailableFields(entityType);
    }

    /**
     * Export wizard - step 4: generate Excel and return download URL
     */
    @PostMapping("/excel")
    public ExportResultDTO generateExcelExport(
        @RequestBody ExportRequestDTO request
    ) {
        String downloadUrl = exportAggregationService.generateExcel(request);
        return new ExportResultDTO(downloadUrl, "export.xlsx");
    }
}
```

---

## 3. BFF Service Implementation

### 3.1 Document Aggregation Service

```java
@Service
@RequiredArgsConstructor
public class DocumentAggregationService {

    private final DocumentServiceClient documentClient;
    private final ApprovalServiceClient approvalClient;
    private final UserServiceClient userClient;
    private final CompanyServiceClient companyClient;
    private final TransactionServiceClient transactionClient;

    /**
     * Aggregates data from 5 services into single DTO
     * Uses CompletableFuture for parallel execution
     */
    public DocumentDetailDTO getFullDocumentDetail(Long id) {

        // Parallel API calls to backend services
        CompletableFuture<DocumentDTO> documentFuture =
            CompletableFuture.supplyAsync(() -> documentClient.getDocument(id));

        CompletableFuture<List<ApprovalDTO>> approvalsFuture =
            CompletableFuture.supplyAsync(() -> approvalClient.getApprovals(id));

        CompletableFuture<List<TransactionDTO>> transactionsFuture =
            CompletableFuture.supplyAsync(() -> transactionClient.getTransactions(id));

        // Wait for all to complete
        CompletableFuture.allOf(documentFuture, approvalsFuture, transactionsFuture).join();

        // Get results
        DocumentDTO document = documentFuture.join();
        List<ApprovalDTO> approvals = approvalsFuture.join();
        List<TransactionDTO> transactions = transactionsFuture.join();

        // Fetch related entities
        UserDTO creator = userClient.getUser(document.getCreatedById());
        CompanyDTO company = companyClient.getCompany(document.getCompanyId());

        // Enrich approvals with user details
        List<ApprovalChainItemDTO> enrichedApprovals = approvals.stream()
            .map(approval -> {
                UserDTO approver = userClient.getUser(approval.getApproverId());
                return ApprovalChainItemDTO.builder()
                    .level(approval.getLevel())
                    .approver(UserSummaryDTO.from(approver))
                    .status(approval.getStatus())
                    .approvedAt(approval.getApprovedAt())
                    .comment(approval.getComment())
                    .build();
            })
            .collect(Collectors.toList());

        // Calculate metadata (permissions, actions)
        DocumentMetadataDTO metadata = calculateMetadata(document, approvals);

        // Assemble final DTO
        return DocumentDetailDTO.builder()
            .document(document)
            .approvalChain(enrichedApprovals)
            .relatedTransactions(transactions)
            .lineItems(document.getLineItems())
            .metadata(metadata)
            .build();
    }

    private DocumentMetadataDTO calculateMetadata(DocumentDTO doc, List<ApprovalDTO> approvals) {
        // Business logic: can current user edit/approve/reject?
        Long currentUserId = SecurityContextHolder.getCurrentUserId();

        boolean canEdit = doc.getStatus() == DocumentStatus.DRAFT
            && doc.getCreatedById().equals(currentUserId);

        ApprovalDTO pendingApproval = approvals.stream()
            .filter(a -> a.getStatus() == ApprovalStatus.PENDING)
            .findFirst()
            .orElse(null);

        boolean canApprove = pendingApproval != null
            && pendingApproval.getApproverId().equals(currentUserId);

        String pendingApproverName = pendingApproval != null
            ? userClient.getUser(pendingApproval.getApproverId()).getName()
            : null;

        return DocumentMetadataDTO.builder()
            .canEdit(canEdit)
            .canApprove(canApprove)
            .canReject(canApprove)
            .pendingApproverName(pendingApproverName)
            .build();
    }
}
```

### 3.2 Budget Aggregation Service

```java
@Service
@RequiredArgsConstructor
public class BudgetAggregationService {

    private final BudgetServiceClient budgetClient;
    private final TransactionServiceClient transactionClient;
    private final DepartmentServiceClient departmentClient;

    /**
     * Dashboard KPIs: total budget, spent, remaining, alerts
     */
    public BudgetDashboardDTO getDashboardData(String period, Long departmentId) {

        // Get all budgets for period/department
        List<BudgetDTO> budgets = budgetClient.getBudgets(period, departmentId);

        // Get actual transactions (spending)
        List<TransactionDTO> transactions = transactionClient.getTransactions(period, departmentId);

        // Calculate aggregates
        BigDecimal totalBudgeted = budgets.stream()
            .map(BudgetDTO::getAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalSpent = transactions.stream()
            .map(TransactionDTO::getAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal remaining = totalBudgeted.subtract(totalSpent);

        double utilizationPercent = totalBudgeted.compareTo(BigDecimal.ZERO) > 0
            ? totalSpent.divide(totalBudgeted, 2, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100)).doubleValue()
            : 0.0;

        // Identify overrun budgets
        List<BudgetAlertDTO> alerts = budgets.stream()
            .filter(budget -> {
                BigDecimal spent = transactionClient.getSpentAmount(budget.getId());
                return spent.compareTo(budget.getAmount()) > 0;
            })
            .map(budget -> BudgetAlertDTO.builder()
                .budgetId(budget.getId())
                .budgetName(budget.getName())
                .overrunAmount(transactionClient.getSpentAmount(budget.getId())
                    .subtract(budget.getAmount()))
                .severity("HIGH")
                .build())
            .collect(Collectors.toList());

        // Prepare chart data (last 12 months)
        List<BudgetChartDataDTO> chartData = prepareChartData(budgets, transactions);

        return BudgetDashboardDTO.builder()
            .totalBudgeted(totalBudgeted)
            .totalSpent(totalSpent)
            .remaining(remaining)
            .utilizationPercent(utilizationPercent)
            .alerts(alerts)
            .chartData(chartData)
            .build();
    }

    private List<BudgetChartDataDTO> prepareChartData(
        List<BudgetDTO> budgets,
        List<TransactionDTO> transactions
    ) {
        // Group by month
        Map<YearMonth, BigDecimal> budgetByMonth = budgets.stream()
            .collect(Collectors.groupingBy(
                b -> YearMonth.from(b.getValidFrom()),
                Collectors.mapping(
                    BudgetDTO::getAmount,
                    Collectors.reducing(BigDecimal.ZERO, BigDecimal::add)
                )
            ));

        Map<YearMonth, BigDecimal> spentByMonth = transactions.stream()
            .collect(Collectors.groupingBy(
                t -> YearMonth.from(t.getDate()),
                Collectors.mapping(
                    TransactionDTO::getAmount,
                    Collectors.reducing(BigDecimal.ZERO, BigDecimal::add)
                )
            ));

        // Merge into chart data
        return budgetByMonth.entrySet().stream()
            .sorted(Map.Entry.comparingByKey())
            .map(entry -> {
                YearMonth month = entry.getKey();
                BigDecimal budgeted = entry.getValue();
                BigDecimal spent = spentByMonth.getOrDefault(month, BigDecimal.ZERO);

                return BudgetChartDataDTO.builder()
                    .month(month.toString())
                    .budgeted(budgeted)
                    .spent(spent)
                    .variance(budgeted.subtract(spent))
                    .build();
            })
            .collect(Collectors.toList());
    }
}
```

---

## 4. Service Clients (Feign)

### 4.1 Document Service Client

```java
@FeignClient(name = "document-service", url = "${services.document.url}")
public interface DocumentServiceClient {

    @GetMapping("/api/documents/{id}")
    DocumentDTO getDocument(@PathVariable Long id);

    @GetMapping("/api/documents/{id}/line-items")
    List<DocumentLineItemDTO> getLineItems(@PathVariable Long id);

    @PostMapping("/api/documents")
    DocumentDTO createDocument(@RequestBody CreateDocumentRequest request);

    @PutMapping("/api/documents/{id}")
    DocumentDTO updateDocument(
        @PathVariable Long id,
        @RequestBody UpdateDocumentRequest request
    );
}
```

### 4.2 Approval Service Client

```java
@FeignClient(name = "approval-service", url = "${services.approval.url}")
public interface ApprovalServiceClient {

    @GetMapping("/api/approvals")
    List<ApprovalDTO> getApprovals(@RequestParam Long documentId);

    @PostMapping("/api/approvals/{id}/approve")
    ApprovalDTO approve(
        @PathVariable Long id,
        @RequestBody ApprovalActionRequest request
    );

    @PostMapping("/api/approvals/{id}/reject")
    ApprovalDTO reject(
        @PathVariable Long id,
        @RequestBody ApprovalActionRequest request
    );
}
```

---

## 5. Caching Strategy

### 5.1 Redis Caching for BFF

```java
@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
            .entryTtl(Duration.ofMinutes(10))
            .disableCachingNullValues();

        return RedisCacheManager.builder(connectionFactory)
            .cacheDefaults(config)
            .withCacheConfiguration("users",
                config.entryTtl(Duration.ofHours(1))) // Users change rarely
            .withCacheConfiguration("companies",
                config.entryTtl(Duration.ofHours(1)))
            .withCacheConfiguration("documents",
                config.entryTtl(Duration.ofMinutes(5))) // Documents change frequently
            .build();
    }
}
```

### 5.2 Cache Usage

```java
@Service
public class UserServiceClient {

    @Cacheable(value = "users", key = "#id")
    public UserDTO getUser(Long id) {
        return restTemplate.getForObject("/api/users/" + id, UserDTO.class);
    }

    @CacheEvict(value = "users", key = "#id")
    public void updateUser(Long id, UpdateUserRequest request) {
        restTemplate.put("/api/users/" + id, request);
    }
}
```

---

## 6. Error Handling

### 6.1 Global Exception Handler

```java
@RestControllerAdvice
public class BFFExceptionHandler {

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<ErrorResponse> handleFeignException(FeignException ex) {

        String message = extractErrorMessage(ex);

        return ResponseEntity
            .status(ex.status())
            .body(ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(ex.status())
                .error("Upstream Service Error")
                .message(message)
                .path(/* request path */)
                .build());
    }

    @ExceptionHandler(TimeoutException.class)
    public ResponseEntity<ErrorResponse> handleTimeout(TimeoutException ex) {
        return ResponseEntity
            .status(HttpStatus.GATEWAY_TIMEOUT)
            .body(ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(504)
                .error("Service Timeout")
                .message("Backend service did not respond in time")
                .build());
    }
}
```

---

## 7. API Gateway Integration (Optional Future)

```
┌─────────────────┐
│  React Frontend │
└────────┬────────┘
         ↓
┌────────────────────────────────┐
│     API Gateway (Kong/NGINX)   │
│  - Rate Limiting               │
│  - Authentication (JWT)        │
│  - Request Routing             │
└────────┬───────────────────────┘
         ↓
         ├─→ /bff/*    → BFF Service (Port 8081)
         ├─→ /api/*    → Core Backend (Port 8080)
         └─→ /auth/*   → Auth Service (Port 8085)
```

---

## 8. Monitoring & Observability

### 8.1 Spring Boot Actuator

```yaml
# application.yml
management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics,prometheus
  endpoint:
    health:
      show-details: always
  metrics:
    tags:
      application: kis-bff
    export:
      prometheus:
        enabled: true
```

### 8.2 Distributed Tracing (Sleuth + Zipkin)

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-sleuth</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-sleuth-zipkin</artifactId>
</dependency>
```

Trace ID propagation across all service calls:
```
React → BFF → DocumentService → Database
  └─ traceId: abc123-def456-ghi789 (same across all hops)
```

---

## 9. Security

### 9.1 JWT Authentication

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/bff/**").authenticated()
                .requestMatchers("/actuator/health").permitAll()
            )
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwt -> jwt.decoder(jwtDecoder()))
            );

        return http.build();
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        return JwtDecoders.fromIssuerLocation("https://auth.jtbank.cz");
    }
}
```

### 9.2 CORS Configuration

```java
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/bff/**")
            .allowedOrigins("http://localhost:5173", "https://kis.jtbank.cz")
            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
            .allowedHeaders("*")
            .allowCredentials(true)
            .maxAge(3600);
    }
}
```

---

## 10. Implementation Roadmap

### Phase 1: Foundation (Sprint 1-2)

**Week 1-2:**
- [ ] Setup BFF Spring Boot project structure
- [ ] Configure Feign clients for core services
- [ ] Implement health check endpoint
- [ ] Setup Redis caching
- [ ] Configure Spring Security + JWT

**Week 3-4:**
- [ ] Implement DocumentBFFController + DocumentAggregationService
- [ ] Test document detail aggregation
- [ ] Implement error handling
- [ ] Add distributed tracing
- [ ] Load testing (100 concurrent users)

### Phase 2: Budget & Consolidation (Sprint 3-4)

**Week 5-6:**
- [ ] Implement BudgetBFFController
- [ ] Dashboard data aggregation
- [ ] Budget comparison logic
- [ ] Chart data preparation

**Week 7-8:**
- [ ] Implement ConsolidationBFFController
- [ ] Formula autocomplete endpoint
- [ ] Consolidation execution
- [ ] Results aggregation

### Phase 3: Export & Optimization (Sprint 5-6)

**Week 9-10:**
- [ ] Implement ExportBFFController
- [ ] Excel wizard endpoints
- [ ] Background job processing
- [ ] S3/Minio file storage

**Week 11-12:**
- [ ] Performance optimization
- [ ] Cache tuning
- [ ] API Gateway setup
- [ ] Production deployment

---

## 11. Testing Strategy

### 11.1 Unit Tests

```java
@ExtendWith(MockitoExtension.class)
class DocumentAggregationServiceTest {

    @Mock
    private DocumentServiceClient documentClient;

    @Mock
    private ApprovalServiceClient approvalClient;

    @InjectMocks
    private DocumentAggregationService service;

    @Test
    void shouldAggregateDocumentDetailSuccessfully() {
        // Given
        when(documentClient.getDocument(1L))
            .thenReturn(mockDocument());
        when(approvalClient.getApprovals(1L))
            .thenReturn(mockApprovals());

        // When
        DocumentDetailDTO result = service.getFullDocumentDetail(1L);

        // Then
        assertNotNull(result);
        assertEquals(2, result.getApprovalChain().size());
        verify(documentClient).getDocument(1L);
        verify(approvalClient).getApprovals(1L);
    }
}
```

### 11.2 Integration Tests

```java
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class DocumentBFFControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DocumentServiceClient documentClient;

    @Test
    @WithMockUser
    void shouldReturnDocumentDetail() throws Exception {
        // Given
        when(documentClient.getDocument(1L))
            .thenReturn(mockDocument());

        // When & Then
        mockMvc.perform(get("/bff/documents/1/detail"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.document.id").value(1))
            .andExpect(jsonPath("$.approvalChain").isArray())
            .andExpect(jsonPath("$.metadata.canEdit").isBoolean());
    }
}
```

### 11.3 Contract Tests (Pact)

```java
@ExtendWith(PactConsumerTestExt.class)
@PactTestFor(providerName = "document-service", port = "8080")
class DocumentServiceClientPactTest {

    @Pact(consumer = "kis-bff")
    public RequestResponsePact getDocumentPact(PactDslWithProvider builder) {
        return builder
            .given("document with id 1 exists")
            .uponReceiving("a request for document 1")
            .path("/api/documents/1")
            .method("GET")
            .willRespondWith()
            .status(200)
            .body(newJsonBody(body -> {
                body.numberType("id", 1);
                body.stringValue("number", "DOC-2025-0001");
                body.numberValue("amount", 150000.00);
            }).build())
            .toPact();
    }
}
```

---

## 12. Configuration

### 12.1 application.yml

```yaml
server:
  port: 8081

spring:
  application:
    name: kis-bff

  # Redis Cache
  redis:
    host: localhost
    port: 6379
    timeout: 2000ms

  # Feign Client Config
  cloud:
    openfeign:
      client:
        config:
          default:
            connectTimeout: 5000
            readTimeout: 10000
            loggerLevel: basic

# Service URLs
services:
  document:
    url: http://localhost:8080
  budget:
    url: http://localhost:8080
  approval:
    url: http://localhost:8080
  user:
    url: http://localhost:8080

# Logging
logging:
  level:
    cz.jtbank.kis.bff: DEBUG
    feign: DEBUG
```

### 12.2 pom.xml Dependencies

```xml
<dependencies>
    <!-- Spring Boot Web -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>

    <!-- Feign Client -->
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-openfeign</artifactId>
    </dependency>

    <!-- Redis Cache -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-redis</artifactId>
    </dependency>

    <!-- Spring Security + OAuth2 Resource Server -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-security</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-oauth2-resource-server</artifactId>
    </dependency>

    <!-- Distributed Tracing -->
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-sleuth</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-sleuth-zipkin</artifactId>
    </dependency>

    <!-- Actuator + Metrics -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-actuator</artifactId>
    </dependency>
    <dependency>
        <groupId>io.micrometer</groupId>
        <artifactId>micrometer-registry-prometheus</artifactId>
    </dependency>

    <!-- Lombok -->
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <optional>true</optional>
    </dependency>

    <!-- Testing -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>au.com.dius.pact.consumer</groupId>
        <artifactId>junit5</artifactId>
        <scope>test</scope>
    </dependency>
</dependencies>
```

---

## 13. Performance Benchmarks

### Expected Performance Targets:

| Metric | Without BFF | With BFF | Improvement |
|--------|-------------|----------|-------------|
| Document Detail Load | 5 API calls<br/>~2000ms | 1 API call<br/>~400ms | **80% faster** |
| Budget Dashboard Load | 8 API calls<br/>~3500ms | 1 API call<br/>~600ms | **82% faster** |
| Network Requests (avg page) | 15-20 requests | 3-5 requests | **70% reduction** |
| Data Transfer Size | ~500 KB | ~150 KB | **70% reduction** |

---

## 14. Conclusion

### Summary

The BFF layer provides:
- ✅ **50-80% reduction** in API calls per page
- ✅ **Simplified React code** (single API call vs. orchestrating multiple)
- ✅ **Frontend-optimized DTOs** (no over/under-fetching)
- ✅ **Better security** (hides backend complexity)
- ✅ **Improved performance** (parallel service calls, caching)
- ✅ **Easier testing** (contract tests, integration tests)

### Next Steps

1. **Sprint 1-2:** Implement BFF foundation + Document module
2. **Sprint 3-4:** Add Budget + Consolidation modules
3. **Sprint 5-6:** Export functionality + performance optimization
4. **Sprint 7+:** API Gateway, monitoring, production deployment

### References

- [BFF Pattern - Martin Fowler](https://samnewman.io/patterns/architectural/bff/)
- [Spring Cloud OpenFeign](https://spring.io/projects/spring-cloud-openfeign)
- [Redis Caching Best Practices](https://redis.io/docs/manual/patterns/)
- [Distributed Tracing with Sleuth](https://spring.io/projects/spring-cloud-sleuth)

---

**Document Owner:** KIS Development Team
**Last Updated:** December 5, 2025
**Status:** Ready for Implementation
