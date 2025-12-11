# Module 2: Assets (Emise a Majetkové Účasti) - Database Schema

**Date**: 2025-12-09
**Source**: Oracle Database Schema Analysis
**Module**: Finanční Investice a Majetkové Účasti

---

## Overview

Assets module manages:
1. **Financial Investments** (Finanční Investice) - Equity securities
2. **Emissions** (Emise) - Stock/bond emissions
3. **Equity Stakes** (Majetkové Účasti) - Ownership in other companies

---

## Core Tables

### 1. KP_KTG_FINANCNIINVESTICE
**Purpose**: Catalog of Financial Investments

| Column | Type | Description |
|--------|------|-------------|
| ID | NUMBER | Primary key |
| ID_KTGSPOLECNOST | NUMBER | Company ID (FK) |
| S_MENA | VARCHAR2(3) | Currency code (CZK, EUR, USD) |
| DT_ZMENA | DATE | Last modified date |
| S_UZIVATEL | VARCHAR2(20) | User who modified |
| S_ISIN | VARCHAR2(13) | ISIN code (international security ID) |

**Relationships**:
- → `KP_KTG_SPOLECNOST` (Company)
- ← `KP_KTG_FININVESTICEEMISE` (Emissions)

---

### 2. KP_KTG_FININVESTICEEMISE
**Purpose**: Emissions for Financial Investments (time-series data)

| Column | Type | Description |
|--------|------|-------------|
| ID | NUMBER | Primary key |
| ID_KTGFINANCNIINVESTICE | NUMBER | Financial investment ID (FK) |
| ID_PARENT | NUMBER | Parent emission ID (for history) |
| DT_PLATNOSTOD | DATE | Valid from date |
| DT_PLATNOSTDO | DATE | Valid to date |
| NL_POCETKUSU | NUMBER | Number of shares/units |
| NL_NOMINAL | NUMBER(17,4) | Nominal value |
| ND_ZAKLADNIJMENI | NUMBER(15,2) | Registered capital |
| DT_ZMENA | DATE | Last modified |
| S_UZIVATEL | VARCHAR2(20) | User |
| C_INVESTICE | CHAR(1) | Investment flag (F/T) |
| C_NENULOVY | CHAR(1) | Non-zero flag |

**View**: `VW_FINANCNIINVEMISE` adds:
- `POCETMU` - Count of equity stakes for this emission

**Relationships**:
- → `KP_KTG_FINANCNIINVESTICE`
- ← `KP_DAT_MAJETKOVAUCAST` (Equity stakes)

---

### 3. KP_DAT_MAJETKOVAUCAST
**Purpose**: Equity Stakes (actual ownership records)

| Column | Type | Description |
|--------|------|-------------|
| ID | NUMBER | Primary key |
| ID_PARENT | NUMBER | Parent record (for history) |
| ID_KTGUCETNISPOLECNOST | NUMBER | Accounting company (owner) |
| ID_KTGFININVESTICEEMISE | NUMBER | Emission ID (FK) |
| S_UCET | VARCHAR2(12) | Account number |
| DT_PLATNOSTOD | DATE | Valid from |
| DT_PLATNOSTDO | DATE | Valid to |
| ID_CISMUTYPTRANSAKCE | NUMBER | Transaction type ID |
| ID_CISMUZPUSOB | NUMBER | Method ID |
| NL_POCETKUSU | NUMBER(17,4) | Number of shares |
| S_MENATRANSAKCE | VARCHAR2(3) | Transaction currency |
| ND_CENATRANSAKCEKUS | NUMBER(15,2) | Price per share (transaction) |
| ND_OBJEMTRANSAKCE | NUMBER(15,2) | Total transaction amount |
| ND_KURZ | NUMBER(10,6) | Exchange rate |
| S_MENAUCETNI | VARCHAR2(3) | Accounting currency |
| ND_CENAUCETNIKUS | NUMBER(15,2) | Price per share (accounting) |
| ND_OBJEMUCETNI | NUMBER(15,2) | Total accounting amount |
| DT_ZMENA | DATE | Last modified |
| S_UZIVATEL | VARCHAR2(20) | User |
| ID_KTGUCETNISPOLECNOSTKOUPENO | NUMBER | Company purchased from |
| C_IGNOROVAT | CHAR(1) | Ignore flag |

**Comment**: `ID_KTGUCETNISPOLECNOSTKOUPENO` - "říká od koho se ta investice koupila" (who we bought the investment from)

---

### 4. Reference/Codebook Tables

#### KP_CIS_MAJETKOVAUCASTTYPTRAN
**Purpose**: Equity Stake Transaction Types

| Column | Type | Description |
|--------|------|-------------|
| ID | NUMBER | Primary key |
| S_POPIS | VARCHAR2 | Description (Nákup, Prodej, Transfer, etc.) |

#### KP_CIS_MAJETKOVAUCASTZPUSOB
**Purpose**: Equity Stake Methods

| Column | Type | Description |
|--------|------|-------------|
| ID | NUMBER | Primary key |
| S_POPIS | VARCHAR2 | Description |

---

### 5. Control/Configuration Tables

#### KP_DEF_KONTROLAMAJETKOVAUCAST
**Purpose**: Asset Control Definition - Account mapping

| Purpose | Description |
|---------|-------------|
| Defines which accounts are valid for equity stake transactions |
| Maps accounts to equity stake types |
| Used for validation in `Majetek Kontrola.jsp` |

#### KP_KTG_MAJETKOVEUCASTIVYJIMKY
**Purpose**: Equity Stake Exceptions

