"""JSP code chunker."""
import re
from typing import List
from .base_chunker import BaseChunker
from ..models import CodeChunk, ChunkType


class JSPChunker(BaseChunker):
    """Chunker for JSP files."""

    def chunk_file(self, file_path: str) -> List[CodeChunk]:
        """Chunk JSP file by scriptlets and page sections."""
        chunks = []
        content = self._read_file(file_path)
        lines = content.split('\n')

        # Extract scriptlets (Java code in JSP)
        scriptlet_pattern = r'<%.*?%>'
        scriptlets = list(re.finditer(scriptlet_pattern, content, re.DOTALL))

        if scriptlets:
            for idx, match in enumerate(scriptlets):
                scriptlet_content = match.group()
                start_pos = match.start()
                start_line = content[:start_pos].count('\n')
                end_line = start_line + scriptlet_content.count('\n')

                chunk = CodeChunk(
                    id=self._generate_chunk_id(file_path, idx),
                    file_path=file_path,
                    chunk_type=ChunkType.JSP_SCRIPTLET,
                    name=f"scriptlet_{idx}",
                    content=scriptlet_content,
                    start_line=start_line,
                    end_line=end_line,
                    language="jsp",
                    metadata={
                        "is_declaration": scriptlet_content.startswith('<%!'),
                        "is_expression": scriptlet_content.startswith('<%='),
                        "is_directive": scriptlet_content.startswith('<%@')
                    }
                )
                chunks.append(chunk)

        # Create a chunk for the overall page structure
        page_chunk = CodeChunk(
            id=self._generate_chunk_id(file_path, -1),
            file_path=file_path,
            chunk_type=ChunkType.JSP_PAGE,
            name=file_path.split('/')[-1].replace('.jsp', ''),
            content=content[:min(1000, len(content))],  # First 1000 chars
            start_line=0,
            end_line=len(lines),
            language="jsp",
            metadata={
                "total_scriptlets": len(scriptlets),
                "has_taglibs": '<%@ taglib' in content,
                "includes": self._extract_includes(content)
            }
        )
        chunks.insert(0, page_chunk)

        return chunks if len(chunks) > 1 else [page_chunk]

    def _extract_includes(self, content: str) -> List[str]:
        """Extract JSP include directives."""
        include_pattern = r'<%@\s*include\s+file="([^"]+)"'
        return re.findall(include_pattern, content)
