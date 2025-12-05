# Jak vidět KIS Stack v Docker Desktop

## 📱 Co uvidíte v Docker Desktop aplikaci

### 1. Containers Tab

```
┌─────────────────────────────────────────────────────────────────┐
│ Containers (2)                                                   │
├─────────────────────────────────────────────────────────────────┤
│                                                                   │
│ ✅ kis-app                                        [RUNNING] 🟢   │
│    kis-banking-app:2.0.0                                         │
│    Created: 2 minutes ago                                        │
│    Port: 8080:8080                                              │
│    ➜ http://localhost:8080                                      │
│                                                                   │
│ ✅ kis-oracle                                     [RUNNING] 🟢   │
│    gvenzl/oracle-free:23-slim                                   │
│    Created: 2 minutes ago                                        │
│    Ports: 1521:1521, 5500:5500                                  │
│                                                                   │
└─────────────────────────────────────────────────────────────────┘
```

**Akce pro každý kontejner:**
- 🖱️ Klik na kontejner → zobrazí detaily
- 📊 Logs → zobrazí výstup aplikace
- 🔧 Exec → otevře shell uvnitř kontejneru
- 🔄 Restart → restartuje kontejner
- 🛑 Stop → zastaví kontejner

---

### 2. Images Tab

```
┌─────────────────────────────────────────────────────────────────┐
│ Images                                                           │
├─────────────────────────────────────────────────────────────────┤
│                                                                   │
│ 📦 kis-banking-app:2.0.0                                         │
│    Size: 439 MB                                                  │
│    Created: 2 hours ago                                          │
│    Used by: kis-app                                             │
│                                                                   │
│ 📦 gvenzl/oracle-free:23-slim                                   │
│    Size: ~2.5 GB                                                 │
│    Created: Today                                                │
│    Used by: kis-oracle                                          │
│                                                                   │
│ 📦 eclipse-temurin:17-jre                                       │
│    Size: 195 MB                                                  │
│    Created: Today                                                │
│    Used as base for: kis-banking-app                           │
│                                                                   │
└─────────────────────────────────────────────────────────────────┘
```

---

### 3. Volumes Tab

```
┌─────────────────────────────────────────────────────────────────┐
│ Volumes (11)                                                     │
├─────────────────────────────────────────────────────────────────┤
│                                                                   │
│ 📂 PRODUCTION VOLUMES (6)                                        │
│                                                                   │
│ kis_oracle_data                                                  │
│ ├─ Size: ~500 MB (database files)                              │
│ └─ Mount: /opt/oracle/oradata                                  │
│                                                                   │
│ kis_oracle_backup                                               │
│ ├─ Size: Variable                                               │
│ └─ Mount: /opt/oracle/backup                                   │
│                                                                   │
│ kis_kis_app_data                                                │
│ ├─ Size: Variable                                               │
│ └─ Mount: /app/data                                            │
│                                                                   │
│ kis_kis_app_logs                                                │
│ ├─ Size: Variable                                               │
│ └─ Mount: /app/logs                                            │
│                                                                   │
│ kis_kis_app_config                                              │
│ ├─ Size: < 1 MB                                                 │
│ └─ Mount: /app/config                                          │
│                                                                   │
│ kis_kis_app_templates                                           │
│ ├─ Size: ~10 MB (Excel templates)                              │
│ └─ Mount: /app/data/templates                                  │
│                                                                   │
│ ─────────────────────────────────────────────────────────────   │
│                                                                   │
│ 📂 ANALYTICS VOLUMES (5) - Unused when analytics stack stopped  │
│                                                                   │
│ kis_neo4j_data                                                  │
│ kis_neo4j_logs                                                  │
│ kis_neo4j_import                                                │
│ kis_neo4j_plugins                                               │
│ kis_qdrant_storage                                              │
│                                                                   │
└─────────────────────────────────────────────────────────────────┘
```

---

### 4. Networks Tab

```
┌─────────────────────────────────────────────────────────────────┐
│ Networks                                                         │
├─────────────────────────────────────────────────────────────────┤
│                                                                   │
│ 🌐 kis_kis-production                                           │
│    Driver: bridge                                                │
│    Subnet: 172.21.0.0/16                                        │
│    Containers (2):                                               │
│    ├─ kis-app (172.21.0.3)                                      │
│    └─ kis-oracle (172.21.0.2)                                   │
│                                                                   │
│ 🌐 bridge                                                        │
│    Default Docker network                                        │
│                                                                   │
│ 🌐 host                                                          │
│    Host networking                                               │
│                                                                   │
└─────────────────────────────────────────────────────────────────┘
```

---

## 🔍 Jak zkontrolovat status v Docker Desktop

### Metoda 1: Containers Tab

1. Otevři Docker Desktop
2. Klikni na **Containers** (levý panel)
3. Uvidíš:
   - ✅ **kis-app** - zelený status (RUNNING)
   - ✅ **kis-oracle** - zelený status (RUNNING)

### Metoda 2: Logs View

1. V Containers tab klikni na **kis-app**
2. Klikni na **Logs** tab
3. Uvidíš:
   ```
   Started KisDockerTestApplication in 0.854 seconds
   Tomcat started on port 8080
   ```

4. Klikni na **kis-oracle**
5. Klikni na **Logs** tab
6. Uvidíš:
   ```
   DATABASE IS READY TO USE!
   Pluggable database KISDB opened read write
   Pluggable database FREEPDB1 opened read write
   ```

### Metoda 3: Stats View

1. V Containers tab klikni na kontejner
2. Klikni na **Stats** tab
3. Uvidíš real-time:
   - CPU usage
   - Memory usage
   - Network I/O
   - Block I/O

