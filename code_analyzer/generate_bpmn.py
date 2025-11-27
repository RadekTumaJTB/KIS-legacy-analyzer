"""Generování BPMN diagramů pro klíčové procesy aplikace."""
import os
import json
from neo4j import GraphDatabase
from config import NEO4J_URI, NEO4J_USER, NEO4J_PASSWORD
from collections import defaultdict


class BPMNGenerator:
    """Generátor BPMN diagramů z analyzovaných procesů."""

    def __init__(self, output_dir: str):
        """Initialize generator."""
        self.output_dir = output_dir
        os.makedirs(os.path.join(output_dir, 'bpmn'), exist_ok=True)
        self.driver = GraphDatabase.driver(NEO4J_URI, auth=(NEO4J_USER, NEO4J_PASSWORD))

    def get_process_details(self, jsp_name: str):
        """Získání detailů procesu pro danou JSP stránku."""
        with self.driver.session() as session:
            # Get the main page
            result = session.run("""
                MATCH (jsp)
                WHERE jsp.name = $jsp_name AND jsp.chunk_type = 'jsp_page'
                RETURN jsp.name as name,
                       jsp.file_path as file_path,
                       jsp.chunk_id as chunk_id
            """, jsp_name=jsp_name).single()

            if not result:
                return None

            process = {
                'name': result['name'],
                'file_path': result['file_path'],
                'steps': []
            }

            # Get scriptlets (internal logic)
            scriptlets = session.run("""
                MATCH (jsp)-[r:DEPENDS_ON]->(scriptlet)
                WHERE jsp.name = $jsp_name
                  AND jsp.chunk_type = 'jsp_page'
                  AND scriptlet.chunk_type = 'jsp_scriptlet'
                  AND r.type = 'contains'
                RETURN scriptlet.name as name,
                       scriptlet.chunk_id as chunk_id
                ORDER BY scriptlet.name
            """, jsp_name=jsp_name)

            for record in scriptlets:
                process['steps'].append({
                    'type': 'scriptlet',
                    'name': record['name'],
                    'chunk_id': record['chunk_id']
                })

            # Get Java method calls
            methods = session.run("""
                MATCH (jsp)-[r:DEPENDS_ON]->(method)
                WHERE jsp.name = $jsp_name
                  AND jsp.chunk_type = 'jsp_page'
                  AND method.chunk_type = 'java_method'
                  AND r.type = 'calls'
                RETURN DISTINCT method.name as name
                LIMIT 10
            """, jsp_name=jsp_name)

            for record in methods:
                process['steps'].append({
                    'type': 'java_call',
                    'name': record['name']
                })

            # Get includes (other JSP pages)
            includes = session.run("""
                MATCH (jsp)-[r:DEPENDS_ON]->(target)
                WHERE jsp.name = $jsp_name
                  AND jsp.chunk_type = 'jsp_page'
                  AND target.chunk_type = 'jsp_page'
                  AND r.type IN ['includes', 'calls']
                RETURN target.name as name, r.type as rel_type
            """, jsp_name=jsp_name)

            for record in includes:
                process['steps'].append({
                    'type': 'jsp_include',
                    'name': record['name'],
                    'rel_type': record['rel_type']
                })

            return process

    def generate_mermaid_flowchart(self, process):
        """Vytvoření Mermaid flowchart diagramu."""
        lines = ["```mermaid"]
        lines.append("flowchart TD")
        lines.append(f"    Start([Začátek: {process['name']}]) --> Process")

        # Create process box
        lines.append(f"    Process[Proces: {process['name']}]")

        # Add steps
        step_nodes = []
        for idx, step in enumerate(process['steps']):
            node_id = f"Step{idx}"
            step_nodes.append(node_id)

            if step['type'] == 'scriptlet':
                lines.append(f"    {node_id}[{step['name']}]")
            elif step['type'] == 'java_call':
                lines.append(f"    {node_id}[Volání: {step['name']}]")
            elif step['type'] == 'jsp_include':
                lines.append(f"    {node_id}{{Include: {step['name']}}}")

        # Connect steps
        if step_nodes:
            prev = "Process"
            for node_id in step_nodes:
                lines.append(f"    {prev} --> {node_id}")
                prev = node_id
            lines.append(f"    {prev} --> End")
        else:
            lines.append("    Process --> End")

        lines.append("    End([Konec])")

        # Add styling
        lines.append("    style Start fill:#90EE90")
        lines.append("    style End fill:#FFB6C1")
        lines.append("    style Process fill:#87CEEB")

        lines.append("```")
        return "\n".join(lines)

    def generate_mermaid_sequence(self, process):
        """Vytvoření Mermaid sequence diagramu."""
        lines = ["```mermaid"]
        lines.append("sequenceDiagram")
        lines.append(f"    participant User as Uživatel")
        lines.append(f"    participant JSP as {process['name']}")
        lines.append("    participant Backend as Backend")

        lines.append("")
        lines.append("    User->>JSP: HTTP Request")

        # Add scriptlets as internal processing
        for step in process['steps']:
            if step['type'] == 'scriptlet':
                lines.append(f"    JSP->>JSP: {step['name']}")
            elif step['type'] == 'java_call':
                lines.append(f"    JSP->>Backend: {step['name']}")
                lines.append(f"    Backend-->>JSP: Response")
            elif step['type'] == 'jsp_include':
                lines.append(f"    JSP->>JSP: Include {step['name']}")

        lines.append("    JSP-->>User: HTTP Response")
        lines.append("```")
        return "\n".join(lines)

    def generate_process_documentation(self, process_name: str):
        """Generování kompletní dokumentace procesu."""
        process = self.get_process_details(process_name)
        if not process:
            print(f"⚠️  Proces {process_name} nenalezen")
            return

        # Create markdown file
        filename = f"{process_name.replace('/', '_')}.md"
        filepath = os.path.join(self.output_dir, 'bpmn', filename)

        with open(filepath, 'w', encoding='utf-8') as f:
            f.write(f"# Proces: {process['name']}\n\n")
            f.write(f"**Soubor:** `{process['file_path']}`\n\n")

            f.write("## Přehled procesu\n\n")
            f.write(f"Proces obsahuje {len(process['steps'])} kroků.\n\n")

            # Step types breakdown
            scriptlet_count = sum(1 for s in process['steps'] if s['type'] == 'scriptlet')
            java_call_count = sum(1 for s in process['steps'] if s['type'] == 'java_call')
            jsp_include_count = sum(1 for s in process['steps'] if s['type'] == 'jsp_include')

            f.write(f"- **Scriptlety:** {scriptlet_count}\n")
            f.write(f"- **Java volání:** {java_call_count}\n")
            f.write(f"- **JSP includes:** {jsp_include_count}\n\n")

            # Flowchart diagram
            f.write("## Flowchart diagram\n\n")
            f.write(self.generate_mermaid_flowchart(process))
            f.write("\n\n")

            # Sequence diagram
            f.write("## Sequence diagram\n\n")
            f.write(self.generate_mermaid_sequence(process))
            f.write("\n\n")

            # Detailed steps
            f.write("## Detailní kroky\n\n")
            for idx, step in enumerate(process['steps'], 1):
                f.write(f"### Krok {idx}: {step['name']}\n\n")
                f.write(f"- **Typ:** {step['type']}\n")
                if 'chunk_id' in step:
                    f.write(f"- **ID:** {step['chunk_id']}\n")
                if 'rel_type' in step:
                    f.write(f"- **Relace:** {step['rel_type']}\n")
                f.write("\n")

        print(f"✅ Vygenerován diagram: {filepath}")

    def generate_all_diagrams(self, top_n=10):
        """Generování diagramů pro top N procesů."""
        print("\n=== GENEROVÁNÍ BPMN DIAGRAMŮ ===\n")

        # Get top processes
        with self.driver.session() as session:
            result = session.run("""
                MATCH (jsp)-[r:DEPENDS_ON]->(target)
                WHERE jsp.chunk_type = 'jsp_page'
                WITH jsp.name as page_name, count(r) as dep_count
                ORDER BY dep_count DESC
                LIMIT $limit
                RETURN page_name
            """, limit=top_n)

            processes = [record['page_name'] for record in result]

        # Generate diagrams
        for process_name in processes:
            self.generate_process_documentation(process_name)

        # Create index
        self.create_index(processes)

        print(f"\n✅ Vygenerováno {len(processes)} BPMN diagramů")

    def create_index(self, processes):
        """Vytvoření indexu všech diagramů."""
        filepath = os.path.join(self.output_dir, 'bpmn', 'INDEX.md')

        with open(filepath, 'w', encoding='utf-8') as f:
            f.write("# BPMN Diagramy - Index\n\n")
            f.write("## Klíčové procesy aplikace\n\n")

            for process_name in processes:
                filename = f"{process_name.replace('/', '_')}.md"
                f.write(f"- [{process_name}]({filename})\n")

        print(f"✅ Index vytvořen: {filepath}")

    def close(self):
        """Close database connection."""
        if self.driver:
            self.driver.close()


if __name__ == "__main__":
    output_dir = "/Users/radektuma/DEV/KIS/analýza_20251127"
    generator = BPMNGenerator(output_dir)

    try:
        generator.generate_all_diagrams(top_n=15)
    finally:
        generator.close()
