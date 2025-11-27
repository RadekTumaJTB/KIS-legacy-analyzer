"""Fix node labels for automatic color assignment in Neo4j Browser."""
from neo4j import GraphDatabase
from config import NEO4J_URI, NEO4J_USER, NEO4J_PASSWORD


def check_current_labels():
    """Check current label setup."""
    driver = GraphDatabase.driver(NEO4J_URI, auth=(NEO4J_USER, NEO4J_PASSWORD))

    with driver.session() as session:
        print("\n=== SOUČASNÉ LABELY ===")
        result = session.run("""
            MATCH (c)
            WITH DISTINCT labels(c) as node_labels
            RETURN node_labels
            LIMIT 10
        """)

        for record in result:
            print(f"  {record['node_labels']}")

    driver.close()


def fix_labels_for_auto_colors():
    """Remove CodeChunk label so specific labels become primary."""
    driver = GraphDatabase.driver(NEO4J_URI, auth=(NEO4J_USER, NEO4J_PASSWORD))

    label_mappings = {
        'JavaClass': 'java_class',
        'JavaMethod': 'java_method',
        'JavaInterface': 'java_interface',
        'JspPage': 'jsp_page',
        'JspScriptlet': 'jsp_scriptlet',
        'SqlTable': 'sql_table',
        'SqlProcedure': 'sql_procedure',
        'SqlPackage': 'sql_package',
        'SqlView': 'sql_view',
    }

    with driver.session() as session:
        print("\n=== ODSTRAŇUJI PRIMÁRNÍ LABEL CodeChunk ===\n")

        for label, chunk_type in label_mappings.items():
            # Remove CodeChunk label from nodes that have specific label
            result = session.run(f"""
                MATCH (c:CodeChunk:{label})
                REMOVE c:CodeChunk
                RETURN count(c) as updated_count
            """)

            count = result.single()['updated_count']
            if count > 0:
                print(f"  ✓ {label:20} → odstraněn CodeChunk label ({count:,} nodů)")

        # For nodes without specific label, keep CodeChunk
        print("\n=== ZACHOVÁVÁM CodeChunk PRO NODY BEZ SPECIFICKÉHO LABELU ===")
        result = session.run("""
            MATCH (c:CodeChunk)
            WHERE NOT c:JavaClass
              AND NOT c:JavaMethod
              AND NOT c:JavaInterface
              AND NOT c:JspPage
              AND NOT c:JspScriptlet
              AND NOT c:SqlTable
              AND NOT c:SqlProcedure
              AND NOT c:SqlPackage
              AND NOT c:SqlView
            RETURN count(c) as remaining_count
        """)

        remaining = result.single()['remaining_count']
        print(f"  CodeChunk-only nodes: {remaining:,}")

    driver.close()


def verify_new_labels():
    """Verify the new label structure."""
    driver = GraphDatabase.driver(NEO4J_URI, auth=(NEO4J_USER, NEO4J_PASSWORD))

    with driver.session() as session:
        print("\n=== NOVÉ LABELY (po úpravě) ===\n")

        result = session.run("""
            CALL db.labels() YIELD label
            CALL {
                WITH label
                MATCH (n)
                WHERE label IN labels(n)
                RETURN count(n) as count
            }
            RETURN label, count
            ORDER BY count DESC
        """)

        for record in result:
            print(f"  {record['label']:20} → {record['count']:,} nodů")

    driver.close()


if __name__ == "__main__":
    print("🎨 Opravuji labely pro automatické obarvení...")

    # Check current state
    check_current_labels()

    # Fix labels
    fix_labels_for_auto_colors()

    # Verify
    verify_new_labels()

    print("\n✅ HOTOVO!")
    print("\n📋 CO DĚLAT DÁLE:")
    print("1. Otevři Neo4j Browser: http://localhost:7474")
    print("2. Smaž GRASS stylesheet (Settings → vymaž vše v Graph Stylesheet → Apply)")
    print("3. Spusť dotaz:")
    print("   MATCH (jsp:JspPage)-[r:DEPENDS_ON]-(related)")
    print("   RETURN jsp, r, related LIMIT 50;")
    print("4. Neo4j Browser AUTOMATICKY přiřadí barvy každému labelu!")
    print("5. Klikni na barevné kruhy v pravém panelu a uprav názvy na 'name'")
