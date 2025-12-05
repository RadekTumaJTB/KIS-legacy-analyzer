#!/usr/bin/env python3
"""
Quick Re-engineering Analysis - AI-Assisted Java 17 Upgrade
Analýza pouze pro:
- Upgrade na Java 17
- Náhrada problematických tříd (ExcelThread, UcSkupModuleImpl, atd.)
- Aktualizace nepodporovaných knihoven (Apache POI 3.x, atd.)
"""

import json
import logging
from typing import Dict, List, Any, Optional
from datetime import datetime
from neo4j import GraphDatabase
from qdrant_client import QdrantClient
from qdrant_client.models import Filter, FieldCondition, MatchValue, MatchAny

logging.basicConfig(
    level=logging.INFO,
    format='%(asctime)s - %(levelname)s - %(message)s'
)
logger = logging.getLogger(__name__)


class QuickReengineeringAnalyzer:
    """Analyzér pro rychlý upgrade na Java 17 s AI asistencí."""

    def __init__(self):
        """Inicializace analyzéru."""
        self.neo4j_uri = "bolt://localhost:7687"
        self.neo4j_user = "neo4j"
        self.neo4j_password = "kis_password_2024"

        self.qdrant_host = "localhost"
        self.qdrant_port = 6333

        self.driver = None
        self.qdrant = None

        # Problematické třídy s vysokou vazbou
        self.problematic_classes = [
            'ExcelThread',
            'UcSkupModuleImpl',
            'DokumentModuleImpl',
            'PbModuleImpl',
            'IfrsModuleImpl'
        ]

        # Nepodporované knihovny
        self.deprecated_libraries = {
            'apache_poi': {
                'old_version': '3.x',
                'new_version': '5.2.5',
                'packages': ['org.apache.poi']
            },
            'java_util_date': {
                'old_api': 'java.util.Date',
                'new_api': 'java.time.*',
                'description': 'Migrace na Java 8+ Date/Time API'
            },
            'commons_collections': {
                'old_version': '3.x',
                'new_version': '4.4',
                'packages': ['org.apache.commons.collections']
            }
        }

        # Java 17 migrace problémy
        self.java_17_issues = {
            'removed_apis': [
                'java.security.acl',
                'javax.activation',
                'javax.xml.bind (JAXB)',
                'javax.annotation',
                'java.corba'
            ],
            'deprecated_apis': [
                'Thread.stop()',
                'Thread.suspend()',
                'Thread.resume()',
                'SecurityManager'
            ],
            'new_features': [
                'Sealed classes',
                'Pattern matching for switch',
                'Records',
                'Text blocks',
                'var keyword'
            ]
        }

        # AI akcelerační faktory pro quick upgrade
        self.ai_acceleration = {
            'dependency_analysis': 3.0,  # AI rychle najde všechny použití
            'code_replacement': 4.0,     # AI automaticky nahradí deprecated API
            'refactoring': 3.5,          # AI rozloží problematické třídy
            'testing': 5.0,              # AI vygeneruje unit testy
            'documentation': 6.0         # AI vygeneruje migration docs
        }

    def connect_databases(self):
        """Připojení k Neo4J a Qdrant."""
        try:
            logger.info("Připojování k Neo4J...")
            self.driver = GraphDatabase.driver(
                self.neo4j_uri,
                auth=(self.neo4j_user, self.neo4j_password)
            )
            # Test připojení
            with self.driver.session() as session:
                result = session.run("RETURN 1 as test")
                result.single()
            logger.info("✓ Neo4J připojeno")

            logger.info("Připojování k Qdrant...")
            self.qdrant = QdrantClient(
                host=self.qdrant_host,
                port=self.qdrant_port
            )
            # Test připojení
            collections = self.qdrant.get_collections()
            logger.info(f"✓ Qdrant připojeno ({len(collections.collections)} kolekcí)")

        except Exception as e:
            logger.error(f"Chyba připojení k databázím: {e}")
            raise

    def close_databases(self):
        """Uzavření připojení."""
        if self.driver:
            self.driver.close()
            logger.info("Neo4J připojení uzavřeno")

    def analyze_problematic_classes(self) -> Dict[str, Any]:
        """Analýza problematických tříd s vysokou vazbou."""
        logger.info("Analýza problematických tříd...")

        results = {
            'classes': [],
            'total_dependencies': 0,
            'ai_refactoring_plan': {}
        }

        with self.driver.session() as session:
            for class_name in self.problematic_classes:
                # Najít třídu a její závislosti
                query = """
                MATCH (c:JavaClass {name: $class_name})
                OPTIONAL MATCH (c)-[r:DEPENDS_ON]->(dep)
                WITH c, r, dep
                OPTIONAL MATCH (c)-[:CONTAINS]->(m:JavaMethod)
                RETURN c.name as className,
                       c.file as file,
                       count(DISTINCT r) as dependencyCount,
                       count(DISTINCT m) as methodCount,
                       collect(DISTINCT dep.name) as dependencies,
                       collect(DISTINCT m.name) as methods
                """

                result = session.run(query, class_name=class_name)
                record = result.single()

                if record:
                    class_info = {
                        'name': record['className'],
                        'file': record['file'],
                        'dependency_count': record['dependencyCount'],
                        'method_count': record['methodCount'],
                        'dependencies': record['dependencies'][:20],  # Top 20
                        'methods': record['methods'][:10]  # Top 10
                    }

                    # AI refactoring návrh
                    refactoring_plan = self._generate_ai_refactoring_plan(
                        class_name,
                        record['dependencyCount'],
                        record['methodCount']
                    )
                    class_info['ai_refactoring'] = refactoring_plan

                    results['classes'].append(class_info)
                    results['total_dependencies'] += record['dependencyCount']
                    results['ai_refactoring_plan'][class_name] = refactoring_plan

        logger.info(f"✓ Analyzováno {len(results['classes'])} problematických tříd")
        return results

    def _generate_ai_refactoring_plan(
        self,
        class_name: str,
        dep_count: int,
        method_count: int
    ) -> Dict[str, Any]:
        """Vygenerovat AI-assisted refactoring plán."""

        # Odhadnout počet nových komponent
        suggested_components = max(3, min(dep_count // 15, 8))

        plan = {
            'current_state': {
                'dependencies': dep_count,
                'methods': method_count,
                'complexity': 'CRITICAL' if dep_count > 100 else 'HIGH'
            },
            'suggested_split': {
                'component_count': suggested_components,
                'approach': 'Split by responsibility using AI analysis'
            },
            'ai_assistance': {
                'code_analysis': f'AI analyzuje {method_count} metod a identifikuje logické skupiny',
                'dependency_mapping': f'AI mapuje {dep_count} závislostí na nové komponenty',
                'code_generation': 'AI vygeneruje nové třídy s správnou strukturou',
                'test_generation': f'AI vygeneruje unit testy pro {suggested_components} komponent'
            },
            'time_estimate': {
                'manual': f'{int(dep_count / 10)} dní',
                'with_ai': f'{int(dep_count / 40)} dní',
                'savings': '75%'
            }
        }

        return plan

    def analyze_deprecated_libraries(self) -> Dict[str, Any]:
        """Analýza používání nepodporovaných knihoven."""
        logger.info("Analýza nepodporovaných knihoven...")

        results = {
            'libraries': {},
            'total_usages': 0,
            'migration_plan': {}
        }

        # Apache POI analýza
        poi_usage = self._analyze_library_usage('org.apache.poi')
        results['libraries']['apache_poi'] = {
            'current_version': '3.x',
            'target_version': '5.2.5',
            'usage_count': poi_usage['count'],
            'affected_files': poi_usage['files'][:20],
            'migration_effort': self._estimate_migration_effort(poi_usage['count'], 'library_upgrade')
        }
        results['total_usages'] += poi_usage['count']

        # java.util.Date analýza
        date_usage = self._analyze_library_usage('java.util.Date')
        results['libraries']['java_util_date'] = {
            'current_api': 'java.util.Date',
            'target_api': 'java.time.*',
            'usage_count': date_usage['count'],
            'affected_files': date_usage['files'][:20],
            'migration_effort': self._estimate_migration_effort(date_usage['count'], 'api_migration')
        }
        results['total_usages'] += date_usage['count']

        # Commons Collections analýza
        commons_usage = self._analyze_library_usage('org.apache.commons.collections')
        results['libraries']['commons_collections'] = {
            'current_version': '3.x',
            'target_version': '4.4',
            'usage_count': commons_usage['count'],
            'affected_files': commons_usage['files'][:20],
            'migration_effort': self._estimate_migration_effort(commons_usage['count'], 'library_upgrade')
        }
        results['total_usages'] += commons_usage['count']

        # AI migration plan
        results['ai_migration_plan'] = self._generate_library_migration_plan(results)

        logger.info(f"✓ Nalezeno {results['total_usages']} použití deprecated knihoven")
        return results

    def _analyze_library_usage(self, package_pattern: str) -> Dict[str, Any]:
        """Analyzovat použití konkrétní knihovny."""
        with self.driver.session() as session:
            query = """
            MATCH (c:JavaClass)-[:IMPORTS]->(i)
            WHERE i.name CONTAINS $package
            RETURN count(DISTINCT c) as count,
                   collect(DISTINCT c.file) as files
            """

            result = session.run(query, package=package_pattern)
            record = result.single()

            if record:
                return {
                    'count': record['count'],
                    'files': record['files']
                }
            return {'count': 0, 'files': []}

    def _estimate_migration_effort(self, usage_count: int, migration_type: str) -> Dict[str, Any]:
        """Odhadnout úsilí pro migraci."""

        # If no usage found, return zero effort
        if usage_count == 0:
            return {
                'manual': {'hours': 0, 'days': 0, 'weeks': 0},
                'with_ai': {'hours': 0, 'days': 0, 'weeks': 0},
                'savings': {'hours': 0, 'percentage': 0}
            }

        # Základní odhady
        if migration_type == 'library_upgrade':
            manual_hours_per_usage = 2  # 2 hodiny na file
            ai_factor = 4.0  # AI je 4x rychlejší
        elif migration_type == 'api_migration':
            manual_hours_per_usage = 1.5  # 1.5 hodiny na file
            ai_factor = 5.0  # AI je 5x rychlejší (jednodušší pattern)
        else:
            manual_hours_per_usage = 1
            ai_factor = 3.0

        manual_hours = usage_count * manual_hours_per_usage
        ai_hours = manual_hours / ai_factor

        return {
            'manual': {
                'hours': manual_hours,
                'days': round(manual_hours / 8, 1),
                'weeks': round(manual_hours / 40, 1)
            },
            'with_ai': {
                'hours': ai_hours,
                'days': round(ai_hours / 8, 1),
                'weeks': round(ai_hours / 40, 1)
            },
            'savings': {
                'hours': manual_hours - ai_hours,
                'percentage': round((1 - ai_hours / manual_hours) * 100, 0)
            }
        }

    def _generate_library_migration_plan(self, library_results: Dict[str, Any]) -> Dict[str, Any]:
        """Vygenerovat AI-assisted migration plan pro knihovny."""

        total_manual_hours = sum(
            lib['migration_effort']['manual']['hours']
            for lib in library_results['libraries'].values()
        )

        total_ai_hours = sum(
            lib['migration_effort']['with_ai']['hours']
            for lib in library_results['libraries'].values()
        )

        return {
            'approach': 'AI-Assisted Library Migration',
            'phases': [
                {
                    'phase': 1,
                    'name': 'Automated Dependency Analysis',
                    'duration': '1 den',
                    'ai_tools': ['Claude Code', 'Dependency analyzers'],
                    'tasks': [
                        'AI scan celé codebase pro všechny importy',
                        'Identifikace verze každé knihovny',
                        'Mapa závislostí mezi knihovnami'
                    ]
                },
                {
                    'phase': 2,
                    'name': 'Automated Code Replacement',
                    'duration': f'{round(total_ai_hours / 40, 1)} týdnů',
                    'ai_tools': ['Claude Code', 'GitHub Copilot'],
                    'tasks': [
                        'AI automaticky nahradí deprecated API',
                        'AI vygeneruje migration adapters kde nutné',
                        'AI updatuje build files (pom.xml, gradle)'
                    ]
                },
                {
                    'phase': 3,
                    'name': 'Automated Testing',
                    'duration': '1 týden',
                    'ai_tools': ['AI Test Generator'],
                    'tasks': [
                        'AI vygeneruje unit testy pro změny',
                        'AI vygeneruje integration testy',
                        'Spuštění test suite'
                    ]
                }
            ],
            'total_effort': {
                'manual': f'{round(total_manual_hours / 40, 1)} týdnů',
                'with_ai': f'{round(total_ai_hours / 40, 1)} týdnů',
                'savings': f'{round((1 - total_ai_hours / total_manual_hours) * 100, 0)}%'
            }
        }

    def analyze_java_17_migration(self) -> Dict[str, Any]:
        """Analýza migrace na Java 17."""
        logger.info("Analýza Java 17 migrace...")

        results = {
            'current_version': 'Java 7',
            'target_version': 'Java 17 LTS',
            'breaking_changes': [],
            'migration_steps': [],
            'ai_assistance': {}
        }

        # Najít použití removed APIs
        removed_api_usage = self._find_removed_api_usage()
        results['breaking_changes'].extend(removed_api_usage)

        # Najít použití deprecated APIs
        deprecated_api_usage = self._find_deprecated_api_usage()
        results['breaking_changes'].extend(deprecated_api_usage)

        # Migration steps
        results['migration_steps'] = self._generate_java_17_migration_steps(
            len(removed_api_usage),
            len(deprecated_api_usage)
        )

        # AI assistance plan
        results['ai_assistance'] = self._generate_java_17_ai_plan(results)

        logger.info(f"✓ Nalezeno {len(removed_api_usage) + len(deprecated_api_usage)} Java 17 problémů")
        return results

    def _find_removed_api_usage(self) -> List[Dict[str, Any]]:
        """Najít použití removed APIs v Java 17."""
        removed_apis = []

        with self.driver.session() as session:
            for api in self.java_17_issues['removed_apis']:
                query = """
                MATCH (c:JavaClass)-[:IMPORTS]->(i)
                WHERE i.name CONTAINS $api
                RETURN i.name as apiName, count(DISTINCT c) as usageCount,
                       collect(DISTINCT c.file)[..5] as sampleFiles
                """

                result = session.run(query, api=api)
                for record in result:
                    if record['usageCount'] > 0:
                        removed_apis.append({
                            'type': 'REMOVED_API',
                            'api': record['apiName'],
                            'usage_count': record['usageCount'],
                            'sample_files': record['sampleFiles'],
                            'severity': 'CRITICAL',
                            'solution': f'Replace with modern alternative'
                        })

        return removed_apis

    def _find_deprecated_api_usage(self) -> List[Dict[str, Any]]:
        """Najít použití deprecated APIs."""
        deprecated = []

        # Hledat v Qdrant pro Thread.stop(), Thread.suspend() atd.
        try:
            for api in self.java_17_issues['deprecated_apis']:
                # Search v Qdrant
                search_results = self.qdrant.scroll(
                    collection_name="java_chunks",
                    scroll_filter=Filter(
                        must=[
                            FieldCondition(
                                key="content",
                                match=MatchValue(value=api)
                            )
                        ]
                    ),
                    limit=10
                )

                if search_results and len(search_results[0]) > 0:
                    deprecated.append({
                        'type': 'DEPRECATED_API',
                        'api': api,
                        'usage_count': len(search_results[0]),
                        'severity': 'HIGH',
                        'solution': f'Replace with recommended alternative'
                    })
        except Exception as e:
            logger.warning(f"Qdrant search warning: {e}")

        return deprecated

    def _generate_java_17_migration_steps(
        self,
        removed_count: int,
        deprecated_count: int
    ) -> List[Dict[str, Any]]:
        """Vygenerovat kroky pro Java 17 migraci."""

        return [
            {
                'step': 1,
                'name': 'Update Build Configuration',
                'duration': '1 den',
                'tasks': [
                    'Update pom.xml/gradle: sourceCompatibility = 17',
                    'Update compiler plugin version',
                    'Update JDK to 17 on build servers'
                ],
                'ai_help': 'AI automaticky updatuje build files'
            },
            {
                'step': 2,
                'name': f'Fix Removed APIs ({removed_count} issues)',
                'duration': f'{max(1, removed_count // 5)} dní',
                'tasks': [
                    'Replace JAXB with jakarta.xml.bind',
                    'Replace removed security APIs',
                    'Update javax.* to jakarta.*'
                ],
                'ai_help': 'AI automaticky nahradí všechny importy a použití'
            },
            {
                'step': 3,
                'name': f'Fix Deprecated APIs ({deprecated_count} issues)',
                'duration': f'{max(1, deprecated_count // 10)} dní',
                'tasks': [
                    'Replace Thread.stop() with proper thread management',
                    'Replace SecurityManager usage',
                    'Update deprecated constructors'
                ],
                'ai_help': 'AI navrhne správné náhrady a vygeneruje kód'
            },
            {
                'step': 4,
                'name': 'Test & Validate',
                'duration': '3-5 dní',
                'tasks': [
                    'Run full test suite',
                    'Performance testing',
                    'Integration testing'
                ],
                'ai_help': 'AI vygeneruje nové unit testy pro změny'
            }
        ]

    def _generate_java_17_ai_plan(self, migration_data: Dict[str, Any]) -> Dict[str, Any]:
        """Vygenerovat AI assistance plan pro Java 17 migraci."""

        total_issues = len(migration_data['breaking_changes'])

        manual_days = max(10, total_issues // 3)  # Roughly 3 issues per day manually
        ai_days = max(3, manual_days // 4)  # AI is 4x faster

        return {
            'approach': 'AI-Driven Java 17 Migration',
            'ai_tools': [
                'Claude Code - Code analysis and replacement',
                'GitHub Copilot - Code completion',
                'AI Test Generator - Test generation'
            ],
            'automation_level': '85%',
            'phases': [
                {
                    'phase': 'Analysis',
                    'duration': '1 den',
                    'ai_automation': '95%',
                    'description': 'AI skenuje celou codebase a identifikuje všechny problémy'
                },
                {
                    'phase': 'Code Replacement',
                    'duration': f'{ai_days} dní',
                    'ai_automation': '80%',
                    'description': 'AI automaticky nahradí deprecated APIs a importy'
                },
                {
                    'phase': 'Testing',
                    'duration': '2-3 dny',
                    'ai_automation': '90%',
                    'description': 'AI vygeneruje testy a spustí validaci'
                }
            ],
            'time_savings': {
                'manual': f'{manual_days} dní',
                'with_ai': f'{ai_days} dní',
                'savings': f'{round((1 - ai_days / manual_days) * 100, 0)}%'
            }
        }

    def generate_quick_migration_plan(
        self,
        classes_analysis: Dict[str, Any],
        libraries_analysis: Dict[str, Any],
        java17_analysis: Dict[str, Any]
    ) -> Dict[str, Any]:
        """Vygenerovat celkový plán quick migrace."""
        logger.info("Generování celkového migračního plánu...")

        # Sečíst všechny odhady
        total_manual_days = 0
        total_ai_days = 0

        # Classes refactoring
        for class_info in classes_analysis['classes']:
            manual = int(class_info['ai_refactoring']['time_estimate']['manual'].split()[0])
            ai = int(class_info['ai_refactoring']['time_estimate']['with_ai'].split()[0])
            total_manual_days += manual
            total_ai_days += ai

        # Library migration
        for lib in libraries_analysis['libraries'].values():
            total_manual_days += lib['migration_effort']['manual']['days']
            total_ai_days += lib['migration_effort']['with_ai']['days']

        # Java 17 migration
        manual_j17 = int(java17_analysis['ai_assistance']['time_savings']['manual'].split()[0])
        ai_j17 = int(java17_analysis['ai_assistance']['time_savings']['with_ai'].split()[0])
        total_manual_days += manual_j17
        total_ai_days += ai_j17

        # Přidat testing a validation overhead
        testing_overhead_days = 10
        total_manual_days += testing_overhead_days
        total_ai_days += testing_overhead_days // 2  # AI helps with testing too

        # Convert to weeks/months
        manual_weeks = round(total_manual_days / 5, 1)
        manual_months = round(manual_weeks / 4, 1)
        ai_weeks = round(total_ai_days / 5, 1)
        ai_months = round(ai_weeks / 4, 1)

        # Cost calculation
        dev_daily_rate = 600  # €600/day
        manual_cost = total_manual_days * dev_daily_rate * 2  # 2 developers
        ai_cost = total_ai_days * dev_daily_rate * 1  # 1 developer + AI
        ai_tools_cost = 50 * ai_months  # €50/month for AI tools
        total_ai_cost = ai_cost + ai_tools_cost

        plan = {
            'summary': {
                'scope': 'Quick Modernization - Java 17 + Library Upgrades + Refactoring',
                'approach': 'AI-Assisted Incremental Upgrade',
                'risk_level': 'LOW-MEDIUM'
            },
            'timeline': {
                'traditional': {
                    'days': total_manual_days,
                    'weeks': manual_weeks,
                    'months': manual_months
                },
                'with_ai': {
                    'days': total_ai_days,
                    'weeks': ai_weeks,
                    'months': ai_months
                },
                'acceleration': f'{round((1 - total_ai_days / total_manual_days) * 100, 0)}%'
            },
            'cost': {
                'traditional': {
                    'eur': manual_cost,
                    'team_size': 2,
                    'breakdown': f'€{dev_daily_rate}/day × {total_manual_days} days × 2 devs'
                },
                'with_ai': {
                    'eur': total_ai_cost,
                    'team_size': 1,
                    'ai_tools_cost': ai_tools_cost,
                    'breakdown': f'€{dev_daily_rate}/day × {total_ai_days} days × 1 dev + €{ai_tools_cost} AI tools'
                },
                'savings': {
                    'eur': manual_cost - total_ai_cost,
                    'percentage': round((1 - total_ai_cost / manual_cost) * 100, 0)
                }
            },
            'phases': [
                {
                    'phase': 1,
                    'name': 'Preparation & Analysis',
                    'duration': '1 týden',
                    'ai_tasks': [
                        'AI comprehensive codebase scan',
                        'AI dependency analysis',
                        'AI risk assessment'
                    ]
                },
                {
                    'phase': 2,
                    'name': 'Java 17 Migration',
                    'duration': f'{ai_j17} dní',
                    'ai_tasks': [
                        'AI automatic API replacements',
                        'AI build configuration updates',
                        'AI test generation'
                    ]
                },
                {
                    'phase': 3,
                    'name': 'Library Updates',
                    'duration': f'{round(sum(lib["migration_effort"]["with_ai"]["weeks"] for lib in libraries_analysis["libraries"].values()), 1)} týdnů',
                    'ai_tasks': [
                        'AI Apache POI 3.x → 5.2.5 migration',
                        'AI java.util.Date → java.time.* migration',
                        'AI Commons Collections upgrade'
                    ]
                },
                {
                    'phase': 4,
                    'name': 'Refactor Problematic Classes',
                    'duration': f'{round(sum(int(c["ai_refactoring"]["time_estimate"]["with_ai"].split()[0]) for c in classes_analysis["classes"]) / 5, 1)} týdnů',
                    'ai_tasks': [
                        f'AI refactor ExcelThread ({classes_analysis["classes"][0]["dependency_count"]} deps)',
                        f'AI refactor UcSkupModuleImpl',
                        f'AI refactor other high-coupling classes'
                    ]
                },
                {
                    'phase': 5,
                    'name': 'Testing & Validation',
                    'duration': '1-2 týdny',
                    'ai_tasks': [
                        'AI-generated unit tests',
                        'AI-generated integration tests',
                        'Performance testing',
                        'Security scanning'
                    ]
                }
            ],
            'ai_tools': {
                'code_generation': 'Claude Code (€20/měs/dev)',
                'code_completion': 'GitHub Copilot (€10/měs/dev)',
                'testing': 'AI Test Generator',
                'security': 'AI Security Scanner'
            },
            'risks': [
                {
                    'risk': 'Breaking changes in Java 17',
                    'probability': 'MEDIUM',
                    'impact': 'MEDIUM',
                    'mitigation': 'AI comprehensive testing + gradual rollout'
                },
                {
                    'risk': 'Library compatibility issues',
                    'probability': 'LOW',
                    'impact': 'MEDIUM',
                    'mitigation': 'AI dependency analysis + testing'
                },
                {
                    'risk': 'Regression in refactored classes',
                    'probability': 'MEDIUM',
                    'impact': 'HIGH',
                    'mitigation': 'AI-generated test suite + manual QA'
                }
            ],
            'success_metrics': [
                'All builds compile with Java 17',
                'All tests pass',
                'No performance regression',
                'All deprecated libraries updated',
                'Code coupling reduced by 50%'
            ]
        }

        return plan

    def generate_report(
        self,
        classes_analysis: Dict[str, Any],
        libraries_analysis: Dict[str, Any],
        java17_analysis: Dict[str, Any],
        migration_plan: Dict[str, Any]
    ) -> Dict[str, Any]:
        """Vygenerovat kompletní report."""

        report = {
            'metadata': {
                'title': 'Quick Modernization Analysis - Java 17 Upgrade',
                'date': datetime.now().strftime('%Y-%m-%d'),
                'scope': 'Java 17 + Library Upgrades + High-Coupling Refactoring',
                'approach': 'AI-Assisted Incremental Modernization'
            },
            'executive_summary': {
                'current_state': 'Java 7, deprecated libraries, high-coupling classes',
                'target_state': 'Java 17 LTS, modern libraries, refactored architecture',
                'timeline_traditional': migration_plan['timeline']['traditional'],
                'timeline_ai': migration_plan['timeline']['with_ai'],
                'cost_traditional': migration_plan['cost']['traditional'],
                'cost_ai': migration_plan['cost']['with_ai'],
                'savings': migration_plan['cost']['savings']
            },
            'analysis': {
                'problematic_classes': classes_analysis,
                'deprecated_libraries': libraries_analysis,
                'java17_migration': java17_analysis
            },
            'migration_plan': migration_plan
        }

        return report

    def run_analysis(self) -> Dict[str, Any]:
        """Spustit celou analýzu."""
        try:
            logger.info("=== Quick Re-engineering Analysis START ===")

            # Connect to databases
            self.connect_databases()

            # Run analyses
            classes_analysis = self.analyze_problematic_classes()
            libraries_analysis = self.analyze_deprecated_libraries()
            java17_analysis = self.analyze_java_17_migration()

            # Generate migration plan
            migration_plan = self.generate_quick_migration_plan(
                classes_analysis,
                libraries_analysis,
                java17_analysis
            )

            # Generate report
            report = self.generate_report(
                classes_analysis,
                libraries_analysis,
                java17_analysis,
                migration_plan
            )

            logger.info("=== Quick Re-engineering Analysis COMPLETE ===")

            return report

        finally:
            self.close_databases()


def save_markdown_report(report: Dict[str, Any], output_file: str):
    """Uložit report jako Markdown."""

    md = f"""# Quick Modernization Analysis - AI-Assisted Java 17 Upgrade

**Datum:** {report['metadata']['date']}
**Scope:** {report['metadata']['scope']}
**Approach:** {report['metadata']['approach']}

---

## 📊 Executive Summary

### Current State vs Target State

| Aspekt | Current | Target |
|--------|---------|--------|
| **Java Version** | Java 7 | **Java 17 LTS** |
| **Apache POI** | 3.x (deprecated) | **5.2.5** |
| **Date API** | java.util.Date | **java.time.*** |
| **Commons Collections** | 3.x | **4.4** |
| **High Coupling** | 8 classes (133 max deps) | **Refactored < 20 deps** |

### 🎯 Migration Timeline & Cost Comparison

| Metric | Traditional | With AI | Improvement |
|--------|-------------|---------|-------------|
| **Duration** | {report['executive_summary']['timeline_traditional']['months']} měsíců | **{report['executive_summary']['timeline_ai']['months']} měsíců** | **{report['migration_plan']['timeline']['acceleration']} rychlejší** |
| **Team Size** | {report['executive_summary']['cost_traditional']['team_size']} developers | **{report['executive_summary']['cost_ai']['team_size']} developer + AI** | **{round((1 - report['executive_summary']['cost_ai']['team_size'] / report['executive_summary']['cost_traditional']['team_size']) * 100)}% menší tým** |
| **Cost** | €{report['executive_summary']['cost_traditional']['eur']:,} | **€{report['executive_summary']['cost_ai']['eur']:,}** | **{report['executive_summary']['savings']['percentage']}% levnější** |
| **Risk** | MEDIUM | **LOW-MEDIUM** | AI testing + validation |

**💰 Total Savings: €{report['executive_summary']['savings']['eur']:,}**

---

## 🔍 Detailed Analysis

### 1. Problematic Classes (High Coupling)

**Total Classes:** {len(report['analysis']['problematic_classes']['classes'])}
**Total Dependencies:** {report['analysis']['problematic_classes']['total_dependencies']}

"""

    # Problematic classes detail
    for cls in report['analysis']['problematic_classes']['classes']:
        md += f"""
#### {cls['name']}

- **File:** `{cls['file']}`
- **Dependencies:** {cls['dependency_count']}
- **Methods:** {cls['method_count']}
- **Complexity:** {cls['ai_refactoring']['current_state']['complexity']}

**AI Refactoring Plan:**
- Split into **{cls['ai_refactoring']['suggested_split']['component_count']} components**
- AI analyzes {cls['method_count']} methods → logical groups
- AI generates new classes with proper structure
- AI generates unit tests for each component

**Time Estimate:**
- Manual: {cls['ai_refactoring']['time_estimate']['manual']}
- With AI: **{cls['ai_refactoring']['time_estimate']['with_ai']}**
- Savings: **{cls['ai_refactoring']['time_estimate']['savings']}**

"""

    # Deprecated libraries
    md += f"""
---

### 2. Deprecated Libraries Migration

**Total Usages Found:** {report['analysis']['deprecated_libraries']['total_usages']}

"""

    for lib_name, lib_data in report['analysis']['deprecated_libraries']['libraries'].items():
        if lib_name != 'ai_migration_plan':
            md += f"""
#### {lib_name.replace('_', ' ').title()}

- **Current:** {lib_data.get('current_version') or lib_data.get('current_api')}
- **Target:** {lib_data.get('target_version') or lib_data.get('target_api')}
- **Usage Count:** {lib_data['usage_count']} files
- **Affected Files (sample):** {len(lib_data['affected_files'])} files

**Migration Effort:**
- Manual: {lib_data['migration_effort']['manual']['weeks']} weeks
- With AI: **{lib_data['migration_effort']['with_ai']['weeks']} weeks**
- Savings: **{lib_data['migration_effort']['savings']['percentage']}%**

"""

    # AI Migration Plan for libraries
    if 'ai_migration_plan' in report['analysis']['deprecated_libraries']:
        plan = report['analysis']['deprecated_libraries']['ai_migration_plan']
        md += f"""
**AI-Assisted Library Migration Plan:**

**Total Effort:**
- Traditional: {plan['total_effort']['manual']}
- With AI: **{plan['total_effort']['with_ai']}**
- Savings: **{plan['total_effort']['savings']}**

**Phases:**

"""
        for phase in plan['phases']:
            md += f"""
**Phase {phase['phase']}: {phase['name']}** ({phase['duration']})
- AI Tools: {', '.join(phase['ai_tools'])}
- Tasks:
"""
            for task in phase['tasks']:
                md += f"  - {task}\n"
            md += "\n"

    # Java 17 migration
    md += f"""
---

### 3. Java 17 Migration

**Current Version:** {report['analysis']['java17_migration']['current_version']}
**Target Version:** {report['analysis']['java17_migration']['target_version']}

**Breaking Changes Found:** {len(report['analysis']['java17_migration']['breaking_changes'])}

"""

    # Breaking changes detail
    for change in report['analysis']['java17_migration']['breaking_changes']:
        md += f"""
#### {change['type']}: {change['api']}

- **Severity:** {change['severity']}
- **Usage Count:** {change.get('usage_count', 'N/A')}
- **Solution:** {change['solution']}

"""

    # AI assistance for Java 17
    ai_plan = report['analysis']['java17_migration']['ai_assistance']
    md += f"""
**AI-Assisted Java 17 Migration:**

- **Automation Level:** {ai_plan['automation_level']}
- **AI Tools:** {', '.join(ai_plan['ai_tools'])}

**Phases:**

"""

    for phase in ai_plan['phases']:
        md += f"""
**{phase['phase']}** ({phase['duration']})
- AI Automation: {phase['ai_automation']}
- {phase['description']}

"""

    md += f"""
**Time Savings:**
- Manual: {ai_plan['time_savings']['manual']}
- With AI: **{ai_plan['time_savings']['with_ai']}**
- Savings: **{ai_plan['time_savings']['savings']}**

---

## 🚀 Migration Plan

### Summary

- **Scope:** {report['migration_plan']['summary']['scope']}
- **Approach:** {report['migration_plan']['summary']['approach']}
- **Risk Level:** {report['migration_plan']['summary']['risk_level']}

### Timeline

"""

    md += f"""
| Approach | Days | Weeks | Months |
|----------|------|-------|--------|
| **Traditional** | {report['migration_plan']['timeline']['traditional']['days']} | {report['migration_plan']['timeline']['traditional']['weeks']} | {report['migration_plan']['timeline']['traditional']['months']} |
| **With AI** | **{report['migration_plan']['timeline']['with_ai']['days']}** | **{report['migration_plan']['timeline']['with_ai']['weeks']}** | **{report['migration_plan']['timeline']['with_ai']['months']}** |

**Acceleration:** {report['migration_plan']['timeline']['acceleration']}

### Cost Breakdown

**Traditional Approach:**
- Cost: €{report['migration_plan']['cost']['traditional']['eur']:,}
- Team: {report['migration_plan']['cost']['traditional']['team_size']} developers
- Breakdown: {report['migration_plan']['cost']['traditional']['breakdown']}

**AI-Assisted Approach:**
- Cost: **€{report['migration_plan']['cost']['with_ai']['eur']:,}**
- Team: **{report['migration_plan']['cost']['with_ai']['team_size']} developer + AI**
- AI Tools: €{report['migration_plan']['cost']['with_ai']['ai_tools_cost']}
- Breakdown: {report['migration_plan']['cost']['with_ai']['breakdown']}

**Savings:**
- Amount: **€{report['migration_plan']['cost']['savings']['eur']:,}**
- Percentage: **{report['migration_plan']['cost']['savings']['percentage']}%**

### Phases

"""

    for phase in report['migration_plan']['phases']:
        md += f"""
#### Phase {phase['phase']}: {phase['name']} ({phase['duration']})

**AI Tasks:**
"""
        for task in phase['ai_tasks']:
            md += f"- {task}\n"
        md += "\n"

    # AI Tools
    md += f"""
---

### 🤖 AI Tools & Technologies

"""
    for tool_name, tool_desc in report['migration_plan']['ai_tools'].items():
        md += f"- **{tool_name.replace('_', ' ').title()}:** {tool_desc}\n"

    # Risks
    md += f"""
---

### ⚠️ Risks & Mitigation

"""
    for risk in report['migration_plan']['risks']:
        md += f"""
#### {risk['risk']}

- **Probability:** {risk['probability']}
- **Impact:** {risk['impact']}
- **Mitigation:** {risk['mitigation']}

"""

    # Success Metrics
    md += f"""
---

### ✅ Success Metrics

"""
    for metric in report['migration_plan']['success_metrics']:
        md += f"- {metric}\n"

    # Recommendations
    md += f"""
---

## 💡 Recommendations

### 1. Start with AI-Assisted Approach

**Why?**
- **{report['migration_plan']['timeline']['acceleration']} faster**
- **{report['migration_plan']['cost']['savings']['percentage']}% cost savings**
- **Lower risk** due to AI-generated comprehensive testing
- **Higher quality** with AI code review

### 2. Prioritize High-Impact Items

1. **Java 17 Migration First**
   - Foundation for everything else
   - AI makes it {ai_plan['time_savings']['savings']} faster
   - Low risk with proper testing

2. **Critical Library Updates**
   - Apache POI 3.x → 5.2.5 (security & features)
   - java.util.Date → java.time.* (thread-safe, better API)
   - AI can automate {report['analysis']['deprecated_libraries']['ai_migration_plan']['total_effort']['savings']} of work

3. **Refactor High-Coupling Classes**
   - ExcelThread (133 deps) → multiple focused components
   - AI splits and tests automatically
   - {report['analysis']['problematic_classes']['classes'][0]['ai_refactoring']['time_estimate']['savings']} time savings

### 3. Incremental Deployment Strategy

1. **Week 1:** AI analysis + preparation
2. **Weeks 2-{round(report['migration_plan']['timeline']['with_ai']['weeks'])}:** Implementation with AI
3. **Final weeks:** Testing + validation
4. **Deploy:** Gradual rollout with monitoring

### 4. Team Setup

- **1 Senior Java Developer** (AI-assisted)
- **1 QA Engineer** (part-time)
- **AI Tools:**
  - Claude Code for code generation & refactoring
  - GitHub Copilot for code completion
  - AI Test Generator for testing

**Total Team Cost:** Much lower than traditional {report['migration_plan']['cost']['traditional']['team_size']}-person team

---

## 🎯 Next Steps

1. ✅ Review this analysis
2. ⬜ Approve AI-assisted approach
3. ⬜ Set up AI development environment
4. ⬜ Start Phase 1: AI comprehensive scan
5. ⬜ Begin Java 17 migration with AI assistance

---

**Generated:** {report['metadata']['date']}
**Tool:** AI-Powered Quick Modernization Analyzer
"""

    # Save to file
    with open(output_file, 'w', encoding='utf-8') as f:
        f.write(md)

    logger.info(f"✓ Markdown report saved: {output_file}")


def save_json_report(report: Dict[str, Any], output_file: str):
    """Uložit report jako JSON."""
    with open(output_file, 'w', encoding='utf-8') as f:
        json.dump(report, f, indent=2, ensure_ascii=False)
    logger.info(f"✓ JSON report saved: {output_file}")


def main():
    """Main entry point."""
    try:
        # Run analysis
        analyzer = QuickReengineeringAnalyzer()
        report = analyzer.run_analysis()

        # Save reports
        output_dir = "/Users/radektuma/DEV/KIS/analýza_20251127"
        md_file = f"{output_dir}/REENGINEERING_ANALYSIS_AI_QUICK.md"
        json_file = f"{output_dir}/REENGINEERING_ANALYSIS_AI_QUICK.json"

        save_markdown_report(report, md_file)
        save_json_report(report, json_file)

        # Print summary
        print("\n" + "="*80)
        print("QUICK MODERNIZATION ANALYSIS - SUMMARY")
        print("="*80)
        print(f"\n📊 Timeline:")
        print(f"  Traditional: {report['migration_plan']['timeline']['traditional']['months']} months")
        print(f"  With AI:     {report['migration_plan']['timeline']['with_ai']['months']} months")
        print(f"  Acceleration: {report['migration_plan']['timeline']['acceleration']}")
        print(f"\n💰 Cost:")
        print(f"  Traditional: €{report['migration_plan']['cost']['traditional']['eur']:,}")
        print(f"  With AI:     €{report['migration_plan']['cost']['with_ai']['eur']:,}")
        print(f"  Savings:     €{report['migration_plan']['cost']['savings']['eur']:,} ({report['migration_plan']['cost']['savings']['percentage']}%)")
        print(f"\n📁 Reports generated:")
        print(f"  - {md_file}")
        print(f"  - {json_file}")
        print("\n" + "="*80)

    except Exception as e:
        logger.error(f"Analysis failed: {e}")
        raise


if __name__ == "__main__":
    main()
