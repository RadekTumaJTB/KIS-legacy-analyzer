"""Add visual labels to Neo4j nodes for better visualization."""
from neo4j import GraphDatabase
from config import NEO4J_URI, NEO4J_USER, NEO4J_PASSWORD


def check_chunk_types():
    """Check what chunk types actually exist in the database."""
    driver = GraphDatabase.driver(NEO4J_URI, auth=(NEO4J_USER, NEO4J_PASSWORD))

    with driver.session() as session:
        print("\n=== CHUNK TYPES V DATABÁZI ===")
        result = session.run("""
            MATCH (c:CodeChunk)
            RETURN c.chunk_type as type, count(*) as count
            ORDER BY count DESC
        """)

        for record in result:
            print(f"  {record['type']}: {record['count']:,}")

        print("\n=== SQL ZÁVISLOSTI ===")
        # Zkontroluj SQL -> SQL závislosti
        sql_deps = session.run("""
            MATCH (c:CodeChunk)-[r:DEPENDS_ON]->(related:CodeChunk)
            WHERE c.chunk_type STARTS WITH 'sql_'
              AND related.chunk_type STARTS WITH 'sql_'
            RETURN count(r) as sql_to_sql_deps
        """).single()
        print(f"  SQL -> SQL závislosti: {sql_deps['sql_to_sql_deps']:,}")

        # Zkontroluj Java -> SQL závislosti
        java_sql_deps = session.run("""
            MATCH (c:CodeChunk)-[r:DEPENDS_ON]->(related:CodeChunk)
            WHERE c.chunk_type STARTS WITH 'java_'
              AND related.chunk_type STARTS WITH 'sql_'
            RETURN count(r) as java_to_sql_deps
        """).single()
        print(f"  Java -> SQL závislosti: {java_sql_deps['java_to_sql_deps']:,}")

        # Ukázka SQL nodů
        print("\n=== UKÁZKA SQL NODŮ ===")
        sql_samples = session.run("""
            MATCH (c:CodeChunk)
            WHERE c.chunk_type STARTS WITH 'sql_'
            RETURN c.chunk_type as type, c.name as name, c.chunk_id as id
            LIMIT 10
        """)
        for record in sql_samples:
            print(f"  {record['type']}: {record['name']} (ID: {record['id'][:30]}...)")

    driver.close()


def add_visual_labels():
    """Add additional labels to nodes for visual distinction."""
    driver = GraphDatabase.driver(NEO4J_URI, auth=(NEO4J_USER, NEO4J_PASSWORD))

    label_mappings = {
        'java_class': 'JavaClass',
        'java_method': 'JavaMethod',
        'java_interface': 'JavaInterface',
        'jsp_page': 'JspPage',
        'jsp_scriptlet': 'JspScriptlet',
        'sql_table': 'SqlTable',
        'sql_procedure': 'SqlProcedure',
        'sql_package': 'SqlPackage',
        'sql_view': 'SqlView',
        'sql_function': 'SqlFunction',
        'sql_trigger': 'SqlTrigger',
    }

    with driver.session() as session:
        print("\n=== PŘIDÁVÁM VISUAL LABELS ===")

        for chunk_type, label in label_mappings.items():
            result = session.run(f"""
                MATCH (c:CodeChunk)
                WHERE c.chunk_type = $chunk_type
                SET c:{label}
                RETURN count(c) as updated_count
            """, chunk_type=chunk_type)

            count = result.single()['updated_count']
            if count > 0:
                print(f"  ✓ {label}: {count:,} nodů")

    driver.close()
    print("\n✅ Labels přidány!")


