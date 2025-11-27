"""Main processing pipeline for code analysis and migration planning."""
import os
import json
from pathlib import Path
from typing import List
from tqdm import tqdm

from config import SOURCES_DIR, OUTPUT_DIR, REPORT_DIR
from models import CodeChunk
from chunkers import JavaChunker, JSPChunker, SQLChunker
from analyzers import DependencyAnalyzer
from storage import QdrantStore, Neo4jStore


class CodeAnalyzerPipeline:
    """Main pipeline for analyzing legacy code and preparing for migration."""

    def __init__(self):
        """Initialize the pipeline."""
        self.java_chunker = JavaChunker()
        self.jsp_chunker = JSPChunker()
        self.sql_chunker = SQLChunker()
        self.dependency_analyzer = DependencyAnalyzer()
        self.qdrant_store = QdrantStore()
        self.neo4j_store = Neo4jStore()

        # Create output directories
        os.makedirs(OUTPUT_DIR, exist_ok=True)
        os.makedirs(REPORT_DIR, exist_ok=True)

    def run(self, initialize_dbs: bool = True):
        """Run the complete analysis pipeline."""
        print("=" * 80)
        print("CODE ANALYZER PIPELINE - LEGACY MIGRATION ANALYSIS")
        print("=" * 80)

        # Step 1: Initialize databases
        if initialize_dbs:
            print("\n[1/6] Initializing databases...")
            self.qdrant_store.initialize_collection(recreate=True)
            self.neo4j_store.initialize_schema(clear_existing=True)

        # Step 2: Scan and chunk code
        print("\n[2/6] Scanning and chunking source code...")
        all_chunks = self._scan_and_chunk_code()
        print(f"Total chunks created: {len(all_chunks)}")

        # Save chunks for reference
        self._save_chunks_json(all_chunks)

        # Step 3: Analyze dependencies
        print("\n[3/6] Analyzing dependencies...")
        dependencies = self.dependency_analyzer.analyze_dependencies(all_chunks)
        print(f"Total dependencies found: {len(dependencies)}")

        # Step 4: Upload to Qdrant
        print("\n[4/6] Uploading chunks to Qdrant vector database...")
        self.qdrant_store.upload_chunks(all_chunks)

        # Step 5: Create Neo4j graph
        print("\n[5/6] Creating dependency graph in Neo4j...")
        self.neo4j_store.create_chunk_nodes(all_chunks)
        self.neo4j_store.create_dependencies(dependencies)

        # Step 6: Generate reports
        print("\n[6/6] Generating analysis reports...")
        self._generate_reports()

        print("\n" + "=" * 80)
        print("PIPELINE COMPLETED SUCCESSFULLY")
        print("=" * 80)
        print(f"\nReports saved to: {REPORT_DIR}")

        return {
            "total_chunks": len(all_chunks),
            "total_dependencies": len(dependencies),
            "output_dir": OUTPUT_DIR
        }

    def _scan_and_chunk_code(self) -> List[CodeChunk]:
        """Scan source directory and chunk all files."""
        all_chunks = []

        # Process Java files
        java_files = list(Path(SOURCES_DIR).rglob("*.java"))
        print(f"Processing {len(java_files)} Java files...")
        for java_file in tqdm(java_files, desc="Java files"):
            try:
                chunks = self.java_chunker.chunk_file(str(java_file))
                all_chunks.extend(chunks)
            except Exception as e:
                print(f"\nError processing {java_file}: {e}")

        # Process JSP files
        jsp_files = list(Path(SOURCES_DIR).rglob("*.jsp"))
        print(f"\nProcessing {len(jsp_files)} JSP files...")
        for jsp_file in tqdm(jsp_files, desc="JSP files"):
            try:
                chunks = self.jsp_chunker.chunk_file(str(jsp_file))
                all_chunks.extend(chunks)
            except Exception as e:
                print(f"\nError processing {jsp_file}: {e}")

        # Process SQL files
        sql_files = list(Path(SOURCES_DIR).rglob("*.sql"))
        print(f"\nProcessing {len(sql_files)} SQL files...")
        for sql_file in tqdm(sql_files, desc="SQL files"):
            try:
                chunks = self.sql_chunker.chunk_file(str(sql_file))
                all_chunks.extend(chunks)
            except Exception as e:
                print(f"\nError processing {sql_file}: {e}")

        return all_chunks

    def _save_chunks_json(self, chunks: List[CodeChunk]):
        """Save chunks to JSON for reference."""
        chunks_data = [chunk.to_dict() for chunk in chunks]

        output_file = os.path.join(OUTPUT_DIR, "chunks.json")
        with open(output_file, 'w', encoding='utf-8') as f:
            json.dump(chunks_data, f, indent=2, ensure_ascii=False)

        print(f"Chunks saved to: {output_file}")

    def _generate_reports(self):
        """Generate analysis and migration reports."""
        # Get Qdrant stats
        qdrant_stats = self.qdrant_store.get_collection_stats()

        # Get Neo4j dependency analysis
        neo4j_analysis = self.neo4j_store.analyze_dependencies()

        # Get migration clusters
        migration_clusters = self.neo4j_store.find_migration_clusters()

        # Combined report
        report = {
            "summary": {
                "total_chunks": qdrant_stats["points_count"],
                "total_dependencies": neo4j_analysis["total_dependencies"],
                "orphaned_chunks": neo4j_analysis["orphaned_chunks"],
                "circular_dependencies": len(neo4j_analysis["circular_dependencies"])
            },
            "qdrant_statistics": qdrant_stats,
            "dependency_analysis": neo4j_analysis,
            "migration_clusters": migration_clusters
        }

        # Save main report
        report_file = os.path.join(REPORT_DIR, "migration_analysis_report.json")
        with open(report_file, 'w', encoding='utf-8') as f:
            json.dump(report, f, indent=2, ensure_ascii=False)

        print(f"\nMain report saved to: {report_file}")

        # Generate summary markdown
        self._generate_markdown_summary(report)

    def _generate_markdown_summary(self, report: dict):
        """Generate a human-readable markdown summary."""
        md_content = f"""# Legacy Code Migration Analysis Report

## Summary

- **Total Code Chunks**: {report['summary']['total_chunks']:,}
- **Total Dependencies**: {report['summary']['total_dependencies']:,}
- **Orphaned Chunks**: {report['summary']['orphaned_chunks']:,}
- **Circular Dependencies**: {report['summary']['circular_dependencies']:,}

## Highly Connected Components

The following components have the most dependencies and should be prioritized for analysis:

"""
        for idx, item in enumerate(report['dependency_analysis']['highly_connected'][:10], 1):
            md_content += f"{idx}. **{item['name']}** ({item['chunk_type']}) - {item['connections']} connections\n"

        md_content += """
## Migration Clusters

These clusters represent groups of related code that should be migrated together:

"""
        for idx, cluster in enumerate(report['migration_clusters'][:10], 1):
            md_content += f"{idx}. **{cluster['name']}** (Package: {cluster['package']}) - Cluster size: {cluster['cluster_size']}\n"

        md_content += """
## Circular Dependencies

The following components have circular dependencies that need to be resolved:

"""
        for idx, circ in enumerate(report['dependency_analysis']['circular_dependencies'][:10], 1):
            md_content += f"{idx}. **{circ['name']}** - Cycle length: {circ['cycle_length']}\n"

        md_content += """
## Next Steps

1. **Review Highly Connected Components**: Start with the most connected components as they likely contain core business logic
2. **Analyze Migration Clusters**: Plan migration in clusters to maintain functionality
3. **Resolve Circular Dependencies**: Break circular dependencies before migration
4. **Prioritize by Business Value**: Use the dependency graph to identify critical paths

## Tools Used

- **QdrantDB**: Vector database for semantic code search
- **Neo4j**: Graph database for dependency visualization and analysis
- **Sentence Transformers**: Code embedding for similarity search
"""

        md_file = os.path.join(REPORT_DIR, "MIGRATION_SUMMARY.md")
        with open(md_file, 'w', encoding='utf-8') as f:
            f.write(md_content)

        print(f"Summary report saved to: {md_file}")


def main():
    """Main entry point."""
    pipeline = CodeAnalyzerPipeline()
    try:
        result = pipeline.run(initialize_dbs=True)
        print(f"\n✅ Analysis complete! Processed {result['total_chunks']} chunks.")
    except Exception as e:
        print(f"\n❌ Error during analysis: {e}")
        raise
    finally:
        pipeline.neo4j_store.close()


if __name__ == "__main__":
    main()
