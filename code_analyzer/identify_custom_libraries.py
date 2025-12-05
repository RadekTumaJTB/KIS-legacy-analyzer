#!/usr/bin/env python3
"""
Identifikace custom knihoven v KIS aplikaci pomocí Neo4J a Qdrant.
"""

import os
import json
from typing import Dict, List, Set, Any
from neo4j import GraphDatabase
from qdrant_client import QdrantClient
from collections import defaultdict, Counter
from config import (
    NEO4J_URI, NEO4J_USER, NEO4J_PASSWORD,
    QDRANT_HOST, QDRANT_PORT
)

class CustomLibraryIdentifier:
    """Identifikuje custom (proprietární) knihovny v kódu."""

    def __init__(self):
        """Inicializace připojení k Neo4J a Qdrant."""
        self.neo4j_driver = GraphDatabase.driver(
            NEO4J_URI,
            auth=(NEO4J_USER, NEO4J_PASSWORD)
        )
        self.qdrant_client = QdrantClient(host=QDRANT_HOST, port=QDRANT_PORT)

        # Standardní balíčky, které NEJSOU custom knihovny
        self.standard_packages = {
            'java', 'javax', 'jakarta',
            'org.apache', 'org.springframework', 'org.hibernate',
            'com.google', 'com.fasterxml', 'org.slf4j',
            'org.junit', 'org.mockito', 'org.testng',
            'net.sf', 'org.xml', 'org.w3c',
            'sun', 'com.sun', 'jdk',
            'org.json', 'org.yaml', 'com.mysql',
            'org.postgresql', 'oracle.jdbc',
            'org.eclipse', 'org.jetbrains',
            'lombok', 'com.fasterxml.jackson'
        }

    def identify_custom_libraries(self) -> Dict[str, Any]:
        """Identifikuje custom knihovny z Neo4J."""
        print("\n🔍 Identifikace custom knihoven z Neo4J...")

        with self.neo4j_driver.session() as session:
            # Získat všechny package names z Java tříd
            query = """
            MATCH (c:JavaClass)
            WHERE c.package IS NOT NULL
            RETURN DISTINCT c.package as package, count(c) as class_count
            ORDER BY class_count DESC
            """
            result = session.run(query)

            all_packages = {}
            for record in result:
                package = record['package']
                count = record['class_count']
                all_packages[package] = count

        # Filtrovat custom balíčky
        custom_packages = {}
        for package, count in all_packages.items():
            if self._is_custom_package(package):
                custom_packages[package] = count

        print(f"✅ Nalezeno {len(custom_packages)} custom balíčků z {len(all_packages)} celkem")

        return {
            'total_packages': len(all_packages),
            'custom_packages': len(custom_packages),
            'packages': custom_packages,
            'all_packages': all_packages
        }

    def _is_custom_package(self, package: str) -> bool:
        """Zkontroluje, zda je balíček custom (ne standardní)."""
        # Ignorovat prázdné balíčky
        if not package or package.strip() == '':
            return False

        # Zkontrolovat proti známým standardním prefixům
        for std_prefix in self.standard_packages:
            if package.startswith(std_prefix):
                return False

        return True

    def analyze_custom_library_usage(self, custom_packages: Dict[str, int]) -> Dict[str, Any]:
        """Analyzuje použití custom knihoven v kódu."""
        print("\n📊 Analýza použití custom knihoven...")

        library_analysis = {}

        with self.neo4j_driver.session() as session:
            for package, class_count in list(custom_packages.items())[:20]:  # Top 20
                print(f"  Analyzuji: {package} ({class_count} tříd)...")

                # Získat všechny třídy v tomto balíčku
                query = """
                MATCH (c:JavaClass)
                WHERE c.package = $package
                RETURN c.name as class_name, c.file_path as file_path
                LIMIT 50
                """
                result = session.run(query, package=package)

                classes = []
                for record in result:
                    classes.append({
                        'name': record['class_name'],
                        'file_path': record.get('file_path', 'N/A')
                    })

                # Spočítat závislosti
                dep_query = """
                MATCH (c:JavaClass)-[r]->(target)
                WHERE c.package = $package
                RETURN type(r) as rel_type, count(r) as count
                """
                dep_result = session.run(dep_query, package=package)

                dependencies = {}
                for record in dep_result:
                    dependencies[record['rel_type']] = record['count']

                library_analysis[package] = {
                    'class_count': class_count,
                    'classes': classes,
                    'dependencies': dependencies,
                    'total_dependencies': sum(dependencies.values())
                }

        return library_analysis

    def search_java7_features_in_custom_libs(self, custom_packages: Dict[str, int]) -> Dict[str, Any]:
        """Vyhledá Java 7 specifické funkce v custom knihovnách pomocí Qdrant."""
        print("\n🔎 Vyhledávání Java 7 funkcí v custom knihovnách...")

        # Java 7 specifické konstrukce, které by mohly být problematické
        java7_patterns = [
            "java.util.Date",
            "SimpleDateFormat",
            "Calendar.getInstance",
            "Thread.stop",
            "Thread.suspend",
            "Vector",
            "Hashtable",
            "Stack",
            "Properties",
            "java.security.acl",
            "javax.activation",
            "javax.xml.bind",
            "javax.annotation",
            "java.corba"
        ]

        results = {}

        for pattern in java7_patterns[:5]:  # Top 5 vzorů
            try:
                # Vyhledat v Qdrant
                search_results = self.qdrant_client.scroll(
                    collection_name="code_chunks",
                    scroll_filter={
                        "must": [
                            {
                                "key": "content",
                                "match": {
                                    "text": pattern
                                }
                            }
                        ]
                    },
                    limit=10
                )

                if search_results and search_results[0]:
                    matches = []
                    for point in search_results[0]:
                        payload = point.payload
                        matches.append({
                            'file': payload.get('file_path', 'N/A'),
                            'chunk': payload.get('content', '')[:200]
                        })

                    if matches:
                        results[pattern] = {
                            'count': len(matches),
                            'matches': matches
                        }
                        print(f"  ✓ {pattern}: {len(matches)} výskytů")
            except Exception as e:
                print(f"  ⚠ Chyba při vyhledávání '{pattern}': {e}")

        return results

    def estimate_migration_impact(self, library_analysis: Dict[str, Any]) -> Dict[str, Any]:
        """Odhadne dopad migrace custom knihoven na Java 17."""

        total_classes = sum(lib['class_count'] for lib in library_analysis.values())
        total_dependencies = sum(lib['total_dependencies'] for lib in library_analysis.values())

        # Odhadnout úsilí (hodin na třídu)
        hours_per_class_manual = 4  # Průměr: analýza + úprava + test
        hours_per_class_ai = 1.2    # S AI: 70% rychlejší

        manual_hours = total_classes * hours_per_class_manual
        ai_hours = total_classes * hours_per_class_ai

        return {
            'total_custom_classes': total_classes,
            'total_dependencies': total_dependencies,
            'estimated_effort': {
                'manual': {
                    'hours': manual_hours,
                    'days': manual_hours / 8,
                    'weeks': manual_hours / 40
                },
                'with_ai': {
                    'hours': ai_hours,
                    'days': ai_hours / 8,
                    'weeks': ai_hours / 40
                },
                'savings': {
                    'hours': manual_hours - ai_hours,
                    'percentage': ((manual_hours - ai_hours) / manual_hours * 100)
                }
            }
        }

    def generate_report(self, output_file: str = 'custom_libraries_report.json'):
        """Vygeneruje kompletní report o custom knihovnách."""
        print("\n" + "="*70)
        print("🔬 ANALÝZA CUSTOM KNIHOVEN - KIS APLIKACE")
        print("="*70)

        # 1. Identifikace custom balíčků
        custom_lib_data = self.identify_custom_libraries()

        # 2. Analýza použití
        library_analysis = self.analyze_custom_library_usage(custom_lib_data['packages'])

        # 3. Vyhledání Java 7 funkcí
        java7_features = self.search_java7_features_in_custom_libs(custom_lib_data['packages'])

        # 4. Odhad dopadu migrace
        migration_impact = self.estimate_migration_impact(library_analysis)

        # Kompletní report
        report = {
            'summary': custom_lib_data,
            'library_analysis': library_analysis,
            'java7_features': java7_features,
            'migration_impact': migration_impact
        }

        # Uložit JSON
        with open(output_file, 'w', encoding='utf-8') as f:
            json.dump(report, f, indent=2, ensure_ascii=False)

        print(f"\n✅ Report uložen: {output_file}")

        # Vypsat souhrn
        print("\n" + "="*70)
        print("📊 SOUHRN CUSTOM KNIHOVEN")
        print("="*70)

        print(f"\n📦 Celkem balíčků: {custom_lib_data['total_packages']}")
        print(f"🔧 Custom balíčků: {custom_lib_data['custom_packages']}")

        print("\n🏆 TOP 10 CUSTOM BALÍČKŮ:")
        top_packages = sorted(
            custom_lib_data['packages'].items(),
            key=lambda x: x[1],
            reverse=True
        )[:10]

        for i, (package, count) in enumerate(top_packages, 1):
            deps = library_analysis.get(package, {}).get('total_dependencies', 0)
            print(f"  {i:2}. {package:50} - {count:3} tříd, {deps:4} závislostí")

        print(f"\n⏱ ODHAD MIGRAČNÍHO ÚSILÍ:")
        effort = migration_impact['estimated_effort']
        print(f"  Custom tříd celkem: {migration_impact['total_custom_classes']}")
        print(f"  Manuální: {effort['manual']['weeks']:.1f} týdnů ({effort['manual']['hours']:.0f} hodin)")
        print(f"  S AI:      {effort['with_ai']['weeks']:.1f} týdnů ({effort['with_ai']['hours']:.0f} hodin)")
        print(f"  Úspora:    {effort['savings']['percentage']:.0f}% ({effort['savings']['hours']:.0f} hodin)")

        if java7_features:
            print(f"\n⚠️ NALEZENÉ JAVA 7 FUNKCE:")
            for pattern, data in java7_features.items():
                print(f"  - {pattern}: {data['count']} výskytů")

        print("\n" + "="*70)

        return report

    def close(self):
        """Uzavře připojení."""
        self.neo4j_driver.close()


if __name__ == "__main__":
    identifier = CustomLibraryIdentifier()

    try:
        report = identifier.generate_report('custom_libraries_report.json')
        print("\n✅ Analýza dokončena!")
    except Exception as e:
        print(f"\n❌ Chyba při analýze: {e}")
        import traceback
        traceback.print_exc()
    finally:
        identifier.close()
