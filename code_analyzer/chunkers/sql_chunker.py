"""SQL code chunker."""
import sqlparse
from typing import List
from chunkers.base_chunker import BaseChunker
from models import CodeChunk, ChunkType


class SQLChunker(BaseChunker):
    """Chunker for SQL files."""

    def chunk_file(self, file_path: str) -> List[CodeChunk]:
        """Chunk SQL file by statements."""
        chunks = []
        content = self._read_file(file_path)

        # Parse SQL statements
        statements = sqlparse.split(content)

        for idx, statement in enumerate(statements):
            if not statement.strip():
                continue

            chunk_type, name = self._identify_statement_type(statement)

            chunk = CodeChunk(
                id=self._generate_chunk_id(file_path, idx),
                file_path=file_path,
                chunk_type=chunk_type,
                name=name,
                content=statement,
                start_line=self._estimate_line_number(content, statement, idx),
                end_line=self._estimate_line_number(content, statement, idx) + statement.count('\n'),
                language="sql",
                metadata=self._extract_sql_metadata(statement)
            )
            chunks.append(chunk)

        return chunks if chunks else [self._create_fallback_chunk(file_path, content)]

    def _identify_statement_type(self, statement: str) -> tuple:
        """Identify SQL statement type and name."""
        parsed = sqlparse.parse(statement)[0] if sqlparse.parse(statement) else None
        if not parsed:
            return ChunkType.SQL_TABLE, "unknown"

        statement_upper = statement.upper().strip()

        if statement_upper.startswith('CREATE TABLE'):
            name = self._extract_object_name(statement, 'TABLE')
            return ChunkType.SQL_TABLE, name
        elif statement_upper.startswith('CREATE VIEW') or statement_upper.startswith('CREATE OR REPLACE VIEW'):
            name = self._extract_object_name(statement, 'VIEW')
            return ChunkType.SQL_VIEW, name
        elif statement_upper.startswith('CREATE PROCEDURE') or statement_upper.startswith('CREATE OR REPLACE PROCEDURE'):
            name = self._extract_object_name(statement, 'PROCEDURE')
            return ChunkType.SQL_PROCEDURE, name
        elif statement_upper.startswith('CREATE FUNCTION') or statement_upper.startswith('CREATE OR REPLACE FUNCTION'):
            name = self._extract_object_name(statement, 'FUNCTION')
            return ChunkType.SQL_FUNCTION, name
        else:
            return ChunkType.SQL_TABLE, f"statement_{hash(statement) % 10000}"

    def _extract_object_name(self, statement: str, object_type: str) -> str:
        """Extract object name from CREATE statement."""
        try:
            parts = statement.upper().split(object_type.upper())
            if len(parts) > 1:
                name_part = parts[1].strip().split()[0]
                # Remove quotes and schema prefix
                name = name_part.replace('"', '').replace("'", '')
                if '.' in name:
                    name = name.split('.')[-1]
                return name
        except:
            pass
        return f"{object_type.lower()}_{hash(statement) % 10000}"

    def _extract_sql_metadata(self, statement: str) -> dict:
        """Extract metadata from SQL statement."""
        metadata = {}
        statement_upper = statement.upper()

        # Extract referenced tables
        if 'FROM' in statement_upper:
            metadata['references_tables'] = True
        if 'JOIN' in statement_upper:
            metadata['has_joins'] = True
        if 'WHERE' in statement_upper:
            metadata['has_where_clause'] = True

        # Complexity indicators
        metadata['line_count'] = statement.count('\n') + 1
        metadata['has_subquery'] = '(SELECT' in statement_upper

        return metadata

    def _estimate_line_number(self, full_content: str, statement: str, idx: int) -> int:
        """Estimate the line number of a statement."""
        pos = full_content.find(statement)
        if pos == -1:
            return idx * 10  # Fallback
        return full_content[:pos].count('\n')

    def _create_fallback_chunk(self, file_path: str, content: str) -> CodeChunk:
        """Create fallback chunk for entire SQL file."""
        return CodeChunk(
            id=self._generate_chunk_id(file_path, 0),
            file_path=file_path,
            chunk_type=ChunkType.SQL_TABLE,
            name=file_path.split('/')[-1].replace('.sql', ''),
            content=content,
            start_line=0,
            end_line=content.count('\n'),
            language="sql"
        )
