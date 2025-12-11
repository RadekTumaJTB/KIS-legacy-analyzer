#!/usr/bin/env python3
"""
Oracle Database Deployment Script
----------------------------------
Automatically deploys database objects from SQL scripts with dependency resolution.

Features:
- Automatic object type detection and ordering
- Dependency resolution with retry mechanism
- Drop all existing objects before deployment
- Detailed logging and error reporting
- Support for both DB_JT and DB_DSA schemas

Usage:
    python deploy_database.py --host localhost --port 1521 --service FREEPDB1 --user db_jt --password your_password

    # Or with connection string:
    python deploy_database.py --connection "db_jt/password@localhost:1521/FREEPDB1"

    # Deploy specific schema only:
    python deploy_database.py --connection "..." --schema DB_JT

    # Dry run (show what would be deployed):
    python deploy_database.py --connection "..." --dry-run
"""

import os
import sys
import glob
import argparse
import logging
from pathlib import Path
from typing import List, Dict, Tuple, Set
from datetime import datetime
from collections import defaultdict
import re

try:
    import oracledb
except ImportError:
    try:
        import cx_Oracle as oracledb
    except ImportError:
        print("ERROR: Neither oracledb nor cx_Oracle is installed.")
        print("Install with: pip install oracledb")
        sys.exit(1)

# ============================================================================
# Configuration
# ============================================================================

# Deployment order (order matters!)
DEPLOYMENT_ORDER = [
    'TYPES',
    'SEQUENCES',
    'TABLES',
    'VIEWS',
    'MATERIALIZED_VIEWS',
    'FUNCTIONS',
    'PROCEDURES',
    'PACKAGES',
    'PACKAGE_BODIES',
    'TRIGGERS',
    'SYNONYMS',
    'DATABASE_LINKS',
    'CONSTRAINTS',
    'REF_CONSTRAINTS',
    'INDEXES'
]

# Drop order (reverse of deployment)
DROP_ORDER = list(reversed(DEPLOYMENT_ORDER))

# SQL patterns for extracting object names
OBJECT_NAME_PATTERNS = {
    'TABLES': r'CREATE\s+TABLE\s+["]?(\w+)["]?[.]?"?(\w+)"?',
    'VIEWS': r'CREATE\s+OR\s+REPLACE\s+(?:FORCE\s+)?(?:EDITIONABLE\s+)?VIEW\s+["]?(\w+)["]?[.]?"?(\w+)"?',
    'MATERIALIZED_VIEWS': r'CREATE\s+MATERIALIZED\s+VIEW\s+["]?(\w+)["]?[.]?"?(\w+)"?',
    'SEQUENCES': r'CREATE\s+SEQUENCE\s+["]?(\w+)["]?[.]?"?(\w+)"?',
    'PROCEDURES': r'CREATE\s+OR\s+REPLACE\s+(?:EDITIONABLE\s+)?PROCEDURE\s+["]?(\w+)["]?[.]?"?(\w+)"?',
    'FUNCTIONS': r'CREATE\s+OR\s+REPLACE\s+(?:EDITIONABLE\s+)?FUNCTION\s+["]?(\w+)["]?[.]?"?(\w+)"?',
    'PACKAGES': r'CREATE\s+OR\s+REPLACE\s+(?:EDITIONABLE\s+)?PACKAGE\s+["]?(\w+)["]?[.]?"?(\w+)"?',
    'PACKAGE_BODIES': r'CREATE\s+OR\s+REPLACE\s+(?:EDITIONABLE\s+)?PACKAGE\s+BODY\s+["]?(\w+)["]?[.]?"?(\w+)"?',
    'TRIGGERS': r'CREATE\s+OR\s+REPLACE\s+(?:EDITIONABLE\s+)?TRIGGER\s+["]?(\w+)["]?[.]?"?(\w+)"?',
    'TYPES': r'CREATE\s+OR\s+REPLACE\s+TYPE\s+["]?(\w+)["]?[.]?"?(\w+)"?',
    'SYNONYMS': r'CREATE\s+(?:OR\s+REPLACE\s+)?(?:PUBLIC\s+)?SYNONYM\s+["]?(\w+)["]?[.]?"?(\w+)"?',
    'DATABASE_LINKS': r'CREATE\s+(?:PUBLIC\s+)?DATABASE\s+LINK\s+["]?(\w+)"?',
    'CONSTRAINTS': r'ADD\s+CONSTRAINT\s+["]?(\w+)"?',
    'REF_CONSTRAINTS': r'ADD\s+CONSTRAINT\s+["]?(\w+)"?\s+FOREIGN\s+KEY',
    'INDEXES': r'CREATE\s+(?:UNIQUE\s+)?INDEX\s+["]?(\w+)["]?[.]?"?(\w+)"?',
}

