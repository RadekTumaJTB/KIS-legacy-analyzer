"""Test - cesta k SQL tabulkám přes scriptlety."""
from neo4j import GraphDatabase
from config import NEO4J_URI, NEO4J_USER, NEO4J_PASSWORD

driver = GraphDatabase.driver(NEO4J_URI, auth=(NEO4J_USER, NEO4J_PASSWORD))

with driver.session() as session:
    # Test: JSP -> scriptlet -> SQL
    print("=== JSP -> SCRIPTLET -> SQL ===")
    result = session.run("""
        MATCH path = (jsp)-[:DEPENDS_ON*1..2]->(sql)
        WHERE jsp.chunk_type = 'jsp_page' 
          AND sql.chunk_type = 'sql_table'
        RETURN jsp.name as jsp_page,
               sql.name as table_name,
               [r in relationships(path) | r.type] as path_types
        LIMIT 10
    """)
    count = 0
    for record in result:
        print(f"  {record['jsp_page']} -> {record['table_name']} via {record['path_types']}")
        count += 1
    print(f"Nalezeno: {count} vztahů")
    
    # Test: Jaké typy vztahů vedou k SQL tabulkám?
    print("\n=== VZTAHY K SQL TABULKÁM ===")
    result = session.run("""
        MATCH (n)-[r:DEPENDS_ON]->(sql)
        WHERE sql.chunk_type = 'sql_table'
        RETURN DISTINCT n.chunk_type as source_type, 
               r.type as rel_type,
               count(*) as count
        ORDER BY count DESC
        LIMIT 10
    """)
    for record in result:
        print(f"  {record['source_type']} --[{record['rel_type']}]-> sql_table ({record['count']}x)")

driver.close()
