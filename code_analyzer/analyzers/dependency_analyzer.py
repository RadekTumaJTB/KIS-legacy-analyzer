"""Analyze dependencies between code chunks."""
import re
from typing import List, Dict, Set
from ..models import CodeChunk, Dependency, DependencyType


class DependencyAnalyzer:
    """Analyze and extract dependencies between code chunks."""

    def __init__(self):
        """Initialize dependency analyzer."""
        self.chunk_registry: Dict[str, CodeChunk] = {}

    def register_chunks(self, chunks: List[CodeChunk]):
        """Register chunks for dependency analysis."""
        for chunk in chunks:
            self.chunk_registry[chunk.id] = chunk
            # Also index by name for lookup
            if chunk.name:
                self.chunk_registry[chunk.name] = chunk

    def analyze_dependencies(self, chunks: List[CodeChunk]) -> List[Dependency]:
        """Analyze all dependencies between chunks."""
        self.register_chunks(chunks)
        dependencies = []

        for chunk in chunks:
            chunk_deps = self._analyze_chunk_dependencies(chunk)
            dependencies.extend(chunk_deps)

        return dependencies

    def _analyze_chunk_dependencies(self, chunk: CodeChunk) -> List[Dependency]:
        """Analyze dependencies for a single chunk."""
        dependencies = []

        if chunk.language == "java":
            dependencies.extend(self._analyze_java_dependencies(chunk))
        elif chunk.language == "jsp":
            dependencies.extend(self._analyze_jsp_dependencies(chunk))
        elif chunk.language == "sql":
            dependencies.extend(self._analyze_sql_dependencies(chunk))

        return dependencies

    def _analyze_java_dependencies(self, chunk: CodeChunk) -> List[Dependency]:
        """Analyze Java-specific dependencies."""
        dependencies = []

        # Analyze imports
        for imp in chunk.imports:
            target_id = self._find_chunk_by_import(imp)
            if target_id:
                dependencies.append(Dependency(
                    source_id=chunk.id,
                    target_id=target_id,
                    dependency_type=DependencyType.IMPORTS,
                    metadata={"import": imp}
                ))

        # Analyze extends/implements from metadata
        if chunk.metadata:
            if "extends" in chunk.metadata and chunk.metadata["extends"]:
                target_id = self._find_chunk_by_name(chunk.metadata["extends"])
                if target_id:
                    dependencies.append(Dependency(
                        source_id=chunk.id,
                        target_id=target_id,
                        dependency_type=DependencyType.EXTENDS
                    ))

            if "implements" in chunk.metadata:
                for interface in chunk.metadata["implements"]:
                    target_id = self._find_chunk_by_name(interface)
                    if target_id:
                        dependencies.append(Dependency(
                            source_id=chunk.id,
                            target_id=target_id,
                            dependency_type=DependencyType.IMPLEMENTS
                        ))

        # Analyze method calls in content
        method_calls = self._extract_method_calls(chunk.content)
        for method_call in method_calls:
            target_id = self._find_chunk_by_name(method_call)
            if target_id and target_id != chunk.id:
                dependencies.append(Dependency(
                    source_id=chunk.id,
                    target_id=target_id,
                    dependency_type=DependencyType.CALLS,
                    metadata={"method": method_call}
                ))

        return dependencies

    def _analyze_jsp_dependencies(self, chunk: CodeChunk) -> List[Dependency]:
        """Analyze JSP-specific dependencies."""
        dependencies = []

        # Analyze includes
        if chunk.metadata and "includes" in chunk.metadata:
            for include in chunk.metadata["includes"]:
                target_id = self._find_chunk_by_file_path(include)
                if target_id:
                    dependencies.append(Dependency(
                        source_id=chunk.id,
                        target_id=target_id,
                        dependency_type=DependencyType.INCLUDES,
                        metadata={"file": include}
                    ))

        # Analyze Java references in scriptlets
        java_refs = self._extract_java_references(chunk.content)
        for ref in java_refs:
            target_id = self._find_chunk_by_name(ref)
            if target_id:
                dependencies.append(Dependency(
                    source_id=chunk.id,
                    target_id=target_id,
                    dependency_type=DependencyType.REFERENCES,
                    metadata={"reference": ref}
                ))

        return dependencies

    def _analyze_sql_dependencies(self, chunk: CodeChunk) -> List[Dependency]:
        """Analyze SQL-specific dependencies."""
        dependencies = []

        # Extract table references
        table_refs = self._extract_table_references(chunk.content)
        for table_ref in table_refs:
            target_id = self._find_chunk_by_name(table_ref)
            if target_id and target_id != chunk.id:
                dependencies.append(Dependency(
                    source_id=chunk.id,
                    target_id=target_id,
                    dependency_type=DependencyType.QUERIES,
                    metadata={"table": table_ref}
                ))

        return dependencies

    def _extract_method_calls(self, content: str) -> Set[str]:
        """Extract method calls from code."""
        # Simple pattern for method calls: identifier.method()
        pattern = r'(\w+)\.(\w+)\s*\('
        matches = re.findall(pattern, content)
        # Return full method names
        return {f"{cls}.{method}" for cls, method in matches}

    def _extract_java_references(self, content: str) -> Set[str]:
        """Extract Java class references from JSP."""
        # Look for class instantiation and type references
        pattern = r'new\s+([A-Z]\w+)|([A-Z]\w+)\s+\w+\s*='
        matches = re.findall(pattern, content)
        return {match[0] or match[1] for match in matches if match[0] or match[1]}

    def _extract_table_references(self, content: str) -> Set[str]:
        """Extract table references from SQL."""
        content_upper = content.upper()
        tables = set()

        # FROM clause
        from_pattern = r'FROM\s+([A-Z_][A-Z0-9_]*)'
        tables.update(re.findall(from_pattern, content_upper))

        # JOIN clause
        join_pattern = r'JOIN\s+([A-Z_][A-Z0-9_]*)'
        tables.update(re.findall(join_pattern, content_upper))

        # INTO clause
        into_pattern = r'INTO\s+([A-Z_][A-Z0-9_]*)'
        tables.update(re.findall(into_pattern, content_upper))

        # UPDATE clause
        update_pattern = r'UPDATE\s+([A-Z_][A-Z0-9_]*)'
        tables.update(re.findall(update_pattern, content_upper))

        return tables

    def _find_chunk_by_import(self, import_path: str) -> str:
        """Find chunk ID by import path."""
        # Try to match by class name from import
        class_name = import_path.split('.')[-1]
        return self._find_chunk_by_name(class_name)

    def _find_chunk_by_name(self, name: str) -> str:
        """Find chunk ID by name."""
        if name in self.chunk_registry:
            chunk = self.chunk_registry[name]
            return chunk.id if isinstance(chunk, CodeChunk) else None
        return None

    def _find_chunk_by_file_path(self, file_path: str) -> str:
        """Find chunk ID by file path pattern."""
        for chunk_id, chunk in self.chunk_registry.items():
            if isinstance(chunk, CodeChunk) and file_path in chunk.file_path:
                return chunk.id
        return None
