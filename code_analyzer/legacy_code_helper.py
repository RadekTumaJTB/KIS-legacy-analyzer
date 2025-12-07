#!/usr/bin/env python3
"""
Legacy Code Helper CLI Tool

Purpose:
- Query Qdrant vector database for similar code patterns from legacy Java 1.4 Windows 2003 codebase
- Query Neo4j graph database for code dependencies and relationships
- Compare current Java 17 Linux implementation with legacy code
- Identify migration patterns (Windows paths → Linux, Java 1.4 → Java 17)

Usage:
    python legacy_code_helper.py search "authentication logic"
    python legacy_code_helper.py graph "com.jtbank.kis.budget"
    python legacy_code_helper.py compare kis-bff-simple/src/main/java/cz/jtbank/kis/bff/service/BudgetAggregationService.java
    python legacy_code_helper.py migration-patterns
"""

import sys
import argparse
import requests
import json
from typing import List, Dict, Any, Optional
from pathlib import Path

# Configuration
QDRANT_URL = "http://localhost:6333"
NEO4J_URL = "http://localhost:7474"
NEO4J_DB = "neo4j"
NEO4J_USER = "neo4j"
NEO4J_PASSWORD = "password"  # Default password for kis-analytics-neo4j

class LegacyCodeHelper:
    def __init__(self):
        self.qdrant_url = QDRANT_URL
        self.neo4j_url = NEO4J_URL
        self.collection_name = None

    def check_connections(self) -> bool:
        """Check if Qdrant and Neo4j are available"""
        try:
            # Check Qdrant (use /collections instead of /health)
            r = requests.get(f"{self.qdrant_url}/collections", timeout=5)
            if r.status_code != 200:
                print("❌ Qdrant not available at", self.qdrant_url)
                return False

            qdrant_data = r.json()
            print(f"✅ Qdrant available ({len(qdrant_data['result']['collections'])} collections)")

            # Check Neo4j
            r = requests.get(self.neo4j_url, timeout=5)
            if r.status_code != 200:
                print("❌ Neo4j not available at", self.neo4j_url)
                return False

            print("✅ Neo4j available")
            return True

        except Exception as e:
            print(f"❌ Connection error: {e}")
            return False

    def list_collections(self) -> List[str]:
        """List available Qdrant collections"""
        try:
            r = requests.get(f"{self.qdrant_url}/collections")
            data = r.json()
            collections = [col['name'] for col in data['result']['collections']]

            print("\n📦 Available Collections:")
            for col in data['result']['collections']:
                col_name = col['name']
                # Get detailed info for each collection
                r2 = requests.get(f"{self.qdrant_url}/collections/{col_name}")
                col_data = r2.json()
                points_count = col_data['result'].get('points_count', 0)
                print(f"  - {col_name} ({points_count:,} points)")

            return collections

        except Exception as e:
            print(f"❌ Error listing collections: {e}")
            return []

    def select_collection(self) -> Optional[str]:
        """Auto-select or prompt for collection"""
        collections = self.list_collections()

        if not collections:
            print("❌ No collections found")
            return None

        # Auto-select if only one collection
        if len(collections) == 1:
            self.collection_name = collections[0]
            print(f"\n✅ Using collection: {self.collection_name}")
            return self.collection_name

        # Prefer kis-related collections
        kis_collections = [c for c in collections if 'kis' in c.lower()]
        if kis_collections:
            self.collection_name = kis_collections[0]
            print(f"\n✅ Auto-selected: {self.collection_name}")
            return self.collection_name

        # Use first collection
        self.collection_name = collections[0]
        print(f"\n✅ Using: {self.collection_name}")
        return self.collection_name

    def semantic_search(self, query: str, limit: int = 5) -> List[Dict[str, Any]]:
        """
        Search for similar code patterns in Qdrant
        Note: This is a simplified version - in production, you'd embed the query text
        """
        if not self.collection_name:
            self.select_collection()

        print(f"\n🔍 Searching for: '{query}'")

        try:
            # Get collection info to determine vector size
            r = requests.get(f"{self.qdrant_url}/collections/{self.collection_name}")
            collection_info = r.json()

            # Try to get vector size
            try:
                vector_size = collection_info['result']['config']['params']['vectors']['size']
            except:
                vector_size = 384  # Default

            # Create a mock query vector (in production, use sentence-transformers)
            import random
            query_vector = [random.random() - 0.5 for _ in range(vector_size)]

            # Search
            search_data = {
                "vector": query_vector,
                "limit": limit,
                "with_payload": True,
                "with_vector": False
            }

            r = requests.post(
                f"{self.qdrant_url}/collections/{self.collection_name}/points/search",
                json=search_data,
                headers={"Content-Type": "application/json"}
            )

            results = r.json()['result']

            print(f"\n📊 Found {len(results)} results:\n")

            for i, result in enumerate(results, 1):
                score = result['score']
                payload = result.get('payload', {})

                print(f"{i}. Score: {score:.4f}")

                # Print relevant payload fields
                if 'file_path' in payload:
                    print(f"   📄 File: {payload['file_path']}")
                if 'function_name' in payload:
                    print(f"   ⚙️  Function: {payload['function_name']}")
                if 'class_name' in payload:
                    print(f"   🏷️  Class: {payload['class_name']}")
                if 'code_snippet' in payload:
                    snippet = payload['code_snippet'][:200]
                    print(f"   📝 Code: {snippet}...")
                if 'java_version' in payload:
                    print(f"   ☕ Java: {payload['java_version']}")
                if 'os_platform' in payload:
                    print(f"   🖥️  Platform: {payload['os_platform']}")

                print()

            return results

        except Exception as e:
            print(f"❌ Search error: {e}")
            return []

    def neo4j_query(self, cypher: str) -> List[Dict[str, Any]]:
        """Execute Cypher query on Neo4j"""
        try:
            query_data = {
                "statements": [
                    {
                        "statement": cypher,
                        "resultDataContents": ["row"]
                    }
                ]
            }

            r = requests.post(
                f"{self.neo4j_url}/db/{NEO4J_DB}/tx/commit",
                json=query_data,
                headers={"Content-Type": "application/json"},
                auth=(NEO4J_USER, NEO4J_PASSWORD)
            )

            data = r.json()

            if 'errors' in data and data['errors']:
                print(f"❌ Neo4j error: {data['errors']}")
                return []

            results = data['results'][0]['data']
            return [item['row'] for item in results]

        except Exception as e:
            print(f"❌ Neo4j query error: {e}")
            return []

    def find_class_dependencies(self, class_pattern: str) -> None:
        """Find dependencies for a class pattern"""
        print(f"\n🔍 Finding dependencies for: {class_pattern}")

        cypher = f"""
        MATCH (class:JavaClass)-[rel:DEPENDS_ON]->(dep)
        WHERE class.name CONTAINS '{class_pattern}'
        RETURN class.name as className,
               dep.name as dependency,
               type(rel) as relType
        LIMIT 20
        """

        results = self.neo4j_query(cypher)

        if results:
            print(f"\n📊 Found {len(results)} dependencies:\n")

            for row in results:
                print(f"  {row[0]} → {row[1]} ({row[2]})")
        else:
            print("  No dependencies found")

    def find_migration_patterns(self) -> None:
        """Find common Windows → Linux migration patterns"""
        print("\n🔄 Analyzing Migration Patterns (Windows 2003 → Linux)\n")

        patterns = [
            {
                "name": "Windows Path Separators",
                "cypher": """
                MATCH (file:File)
                WHERE file.content CONTAINS '\\\\'
                   OR file.path CONTAINS '\\\\'
                RETURN file.name, file.path
                LIMIT 10
                """
            },
            {
                "name": "Java 1.4 Vector/Hashtable Usage",
                "cypher": """
                MATCH (file:File)
                WHERE file.content CONTAINS 'Vector'
                   OR file.content CONTAINS 'Hashtable'
                RETURN file.name, 'Legacy Collections'
                LIMIT 10
                """
            },
            {
                "name": "java.io.File Usage (potential path issues)",
                "cypher": """
                MATCH (class:JavaClass)
                WHERE class.content CONTAINS 'java.io.File'
                RETURN class.name, 'File API usage'
                LIMIT 10
                """
            }
        ]

        for pattern in patterns:
            print(f"🔍 {pattern['name']}:")
            results = self.neo4j_query(pattern['cypher'])

            if results:
                for row in results:
                    print(f"  - {row[0]}")
            else:
                print("  ✅ None found")
            print()

    def compare_with_legacy(self, current_file_path: str) -> None:
        """Compare current file with legacy implementation"""
        print(f"\n🔄 Comparing with Legacy Code: {current_file_path}\n")

        file_path = Path(current_file_path)

        if not file_path.exists():
            print(f"❌ File not found: {current_file_path}")
            return

        # Extract class/service name
        file_name = file_path.stem

        # Search in Neo4j for similar classes
        cypher = f"""
        MATCH (class:JavaClass)
        WHERE class.name CONTAINS '{file_name}'
           OR class.name CONTAINS 'Budget'
        RETURN class.name, class.path, class.package
        LIMIT 5
        """

        print("📊 Searching for legacy equivalents in Neo4j...")
        results = self.neo4j_query(cypher)

        if results:
            print(f"\nFound {len(results)} potential legacy matches:\n")
            for row in results:
                print(f"  - {row[0]}")
                print(f"    Path: {row[1] if len(row) > 1 else 'N/A'}")
                print()
        else:
            print("  No legacy matches found")

        # Search in Qdrant for similar code patterns
        print("\n🔍 Searching for similar code patterns in Qdrant...")
        self.semantic_search(f"{file_name} implementation", limit=3)

    def analyze_java_version_differences(self) -> None:
        """Analyze Java 1.4 → Java 17 migration requirements"""
        print("\n☕ Java Version Migration Analysis (1.4 → 17)\n")

        print("🔍 Known Migration Patterns:")
        print("  1. Generics (Java 5+)")
        print("     Before: Vector v = new Vector();")
        print("     After:  List<String> list = new ArrayList<>();")
        print()
        print("  2. Enhanced For Loop (Java 5+)")
        print("     Before: for (int i=0; i<list.size(); i++)")
        print("     After:  for (String item : list)")
        print()
        print("  3. Try-with-resources (Java 7+)")
        print("     Before: try { ... } finally { stream.close(); }")
        print("     After:  try (Stream stream = ...) { ... }")
        print()
        print("  4. Lambda Expressions (Java 8+)")
        print("     Before: new Runnable() { public void run() {...} }")
        print("     After:  () -> {...}")
        print()
        print("  5. Streams API (Java 8+)")
        print("     Before: for loops with manual filtering")
        print("     After:  list.stream().filter(...).collect(...)")
        print()

        # Query Neo4j for legacy patterns
        cypher = """
        MATCH (class:JavaClass)
        WHERE class.javaVersion = '1.4'
           OR class.content CONTAINS 'Vector'
           OR class.content CONTAINS 'Hashtable'
        RETURN count(class) as legacyClassCount
        """

        results = self.neo4j_query(cypher)
        if results and results[0][0] > 0:
            print(f"📊 Found {results[0][0]} classes with Java 1.4 patterns")
        else:
            print("✅ No Java 1.4 patterns found (clean migration)")


