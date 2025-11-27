"""Detailed analysis of JSP dependencies."""
from neo4j import GraphDatabase
from config import NEO4J_URI, NEO4J_USER, NEO4J_PASSWORD


def analyze_jsp_deps_detail():
    """Analyze JSP dependencies in detail."""
    driver = GraphDatabase.driver(NEO4J_URI, auth=(NEO4J_USER, NEO4J_PASSWORD))

    with driver.session() as session:
        print("\n=== DETAILNÍ ANALÝZA JSP ZÁVISLOSTÍ ===\n")

        # JSP Pages -> All
        print("=== JSP PAGES -> * (podle typu dependency) ===")
        result = session.run("""
            MATCH (jsp)-[r:DEPENDS_ON]->(target)
            WHERE jsp.chunk_type = 'jsp_page'
            RETURN r.type as dep_type, count(*) as count,
                   collect(DISTINCT target.chunk_type)[0..5] as sample_targets
            ORDER BY count DESC
        """)
        jsp_page_total = 0
        for record in result:
            jsp_page_total += record['count']
            print(f"  {record['dep_type']:15} → {record['count']:6,} (targets: {record['sample_targets']})")
        print(f"  {'TOTAL':15} → {jsp_page_total:6,}\n")

        # JSP Scriptlets -> All
        print("=== JSP SCRIPTLETS -> * (podle typu dependency) ===")
        result = session.run("""
            MATCH (scriptlet)-[r:DEPENDS_ON]->(target)
            WHERE scriptlet.chunk_type = 'jsp_scriptlet'
            RETURN r.type as dep_type, count(*) as count,
                   collect(DISTINCT target.chunk_type)[0..5] as sample_targets
            ORDER BY count DESC
        """)
        jsp_scriptlet_total = 0
        for record in result:
            jsp_scriptlet_total += record['count']
            print(f"  {record['dep_type']:15} → {record['count']:6,} (targets: {record['sample_targets']})")
        print(f"  {'TOTAL':15} → {jsp_scriptlet_total:6,}\n")

        # Total JSP dependencies
        print(f"=== CELKEM JSP ZÁVISLOSTÍ ===")
        print(f"  JSP Pages:      {jsp_page_total:6,}")
        print(f"  JSP Scriptlets: {jsp_scriptlet_total:6,}")
        print(f"  TOTAL:          {jsp_page_total + jsp_scriptlet_total:6,}\n")

        # Top JSP pages by dependency count
        print("=== TOP 10 JSP PAGES (podle počtu závislostí) ===")
        result = session.run("""
            MATCH (jsp)-[r:DEPENDS_ON]->(target)
            WHERE jsp.chunk_type = 'jsp_page'
            RETURN jsp.name as page_name,
                   count(r) as dep_count,
                   collect(DISTINCT r.type) as dep_types
            ORDER BY dep_count DESC
            LIMIT 10
        """)
        for record in result:
            print(f"  {record['page_name']:30} → {record['dep_count']:4} deps (types: {record['dep_types']})")

        # JSP pages with NO dependencies (besides CONTAINS)
        print("\n=== JSP PAGES BEZ ZÁVISLOSTÍ (kromě CONTAINS) ===")
        result = session.run("""
            MATCH (jsp)
            WHERE jsp.chunk_type = 'jsp_page'
            WITH jsp
            OPTIONAL MATCH (jsp)-[r:DEPENDS_ON]->(target)
            WHERE r.type <> 'contains'
            WITH jsp, count(r) as non_contains_count
            WHERE non_contains_count = 0
            RETURN count(jsp) as isolated_count
        """).single()
        print(f"  {result['isolated_count']} pages (z 644 celkem)\n")

        # Sample JSP scriptlet dependencies
        print("=== UKÁZKA JSP SCRIPTLET DEPENDENCIES ===")
        result = session.run("""
            MATCH (scriptlet)-[r:DEPENDS_ON]->(target)
            WHERE scriptlet.chunk_type = 'jsp_scriptlet'
            RETURN scriptlet.name as scriptlet_name,
                   r.type as dep_type,
                   target.chunk_type as target_type,
                   target.name as target_name
            LIMIT 20
        """)
        for record in result:
            print(f"  {record['scriptlet_name']:20} --{record['dep_type']:10}--> {record['target_type']:15} ({record['target_name']})")

    driver.close()


if __name__ == "__main__":
    analyze_jsp_deps_detail()
