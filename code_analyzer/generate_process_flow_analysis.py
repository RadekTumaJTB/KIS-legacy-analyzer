"""Propojení business procesních flow s detailní analýzou stránek.

Kombinuje:
- BPMN business procesy (flow mezi stránkami)
- Detailní analýzu stránek (SQL, Java, business logika)
- Neo4J vztahy mezi stránkami
"""
import os
import json
from neo4j import GraphDatabase
from qdrant_client import QdrantClient
from config import (
    NEO4J_URI, NEO4J_USER, NEO4J_PASSWORD,
    QDRANT_HOST, QDRANT_PORT, QDRANT_COLLECTION_NAME
)
from generate_detailed_page_analysis import DetailedPageAnalyzer


class ProcessFlowAnalyzer:
    """Analyzér procesních flow s detailními informacemi."""

    def __init__(self, output_dir: str):
        """Initialize analyzer."""
        self.output_dir = output_dir
        self.driver = GraphDatabase.driver(NEO4J_URI, auth=(NEO4J_USER, NEO4J_PASSWORD))
        try:
            self.qdrant_client = QdrantClient(host=QDRANT_HOST, port=QDRANT_PORT)
        except Exception as e:
            print(f"⚠️  Qdrant připojení selhalo: {e}")
            self.qdrant_client = None

        # Použij DetailedPageAnalyzer pro konzistentní analýzu
        self.page_analyzer = DetailedPageAnalyzer(output_dir)

    def get_business_processes(self):
        """Získat business procesy z Neo4J."""
        print("\n=== ZÍSKÁVÁNÍ BUSINESS PROCESŮ ===\n")

        with self.driver.session() as session:
            # Najdi procesní flow mezi stránkami
            result = session.run("""
                MATCH path = (start)-[r:DEPENDS_ON*1..3]->(end)
                WHERE start.chunk_type = 'jsp_page'
                  AND end.chunk_type = 'jsp_page'
                  AND start.chunk_id <> end.chunk_id
                  AND ALL(rel in r WHERE rel.type IN ['includes', 'calls', 'references'])
                WITH start.name as start_page,
                     [node in nodes(path) WHERE node.chunk_type = 'jsp_page' | node.name] as flow_pages,
                     length(path) as path_length
                WHERE path_length >= 1 AND size(flow_pages) > 1
                RETURN start_page,
                       flow_pages,
                       path_length
                ORDER BY path_length DESC, start_page
                LIMIT 50
            """)

            flows = list(result)

        # Seskupit podle vstupní stránky
        from collections import defaultdict
        process_groups = defaultdict(list)
        for record in flows:
            start = record['start_page']
            flow = record['flow_pages']
            process_groups[start].append(flow)

        print(f"Nalezeno {len(process_groups)} procesních skupin\n")
        return process_groups

    def analyze_process_with_details(self, start_page: str, flows: list):
        """Analyzovat proces s detaily jednotlivých stránek."""
        print(f"Analyzuji proces: {start_page}")

        # Získej všechny unikátní stránky v procesu
        all_pages = set()
        for flow in flows:
            all_pages.update(flow)

        # Analyzuj každou stránku
        page_details = {}
        for page_name in all_pages:
            # Získej chunk_id z Neo4J
            with self.driver.session() as session:
                result = session.run("""
                    MATCH (jsp)
                    WHERE jsp.chunk_type = 'jsp_page' AND jsp.name = $page_name
                    RETURN jsp.chunk_id as chunk_id, jsp.file_path as file_path
                    LIMIT 1
                """, page_name=page_name)
                record = result.single()

                if record:
                    chunk_id = record['chunk_id']
                    file_path = record['file_path']

                    # Získej obsah z Qdrant
                    content = self.page_analyzer.get_page_content(chunk_id)

                    # Analyzuj SQL a Java
                    sql_info = self.page_analyzer.extract_sql_operations(content)
                    java_info = self.page_analyzer.extract_java_calls(content)
                    business_logic = self.page_analyzer.infer_business_logic(
                        page_name, sql_info, java_info, content
                    )

                    page_details[page_name] = {
                        'file_path': file_path,
                        'sql_tables': sorted(sql_info['tables']),
                        'sql_operations': dict(sql_info['operations']),
                        'sql_columns': {k: sorted(v) for k, v in sql_info['columns'].items()},
                        'java_methods': sorted(java_info['methods'])[:10],
                        'business_logic': business_logic
                    }

        return page_details

    def generate_process_flow_documentation(self, start_page: str, flows: list, page_details: dict):
        """Generovat dokumentaci procesního flow s detaily."""
        process_name = self._infer_process_name(start_page)

        output_path = os.path.join(
            self.output_dir,
            'process_flows',
            f"{start_page.replace('/', '_')}_FLOW.md"
        )

        os.makedirs(os.path.join(self.output_dir, 'process_flows'), exist_ok=True)

        with open(output_path, 'w', encoding='utf-8') as f:
            f.write(f"# {process_name}\n\n")
            f.write(f"**Vstupní stránka:** {start_page}\n\n")

            # Přehled procesu
            all_pages = set()
            for flow in flows:
                all_pages.update(flow)

            f.write("## Přehled procesu\n\n")
            f.write(f"**Počet kroků:** {len(all_pages)}\n")
            f.write(f"**Počet variant flow:** {len(flows)}\n\n")

            # Procesní diagram (Mermaid)
            f.write("## Procesní diagram\n\n")
            f.write(self._generate_mermaid_flow(start_page, flows, page_details))
            f.write("\n\n")

            # Detailní kroky s SQL a Java informacemi
            f.write("## Detailní analýza kroků\n\n")

            # Použij nejdelší flow jako hlavní cestu
            main_flow = max(flows, key=len) if flows else []

            for idx, page_name in enumerate(main_flow, 1):
                details = page_details.get(page_name, {})
                bl = details.get('business_logic', {})

                f.write(f"### Krok {idx}: {page_name}\n\n")
                f.write(f"**Soubor:** `{details.get('file_path', 'N/A')}`\n\n")

                # Primární funkce
                primary_func = bl.get('primary_function', 'Neznámá funkce')
                f.write(f"**Funkce v procesu:** {primary_func}\n\n")

                # SQL operace
                if details.get('sql_tables'):
                    f.write("#### Databázové operace\n\n")
                    for table in details['sql_tables']:
                        operations = details['sql_operations'].get(table, set())
                        columns = details['sql_columns'].get(table, [])

                        ops_str = ', '.join(sorted(operations))
                        f.write(f"- **{table}**: {ops_str}\n")
                        if columns:
                            f.write(f"  - Sloupce: {', '.join(columns[:10])}\n")
                    f.write("\n")

                # Java metody
                if details.get('java_methods'):
                    f.write("#### Volané Java metody\n\n")
                    for method in details['java_methods'][:5]:
                        f.write(f"- `{method}()`\n")
                    f.write("\n")

                # Business pravidla
                if bl.get('business_rules'):
                    f.write("#### Business pravidla\n\n")
                    for rule in bl['business_rules']:
                        f.write(f"- {rule}\n")
                    f.write("\n")

                f.write("---\n\n")

            # Alternativní flow
            if len(flows) > 1:
                f.write("## Alternativní procesní cesty\n\n")
                for idx, flow in enumerate(flows[:5], 1):
                    flow_str = " → ".join(flow)
                    f.write(f"{idx}. {flow_str}\n")
                if len(flows) > 5:
                    f.write(f"\n... a dalších {len(flows) - 5} variant\n")

        print(f"✅ Process flow: {output_path}")

    def _generate_mermaid_flow(self, start_page: str, flows: list, page_details: dict):
        """Vytvořit Mermaid diagram s business kontextem."""
        lines = ["```mermaid", "flowchart TD"]

        # Start
        lines.append(f"    Start([Začátek procesu]) --> Entry")

        # Vstupní bod
        entry_details = page_details.get(start_page, {})
        entry_bl = entry_details.get('business_logic', {})
        entry_func = entry_bl.get('primary_function', start_page)
        lines.append(f"    Entry[\"{entry_func}\"]")

        # Další kroky
        all_pages = set()
        for flow in flows:
            all_pages.update(flow)
        all_pages.discard(start_page)

        step_ids = {start_page: 'Entry'}
        for idx, page in enumerate(sorted(all_pages), 1):
            step_id = f"Step{idx}"
            step_ids[page] = step_id

            details = page_details.get(page, {})
            bl = details.get('business_logic', {})
            func = bl.get('primary_function', page)

            lines.append(f"    {step_id}[\"{func}\"]")

        # Hlavní flow (nejdelší)
        if flows:
            main_flow = max(flows, key=len)
            prev_id = 'Entry'

            for page in main_flow[1:]:
                if page in step_ids:
                    current_id = step_ids[page]
                    lines.append(f"    {prev_id} --> {current_id}")
                    prev_id = current_id

            lines.append(f"    {prev_id} --> End")

        lines.append("    End([Konec procesu])")

        # Styling
        lines.append("")
        lines.append("    style Start fill:#90EE90")
        lines.append("    style End fill:#FFB6C1")
        lines.append("    style Entry fill:#87CEEB")

        lines.append("```")
        return "\n".join(lines)

    def _infer_process_name(self, start_page: str):
        """Odvodit název procesu."""
        page_lower = start_page.lower()

        if 'doklad' in page_lower and 'parovani' in page_lower:
            return f"Business proces: Párování dokladů ({start_page})"
        elif 'doklad' in page_lower and 'schval' in page_lower:
            return f"Business proces: Schvalování dokladů ({start_page})"
        elif 'budget' in page_lower:
            return f"Business proces: Budgetování ({start_page})"
        elif 'process' in page_lower:
            return f"Business proces: {start_page}"
        else:
            return f"Business proces: {start_page}"

    def generate_all_process_flows(self, top_n=15):
        """Generovat všechny procesní flow s detaily."""
        print("\n=== GENEROVÁNÍ PROCESNÍCH FLOW S DETAILY ===\n")

        # Získej business procesy
        process_groups = self.get_business_processes()

        # Seřaď podle složitosti
        sorted_processes = sorted(
            process_groups.items(),
            key=lambda x: (len(x[1]), max(len(f) for f in x[1])),
            reverse=True
        )[:top_n]

        # Generuj dokumentaci
        for start_page, flows in sorted_processes:
            # Analyzuj stránky v procesu
            page_details = self.analyze_process_with_details(start_page, flows)

            # Generuj dokumentaci
            self.generate_process_flow_documentation(start_page, flows, page_details)

        # Vytvoř index
        self._create_index([p[0] for p in sorted_processes])

        print(f"\n✅ Vygenerováno {len(sorted_processes)} procesních flow")

    def _create_index(self, processes: list):
        """Vytvoř index procesních flow."""
        output_path = os.path.join(self.output_dir, 'process_flows', 'INDEX.md')

        with open(output_path, 'w', encoding='utf-8') as f:
            f.write("# Index procesních flow s detaily\n\n")
            f.write("Každý proces obsahuje:\n")
            f.write("- Business process diagram\n")
            f.write("- Detailní analýzu každého kroku (SQL, Java, business logika)\n")
            f.write("- Alternativní procesní cesty\n\n")
            f.write("---\n\n")

            for process_page in processes:
                filename = f"{process_page.replace('/', '_')}_FLOW.md"
                process_name = self._infer_process_name(process_page)
                f.write(f"- [{process_name}]({filename})\n")

        print(f"✅ Index procesních flow: {output_path}")

    def close(self):
        """Close connections."""
        if self.driver:
            self.driver.close()
        if self.page_analyzer:
            self.page_analyzer.close()


if __name__ == "__main__":
    output_dir = "/Users/radektuma/DEV/KIS/analýza_20251127"
    analyzer = ProcessFlowAnalyzer(output_dir)

    try:
        analyzer.generate_all_process_flows(top_n=15)
    finally:
        analyzer.close()
