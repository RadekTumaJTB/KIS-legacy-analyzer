"""Data models for code chunks and dependencies."""
from dataclasses import dataclass, field
from typing import List, Dict, Optional
from enum import Enum


class ChunkType(Enum):
    """Type of code chunk."""
    JAVA_CLASS = "java_class"
    JAVA_METHOD = "java_method"
    JAVA_INTERFACE = "java_interface"
    JSP_PAGE = "jsp_page"
    JSP_SCRIPTLET = "jsp_scriptlet"
    SQL_TABLE = "sql_table"
    SQL_VIEW = "sql_view"
    SQL_PROCEDURE = "sql_procedure"
    SQL_FUNCTION = "sql_function"


class DependencyType(Enum):
    """Type of dependency between chunks."""
    IMPORTS = "imports"
    EXTENDS = "extends"
    IMPLEMENTS = "implements"
    CALLS = "calls"
    REFERENCES = "references"
    INCLUDES = "includes"
    QUERIES = "queries"
    CONTAINS = "contains"  # For JSP page containing scriptlets


@dataclass
class CodeChunk:
    """Represents a logical chunk of code."""
    id: str
    file_path: str
    chunk_type: ChunkType
    name: str
    content: str
    start_line: int
    end_line: int
    language: str
    package: Optional[str] = None
    module: Optional[str] = None
    imports: List[str] = field(default_factory=list)
    dependencies: List[str] = field(default_factory=list)
    metadata: Dict = field(default_factory=dict)

    def to_dict(self) -> Dict:
        """Convert to dictionary for storage."""
        # Convert metadata values to JSON-serializable types
        serializable_metadata = {}
        for key, value in (self.metadata or {}).items():
            if isinstance(value, set):
                serializable_metadata[key] = list(value)
            elif isinstance(value, list):
                # Handle lists that might contain sets
                serializable_metadata[key] = [
                    list(item) if isinstance(item, set) else item
                    for item in value
                ]
            else:
                serializable_metadata[key] = value

        return {
            "id": self.id,
            "file_path": self.file_path,
            "chunk_type": self.chunk_type.value,
            "name": self.name,
            "content": self.content,
            "start_line": self.start_line,
            "end_line": self.end_line,
            "language": self.language,
            "package": self.package,
            "module": self.module,
            "imports": list(self.imports) if isinstance(self.imports, set) else self.imports,
            "dependencies": list(self.dependencies) if isinstance(self.dependencies, set) else self.dependencies,
            "metadata": serializable_metadata
        }


@dataclass
class Dependency:
    """Represents a dependency between code chunks."""
    source_id: str
    target_id: str
    dependency_type: DependencyType
    metadata: Dict = field(default_factory=dict)

    def to_dict(self) -> Dict:
        """Convert to dictionary for storage."""
        return {
            "source_id": self.source_id,
            "target_id": self.target_id,
            "dependency_type": self.dependency_type.value,
            "metadata": self.metadata
        }


@dataclass
class MigrationInsight:
    """Insights for migration planning."""
    chunk_id: str
    complexity_score: float  # 0-1, where 1 is most complex
    modernization_priority: str  # high, medium, low
    suggested_technologies: List[str]
    risks: List[str]
    estimated_effort: str  # small, medium, large
    dependencies_count: int
    notes: str = ""
