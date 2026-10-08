# CSV Validator - Business Requirements

## Background

Users receive CSV files containing provider information.
The files sometimes contain missing, invalid, or duplicate data.

Users need a simple application that validates a CSV file before it is processed.

## Business Objective

Create a simple application that allows a user to upload a CSV file and receive a validation report.

## CSV Format

Required columns:

- provider_id
- provider_name
- effective_date

## Validation Rules

1. provider_id is required.
2. provider_name is required.
3. effective_date is required.
4. effective_date must be a valid date.
5. provider_id must be unique within the file.
6. Missing required columns must cause validation to fail.

## Output

After validation, display:

- total number of rows
- number of valid rows
- number of invalid rows
- row number for each error
- reason for each error

## Constraints

- Do not store uploaded data permanently.
- Support CSV files only.
- Keep the first version simple.