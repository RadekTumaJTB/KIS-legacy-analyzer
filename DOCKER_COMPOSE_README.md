# KIS Docker Compose Konfigurace

## Struktura Stacků

Projekt má **2 oddělené Docker Compose konfigurace**:

### 1. Produkční Stack (docker-compose.yml)
**Účel:** Běh KIS Banking Application v produkčním prostředí

**Služby:**
- `kis-oracle` - Oracle Database 23c Free Edition
- `kis-app` - KIS Banking Application (Java 17 + Spring Boot 3.2.1)

**Network:** `kis-production` (172.20.0.0/16)

### 2. Analytický Stack (docker-compose.analytics.yml)
**Účel:** Analýza kódu a dokumentace (nepoužívá se v produkci)

**Služby:**
- `kis-analytics-qdrant` - Vector database pro embedding
- `kis-analytics-neo4j` - Graph database pro code analysis

**Network:** `analytics-network`

---

## Spuštění Stacků

### Produkční Stack (Doporučeno)

```bash
# Spustit KIS aplikaci + Oracle databázi
docker-compose up -d

# Zobrazit logy
docker-compose logs -f

# Zobrazit status
docker-compose ps

# Zastavit
docker-compose down
```

### Analytický Stack (Pouze pro analýzu)

```bash
# Spustit Qdrant + Neo4j pro analýzu
docker-compose -f docker-compose.analytics.yml up -d

# Zobrazit logy
docker-compose -f docker-compose.analytics.yml logs -f

# Zastavit
docker-compose -f docker-compose.analytics.yml down
```

### Spustit Oba Stacky Současně

```bash
# Produkce + Analytics
docker-compose up -d
docker-compose -f docker-compose.analytics.yml up -d

# Zastavit vše
docker-compose down
docker-compose -f docker-compose.analytics.yml down
```

---

## Produkční Stack - Detaily

### Architektura

```
┌─────────────────────────────────────┐
│   KIS Banking Application           │
│   Java 17 + Spring Boot 3.2.1       │
│   Container: kis-app                │
│   Port: 8080                        │
│   Linux: Ubuntu (via Temurin)       │
└──────────────┬──────────────────────┘
               │
               │ JDBC Connection
               ↓
┌─────────────────────────────────────┐
│   Oracle Database 23c Free          │
│   Container: kis-oracle             │
│   Port: 1521 (listener)             │
│   Port: 5500 (EM Express)           │
│   Database: KISDB                   │
│   Pluggable DB: FREEPDB1            │
└─────────────────────────────────────┘
               │
               │ Network
               ↓
       kis-production (172.20.0.0/16)
```

### Endpointy

**KIS Application:**
- http://localhost:8080/ - Root endpoint
- http://localhost:8080/health - Health check
- http://localhost:8080/actuator/health - Spring Actuator

**Oracle Database:**
- `localhost:1521` - Oracle listener
- http://localhost:5500/em - Enterprise Manager Express
- Connection: `kis_user/kis_user_2024@localhost:1521/FREEPDB1`

### Volumes (Persistent Data)

```
oracle_data:         /opt/oracle/oradata (database files)
oracle_backup:       /opt/oracle/backup (backups)
kis_app_data:        /app/data (application data)
kis_app_logs:        /app/logs (logs)
kis_app_config:      /app/config (configuration)
kis_app_templates:   /app/data/templates (Excel templates)
```

### Environment Variables

**Oracle:**
- `ORACLE_PASSWORD=kis_oracle_2024`
- `ORACLE_DATABASE=KISDB`
- `ORACLE_SGA_TARGET=2G`

**KIS Application:**
- `SPRING_PROFILES_ACTIVE=prod`
- `DB_HOST=kis-oracle`
- `JAVA_OPTS=-Xmx2g -Xms512m`

---

## Analytický Stack - Detaily

### Účel

Tento stack byl používán **pouze pro analýzu kódu** během migrace:
- Neo4j: Mapování závislostí, call graphy, package structure
- Qdrant: Semantic search v kódu, dokumentace embedding

**Není součástí produkční aplikace!**

### Endpointy (když běží)

- http://localhost:7474/ - Neo4j Browser
- http://localhost:6333/ - Qdrant API
- http://localhost:6333/dashboard - Qdrant Dashboard

---

## Docker Desktop Zobrazení

### Produkční Stack
V Docker Desktop uvidíte:

**Containers:**
- `kis-oracle` (Running) 🟢
- `kis-app` (Running) 🟢

