"""Detailní analýza JSP stránek z perspektivy senior Java developera.

Analyzuje:
- SQL tabulky a sloupce používané v kódu
- Java metody a třídy volané ze stránek
- Business logiku odvozenou z kódu a databázových operací
"""
import os
import re
from collections import defaultdict
from neo4j import GraphDatabase
from qdrant_client import QdrantClient
from config import (
    NEO4J_URI, NEO4J_USER, NEO4J_PASSWORD,
    QDRANT_HOST, QDRANT_PORT, QDRANT_COLLECTION_NAME
)


class DetailedPageAnalyzer:
    """Senior developer perspektiva - analýza ze zdrojového kódu."""

    def __init__(self, output_dir: str):
        """Initialize analyzer."""
        self.output_dir = output_dir
        self.driver = GraphDatabase.driver(NEO4J_URI, auth=(NEO4J_USER, NEO4J_PASSWORD))
        try:
            self.qdrant_client = QdrantClient(host=QDRANT_HOST, port=QDRANT_PORT)
        except Exception as e:
            print(f"⚠️  Qdrant připojení selhalo: {e}")
            self.qdrant_client = None

    def get_page_content(self, chunk_id: str):
        """Získat plný obsah stránky z Qdrant."""
        if not self.qdrant_client:
            return None

        try:
            results = self.qdrant_client.scroll(
                collection_name=QDRANT_COLLECTION_NAME,
                scroll_filter={
                    "must": [
                        {"key": "chunk_id", "match": {"value": chunk_id}}
                    ]
                },
                limit=1,
                with_payload=True,
                with_vectors=False
            )

            if results and results[0]:
                point = results[0][0]
                return point.payload.get('content', '')
            return None
        except Exception as e:
            print(f"  ⚠️  Chyba při čtení z Qdrant: {e}")
            return None

    def extract_sql_operations(self, content: str):
        """Extrahovat SQL operace, tabulky a sloupce z kódu."""
        if not content:
            return {}

        sql_info = {
            'tables': set(),
            'operations': defaultdict(set),  # table -> {SELECT, INSERT, UPDATE, DELETE}
            'columns': defaultdict(set),  # table -> {column names}
        }

        # SELECT ... FROM [schema.]table_name
        # Pattern podporuje: select, SELECT, db_jt.table_name, table_name
        select_pattern = r'\bselect\s+(.+?)\s+from\s+(?:[a-z_][a-z0-9_]*\.)?([a-z_][a-z0-9_]*)'
        for match in re.finditer(select_pattern, content, re.IGNORECASE | re.DOTALL):
            columns_str = match.group(1)
            table = match.group(2).upper()  # Normalize to uppercase
            sql_info['tables'].add(table)
            sql_info['operations'][table].add('SELECT')

            # Extrahuj názvy sloupců
            if '*' not in columns_str:
                # Rozdělení podle čárky, odstranění aliasů (as xyz)
                col_parts = columns_str.split(',')
                for col in col_parts:
                    col_clean = col.strip().split()[0]  # První slovo
                    col_clean = col_clean.split('.')[-1]  # Odstranění table prefix
                    if col_clean and not col_clean.upper() in ['DISTINCT', 'TOP', 'ALL']:
                        sql_info['columns'][table].add(col_clean.upper())

        # INSERT INTO [schema.]table_name (columns) VALUES
        insert_pattern = r'\binsert\s+into\s+(?:[a-z_][a-z0-9_]*\.)?([a-z_][a-z0-9_]*)\s*\(([^)]+)\)'
        for match in re.finditer(insert_pattern, content, re.IGNORECASE):
            table = match.group(1).upper()
            columns_str = match.group(2)
            sql_info['tables'].add(table)
            sql_info['operations'][table].add('INSERT')
            columns = [c.strip().upper() for c in columns_str.split(',')]
            sql_info['columns'][table].update(columns)

        # UPDATE [schema.]table_name SET column = value
        update_pattern = r'\bupdate\s+(?:[a-z_][a-z0-9_]*\.)?([a-z_][a-z0-9_]*)\s+set\s+([^;]+?)(?:where|$)'
        for match in re.finditer(update_pattern, content, re.IGNORECASE | re.DOTALL):
            table = match.group(1).upper()
            set_clause = match.group(2).strip()
            sql_info['tables'].add(table)
            sql_info['operations'][table].add('UPDATE')
            # Extrahuj sloupce z SET clause
            for assignment in set_clause.split(','):
                if '=' in assignment:
                    column = assignment.split('=')[0].strip().upper()
                    # Odstranění table prefix
                    if '.' in column:
                        column = column.split('.')[-1]
                    sql_info['columns'][table].add(column)

        # DELETE FROM [schema.]table_name
        delete_pattern = r'\bdelete\s+from\s+(?:[a-z_][a-z0-9_]*\.)?([a-z_][a-z0-9_]*)'
        for match in re.finditer(delete_pattern, content, re.IGNORECASE):
            table = match.group(1).upper()
            sql_info['tables'].add(table)
            sql_info['operations'][table].add('DELETE')

        return sql_info

    def extract_java_calls(self, content: str):
        """Extrahovat volání Java metod a tříd."""
        if not content:
            return {'classes': set(), 'methods': set()}

        java_info = {
            'classes': set(),
            'methods': set(),
        }

        # Najdi volání metod: ClassName.methodName() nebo object.methodName()
        method_pattern = r'([A-Z][a-zA-Z0-9_]*\.)?([a-z][a-zA-Z0-9_]*)\s*\('
        for match in re.finditer(method_pattern, content):
            if match.group(1):  # Má prefix třídy
                class_name = match.group(1).rstrip('.')
                method_name = match.group(2)
                java_info['classes'].add(class_name)
                java_info['methods'].add(f"{class_name}.{method_name}")
            else:
                java_info['methods'].add(match.group(2))

        # Najdi import statements
        import_pattern = r'import\s+([a-zA-Z0-9_.]+);'
        for match in re.finditer(import_pattern, content):
            import_path = match.group(1)
            class_name = import_path.split('.')[-1]
            java_info['classes'].add(class_name)

        return java_info

    def infer_business_logic(self, page_name: str, sql_info: dict, java_info: dict, content: str):
        """Odvodit business logiku jako senior Java developer."""
        logic = {
            'primary_function': '',
            'data_operations': [],
            'business_rules': [],
            'integration_points': []
        }

        page_lower = page_name.lower()

        # Odvození primární funkce
        if 'edit' in page_lower:
            if any('INSERT' in ops or 'UPDATE' in ops for ops in sql_info['operations'].values()):
                logic['primary_function'] = 'Editace a ukládání záznamů'
            else:
                logic['primary_function'] = 'Zobrazení a úprava dat'
        elif 'view' in page_lower:
            logic['primary_function'] = 'Zobrazení dat pouze pro čtení'
        elif 'list' in page_lower or 'seznam' in page_lower:
            logic['primary_function'] = 'Seznam záznamů s možností vyhledávání'
        elif 'process' in page_lower:
            logic['primary_function'] = 'Procesní zpracování dat'
        elif 'schval' in page_lower:
            logic['primary_function'] = 'Schvalovací workflow'
        elif 'delete' in page_lower:
            logic['primary_function'] = 'Mazání záznamů'
        elif 'create' in page_lower or 'new' in page_lower:
            logic['primary_function'] = 'Vytváření nových záznamů'
        else:
            logic['primary_function'] = 'Obecná business funkcionalita'

        # Data operations
        for table, operations in sql_info['operations'].items():
            ops_list = sorted(operations)
            columns_info = ''
            if table in sql_info['columns'] and sql_info['columns'][table]:
                columns = sorted(sql_info['columns'][table])[:5]  # Top 5 sloupců
                columns_info = f" ({', '.join(columns)})"

            operation_desc = self._describe_operations(ops_list, table)
            logic['data_operations'].append(f"{operation_desc}{columns_info}")

        # Business rules z kódu
        if content:
            content_lower = content.lower()

            if 'validate' in content_lower or 'check' in content_lower:
                logic['business_rules'].append('Validace vstupních dat')

            if 'permission' in content_lower or 'role' in content_lower:
                logic['business_rules'].append('Kontrola oprávnění uživatele')

            if 'transaction' in content_lower or 'commit' in content_lower:
                logic['business_rules'].append('Transakční zpracování')

            if 'email' in content_lower or 'notification' in content_lower:
                logic['business_rules'].append('Odesílání notifikací')

            if 'export' in content_lower or 'excel' in content_lower:
                logic['business_rules'].append('Export dat do externích formátů')

            if 'import' in content_lower:
                logic['business_rules'].append('Import dat z externích zdrojů')

            if 'calculate' in content_lower or 'compute' in content_lower:
                logic['business_rules'].append('Výpočty a kalkulace')

        # Integration points
        for method in java_info['methods']:
            if 'Utils.' in method:
                logic['integration_points'].append(f'Utility třída: {method}')
            elif 'Service' in method or 'Manager' in method:
                logic['integration_points'].append(f'Business služba: {method}')

        return logic

    def _describe_operations(self, operations: list, table: str):
        """Popsat databázové operace lidsky čitelně."""
        if operations == ['SELECT']:
            return f"Čte data z {table}"
        elif operations == ['INSERT']:
            return f"Vytváří nové záznamy v {table}"
        elif operations == ['UPDATE']:
            return f"Aktualizuje existující záznamy v {table}"
        elif operations == ['DELETE']:
            return f"Maže záznamy z {table}"
        elif set(operations) == {'SELECT', 'INSERT'}:
            return f"Čte a vytváří záznamy v {table}"
        elif set(operations) == {'SELECT', 'UPDATE'}:
            return f"Načítá a upravuje záznamy v {table}"
        elif set(operations) == {'SELECT', 'DELETE'}:
            return f"Načítá a maže záznamy z {table}"
        elif len(operations) >= 3:
            return f"Kompletní CRUD operace nad {table}"
        else:
            ops_str = ', '.join(operations)
            return f"{ops_str} operace nad {table}"

    def analyze_pages(self, limit=None):
        """Analyzovat JSP stránky."""
        print("\n=== DETAILNÍ ANALÝZA JSP STRÁNEK ===\n")

        os.makedirs(os.path.join(self.output_dir, 'pages'), exist_ok=True)

        # Získej seznam stránek z Neo4J
        with self.driver.session() as session:
            result = session.run("""
                MATCH (jsp)
                WHERE jsp.chunk_type = 'jsp_page'
                RETURN DISTINCT jsp.name as page_name,
                       jsp.file_path as file_path,
                       jsp.chunk_id as chunk_id
                ORDER BY page_name
            """)
            pages = list(result)

        # Deduplikace
        seen = set()
        unique_pages = []
        for page in pages:
            if page['page_name'] not in seen:
                seen.add(page['page_name'])
                unique_pages.append(page)

        if limit:
            pages = unique_pages[:limit]
        else:
            pages = unique_pages
        print(f"Analyzuji {len(pages)} stránek...\n")

        # Kategorizace
        categories = {}
        page_count = 0

        for record in pages:
            page_name = record['page_name']
            chunk_id = record['chunk_id']
            file_path = record['file_path']

            # Získej obsah
            content = self.get_page_content(chunk_id)

            # Analyzuj SQL a Java
            sql_info = self.extract_sql_operations(content)
            java_info = self.extract_java_calls(content)

            # Odvoď business logiku
            business_logic = self.infer_business_logic(
                page_name, sql_info, java_info, content
            )

            # Kategorizace podle domény
            domain = self._determine_domain(page_name)
            if domain not in categories:
                categories[domain] = []

            categories[domain].append({
                'name': page_name,
                'file_path': file_path,
                'sql_tables': sorted(sql_info['tables']),
                'sql_operations': dict(sql_info['operations']),
                'sql_columns': {k: sorted(v) for k, v in sql_info['columns'].items()},
                'java_methods': sorted(java_info['methods'])[:10],  # Top 10
                'java_classes': sorted(java_info['classes'])[:5],   # Top 5
                'business_logic': business_logic
            })

            page_count += 1
            if page_count % 10 == 0:
                print(f"  Zpracováno {page_count}/{len(pages)} stránek...")

        # Generuj dokumentaci
        self._generate_detailed_documentation(categories, page_count)

        print(f"\n✅ Analýza dokončena pro {page_count} stránek")

    def _determine_domain(self, page_name: str):
        """Určit doménu podle názvu stránky."""
        page_lower = page_name.lower()

        if 'doklad' in page_lower:
            return 'Správa dokladů'
        elif 'budget' in page_lower:
            return 'Budgetování'
        elif 'doc' in page_lower:
            return 'Dokumenty'
        elif 'evi' in page_lower:
            return 'Evidence'
        elif 'pb' in page_lower:
            return 'Projektový budget'
        elif 'ifrs' in page_lower:
            return 'IFRS reporting'
        elif 'ucet' in page_lower or 'account' in page_lower:
            return 'Účetnictví'
        else:
            return 'Obecné'

    def _generate_detailed_documentation(self, categories: dict, page_count: int):
        """Generovat detailní dokumentaci."""
        output_path = os.path.join(self.output_dir, 'pages', 'DETAILED_ANALYSIS.md')

        with open(output_path, 'w', encoding='utf-8') as f:
            f.write("# Detailní analýza JSP stránek\n\n")
            f.write("**Perspektiva:** Senior Java Developer\n\n")
            f.write(f"**Analyzováno:** {page_count} stránek\n\n")
            f.write("---\n\n")

            for domain, pages in sorted(categories.items()):
                f.write(f"## {domain}\n\n")
                f.write(f"*Celkem {len(pages)} stránek v této doméně*\n\n")

                for page in pages:
                    f.write(f"### {page['name']}\n\n")
                    f.write(f"**Soubor:** `{page['file_path']}`\n\n")

                    # Business function
                    bl = page['business_logic']
                    f.write(f"**Primární funkce:** {bl['primary_function']}\n\n")

                    # SQL Operations
                    if page['sql_tables']:
                        f.write("#### Databázové operace\n\n")
                        if bl['data_operations']:
                            for op in bl['data_operations']:
                                f.write(f"- {op}\n")
                        else:
                            f.write(f"- Tabulky: {', '.join(page['sql_tables'])}\n")
                        f.write("\n")

                        # Detailní sloupce
                        if any(page['sql_columns'].values()):
                            f.write("**Používané sloupce:**\n\n")
                            for table, columns in sorted(page['sql_columns'].items()):
                                if columns:
                                    f.write(f"- `{table}`: {', '.join(columns[:10])}\n")
                            f.write("\n")

                    # Java Integration
                    if page['java_methods']:
                        f.write("#### Java integrace\n\n")
                        f.write("**Volané metody:**\n")
                        for method in page['java_methods'][:5]:
                            f.write(f"- `{method}()`\n")
                        f.write("\n")

                    # Business Rules
                    if bl['business_rules']:
                        f.write("#### Business pravidla\n\n")
                        for rule in bl['business_rules']:
                            f.write(f"- {rule}\n")
                        f.write("\n")

                    # Integration Points
                    if bl['integration_points']:
                        f.write("#### Integrační body\n\n")
                        for point in bl['integration_points'][:3]:
                            f.write(f"- {point}\n")
                        f.write("\n")

                    f.write("---\n\n")

        print(f"✅ Detailní analýza: {output_path}")

    def close(self):
        """Close connections."""
        if self.driver:
            self.driver.close()


if __name__ == "__main__":
    output_dir = "/Users/radektuma/DEV/KIS/analýza_20251127"
    analyzer = DetailedPageAnalyzer(output_dir)

    try:
        # Analyzuj všechny stránky (žádný limit)
        analyzer.analyze_pages(limit=None)
    finally:
        analyzer.close()
