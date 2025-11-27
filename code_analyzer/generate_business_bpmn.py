"""Generování BPMN business process diagramů."""
import os
from neo4j import GraphDatabase
from config import NEO4J_URI, NEO4J_USER, NEO4J_PASSWORD
from collections import defaultdict


class BusinessBPMNGenerator:
    """Generátor BPMN business process diagramů."""

    def __init__(self, output_dir: str):
        """Initialize generator."""
        self.output_dir = output_dir
        os.makedirs(os.path.join(output_dir, 'bpmn_business'), exist_ok=True)
        self.driver = GraphDatabase.driver(NEO4J_URI, auth=(NEO4J_USER, NEO4J_PASSWORD))

    def identify_business_processes(self):
        """Identifikovat business procesy z flow mezi stránkami."""
        print("\n=== IDENTIFIKACE BUSINESS PROCESŮ ===\n")

        with self.driver.session() as session:
            # Find page flows (JSP -> JSP connections, only pages not scriptlets)
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
                ORDER BY path_length DESC
                LIMIT 50
            """)

            flows = list(result)

        # Group flows by starting page
        process_groups = defaultdict(list)
        for record in flows:
            start = record['start_page']
            flow = record['flow_pages']
            process_groups[start].append(flow)

        print(f"Nalezeno {len(process_groups)} procesních skupin")
        return process_groups

    def infer_business_process_name(self, start_page: str, flows: list):
        """Odvodit název business procesu."""
        start_lower = start_page.lower()

        # Patterns for common business processes
        if 'doklad' in start_lower and ('parovani' in start_lower or 'vazby' in start_lower):
            return "Párování dokladů a vazeb"
        elif 'doklad' in start_lower and 'schval' in start_lower:
            return "Schvalování dokladů"
        elif 'doklad' in start_lower and 'edit' in start_lower:
            return "Editace dokladů"
        elif 'doklad' in start_lower and 'zadavani' in start_lower:
            return "Zadávání dokladů"
        elif 'budget' in start_lower and 'view' in start_lower:
            return "Zobrazení rozpočtu"
        elif 'budget' in start_lower and 'edit' in start_lower:
            return "Tvorba/úprava rozpočtu"
        elif 'budget' in start_lower and 'std' in start_lower:
            return "Standardní budgetování"
        elif 'budget' in start_lower and 'projekt' in start_lower:
            return "Projektové budgetování"
        elif 'evi' in start_lower and 'edit' in start_lower:
            return "Evidence - editace záznamů"
        elif 'evi' in start_lower and 'view' in start_lower:
            return "Evidence - zobrazení záznamů"
        elif 'process' in start_lower:
            return f"Proces: {start_page}"
        else:
            return f"Proces začínající v {start_page}"

    def infer_step_purpose(self, page_name: str):
        """Odvodit účel kroku v procesu."""
        name_lower = page_name.lower()

        if 'edit' in name_lower:
            return "Editace dat"
        elif 'view' in name_lower:
            return "Zobrazení informací"
        elif 'schval' in name_lower:
            return "Schválení"
        elif 'kontrola' in name_lower or 'kontrol' in name_lower:
            return "Kontrola dat"
        elif 'zadavani' in name_lower or 'zadani' in name_lower:
            return "Zadání dat"
        elif 'detail' in name_lower:
            return "Detailní zobrazení"
        elif 'list' in name_lower or 'seznam' in name_lower:
            return "Výběr ze seznamu"
        elif 'parovani' in name_lower:
            return "Párování záznamů"
        elif 'vazby' in name_lower:
            return "Správa vazeb"
        elif 'process' in name_lower:
            return "Zpracování"
        else:
            return page_name

    def generate_bpmn_flowchart(self, process_name: str, start_page: str, flows: list):
        """Vytvoření BPMN business process flowchart."""
        lines = ["```mermaid"]
        lines.append("flowchart TD")

        # Start event
        lines.append(f"    Start([Začátek procesu]) --> Entry")
        lines.append(f"    Entry[{self.infer_step_purpose(start_page)}]")

        # Get unique steps from all flows
        all_steps = set()
        for flow in flows:
            all_steps.update(flow)

        # Remove start page (already added as Entry)
        all_steps.discard(start_page)

        # Create nodes for each step
        step_ids = {start_page: 'Entry'}
        for idx, step in enumerate(sorted(all_steps), 1):
            step_id = f"Step{idx}"
            step_ids[step] = step_id
            purpose = self.infer_step_purpose(step)
            lines.append(f"    {step_id}[{purpose}]")

        # Add flow connections (from most common flow)
        if flows:
            # Use the first (longest) flow as main path
            main_flow = flows[0]
            prev_id = 'Entry'

            for page in main_flow[1:]:  # Skip first (start) page
                if page in step_ids:
                    current_id = step_ids[page]
                    lines.append(f"    {prev_id} --> {current_id}")
                    prev_id = current_id

            # Add end
            lines.append(f"    {prev_id} --> End")

        # End event
        lines.append("    End([Konec procesu])")

        # Add styling
        lines.append("")
        lines.append("    style Start fill:#90EE90")
        lines.append("    style End fill:#FFB6C1")
        lines.append("    style Entry fill:#87CEEB")

        lines.append("```")
        return "\n".join(lines)

    def generate_process_documentation(self, start_page: str, flows: list):
        """Generování dokumentace business procesu."""
        process_name = self.infer_business_process_name(start_page, flows)

        # Create markdown file
        filename = f"{start_page.replace('/', '_')}_PROCESS.md"
        filepath = os.path.join(self.output_dir, 'bpmn_business', filename)

        with open(filepath, 'w', encoding='utf-8') as f:
            f.write(f"# {process_name}\n\n")
            f.write(f"**Vstupní bod:** {start_page}\n\n")

            f.write("## Přehled procesu\n\n")
            f.write(f"Tento business proces začíná na stránce **{start_page}** ")
            f.write(f"a pokračuje přes {len(set([p for flow in flows for p in flow])) - 1} dalších kroků.\n\n")

            # Business process flowchart
            f.write("## Business Process Flow\n\n")
            f.write(self.generate_bpmn_flowchart(process_name, start_page, flows))
            f.write("\n\n")

            # Detailní kroky
            f.write("## Kroky procesu\n\n")

            # Use longest flow for detailed description
            if flows:
                main_flow = max(flows, key=len)

                for idx, page in enumerate(main_flow, 1):
                    purpose = self.infer_step_purpose(page)
                    f.write(f"### Krok {idx}: {purpose}\n\n")
                    f.write(f"- **Stránka:** `{page}`\n")

                    # Determine step type
                    if idx == 1:
                        f.write(f"- **Typ:** Vstupní bod procesu\n")
                    elif idx == len(main_flow):
                        f.write(f"- **Typ:** Konečný krok\n")
                    else:
                        f.write(f"- **Typ:** Procesní krok\n")

                    f.write("\n")

            # Alternative flows
            if len(flows) > 1:
                f.write("## Alternativní flow\n\n")
                f.write(f"Proces má {len(flows)} různých variant flow:\n\n")
                for idx, flow in enumerate(flows[:5], 1):  # Show first 5
                    flow_str = " → ".join([self.infer_step_purpose(p) for p in flow])
                    f.write(f"{idx}. {flow_str}\n")
                if len(flows) > 5:
                    f.write(f"\n... a dalších {len(flows) - 5} variant\n")

        print(f"✅ Business proces: {filepath}")

    def generate_all_business_processes(self, top_n=15):
        """Generování všech business procesů."""
        print("\n=== GENEROVÁNÍ BUSINESS BPMN DIAGRAMŮ ===\n")

        process_groups = self.identify_business_processes()

        # Sort by number of flows and complexity
        sorted_processes = sorted(
            process_groups.items(),
            key=lambda x: (len(x[1]), max(len(f) for f in x[1])),
            reverse=True
        )[:top_n]

        # Generate diagrams
        for start_page, flows in sorted_processes:
            self.generate_process_documentation(start_page, flows)

        # Create index
        self.create_index([p[0] for p in sorted_processes])

        print(f"\n✅ Vygenerováno {len(sorted_processes)} business procesů")

    def create_index(self, processes):
        """Vytvoření indexu business procesů."""
        filepath = os.path.join(self.output_dir, 'bpmn_business', 'INDEX.md')

        with open(filepath, 'w', encoding='utf-8') as f:
            f.write("# Business Process Index\n\n")
            f.write("## Identifikované business procesy\n\n")

            for process_page in processes:
                filename = f"{process_page.replace('/', '_')}_PROCESS.md"
                process_name = self.infer_business_process_name(process_page, [])
                f.write(f"- [{process_name}]({filename})\n")

        print(f"✅ Index business procesů: {filepath}")

    def close(self):
        """Close database connection."""
        if self.driver:
            self.driver.close()


if __name__ == "__main__":
    output_dir = "/Users/radektuma/DEV/KIS/analýza_20251127"
    generator = BusinessBPMNGenerator(output_dir)

    try:
        generator.generate_all_business_processes(top_n=15)
    finally:
        generator.close()
