"""Check CONTAINS dependencies between JSP pages and scriptlets."""
from neo4j import GraphDatabase
from config import NEO4J_URI, NEO4J_USER, NEO4J_PASSWORD


def check_contains_deps():
    """Check JSP page -> scriptlet CONTAINS dependencies."""
    driver = GraphDatabase.driver(NEO4J_URI, auth=(NEO4J_USER, NEO4J_PASSWORD))

    with driver.session() as session:
        print("\n=== KONTROLA CONTAINS ZÁVISLOSTÍ ===\n")

        # Count CONTAINS dependencies
        result = session.run("""
            MATCH (page:JspPage)-[r:DEPENDS_ON]->(scriptlet:JspScriptlet)
            WHERE r.type = 'contains'
            RETURN count(r) as contains_count
        """).single()

        contains_count = result['contains_count']
        print(f"JSP Page -> Scriptlet (CONTAINS): {contains_count:,}")

        if contains_count > 0:
            # Show examples
            print("\n=== UKÁZKA CONTAINS ZÁVISLOSTÍ ===")
            result = session.run("""
                MATCH (page:JspPage)-[r:DEPENDS_ON]->(scriptlet:JspScriptlet)
                WHERE r.type = 'contains'
                RETURN page.name as page_name,
                       count(scriptlet) as scriptlet_count
                ORDER BY scriptlet_count DESC
                LIMIT 10
            """)

            for record in result:
                print(f"  {record['page_name']:40} → {record['scriptlet_count']} scriptletů")
        else:
            print("\n⚠️  PROBLÉM: Žádné CONTAINS závislosti!")
            print("Parser pravděpodobně neuložil scriptlet_ids správně.")

        # Check all dependency types
        print("\n=== VŠECHNY TYPY ZÁVISLOSTÍ ===")
        result = session.run("""
            MATCH ()-[r:DEPENDS_ON]->()
            RETURN r.type as dep_type, count(*) as count
            ORDER BY count DESC
        """)

        for record in result:
            print(f"  {record['dep_type']:20} → {record['count']:,} závislostí")

        # Where did the +18k dependencies come from?
        print("\n=== ZLEPŠENÍ V DEPENDENCY DETECTION ===")

        # Java dependencies
        java_deps = session.run("""
            MATCH (j)-[r:DEPENDS_ON]->(target)
            WHERE (j:JavaClass OR j:JavaMethod OR j:JavaInterface)
            RETURN count(r) as java_dep_count
        """).single()
        print(f"  Java → *: {java_deps['java_dep_count']:,}")

        # JSP dependencies
        jsp_deps = session.run("""
            MATCH (j)-[r:DEPENDS_ON]->(target)
            WHERE (j:JspPage OR j:JspScriptlet)
            RETURN count(r) as jsp_dep_count
        """).single()
        print(f"  JSP → *: {jsp_deps['jsp_dep_count']:,}")

        # SQL dependencies
        sql_deps = session.run("""
            MATCH (s:SqlTable)-[r:DEPENDS_ON]->(target)
            RETURN count(r) as sql_dep_count
        """).single()
        print(f"  SQL → *: {sql_deps['sql_dep_count']:,}")

    driver.close()


if __name__ == "__main__":
    check_contains_deps()
