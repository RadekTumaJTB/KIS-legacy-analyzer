# KIS Production Stack - Quick Reference

**Datum:** 5. prosince 2025
**Status:** ✅ BĚŽÍ

---

## 🚀 Quick Start

```bash
cd /Users/radektuma/DEV/KIS

# Spustit produkci
docker-compose up -d

# Zastavit produkci
docker-compose down

# Zobrazit status
docker-compose ps
```

---

## 🌐 Endpointy

### KIS Application
- **Root:** http://localhost:8080/
- **Health:** http://localhost:8080/health
- **Actuator:** http://localhost:8080/actuator/health

### Oracle Database
- **Port:** localhost:1521
- **EM Express:** http://localhost:5500/em
- **Connection String:** `kis_user/kis_user_2024@localhost:1521/FREEPDB1`

---

## 📦 Kontejnery

| Kontejner   | Image                      | Port       | Status  |
|-------------|----------------------------|------------|---------|
| kis-app     | kis-banking-app:2.0.0      | 8080       | ✅ healthy |
| kis-oracle  | gvenzl/oracle-free:23-slim | 1521, 5500 | ✅ healthy |

---

## 📋 Příkazy

### Start/Stop
```bash
docker-compose up -d              # Spustit vše
docker-compose down               # Zastavit vše
docker-compose restart            # Restart všech služeb
docker-compose restart kis-app    # Restart jen aplikace
```

### Logy
```bash
docker-compose logs -f            # Všechny logy (real-time)
docker-compose logs -f kis-app    # Jen aplikace
docker-compose logs -f oracle     # Jen Oracle
docker-compose logs --tail=100    # Posledních 100 řádků
```

### Exec do kontejneru
```bash
docker exec -it kis-app sh                    # Shell v aplikaci
docker exec -it kis-oracle bash               # Bash v Oracle
docker exec -it kis-oracle sqlplus / as sysdba # SQL*Plus
```

### Rebuild
```bash
docker-compose up -d --build kis-app          # Rebuild aplikace
docker-compose build --no-cache kis-app       # Full rebuild
```

---

## 🔧 Troubleshooting

### Aplikace nereaguje
```bash
docker logs kis-app --tail 50
docker-compose restart kis-app
```

### Oracle problém
```bash
docker logs kis-oracle --tail 50
docker inspect kis-oracle | grep -A 10 Health
docker-compose restart oracle
```

### Port conflict
```bash
lsof -i :8080     # Najít proces na portu
```

---

## 💾 Volumes (Persistentní data)

```
kis_oracle_data           Database files
kis_oracle_backup         Database backups
kis_kis_app_data          Application data
kis_kis_app_logs          Application logs
kis_kis_app_config        Configuration
kis_kis_app_templates     Excel templates
```

---

## 🌐 Network

```
Network: kis_kis-production
Subnet:  172.21.0.0/16
Containers: kis-app, kis-oracle
```

---

## 📊 Health Checks

```bash
# Application
curl http://localhost:8080/health

# Docker health
docker inspect --format='{{.State.Health.Status}}' kis-app
docker inspect --format='{{.State.Health.Status}}' kis-oracle
```

**Očekávaný výsledek:** `healthy`

---

## 🔐 Credentials

### Oracle
- **System:** system / kis_oracle_2024
- **KIS User:** kis_user / kis_user_2024
- **Database:** FREEPDB1

### Application
- No authentication (test app)

---

## 📁 Důležité Soubory

```
/Users/radektuma/DEV/KIS/
├── docker-compose.yml              ← Produkční stack
├── docker-compose.analytics.yml    ← Analytics (oddělený)
├── DOCKER_COMPOSE_README.md        ← Kompletní dokumentace
├── PRODUCTION_STACK_STATUS.md      ← Status report
├── DOCKER_DESKTOP_VIEW.md          ← Docker Desktop guide
└── QUICK_REFERENCE.md              ← Tento soubor
```

---

## ⚡ Nejpoužívanější Příkazy

```bash
# Restart celého stacku
docker-compose restart

# Sledovat logy aplikace
docker-compose logs -f kis-app

# Zkontrolovat health status
docker-compose ps

# Rebuild aplikace po změnách
docker-compose up -d --build kis-app

# Připojit se do Oracle
docker exec -it kis-oracle sqlplus kis_user/kis_user_2024@FREEPDB1
```

---

## 🎯 Quick Test

```bash
# Test že vše běží
curl http://localhost:8080/health

# Očekávaný výstup:
# {
#   "status": "UP",
#   "java": "17.0.17",
#   "application": "KIS Docker Test"
# }
```

---

## 📞 Pokud něco nefunguje

1. **Zkontroluj logy:** `docker-compose logs -f`
2. **Zkontroluj status:** `docker-compose ps`
3. **Restart:** `docker-compose restart`
4. **Full restart:** `docker-compose down && docker-compose up -d`

---

**Pro detailní dokumentaci viz:**
- `DOCKER_COMPOSE_README.md` - Kompletní průvodce
- `PRODUCTION_STACK_STATUS.md` - Aktuální status
- `DOCKER_DESKTOP_VIEW.md` - Docker Desktop návod
