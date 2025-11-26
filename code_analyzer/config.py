"""Configuration for code analyzer."""
import os
from dotenv import load_dotenv

load_dotenv()

# Qdrant Configuration
QDRANT_HOST = os.getenv("QDRANT_HOST", "localhost")
QDRANT_PORT = int(os.getenv("QDRANT_PORT", "6333"))
QDRANT_API_KEY = os.getenv("QDRANT_API_KEY")
QDRANT_COLLECTION_NAME = "code_chunks"

# Neo4j Configuration
NEO4J_URI = os.getenv("NEO4J_URI", "bolt://localhost:7687")
NEO4J_USER = os.getenv("NEO4J_USER", "neo4j")
NEO4J_PASSWORD = os.getenv("NEO4J_PASSWORD")

# Embedding Model Configuration
EMBEDDING_MODEL = os.getenv("EMBEDDING_MODEL", "sentence-transformers/all-MiniLM-L6-v2")
EMBEDDING_DIMENSION = 384  # Dimension for all-MiniLM-L6-v2

# Source Code Paths
SOURCES_DIR = os.path.join(os.path.dirname(os.path.dirname(__file__)), "sources")
JAVA_DIR = os.path.join(SOURCES_DIR, "JAVA")
JSP_DIR = os.path.join(SOURCES_DIR, "JSP")
DB_DIR = os.path.join(SOURCES_DIR, "DB")

# Chunking Strategy
CHUNK_STRATEGIES = {
    "java": {
        "type": "class_method",  # Split by class and method
        "max_lines": 200,  # Maximum lines per chunk
        "overlap": 10  # Lines of overlap between chunks
    },
    "jsp": {
        "type": "scriptlet",  # Split by JSP scriptlets and tags
        "max_lines": 100,
        "overlap": 5
    },
    "sql": {
        "type": "statement",  # Split by SQL statements
        "max_lines": 50,
        "overlap": 0
    }
}

# Analysis Output
OUTPUT_DIR = os.path.join(os.path.dirname(os.path.dirname(__file__)), "analysis_output")
REPORT_DIR = os.path.join(OUTPUT_DIR, "reports")
