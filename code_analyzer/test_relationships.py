"""Test - jaké vztahy máme mezi JSP, SQL a Java."""
from neo4j import GraphDatabase
from config import NEO4J_URI, NEO4J_USER, NEO4J_PASSWORD

driver = GraphDatabase.driver(NEO4J_URI, auth=(NEO4J_USER, NEO4J_PASSWORD))

with driver.session() as session:
    # Test 1: JSP -> SQL vztahy
    print("=== JSP -> SQL TABULKY ===")
    result = session.run("""
        MATCH (jsp)-[r:DEPENDS_ON]->(sql)
        WHERE jsp.chunk_type = 'jsp_page' 
          AND sql.chunk_type = 'sql_table'
        RETURN jsp.name as jsp_page, 
               sql.name as table_name,
               r.type as rel_type
        LIMIT 5
    """)
    for record in result:
        print(f"  {record['jsp_page']} -> {record['table_name']} ({record['rel_type']})")
    
    # Test 2: JSP -> Java metody
    print("\n=== JSP -> JAVA METODY ===")
    result = session.run("""
        MATCH (jsp)-[r:DEPENDS_ON]->(method)
        WHERE jsp.chunk_type = 'jsp_page' 
          AND method.chunk_type = 'java_method'
        RETURN jsp.name as jsp_page, 
               method.name as method_name,
               r.type as rel_type
        LIMIT 5
    """)
    for record in result:
        print(f"  {record['jsp_page']} -> {record['method_name']} ({record['rel_type']})")
    
    # Test 3: SQL sloupce
    print("\n=== SQL TABULKY -> SLOUPCE ===")
    result = session.run("""
        MATCH (table)-[r:DEPENDS_ON]->(column)
        WHERE table.chunk_type = 'sql_table'
          AND column.chunk_type = 'sql_column'
        RETURN table.name as table_name,
               column.name as column_name
        LIMIT 10
    """)
    for record in result:
        print(f"  {record['table_name']}.{record['column_name']}")

driver.close()
