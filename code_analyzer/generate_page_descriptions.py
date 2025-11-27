"""Generování popisů JSP stránek podle jejich obsahu."""
import os
import json
from neo4j import GraphDatabase
from qdrant_client import QdrantClient
from config import (
    NEO4J_URI, NEO4J_USER, NEO4J_PASSWORD,
    QDRANT_HOST, QDRANT_PORT, QDRANT_COLLECTION_NAME
)


class PageDescriptionGenerator:
    """Generátor popisů funkcionalit JSP stránek."""

    def __init__(self, output_dir: str):
        """Initialize generator."""
        self.output_dir = output_dir
        self.driver = GraphDatabase.driver(NEO4J_URI, auth=(NEO4J_USER, NEO4J_PASSWORD))
        try:
            self.qdrant_client = QdrantClient(host=QDRANT_HOST, port=QDRANT_PORT)
        except Exception as e:
            print(f"⚠️  Qdrant připojení selhalo: {e}")
            self.qdrant_client = None

    def get_page_content_from_qdrant(self, chunk_id: str):
        """Získat obsah stránky z Qdrant."""
        if not self.qdrant_client:
            return None

        try:
            # Search by chunk_id in payload
            results = self.qdrant_client.scroll(
                collection_name=QDRANT_COLLECTION_NAME,
                scroll_filter={
                    "must": [
                        {
                            "key": "chunk_id",
                            "match": {"value": chunk_id}
                        }
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

    def analyze_page_functionality(self, page_name: str, content: str):
        """Analyzovat funkcionalitu stránky podle obsahu."""
        functionality = {
            'type': 'unknown',
            'purpose': '',
            'features': []
        }

        content_lower = content.lower() if content else ''

        # Determine type
        if 'edit' in page_name.lower():
            functionality['type'] = 'edit'
            functionality['purpose'] = 'Editace/úprava dat'
        elif 'view' in page_name.lower():
            functionality['type'] = 'view'
            functionality['purpose'] = 'Zobrazení dat'
        elif 'process' in page_name.lower():
            functionality['type'] = 'process'
            functionality['purpose'] = 'Procesní stránka'
        elif 'schval' in page_name.lower():
            functionality['type'] = 'approval'
            functionality['purpose'] = 'Schvalování'
        elif 'list' in page_name.lower() or 'seznam' in content_lower:
            functionality['type'] = 'list'
            functionality['purpose'] = 'Seznam/přehled záznamů'

        # Detect features from content
        if content:
            if 'form' in content_lower or 'input' in content_lower:
                functionality['features'].append('Formulář pro zadávání dat')
            if 'table' in content_lower or '<table' in content_lower:
                functionality['features'].append('Tabulkové zobrazení')
            if 'select' in content_lower and 'option' in content_lower:
                functionality['features'].append('Dropdown výběry')
            if 'button' in content_lower or 'submit' in content_lower:
                functionality['features'].append('Akční tlačítka')
            if 'ajax' in content_lower or 'xmlhttprequest' in content_lower:
                functionality['features'].append('AJAX komunikace')
            if 'validate' in content_lower or 'validation' in content_lower:
                functionality['features'].append('Validace vstupů')
            if 'export' in content_lower or 'excel' in content_lower:
                functionality['features'].append('Export dat')
            if 'import' in content_lower:
                functionality['features'].append('Import dat')
            if 'search' in content_lower or 'filter' in content_lower:
                functionality['features'].append('Vyhledávání/filtrování')
            if 'delete' in content_lower or 'remove' in content_lower:
                functionality['features'].append('Mazání záznamů')
            if 'save' in content_lower or 'ulozit' in content_lower:
                functionality['features'].append('Ukládání dat')

        # Analyze based on page name patterns
        if 'doklad' in page_name.lower():
            functionality['domain'] = 'Správa dokladů'
        elif 'budget' in page_name.lower():
            functionality['domain'] = 'Budgetování'
        elif 'evi' in page_name.lower():
            functionality['domain'] = 'Evidence'
        elif 'doc' in page_name.lower():
            functionality['domain'] = 'Dokumenty'
        elif 'pb' in page_name.lower():
            functionality['domain'] = 'Projektový budget'
        elif 'ifrs' in page_name.lower():
            functionality['domain'] = 'IFRS reporting'
        else:
            functionality['domain'] = 'Obecné'

        return functionality

    def generate_page_descriptions(self):
        """Generovat popisy všech stránek."""
        print("\n=== GENEROVÁNÍ POPISŮ STRÁNEK ===\n")

        os.makedirs(os.path.join(self.output_dir, 'pages'), exist_ok=True)

        with self.driver.session() as session:
            result = session.run("""
                MATCH (jsp)
                WHERE jsp.chunk_type = 'jsp_page'
                RETURN DISTINCT jsp.name as page_name,
                       jsp.file_path as file_path,
                       jsp.chunk_id as chunk_id
                ORDER BY page_name
                LIMIT 100
            """)

            pages = list(result)

            # Remove duplicates by page_name
            seen = set()
            unique_pages = []
            for page in pages:
                if page['page_name'] not in seen:
                    seen.add(page['page_name'])
                    unique_pages.append(page)
            pages = unique_pages

        print(f"Zpracovávám {len(pages)} stránek...")

        # Categorize and describe
        categories = {}
        page_count = 0

        for record in pages[:100]:  # Process first 100 pages
            page_name = record['page_name']
            chunk_id = record['chunk_id']
            file_path = record['file_path']

            # Get content from Qdrant
            content = self.get_page_content_from_qdrant(chunk_id)

            # Analyze functionality
            func = self.analyze_page_functionality(page_name, content)

            domain = func.get('domain', 'Obecné')
            if domain not in categories:
                categories[domain] = []

            categories[domain].append({
                'name': page_name,
                'file_path': file_path,
                'type': func['type'],
                'purpose': func['purpose'],
                'features': func['features']
            })

            page_count += 1
            if page_count % 10 == 0:
                print(f"  Zpracováno {page_count}/{len(pages)} stránek...")

        # Generate catalog with descriptions
        catalog_path = os.path.join(self.output_dir, 'pages', 'DESCRIPTIONS.md')
        with open(catalog_path, 'w', encoding='utf-8') as f:
            f.write("# Popisy funkcionalit JSP stránek\n\n")
            f.write(f"Analyzováno: **{page_count}** stránek\n\n")

            for domain, pages_list in sorted(categories.items()):
                f.write(f"## {domain}\n\n")

                for page in pages_list:
                    f.write(f"### {page['name']}\n\n")
                    f.write(f"**Soubor:** `{page['file_path']}`\n\n")
                    f.write(f"**Typ:** {page['type']}\n\n")
                    f.write(f"**Účel:** {page['purpose']}\n\n")

                    if page['features']:
                        f.write("**Funkce:**\n")
                        for feature in page['features']:
                            f.write(f"- {feature}\n")
                        f.write("\n")
                    else:
                        f.write("**Funkce:** Základní zobrazení/editace\n\n")

        print(f"\n✅ Katalog popisů: {catalog_path}")

        # Generate summary
        summary_path = os.path.join(self.output_dir, 'pages', 'FUNCTIONAL_SUMMARY.md')
        with open(summary_path, 'w', encoding='utf-8') as f:
            f.write("# Funkční souhrn stránek podle domén\n\n")

            for domain, pages_list in sorted(categories.items()):
                f.write(f"## {domain} ({len(pages_list)} stránek)\n\n")

                # Count by type
                type_counts = {}
                for page in pages_list:
                    ptype = page['type']
                    type_counts[ptype] = type_counts.get(ptype, 0) + 1

                f.write("**Typy stránek:**\n")
                for ptype, count in sorted(type_counts.items()):
                    f.write(f"- {ptype}: {count}\n")
                f.write("\n")

                # List key features
                all_features = []
                for page in pages_list:
                    all_features.extend(page['features'])

                if all_features:
                    feature_counts = {}
                    for feat in all_features:
                        feature_counts[feat] = feature_counts.get(feat, 0) + 1

                    f.write("**Nejčastější funkce:**\n")
                    for feat, count in sorted(feature_counts.items(), key=lambda x: x[1], reverse=True)[:5]:
                        f.write(f"- {feat} ({count}x)\n")
                    f.write("\n")

        print(f"✅ Funkční souhrn: {summary_path}")

    def run_generation(self):
        """Spustit generování."""
        try:
            self.generate_page_descriptions()

            print("\n" + "="*60)
            print("✅ GENEROVÁNÍ POPISŮ DOKONČENO")
            print("="*60)

        finally:
            if self.driver:
                self.driver.close()


if __name__ == "__main__":
    output_dir = "/Users/radektuma/DEV/KIS/analýza_20251127"
    generator = PageDescriptionGenerator(output_dir)
    generator.run_generation()
