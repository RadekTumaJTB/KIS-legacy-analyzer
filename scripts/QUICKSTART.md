# Rychlý start - Database Deployment

## 1. Příprava

### Instalace závislostí

```bash
# Oracle klient knihovna
pip install oracledb

# Nebo starší verze
pip install cx_Oracle
```

### Kontrola struktury souborů

Ujistěte se, že máte SQL scripty ve správné struktuře:

```
sources/DB/
├── DB_JT/
│   ├── TABLES/
│   ├── SEQUENCES/
│   ├── VIEWS/
│   ├── PROCEDURES/
│   ├── PACKAGES/
│   └── PACKAGE_BODIES/
└── DB_DSA/
    └── ...
```

## 2. Ověření databáze

```bash
# Zkontrolujte, že databáze běží
docker ps | grep oracle

# Nebo
lsof -i :1521
```

## 3. Spuštění deploymentu

### Možnost A: Pomocí bash scriptu (Doporučeno)

```bash
# 1. Editujte proměnné ve scriptu
nano scripts/deploy_db.sh

# 2. Spusťte
./scripts/deploy_db.sh

# Nebo předejte parametry přímo
DB_USER=db_jt DB_PASSWORD=yourpass ./scripts/deploy_db.sh
```

### Možnost B: Přímo Python scriptem

```bash
# Základní deployment
python3 scripts/deploy_database.py \
  --connection "db_jt/password@localhost:1521/FREEPDB1"

# Deployment s více parametry
python3 scripts/deploy_database.py \
  --host localhost \
  --port 1521 \
  --service FREEPDB1 \
  --user db_jt \
  --password yourpass \
  --schema BOTH
```

## 4. První deployment (Doporučený postup)

### Krok 1: Dry-run

Nejprve spusťte dry-run pro zobrazení co by se nasadilo:

```bash
python3 scripts/deploy_database.py \
  --connection "db_jt/password@localhost:1521/FREEPDB1" \
  --dry-run
```

### Krok 2: Deploy do testovací databáze

```bash
python3 scripts/deploy_database.py \
  --connection "db_jt/password@localhost:1521/FREEPDB1" \
  --schema DB_JT
```

### Krok 3: Kontrola logů

```bash
# Najít poslední log
ls -lt deployment_*.log | head -1

# Zobrazit log
cat deployment_20251210_153000.log

# Hledat chyby
grep "Failed" deployment_*.log
grep "ERROR" deployment_*.log
```

### Krok 4: Full deployment

Když jste spokojeni s výsledky:

```bash
python3 scripts/deploy_database.py \
  --connection "db_jt/password@localhost:1521/FREEPDB1" \
  --schema BOTH
```

## 5. Časté použití

### Incremental deployment (bez mazání)

```bash
python3 scripts/deploy_database.py \
  --connection "db_jt/password@localhost:1521/FREEPDB1" \
  --skip-drop
```

### Zvýšit retry pokusy

```bash
python3 scripts/deploy_database.py \
  --connection "db_jt/password@localhost:1521/FREEPDB1" \
  --max-retries 5
```

### Vlastní log soubor

```bash
python3 scripts/deploy_database.py \
  --connection "db_jt/password@localhost:1521/FREEPDB1" \
  --log-file my_deployment.log
```

## 6. Troubleshooting

### Problém: "Connection refused"

```bash
# Zkontrolujte, že Oracle běží
docker ps | grep oracle
docker logs <container_id>

# Zkontrolujte port
lsof -i :1521

# Zkuste přímé připojení
sqlplus db_jt/password@localhost:1521/FREEPDB1
```

### Problém: "Table already exists"

Použijte default chování (maže objekty):

```bash
python3 scripts/deploy_database.py \
  --connection "db_jt/password@localhost:1521/FREEPDB1"
```

Nebo vynechte `--skip-drop` flag.

### Problém: Příliš mnoho failed objektů

1. Zkontrolujte detailní log:
```bash
cat deployment_*.log | grep -A 5 "Failed"
```

2. Zvyšte retry pokusy:
```bash
python3 scripts/deploy_database.py \
  --connection "..." \
  --max-retries 10
```

3. Některé objekty mohou vyžadovat manuální opravu SQL skriptů

### Problém: Encoding errors

Script používá UTF-8 s `errors='ignore'`. Pokud máte problémy:

```bash
# Zkontrolujte encoding souborů
file sources/DB/DB_JT/TABLES/*.sql | head

# Případně převeďte na UTF-8
iconv -f WINDOWS-1250 -t UTF-8 input.sql > output.sql
```

## 7. Typický workflow

```bash
# 1. Pull nejnovější změny
cd /Users/radektuma/DEV/KIS
git pull

# 2. Dry-run
python3 scripts/deploy_database.py \
  --connection "db_jt/pass@localhost:1521/FREEPDB1" \
  --dry-run

# 3. Deploy
python3 scripts/deploy_database.py \
  --connection "db_jt/pass@localhost:1521/FREEPDB1"

# 4. Kontrola výsledků
tail -50 deployment_*.log

# 5. Test aplikace
cd kis-bff-simple
mvn spring-boot:run
```

## 8. Pro Docker Oracle

Pokud používáte Docker Oracle kontejner:

```bash
# Získat connection string
docker inspect <container> | grep IPAddress

# Nebo použít localhost s port mappingem
python3 scripts/deploy_database.py \
  --connection "db_jt/password@localhost:1521/FREEPDB1"
```

## 9. Zkratky (aliasy)

Přidejte do `~/.bashrc` nebo `~/.zshrc`:

```bash
# Database deployment
alias deploy-db='cd /Users/radektuma/DEV/KIS && ./scripts/deploy_db.sh'
alias deploy-db-dry='cd /Users/radektuma/DEV/KIS && DRY_RUN=true ./scripts/deploy_db.sh'
alias deploy-db-jt='cd /Users/radektuma/DEV/KIS && SCHEMA=DB_JT ./scripts/deploy_db.sh'
```

Pak můžete použít:

```bash
deploy-db-dry      # Dry run
deploy-db          # Full deployment
deploy-db-jt       # Pouze DB_JT
```

## 10. Best Practices

✅ **Vždy používejte dry-run před prvním deploymentem**
✅ **Zálohujte databázi před deploymentem**
✅ **Zkontrolujte logy po deploymenentu**
✅ **Testujte aplikaci po deploymentu**
✅ **Používejte version control pro SQL scripty**

❌ **Nepoužívejte na produkci bez testování**
❌ **Neukládejte hesla do historie shellu**
❌ **Neignorujte chyby v lozích**

## Nápověda

Pro kompletní seznam parametrů:

```bash
python3 scripts/deploy_database.py --help
```

Pro detailní dokumentaci viz: `scripts/README_DEPLOYMENT.md`
