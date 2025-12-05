# KIS Docker Test Application

Minimální Spring Boot aplikace pro testování Docker infrastruktury pro KIS Banking Application.

## Quick Start

```bash
# Build JAR
mvn clean package -DskipTests

# Build Docker image
docker build -t kis-app:test .

# Run container
docker run -d -p 8080:8080 --name kis-test kis-app:test

# Test endpoints
curl http://localhost:8080/
curl http://localhost:8080/health
curl http://localhost:8080/actuator/health

# Stop container
docker stop kis-test && docker rm kis-test
```

## Stack

- **Java:** 17 LTS
- **Framework:** Spring Boot 3.2.1
- **Base Image:** eclipse-temurin:17-jre
- **Platform:** Linux (multi-arch)

## Results

- ✅ Build time: ~3 seconds
- ✅ Startup time: 0.85 seconds
- ✅ Image size: 439 MB
- ✅ Health check: functional
- ✅ Security: non-root user

## Purpose

Tento projekt slouží k:
1. Validaci Docker infrastructure setupu
2. Testování Java 17 runtime v kontejneru
3. Ověření Spring Boot 3.2.1 compatibility
4. Proof-of-concept pro produkční deployment

## Next Steps

Po úspěšném testu bude plný KIS projekt migrován:
- Oracle ADF → Spring Boot services
- Přidání Oracle database connectivi ty
- Kompletní business logic migrace

---

**Status:** ✅ Test Successful
**Date:** 2025-12-05
