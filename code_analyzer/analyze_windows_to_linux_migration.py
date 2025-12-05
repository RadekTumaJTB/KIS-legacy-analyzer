#!/usr/bin/env python3
"""
Analýza migrace KIS Banking Application z Windows Server 2023 na Linux (ubi-minimal 10).

Identifikuje:
1. Windows-specific závislosti (Registry, COM, ActiveX, .dll)
2. OS-dependent kód (file paths, line endings)
3. 32-bit vs 64-bit problémy (JNI, native libraries)
4. Encoding problémy (Windows-1250 vs UTF-8)
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


class WindowsToLinuxAnalyzer:
    """Analyzuje Windows-specific závislosti pro migraci na Linux."""

    def __init__(self):
        print("🔌 Připojuji se k Neo4j a Qdrant...")
        self.neo4j_driver = GraphDatabase.driver(
            NEO4J_URI,
            auth=(NEO4J_USER, NEO4J_PASSWORD)
        )
        self.qdrant_client = QdrantClient(host=QDRANT_HOST, port=QDRANT_PORT)

        # Windows-specific patterns
        self.windows_patterns = {
            'registry': [
                r'Advapi32',
                r'RegOpenKey',
                r'RegQueryValue',
                r'RegSetValue',
                r'HKEY_LOCAL_MACHINE',
                r'HKEY_CURRENT_USER',
                r'WinReg',
            ],
            'com_activex': [
                r'CoCreateInstance',
                r'IDispatch',
                r'IUnknown',
                r'CLSID',
                r'ActiveX',
                r'OLE',
                r'jacob',  # Java-COM bridge
            ],
            'native_libs': [
                r'\.dll',
                r'kernel32',
                r'user32',
                r'gdi32',
                r'shell32',
                r'LoadLibrary',
                r'System\.load.*\.dll',
            ],
            'file_paths': [
                r'[A-Z]:\\\\',  # C:\, D:\
                r'\\\\\\\\',  # UNC paths \\server\share
                r'File\.separator',
                r'File\.pathSeparator',
                r'System\.getProperty\("file\.separator"\)',
            ],
            'line_endings': [
                r'\\r\\n',  # Windows line ending
                r'System\.getProperty\("line\.separator"\)',
            ],
            'jni_native': [
                r'System\.loadLibrary',
                r'native\s+\w+\s*\(',  # native methods
                r'JNI',
                r'\.so$',
                r'\.dll$',
            ],
            'encoding': [
                r'windows-1250',
                r'cp1250',
                r'Cp1250',
                r'ISO-8859-2',
                r'Charset\.forName\("windows',
            ],
            'windows_services': [
                r'SERVICE_',
                r'StartService',
                r'ControlService',
                r'CreateService',
            ],
            'process_management': [
                r'Runtime\.getRuntime\(\)\.exec',
                r'ProcessBuilder',
                r'cmd\.exe',
                r'\.bat',
                r'\.cmd',
            ]
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

            # Třídy podle balíčků
            result = session.run("""
                MATCH (c:JavaClass)
                WHERE c.package IS NOT NULL
                RETURN c.package as package, count(c) as count
                ORDER BY count DESC
                LIMIT 50
            """)
            packages = {rec['package']: rec['count'] for rec in result}

            # Třídy s native metodami
            result = session.run("""
                MATCH (c:JavaClass)
                WHERE c.hasNativeMethods = true
                RETURN c.name as className, c.package as package
            """)
            native_classes = [
                {'class': rec['className'], 'package': rec['package']}
                for rec in result
            ]

            # Závislosti na externích knihovnách
            result = session.run("""
                MATCH (c:JavaClass)-[:IMPORTS]->(ext:JavaClass)
                WHERE ext.package STARTS WITH 'com.sun'
                   OR ext.package STARTS WITH 'sun.'
                RETURN DISTINCT ext.package as package, count(c) as usage_count
                ORDER BY usage_count DESC
            """)
            sun_dependencies = {rec['package']: rec['usage_count'] for rec in result}

        return {
            'total_classes': total_classes,
            'top_packages': packages,
            'native_classes': native_classes,
            'sun_dependencies': sun_dependencies,
        }

    def search_qdrant_for_patterns(self, category: str, patterns: List[str]) -> List[Dict[str, Any]]:
        """Vyhledá Windows-specific patterns v Qdrant pomocí scroll."""
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

    def analyze_windows_dependencies(self) -> Dict[str, Any]:
        """Analyzuje všechny Windows-specific závislosti."""
        print("\n🔍 Analyzuji Windows-specific závislosti v kódu...")

        all_findings = {}
        total_findings = 0

        for category, patterns in self.windows_patterns.items():
            findings = self.search_qdrant_for_patterns(category, patterns)
            all_findings[category] = findings
            total_findings += len(findings)

        print(f"\n✅ Celkem nalezeno {total_findings} Windows-specific výskytů")

        return all_findings

    def analyze_file_system_usage(self) -> Dict[str, Any]:
        """Analyzuje použití file systému."""
        print("\n📁 Analyzuji file system usage...")

        with self.neo4j_driver.session() as session:
            # Hledej třídy používající java.io.File
            result = session.run("""
                MATCH (c:JavaClass)-[:IMPORTS]->(f:JavaClass)
                WHERE f.name = 'File' AND f.package = 'java.io'
                RETURN c.name as className, c.package as package, c.file_path as filePath
            """)

            file_classes = [
                {
                    'class': rec['className'],
                    'package': rec['package'],
                    'file_path': rec['filePath']
                }
                for rec in result
            ]

        return {
            'classes_using_file_io': len(file_classes),
            'file_classes': file_classes[:50],  # Top 50
        }

    def generate_migration_report(self, neo4j_data: Dict, findings: Dict, filesystem_data: Dict) -> Dict[str, Any]:
        """Generuje kompletní migrační report."""
        print("\n📝 Generuji migrační report...")

        # Aggregate findings by category
        category_summary = {}
        critical_files = set()

        for category, items in findings.items():
            category_summary[category] = {
                'count': len(items),
                'files': list(set(item['file_path'] for item in items)),
                'patterns_found': list(set(item['pattern'] for item in items)),
            }

            # Mark critical files
            for item in items:
                if category in ['registry', 'com_activex', 'native_libs', 'windows_services']:
                    critical_files.add(item['file_path'])

        # Calculate migration complexity
        complexity_score = 0
        complexity_factors = []

        if findings['registry']:
            complexity_score += 50
            complexity_factors.append("❌ Registry access - vyžaduje kompletní přepis")

        if findings['com_activex']:
            complexity_score += 80
            complexity_factors.append("❌ COM/ActiveX - nelze portovat na Linux")

        if findings['native_libs']:
            complexity_score += 60
            complexity_factors.append("⚠️ Native libraries (.dll) - vyžadují Linux ekvivalent (.so)")

        if findings['jni_native']:
            complexity_score += 40
            complexity_factors.append("⚠️ JNI kód - vyžaduje rekompilaci pro Linux")

        if findings['file_paths']:
            complexity_score += 20
            complexity_factors.append("✅ File paths - jednoduché opravy")

        if findings['encoding']:
            complexity_score += 15
            complexity_factors.append("✅ Encoding - migrace na UTF-8")

        if findings['process_management']:
            complexity_score += 30
            complexity_factors.append("⚠️ Process management - cmd.exe → bash")

        # Determine migration risk
        if complexity_score > 150:
            risk_level = "KRITICKÉ"
            risk_description = "Aplikace je silně vázána na Windows. Vyžaduje rozsáhlý refactoring."
        elif complexity_score > 80:
            risk_level = "VYSOKÉ"
            risk_description = "Významné Windows závislosti. Vyžaduje středně rozsáhlou migraci."
        elif complexity_score > 40:
            risk_level = "STŘEDNÍ"
            risk_description = "Některé Windows závislosti. Vyžaduje cílenou migraci."
        else:
            risk_level = "NÍZKÉ"
            risk_description = "Minimální Windows závislosti. Relativně snadná migrace."

        report = {
            'summary': {
                'total_classes': neo4j_data['total_classes'],
                'total_windows_specific_findings': sum(len(items) for items in findings.values()),
                'critical_files_count': len(critical_files),
                'complexity_score': complexity_score,
                'risk_level': risk_level,
                'risk_description': risk_description,
            },
            'category_breakdown': category_summary,
            'complexity_factors': complexity_factors,
            'critical_files': list(critical_files)[:100],  # Top 100
            'native_classes': neo4j_data['native_classes'],
            'sun_dependencies': neo4j_data['sun_dependencies'],
            'filesystem_usage': filesystem_data,
            'detailed_findings': {
                category: items[:20]  # Top 20 per category
                for category, items in findings.items()
            },
        }

        return report

    def save_report(self, report: Dict[str, Any], output_file: str = 'windows_to_linux_migration_report.json'):
        """Uloží report do JSON."""
        output_path = f"/Users/radektuma/DEV/KIS/code_analyzer/{output_file}"

        with open(output_path, 'w', encoding='utf-8') as f:
            json.dump(report, f, indent=2, ensure_ascii=False)

        print(f"\n✅ Report uložen do: {output_path}")

    def print_summary(self, report: Dict[str, Any]):
        """Vytiskne přehled reportu."""
        print("\n" + "="*80)
        print("📊 WINDOWS → LINUX MIGRATION ANALYSIS SUMMARY")
        print("="*80)

        summary = report['summary']
        print(f"\n🎯 Celkové Statistiky:")
        print(f"  • Celkem tříd: {summary['total_classes']}")
        print(f"  • Windows-specific výskyty: {summary['total_windows_specific_findings']}")
        print(f"  • Kritické soubory: {summary['critical_files_count']}")
        print(f"  • Complexity Score: {summary['complexity_score']}/300")
        print(f"  • Risk Level: {summary['risk_level']}")
        print(f"  • {summary['risk_description']}")

        print(f"\n⚠️ Complexity Faktory:")
        for factor in report['complexity_factors']:
            print(f"  {factor}")

        print(f"\n📂 Breakdown podle Kategorií:")
        for category, data in report['category_breakdown'].items():
            if data['count'] > 0:
                print(f"\n  {category.upper()}: {data['count']} výskytů")
                print(f"    Soubory: {len(data['files'])}")
                print(f"    Patterns: {', '.join(data['patterns_found'][:5])}")

        if report['native_classes']:
            print(f"\n🔧 Native Classes: {len(report['native_classes'])}")
            for nc in report['native_classes'][:5]:
                print(f"  • {nc['package']}.{nc['class']}")

        if report['sun_dependencies']:
            print(f"\n☀️ Sun/Oracle Dependencies:")
            for pkg, count in list(report['sun_dependencies'].items())[:5]:
                print(f"  • {pkg}: {count} použití")

        print("\n" + "="*80)


def main():
    """Hlavní funkce."""
    print("🚀 Windows → Linux Migration Analyzer")
    print("="*80)

    analyzer = WindowsToLinuxAnalyzer()

    try:
        # 1. Analyzuj Neo4j strukturu
        neo4j_data = analyzer.analyze_neo4j_structure()

        # 2. Analyzuj Windows závislosti v Qdrant
        findings = analyzer.analyze_windows_dependencies()

        # 3. Analyzuj file system usage
        filesystem_data = analyzer.analyze_file_system_usage()

        # 4. Vygeneruj report
        report = analyzer.generate_migration_report(neo4j_data, findings, filesystem_data)

        # 5. Ulož report
        analyzer.save_report(report)

        # 6. Vytiskni summary
        analyzer.print_summary(report)

    finally:
        analyzer.close()


if __name__ == "__main__":
    main()
