"""Neo4j graph store for code dependencies."""
from typing import List, Dict
from neo4j import GraphDatabase
from tqdm import tqdm

from ..models import CodeChunk, Dependency
from ..config import NEO4J_URI, NEO4J_USER, NEO4J_PASSWORD


class Neo4jStore:
    """Store for code dependencies in Neo4j graph database."""

    def __init__(self):
        """Initialize Neo4j connection."""
        self.driver = GraphDatabase.driver(
            NEO4J_URI,
            auth=(NEO4J_USER, NEO4J_PASSWORD)
        )

    def close(self):
        """Close Neo4j connection."""
        self.driver.close()

    def initialize_schema(self, clear_existing: bool = False):
        """Initialize Neo4j schema and indexes."""
        with self.driver.session() as session:
            if clear_existing:
                print("Clearing existing graph data...")
                session.run("MATCH (n) DETACH DELETE n")

            # Create indexes
            print("Creating indexes...")
            session.run("""
                CREATE INDEX chunk_id_index IF NOT EXISTS
                FOR (c:CodeChunk) ON (c.chunk_id)
            """)

            session.run("""
                CREATE INDEX chunk_name_index IF NOT EXISTS
                FOR (c:CodeChunk) ON (c.name)
            """)

            session.run("""
                CREATE INDEX file_path_index IF NOT EXISTS
                FOR (c:CodeChunk) ON (c.file_path)
            """)

            print("Schema initialized successfully")

    def create_chunk_nodes(self, chunks: List[CodeChunk], batch_size: int = 1000):
        """
        Create nodes for code chunks.

        Args:
            chunks: List of CodeChunk objects
            batch_size: Number of chunks to create per batch
        """
        print(f"Creating {len(chunks)} chunk nodes in Neo4j...")

        with self.driver.session() as session:
            for i in tqdm(range(0, len(chunks), batch_size)):
                batch = chunks[i:i + batch_size]
                chunk_data = [chunk.to_dict() for chunk in batch]

                session.run("""
                    UNWIND $chunks AS chunk
                    MERGE (c:CodeChunk {chunk_id: chunk.id})
                    SET c.name = chunk.name,
                        c.file_path = chunk.file_path,
                        c.chunk_type = chunk.chunk_type,
                        c.language = chunk.language,
                        c.package = chunk.package,
                        c.module = chunk.module,
                        c.start_line = chunk.start_line,
                        c.end_line = chunk.end_line,
                        c.line_count = chunk.end_line - chunk.start_line
                """, chunks=chunk_data)

        print(f"Successfully created {len(chunks)} chunk nodes")

    def create_dependencies(self, dependencies: List[Dependency], batch_size: int = 1000):
        """
        Create relationships for dependencies.

        Args:
            dependencies: List of Dependency objects
            batch_size: Number of dependencies to create per batch
        """
        print(f"Creating {len(dependencies)} dependency relationships...")

        with self.driver.session() as session:
            for i in tqdm(range(0, len(dependencies), batch_size)):
                batch = dependencies[i:i + batch_size]
                dep_data = [dep.to_dict() for dep in batch]

                session.run("""
                    UNWIND $dependencies AS dep
                    MATCH (source:CodeChunk {chunk_id: dep.source_id})
                    MATCH (target:CodeChunk {chunk_id: dep.target_id})
                    MERGE (source)-[r:DEPENDS_ON {type: dep.dependency_type}]->(target)
                    SET r.metadata = dep.metadata
                """, dependencies=dep_data)

        print(f"Successfully created {len(dependencies)} dependency relationships")

    def analyze_dependencies(self) -> Dict:
        """Analyze dependency patterns."""
        with self.driver.session() as session:
            # Count total chunks and dependencies
            stats = session.run("""
                MATCH (c:CodeChunk)
                OPTIONAL MATCH (c)-[r:DEPENDS_ON]->()
                RETURN count(DISTINCT c) as chunk_count,
                       count(r) as dependency_count
            """).single()

            # Find highly connected chunks
            highly_connected = session.run("""
                MATCH (c:CodeChunk)
                OPTIONAL MATCH (c)-[r:DEPENDS_ON]-()
                WITH c, count(r) as connection_count
                WHERE connection_count > 0
                RETURN c.chunk_id as chunk_id,
                       c.name as name,
                       c.chunk_type as chunk_type,
                       connection_count
                ORDER BY connection_count DESC
                LIMIT 20
            """).values()

            # Find circular dependencies
            circular = session.run("""
                MATCH path = (c:CodeChunk)-[:DEPENDS_ON*2..5]->(c)
                RETURN c.chunk_id as chunk_id,
                       c.name as name,
                       length(path) as cycle_length
                LIMIT 10
            """).values()

            # Find orphaned chunks (no dependencies)
            orphaned = session.run("""
                MATCH (c:CodeChunk)
                WHERE NOT (c)-[:DEPENDS_ON]-()
                      AND NOT ()-[:DEPENDS_ON]->(c)
                RETURN count(c) as orphaned_count
            """).single()

            return {
                "total_chunks": stats["chunk_count"],
                "total_dependencies": stats["dependency_count"],
                "highly_connected": [
                    {
                        "chunk_id": row[0],
                        "name": row[1],
                        "chunk_type": row[2],
                        "connections": row[3]
                    }
                    for row in highly_connected
                ],
                "circular_dependencies": [
                    {
                        "chunk_id": row[0],
                        "name": row[1],
                        "cycle_length": row[2]
                    }
                    for row in circular
                ],
                "orphaned_chunks": orphaned["orphaned_count"]
            }

    def get_chunk_dependencies(self, chunk_id: str, depth: int = 1) -> Dict:
        """Get dependencies for a specific chunk."""
        with self.driver.session() as session:
            result = session.run("""
                MATCH path = (c:CodeChunk {chunk_id: $chunk_id})-[:DEPENDS_ON*1..$depth]->(dep)
                RETURN c.chunk_id as source_id,
                       c.name as source_name,
                       collect(DISTINCT dep.chunk_id) as dependency_ids,
                       collect(DISTINCT dep.name) as dependency_names
            """, chunk_id=chunk_id, depth=depth).single()

            if result:
                return {
                    "chunk_id": result["source_id"],
                    "name": result["source_name"],
                    "dependencies": list(zip(
                        result["dependency_ids"],
                        result["dependency_names"]
                    ))
                }
            return {}

    def find_migration_clusters(self) -> List[Dict]:
        """Identify clusters of related code for migration planning."""
        with self.driver.session() as session:
            # Use community detection or simple clustering
            result = session.run("""
                MATCH (c:CodeChunk)
                OPTIONAL MATCH (c)-[:DEPENDS_ON]-(related:CodeChunk)
                WITH c, collect(DISTINCT related.chunk_id) as related_chunks
                WHERE size(related_chunks) > 3
                RETURN c.chunk_id as chunk_id,
                       c.name as name,
                       c.package as package,
                       size(related_chunks) as cluster_size
                ORDER BY cluster_size DESC
                LIMIT 50
            """).values()

            return [
                {
                    "chunk_id": row[0],
                    "name": row[1],
                    "package": row[2],
                    "cluster_size": row[3]
                }
                for row in result
            ]
