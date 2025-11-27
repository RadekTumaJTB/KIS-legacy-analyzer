"""Generování detailních dokumentací procesů, stránek a komponent."""
import os
import json
from neo4j import GraphDatabase
from qdrant_client import QdrantClient
from config import (
    NEO4J_URI, NEO4J_USER, NEO4J_PASSWORD,
    QDRANT_HOST, QDRANT_PORT, QDRANT_COLLECTION_NAME
)


class DetailedDocsGenerator:
    """Generátor detailní dokumentace aplikace."""

    def __init__(self, output_dir: str):
        """Initialize generator."""
        self.output_dir = output_dir
        self.driver = GraphDatabase.driver(NEO4J_URI, auth=(NEO4J_USER, NEO4J_PASSWORD))
        try:
            self.qdrant_client = QdrantClient(host=QDRANT_HOST, port=QDRANT_PORT)
        except:
            self.qdrant_client = None
            print("⚠️  Qdrant připojení selhalo, pokračuji bez něj")

    def generate_process_catalog(self):
        """Generování katalogu všech procesů."""
        print("\n=== GENEROVÁNÍ KATALOGU PROCESŮ ===\n")

        os.makedirs(os.path.join(self.output_dir, 'processes'), exist_ok=True)

        with self.driver.session() as session:
            # Get all JSP page flows
            result = session.run("""
                MATCH (source)-[r:DEPENDS_ON]->(target)
                WHERE source.chunk_type = 'jsp_page'
                  AND target.chunk_type = 'jsp_page'
                  AND r.type IN ['includes', 'calls']
                RETURN DISTINCT source.name as source_page
                ORDER BY source.name
            """)

            process_pages = [record['source_page'] for record in result]

        # Create process catalog
        catalog_path = os.path.join(self.output_dir, 'processes', 'CATALOG.md')
        with open(catalog_path, 'w', encoding='utf-8') as f:
            f.write("# Katalog procesů aplikace KIS\n\n")
            f.write(f"Celkem identifikováno: **{len(process_pages)}** procesových flow\n\n")

            f.write("## Procesy s návaznostmi\n\n")
            f.write("Tyto JSP stránky obsahují odkazy na další stránky, což naznačuje proces workflow:\n\n")

            for page in process_pages:
                f.write(f"- **{page}**\n")
                self._describe_process_flow(f, page)

        print(f"✅ Katalog procesů: {catalog_path}")

    def _describe_process_flow(self, file, jsp_name):
        """Popsat flow procesu."""
        with self.driver.session() as session:
            result = session.run("""
                MATCH (source)-[r:DEPENDS_ON]->(target)
                WHERE source.name = $jsp_name
                  AND source.chunk_type = 'jsp_page'
                  AND target.chunk_type = 'jsp_page'
                  AND r.type IN ['includes', 'calls']
                RETURN target.name as target_page, r.type as rel_type
            """, jsp_name=jsp_name)

            targets = list(result)
            if targets:
                for record in targets:
                    file.write(f"  → {record['rel_type']}: `{record['target_page']}`\n")
                file.write("\n")

    def generate_pages_catalog(self):
        """Generování katalogu všech stránek."""
        print("\n=== GENEROVÁNÍ KATALOGU STRÁNEK ===\n")

        os.makedirs(os.path.join(self.output_dir, 'pages'), exist_ok=True)

        with self.driver.session() as session:
            result = session.run("""
                MATCH (jsp)
                WHERE jsp.chunk_type = 'jsp_page'
                WITH jsp
                OPTIONAL MATCH (jsp)-[r:DEPENDS_ON]->(target)
                WITH jsp, count(r) as dep_count,
                     collect(DISTINCT target.chunk_type) as target_types
                RETURN jsp.name as page_name,
                       jsp.file_path as file_path,
                       dep_count,
                       target_types
                ORDER BY page_name
            """)

            pages = list(result)

        # Kategorize pages by name pattern
        categories = {
            'Document Management': [],
            'Budget': [],
            'Evidence': [],
            'Administration': [],
            'Process': [],
            'View/Edit': [],
            'Other': []
        }

        for record in pages:
            name = record['page_name']
            if 'Doc' in name or 'Doklad' in name:
                categories['Document Management'].append(record)
            elif 'Budget' in name:
                categories['Budget'].append(record)
            elif 'Evi' in name:
                categories['Evidence'].append(record)
            elif 'Admin' in name or 'Set' in name:
                categories['Administration'].append(record)
            elif 'Process' in name:
                categories['Process'].append(record)
            elif 'Edit' in name or 'View' in name:
                categories['View/Edit'].append(record)
            else:
                categories['Other'].append(record)

        # Create catalog
        catalog_path = os.path.join(self.output_dir, 'pages', 'CATALOG.md')
        with open(catalog_path, 'w', encoding='utf-8') as f:
            f.write("# Katalog stránek aplikace KIS\n\n")
            f.write(f"Celkem: **{len(pages)}** JSP stránek\n\n")

            for category, items in categories.items():
                if items:
                    f.write(f"## {category} ({len(items)} stránek)\n\n")

                    for record in items:
                        f.write(f"### {record['page_name']}\n\n")
                        f.write(f"- **Soubor:** `{record['file_path']}`\n")
                        f.write(f"- **Závislosti:** {record['dep_count']}\n")
                        if record['target_types']:
                            types = ', '.join(record['target_types'])
                            f.write(f"- **Typy cílů:** {types}\n")
                        f.write("\n")

        print(f"✅ Katalog stránek: {catalog_path}")

        # Summary by category
        summary_path = os.path.join(self.output_dir, 'pages', 'SUMMARY_BY_CATEGORY.md')
        with open(summary_path, 'w', encoding='utf-8') as f:
            f.write("# Souhrn stránek podle kategorií\n\n")

            for category, items in categories.items():
                f.write(f"## {category}\n\n")
                f.write(f"Počet stránek: **{len(items)}**\n\n")

                if items:
                    f.write("| Stránka | Počet závislostí |\n")
                    f.write("|---------|------------------|\n")
                    for record in items:
                        f.write(f"| {record['page_name']} | {record['dep_count']} |\n")
                    f.write("\n")

        print(f"✅ Souhrn kategorií: {summary_path}")

    def generate_components_catalog(self):
        """Generování katalogu komponent a modulů."""
        print("\n=== GENEROVÁNÍ KATALOGU KOMPONENT ===\n")

        os.makedirs(os.path.join(self.output_dir, 'components'), exist_ok=True)

        with self.driver.session() as session:
            # Group Java classes by package
            result = session.run("""
                MATCH (cls)
                WHERE cls.chunk_type = 'java_class'
                RETURN cls.name as class_name,
                       cls.file_path as file_path
                ORDER BY file_path, class_name
            """)

            classes = list(result)

        # Extract packages from file paths
        packages = {}
        for record in classes:
            path = record['file_path']
            if path:
                # Extract package from path (e.g., /path/to/com/example/MyClass.java -> com.example)
                parts = path.split('/')
                if 'JAVA' in parts:
                    java_idx = parts.index('JAVA')
                    if java_idx + 1 < len(parts):
                        # Take everything after JAVA and before filename
                        package_parts = parts[java_idx + 1:-1]
                        package = '.'.join(package_parts) if package_parts else 'root'
                    else:
                        package = 'root'
                else:
                    package = 'unknown'
            else:
                package = 'unknown'

            if package not in packages:
                packages[package] = []
            packages[package].append(record)

        # Create catalog
        catalog_path = os.path.join(self.output_dir, 'components', 'CATALOG.md')
        with open(catalog_path, 'w', encoding='utf-8') as f:
            f.write("# Katalog komponent a modulů\n\n")
            f.write(f"Celkem: **{len(classes)}** Java tříd\n")
            f.write(f"Celkem: **{len(packages)}** balíčků\n\n")

            f.write("## Balíčky a jejich třídy\n\n")

            for package, items in sorted(packages.items(), key=lambda x: len(x[1]), reverse=True)[:20]:
                f.write(f"### `{package}` ({len(items)} tříd)\n\n")

                for record in items[:10]:  # Top 10 classes per package
                    f.write(f"- **{record['class_name']}**\n")
                    f.write(f"  - Path: `{record['file_path']}`\n")

                if len(items) > 10:
                    f.write(f"- ... a dalších {len(items) - 10} tříd\n")

                f.write("\n")

        print(f"✅ Katalog komponent: {catalog_path}")

    def generate_dependencies_report(self):
        """Generování reportu závislostí."""
        print("\n=== GENEROVÁNÍ REPORTU ZÁVISLOSTÍ ===\n")

        os.makedirs(os.path.join(self.output_dir, 'dependencies'), exist_ok=True)

        with self.driver.session() as session:
            # High coupling classes
            high_coupling_result = session.run("""
                MATCH (cls)-[r:DEPENDS_ON]->(target)
                WHERE cls.chunk_type = 'java_class'
                WITH cls.name as class_name,
                     count(r) as dep_count,
                     collect(DISTINCT target.chunk_type) as target_types
                WHERE dep_count > 10
                RETURN class_name, dep_count, target_types
                ORDER BY dep_count DESC
                LIMIT 50
            """)
            high_coupling = list(high_coupling_result)

            # Circular dependencies
            circular_result = session.run("""
                MATCH (a)-[r1:DEPENDS_ON]->(b)-[r2:DEPENDS_ON]->(a)
                WHERE a.chunk_type = 'java_class' AND b.chunk_type = 'java_class'
                RETURN DISTINCT a.name as class_a, b.name as class_b
                LIMIT 50
            """)
            circular = list(circular_result)

        # Create report
        report_path = os.path.join(self.output_dir, 'dependencies', 'ANALYSIS.md')
        with open(report_path, 'w', encoding='utf-8') as f:
            f.write("# Analýza závislostí\n\n")

            f.write("## Třídy s vysokou vazbou\n\n")
            f.write("Třídy s více než 10 závislostmi mohou být obtížně udržovatelné:\n\n")
            f.write("| Třída | Počet závislostí | Typy cílů |\n")
            f.write("|-------|------------------|------------|\n")

            for record in high_coupling:
                targets = ', '.join(record['target_types'])
                f.write(f"| {record['class_name']} | {record['dep_count']} | {targets} |\n")

            f.write("\n## Kruhové závislosti\n\n")

            if circular:
                f.write("⚠️  **Nalezeny kruhové závislosti!**\n\n")
                f.write("| Třída A | Třída B |\n")
                f.write("|---------|----------|\n")
                for record in circular:
                    f.write(f"| {record['class_a']} | {record['class_b']} |\n")
            else:
                f.write("✅ Žádné kruhové závislosti nenalezeny.\n")

            f.write("\n## Doporučení\n\n")
            f.write("1. **Vysoká vazba:** Zvažte refaktoring tříd s vysokým počtem závislostí\n")
            f.write("2. **Kruhové závislosti:** Odstraňte kruhové závislosti pomocí dependency injection nebo interface extraction\n")
            f.write("3. **Modularizace:** Rozdělte velké komponenty na menší, lépe udržovatelné moduly\n")

        print(f"✅ Report závislostí: {report_path}")

    def run_generation(self):
        """Spustit generování všech dokumentů."""
        try:
            self.generate_process_catalog()
            self.generate_pages_catalog()
            self.generate_components_catalog()
            self.generate_dependencies_report()

            print("\n" + "="*60)
            print("✅ GENEROVÁNÍ DOKUMENTACE DOKONČENO")
            print("="*60)

        finally:
            if self.driver:
                self.driver.close()


if __name__ == "__main__":
    output_dir = "/Users/radektuma/DEV/KIS/analýza_20251127"
    generator = DetailedDocsGenerator(output_dir)
    generator.run_generation()
