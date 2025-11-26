"""Base chunker interface."""
from abc import ABC, abstractmethod
from typing import List
from ..models import CodeChunk


class BaseChunker(ABC):
    """Base class for code chunkers."""

    @abstractmethod
    def chunk_file(self, file_path: str) -> List[CodeChunk]:
        """
        Split a file into logical chunks.

        Args:
            file_path: Path to the file to chunk

        Returns:
            List of CodeChunk objects
        """
        pass

    def _generate_chunk_id(self, file_path: str, chunk_index: int) -> str:
        """Generate unique chunk ID."""
        import hashlib
        base = f"{file_path}:{chunk_index}"
        return hashlib.sha256(base.encode()).hexdigest()[:16]

    def _read_file(self, file_path: str) -> str:
        """Read file content."""
        try:
            with open(file_path, 'r', encoding='utf-8') as f:
                return f.read()
        except UnicodeDecodeError:
            # Try with latin-1 encoding for older files
            with open(file_path, 'r', encoding='latin-1') as f:
                return f.read()
