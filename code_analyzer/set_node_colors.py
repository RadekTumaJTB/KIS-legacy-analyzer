"""Set visual properties (colors, sizes) directly in Neo4j nodes."""
from neo4j import GraphDatabase
from config import NEO4J_URI, NEO4J_USER, NEO4J_PASSWORD


def set_visual_properties():
    """Add color and size properties to nodes for automatic visualization."""
    driver = GraphDatabase.driver(NEO4J_URI, auth=(NEO4J_USER, NEO4J_PASSWORD))

    # Define visual properties for each chunk type
    visual_config = {
        'java_class': {
            'color': '#3B82F6',  # Modrá
            'size': 50,
            'caption': 'name'
        },
        'java_method': {
            'color': '#8B5CF6',  # Fialová
            'size': 25,
            'caption': 'name'
        },
        'java_interface': {
            'color': '#06B6D4',  # Světle modrá
            'size': 45,
            'caption': 'name'
        },
        'jsp_page': {
            'color': '#10B981',  # Zelená
            'size': 40,
            'caption': 'name'
        },
        'jsp_scriptlet': {
            'color': '#F59E0B',  # Žlutá
            'size': 20,
            'caption': 'name'
        },
        'sql_table': {
            'color': '#EF4444',  # Červená
            'size': 35,
            'caption': 'name'
        },
        'sql_procedure': {
            'color': '#F97316',  # Oranžová
            'size': 30,
            'caption': 'name'
        },
        'sql_package': {
            'color': '#A16207',  # Hnědá
            'size': 40,
            'caption': 'name'
        },
        'sql_view': {
            'color': '#EC4899',  # Růžová
            'size': 30,
            'caption': 'name'
        },
        'sql_function': {
            'color': '#F472B6',  # Světle růžová
            'size': 25,
            'caption': 'name'
        },
        'sql_trigger': {
            'color': '#DC2626',  # Tmavě červená
            'size': 25,
            'caption': 'name'
        }
    }

    with driver.session() as session:
        print("\n=== NASTAVUJI BARVY A VELIKOSTI V NEO4J ===\n")

        for chunk_type, props in visual_config.items():
            result = session.run("""
                MATCH (c:CodeChunk)
                WHERE c.chunk_type = $chunk_type
                SET c.color = $color,
                    c.size = $size
                RETURN count(c) as updated_count
            """,
                chunk_type=chunk_type,
                color=props['color'],
                size=props['size']
            )

            count = result.single()['updated_count']
            if count > 0:
                color_preview = props['color']
                print(f"  ✓ {chunk_type:20} → {color_preview:10} size={props['size']:2} ({count:,} nodů)")

        print("\n✅ Barvy a velikosti nastaveny!")
        print("\n📊 LEGENDA BAREV:")
        print("  🔵 #3B82F6 - JavaClass (modrá)")
        print("  🟣 #8B5CF6 - JavaMethod (fialová)")
        print("  🔷 #06B6D4 - JavaInterface (světle modrá)")
        print("  🟢 #10B981 - JspPage (zelená)")
        print("  🟡 #F59E0B - JspScriptlet (žlutá)")
        print("  🔴 #EF4444 - SqlTable (červená)")
        print("  🟠 #F97316 - SqlProcedure (oranžová)")
        print("  🟤 #A16207 - SqlPackage (hnědá)")
        print("  🩷 #EC4899 - SqlView (růžová)")

    driver.close()


def verify_colors():
    """Verify that colors are set correctly."""
    driver = GraphDatabase.driver(NEO4J_URI, auth=(NEO4J_USER, NEO4J_PASSWORD))

    with driver.session() as session:
        print("\n=== KONTROLA NASTAVENÝCH BAREV ===\n")

        result = session.run("""
            MATCH (c:CodeChunk)
            WHERE c.color IS NOT NULL
            RETURN c.chunk_type as type,
                   c.color as color,
                   c.size as size,
                   count(*) as count
            ORDER BY count DESC
        """)

        for record in result:
            print(f"  {record['type']:20} → {record['color']:10} size={record['size']:2} ({record['count']:,} nodů)")

    driver.close()


if __name__ == "__main__":
    print("🎨 Nastavuji barvy a velikosti nodů v Neo4j...")

    # Nastav barvy a velikosti
    set_visual_properties()

    # Ověř, že bylo vše nastaveno
    verify_colors()

    print("\n📋 NÁVOD:")
    print("1. Otevři Neo4j Browser: http://localhost:7474")
    print("2. Spusť dotaz:")
    print("   MATCH (c:JavaClass)-[r:DEPENDS_ON]-(related)")
    print("   RETURN c, r, related")
    print("   LIMIT 100;")
    print("3. Neo4j Browser automaticky použije nastavené barvy!")
    print("4. V pravém panelu můžeš ještě upravit caption na 'name'")
