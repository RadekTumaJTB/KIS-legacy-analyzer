"""Java code chunker."""
import javalang
from typing import List, Optional
from .base_chunker import BaseChunker
from ..models import CodeChunk, ChunkType


class JavaChunker(BaseChunker):
    """Chunker for Java files."""

    def chunk_file(self, file_path: str) -> List[CodeChunk]:
        """Chunk Java file by classes and methods."""
        chunks = []
        content = self._read_file(file_path)
        lines = content.split('\n')

        try:
            tree = javalang.parse.parse(content)
            package_name = tree.package.name if tree.package else None

            # Extract imports
            imports = [imp.path for imp in tree.imports] if tree.imports else []

            # Process classes
            for path, node in tree.filter(javalang.tree.ClassDeclaration):
                chunks.extend(self._process_class(
                    node, file_path, lines, package_name, imports
                ))

            # Process interfaces
            for path, node in tree.filter(javalang.tree.InterfaceDeclaration):
                chunks.extend(self._process_interface(
                    node, file_path, lines, package_name, imports
                ))

        except Exception as e:
            # If parsing fails, create a single chunk for the entire file
            print(f"Warning: Failed to parse {file_path}: {e}")
            chunks.append(self._create_fallback_chunk(file_path, content, lines))

        return chunks

    def _process_class(
        self,
        class_node: javalang.tree.ClassDeclaration,
        file_path: str,
        lines: List[str],
        package_name: Optional[str],
        imports: List[str]
    ) -> List[CodeChunk]:
        """Process a class and its methods."""
        chunks = []

        # Create chunk for the class declaration
        class_chunk = CodeChunk(
            id=self._generate_chunk_id(file_path, len(chunks)),
            file_path=file_path,
            chunk_type=ChunkType.JAVA_CLASS,
            name=class_node.name,
            content=self._extract_class_header(class_node, lines),
            start_line=self._get_start_line(class_node),
            end_line=self._get_start_line(class_node) + 10,  # Approximate
            language="java",
            package=package_name,
            imports=imports,
            metadata={
                "extends": class_node.extends.name if class_node.extends else None,
                "implements": [impl.name for impl in class_node.implements] if class_node.implements else [],
                "modifiers": class_node.modifiers if class_node.modifiers else []
            }
        )
        chunks.append(class_chunk)

        # Process methods
        for method in class_node.methods:
            method_chunk = self._process_method(
                method, file_path, lines, package_name, class_node.name, imports
            )
            chunks.append(method_chunk)

        return chunks

    def _process_interface(
        self,
        interface_node: javalang.tree.InterfaceDeclaration,
        file_path: str,
        lines: List[str],
        package_name: Optional[str],
        imports: List[str]
    ) -> List[CodeChunk]:
        """Process an interface."""
        chunks = []

        interface_chunk = CodeChunk(
            id=self._generate_chunk_id(file_path, len(chunks)),
            file_path=file_path,
            chunk_type=ChunkType.JAVA_INTERFACE,
            name=interface_node.name,
            content=self._extract_node_content(interface_node, lines),
            start_line=self._get_start_line(interface_node),
            end_line=self._get_end_line(interface_node, lines),
            language="java",
            package=package_name,
            imports=imports,
            metadata={
                "extends": [ext.name for ext in interface_node.extends] if interface_node.extends else [],
                "modifiers": interface_node.modifiers if interface_node.modifiers else []
            }
        )
        chunks.append(interface_chunk)

        return chunks

    def _process_method(
        self,
        method_node: javalang.tree.MethodDeclaration,
        file_path: str,
        lines: List[str],
        package_name: Optional[str],
        class_name: str,
        imports: List[str]
    ) -> CodeChunk:
        """Process a method."""
        return CodeChunk(
            id=self._generate_chunk_id(file_path, hash(method_node.name)),
            file_path=file_path,
            chunk_type=ChunkType.JAVA_METHOD,
            name=f"{class_name}.{method_node.name}",
            content=self._extract_node_content(method_node, lines),
            start_line=self._get_start_line(method_node),
            end_line=self._get_end_line(method_node, lines),
            language="java",
            package=package_name,
            module=class_name,
            imports=imports,
            metadata={
                "return_type": method_node.return_type.name if method_node.return_type else "void",
                "parameters": [p.name for p in method_node.parameters] if method_node.parameters else [],
                "modifiers": method_node.modifiers if method_node.modifiers else []
            }
        )

    def _extract_class_header(self, class_node, lines: List[str]) -> str:
        """Extract class header and fields."""
        start_line = self._get_start_line(class_node)
        # Extract approximately first 20 lines or until first method
        end_line = min(start_line + 20, len(lines))
        return '\n'.join(lines[start_line:end_line])

    def _extract_node_content(self, node, lines: List[str]) -> str:
        """Extract full node content."""
        start_line = self._get_start_line(node)
        end_line = self._get_end_line(node, lines)
        return '\n'.join(lines[start_line:end_line])

    def _get_start_line(self, node) -> int:
        """Get start line of node."""
        if hasattr(node, 'position') and node.position:
            return node.position.line - 1  # Convert to 0-indexed
        return 0

    def _get_end_line(self, node, lines: List[str]) -> int:
        """Estimate end line of node."""
        start = self._get_start_line(node)
        # Simple heuristic: find matching closing brace
        brace_count = 0
        for i in range(start, len(lines)):
            brace_count += lines[i].count('{') - lines[i].count('}')
            if brace_count == 0 and i > start:
                return i + 1
        return min(start + 100, len(lines))  # Fallback

    def _create_fallback_chunk(self, file_path: str, content: str, lines: List[str]) -> CodeChunk:
        """Create a fallback chunk for unparseable files."""
        return CodeChunk(
            id=self._generate_chunk_id(file_path, 0),
            file_path=file_path,
            chunk_type=ChunkType.JAVA_CLASS,
            name=file_path.split('/')[-1].replace('.java', ''),
            content=content,
            start_line=0,
            end_line=len(lines),
            language="java",
            metadata={"parse_error": True}
        )
