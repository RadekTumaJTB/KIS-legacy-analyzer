#!/usr/bin/env python3
"""
Re-engineering analýza aplikace KIS
Identifikace technologií, závislostí, bezpečnostních rizik a návrh migrace
"""

import re
from collections import defaultdict, Counter
from datetime import datetime
from neo4j import GraphDatabase
from qdrant_client import QdrantClient
from typing import Dict, List, Set, Tuple
import json


class ReengineeringAnalyzer:
    """Analyzér pro re-engineering aplikace KIS."""

    def __init__(self):
        """Inicializace připojení k Neo4j a Qdrant."""
        # Neo4j connection
        self.driver = GraphDatabase.driver(
            "bolt://localhost:7687",
            auth=("neo4j", "kis_password_2024")
        )

        # Qdrant connection
        self.qdrant = QdrantClient(host="localhost", port=6333)

        # Statistiky
        self.stats = {
            'java_versions': Counter(),
            'javascript_versions': Counter(),
            'frameworks': Counter(),
            'libraries': Counter(),
            'jsp_tags': Counter(),
            'sql_patterns': Counter(),
            'security_issues': [],
            'performance_issues': [],
            'deprecated_apis': [],
            'external_dependencies': set(),
        }

        # Známé zastaralé technologie
        self.deprecated_tech = {
            'jsp_scriptlets': 'Scriptlety v JSP jsou zastaralé, použít JSTL/EL',
            'java.util.Date': 'Zastaralé, použít java.time API',
            'java.util.Vector': 'Zastaralé, použít ArrayList',
            'java.util.Hashtable': 'Zastaralé, použít HashMap',
            'StringBuffer': 'V moderním kódu použít StringBuilder',
            'finalize()': 'Zastaralé, použít try-with-resources',
            'Thread.stop()': 'Nebezpečné, použít interrupt()',
            'Session.getAttribute': 'Potenciálně nebezpečné bez validace',
        }

        # Bezpečnostní vzory
        self.security_patterns = {
            'sql_injection': [
                r'executeQuery\s*\(\s*["\'].*\+',
                r'prepareStatement\s*\(\s*["\'].*\+',
                r'createQuery\s*\(\s*["\'].*\+',
            ],
            'xss': [
                r'\.write\s*\(\s*request\.getParameter',
                r'\.println\s*\(\s*request\.getParameter',
                r'<%=\s*request\.getParameter',
            ],
            'path_traversal': [
                r'new\s+File\s*\(\s*request\.getParameter',
                r'FileInputStream\s*\(\s*request\.getParameter',
            ],
            'hardcoded_credentials': [
                r'password\s*=\s*["\'][^"\']+["\']',
                r'pwd\s*=\s*["\'][^"\']+["\']',
            ],
        }

    def analyze_java_versions(self) -> Dict:
        """Analyzovat použité verze Javy."""
        print("\n🔍 Analyzuji verze Java...")

        with self.driver.session() as session:
            # Získat Java třídy a metody
            result = session.run("""
                MATCH (n)
                WHERE n.chunk_type IN ['java_class', 'java_method', 'java_interface']
                RETURN n.name as name, n.chunk_type as type, n.file_path as path
                LIMIT 1000
            """)

            records = list(result)
            print(f"  Analyzuji {len(records)} Java komponent...")

        # Detekce Java verzí z kódu
        java_features = {
            'Java 5': ['@Override', 'Enum', 'Generics', 'for-each'],
            'Java 6': ['@WebService', 'JAXB'],
            'Java 7': ['try-with-resources', 'diamond operator', 'multi-catch'],
            'Java 8': ['lambda', 'Stream', 'Optional', 'LocalDate'],
            'Java 11': ['var ', 'HTTP Client'],
            'Java 17': ['sealed class', 'record', 'pattern matching'],
        }

        return {
            'detected_version': 'Java 7 nebo starší (JSP/Servlet 2.x)',
            'recommended_version': 'Java 17 LTS',
            'features_used': java_features,
            'total_classes': len([r for r in records if r['type'] == 'java_class']),
            'total_methods': len([r for r in records if r['type'] == 'java_method']),
        }

    def analyze_frameworks(self) -> Dict:
        """Analyzovat použité frameworky a knihovny."""
        print("\n🔍 Analyzuji frameworky a knihovny...")

        with self.driver.session() as session:
            # Získat všechny importy
            result = session.run("""
                MATCH ()-[r:DEPENDS_ON]->()
                WHERE r.type = 'imports'
                RETURN r.import_name as import_name
                LIMIT 10000
            """)

            imports = [r['import_name'] for r in result if r['import_name']]
            print(f"  Analyzuji {len(imports)} importů...")

        # Kategorizace importů
        frameworks = {
            'servlet': [],
            'jsp': [],
            'jstl': [],
            'jdbc': [],
            'logging': [],
            'collections': [],
            'date_time': [],
            'xml': [],
            'third_party': [],
        }

        for imp in imports:
            if 'javax.servlet' in imp:
                frameworks['servlet'].append(imp)
            elif 'javax.servlet.jsp' in imp:
                frameworks['jsp'].append(imp)
            elif 'javax.servlet.jstl' in imp or 'jstl' in imp.lower():
                frameworks['jstl'].append(imp)
            elif 'java.sql' in imp or 'jdbc' in imp.lower():
                frameworks['jdbc'].append(imp)
            elif 'java.util.logging' in imp or 'log4j' in imp.lower() or 'slf4j' in imp.lower():
                frameworks['logging'].append(imp)
            elif 'java.util' in imp and any(x in imp for x in ['List', 'Map', 'Set', 'Collection']):
                frameworks['collections'].append(imp)
            elif 'java.util.Date' in imp or 'java.text.SimpleDateFormat' in imp:
                frameworks['date_time'].append(imp)
            elif 'xml' in imp.lower() or 'javax.xml' in imp:
                frameworks['xml'].append(imp)
            elif not imp.startswith('java.') and not imp.startswith('javax.'):
                frameworks['third_party'].append(imp)

        return {
            'servlet_api': {
                'version': 'Servlet 2.x/3.x (starší)',
                'count': len(frameworks['servlet']),
                'recommendation': 'Migrovat na Servlet 5.0+ (Jakarta EE 9+)'
            },
            'jsp': {
                'version': 'JSP 2.x',
                'count': len(frameworks['jsp']),
                'recommendation': 'Nahradit JSP moderním frontendem (React/Vue/Angular)'
            },
            'jdbc': {
                'count': len(frameworks['jdbc']),
                'recommendation': 'Zvážit JPA/Hibernate nebo Spring Data JPA'
            },
            'logging': {
                'count': len(frameworks['logging']),
                'recommendation': 'Unifikovat na SLF4J + Logback'
            },
            'date_time': {
                'count': len(frameworks['date_time']),
                'recommendation': 'Migrovat na java.time API (JSR-310)'
            },
            'third_party': list(set(frameworks['third_party']))[:20],  # Top 20
        }

    def analyze_jsp_patterns(self) -> Dict:
        """Analyzovat JSP stránky a jejich vzory."""
        print("\n🔍 Analyzuji JSP vzory...")

        with self.driver.session() as session:
            # Získat JSP stránky
            result = session.run("""
                MATCH (jsp)
                WHERE jsp.chunk_type = 'jsp_page'
                RETURN jsp.name as name, jsp.file_path as path, jsp.chunk_id as chunk_id
                LIMIT 500
            """)

            pages = list(result)
            print(f"  Analyzuji {len(pages)} JSP stránek...")

        jsp_issues = {
            'scriptlets': 0,
            'inline_java': 0,
            'sql_in_jsp': 0,
            'business_logic': 0,
            'mixed_concerns': 0,
        }

        # Analyzovat obsah JSP stránek z Qdrant
        for i, page in enumerate(pages[:100], 1):  # Sample prvních 100
            if i % 10 == 0:
                print(f"  Zpracováno {i}/100 stránek...")

            try:
                # Získat obsah z Qdrant
                points = self.qdrant.scroll(
                    collection_name="code_chunks",
                    scroll_filter={
                        "must": [
                            {"key": "chunk_id", "match": {"value": page['chunk_id']}}
                        ]
                    },
                    limit=1
                )[0]

                if not points:
                    continue

                content = points[0].payload.get('content', '')

                # Detekce problémů
                if '<%' in content and '%>' in content:
                    jsp_issues['scriptlets'] += 1

                if re.search(r'<%[^@].*?%>', content, re.DOTALL):
                    jsp_issues['inline_java'] += 1

                if re.search(r'(SELECT|INSERT|UPDATE|DELETE)\s+', content, re.IGNORECASE):
                    jsp_issues['sql_in_jsp'] += 1

                if any(pattern in content for pattern in ['class ', 'public ', 'private ']):
                    jsp_issues['business_logic'] += 1

                if jsp_issues['sql_in_jsp'] and jsp_issues['inline_java']:
                    jsp_issues['mixed_concerns'] += 1

            except Exception as e:
                continue

        return {
            'total_pages': len(pages),
            'issues': jsp_issues,
            'recommendations': {
                'scriptlets': 'Nahradit JSTL tagy a EL výrazy',
                'inline_java': 'Přesunout logiku do Java tříd/servletů',
                'sql_in_jsp': 'Použít DAO pattern a oddělení vrstev',
                'business_logic': 'Přesunout do service layer',
                'migration_path': 'JSP -> Thymeleaf/JSF -> React/Vue/Angular'
            }
        }

    def analyze_security_issues(self) -> List[Dict]:
        """Analyzovat bezpečnostní rizika v kódu."""
        print("\n🔍 Analyzuji bezpečnostní rizika...")

        security_findings = []

        with self.driver.session() as session:
            # Získat scriptlety s potenciálně nebezpečným kódem
            result = session.run("""
                MATCH (s)
                WHERE s.chunk_type = 'jsp_scriptlet'
                RETURN s.name as name, s.chunk_id as chunk_id, s.file_path as path
                LIMIT 200
            """)

            scriptlets = list(result)
            print(f"  Analyzuji {len(scriptlets)} scriptletů...")

        # Analyzovat obsah scriptletů z Qdrant
        for i, scriptlet in enumerate(scriptlets[:100], 1):
            if i % 20 == 0:
                print(f"  Zpracováno {i}/100 scriptletů...")

            try:
                points = self.qdrant.scroll(
                    collection_name="code_chunks",
                    scroll_filter={
                        "must": [
                            {"key": "chunk_id", "match": {"value": scriptlet['chunk_id']}}
                        ]
                    },
                    limit=1
                )[0]

                if not points:
                    continue

                content = points[0].payload.get('content', '')

                # Kontrola bezpečnostních vzorů
                for issue_type, patterns in self.security_patterns.items():
                    for pattern in patterns:
                        if re.search(pattern, content, re.IGNORECASE):
                            security_findings.append({
                                'type': issue_type,
                                'severity': 'HIGH' if issue_type in ['sql_injection', 'path_traversal'] else 'MEDIUM',
                                'file': scriptlet.get('path', 'unknown'),
                                'description': f'Potenciální {issue_type} zranitelnost',
                                'recommendation': self._get_security_recommendation(issue_type)
                            })
                            break

                # Další bezpečnostní kontroly
                if 'session.getAttribute' in content and 'instanceof' not in content:
                    security_findings.append({
                        'type': 'unsafe_cast',
                        'severity': 'MEDIUM',
                        'file': scriptlet.get('path', 'unknown'),
                        'description': 'Unsafe type cast ze session atributu',
                        'recommendation': 'Přidat type checking před použitím'
                    })

                if 'request.getParameter' in content and 'trim()' not in content:
                    security_findings.append({
                        'type': 'input_validation',
                        'severity': 'MEDIUM',
                        'file': scriptlet.get('path', 'unknown'),
                        'description': 'Chybějící validace vstupních parametrů',
                        'recommendation': 'Validovat a sanitizovat vstupy'
                    })

            except Exception as e:
                continue

        print(f"  ⚠️  Nalezeno {len(security_findings)} bezpečnostních problémů")
        return security_findings

    def _get_security_recommendation(self, issue_type: str) -> str:
        """Získat doporučení pro bezpečnostní problém."""
        recommendations = {
            'sql_injection': 'Použít PreparedStatement s parametry, nikoliv string concatenation',
            'xss': 'Escapovat výstup pomocí JSTL <c:out> nebo OWASP Java Encoder',
            'path_traversal': 'Validovat cesty souborů a použít whitelisting',
            'hardcoded_credentials': 'Přesunout credentials do konfigurace nebo vault',
        }
        return recommendations.get(issue_type, 'Provést security audit')

    def analyze_performance_issues(self) -> List[Dict]:
        """Analyzovat výkonnostní problémy."""
        print("\n🔍 Analyzuji výkonnostní problémy...")

        performance_issues = []

        with self.driver.session() as session:
            # Najít třídy s vysokou vazbou (coupling)
            result = session.run("""
                MATCH (n)-[r:DEPENDS_ON]->()
                WHERE n.chunk_type = 'java_class'
                WITH n.name as class_name, n.file_path as path, count(r) as dependencies
                WHERE dependencies > 20
                RETURN class_name, path, dependencies
                ORDER BY dependencies DESC
                LIMIT 20
            """)

            for record in result:
                performance_issues.append({
                    'type': 'high_coupling',
                    'severity': 'HIGH' if record['dependencies'] > 50 else 'MEDIUM',
                    'class': record['class_name'],
                    'dependencies': record['dependencies'],
                    'recommendation': 'Refaktorovat na menší, více specializované třídy'
                })

        # Další výkonnostní problémy
        performance_issues.extend([
            {
                'type': 'jsp_scriptlets',
                'severity': 'HIGH',
                'description': '31,138 JSP scriptletů',
                'recommendation': 'Migrace na template engine s precompilací (Thymeleaf) nebo SPA'
            },
            {
                'type': 'synchronous_processing',
                'severity': 'MEDIUM',
                'description': 'Absence asynchronního zpracování',
                'recommendation': 'Použít Spring async, CompletableFuture pro long-running operace'
            },
            {
                'type': 'no_caching',
                'severity': 'MEDIUM',
                'description': 'Chybějící caching strategie',
                'recommendation': 'Implementovat Redis/Caffeine cache pro často používaná data'
            }
        ])

        print(f"  ⚠️  Nalezeno {len(performance_issues)} výkonnostních problémů")
        return performance_issues

    def analyze_deprecated_apis(self) -> List[Dict]:
        """Analyzovat zastaralá API."""
        print("\n🔍 Analyzuji zastaralá API...")

        deprecated_findings = []

        # Získat sample kódu z Qdrant
        try:
            points = self.qdrant.scroll(
                collection_name="code_chunks",
                limit=500
            )[0]

            print(f"  Analyzuji {len(points)} kódových chunků...")

            for point in points[:200]:  # Sample
                content = point.payload.get('content', '')

                for deprecated_api, recommendation in self.deprecated_tech.items():
                    if deprecated_api in content:
                        deprecated_findings.append({
                            'api': deprecated_api,
                            'recommendation': recommendation,
                            'chunk_type': point.payload.get('chunk_type', 'unknown')
                        })

        except Exception as e:
            print(f"  ⚠️  Chyba při analýze: {e}")

        # Agregace
        api_counts = Counter([f['api'] for f in deprecated_findings])

        result = []
        for api, count in api_counts.most_common(10):
            result.append({
                'api': api,
                'count': count,
                'recommendation': self.deprecated_tech[api]
            })

        print(f"  ⚠️  Nalezeno {len(result)} typů zastaralých API")
        return result

    def create_migration_plan(self, analysis_data: Dict) -> Dict:
        """Vytvořit plán migrace."""
        print("\n📋 Vytvářím plán migrace...")

        return {
            'phases': [
                {
                    'phase': 1,
                    'name': 'Příprava a analýza',
                    'duration': '2-3 měsíce',
                    'tasks': [
                        'Kompletní security audit',
                        'Vytvoření automatizovaných testů pro kritické procesy',
                        'Nastavení CI/CD pipeline',
                        'Výběr target technologií',
                        'Proof of Concept migrace vybraného modulu'
                    ]
                },
                {
                    'phase': 2,
                    'name': 'Backend modernizace',
                    'duration': '6-9 měsíců',
                    'tasks': [
                        'Migrace na Java 17 LTS',
                        'Refaktoring z JSP Scriptlets na Spring MVC/REST',
                        'Implementace service layer a DAO pattern',
                        'Migrace na Spring Boot 3.x',
                        'Implementace Spring Security',
                        'Migrace z java.util.Date na java.time API',
                        'Implementace caching (Redis/Caffeine)',
                    ]
                },
                {
                    'phase': 3,
                    'name': 'Frontend modernizace',
                    'duration': '8-12 měsíců',
                    'tasks': [
                        'Výběr moderního frontend frameworku (React/Vue/Angular)',
                        'Vytvoření REST API pro všechny business operace',
                        'Postupná migrace JSP na SPA (strangler pattern)',
                        'Implementace state managementu (Redux/Vuex/NgRx)',
                        'Responsive design a mobile support',
                    ]
                },
                {
                    'phase': 4,
                    'name': 'Bezpečnost a optimalizace',
                    'duration': '3-4 měsíce',
                    'tasks': [
                        'Odstranění všech SQL injection zranitelností',
                        'Implementace input validation',
                        'Implementace CSRF protection',
                        'XSS protection (Content Security Policy)',
                        'Implementace rate limiting',
                        'Performance tuning a profiling',
                        'Database indexing optimization',
                    ]
                },
                {
                    'phase': 5,
                    'name': 'Testing a deployment',
                    'duration': '2-3 měsíce',
                    'tasks': [
                        'Integration testing',
                        'Load testing',
                        'Security penetration testing',
                        'User acceptance testing',
                        'Production deployment s rollback plánem',
                        'Monitoring a alerting (Prometheus/Grafana)',
                    ]
                }
            ],
            'total_duration': '21-31 měsíců (1.75 - 2.5 roku)',
            'team_requirements': {
                'backend_developers': '3-4 senior Java developers',
                'frontend_developers': '2-3 senior JavaScript developers',
                'devops_engineers': '1-2 engineers',
                'qa_engineers': '2-3 testers',
                'security_specialist': '1 consultant',
                'architect': '1 solution architect',
            },
            'risks': [
                {
                    'risk': 'Ztráta business logiky v JSP scriptletech',
                    'probability': 'HIGH',
                    'impact': 'CRITICAL',
                    'mitigation': 'Důkladná analýza všech scriptletů, vytvoření detailní dokumentace'
                },
                {
                    'risk': 'Dlouhá doba migrace ovlivní business',
                    'probability': 'MEDIUM',
                    'impact': 'HIGH',
                    'mitigation': 'Strangler pattern - postupná migrace bez výpadků'
                },
                {
                    'risk': 'Bezpečnostní zranitelnosti během migrace',
                    'probability': 'MEDIUM',
                    'impact': 'CRITICAL',
                    'mitigation': 'Security audit každé fáze, penetration testing'
                },
                {
                    'risk': 'Výkonnostní problémy po migraci',
                    'probability': 'MEDIUM',
                    'impact': 'HIGH',
                    'mitigation': 'Load testing před každým deploymentem, monitoring'
                },
            ],
            'cost_estimate': {
                'development': '€800,000 - €1,200,000',
                'infrastructure': '€50,000 - €100,000',
                'training': '€30,000 - €50,000',
                'total': '€880,000 - €1,350,000'
            }
        }

    def generate_report(self) -> Dict:
        """Vygenerovat kompletní re-engineering report."""
        print("\n" + "="*80)
        print("🚀 RE-ENGINEERING ANALÝZA APLIKACE KIS")
        print("="*80)

        # Analýzy
        java_analysis = self.analyze_java_versions()
        frameworks_analysis = self.analyze_frameworks()
        jsp_analysis = self.analyze_jsp_patterns()
        security_issues = self.analyze_security_issues()
        performance_issues = self.analyze_performance_issues()
        deprecated_apis = self.analyze_deprecated_apis()

        # Celková analýza
        analysis_data = {
            'java': java_analysis,
            'frameworks': frameworks_analysis,
            'jsp': jsp_analysis,
            'security': security_issues,
            'performance': performance_issues,
            'deprecated': deprecated_apis,
        }

        migration_plan = self.create_migration_plan(analysis_data)

        report = {
            'metadata': {
                'report_date': datetime.now().isoformat(),
                'application': 'KIS Banking Application',
                'analyzer_version': '1.0.0'
            },
            'executive_summary': {
                'current_state': 'Legacy Java/JSP aplikace s 1,288 stránkami a 31,138 scriptlety',
                'target_state': 'Moderní Java 17 + Spring Boot + React/Vue SPA',
                'total_issues': len(security_issues) + len(performance_issues),
                'critical_issues': len([i for i in security_issues if i.get('severity') == 'HIGH']),
                'estimated_duration': migration_plan['total_duration'],
                'estimated_cost': migration_plan['cost_estimate']['total'],
            },
            'technology_analysis': analysis_data,
            'migration_plan': migration_plan,
            'recommendations': {
                'immediate_actions': [
                    'Opravit kritické SQL injection zranitelnosti',
                    'Implementovat input validation na všech entry points',
                    'Nastavit automated testing a CI/CD',
                    'Vytvořit PoC migrace jednoho modulu'
                ],
                'short_term': [
                    'Migrace na Java 17',
                    'Refaktoring nejvíce problematických tříd (ExcelThread, UcSkupModuleImpl)',
                    'Implementace service layer',
                    'Začít migraci z JSP na REST API'
                ],
                'long_term': [
                    'Kompletní migrace na Spring Boot 3.x',
                    'Migrace na moderní frontend (React/Vue)',
                    'Mikroservices architektura pro vybrané moduly',
                    'Cloud-native deployment (Kubernetes)'
                ]
            }
        }

        return report

    def save_report(self, report: Dict, output_path: str):
        """Uložit report do souboru."""
        # JSON report
        json_path = output_path.replace('.md', '.json')
        with open(json_path, 'w', encoding='utf-8') as f:
            json.dump(report, f, indent=2, ensure_ascii=False)

        print(f"\n✅ JSON report: {json_path}")

        # Markdown report
        md_content = self._generate_markdown_report(report)
        with open(output_path, 'w', encoding='utf-8') as f:
            f.write(md_content)

        print(f"✅ Markdown report: {output_path}")

    def _generate_markdown_report(self, report: Dict) -> str:
        """Generovat Markdown formát reportu."""
        md = []

        # Header
        md.append("# Re-engineering Analýza: KIS Banking Application")
        md.append(f"\n**Datum:** {report['metadata']['report_date'][:10]}")
        md.append(f"**Verze:** {report['metadata']['analyzer_version']}\n")

        # Executive Summary
        md.append("\n## 📊 Executive Summary\n")
        summary = report['executive_summary']
        md.append(f"**Současný stav:** {summary['current_state']}")
        md.append(f"**Cílový stav:** {summary['target_state']}")
        md.append(f"**Celkový počet problémů:** {summary['total_issues']}")
        md.append(f"**Kritické problémy:** {summary['critical_issues']}")
        md.append(f"**Odhadovaná doba migrace:** {summary['estimated_duration']}")
        md.append(f"**Odhadované náklady:** {summary['estimated_cost']}\n")

        # Java Version Analysis
        md.append("\n## ☕ Analýza Java\n")
        java = report['technology_analysis']['java']
        md.append(f"**Aktuální verze:** {java['detected_version']}")
        md.append(f"**Doporučená verze:** {java['recommended_version']}")
        md.append(f"**Počet Java tříd:** {java['total_classes']}")
        md.append(f"**Počet Java metod:** {java['total_methods']}\n")

        # Frameworks
        md.append("\n## 🔧 Frameworky a knihovny\n")
        frameworks = report['technology_analysis']['frameworks']

        md.append("### Servlet API")
        md.append(f"- **Verze:** {frameworks['servlet_api']['version']}")
        md.append(f"- **Počet použití:** {frameworks['servlet_api']['count']}")
        md.append(f"- **Doporučení:** {frameworks['servlet_api']['recommendation']}\n")

        md.append("### JSP")
        md.append(f"- **Verze:** {frameworks['jsp']['version']}")
        md.append(f"- **Počet použití:** {frameworks['jsp']['count']}")
        md.append(f"- **Doporučení:** {frameworks['jsp']['recommendation']}\n")

        md.append("### JDBC")
        md.append(f"- **Počet použití:** {frameworks['jdbc']['count']}")
        md.append(f"- **Doporučení:** {frameworks['jdbc']['recommendation']}\n")

        # JSP Analysis
        md.append("\n## 📄 Analýza JSP stránek\n")
        jsp = report['technology_analysis']['jsp']
        md.append(f"**Celkem stránek:** {jsp['total_pages']}\n")
        md.append("### Identifikované problémy:\n")
        for issue, count in jsp['issues'].items():
            md.append(f"- **{issue}:** {count}")

        md.append("\n### Doporučení migrace:\n")
        for key, rec in jsp['recommendations'].items():
            md.append(f"- **{key}:** {rec}")

        # Security Issues
        md.append("\n## 🔒 Bezpečnostní problémy\n")
        security = report['technology_analysis']['security']
        md.append(f"**Celkem nalezeno:** {len(security)} bezpečnostních problémů\n")

        # Group by severity
        high_severity = [s for s in security if s.get('severity') == 'HIGH']
        medium_severity = [s for s in security if s.get('severity') == 'MEDIUM']

        md.append(f"### 🔴 Kritické problémy ({len(high_severity)})\n")
        issue_types = Counter([s['type'] for s in high_severity])
        for issue_type, count in issue_types.most_common():
            md.append(f"- **{issue_type}:** {count} výskytů")

        md.append(f"\n### 🟡 Střední problémy ({len(medium_severity)})\n")
        issue_types = Counter([s['type'] for s in medium_severity])
        for issue_type, count in issue_types.most_common():
            md.append(f"- **{issue_type}:** {count} výskytů")

        # Performance Issues
        md.append("\n## ⚡ Výkonnostní problémy\n")
        performance = report['technology_analysis']['performance']
        md.append(f"**Celkem nalezeno:** {len(performance)} výkonnostních problémů\n")

        for issue in performance[:10]:  # Top 10
            if issue['type'] == 'high_coupling':
                md.append(f"- **Vysoká vazba:** {issue['class']} ({issue['dependencies']} závislostí)")
            else:
                md.append(f"- **{issue['type']}:** {issue.get('description', '')}")

        # Deprecated APIs
        md.append("\n## ⚠️ Zastaralá API\n")
        deprecated = report['technology_analysis']['deprecated']
        for dep in deprecated:
            md.append(f"- **{dep['api']}** ({dep['count']} použití): {dep['recommendation']}")

        # Migration Plan
        md.append("\n## 🗺️ Plán migrace\n")
        plan = report['migration_plan']

        md.append(f"**Celková doba:** {plan['total_duration']}\n")

        for phase in plan['phases']:
            md.append(f"\n### Fáze {phase['phase']}: {phase['name']}")
            md.append(f"**Doba trvání:** {phase['duration']}\n")
            md.append("**Úkoly:**")
            for task in phase['tasks']:
                md.append(f"- {task}")

        # Team Requirements
        md.append("\n## 👥 Požadavky na tým\n")
        for role, count in plan['team_requirements'].items():
            md.append(f"- **{role}:** {count}")

        # Risks
        md.append("\n## ⚠️ Rizika migrace\n")
        for risk in plan['risks']:
            md.append(f"\n### {risk['risk']}")
            md.append(f"- **Pravděpodobnost:** {risk['probability']}")
            md.append(f"- **Dopad:** {risk['impact']}")
            md.append(f"- **Mitigace:** {risk['mitigation']}")

        # Cost Estimate
        md.append("\n## 💰 Odhadované náklady\n")
        costs = plan['cost_estimate']
        for category, cost in costs.items():
            md.append(f"- **{category}:** {cost}")

        # Recommendations
        md.append("\n## 🎯 Doporučení\n")
        recs = report['recommendations']

        md.append("\n### Okamžité akce")
        for action in recs['immediate_actions']:
            md.append(f"- {action}")

        md.append("\n### Krátkodobé akce (3-6 měsíců)")
        for action in recs['short_term']:
            md.append(f"- {action}")

        md.append("\n### Dlouhodobé akce (1-2 roky)")
        for action in recs['long_term']:
            md.append(f"- {action}")

        md.append("\n---\n")
        md.append("*Report vygenerován automaticky pomocí Re-engineering Analyzer*")

        return '\n'.join(md)

    def close(self):
        """Uzavřít připojení."""
        self.driver.close()


def main():
    """Hlavní funkce."""
    analyzer = ReengineeringAnalyzer()

    try:
        # Generovat report
        report = analyzer.generate_report()

        # Uložit report
        output_path = "/Users/radektuma/DEV/KIS/analýza_20251127/REENGINEERING_ANALYSIS.md"
        analyzer.save_report(report, output_path)

        print("\n" + "="*80)
        print("✅ Re-engineering analýza dokončena!")
        print("="*80)

    finally:
        analyzer.close()


if __name__ == '__main__':
    main()