# ============================================================================
# Logging Setup
# ============================================================================

def setup_logging(log_file: str = None):
    """Configure logging with both console and file output."""
    log_format = '%(asctime)s - %(levelname)s - %(message)s'

    # Console handler
    console_handler = logging.StreamHandler(sys.stdout)
    console_handler.setLevel(logging.INFO)
    console_handler.setFormatter(logging.Formatter(log_format))

    handlers = [console_handler]

    # File handler if specified
    if log_file:
        file_handler = logging.FileHandler(log_file)
        file_handler.setLevel(logging.DEBUG)
        file_handler.setFormatter(logging.Formatter(log_format))
        handlers.append(file_handler)

    logging.basicConfig(
        level=logging.DEBUG,
        format=log_format,
        handlers=handlers
    )

# ============================================================================
# Database Connection
# ============================================================================

class DatabaseConnection:
    """Manages Oracle database connection."""

    def __init__(self, connection_string: str = None, host: str = None,
                 port: int = None, service: str = None, user: str = None,
                 password: str = None):
        self.connection = None
        self.cursor = None

        if connection_string:
            self.connection_string = connection_string
        elif all([host, port, service, user, password]):
            self.connection_string = f"{user}/{password}@{host}:{port}/{service}"
        else:
            raise ValueError("Either connection_string or all connection parameters must be provided")

    def __enter__(self):
        logging.info("Connecting to database...")
        self.connection = oracledb.connect(self.connection_string)
        self.cursor = self.connection.cursor()
        logging.info("Connected successfully")
        return self

    def __exit__(self, exc_type, exc_val, exc_tb):
        if self.cursor:
            self.cursor.close()
        if self.connection:
            self.connection.close()
        logging.info("Database connection closed")

    def execute(self, sql: str, commit: bool = True) -> bool:
        """Execute SQL statement and return success status."""
        try:
            self.cursor.execute(sql)
            if commit:
                self.connection.commit()
            return True
        except Exception as e:
            logging.error(f"SQL execution failed: {str(e)}")
            logging.debug(f"Failed SQL: {sql[:200]}...")
            return False

    def execute_script(self, sql_script: str) -> Tuple[bool, str]:
        """Execute SQL script and return (success, error_message)."""
        try:
            # Remove comments and split by forward slash delimiter
            statements = self._parse_sql_script(sql_script)

            for statement in statements:
                if statement.strip():
                    self.cursor.execute(statement)

            self.connection.commit()
            return True, None
        except Exception as e:
            error_msg = str(e)
            self.connection.rollback()
            return False, error_msg

    def _parse_sql_script(self, script: str) -> List[str]:
        """Parse SQL script into individual statements."""
        # Remove SQL*Plus commands
        script = re.sub(r'^\s*set\s+.*$', '', script, flags=re.MULTILINE | re.IGNORECASE)
        script = re.sub(r'^\s*whenever\s+.*$', '', script, flags=re.MULTILINE | re.IGNORECASE)
        script = re.sub(r'^\s*prompt\s+.*$', '', script, flags=re.MULTILINE | re.IGNORECASE)
        script = re.sub(r'^\s*spool\s+.*$', '', script, flags=re.MULTILINE | re.IGNORECASE)

        # First, try to split by semicolon (;) for most SQL statements
        # This handles CREATE TABLE, ALTER TABLE, CREATE INDEX, etc.
        statements = []

        # Split by semicolon, but be careful with PL/SQL blocks
        parts = script.split(';')

        for i, part in enumerate(parts):
            part = part.strip()
            if not part:
                continue

            # Check if this is the last part (no semicolon after it)
            if i == len(parts) - 1:
                # Last part - add it if non-empty
                if part:
                    statements.append(part)
            else:
                # Add the statement with semicolon removed
                statements.append(part)

        # Also handle forward slash delimiter (for PL/SQL blocks)
        # Split each statement further by forward slash if present
        final_statements = []
        for stmt in statements:
            if '\n/\n' in stmt or '\n/ \n' in stmt or stmt.strip().endswith('\n/'):
                # Split by forward slash
                sub_stmts = re.split(r'\n\s*/\s*\n', stmt)
                final_statements.extend([s.strip() for s in sub_stmts if s.strip()])
            else:
                if stmt.strip():
                    final_statements.append(stmt.strip())

        return [stmt for stmt in final_statements if stmt.strip()]

