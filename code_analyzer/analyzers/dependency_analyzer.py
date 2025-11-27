"""Analyze dependencies between code chunks."""
import re
from typing import List, Dict, Set
from models import CodeChunk, Dependency, DependencyType


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
                extends_value = chunk.metadata["extends"]
                # Handle both string and list of extends
                if isinstance(extends_value, list):
                    for ext in extends_value:
                        target_id = self._find_chunk_by_name(ext)
                        if target_id:
                            dependencies.append(Dependency(
                                source_id=chunk.id,
                                target_id=target_id,
                                dependency_type=DependencyType.EXTENDS
                            ))
                else:
                    target_id = self._find_chunk_by_name(extends_value)
                    if target_id:
                        dependencies.append(Dependency(
                            source_id=chunk.id,
                            target_id=target_id,
                            dependency_type=DependencyType.EXTENDS
                        ))

            if "implements" in chunk.metadata and chunk.metadata["implements"]:
                implements_value = chunk.metadata["implements"]
                # Ensure implements is iterable
                if isinstance(implements_value, (list, tuple)):
                    for interface in implements_value:
                        if interface:  # Skip None or empty strings
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

        # For JSP_PAGE: connect to its scriptlets
        if chunk.chunk_type.value == "jsp_page" and chunk.metadata:
            if "scriptlet_ids" in chunk.metadata:
                for scriptlet_id in chunk.metadata["scriptlet_ids"]:
                    if scriptlet_id in self.chunk_registry:
                        dependencies.append(Dependency(
                            source_id=chunk.id,
                            target_id=scriptlet_id,
                            dependency_type=DependencyType.CONTAINS,
                            metadata={"relationship": "page_contains_scriptlet"}
                        ))

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

        # Analyze imports (similar to Java)
        for imp in chunk.imports:
            target_id = self._find_chunk_by_import(imp)
            if target_id:
                dependencies.append(Dependency(
                    source_id=chunk.id,
                    target_id=target_id,
                    dependency_type=DependencyType.IMPORTS,
                    metadata={"import": imp}
                ))

        # Analyze useBeans
        if chunk.metadata and "use_beans" in chunk.metadata:
            for bean_class in chunk.metadata["use_beans"]:
                class_name = bean_class.split('.')[-1]
                target_id = self._find_chunk_by_name(class_name)
                if target_id:
                    dependencies.append(Dependency(
                        source_id=chunk.id,
                        target_id=target_id,
                        dependency_type=DependencyType.REFERENCES,
                        metadata={"useBean": bean_class}
                    ))

        # Analyze Java references (class instantiations, type references)
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

        # Analyze method calls in JSP/scriptlets (similar to Java)
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

        # Extract more Java patterns from scriptlets
        if chunk.chunk_type.value == "jsp_scriptlet":
            # Static method calls: ClassName.methodName()
            static_calls = self._extract_static_calls(chunk.content)
            for class_name in static_calls:
                target_id = self._find_chunk_by_name(class_name)
                if target_id:
                    dependencies.append(Dependency(
                        source_id=chunk.id,
                        target_id=target_id,
                        dependency_type=DependencyType.CALLS,
                        metadata={"static_call": class_name}
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
        refs = set()

        # Pattern 1: new ClassName()
        new_pattern = r'new\s+([A-Z][a-zA-Z0-9_]*)'
        refs.update(re.findall(new_pattern, content))

        # Pattern 2: ClassName variableName =
        type_pattern = r'([A-Z][a-zA-Z0-9_]*)\s+\w+\s*='
        refs.update(re.findall(type_pattern, content))

        # Pattern 3: (ClassName) cast
        cast_pattern = r'\(\s*([A-Z][a-zA-Z0-9_]*)\s*\)'
        refs.update(re.findall(cast_pattern, content))

        # Pattern 4: ClassName.staticField or ClassName.staticMethod
        static_ref_pattern = r'([A-Z][a-zA-Z0-9_]*)\.[a-zA-Z]'
        refs.update(re.findall(static_ref_pattern, content))

        # Pattern 5: instanceof ClassName
        instanceof_pattern = r'instanceof\s+([A-Z][a-zA-Z0-9_]*)'
        refs.update(re.findall(instanceof_pattern, content))

        return refs

    def _extract_static_calls(self, content: str) -> Set[str]:
        """Extract static method calls (ClassName.methodName)."""
        # Pattern: ClassName.methodName(
        pattern = r'([A-Z][a-zA-Z0-9_]*)\.\w+\s*\('
        return set(re.findall(pattern, content))

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
