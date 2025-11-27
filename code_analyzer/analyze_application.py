"""Kompletní analýza aplikace KIS z Neo4J a Qdrant databází."""
import json
import os
from datetime import datetime
from collections import defaultdict
from typing import Dict, List, Tuple

from neo4j import GraphDatabase
from qdrant_client import QdrantClient
from config import (
    NEO4J_URI, NEO4J_USER, NEO4J_PASSWORD,
    QDRANT_HOST, QDRANT_PORT, QDRANT_COLLECTION_NAME
)


class ApplicationAnalyzer:
    """Analyzér pro kompletní analýzu aplikace."""

    def __init__(self, output_dir: str):
        """Initialize analyzer."""
        self.output_dir = output_dir
        os.makedirs(output_dir, exist_ok=True)

        # Database connections
        self.neo4j_driver = None
        self.qdrant_client = None

        # Analysis results
        self.results = {
            'metadata': {
                'analysis_date': datetime.now().isoformat(),
                'analyzer_version': '1.0.0'
            },
            'database_stats': {},
            'jsp_pages': [],
            'java_classes': [],
            'processes': [],
            'components': [],
            'dependencies': {},
            'issues': []
        }

    def connect_databases(self):
        """Připojení k Neo4J a Qdrant databázím."""
        print("\n=== PŘIPOJOVÁNÍ K DATABÁZÍM ===\n")

        # Neo4J connection
        try:
            self.neo4j_driver = GraphDatabase.driver(
                NEO4J_URI,
                auth=(NEO4J_USER, NEO4J_PASSWORD)
            )
            # Test connection
            with self.neo4j_driver.session() as session:
                result = session.run("RETURN 1 as test").single()
                if result['test'] == 1:
                    print("✅ Neo4J připojení úspěšné")
                    self.results['database_stats']['neo4j'] = {'status': 'connected'}
        except Exception as e:
            print(f"❌ Neo4J chyba: {e}")
            self.results['database_stats']['neo4j'] = {'status': 'failed', 'error': str(e)}
            raise

        # Qdrant connection
        try:
            self.qdrant_client = QdrantClient(host=QDRANT_HOST, port=QDRANT_PORT)
            # Test connection
            collections = self.qdrant_client.get_collections()
            print(f"✅ Qdrant připojení úspěšné ({len(collections.collections)} kolekcí)")
            self.results['database_stats']['qdrant'] = {
                'status': 'connected',
                'collections': len(collections.collections)
            }
        except Exception as e:
            print(f"❌ Qdrant chyba: {e}")
            self.results['database_stats']['qdrant'] = {'status': 'failed', 'error': str(e)}
            raise

    def get_database_stats(self):
        """Získání základních statistik z databází."""
        print("\n=== STATISTIKY DATABÁZÍ ===\n")

        with self.neo4j_driver.session() as session:
            # Node counts
            result = session.run("""
                MATCH (n)
                RETURN n.chunk_type as type, count(*) as count
                ORDER BY count DESC
            """)
            node_stats = {record['type']: record['count'] for record in result}

            # Relationship counts
            result = session.run("""
                MATCH ()-[r:DEPENDS_ON]->()
                RETURN r.type as type, count(*) as count
                ORDER BY count DESC
            """)
            rel_stats = {record['type']: record['count'] for record in result}

            self.results['database_stats']['neo4j_nodes'] = node_stats
            self.results['database_stats']['neo4j_relationships'] = rel_stats

            print("Neo4J Uzly:")
            for node_type, count in node_stats.items():
                print(f"  {node_type:20} → {count:,}")

            print("\nNeo4J Vztahy:")
            for rel_type, count in rel_stats.items():
                print(f"  {rel_type:20} → {count:,}")

        # Qdrant stats
        try:
            collection = self.qdrant_client.get_collection(QDRANT_COLLECTION_NAME)
            self.results['database_stats']['qdrant_points'] = collection.points_count
            print(f"\nQdrant body: {collection.points_count:,}")
        except Exception as e:
            print(f"⚠️  Qdrant kolekce nenalezena: {e}")

    def analyze_jsp_pages(self):
        """Analýza JSP stránek a jejich funkcí."""
        print("\n=== ANALÝZA JSP STRÁNEK ===\n")

        with self.neo4j_driver.session() as session:
            # Get all JSP pages with their dependencies
            result = session.run("""
                MATCH (jsp)
                WHERE jsp.chunk_type = 'jsp_page'
                OPTIONAL MATCH (jsp)-[r:DEPENDS_ON]->(target)
                WITH jsp,
                     count(r) as dep_count,
                     collect(DISTINCT r.type) as dep_types,
                     collect(DISTINCT target.chunk_type) as target_types
                RETURN jsp.name as page_name,
                       jsp.file_path as file_path,
                       jsp.chunk_id as chunk_id,
                       dep_count,
                       dep_types,
                       target_types
                ORDER BY dep_count DESC
            """)

            pages = []
            for record in result:
                page = {
                    'name': record['page_name'],
                    'file_path': record['file_path'],
                    'chunk_id': record['chunk_id'],
                    'dependency_count': record['dep_count'],
                    'dependency_types': record['dep_types'],
                    'target_types': record['target_types']
                }
                pages.append(page)

            self.results['jsp_pages'] = pages
            print(f"Nalezeno {len(pages)} JSP stránek")

            # Top 10 most complex pages
            print("\nNejkomplexnější stránky:")
            for page in pages[:10]:
                print(f"  {page['name']:30} → {page['dependency_count']:4} závislostí")

    def analyze_java_classes(self):
        """Analýza Java tříd a jejich komponent."""
        print("\n=== ANALÝZA JAVA TŘÍD ===\n")

        with self.neo4j_driver.session() as session:
            result = session.run("""
                MATCH (cls)
                WHERE cls.chunk_type = 'java_class'
                OPTIONAL MATCH (cls)-[r:DEPENDS_ON]->(target)
                WITH cls,
                     count(r) as dep_count,
                     collect(DISTINCT r.type) as dep_types
                RETURN cls.name as class_name,
                       cls.file_path as file_path,
                       dep_count,
                       dep_types
                ORDER BY dep_count DESC
                LIMIT 100
            """)

            classes = []
            for record in result:
                cls = {
                    'name': record['class_name'],
                    'file_path': record['file_path'],
                    'dependency_count': record['dep_count'],
                    'dependency_types': record['dep_types']
                }
                classes.append(cls)

            self.results['java_classes'] = classes
            print(f"Top 100 Java tříd (seřazeno podle závislostí)")

    def identify_processes(self):
        """Identifikace klíčových procesů aplikace."""
        print("\n=== IDENTIFIKACE PROCESŮ ===\n")

        with self.neo4j_driver.session() as session:
            # Hledání JSP flow (page -> page includes/calls)
            result = session.run("""
                MATCH (source)-[r:DEPENDS_ON]->(target)
                WHERE source.chunk_type = 'jsp_page'
                  AND target.chunk_type = 'jsp_page'
                  AND r.type IN ['includes', 'calls', 'references']
                RETURN source.name as source_page,
                       target.name as target_page,
                       r.type as relation_type
                ORDER BY source.name
            """)

            processes = defaultdict(list)
            for record in result:
                source = record['source_page']
                target = record['target_page']
                rel_type = record['relation_type']

                processes[source].append({
                    'target': target,
                    'relation': rel_type
                })

            # Store as list
            process_list = []
            for source, targets in processes.items():
                process_list.append({
                    'entry_point': source,
                    'flow': targets,
                    'complexity': len(targets)
                })

            self.results['processes'] = process_list
            print(f"Identifikováno {len(process_list)} procesových flow")

            # Top processes
            sorted_processes = sorted(process_list, key=lambda x: x['complexity'], reverse=True)
            print("\nNejsložitější procesy:")
            for proc in sorted_processes[:10]:
                print(f"  {proc['entry_point']:30} → {proc['complexity']} kroků")

    def identify_components(self):
        """Identifikace klíčových komponent a modulů."""
        print("\n=== IDENTIFIKACE KOMPONENT ===\n")

        with self.neo4j_driver.session() as session:
            # Group by package/directory
            result = session.run("""
                MATCH (n)
                WHERE n.chunk_type = 'java_class'
                WITH n.file_path as path, count(*) as class_count
                RETURN path, class_count
                ORDER BY class_count DESC
                LIMIT 50
            """)

            components = []
            for record in result:
                path = record['path']
                # Extract package from path
                if path:
                    parts = path.split('/')
                    if len(parts) > 2:
                        component = '/'.join(parts[-3:-1])  # Last 2 dirs
                    else:
                        component = parts[0] if parts else 'unknown'
                else:
                    component = 'unknown'

                components.append({
                    'component': component,
                    'class_count': record['class_count'],
                    'path': path
                })

            self.results['components'] = components
            print(f"Identifikováno {len(components)} komponent")

    def analyze_dependencies(self):
        """Analýza kódových závislostí a identifikace problémů."""
        print("\n=== ANALÝZA ZÁVISLOSTÍ ===\n")

        with self.neo4j_driver.session() as session:
            # Circular dependencies
            result = session.run("""
                MATCH (a)-[r1:DEPENDS_ON]->(b)-[r2:DEPENDS_ON]->(a)
                WHERE a.chunk_type = 'java_class' AND b.chunk_type = 'java_class'
                RETURN a.name as class_a, b.name as class_b
                LIMIT 50
            """)

            circular_deps = []
            for record in result:
                circular_deps.append({
                    'class_a': record['class_a'],
                    'class_b': record['class_b'],
                    'issue': 'circular_dependency'
                })

            self.results['dependencies']['circular'] = circular_deps

            if circular_deps:
                print(f"⚠️  Nalezeno {len(circular_deps)} kruhových závislostí")
                for dep in circular_deps[:5]:
                    print(f"     {dep['class_a']} ↔ {dep['class_b']}")
            else:
                print("✅ Žádné kruhové závislosti")

            # High coupling (classes with many dependencies)
            result = session.run("""
                MATCH (cls)-[r:DEPENDS_ON]->(target)
                WHERE cls.chunk_type = 'java_class'
                WITH cls.name as class_name, count(r) as dep_count
                WHERE dep_count > 20
                RETURN class_name, dep_count
                ORDER BY dep_count DESC
                LIMIT 20
            """)

            high_coupling = []
            for record in result:
                high_coupling.append({
                    'class': record['class_name'],
                    'dependency_count': record['dep_count'],
                    'issue': 'high_coupling'
                })

            self.results['dependencies']['high_coupling'] = high_coupling

            if high_coupling:
                print(f"\n⚠️  {len(high_coupling)} tříd s vysokou vazbou (>20 závislostí)")
                for item in high_coupling[:5]:
                    print(f"     {item['class']:30} → {item['dependency_count']} závislostí")

    def generate_reports(self):
        """Generování výstupních reportů."""
        print("\n=== GENEROVÁNÍ REPORTŮ ===\n")

        # JSON report
        json_path = os.path.join(self.output_dir, 'analysis_report.json')
        with open(json_path, 'w', encoding='utf-8') as f:
            json.dump(self.results, f, indent=2, ensure_ascii=False)
        print(f"✅ JSON report: {json_path}")

        # Markdown summary
        md_path = os.path.join(self.output_dir, 'SUMMARY.md')
        with open(md_path, 'w', encoding='utf-8') as f:
            f.write("# Analýza aplikace KIS\n\n")
            f.write(f"**Datum analýzy:** {self.results['metadata']['analysis_date']}\n\n")

            f.write("## Přehled databází\n\n")
            f.write("### Neo4J uzly\n\n")
            for node_type, count in self.results['database_stats']['neo4j_nodes'].items():
                f.write(f"- **{node_type}**: {count:,}\n")

            f.write("\n### Neo4J vztahy\n\n")
            for rel_type, count in self.results['database_stats']['neo4j_relationships'].items():
                f.write(f"- **{rel_type}**: {count:,}\n")

            f.write(f"\n## JSP Stránky\n\n")
            f.write(f"Celkem nalezeno: **{len(self.results['jsp_pages'])}** stránek\n\n")
            f.write("### Top 20 nejkomplexnějších stránek\n\n")
            f.write("| Stránka | Počet závislostí | Typy závislostí |\n")
            f.write("|---------|------------------|------------------|\n")
            for page in self.results['jsp_pages'][:20]:
                deps = ', '.join(page['dependency_types']) if page['dependency_types'] else 'žádné'
                f.write(f"| {page['name']} | {page['dependency_count']} | {deps} |\n")

            f.write(f"\n## Java Třídy\n\n")
            f.write(f"Top 100 tříd analyzováno\n\n")

            f.write(f"\n## Procesy\n\n")
            f.write(f"Identifikováno: **{len(self.results['processes'])}** procesových flow\n\n")

            f.write(f"\n## Komponenty\n\n")
            f.write(f"Identifikováno: **{len(self.results['components'])}** komponent\n\n")

            f.write("\n## Problémy a rizika\n\n")

            if self.results['dependencies'].get('circular'):
                f.write(f"### ⚠️ Kruhové závislosti\n\n")
                f.write(f"Nalezeno: **{len(self.results['dependencies']['circular'])}** kruhových závislostí\n\n")

            if self.results['dependencies'].get('high_coupling'):
                f.write(f"### ⚠️ Vysoká vazba\n\n")
                f.write(f"Nalezeno: **{len(self.results['dependencies']['high_coupling'])}** tříd s vysokou vazbou\n\n")

        print(f"✅ Markdown summary: {md_path}")

    def run_analysis(self):
        """Spustit kompletní analýzu."""
        try:
            self.connect_databases()
            self.get_database_stats()
            self.analyze_jsp_pages()
            self.analyze_java_classes()
            self.identify_processes()
            self.identify_components()
            self.analyze_dependencies()
            self.generate_reports()

            print("\n" + "="*60)
            print("✅ ANALÝZA DOKONČENA")
            print("="*60)
            print(f"\nVýsledky uloženy v: {self.output_dir}")

        finally:
            if self.neo4j_driver:
                self.neo4j_driver.close()


if __name__ == "__main__":
    output_dir = "/Users/radektuma/DEV/KIS/analýza_20251127"
    analyzer = ApplicationAnalyzer(output_dir)
    analyzer.run_analysis()
