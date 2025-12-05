#!/usr/bin/env python3
"""
Analýza migrace KIS Banking Application z Windows Server 2003 na Windows Server 2008 (32-bit).

Identifikuje:
1. Java 1.4 deprecated API usage
2. Windows 2003-specific API usage
3. UAC (User Account Control) kompatibilitu
4. File system virtualization problémy
5. Registry virtualization problémy
6. Windows Services API změny
"""

import json
from typing import Dict, List, Any, Set
from collections import defaultdict
import re

from neo4j import GraphDatabase
from qdrant_client import QdrantClient

from config import (
    NEO4J_URI, NEO4J_USER, NEO4J_PASSWORD,
    QDRANT_HOST, QDRANT_PORT,
    QDRANT_COLLECTION_NAME
)


class Win2003To2008Analyzer:
    """Analyzuje Windows Server 2003 → 2008 migraci pro Java 1.4 aplikaci."""

    def __init__(self):
        print("🔌 Připojuji se k Neo4j a Qdrant...")
        self.neo4j_driver = GraphDatabase.driver(
            NEO4J_URI,
            auth=(NEO4J_USER, NEO4J_PASSWORD)
        )
        self.qdrant_client = QdrantClient(host=QDRANT_HOST, port=QDRANT_PORT)

        # Windows 2003 → 2008 migration patterns
        self.migration_patterns = {
            # Java 1.4 deprecated APIs
            'java14_deprecated': [
                r'sun\.misc\.BASE64Encoder',  # Deprecated, use javax.xml.bind.DatatypeConverter
                r'sun\.misc\.BASE64Decoder',
                r'com\.sun\.image\.codec\.jpeg',  # Removed in Java 7
                r'Thread\.stop\(',  # Deprecated since 1.2, problematic
                r'Thread\.suspend\(',
                r'Thread\.resume\(',
                r'Runtime\.runFinalizersOnExit',
                r'System\.runFinalizersOnExit',
                r'SecurityManager\.checkMemberAccess',
            ],

            # Windows Registry - UAC virtualization
            'registry_access': [
                r'HKEY_LOCAL_MACHINE\\\\SOFTWARE',  # Může být virtualizováno UAC
                r'Advapi32',
                r'RegOpenKey',
                r'RegCreateKey',
                r'RegSetValue',
                r'RegQueryValue',
            ],

            # File system - UAC virtualization
            'filesystem_protected': [
                r'C:\\\\Windows\\\\',  # Protected by UAC
                r'C:\\\\Program Files\\\\',  # Protected by UAC
                r'%ProgramFiles%',
                r'System32',
                r'C:\\\\WINNT',  # Windows 2003 path
            ],

            # Windows Services
            'windows_services': [
                r'CreateService',
                r'OpenService',
                r'StartService',
                r'ControlService',
                r'SERVICE_AUTO_START',
                r'SERVICE_DEMAND_START',
                r'sc\.exe',  # Service control command
            ],

            # Security/Permissions - impacted by UAC
            'security_permissions': [
                r'SeDebugPrivilege',
                r'SeTcbPrivilege',
                r'SeBackupPrivilege',
                r'SeRestorePrivilege',
                r'AdjustTokenPrivileges',
                r'SetSecurityDescriptor',
            ],

            # Process management
            'process_management': [
                r'CreateProcess',
                r'ShellExecute',
                r'WinExec',
                r'Runtime\.getRuntime\(\)\.exec',
                r'ProcessBuilder',
            ],

            # Network/IPC
            'network_ipc': [
                r'NetBIOS',
                r'Named Pipes',
                r'\\\\\\\\\.\\\\pipe\\\\',  # Named pipe path
                r'Mailslot',
            ],

            # COM/DCOM
            'com_dcom': [
                r'CoInitialize',
                r'CoCreateInstance',
                r'CLSID',
                r'ProgID',
                r'IDispatch',
                r'jacob',  # Java-COM bridge
            ],

            # .NET Framework version dependencies
            'dotnet_framework': [
                r'\.NET Framework 1\.1',  # 2003 default
                r'\.NET Framework 2\.0',  # 2008 default
                r'mscorlib\.dll',
                r'System\.dll',
            ],

            # File paths encoding
            'file_encoding': [
                r'windows-1250',
                r'cp1250',
                r'Cp1250',
                r'ISO-8859-2',
            ],
        }

    def close(self):
        """Zavře připojení."""
        self.neo4j_driver.close()

    def analyze_neo4j_structure(self) -> Dict[str, Any]:
        """Analyzuje strukturu aplikace v Neo4j."""
        print("\n📊 Analyzuji strukturu aplikace v Neo4j...")

        with self.neo4j_driver.session() as session:
            # Celkový počet tříd
            result = session.run("MATCH (c:JavaClass) RETURN count(c) as total")
            total_classes = result.single()['total']

            # Java packages
            result = session.run("""
                MATCH (c:JavaClass)
                WHERE c.package IS NOT NULL
                RETURN c.package as package, count(c) as count
                ORDER BY count DESC
                LIMIT 50
            """)
            packages = {rec['package']: rec['count'] for rec in result}

            # Sun/deprecated API usage
            result = session.run("""
                MATCH (c:JavaClass)-[:IMPORTS]->(dep:JavaClass)
                WHERE dep.package STARTS WITH 'sun.'
                   OR dep.package STARTS WITH 'com.sun.'
                RETURN dep.package as package, dep.name as className, count(c) as usage_count
                ORDER BY usage_count DESC
            """)
            sun_api_usage = [
                {'package': rec['package'], 'class': rec['className'], 'count': rec['usage_count']}
                for rec in result
            ]

            # Native methods
            result = session.run("""
                MATCH (c:JavaClass)
                WHERE c.hasNativeMethods = true
                RETURN c.name as className, c.package as package, c.file_path as filePath
            """)
            native_classes = [
                {'class': rec['className'], 'package': rec['package'], 'file_path': rec['filePath']}
                for rec in result
            ]

        return {
            'total_classes': total_classes,
            'top_packages': packages,
            'sun_api_usage': sun_api_usage,
            'native_classes': native_classes,
        }

    def search_qdrant_for_patterns(self, category: str, patterns: List[str]) -> List[Dict[str, Any]]:
        """Vyhledá migration patterns v Qdrant pomocí scroll."""
        print(f"  🔍 Hledám {category} patterns...")

        findings = []

        # Get total number of points
        collection_info = self.qdrant_client.get_collection(QDRANT_COLLECTION_NAME)
        total_points = collection_info.points_count

        print(f"    Celkem {total_points} chunks v kolekci")

        # Scroll through all points
        offset = None
        batch_size = 100
        processed = 0

        while True:
            # Scroll through points
            records, next_offset = self.qdrant_client.scroll(
                collection_name=QDRANT_COLLECTION_NAME,
                limit=batch_size,
                offset=offset,
                with_payload=True,
                with_vectors=False
            )

            if not records:
                break

            # Check each record for patterns
            for record in records:
                payload = record.payload
                content = payload.get('content', '')

                # Check all patterns
                for pattern in patterns:
                    if re.search(pattern, content, re.IGNORECASE):
                        findings.append({
                            'category': category,
                            'pattern': pattern,
                            'file_path': payload.get('file_path', 'unknown'),
                            'chunk_index': payload.get('chunk_index', 0),
                            'content_preview': content[:200],
                            'match': re.search(pattern, content, re.IGNORECASE).group(0)
                        })
                        break  # One match per chunk is enough

            processed += len(records)
            print(f"    Zpracováno {processed}/{total_points} chunks...", end='\r')

            # Check if we're done
            if next_offset is None:
                break

            offset = next_offset

        print(f"    ✅ Nalezeno {len(findings)} výskytů v kategorii {category}")
        return findings

    def analyze_migration_issues(self) -> Dict[str, Any]:
        """Analyzuje všechny migrační problémy."""
        print("\n🔍 Analyzuji Windows 2003 → 2008 migrační problémy...")

        all_findings = {}
        total_findings = 0

        for category, patterns in self.migration_patterns.items():
            findings = self.search_qdrant_for_patterns(category, patterns)
            all_findings[category] = findings
            total_findings += len(findings)

        print(f"\n✅ Celkem nalezeno {total_findings} potenciálních problémů")

        return all_findings

    def generate_migration_report(self, neo4j_data: Dict, findings: Dict) -> Dict[str, Any]:
        """Generuje kompletní migrační report."""
        print("\n📝 Generuji migrační report...")

        # Aggregate findings by category
        category_summary = {}
        critical_files = set()
        high_risk_files = set()

        for category, items in findings.items():
            category_summary[category] = {
                'count': len(items),
                'files': list(set(item['file_path'] for item in items)),
                'patterns_found': list(set(item['pattern'] for item in items)),
            }

            # Mark critical and high-risk files
            if category in ['java14_deprecated', 'registry_access', 'filesystem_protected', 'security_permissions']:
                for item in items:
                    if category == 'java14_deprecated':
                        critical_files.add(item['file_path'])
                    else:
                        high_risk_files.add(item['file_path'])

        # Calculate migration complexity
        complexity_score = 0
        risk_factors = []

        # Java 1.4 deprecated APIs
        if findings['java14_deprecated']:
            complexity_score += 40
            risk_factors.append({
                'severity': 'VYSOKÉ',
                'category': 'Java 1.4 Deprecated APIs',
                'count': len(findings['java14_deprecated']),
                'description': '⚠️ Deprecated Java 1.4 API - může nefungovat na novějších JVM',
                'recommendation': 'Nahradit moderními alternativami před migrací'
            })

        # Registry access - UAC impact
        if findings['registry_access']:
            complexity_score += 35
            risk_factors.append({
                'severity': 'VYSOKÉ',
                'category': 'Registry Access',
                'count': len(findings['registry_access']),
                'description': '⚠️ Registry přístup - UAC virtualization na Win 2008',
                'recommendation': 'Testovat s UAC zapnutým, možná vyžaduje admin práva'
            })

        # Protected file system locations
        if findings['filesystem_protected']:
            complexity_score += 30
            risk_factors.append({
                'severity': 'STŘEDNÍ',
                'category': 'Protected File System',
                'count': len(findings['filesystem_protected']),
                'description': '⚠️ Zápis do chráněných složek - UAC virtualization',
                'recommendation': 'Přesunout data do %APPDATA% nebo %ProgramData%'
            })

        # Security permissions
        if findings['security_permissions']:
            complexity_score += 25
            risk_factors.append({
                'severity': 'STŘEDNÍ',
                'category': 'Security Permissions',
                'count': len(findings['security_permissions']),
                'description': '⚠️ Pokročilá oprávnění - může vyžadovat admin práva',
                'recommendation': 'Minimalizovat privilegované operace, testovat s UAC'
            })

        # Windows services
        if findings['windows_services']:
            complexity_score += 20
            risk_factors.append({
                'severity': 'NÍZKÉ',
                'category': 'Windows Services',
                'count': len(findings['windows_services']),
                'description': '✅ Windows služby - kompatibilní s Win 2008',
                'recommendation': 'Testovat start/stop/restart operace'
            })

        # COM/DCOM
        if findings['com_dcom']:
            complexity_score += 15
            risk_factors.append({
                'severity': 'NÍZKÉ',
                'category': 'COM/DCOM',
                'count': len(findings['com_dcom']),
                'description': '✅ COM/DCOM - kompatibilní s Win 2008',
                'recommendation': 'Ověřit DCOM security settings'
            })

        # Process management
        if findings['process_management']:
            complexity_score += 15
            risk_factors.append({
                'severity': 'NÍZKÉ',
                'category': 'Process Management',
                'count': len(findings['process_management']),
                'description': '✅ Spouštění procesů - testovat s UAC',
                'recommendation': 'Zajistit správné cesty k exekutabilním souborům'
            })

        # Encoding
        if findings['file_encoding']:
            complexity_score += 10
            risk_factors.append({
                'severity': 'NÍZKÉ',
                'category': 'File Encoding',
                'count': len(findings['file_encoding']),
                'description': '✅ Encoding - kompatibilní',
                'recommendation': 'Zachovat windows-1250 pro české znaky'
            })

        # Determine overall risk
        if complexity_score > 120:
            risk_level = "VYSOKÉ"
            risk_description = "Aplikace vyžaduje rozsáhlé úpravy kvůli deprecated Java API a UAC."
            verdict = "⚠️ VYŽADUJE ÚPRAVY"
        elif complexity_score > 60:
            risk_level = "STŘEDNÍ"
            risk_description = "Některé oblasti vyžadují úpravu kvůli UAC a deprecated API."
            verdict = "⚠️ MIGROVATELNÉ S ÚPRAVAMI"
        else:
            risk_level = "NÍZKÉ"
            risk_description = "Minimální problémy, většinou UAC testing."
            verdict = "✅ DOBŘE MIGROVATELNÉ"

        report = {
            'summary': {
                'source_platform': 'Windows Server 2003 (32-bit)',
                'target_platform': 'Windows Server 2008 (32-bit)',
                'java_version': 'Java 1.4',
                'total_classes': neo4j_data['total_classes'],
                'total_issues': sum(len(items) for items in findings.values()),
                'critical_files': len(critical_files),
                'high_risk_files': len(high_risk_files),
                'complexity_score': complexity_score,
                'risk_level': risk_level,
                'risk_description': risk_description,
                'verdict': verdict,
            },
            'category_breakdown': category_summary,
            'risk_factors': risk_factors,
            'critical_files': list(critical_files)[:50],
            'high_risk_files': list(high_risk_files)[:50],
            'sun_api_usage': neo4j_data['sun_api_usage'][:20],
            'native_classes': neo4j_data['native_classes'],
            'detailed_findings': {
                category: items[:10]  # Top 10 per category
                for category, items in findings.items()
            },
        }

        return report

    def save_report(self, report: Dict[str, Any], output_file: str = 'win2003_to_win2008_migration_report.json'):
        """Uloží report do JSON."""
        output_path = f"/Users/radektuma/DEV/KIS/code_analyzer/{output_file}"

        with open(output_path, 'w', encoding='utf-8') as f:
            json.dump(report, f, indent=2, ensure_ascii=False)

        print(f"\n✅ Report uložen do: {output_path}")

    def print_summary(self, report: Dict[str, Any]):
        """Vytiskne přehled reportu."""
        print("\n" + "="*80)
        print("📊 WINDOWS SERVER 2003 → 2008 MIGRATION ANALYSIS")
        print("="*80)

        summary = report['summary']
        print(f"\n🎯 Migrace:")
        print(f"  • Z: {summary['source_platform']}")
        print(f"  • Na: {summary['target_platform']}")
        print(f"  • Java: {summary['java_version']}")

        print(f"\n📈 Statistiky:")
        print(f"  • Celkem tříd: {summary['total_classes']}")
        print(f"  • Celkem problémů: {summary['total_issues']}")
        print(f"  • Kritické soubory: {summary['critical_files']}")
        print(f"  • Vysoké riziko soubory: {summary['high_risk_files']}")
        print(f"  • Complexity Score: {summary['complexity_score']}/200")

        print(f"\n⚖️ Risk Assessment:")
        print(f"  • Risk Level: {summary['risk_level']}")
        print(f"  • Verdict: {summary['verdict']}")
        print(f"  • {summary['risk_description']}")

        print(f"\n⚠️ Risk Faktory:")
        for factor in report['risk_factors']:
            print(f"\n  [{factor['severity']}] {factor['category']}: {factor['count']} výskytů")
            print(f"    {factor['description']}")
            print(f"    → {factor['recommendation']}")

        print(f"\n📂 Breakdown podle Kategorií:")
        for category, data in report['category_breakdown'].items():
            if data['count'] > 0:
                print(f"\n  {category.upper()}: {data['count']} výskytů")
                print(f"    Soubory: {len(data['files'])}")

        if report['sun_api_usage']:
            print(f"\n☀️ Sun/Deprecated API Usage:")
            for api in report['sun_api_usage'][:5]:
                print(f"  • {api['package']}.{api['class']}: {api['count']} použití")

        if report['native_classes']:
            print(f"\n🔧 Native Classes: {len(report['native_classes'])}")
            for nc in report['native_classes'][:5]:
                print(f"  • {nc['package']}.{nc['class']}")

        print("\n" + "="*80)


def main():
    """Hlavní funkce."""
    print("🚀 Windows Server 2003 → 2008 Migration Analyzer (Java 1.4)")
    print("="*80)

    analyzer = Win2003To2008Analyzer()

    try:
        # 1. Analyzuj Neo4j strukturu
        neo4j_data = analyzer.analyze_neo4j_structure()

        # 2. Analyzuj migrační problémy v Qdrant
        findings = analyzer.analyze_migration_issues()

        # 3. Vygeneruj report
        report = analyzer.generate_migration_report(neo4j_data, findings)

        # 4. Ulož report
        analyzer.save_report(report)

        # 5. Vytiskni summary
        analyzer.print_summary(report)

    finally:
        analyzer.close()


if __name__ == "__main__":
    main()