**Volumes:**
- `oracle_data`
- `oracle_backup`
- `kis_app_data`
- `kis_app_logs`
- `kis_app_config`
- `kis_app_templates`

**Networks:**
- `kis-production`

### Analytický Stack (pokud běží)
**Containers:**
- `kis-analytics-qdrant` (Running) 🟢
- `kis-analytics-neo4j` (Running) 🟢

**Networks:**
- `analytics-network`

---

## Správa a Údržba

### Restart Služeb

```bash
# Restart všech služeb
docker-compose restart

# Restart konkrétní služby
docker-compose restart kis-app
docker-compose restart oracle
```

### Rebuild Aplikace

```bash
# Po změnách v kódu
docker-compose up -d --build kis-app
```

### Zobrazení Logů

```bash
# Všechny logy
docker-compose logs -f

# Pouze aplikace
docker-compose logs -f kis-app

# Pouze Oracle
docker-compose logs -f oracle

# Posledních 100 řádků
docker-compose logs --tail=100 kis-app
```

### Připojení do Kontejneru

```bash
# Shell v aplikačním kontejneru
docker exec -it kis-app sh

# SQL*Plus v Oracle kontejneru
docker exec -it kis-oracle sqlplus / as sysdba

# Cypher Shell v Neo4j (analytics)
docker exec -it kis-analytics-neo4j cypher-shell
```

### Vyčištění

```bash
# Zastavit a odstranit kontejnery (volumes zůstanou)
docker-compose down

# Zastavit a odstranit včetně volumes (POZOR: data se smažou!)
docker-compose down -v

# Odstranit i images
docker-compose down --rmi all
```

---

## Troubleshooting

### Oracle nedostupná

```bash
# Zkontrolovat health check
docker inspect kis-oracle | grep -A 10 Health

# Zkontrolovat logy
docker logs kis-oracle --tail 50

# Restartovat
docker-compose restart oracle
```

### Aplikace se nespustí

```bash
# Zkontrolovat, jestli Oracle běží
docker-compose ps

# Zkontrolovat database connection
docker logs kis-app | grep -i "database\|oracle\|connection"

# Rebuild aplikace
docker-compose up -d --build kis-app
```

### Port již používán

```bash
# Najít proces na portu 8080
lsof -i :8080

# Změnit port v docker-compose.yml
ports:
  - "8081:8080"  # Externí:Interní
```

---

## Security Best Practices

### Produkční Nasazení

1. **Změnit hesla:**
   ```yaml
   environment:
     - ORACLE_PASSWORD=<silné heslo>
     - DB_PASSWORD=<silné heslo>
   ```

2. **Omezit porty:**
   ```yaml
   # Aplikace dostupná pouze lokálně
   ports:
     - "127.0.0.1:8080:8080"
   ```

3. **Použít secrets:**
   ```yaml
   secrets:
     - db_password
   ```

4. **Read-only filesystem:**
   ```yaml
   read_only: true
   tmpfs:
     - /tmp
   ```

---

## Migrace dat

### Export Oracle dat

```bash
# Export schema
docker exec kis-oracle expdp kis_user/kis_user_2024@FREEPDB1 \
  schemas=KIS_USER directory=DATA_PUMP_DIR dumpfile=kis_backup.dmp

# Zkopírovat dump z kontejneru
docker cp kis-oracle:/opt/oracle/oradata/KIS_USER.dmp ./backup/
```

### Import Oracle dat

```bash
# Zkopírovat dump do kontejneru
docker cp ./backup/kis_backup.dmp kis-oracle:/opt/oracle/admin/FREE/dpdump/

# Import
docker exec kis-oracle impdp system/kis_oracle_2024@FREEPDB1 \
  directory=DATA_PUMP_DIR dumpfile=kis_backup.dmp
```

---

## Performance Tuning

### Oracle Memory

```yaml
environment:
  - ORACLE_SGA_TARGET=4G  # Zvýšit pro více paměti
  - ORACLE_PGA_AGGREGATE_TARGET=2G
```

### JVM Tuning

```yaml
environment:
  - JAVA_OPTS=-Xmx4g -Xms1g -XX:+UseG1GC -XX:MaxGCPauseMillis=200
```

---

**Poznámka:** Pro produkční nasazení použijte **pouze docker-compose.yml**.
Analytický stack (docker-compose.analytics.yml) slouží pouze pro vývoj a analýzu.
