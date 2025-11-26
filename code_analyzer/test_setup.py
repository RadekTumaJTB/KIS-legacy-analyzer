#!/usr/bin/env python3
"""Test script to verify setup before running full analysis."""
import sys
import os


def test_imports():
    """Test that all required packages are installed."""
    print("Testing Python package imports...")
    required_packages = [
        ('qdrant_client', 'QdrantClient'),
        ('neo4j', 'GraphDatabase'),
        ('javalang', 'javalang'),
        ('sqlparse', 'sqlparse'),
        ('sentence_transformers', 'SentenceTransformer'),
        ('tqdm', 'tqdm'),
    ]

    failed = []
    for package, import_name in required_packages:
        try:
            if package == 'qdrant_client':
                from qdrant_client import QdrantClient
            elif package == 'neo4j':
                from neo4j import GraphDatabase
            elif package == 'javalang':
                import javalang
            elif package == 'sqlparse':
                import sqlparse
            elif package == 'sentence_transformers':
                from sentence_transformers import SentenceTransformer
            elif package == 'tqdm':
                from tqdm import tqdm
            print(f"  ✅ {package}")
        except ImportError as e:
            print(f"  ❌ {package}: {e}")
            failed.append(package)

    return len(failed) == 0


def test_qdrant_connection():
    """Test Qdrant connection."""
    print("\nTesting Qdrant connection...")
    try:
        from qdrant_client import QdrantClient
        from config import QDRANT_HOST, QDRANT_PORT

        client = QdrantClient(host=QDRANT_HOST, port=QDRANT_PORT)
        collections = client.get_collections()
        print(f"  ✅ Connected to Qdrant at {QDRANT_HOST}:{QDRANT_PORT}")
        print(f"     Collections: {len(collections.collections)}")
        return True
    except Exception as e:
        print(f"  ❌ Failed to connect to Qdrant: {e}")
        print(f"     Make sure Docker is running: docker-compose up -d")
        return False


def test_neo4j_connection():
    """Test Neo4j connection."""
    print("\nTesting Neo4j connection...")
    try:
        from neo4j import GraphDatabase
        from config import NEO4J_URI, NEO4J_USER, NEO4J_PASSWORD

        driver = GraphDatabase.driver(
            NEO4J_URI,
            auth=(NEO4J_USER, NEO4J_PASSWORD)
        )

        with driver.session() as session:
            result = session.run("RETURN 1 as test")
            result.single()

        driver.close()
        print(f"  ✅ Connected to Neo4j at {NEO4J_URI}")
        return True
    except Exception as e:
        print(f"  ❌ Failed to connect to Neo4j: {e}")
        print(f"     Make sure Docker is running: docker-compose up -d")
        print(f"     Check credentials in .env file")
        return False


def test_sources_directory():
    """Test that sources directory exists and has files."""
    print("\nTesting sources directory...")
    try:
        from config import SOURCES_DIR
        from pathlib import Path

        if not os.path.exists(SOURCES_DIR):
            print(f"  ❌ Sources directory not found: {SOURCES_DIR}")
            return False

        java_files = list(Path(SOURCES_DIR).rglob("*.java"))
        jsp_files = list(Path(SOURCES_DIR).rglob("*.jsp"))
        sql_files = list(Path(SOURCES_DIR).rglob("*.sql"))

        print(f"  ✅ Sources directory found: {SOURCES_DIR}")
        print(f"     Java files: {len(java_files)}")
        print(f"     JSP files: {len(jsp_files)}")
        print(f"     SQL files: {len(sql_files)}")

        if len(java_files) == 0 and len(jsp_files) == 0 and len(sql_files) == 0:
            print("  ⚠️  Warning: No source files found!")
            return False

        return True
    except Exception as e:
        print(f"  ❌ Error checking sources: {e}")
        return False


def test_embedding_model():
    """Test that embedding model can be loaded."""
    print("\nTesting embedding model...")
    try:
        from sentence_transformers import SentenceTransformer
        from config import EMBEDDING_MODEL

        print(f"  Loading model: {EMBEDDING_MODEL}")
        print(f"  (This may take a few minutes on first run...)")

        model = SentenceTransformer(EMBEDDING_MODEL)
        test_text = "This is a test"
        embedding = model.encode(test_text)

        print(f"  ✅ Model loaded successfully")
        print(f"     Embedding dimension: {len(embedding)}")
        return True
    except Exception as e:
        print(f"  ❌ Failed to load embedding model: {e}")
        return False


def main():
    """Run all tests."""
    print("=" * 70)
    print("CODE ANALYZER - SETUP TEST")
    print("=" * 70)

    tests = [
        ("Package Imports", test_imports),
        ("Qdrant Connection", test_qdrant_connection),
        ("Neo4j Connection", test_neo4j_connection),
        ("Sources Directory", test_sources_directory),
        ("Embedding Model", test_embedding_model),
    ]

    results = {}
    for name, test_func in tests:
        try:
            results[name] = test_func()
        except Exception as e:
            print(f"\n❌ Unexpected error in {name}: {e}")
            results[name] = False

    print("\n" + "=" * 70)
    print("TEST SUMMARY")
    print("=" * 70)

    all_passed = True
    for name, passed in results.items():
        status = "✅ PASS" if passed else "❌ FAIL"
        print(f"{status}: {name}")
        if not passed:
            all_passed = False

    if all_passed:
        print("\n🎉 All tests passed! You're ready to run the analysis.")
        print("\nRun: python run_analysis.py")
        return 0
    else:
        print("\n⚠️  Some tests failed. Please fix the issues above before running.")
        print("\nCommon fixes:")
        print("  - Install packages: pip install -r requirements.txt")
        print("  - Start databases: docker-compose up -d")
        print("  - Check .env configuration")
        return 1


if __name__ == "__main__":
    sys.exit(main())
