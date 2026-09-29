# CSV Validator - Product Specification

## Overview

CSV Validator is a small browser-based application for checking provider CSV files before they are processed. A user selects a CSV file and receives a validation summary with row-level errors. Uploaded data is not stored permanently.

## Goal

Help a user quickly identify missing, invalid, or duplicate provider data in a CSV file.

## MVP Scope

- Upload and validate one CSV file at a time in a browser page.
- Display validation totals and row-level errors.
- Support CSV files only.
- Do not retain uploaded data after validation.

Saving reports, editing CSV data, processing multiple files, and keeping validation history are out of scope for the first version.

## CSV Contract

- The file is comma-delimited UTF-8 CSV.
- The first record contains headers.
- These exact, case-sensitive headers are required: `provider_id`, `provider_name`, and `effective_date`.
- Additional columns are allowed and ignored by validation.
- Data row numbers start at 1 for the first record after the header; the header is not counted as a data row.

## Functional Requirements

### FR-1: Select a CSV file

The user can select a CSV file for validation. Non-CSV files are rejected with a clear message.

### FR-2: Validate required columns

The file must contain all required headers. If one or more are missing, validation fails for the file and the application identifies each missing header. Row-level validation does not run in this case.

### FR-3: Validate required values

For every data row, `provider_id`, `provider_name`, and `effective_date` must have a non-empty value. A value containing only whitespace is considered missing.

### FR-4: Validate effective dates

`effective_date` must be a real calendar date in strict `YYYY-MM-DD` format.

### FR-5: Detect duplicate provider IDs

`provider_id` values must be unique within the file. Surrounding whitespace is ignored when comparing IDs; comparison is case-sensitive. Every row containing an ID that appears more than once is invalid.

### FR-6: Display a validation report

After row validation, display:

- Total data rows
- Valid row count
- Invalid row count
- Each row number with one or more validation errors
- A reason for each error

Each data row with at least one error counts as invalid once. The total row count equals the sum of valid and invalid rows. File-level failures, such as missing required headers, display a clear error instead of row-level results.

### FR-7: Avoid permanent storage

Uploaded file contents are used only to produce the validation result and are not stored permanently. The application does not provide a file or report history.

## Acceptance Criteria

1. Given a CSV with all required headers and valid, unique data, when the user validates it, then the report shows all data rows as valid and shows no row errors.
2. Given a row with an empty or whitespace-only required value, when the user validates the file, then that row is marked invalid with the missing field identified.
3. Given a row with an invalid or non-`YYYY-MM-DD` effective date, when the user validates the file, then that row is marked invalid with the date error identified.
4. Given a provider ID repeated in multiple rows, when the user validates the file, then every row containing that duplicate ID is marked invalid.
5. Given a file missing one or more required headers, when the user validates it, then validation fails with the missing header names and no row-level results are shown.
6. Given a non-CSV file, when the user selects it for validation, then the application rejects it with a clear message.
7. Given any completed row-level validation, then the displayed total equals the number of data rows and equals valid rows plus invalid rows.

## Constraints and Assumptions

- The first version is intentionally simple and browser-based.
- CSV conventions, date format, whitespace handling, and duplicate-ID behavior are as defined above.
- No report download, configuration of validation rules, or long-term data retention is required.