def generate_cypher_queries():
    """Generate useful Cypher queries for visualization."""
    queries = {
        "1. Statistika typů chunks": """
MATCH (c:CodeChunk)
RETURN c.chunk_type as type, count(*) as count
ORDER BY count DESC;
""",

        "2. Java třídy a jejich závislosti": """
MATCH (c:JavaClass)-[r:DEPENDS_ON]-(related)
RETURN c, r, related
LIMIT 100;
""",

        "3. SQL tabulky a jejich vztahy": """
MATCH (t:SqlTable)
OPTIONAL MATCH (t)-[r:DEPENDS_ON]-(related)
RETURN t, r, related
LIMIT 50;
""",

        "4. Java -> SQL závislosti (queries)": """
MATCH (java:CodeChunk)-[r:DEPENDS_ON]->(sql:CodeChunk)
WHERE java.chunk_type IN ['java_class', 'java_method']
  AND sql.chunk_type STARTS WITH 'sql_'
RETURN java, r, sql
LIMIT 100;
""",

        "5. JSP -> Java/SQL závislosti": """
MATCH (jsp:CodeChunk)-[r:DEPENDS_ON]->(target:CodeChunk)
WHERE jsp.chunk_type IN ['jsp_page', 'jsp_scriptlet']
RETURN jsp, r, target
LIMIT 100;
""",

        "6. Nejpoužívanější SQL tabulky": """
MATCH (t:CodeChunk)<-[r:DEPENDS_ON]-(other)
WHERE t.chunk_type = 'sql_table'
WITH t, count(r) as usage_count
WHERE usage_count > 0
RETURN t.name as table_name,
       t.package as schema,
       usage_count
ORDER BY usage_count DESC
LIMIT 20;
""",

        "7. Nejdůležitější Java třídy": """
MATCH (c:JavaClass)
OPTIONAL MATCH (c)-[r:DEPENDS_ON]-()
WITH c, count(r) as connection_count
WHERE connection_count > 10
RETURN c.name as class_name,
       c.package as package,
       connection_count
ORDER BY connection_count DESC
LIMIT 20;
""",

        "8. Package cross-dependencies": """
MATCH (source:CodeChunk)-[r:DEPENDS_ON]->(target:CodeChunk)
WHERE source.package <> target.package
  AND source.package IS NOT NULL
  AND target.package IS NOT NULL
RETURN source.package as from_package,
       target.package as to_package,
       count(r) as dependency_count
ORDER BY dependency_count DESC
LIMIT 30;
""",

        "9. Orphaned chunks (bez závislostí)": """
MATCH (c:CodeChunk)
WHERE NOT (c)-[:DEPENDS_ON]-()
RETURN c.chunk_type as type,
       count(c) as orphaned_count
ORDER BY orphaned_count DESC;
""",

        "10. Vyhledání podle názvu (např. 'Logging')": """
MATCH (c:CodeChunk)
WHERE c.name CONTAINS 'Logging'
OPTIONAL MATCH (c)-[r:DEPENDS_ON]-(related)
RETURN c, r, related
LIMIT 50;
"""
    }

    print("\n=== FUNKČNÍ CYPHER DOTAZY PRO NEO4J BROWSER ===\n")
    for title, query in queries.items():
        print(f"// {title}")
        print(query)
        print()


if __name__ == "__main__":
    print("🔍 Kontrola Neo4j databáze...")

    # Nejprve zkontroluj, co je v databázi
    check_chunk_types()

    # Přidej visual labels
    add_visual_labels()

    # Vygeneruj funkční dotazy
    generate_cypher_queries()

    print("\n📋 NÁVOD PRO NEO4J BROWSER:")
    print("1. Otevři http://localhost:7474")
    print("2. Přihlas se: neo4j / kis_password_2024")
    print("3. Zkopíruj dotazy výše do Neo4j Browser")
    print("4. Pro změnu barev: klikni na barevný kruh vedle labelu v levém panelu")
    print("5. Nastav barvy:")
    print("   - JavaClass: modrá")
    print("   - JavaMethod: fialová")
    print("   - SqlTable: červená")
    print("   - JspPage: zelená")
    print("   atd.")
