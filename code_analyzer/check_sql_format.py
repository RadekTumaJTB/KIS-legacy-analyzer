"""Zjistit, jak vypadají SQL dotazy v kódu."""
from qdrant_client import QdrantClient
from config import QDRANT_HOST, QDRANT_PORT, QDRANT_COLLECTION_NAME

qdrant = QdrantClient(host=QDRANT_HOST, port=QDRANT_PORT)

# Najdi JSP stránku s nějakým obsahem
results = qdrant.scroll(
    collection_name=QDRANT_COLLECTION_NAME,
    scroll_filter={
        "must": [
            {"key": "chunk_type", "match": {"value": "jsp_page"}}
        ]
    },
    limit=10,
    with_payload=True,
    with_vectors=False
)

for point in results[0]:
    content = point.payload.get('content', '')
    name = point.payload.get('name', '')
    
    # Hledej SQL klíčová slova
    if any(keyword in content.upper() for keyword in ['SELECT', 'INSERT', 'UPDATE', 'DELETE', 'FROM']):
        print(f"\n=== {name} ===")
        print(f"Délka obsahu: {len(content)} znaků")
        
        # Zobraz první výskyt SELECT
        idx = content.upper().find('SELECT')
        if idx >= 0:
            print("\nPříklad SQL dotazu:")
            print(content[idx:idx+200])
        
        break