# ============================================================================
# SQL File Scanner
# ============================================================================

class SQLFileScanner:
    """Scans and categorizes SQL files."""

    def __init__(self, base_path: str):
        self.base_path = Path(base_path)
        self.files_by_type: Dict[str, List[Path]] = defaultdict(list)

    def scan(self, schemas: List[str] = None) -> Dict[str, List[Path]]:
        """Scan for SQL files and categorize them by type."""
        if schemas is None:
            schemas = ['DB_JT', 'DB_DSA']

        logging.info(f"Scanning for SQL files in {self.base_path}")

        for schema in schemas:
            schema_path = self.base_path / schema
            if not schema_path.exists():
                logging.warning(f"Schema path not found: {schema_path}")
                continue

            for object_type in DEPLOYMENT_ORDER:
                type_path = schema_path / object_type
                if type_path.exists():
                    sql_files = list(type_path.glob('*.sql'))
                    self.files_by_type[object_type].extend(sql_files)
                    logging.info(f"  Found {len(sql_files)} {object_type} files in {schema}")

        # Log summary
        total = sum(len(files) for files in self.files_by_type.values())
        logging.info(f"Total SQL files found: {total}")

        return self.files_by_type

    def get_object_info(self, file_path: Path, object_type: str) -> Tuple[str, str]:
        """Extract schema and object name from SQL file."""
        try:
            with open(file_path, 'r', encoding='utf-8', errors='ignore') as f:
                content = f.read()

            # Try to extract object name using pattern
            pattern = OBJECT_NAME_PATTERNS.get(object_type)
            if pattern:
                match = re.search(pattern, content, re.IGNORECASE | re.MULTILINE)
                if match:
                    if len(match.groups()) == 2:
                        return match.group(1), match.group(2)  # schema, object_name
                    else:
                        return None, match.group(1)  # only object_name for DB_LINKS

            # Fallback to filename
            object_name = file_path.stem
            return None, object_name

        except Exception as e:
            logging.warning(f"Could not extract object info from {file_path}: {e}")
            return None, file_path.stem

# ============================================================================
# Object Dropper
# ============================================================================