**Příklad pro kis-app:**
```
CPU:      5-10%
Memory:   ~200 MB / 2 GB limit
Network:  RX: 2 KB | TX: 15 KB
```

**Příklad pro kis-oracle:**
```
CPU:      10-15%
Memory:   ~1.5 GB / 4 GB limit
Network:  RX: 5 KB | TX: 20 KB
```

---

## 🎯 Quick Actions v Docker Desktop

### Pro kis-app kontejner:

**1. Otevřít aplikaci v browseru:**
- Klikni na kontejner `kis-app`
- V detailu uvidíš port `8080:8080`
- Klikni na `8080:8080` → otevře http://localhost:8080

**2. Zobrazit logy:**
- Klikni na kontejner → Logs tab
- Real-time log stream
- Možnost vyhledávání v lozích

**3. Exec do kontejneru:**
- Klikni na kontejner → Exec tab
- Otevře shell: `/bin/sh`
- Můžeš spouštět příkazy uvnitř kontejneru

**4. Inspect:**
- Klikni na kontejner → Inspect tab
- Zobrazí kompletní JSON konfiguraci
- Environment variables, volumes, network settings

### Pro kis-oracle kontejner:

**1. Zobrazit logy:**
- Zkontrolovat, že databáze je "READY TO USE"
- Sledovat SQL operace
- Debug connection issues

**2. Exec do kontejneru:**
- Otevře bash shell
- Můžeš spustit: `sqlplus / as sysdba`
- Přímý přístup k databázi

**3. EM Express (Enterprise Manager):**
- Port 5500 je mapován
- Otevři: http://localhost:5500/em
- Login: system / kis_oracle_2024
- Web-based Oracle management

---

## 📊 Monitoring v Docker Desktop

### Container Health Status

V Docker Desktop uvidíš health status ikony:

```
✅ 🟢 Healthy     - Health check prošel
⚠️  🟡 Starting   - Kontejner startuje
🔴 ❌ Unhealthy  - Health check selhal
⏸️  🔵 Stopped    - Kontejner zastaven
```

**kis-app Health Check:**
- Test: `curl -f http://localhost:8080/health`
- Interval: každých 30 sekund
- Timeout: 5 sekund
- Retries: 3

**kis-oracle Health Check:**
- Test: `healthcheck.sh` (built-in Oracle script)
- Interval: každých 30 sekund
- Timeout: 10 sekund
- Retries: 5

---

## 🛠️ Troubleshooting v Docker Desktop

### Pokud kontejner není vidět:

1. **Zkontroluj filter:**
   - Docker Desktop má filter "All" / "Running" / "Exited"
   - Ujisti se, že je vybrán "Running" nebo "All"

2. **Refresh view:**
   - Klikni na refresh ikonu (🔄) v pravém horním rohu

3. **Zkontroluj docker-compose:**
   - Otevři Terminal
   - Spusť: `docker-compose ps`
   - Pokud nevidíš kontejnery, spusť: `docker-compose up -d`

### Pokud kontejner má červený status (unhealthy):

1. **Zkontroluj logy:**
   - Klikni na kontejner → Logs
   - Hledej error messages

2. **Restart kontejner:**
   - Klikni na kontejner → klik na Restart tlačítko
   - Nebo v terminálu: `docker-compose restart <service-name>`

3. **Rebuild kontejner:**
   - V terminálu: `docker-compose up -d --build`

---

## 🎨 Visual Reference

### Co znamenají barevné indikátory:

- 🟢 **Zelená** = Running & Healthy
- 🟡 **Žlutá** = Starting / Initializing
- 🔴 **Červená** = Stopped / Unhealthy / Error
- 🔵 **Modrá** = Paused
- ⚪ **Šedá** = Created (not started)

### Ikony v Docker Desktop:

- 📦 **Box** = Container
- 🖼️ **Image** = Docker Image
- 💾 **Disk** = Volume
- 🌐 **Network** = Network
- 📊 **Graph** = Stats/Monitoring
- 📝 **Document** = Logs
- ⚙️ **Gear** = Settings

---

## ✅ Checklist - Jak ověřit že vše běží

V Docker Desktop zkontroluj:

1. **Containers Tab:**
   - [ ] Vidíš 2 kontejnery (kis-app, kis-oracle)
   - [ ] Oba mají zelený status 🟢
   - [ ] Status říká "RUNNING"
   - [ ] Health status je "healthy"

2. **Images Tab:**
   - [ ] kis-banking-app:2.0.0 existuje (439 MB)
   - [ ] gvenzl/oracle-free:23-slim existuje (~2.5 GB)

3. **Volumes Tab:**
   - [ ] Vidíš 6 production volumes (kis_oracle_*, kis_kis_app_*)
   - [ ] Volumes mají non-zero size

4. **Networks Tab:**
   - [ ] Síť kis_kis-production existuje
   - [ ] Síť obsahuje 2 kontejnery

5. **Funkční Test:**
   - [ ] http://localhost:8080/ → JSON response
   - [ ] http://localhost:8080/health → status: "UP"
   - [ ] http://localhost:5500/em → Oracle EM login

---

**Poznámka:** Pokud nevidíš očekávané kontejnery nebo volumes, zkontroluj že běží správný docker-compose soubor:

```bash
cd /Users/radektuma/DEV/KIS
docker-compose ps
```

Pokud je vše prázdné:
```bash
docker-compose up -d
```

---

**Docker Desktop verze:** 4.x+
**Platform:** macOS / Windows / Linux
**Project:** KIS Banking Application
**Date:** 5. prosince 2025
