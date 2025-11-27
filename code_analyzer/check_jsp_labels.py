"""Check actual labels on JSP nodes."""
from neo4j import GraphDatabase
from config import NEO4J_URI, NEO4J_USER, NEO4J_PASSWORD


def check_jsp_labels():
    """Check what labels JSP nodes actually have."""
    driver = GraphDatabase.driver(NEO4J_URI, auth=(NEO4J_USER, NEO4J_PASSWORD))

    with driver.session() as session:
        print("\n=== KONTROLA JSP LABELS ===\n")

        # Check all labels in database
        print("=== VŠECHNY LABELS V DATABÁZI ===")
        result = session.run("CALL db.labels()")
        for record in result:
            print(f"  {record[0]}")

        # Check nodes with chunk_type = jsp_page
        print("\n=== UZLY S chunk_type='jsp_page' ===")
        result = session.run("""
            MATCH (n)
            WHERE n.chunk_type = 'jsp_page'
            RETURN labels(n) as node_labels, count(*) as count
        """)
        for record in result:
            print(f"  Labels: {record['node_labels']} → {record['count']} uzlů")

        # Check nodes with chunk_type = jsp_scriptlet
        print("\n=== UZLY S chunk_type='jsp_scriptlet' ===")
        result = session.run("""
            MATCH (n)
            WHERE n.chunk_type = 'jsp_scriptlet'
            RETURN labels(n) as node_labels, count(*) as count
        """)
        for record in result:
            print(f"  Labels: {record['node_labels']} → {record['count']} uzlů")

        # Check CONTAINS relationships
        print("\n=== CONTAINS RELATIONSHIPS (first 10) ===")
        result = session.run("""
            MATCH (source)-[r:DEPENDS_ON]->(target)
            WHERE r.type = 'contains'
            RETURN
                labels(source) as source_labels,
                source.chunk_type as source_type,
                source.name as source_name,
                labels(target) as target_labels,
                target.chunk_type as target_type,
                target.name as target_name
            LIMIT 10
        """)
        for record in result:
            print(f"\n  Source: {record['source_labels']} ({record['source_type']}) - {record['source_name']}")
            print(f"  Target: {record['target_labels']} ({record['target_type']}) - {record['target_name']}")

        # Count CONTAINS by source/target chunk types
        print("\n=== CONTAINS PODLE CHUNK TYPES ===")
        result = session.run("""
            MATCH (source)-[r:DEPENDS_ON]->(target)
            WHERE r.type = 'contains'
            RETURN
                source.chunk_type as source_type,
                target.chunk_type as target_type,
                count(*) as count
            ORDER BY count DESC
        """)
        for record in result:
            print(f"  {record['source_type']:20} → {record['target_type']:20} = {record['count']:,}")

    driver.close()


if __name__ == "__main__":
    check_jsp_labels()
