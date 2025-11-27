"""JSP code chunker."""
import re
from typing import List
from chunkers.base_chunker import BaseChunker
from models import CodeChunk, ChunkType


class JSPChunker(BaseChunker):
    """Chunker for JSP files."""

    def chunk_file(self, file_path: str) -> List[CodeChunk]:
        """Chunk JSP file by scriptlets and page sections."""
        chunks = []
        content = self._read_file(file_path)
        lines = content.split('\n')

        # Extract full content for page analysis
        imports = self._extract_imports(content)
        use_beans = self._extract_use_beans(content)

        # Extract scriptlets (Java code in JSP)
        scriptlet_pattern = r'<%.*?%>'
        scriptlets = list(re.finditer(scriptlet_pattern, content, re.DOTALL))

        scriptlet_ids = []
        if scriptlets:
            for idx, match in enumerate(scriptlets):
                scriptlet_content = match.group()
                start_pos = match.start()
                start_line = content[:start_pos].count('\n')
                end_line = start_line + scriptlet_content.count('\n')

                scriptlet_id = self._generate_chunk_id(file_path, idx)
                scriptlet_ids.append(scriptlet_id)

                # Extract imports from this specific scriptlet
                scriptlet_imports = self._extract_imports(scriptlet_content)

                chunk = CodeChunk(
                    id=scriptlet_id,
                    file_path=file_path,
                    chunk_type=ChunkType.JSP_SCRIPTLET,
                    name=f"scriptlet_{idx}",
                    content=scriptlet_content,
                    start_line=start_line,
                    end_line=end_line,
                    language="jsp",
                    imports=scriptlet_imports,
                    metadata={
                        "is_declaration": scriptlet_content.startswith('<%!'),
                        "is_expression": scriptlet_content.startswith('<%='),
                        "is_directive": scriptlet_content.startswith('<%@'),
                        "parent_page": file_path.split('/')[-1].replace('.jsp', '')
                    }
                )
                chunks.append(chunk)

        # Create a chunk for the overall page structure
        page_chunk = CodeChunk(
            id=self._generate_chunk_id(file_path, -1),
            file_path=file_path,
            chunk_type=ChunkType.JSP_PAGE,
            name=file_path.split('/')[-1].replace('.jsp', ''),
            content=content[:min(2000, len(content))],  # First 2000 chars for more context
            start_line=0,
            end_line=len(lines),
            language="jsp",
            imports=imports,
            metadata={
                "total_scriptlets": len(scriptlets),
                "scriptlet_ids": scriptlet_ids,
                "has_taglibs": '<%@ taglib' in content,
                "includes": self._extract_includes(content),
                "use_beans": use_beans,
                "taglibs": self._extract_taglibs(content)
            }
        )
        chunks.insert(0, page_chunk)

        return chunks if len(chunks) > 1 else [page_chunk]

    def _extract_imports(self, content: str) -> List[str]:
        """Extract Java imports from JSP page directive."""
        import_pattern = r'<%@\s*page\s+import="([^"]+)"'
        imports = []
        for match in re.findall(import_pattern, content):
            # Split multiple imports separated by comma
            imports.extend([imp.strip() for imp in match.split(',')])
        return imports

    def _extract_includes(self, content: str) -> List[str]:
        """Extract JSP include directives."""
        include_pattern = r'<%@\s*include\s+file="([^"]+)"'
        return re.findall(include_pattern, content)

    def _extract_use_beans(self, content: str) -> List[str]:
        """Extract useBean directives."""
        usebean_pattern = r'<jsp:useBean[^>]+class="([^"]+)"'
        return re.findall(usebean_pattern, content)

    def _extract_taglibs(self, content: str) -> List[str]:
        """Extract taglib directives."""
        taglib_pattern = r'<%@\s*taglib\s+uri="([^"]+)"'
        return re.findall(taglib_pattern, content)