class ObjectDropper:
    """Drops existing database objects."""

    def __init__(self, db: DatabaseConnection):
        self.db = db

    def drop_all_objects(self, schema: str = None):
        """Drop all objects in the specified schema."""
        logging.info("=" * 80)
        logging.info("DROPPING EXISTING OBJECTS")
        logging.info("=" * 80)

        if schema:
            schemas = [schema]
        else:
            schemas = self._get_schemas()

        for schema_name in schemas:
            logging.info(f"\nDropping objects in schema: {schema_name}")
            self._drop_schema_objects(schema_name)

    def _get_schemas(self) -> List[str]:
        """Get list of schemas to process."""
        try:
            self.db.cursor.execute("""
                SELECT username FROM all_users
                WHERE username IN ('DB_JT', 'DB_DSA')
                ORDER BY username
            """)
            return [row[0] for row in self.db.cursor.fetchall()]
        except Exception as e:
            logging.warning(f"Could not fetch schemas: {e}")
            return ['DB_JT', 'DB_DSA']

    def _drop_schema_objects(self, schema: str):
        """Drop all objects in a schema."""
        drop_functions = [
            ('INDEX', self._drop_indexes),
            ('REF_CONSTRAINT', self._drop_ref_constraints),
            ('CONSTRAINT', self._drop_constraints),
            ('MATERIALIZED VIEW', self._drop_materialized_views),
            ('PACKAGE', self._drop_packages),
            ('PROCEDURE', self._drop_procedures),
            ('FUNCTION', self._drop_functions),
            ('TRIGGER', self._drop_triggers),
            ('VIEW', self._drop_views),
            ('TABLE', self._drop_tables),
            ('SEQUENCE', self._drop_sequences),
            ('TYPE', self._drop_types),
            ('SYNONYM', self._drop_synonyms),
            ('DATABASE LINK', self._drop_db_links),
        ]

        for obj_type, drop_func in drop_functions:
            logging.info(f"  Dropping {obj_type}s...")
            drop_func(schema)

    def _drop_tables(self, schema: str):
        """Drop all tables with CASCADE CONSTRAINTS."""
        self.db.cursor.execute(f"""
            SELECT table_name FROM all_tables
            WHERE owner = '{schema}'
            AND table_name NOT LIKE 'BIN$%'
        """)
        for (table_name,) in self.db.cursor.fetchall():
            sql = f'DROP TABLE {schema}.{table_name} CASCADE CONSTRAINTS PURGE'
            if not self.db.execute(sql, commit=False):
                logging.debug(f"    Could not drop table {table_name}")
        self.db.connection.commit()

    def _drop_views(self, schema: str):
        """Drop all views."""
        self.db.cursor.execute(f"""
            SELECT view_name FROM all_views
            WHERE owner = '{schema}'
        """)
        for (view_name,) in self.db.cursor.fetchall():
            sql = f'DROP VIEW {schema}.{view_name}'
            self.db.execute(sql, commit=False)
        self.db.connection.commit()

    def _drop_materialized_views(self, schema: str):
        """Drop all materialized views."""
        self.db.cursor.execute(f"""
            SELECT mview_name FROM all_mviews
            WHERE owner = '{schema}'
        """)
        for (mview_name,) in self.db.cursor.fetchall():
            sql = f'DROP MATERIALIZED VIEW {schema}.{mview_name}'
            self.db.execute(sql, commit=False)
        self.db.connection.commit()

    def _drop_sequences(self, schema: str):
        """Drop all sequences."""
        self.db.cursor.execute(f"""
            SELECT sequence_name FROM all_sequences
            WHERE sequence_owner = '{schema}'
        """)
        for (seq_name,) in self.db.cursor.fetchall():
            sql = f'DROP SEQUENCE {schema}.{seq_name}'
            self.db.execute(sql, commit=False)
        self.db.connection.commit()

    def _drop_procedures(self, schema: str):
        """Drop all procedures."""
        self.db.cursor.execute(f"""
            SELECT object_name FROM all_objects
            WHERE owner = '{schema}' AND object_type = 'PROCEDURE'
        """)
        for (proc_name,) in self.db.cursor.fetchall():
            sql = f'DROP PROCEDURE {schema}.{proc_name}'
            self.db.execute(sql, commit=False)
        self.db.connection.commit()

    def _drop_functions(self, schema: str):
        """Drop all functions."""
        self.db.cursor.execute(f"""
            SELECT object_name FROM all_objects
            WHERE owner = '{schema}' AND object_type = 'FUNCTION'
        """)
        for (func_name,) in self.db.cursor.fetchall():
            sql = f'DROP FUNCTION {schema}.{func_name}'
            self.db.execute(sql, commit=False)
        self.db.connection.commit()

    def _drop_packages(self, schema: str):
        """Drop all packages."""
        self.db.cursor.execute(f"""
            SELECT object_name FROM all_objects
            WHERE owner = '{schema}' AND object_type = 'PACKAGE'
        """)
        for (pkg_name,) in self.db.cursor.fetchall():
            sql = f'DROP PACKAGE {schema}.{pkg_name}'
            self.db.execute(sql, commit=False)
        self.db.connection.commit()

    def _drop_triggers(self, schema: str):
        """Drop all triggers."""
        self.db.cursor.execute(f"""
            SELECT trigger_name FROM all_triggers
            WHERE owner = '{schema}'
        """)
        for (trigger_name,) in self.db.cursor.fetchall():
            sql = f'DROP TRIGGER {schema}.{trigger_name}'
            self.db.execute(sql, commit=False)
        self.db.connection.commit()

    def _drop_types(self, schema: str):
        """Drop all types."""
        self.db.cursor.execute(f"""
            SELECT type_name FROM all_types
            WHERE owner = '{schema}'
        """)
        for (type_name,) in self.db.cursor.fetchall():
            sql = f'DROP TYPE {schema}.{type_name} FORCE'
            self.db.execute(sql, commit=False)
        self.db.connection.commit()

    def _drop_synonyms(self, schema: str):
        """Drop all synonyms."""
        self.db.cursor.execute(f"""
            SELECT synonym_name FROM all_synonyms
            WHERE owner = '{schema}'
        """)
        for (syn_name,) in self.db.cursor.fetchall():
            sql = f'DROP SYNONYM {schema}.{syn_name}'
            self.db.execute(sql, commit=False)
        self.db.connection.commit()

    def _drop_db_links(self, schema: str):
        """Drop all database links."""
        self.db.cursor.execute(f"""
            SELECT db_link FROM all_db_links
            WHERE owner = '{schema}'
        """)
        for (link_name,) in self.db.cursor.fetchall():
            sql = f'DROP DATABASE LINK {link_name}'
            self.db.execute(sql, commit=False)
        self.db.connection.commit()

    def _drop_constraints(self, schema: str):
        """Drop all constraints (excluding FK, which are ref_constraints)."""
        # Drop unique, check, and primary key constraints
        # Foreign keys are dropped separately in _drop_ref_constraints
        self.db.cursor.execute(f"""
            SELECT constraint_name, table_name, constraint_type
            FROM all_constraints
            WHERE owner = '{schema}'
            AND constraint_type IN ('U', 'C', 'P')
            AND constraint_name NOT LIKE 'SYS_%'
        """)
        for (constraint_name, table_name, constraint_type) in self.db.cursor.fetchall():
            sql = f'ALTER TABLE {schema}.{table_name} DROP CONSTRAINT {constraint_name}'
            self.db.execute(sql, commit=False)
        self.db.connection.commit()

    def _drop_ref_constraints(self, schema: str):
        """Drop all foreign key constraints."""
        self.db.cursor.execute(f"""
            SELECT constraint_name, table_name
            FROM all_constraints
            WHERE owner = '{schema}'
            AND constraint_type = 'R'
        """)
        for (constraint_name, table_name) in self.db.cursor.fetchall():
            sql = f'ALTER TABLE {schema}.{table_name} DROP CONSTRAINT {constraint_name}'
            self.db.execute(sql, commit=False)
        self.db.connection.commit()

    def _drop_indexes(self, schema: str):
        """Drop all indexes (excluding system and unique constraint indexes)."""
        self.db.cursor.execute(f"""
            SELECT index_name
            FROM all_indexes
            WHERE owner = '{schema}'
            AND index_name NOT LIKE 'SYS_%'
            AND uniqueness = 'NONUNIQUE'
        """)
        for (index_name,) in self.db.cursor.fetchall():
            sql = f'DROP INDEX {schema}.{index_name}'
            self.db.execute(sql, commit=False)
        self.db.connection.commit()

