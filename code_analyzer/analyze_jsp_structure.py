"""Analyze JSP structure and dependencies to verify graph accuracy."""
from neo4j import GraphDatabase
from config import NEO4J_URI, NEO4J_USER, NEO4J_PASSWORD


def analyze_jsp_dependencies():
    """Analyze what JSP pages are connected to."""
    driver = GraphDatabase.driver(NEO4J_URI, auth=(NEO4J_USER, NEO4J_PASSWORD))

    with driver.session() as session:
        print("\n=== ANALÝZA JSP ZÁVISLOSTÍ ===\n")

        # 1. Kolik JSP stránek existuje?
        print("1. POČET JSP STRÁNEK:")
        result = session.run("""
            MATCH (jsp:JspPage)
            RETURN count(jsp) as jsp_count
        """).single()
        print(f"   Celkem JSP pages: {result['jsp_count']}")

        # 2. Na co se JSP stránky napojují?
        print("\n2. JSP STRÁNKY SE NAPOJUJÍ NA:")
        result = session.run("""
            MATCH (jsp:JspPage)-[r:DEPENDS_ON]-(related)
            RETURN labels(related) as related_labels,
                   count(*) as count
            ORDER BY count DESC
        """)
        for record in result:
            print(f"   {record['related_labels'][0]:20} → {record['count']:,} spojení")

        # 3. Jsou JSP stránky propojené mezi sebou?
        print("\n3. JSP -> JSP PROPOJENÍ:")
        result = session.run("""
            MATCH (jsp1:JspPage)-[r:DEPENDS_ON]-(jsp2:JspPage)
            RETURN count(r) as jsp_to_jsp_count
        """).single()
        print(f"   JSP -> JSP závislosti: {result['jsp_to_jsp_count']}")

        # 4. Jsou JSP scriptlety propojené s JSP stránkami?
        print("\n4. JSP PAGE ↔ JSP SCRIPTLET:")
        result = session.run("""
            MATCH (page:JspPage)-[r:DEPENDS_ON]-(scriptlet:JspScriptlet)
            RETURN count(r) as page_scriptlet_count
        """).single()
        print(f"   JspPage ↔ JspScriptlet: {result['page_scriptlet_count']}")

        # 5. Ukázka konkrétních JSP závislostí
        print("\n5. UKÁZKA KONKRÉTNÍCH JSP ZÁVISLOSTÍ:")
        result = session.run("""
            MATCH (jsp:JspPage)-[r:DEPENDS_ON]-(related)
            RETURN jsp.name as jsp_name,
                   labels(related)[0] as related_type,
                   related.name as related_name,
                   type(r) as rel_type
            LIMIT 10
        """)
        for record in result:
            print(f"   {record['jsp_name']:30} → {record['related_type']:15} {record['related_name']}")

        # 6. JSP stránky bez závislostí
        print("\n6. IZOLOVANÉ JSP STRÁNKY (bez závislostí):")
        result = session.run("""
            MATCH (jsp:JspPage)
            WHERE NOT (jsp)-[:DEPENDS_ON]-()
            RETURN count(jsp) as isolated_count
        """).single()
        print(f"   Izolované JSP stránky: {result['isolated_count']}")

        # 7. Průměrný počet závislostí na JSP stránku
        print("\n7. STATISTIKA ZÁVISLOSTÍ:")
        result = session.run("""
            MATCH (jsp:JspPage)
            OPTIONAL MATCH (jsp)-[r:DEPENDS_ON]-()
            WITH jsp, count(r) as dep_count
            RETURN min(dep_count) as min_deps,
                   max(dep_count) as max_deps,
                   avg(dep_count) as avg_deps
        """).single()
        print(f"   Min závislostí: {result['min_deps']}")
        print(f"   Max závislostí: {result['max_deps']}")
        print(f"   Průměr závislostí: {result['avg_deps']:.1f}")

        # 8. TOP JSP stránky podle počtu závislostí
        print("\n8. TOP JSP STRÁNKY (nejvíce závislostí):")
        result = session.run("""
            MATCH (jsp:JspPage)
            OPTIONAL MATCH (jsp)-[r:DEPENDS_ON]-()
            WITH jsp, count(r) as dep_count
            WHERE dep_count > 0
            RETURN jsp.name as jsp_name,
                   jsp.file_path as file_path,
                   dep_count
            ORDER BY dep_count DESC
            LIMIT 10
        """)
        for record in result:
            filename = record['file_path'].split('/')[-1] if record['file_path'] else 'N/A'
            print(f"   {filename:40} → {record['dep_count']:3} závislostí")

        # 9. Typ závislostí (IMPORTS, CALLS, atd.)
        print("\n9. TYPY ZÁVISLOSTÍ U JSP:")
        result = session.run("""
            MATCH (jsp:JspPage)-[r:DEPENDS_ON]-(related)
            RETURN r.type as dep_type, count(*) as count
            ORDER BY count DESC
        """)
        dep_types_found = False
        for record in result:
            dep_types_found = True
            print(f"   {record['dep_type']:20} → {record['count']:,} závislostí")
        if not dep_types_found:
            print("   (Typy závislostí nejsou nastaveny v relationship properties)")

    driver.close()


def check_scriptlets():
    """Check JSP scriptlet structure."""
    driver = GraphDatabase.driver(NEO4J_URI, auth=(NEO4J_USER, NEO4J_PASSWORD))

    with driver.session() as session:
        print("\n\n=== ANALÝZA JSP SCRIPTLETŮ ===\n")

        print("1. POČET SCRIPTLETŮ:")
        result = session.run("""
            MATCH (s:JspScriptlet)
            RETURN count(s) as scriptlet_count
        """).single()
        print(f"   Celkem JspScriptlet: {result['scriptlet_count']:,}")

        print("\n2. SCRIPTLET ZÁVISLOSTI:")
        result = session.run("""
            MATCH (s:JspScriptlet)-[r:DEPENDS_ON]-(related)
            RETURN labels(related)[0] as related_type, count(*) as count
            ORDER BY count DESC
        """)
        for record in result:
            print(f"   {record['related_type']:20} → {record['count']:,} spojení")

        print("\n3. UKÁZKA SCRIPTLET ZÁVISLOSTÍ:")
        result = session.run("""
            MATCH (s:JspScriptlet)-[r:DEPENDS_ON]-(related)
            RETURN s.name as scriptlet_name,
                   labels(related)[0] as related_type,
                   related.name as related_name
            LIMIT 10
        """)
        for record in result:
            print(f"   {record['scriptlet_name']:30} → {record['related_type']:15} {record['related_name']}")

    driver.close()


if __name__ == "__main__":
    print("🔍 Analyzuji strukturu JSP v KIS aplikaci...")

    analyze_jsp_dependencies()
    check_scriptlets()

    print("\n✅ ANALÝZA DOKONČENA")
    print("\n💡 INTERPRETACE:")
    print("   - Pokud jsou JSP stránky propojené hlavně s JavaMethod, je to normální")
    print("   - JSP stránky obvykle volají Java business logiku (metody, třídy)")
    print("   - JSP -> JSP závislosti jsou vzácné (stránky se navzájem nevolají)")
    print("   - JspScriptlet jsou kousky kódu uvnitř JSP stránek")
