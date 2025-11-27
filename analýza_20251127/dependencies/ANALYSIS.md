# Analýza závislostí

## Třídy s vysokou vazbou

Třídy s více než 10 závislostmi mohou být obtížně udržovatelné:

| Třída | Počet závislostí | Typy cílů |
|-------|------------------|------------|
| ExcelThread | 133 | java_class, java_method |
| UcSkupModuleImpl | 50 | java_class |
| DokumentModuleImpl | 49 | java_method, java_class |
| PbModuleImpl | 40 | java_class |
| IfrsModuleImpl | 32 | java_class |
| EviModuleImpl | 24 | java_class |
| BudgetModuleImpl | 24 | java_class |
| ProjektModuleImpl | 22 | java_class |
| MajetekModuleImpl | 20 | java_class |
| CartesisModuleImpl | 17 | java_method, java_class |
| SubkonsModuleImpl | 16 | java_class |
| GenerateAll | 15 | java_method, java_class, java_interface |
| GeneratorThread | 11 | java_method, java_class, java_interface |

## Kruhové závislosti

✅ Žádné kruhové závislosti nenalezeny.

## Doporučení

1. **Vysoká vazba:** Zvažte refaktoring tříd s vysokým počtem závislostí
2. **Kruhové závislosti:** Odstraňte kruhové závislosti pomocí dependency injection nebo interface extraction
3. **Modularizace:** Rozdělte velké komponenty na menší, lépe udržovatelné moduly