| Purpose | Description |
|---------|-------------|
| Exceptions to normal equity stake rules |
| Special cases for consolidation |

---

### 6. Audit/History Tables

#### KP_LOG_FINANCNIINVESTICE
**Purpose**: Financial Investment Change Log

| Purpose | Description |
|---------|-------------|
| Tracks all changes to financial investments |
| Audit trail for compliance |

#### KP_LOG_FININVESTICEEMISE
**Purpose**: Emission Change Log

#### KP_LOG_MAJETKOVAUCAST
**Purpose**: Equity Stake Change Log

#### KP_LOG_KONTROLAMAJETKOVAUCAST
**Purpose**: Asset Control Change Log

---

## Entity Relationships

```
KP_KTG_SPOLECNOST (Company)
  ↓
KP_KTG_FINANCNIINVESTICE (Financial Investment)
  ↓
KP_KTG_FININVESTICEEMISE (Emission - time series)
  ↓
KP_DAT_MAJETKOVAUCAST (Equity Stake)
  ↓ (references)
KP_CIS_MAJETKOVAUCASTTYPTRAN (Transaction Type)
KP_CIS_MAJETKOVAUCASTZPUSOB (Method)
```

---

## Data Flow

### 1. Create Financial Investment
1. Insert into `KP_KTG_FINANCNIINVESTICE` (company, currency, ISIN)
2. Log in `KP_LOG_FINANCNIINVESTICE`

### 2. Create Emission
1. Insert into `KP_KTG_FININVESTICEEMISE` (shares, nominal value)
2. Link to financial investment
3. Log in `KP_LOG_FININVESTICEEMISE`

### 3. Record Equity Stake
1. Insert into `KP_DAT_MAJETKOVAUCAST` (owner, shares, price)
2. Link to emission
3. Calculate accounting values (exchange rate)
4. Log in `KP_LOG_MAJETKOVAUCAST`

### 4. Control/Validation
1. Check `KP_DEF_KONTROLAMAJETKOVAUCAST` for valid accounts
2. Apply exceptions from `KP_KTG_MAJETKOVEUCASTIVYJIMKY`
3. Log validation results

---

## Views for Reporting

### VW_FINANCNIINVEMISE
- Enriched emission data with equity stake count
- Used in overview screens

### Expected Views (to be discovered):
- `VW_FINANCNIINVEMISEHISTOR` - Historical view
- `VW_MAJETKOVAUCAST_OVERVIEW` - Equity stakes overview
- `VW_MAJETKOVAUCAST_CHANGES` - Changes over time

---

## Backend Implementation Plan

### Entities to Create:

1. **FinancialInvestmentEntity** - Maps to `KP_KTG_FINANCNIINVESTICE`
2. **FinancialInvestmentEmissionEntity** - Maps to `KP_KTG_FININVESTICEEMISE`
3. **EquityStakeEntity** - Maps to `KP_DAT_MAJETKOVAUCAST`
4. **EquityStakeTransactionTypeEntity** - Maps to `KP_CIS_MAJETKOVAUCASTTYPTRAN`
5. **EquityStakeMethodEntity** - Maps to `KP_CIS_MAJETKOVAUCASTZPUSOB`
6. **AssetControlDefinitionEntity** - Maps to `KP_DEF_KONTROLAMAJETKOVAUCAST`

### Repositories:
- `FinancialInvestmentRepository`
- `FinancialInvestmentEmissionRepository`
- `EquityStakeRepository`
- Reference repositories for codebooks

### Services:
- `AssetAggregationService` - Main service for BFF
  - `getFinancialInvestments()`
  - `getFinancialInvestmentDetail(id)`
  - `getEquityStakes(filters)`
  - `getEquityStakeChanges(dateFrom, dateTo)`
  - `validateAssetControl(accountId)`

---

## API Endpoints (BFF)

```
GET    /bff/assets/investments                 # List financial investments
GET    /bff/assets/investments/{id}            # Investment detail
POST   /bff/assets/investments                 # Create investment
PUT    /bff/assets/investments/{id}            # Update investment
DELETE /bff/assets/investments/{id}            # Delete investment

GET    /bff/assets/emissions                   # List emissions
GET    /bff/assets/emissions/{id}              # Emission detail
POST   /bff/assets/emissions                   # Create emission
PUT    /bff/assets/emissions/{id}              # Update emission

GET    /bff/assets/equity-stakes               # List equity stakes
GET    /bff/assets/equity-stakes/{id}          # Stake detail
POST   /bff/assets/equity-stakes               # Create stake
PUT    /bff/assets/equity-stakes/{id}          # Update stake

GET    /bff/assets/overview                    # Overview dashboard
GET    /bff/assets/changes                     # Changes history
POST   /bff/assets/control/validate            # Validate account control

# Reference data
GET    /bff/reference/equity-stake-types       # Transaction types
GET    /bff/reference/equity-stake-methods     # Methods
```

---

## Migration SQL Scripts Needed

1. `create_assets_tables.sql` - Create all tables
2. `insert_assets_reference_data.sql` - Populate codebooks
3. `insert_assets_test_data.sql` - Test investments & equity stakes

---

## Notes

- **ISIN**: International Securities Identification Number (global standard)
- **Nominal Value**: Face value of a security
- **Equity Stake**: Ownership percentage in another company
- **Emission**: Issuance of shares/bonds
- **Time Series**: Historical data tracked with `DT_PLATNOSTOD/DO` (valid from/to)
- **Parent Records**: `ID_PARENT` creates version history chain