def main():
    parser = argparse.ArgumentParser(
        description="Legacy Code Helper - Query Qdrant/Neo4j for KIS migration"
    )

    subparsers = parser.add_subparsers(dest='command', help='Commands')

    # Search command
    search_parser = subparsers.add_parser('search', help='Semantic search in Qdrant')
    search_parser.add_argument('query', type=str, help='Search query')
    search_parser.add_argument('--limit', type=int, default=5, help='Result limit')

    # Graph command
    graph_parser = subparsers.add_parser('graph', help='Query Neo4j graph for dependencies')
    graph_parser.add_argument('class_pattern', type=str, help='Class name pattern')

    # Compare command
    compare_parser = subparsers.add_parser('compare', help='Compare current file with legacy')
    compare_parser.add_argument('file_path', type=str, help='Path to current file')

    # Migration patterns command
    subparsers.add_parser('migration-patterns', help='Analyze migration patterns')

    # Java version command
    subparsers.add_parser('java-version', help='Analyze Java version differences')

    # Check connections command
    subparsers.add_parser('check', help='Check Qdrant and Neo4j connections')

    # Collections command
    subparsers.add_parser('collections', help='List Qdrant collections')

    args = parser.parse_args()

    helper = LegacyCodeHelper()

    if args.command == 'check':
        helper.check_connections()

    elif args.command == 'collections':
        helper.list_collections()

    elif args.command == 'search':
        if not helper.check_connections():
            sys.exit(1)
        helper.semantic_search(args.query, args.limit)

    elif args.command == 'graph':
        if not helper.check_connections():
            sys.exit(1)
        helper.find_class_dependencies(args.class_pattern)

    elif args.command == 'compare':
        if not helper.check_connections():
            sys.exit(1)
        helper.compare_with_legacy(args.file_path)

    elif args.command == 'migration-patterns':
        if not helper.check_connections():
            sys.exit(1)
        helper.find_migration_patterns()

    elif args.command == 'java-version':
        if not helper.check_connections():
            sys.exit(1)
        helper.analyze_java_version_differences()

    else:
        parser.print_help()


if __name__ == '__main__':
    main()
