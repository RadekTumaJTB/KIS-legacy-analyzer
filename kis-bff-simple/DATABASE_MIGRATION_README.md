# KIS Project Module - Database Migration Guide

Tento dokument popisuje kroky pro naplnění Oracle databáze testovacími daty pro modul Projekty.

## Prerekvizity

1. **Oracle Database** (verze 19c nebo vyšší)
   - Database running on `localhost:1521/FREEPDB1`
   - Schéma `DB_JT` vytvořeno
   - User `DB_JT` s heslem `kis_db_jt_2024`

2. **Oracle SQLPlus Client** nainstalován a dostupný v PATH

3. **Vytvořené tabulky** v schématu DB_JT:
   - `KP_KTG_PROJEKT` - hlavní tabulka projektů
   - `KP_DAT_PROJEKTCASHFLOW` - cash flow záznamy
   - `KP_CIS_PROJEKTSTATUS` - statusy projektů
   - `KP_CIS_PROJEKTKATEGORIE` - kategorie projektů
   - `KP_CIS_PROJEKTFREKVENCE` - frekvence hodnocení
   - `KP_CIS_MNGSEGMENT` - management segmenty
   - `KP_CIS_MENA` - měny
   - `KP_CIS_TYPBUDGETUPROJEKTU` - typy rozpočtů
   - `KP_CIS_TYPPROJEKTOVEBILANCE` - typy bilancí
   - `KP_CIS_PROJEKTCASHFLOWTYP` - typy cash flow
   - `KP_CIS_PROJEKTINOOUTTYP` - typy příjem/výdaj

## Postup migrace

### Možnost 1: Automatický skript (Doporučeno)

```bash
cd /Users/radektuma/DEV/KIS/kis-bff-simple/src/main/resources/db/migration
./run_migration.sh
```

### Možnost 2: Manuální spuštění přes SQLPlus

```bash
cd /Users/radektuma/DEV/KIS/kis-bff-simple/src/main/resources/db/migration

sqlplus DB_JT/kis_db_jt_2024@localhost:1521/FREEPDB1 @insert_test_data.sql
```

### Možnost 3: SQL Developer / DBeaver

1. Otevřete soubor `insert_test_data.sql` v SQL Developer nebo DBeaver
2. Připojte se k databázi jako user `DB_JT`
3. Spusťte celý skript (F5 nebo Execute Script)

## Co script vytvoří

### Číselníky (Lookup Tables)

- **5 Project Statuses**: Aktivní, V přípravě, Probíhá, Pozastaveno, Ukončeno
- **3 Project Categories**: IT Projekty, Business Development, Infrastruktura
- **3 Project Frequencies**: Měsíční, Čtvrtletní, Roční
- **3 Management Segments**: IT oddělení, Business Development, Finance
- **3 Currencies**: CZK, EUR, USD
- **2 Budget Types**: CAPEX, OPEX
- **2 Balance Types**: Aktivní, Pasivní
- **4 Cash Flow Types**: Investice, Provozní náklady, Licence, Služby
- **2 In/Out Types**: Příjem, Výdaj

### Testovací projekty

#### Projekt 1: Implementace CRM systému
- **Číslo**: PRJ-2025-001
- **Status**: Aktivní
- **PM ID**: 101
- **Management Segment**: IT oddělení (ID: 1)
- **Měna**: CZK
- **Datum zahájení**: 15.1.2025
- **Schvalovací úrovně**: 500k / 1M / 2M CZK
- **Budget Tracking**: Ano
- **Cash Flow položky**: 3 záznamy (Investice, Provozní náklady, Licence)

#### Projekt 2: Modernizace IT infrastruktury
- **Číslo**: PRJ-2025-002
- **Status**: Aktivní
- **PM ID**: 102
- **Management Segment**: IT oddělení (ID: 1)
- **Měna**: CZK
- **Datum zahájení**: 1.2.2025
- **Schvalovací úrovně**: 800k / 1.5M / 3M CZK
- **Cash Flow položky**: 2 záznamy (Investice, Služby)

#### Projekt 3: Digitalizace procesů
- **Číslo**: PRJ-2025-003
- **Status**: V přípravě
- **PM ID**: 103
- **Management Segment**: Business Development (ID: 2)
- **Měna**: CZK
- **Datum zahájení**: 1.3.2025
- **Schvalovací úrovně**: 300k / 600k / 1M CZK
- **Cash Flow položky**: 2 záznamy (Analytické služby, Licence)

## Ověření migrace

Po spuštění scriptu můžete ověřit data:

