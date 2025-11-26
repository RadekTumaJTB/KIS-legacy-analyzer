#!/usr/bin/env python3
"""CLI script to run code analysis pipeline."""
import argparse
import sys
from main import CodeAnalyzerPipeline


def main():
    parser = argparse.ArgumentParser(
        description="Analyze legacy code and prepare for migration"
    )
    parser.add_argument(
        "--no-init-dbs",
        action="store_true",
        help="Skip database initialization (use existing data)"
    )
    parser.add_argument(
        "--report-only",
        action="store_true",
        help="Only generate reports from existing data"
    )

    args = parser.parse_args()

    print("""
╔══════════════════════════════════════════════════════════════════════╗
║           LEGACY CODE ANALYZER & MIGRATION PLANNER                    ║
║                                                                       ║
║  Analyzing codebase for migration to modern technologies             ║
╚══════════════════════════════════════════════════════════════════════╝
    """)

    pipeline = CodeAnalyzerPipeline()

    try:
        if args.report_only:
            print("\n📊 Generating reports from existing data...")
            pipeline._generate_reports()
            print("\n✅ Reports generated successfully!")
        else:
            initialize_dbs = not args.no_init_dbs
            result = pipeline.run(initialize_dbs=initialize_dbs)
            print(f"""
╔══════════════════════════════════════════════════════════════════════╗
║                        ANALYSIS COMPLETE                              ║
╠══════════════════════════════════════════════════════════════════════╣
║  Total Chunks:       {result['total_chunks']:>6}                                       ║
║  Dependencies:       {result['total_dependencies']:>6}                                       ║
║  Output Directory:   {result['output_dir']:<45} ║
╚══════════════════════════════════════════════════════════════════════╝

Next steps:
1. Review the migration summary: {result['output_dir']}/reports/MIGRATION_SUMMARY.md
2. Explore the dependency graph in Neo4j Browser: http://localhost:7474
3. Use Qdrant for semantic code search: http://localhost:6333/dashboard
            """)

        return 0

    except Exception as e:
        print(f"\n❌ ERROR: {e}", file=sys.stderr)
        import traceback
        traceback.print_exc()
        return 1

    finally:
        pipeline.neo4j_store.close()


if __name__ == "__main__":
    sys.exit(main())
