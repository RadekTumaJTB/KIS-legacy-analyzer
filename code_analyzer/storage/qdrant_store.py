"""Qdrant vector store for code chunks."""
from typing import List, Dict, Optional
from qdrant_client import QdrantClient
from qdrant_client.models import Distance, VectorParams, PointStruct
from sentence_transformers import SentenceTransformer
import numpy as np
from tqdm import tqdm

from models import CodeChunk
from config import (
    QDRANT_HOST, QDRANT_PORT, QDRANT_API_KEY,
    QDRANT_COLLECTION_NAME, EMBEDDING_MODEL, EMBEDDING_DIMENSION
)


class QdrantStore:
    """Store for code chunks in Qdrant vector database."""

    def __init__(self):
        """Initialize Qdrant client and embedding model."""
        self.client = QdrantClient(
            host=QDRANT_HOST,
            port=QDRANT_PORT,
            api_key=QDRANT_API_KEY,
            https=False  # Use HTTP instead of HTTPS
        )
        self.collection_name = QDRANT_COLLECTION_NAME
        self.embedding_model = SentenceTransformer(EMBEDDING_MODEL)

    def initialize_collection(self, recreate: bool = False):
        """Initialize or recreate the Qdrant collection."""
        collections = self.client.get_collections().collections
        exists = any(c.name == self.collection_name for c in collections)

        if exists and recreate:
            print(f"Deleting existing collection: {self.collection_name}")
            self.client.delete_collection(self.collection_name)
            exists = False

        if not exists:
            print(f"Creating collection: {self.collection_name}")
            self.client.create_collection(
                collection_name=self.collection_name,
                vectors_config=VectorParams(
                    size=EMBEDDING_DIMENSION,
                    distance=Distance.COSINE
                )
            )
            print(f"Collection created successfully")

    def upload_chunks(self, chunks: List[CodeChunk], batch_size: int = 100):
        """
        Upload code chunks to Qdrant with embeddings.

        Args:
            chunks: List of CodeChunk objects
            batch_size: Number of chunks to upload per batch
        """
        print(f"Uploading {len(chunks)} chunks to Qdrant...")

        for i in tqdm(range(0, len(chunks), batch_size)):
            batch = chunks[i:i + batch_size]
            points = []

            for chunk in batch:
                # Create embedding from chunk content and metadata
                text_to_embed = self._prepare_text_for_embedding(chunk)
                embedding = self.embedding_model.encode(text_to_embed).tolist()

                point = PointStruct(
                    id=hash(chunk.id) % (2**63),  # Convert string ID to int
                    vector=embedding,
                    payload={
                        "chunk_id": chunk.id,
                        "file_path": chunk.file_path,
                        "chunk_type": chunk.chunk_type.value,
                        "name": chunk.name,
                        "content": chunk.content[:5000],  # Limit content size
                        "start_line": chunk.start_line,
                        "end_line": chunk.end_line,
                        "language": chunk.language,
                        "package": chunk.package,
                        "module": chunk.module,
                        "imports": chunk.imports[:50] if chunk.imports else [],  # Limit imports
                        "metadata": chunk.metadata
                    }
                )
                points.append(point)

            self.client.upsert(
                collection_name=self.collection_name,
                points=points
            )

        print(f"Successfully uploaded {len(chunks)} chunks")

    def _prepare_text_for_embedding(self, chunk: CodeChunk) -> str:
        """Prepare chunk text for embedding."""
        parts = [
            f"Name: {chunk.name}",
            f"Type: {chunk.chunk_type.value}",
            f"Language: {chunk.language}"
        ]

        if chunk.package:
            parts.append(f"Package: {chunk.package}")

        if chunk.imports:
            parts.append(f"Imports: {', '.join(chunk.imports[:10])}")

        # Add content (truncated)
        content_preview = chunk.content[:1000]
        parts.append(f"Content:\n{content_preview}")

        return "\n".join(parts)

    def search_similar(
        self,
        query: str,
        limit: int = 10,
        filter_dict: Optional[Dict] = None
    ) -> List[Dict]:
        """
        Search for similar code chunks.

        Args:
            query: Search query
            limit: Number of results to return
            filter_dict: Optional filters for search

        Returns:
            List of matching chunks with scores
        """
        query_vector = self.embedding_model.encode(query).tolist()

        results = self.client.search(
            collection_name=self.collection_name,
            query_vector=query_vector,
            limit=limit,
            query_filter=filter_dict
        )

        return [
            {
                "chunk_id": hit.payload["chunk_id"],
                "name": hit.payload["name"],
                "file_path": hit.payload["file_path"],
                "chunk_type": hit.payload["chunk_type"],
                "score": hit.score,
                "content": hit.payload["content"]
            }
            for hit in results
        ]

    def get_chunk_by_id(self, chunk_id: str) -> Optional[Dict]:
        """Retrieve a specific chunk by ID."""
        results = self.client.scroll(
            collection_name=self.collection_name,
            scroll_filter={
                "must": [
                    {
                        "key": "chunk_id",
                        "match": {"value": chunk_id}
                    }
                ]
            },
            limit=1
        )

        if results[0]:
            return results[0][0].payload
        return None

    def get_collection_stats(self) -> Dict:
        """Get statistics about the collection."""
        info = self.client.get_collection(self.collection_name)
        # Handle different Qdrant API versions - use points_count which is universal
        return {
            "vectors_count": getattr(info, 'points_count', 0),
            "indexed_vectors_count": getattr(info, 'points_count', 0),
            "points_count": getattr(info, 'points_count', 0)
        }
