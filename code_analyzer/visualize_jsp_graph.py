"""Visualize JSP dependency graph."""
import matplotlib.pyplot as plt
import networkx as nx
from neo4j import GraphDatabase
from config import NEO4J_URI, NEO4J_USER, NEO4J_PASSWORD
from collections import defaultdict


def visualize_jsp_graph():
    """Create visualization of JSP dependency graph."""
    driver = GraphDatabase.driver(NEO4J_URI, auth=(NEO4J_USER, NEO4J_PASSWORD))

    # Create directed graph
    G = nx.DiGraph()

    with driver.session() as session:
        print("Načítám JSP dependencies...")

        # Get top 5 JSP pages with most dependencies
        result = session.run("""
            MATCH (jsp)-[r:DEPENDS_ON]->(target)
            WHERE jsp.chunk_type = 'jsp_page'
            WITH jsp, count(r) as dep_count
            ORDER BY dep_count DESC
            LIMIT 5
            RETURN jsp.name as jsp_name, jsp.chunk_id as jsp_id
        """)

        top_jsps = [(record['jsp_name'], record['jsp_id']) for record in result]
        print(f"Top 5 JSP pages: {[name for name, _ in top_jsps]}")

        # For each top JSP, get its dependencies
        for jsp_name, jsp_id in top_jsps:
            result = session.run("""
                MATCH (jsp)-[r:DEPENDS_ON]->(target)
                WHERE jsp.chunk_id = $jsp_id
                RETURN
                    jsp.name as source_name,
                    jsp.chunk_type as source_type,
                    r.type as rel_type,
                    target.name as target_name,
                    target.chunk_type as target_type
                LIMIT 50
            """, jsp_id=jsp_id)

            for record in result:
                source = f"{record['source_name']}\n({record['source_type']})"
                target = f"{record['target_name']}\n({record['target_type']})"

                G.add_edge(source, target,
                          rel_type=record['rel_type'])

    driver.close()

    if len(G.nodes()) == 0:
        print("⚠️  Žádná data pro vizualizaci!")
        return

    print(f"Graf obsahuje {len(G.nodes())} uzlů a {len(G.edges())} hran")

    # Create figure
    plt.figure(figsize=(20, 16))

    # Use hierarchical layout
    try:
        pos = nx.spring_layout(G, k=2, iterations=50, seed=42)
    except:
        pos = nx.circular_layout(G)

    # Color nodes by type
    node_colors = []
    for node in G.nodes():
        if 'jsp_page' in node:
            node_colors.append('#FF6B6B')  # Red for JSP pages
        elif 'jsp_scriptlet' in node:
            node_colors.append('#4ECDC4')  # Teal for scriptlets
        elif 'java_class' in node:
            node_colors.append('#45B7D1')  # Blue for Java classes
        elif 'java_method' in node:
            node_colors.append('#96CEB4')  # Green for Java methods
        elif 'java_interface' in node:
            node_colors.append('#FFEAA7')  # Yellow for interfaces
        else:
            node_colors.append('#DFE6E9')  # Gray for others

    # Draw nodes
    nx.draw_networkx_nodes(G, pos,
                          node_color=node_colors,
                          node_size=1000,
                          alpha=0.9)

    # Draw edges with different colors by type
    edge_colors = []
    for u, v in G.edges():
        rel_type = G[u][v]['rel_type']
        if rel_type == 'contains':
            edge_colors.append('#FF6B6B')
        elif rel_type == 'references':
            edge_colors.append('#4ECDC4')
        elif rel_type == 'calls':
            edge_colors.append('#45B7D1')
        elif rel_type == 'imports':
            edge_colors.append('#96CEB4')
        else:
            edge_colors.append('#DFE6E9')

    nx.draw_networkx_edges(G, pos,
                          edge_color=edge_colors,
                          arrows=True,
                          arrowsize=15,
                          width=1.5,
                          alpha=0.6,
                          connectionstyle='arc3,rad=0.1')

    # Draw labels with smaller font
    labels = {node: node.split('\n')[0][:20] for node in G.nodes()}
    nx.draw_networkx_labels(G, pos, labels,
                           font_size=6,
                           font_weight='bold')

    # Add legend
    from matplotlib.patches import Patch
    legend_elements = [
        Patch(facecolor='#FF6B6B', label='JSP Page'),
        Patch(facecolor='#4ECDC4', label='JSP Scriptlet'),
        Patch(facecolor='#45B7D1', label='Java Class'),
        Patch(facecolor='#96CEB4', label='Java Method'),
        Patch(facecolor='#FFEAA7', label='Java Interface'),
    ]
    plt.legend(handles=legend_elements, loc='upper left', fontsize=10)

    plt.title("JSP Dependency Graph - Top 5 Pages", fontsize=16, fontweight='bold')
    plt.axis('off')
    plt.tight_layout()

    output_file = '/Users/radektuma/DEV/KIS/code_analyzer/jsp_graph.png'
    plt.savefig(output_file, dpi=150, bbox_inches='tight')
    print(f"\n✅ Graf uložen do: {output_file}")

    # Show in window
    plt.show()


if __name__ == "__main__":
    visualize_jsp_graph()
