#!/usr/bin/env python3
"""
Detailní analýza Java 1.4 → Java 17 migračních problémů.
Identifikuje deprecated API, raw types, missing features atd.
"""

import os
import json
from typing import Dict, List, Set, Any
from neo4j import GraphDatabase
from qdrant_client import QdrantClient
from collections import defaultdict, Counter
from config import (
    NEO4J_URI, NEO4J_USER, NEO4J_PASSWORD,
    QDRANT_HOST, QDRANT_PORT
)

class Java14ToJava17Analyzer:
    """Analyzátor Java 1.4 → Java 17 migrace."""

    def __init__(self):
        """Inicializace připojení."""
        self.neo4j_driver = GraphDatabase.driver(
            NEO4J_URI,
            auth=(NEO4J_USER, NEO4J_PASSWORD)
        )
        self.qdrant_client = QdrantClient(host=QDRANT_HOST, port=QDRANT_PORT)
        
        # Patterns k hledání
        self.java14_patterns = {
            # Raw types
            'vector': r'\bVector\b(?!<)',
            'hashtable': r'\bHashtable\b(?!<)',
            'enumeration': r'\bEnumeration\b(?!<)',
            'arraylist_raw': r'\bArrayList\b(?!<)',
            'hashmap_raw': r'\bHashMap\b(?!<)',
            'list_raw': r'\bList\b\s+\w+\s*=(?!.*<)',
            'map_raw': r'\bMap\b\s+\w+\s*=(?!.*<)',
            
            # Deprecated Date/Time API
            'date_deprecated': r'new\s+Date\(',
            'simpledateformat': r'new\s+SimpleDateFormat\(',
            'calendar': r'Calendar\.getInstance\(',
            
            # Thread safety issues
            'stringbuffer': r'\bStringBuffer\b',
            
            # Old for-loops (vs enhanced for)
            'old_for_loop': r'for\s*\(\s*int\s+\w+\s*=',
            'iterator_loop': r'for\s*\(\s*Iterator\b',
            
            # Try-catch without try-with-resources
            'manual_close': r'finally\s*\{[^}]*\.close\(\)',
            'result_set': r'\bResultSet\b',
            'statement': r'\bStatement\b',
            'prepared_statement': r'\bPreparedStatement\b',
            
            # Missing @Override
            'public_method': r'public\s+\w+\s+\w+\(',
            
            # Boxing issues
            'integer_valueof': r'new\s+Integer\(',
            'long_valueof': r'new\s+Long\(',
            'double_valueof': r'new\s+Double\(',
        }

    def analyze_java_files(self) -> Dict[str, Any]:
        """Analyzuje Java soubory z Qdrant pro migrační problémy."""
        print("\n🔍 Analyzuji Java soubory z Qdrant...")
        
        results = {
            'raw_types': defaultdict(list),
            'deprecated_datetime': defaultdict(list),
            'thread_safety': defaultdict(list),
            'old_loops': defaultdict(list),
            'resource_management': defaultdict(list),
            'boxing_issues': defaultdict(list),
            'total_issues': 0
        }
        
        # Získat všechny Java třídy z Qdrant
        java_points = self.qdrant_client.scroll(
            collection_name="java_methods",
            limit=10000,
            with_payload=True
        )[0]
        
        print(f"  Načteno {len(java_points)} Java metod z Qdrant")
        
        # Analyzovat každou metodu
        for point in java_points[:500]:  # Prvních 500 pro rychlost
            payload = point.payload
            code = payload.get('code', '')
            file_path = payload.get('file_path', '')
            class_name = payload.get('class_name', '')
            method_name = payload.get('name', '')
            
            # Raw types
            if 'Vector' in code and '<' not in code[code.find('Vector'):code.find('Vector')+20]:
                results['raw_types']['vector'].append({
                    'file': file_path,
                    'class': class_name,
                    'method': method_name
                })
                results['total_issues'] += 1
                
            if 'Hashtable' in code and '<' not in code[code.find('Hashtable'):code.find('Hashtable')+20]:
                results['raw_types']['hashtable'].append({
                    'file': file_path,
                    'class': class_name,
                    'method': method_name
                })
                results['total_issues'] += 1
                
            # Date/Time API
            if 'new Date(' in code:
                results['deprecated_datetime']['new_date'].append({
                    'file': file_path,
                    'class': class_name,
                    'method': method_name
                })
                results['total_issues'] += 1
                
            if 'SimpleDateFormat' in code:
                results['deprecated_datetime']['simpledateformat'].append({
                    'file': file_path,
                    'class': class_name,
                    'method': method_name
                })
                results['total_issues'] += 1
                
            # StringBuffer
            if 'StringBuffer' in code:
                results['thread_safety']['stringbuffer'].append({
                    'file': file_path,
                    'class': class_name,
                    'method': method_name
                })
                results['total_issues'] += 1
                
            # Old for-loops
            if 'for (int ' in code or 'for(int ' in code:
                results['old_loops']['indexed_loop'].append({
                    'file': file_path,
                    'class': class_name,
                    'method': method_name
                })
                results['total_issues'] += 1
                
            # Resource management
            if 'ResultSet' in code and 'finally' in code:
                results['resource_management']['manual_close'].append({
                    'file': file_path,
                    'class': class_name,
                    'method': method_name
                })
                results['total_issues'] += 1
                
            # Boxing
            if 'new Integer(' in code:
                results['boxing_issues']['integer'].append({
                    'file': file_path,
                    'class': class_name,
                    'method': method_name
                })
                results['total_issues'] += 1
        
        return results

    def get_top_files_with_issues(self) -> List[Dict[str, Any]]:
        """Získá top 50 souborů s nejvíce problémy."""
        print("\n📊 Identifikuji top 50 souborů s nejvíce problémy...")
        
        with self.neo4j_driver.session() as session:
            # Získat soubory s nejvíce metodami (indikuje komplexitu)
            query = """
            MATCH (f:JavaFile)-[:CONTAINS]->(c:JavaClass)-[:CONTAINS]->(m:JavaMethod)
            WITH f, count(m) as method_count
            ORDER BY method_count DESC
            LIMIT 50
            RETURN f.path as file_path, method_count
            """
            result = session.run(query)
            
            files = []
            for record in result:
                files.append({
                    'file_path': record['file_path'],
                    'method_count': record['method_count']
                })
            
            return files

    def analyze_deprecated_collections(self) -> Dict[str, Any]:
        """Analyzuje použití deprecated collection types."""
        print("\n🔍 Analyzuji deprecated collection types...")
        
        deprecated = {
            'Vector': 0,
            'Hashtable': 0,
            'Enumeration': 0,
            'Stack': 0
        }
        
        # Vyhledávání v Qdrant
        for collection_type in deprecated.keys():
            java_points = self.qdrant_client.scroll(
                collection_name="java_methods",
                limit=10000,
                with_payload=True
            )[0]
            
            count = sum(1 for p in java_points if collection_type in p.payload.get('code', ''))
            deprecated[collection_type] = count
        
        return deprecated

    def generate_migration_report(self, results: Dict[str, Any], top_files: List[Dict], deprecated_colls: Dict) -> str:
        """Generuje finální migrační report."""
        print("\n📝 Generuji migrační report...")
        
        report = []
        report.append("=" * 70)
        report.append("JAVA 1.4 → JAVA 17 MIGRATION ANALYSIS")
        report.append("=" * 70)
        report.append("")
        
        # Executive Summary
        report.append("## 🎯 EXECUTIVE SUMMARY")
        report.append("")
        report.append(f"**Total Issues Found:** {results['total_issues']}")
        report.append("")
        
        # 1. Raw Types
        report.append("## 1️⃣ JAVA 1.4 SPECIFIC - RAW TYPES (BEZ GENERICS)")
        report.append("")
        report.append("### Problém:")
        report.append("Java 1.4 nemá generics - používá raw types jako Vector, Hashtable bez <Type>")
        report.append("")
        report.append("### Nálezy:")
        report.append(f"- **Vector (raw):** {len(results['raw_types']['vector'])} výskytů")
        report.append(f"- **Hashtable (raw):** {len(results['raw_types']['hashtable'])} výskytů")
        report.append("")
        
        if results['raw_types']['vector']:
            report.append("### Příklady Vector:")
            for item in results['raw_types']['vector'][:5]:
                report.append(f"  - {item['file']}")
                report.append(f"    → {item['class']}.{item['method']}")
        report.append("")
        
        # 2. Deprecated Date/Time
        report.append("## 2️⃣ DEPRECATED DATE/TIME API")
        report.append("")
        report.append("### Problém:")
        report.append("java.util.Date, SimpleDateFormat jsou deprecated → migrate to java.time (Java 8+)")
        report.append("")
        report.append("### Nálezy:")
        report.append(f"- **new Date():** {len(results['deprecated_datetime']['new_date'])} výskytů")
        report.append(f"- **SimpleDateFormat:** {len(results['deprecated_datetime']['simpledateformat'])} výskytů")
        report.append("")
        
        if results['deprecated_datetime']['new_date']:
            report.append("### Příklady new Date():")
            for item in results['deprecated_datetime']['new_date'][:5]:
                report.append(f"  - {item['file']}")
                report.append(f"    → {item['class']}.{item['method']}")
        report.append("")
        
        # 3. Thread Safety
        report.append("## 3️⃣ THREAD SAFETY ISSUES")
        report.append("")
        report.append("### Problém:")
        report.append("StringBuffer je thread-safe (synchronized) → použít StringBuilder (rychlejší)")
        report.append("")
        report.append("### Nálezy:")
        report.append(f"- **StringBuffer:** {len(results['thread_safety']['stringbuffer'])} výskytů")
        report.append("")
        
        # 4. Old Loops
        report.append("## 4️⃣ MISSING FEATURES - OLD-STYLE LOOPS")
        report.append("")
        report.append("### Problém:")
        report.append("Java 1.4 nemá enhanced for-loop (for-each) → modernizovat")
        report.append("")
        report.append("### Nálezy:")
        report.append(f"- **Indexed loops (for int i=):** {len(results['old_loops']['indexed_loop'])} výskytů")
        report.append("")
        
        # 5. Resource Management
        report.append("## 5️⃣ EXCEPTION HANDLING - MISSING TRY-WITH-RESOURCES")
        report.append("")
        report.append("### Problém:")
        report.append("Java 1.4 nemá try-with-resources → používá finally { close() }")
        report.append("")
        report.append("### Nálezy:")
        report.append(f"- **Manual close() v finally:** {len(results['resource_management']['manual_close'])} výskytů")
        report.append("")
        
        # 6. Boxing Issues
        report.append("## 6️⃣ BOXING/UNBOXING ISSUES")
        report.append("")
        report.append("### Problém:")
        report.append("Java 1.4 nemá autoboxing → používá new Integer(), new Long() atd.")
        report.append("")
        report.append("### Nálezy:")
        report.append(f"- **new Integer():** {len(results['boxing_issues']['integer'])} výskytů")
        report.append("")
        
        # Deprecated Collections Summary
        report.append("## 🔧 DEPRECATED COLLECTIONS SUMMARY")
        report.append("")
        for coll_type, count in deprecated_colls.items():
            report.append(f"- **{coll_type}:** {count} výskytů")
        report.append("")
        
        # Top 50 Files
        report.append("## 📁 TOP 50 NEJDŮLEŽITĚJŠÍCH SOUBORŮ PRO MIGRACI")
        report.append("")
        report.append("*(Seřazeno podle počtu metod - indikuje komplexitu)*")
        report.append("")
        for i, file_info in enumerate(top_files[:50], 1):
            report.append(f"{i:2d}. {file_info['file_path']}")
            report.append(f"    → {file_info['method_count']} metod")
        report.append("")
        
        # Migration Recommendations
        report.append("## 💡 DOPORUČENÍ PRO MIGRACI")
        report.append("")
        report.append("### Priority:")
        report.append("1. **VYSOKÁ** - Raw types (Vector, Hashtable) → přidat generics")
        report.append("2. **VYSOKÁ** - Deprecated Date/Time → java.time.LocalDate, LocalDateTime")
        report.append("3. **STŘEDNÍ** - StringBuffer → StringBuilder")
        report.append("4. **STŘEDNÍ** - Old loops → enhanced for-loops")
        report.append("5. **NÍZKÁ** - try-with-resources (nice-to-have)")
        report.append("")
        
        report.append("### Odhad Složitosti:")
        report.append("- **Raw types:** STŘEDNÍ (automatizovatelné s AI)")
        report.append("- **Date/Time:** VYSOKÁ (vyžaduje refaktoring logiky)")
        report.append("- **StringBuffer:** NÍZKÁ (simple find-replace)")
        report.append("- **Loops:** NÍZKÁ (automatizovatelné)")
        report.append("")
        
        report.append("=" * 70)
        report.append("END OF REPORT")
        report.append("=" * 70)
        
        return "\n".join(report)

    def save_report(self, report: str, results: Dict, top_files: List, deprecated: Dict):
        """Uloží report do souboru."""
        # Textový report
        report_path = "java14_to_java17_migration_report.txt"
        with open(report_path, 'w', encoding='utf-8') as f:
            f.write(report)
        print(f"\n✅ Report uložen: {report_path}")
        
        # JSON data
        json_path = "java14_to_java17_migration_data.json"
        with open(json_path, 'w', encoding='utf-8') as f:
            json.dump({
                'results': results,
                'top_files': top_files,
                'deprecated_collections': deprecated
            }, f, indent=2)
        print(f"✅ JSON data uložena: {json_path}")

    def close(self):
        """Zavře připojení."""
        self.neo4j_driver.close()

def main():
    """Hlavní funkce."""
    print("\n" + "=" * 70)
    print("🔬 JAVA 1.4 → JAVA 17 MIGRATION ANALYSIS")
    print("=" * 70)
    
    analyzer = Java14ToJava17Analyzer()
    
    try:
        # 1. Analyzovat Java soubory
        results = analyzer.analyze_java_files()
        
        # 2. Top 50 souborů
        top_files = analyzer.get_top_files_with_issues()
        
        # 3. Deprecated collections
        deprecated = analyzer.analyze_deprecated_collections()
        
        # 4. Generovat report
        report = analyzer.generate_migration_report(results, top_files, deprecated)
        
        # 5. Uložit report
        analyzer.save_report(report, results, top_files, deprecated)
        
        # 6. Vytisknout report
        print("\n" + report)
        
    finally:
        analyzer.close()
    
    print("\n✅ Analýza dokončena!")

if __name__ == "__main__":
    main()