```sql
-- Počet projektů (mělo by být 3)
SELECT COUNT(*) FROM DB_JT.KP_KTG_PROJEKT;

-- Počet cash flow záznamů (mělo by být 7)
SELECT COUNT(*) FROM DB_JT.KP_DAT_PROJEKTCASHFLOW;

-- Seznam projektů
SELECT ID, S_CISLO, S_NAZEV, ID_STATUS
FROM DB_JT.KP_KTG_PROJEKT
ORDER BY ID;

-- Cash flow pro projekt 1
SELECT CF.ID, CF.DT_DATUM, CF.ND_CASTKA, CFT.S_NAZEV as TYP
FROM DB_JT.KP_DAT_PROJEKTCASHFLOW CF
JOIN DB_JT.KP_CIS_PROJEKTCASHFLOWTYP CFT ON CF.ID_CASHFLOWTYP = CFT.ID
WHERE CF.ID_KTGPROJEKT = 1
ORDER BY CF.DT_DATUM;
```

## Aktualizace aplikace

Po naplnění databáze je třeba aktualizovat `ProjectAggregationService.java`:

1. Odkomentovat repository volání
2. Odstranit mock data metody (`createMockProjectList()`, `createMockProjectDetail()`, atd.)
3. Restartovat Spring Boot aplikaci

Podrobné instrukce viz sekce níže.

## Troubleshooting

### Chyba: "ORA-00001: unique constraint violated"

**Řešení**: Data již existují v databázi. Smažte je pomocí:

```sql
DELETE FROM DB_JT.KP_DAT_PROJEKTCASHFLOW;
DELETE FROM DB_JT.KP_KTG_PROJEKT;
-- Případně i číselníky
DELETE FROM DB_JT.KP_CIS_PROJEKTSTATUS;
-- atd.
COMMIT;
```

Poté spusťte migraci znovu.

### Chyba: "ORA-00942: table or view does not exist"

**Řešení**: Tabulky v DB_JT schématu neexistují. Vytvořte je pomocí DDL skriptů.

### Chyba: "ORA-01017: invalid username/password"

**Řešení**: Zkontrolujte credentials v `application.properties`:
- Username: `DB_JT`
- Password: `kis_db_jt_2024`

### Chyba: "ORA-12541: TNS:no listener"

**Řešení**: Oracle Database neběží nebo je nedostupná na `localhost:1521`.

Zkontrolujte:
```bash
lsnrctl status
```

## Další kroky

Po úspěšné migraci:

1. Aktualizujte `ProjectAggregationService.java` (viz instrukce níže)
2. Restartujte Spring Boot aplikaci
3. Otestujte API endpoints:
   - GET http://localhost:8081/bff/projects
   - GET http://localhost:8081/bff/projects/1
4. Otestujte frontend:
   - http://localhost:5173/projects

---

# Aktualizace ProjectAggregationService

## Současný stav

Service aktuálně používá mock data:

```java
public List<ProjectSummaryDTO> getProjectList() {
    return createMockProjectList(); // MOCK DATA
}
```

## Požadovaný stav

Service by měl používat repository:

```java
public List<ProjectSummaryDTO> getProjectList() {
    List<ProjectEntity> projects = projectRepository.findAllByOrderByCreatedAtDesc();

    return projects.stream()
        .map(this::mapToSummaryDTO)
        .collect(Collectors.toList());
}

private ProjectSummaryDTO mapToSummaryDTO(ProjectEntity entity) {
    // Načíst související data z číselníků
    String statusName = getStatusName(entity.getIdStatus());
    String managementSegmentName = getManagementSegmentName(entity.getIdManagementSegment());

    return ProjectSummaryDTO.builder()
        .id(entity.getId())
        .name(entity.getName())
        .projectNumber(entity.getProjectNumber())
        .status(statusName)
        .statusCode(getStatusCode(entity.getIdStatus()))
        .projectManagerName("PM" + entity.getIdProjectManager()) // TODO: join with user table
        .managementSegmentName(managementSegmentName)
        .startDate(entity.getValuationStartDate())
        .description(entity.getDescription())
        .build();
}
```

## Postup aktualizace

1. Otevřete `ProjectAggregationService.java`
2. Najděte metody `getProjectList()` a `getProjectDetail()`
3. Odkomentujte kód s repository voláními
4. Zakomentujte/odstraňte mock data metody
5. Přidejte repository pro číselníky (ProjectStatusRepository, atd.)
6. Implementujte mapper metody
7. Zkompilujte a restartujte aplikaci

---

**Autor**: Claude Code
**Datum**: 2025-12-08
**Verze**: 1.0