# ============================================================================
# Object Deployer
# ============================================================================

class ObjectDeployer:
    """Deploys database objects with dependency resolution."""

    def __init__(self, db: DatabaseConnection, max_retries: int = 3):
        self.db = db
        self.max_retries = max_retries
        self.deployed: Set[str] = set()
        self.failed: Dict[str, str] = {}
        self.retry_queue: Dict[str, List[Tuple[Path, str]]] = defaultdict(list)

    def deploy_all(self, files_by_type: Dict[str, List[Path]]):
        """Deploy all objects in correct order."""
        logging.info("=" * 80)
        logging.info("DEPLOYING DATABASE OBJECTS")
        logging.info("=" * 80)

        for object_type in DEPLOYMENT_ORDER:
            if object_type not in files_by_type or not files_by_type[object_type]:
                continue

            logging.info(f"\nDeploying {object_type}...")
            files = files_by_type[object_type]

            success_count = 0
            failed_count = 0

            for file_path in files:
                if self._deploy_file(file_path, object_type):
                    success_count += 1
                else:
                    failed_count += 1

            logging.info(f"  ✓ Success: {success_count}, ✗ Failed: {failed_count}")

            # Retry failed objects
            if self.retry_queue[object_type]:
                logging.info(f"  Retrying {len(self.retry_queue[object_type])} failed objects...")

                # Special handling for TABLES: retry until all are deployed or no progress
                if object_type == 'TABLES':
                    self._retry_failed_until_all_deployed(object_type)
                else:
                    self._retry_failed(object_type)

        # Final summary
        self._print_summary()

    def _deploy_file(self, file_path: Path, object_type: str) -> bool:
        """Deploy a single SQL file."""
        try:
            with open(file_path, 'r', encoding='utf-8', errors='ignore') as f:
                sql_content = f.read()

            logging.debug(f"  Deploying: {file_path.name}")

            success, error = self.db.execute_script(sql_content)

            if success:
                self.deployed.add(str(file_path))
                return True
            else:
                # For TABLES: always retry all failures (circular dependencies)
                # For other objects: only retry dependency errors
                if object_type == 'TABLES' or self._is_dependency_error(error):
                    logging.debug(f"    Will retry: {file_path.name}")
                    self.retry_queue[object_type].append((file_path, error))
                else:
                    logging.warning(f"    ✗ Failed: {file_path.name}")
                    logging.debug(f"      Error: {error}")
                    self.failed[str(file_path)] = error
                return False

        except Exception as e:
            logging.error(f"  ✗ Error reading file {file_path}: {e}")
            self.failed[str(file_path)] = str(e)
            return False

    def _is_dependency_error(self, error: str) -> bool:
        """Check if error is due to missing dependency."""
        if not error:
            return False

        dependency_patterns = [
            'does not exist',
            'invalid identifier',
            'table or view does not exist',
            'must be declared',
            'not found',
            'undefined',
        ]

        error_lower = error.lower()
        return any(pattern in error_lower for pattern in dependency_patterns)

    def _retry_failed_until_all_deployed(self, object_type: str):
        """
        Retry failed TABLES until all are deployed or no progress is made.
        This method keeps retrying indefinitely as long as progress is being made.
        """
        retry_count = 0
        total_tables = len(self.retry_queue[object_type])

        logging.info(f"  ⟳ Starting continuous retry for {total_tables} failed tables...")
        logging.info(f"  ⟳ Will retry until all tables are deployed or no progress is made")

        while self.retry_queue[object_type]:
            retry_count += 1
            previous_failed_count = len(self.retry_queue[object_type])

            logging.info(f"    ⟳ Retry iteration {retry_count}: {previous_failed_count} tables remaining")

            to_retry = self.retry_queue[object_type].copy()
            self.retry_queue[object_type].clear()

            success_this_round = 0
            for file_path, prev_error in to_retry:
                if self._deploy_file(file_path, object_type):
                    success_this_round += 1
                    logging.debug(f"      ✓ Retry successful: {file_path.name}")

            current_failed_count = len(self.retry_queue[object_type])

            logging.info(f"      ✓ Deployed: {success_this_round}, ✗ Still failing: {current_failed_count}")

            # If no progress was made, stop retrying
            if current_failed_count == previous_failed_count:
                logging.warning(f"    ⚠ No progress made in iteration {retry_count}")
                logging.warning(f"    ⚠ Stopping retry loop - {current_failed_count} tables could not be deployed")

                # Move remaining failures to failed dict
                for file_path, error in self.retry_queue[object_type]:
                    self.failed[str(file_path)] = error
                    logging.warning(f"      ✗ Permanently failed: {file_path.name}")

                self.retry_queue[object_type].clear()
                break

            # If all tables deployed successfully
            if current_failed_count == 0:
                logging.info(f"    ✅ SUCCESS! All tables deployed after {retry_count} iterations")
                break

        # Final status
        successfully_deployed = total_tables - len(self.failed)
        logging.info(f"  📊 Final result: {successfully_deployed}/{total_tables} tables deployed successfully")

    def _retry_failed(self, object_type: str):
        """Retry failed objects with dependency resolution."""
        retry_count = 0
        max_retries = self.max_retries

        while self.retry_queue[object_type] and retry_count < max_retries:
            retry_count += 1
            logging.debug(f"    Retry attempt {retry_count}/{max_retries}")

            to_retry = self.retry_queue[object_type].copy()
            self.retry_queue[object_type].clear()

            for file_path, prev_error in to_retry:
                if self._deploy_file(file_path, object_type):
                    logging.debug(f"      ✓ Retry successful: {file_path.name}")

            # If no progress, stop retrying
            if len(self.retry_queue[object_type]) == len(to_retry):
                logging.warning(f"    No progress in retry, stopping")
                for file_path, error in self.retry_queue[object_type]:
                    self.failed[str(file_path)] = error
                self.retry_queue[object_type].clear()
                break

    def _print_summary(self):
        """Print deployment summary."""
        logging.info("\n" + "=" * 80)
        logging.info("DEPLOYMENT SUMMARY")
        logging.info("=" * 80)
        logging.info(f"✓ Successfully deployed: {len(self.deployed)} objects")
        logging.info(f"✗ Failed: {len(self.failed)} objects")

        if self.failed:
            logging.info("\nFailed objects:")
            for file_path, error in list(self.failed.items())[:20]:
                logging.info(f"  ✗ {Path(file_path).name}")
                logging.debug(f"    Error: {error[:200]}")

            if len(self.failed) > 20:
                logging.info(f"  ... and {len(self.failed) - 20} more")

