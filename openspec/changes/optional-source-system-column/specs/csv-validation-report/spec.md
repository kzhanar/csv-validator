# Spec Delta

## Purpose

Defines the CSV validation report presented to users, including per-row
validation outcomes and optional source-system metadata.

## ADDED Requirements

### Requirement: Source-system column is optional
The system MUST accept CSV files with or without a `source_system` column.

#### Scenario: File omits source_system
- **WHEN** a CSV file contains the existing required columns but no `source_system` column
- **THEN** the file is processed with the same report and validation behavior as before

#### Scenario: File includes source_system
- **WHEN** a CSV file contains the existing required columns and a `source_system` column
- **THEN** the file passes file-level column validation

### Requirement: Report source-system values for every row
When the `source_system` column is present, the report MUST show its value for every data row.

#### Scenario: Valid row has a source-system value
- **WHEN** a row has no validation errors and has a `source_system` value
- **THEN** the report shows that value for the row

#### Scenario: Invalid row has a source-system value
- **WHEN** a row has one or more validation errors and has a `source_system` value
- **THEN** the report shows that value alongside the row's validation outcome

#### Scenario: Source-system value is empty or whitespace-only
- **WHEN** a row's `source_system` value is empty or contains only whitespace
- **THEN** the report accepts the value and shows it for the row without adding a validation error

### Requirement: Source-system metadata does not affect validation
The system MUST determine row validity independently of the `source_system` value.

#### Scenario: Source-system value is present
- **WHEN** all existing validation rules are applied to a row that has a `source_system` value
- **THEN** the row's validity and validation reasons are determined only by the existing rules for other columns

#### Scenario: Source-system value is absent
- **WHEN** a row belongs to a file without a `source_system` column
- **THEN** existing validation rules and their outcomes remain unchanged