# ============================================================================
# Main Entry Point
# ============================================================================

def main():
    parser = argparse.ArgumentParser(
        description='Deploy Oracle database objects from SQL scripts',
        formatter_class=argparse.RawDescriptionHelpFormatter,
        epilog=__doc__
    )

    # Connection options
    conn_group = parser.add_mutually_exclusive_group(required=True)
    conn_group.add_argument('--connection', help='Connection string (user/password@host:port/service)')

    parser.add_argument('--host', help='Database host')
    parser.add_argument('--port', type=int, default=1521, help='Database port (default: 1521)')
    parser.add_argument('--service', help='Database service name')
    parser.add_argument('--user', help='Database user')
    parser.add_argument('--password', help='Database password')

    # Deployment options
    parser.add_argument('--source-dir', default='/Users/radektuma/DEV/KIS/sources/DB',
                       help='Base directory containing SQL scripts (default: /Users/radektuma/DEV/KIS/sources/DB)')
    parser.add_argument('--schema', choices=['DB_JT', 'DB_DSA', 'BOTH'], default='BOTH',
                       help='Schema to deploy (default: BOTH)')
    parser.add_argument('--skip-drop', action='store_true',
                       help='Skip dropping existing objects')
    parser.add_argument('--dry-run', action='store_true',
                       help='Show what would be deployed without actually deploying')
    parser.add_argument('--log-file', help='Log file path')
    parser.add_argument('--max-retries', type=int, default=3,
                       help='Maximum retry attempts for failed objects (default: 3)')

    args = parser.parse_args()

    # Setup logging
    log_file = args.log_file or f"deployment_{datetime.now().strftime('%Y%m%d_%H%M%S')}.log"
    setup_logging(log_file)

    logging.info("=" * 80)
    logging.info("ORACLE DATABASE DEPLOYMENT")
    logging.info("=" * 80)
    logging.info(f"Start time: {datetime.now()}")
    logging.info(f"Source directory: {args.source_dir}")
    logging.info(f"Schema: {args.schema}")
    logging.info(f"Log file: {log_file}")

    # Determine schemas to deploy
    schemas = ['DB_JT', 'DB_DSA'] if args.schema == 'BOTH' else [args.schema]

    # Scan for SQL files
    scanner = SQLFileScanner(args.source_dir)
    files_by_type = scanner.scan(schemas)

    if args.dry_run:
        logging.info("\n" + "=" * 80)
        logging.info("DRY RUN - No changes will be made")
        logging.info("=" * 80)
        for object_type in DEPLOYMENT_ORDER:
            if files_by_type.get(object_type):
                logging.info(f"{object_type}: {len(files_by_type[object_type])} files")
        return

    # Connect to database and deploy
    try:
        if args.connection:
            with DatabaseConnection(connection_string=args.connection) as db:
                if not args.skip_drop:
                    dropper = ObjectDropper(db)
                    dropper.drop_all_objects(args.schema if args.schema != 'BOTH' else None)

                deployer = ObjectDeployer(db, max_retries=args.max_retries)
                deployer.deploy_all(files_by_type)
        else:
            with DatabaseConnection(host=args.host, port=args.port, service=args.service,
                                   user=args.user, password=args.password) as db:
                if not args.skip_drop:
                    dropper = ObjectDropper(db)
                    dropper.drop_all_objects(args.schema if args.schema != 'BOTH' else None)

                deployer = ObjectDeployer(db, max_retries=args.max_retries)
                deployer.deploy_all(files_by_type)

        logging.info("\n" + "=" * 80)
        logging.info(f"Deployment completed: {datetime.now()}")
        logging.info(f"Log saved to: {log_file}")
        logging.info("=" * 80)

    except Exception as e:
        logging.error(f"Deployment failed: {e}", exc_info=True)
        sys.exit(1)

if __name__ == '__main__':
    main()
