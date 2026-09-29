# CSV Validator - Epics and User Stories

This backlog is derived from the approved [product specification](product-spec.md) and [proposed architecture](architecture.md). Stories describe MVP behavior and are small enough to implement and verify separately. No application code is included here.

## Epic 1: Upload a CSV

Give the user a minimal browser flow for submitting one CSV file for validation.

### US-1: Open the upload page

As a user, I want a simple page to select a CSV file so that I can start validation.

**Acceptance criteria**

- Given the application is available, when I open its home page, then I see a file selector and a submit control.
- The page identifies that the input must be a CSV file.
- The file selector has an accessible label.

### US-2: Submit one CSV for validation

As a user, I want to submit one CSV file so that the application can validate it and show me the result.

**Acceptance criteria**

- Given I select a non-empty CSV file, when I submit it, then the server receives it as a multipart upload and returns a rendered response.
- Given I submit without a file, when the request is handled, then the page shows a clear file-level error.
- Given I select a file that is not a CSV, when I submit it, then the page shows a clear unsupported-file error instead of a validation report.
- File-type rejection is enforced server-side and does not rely only on a browser-provided content type.

## Epic 2: Validate provider data

Parse the agreed CSV format and identify row-level data errors according to the product rules.

### US-3: Check the CSV structure

As a user, I want the application to verify the required columns so that a structurally incorrect file is rejected before row validation.

**Acceptance criteria**

- Given a comma-delimited UTF-8 CSV with a header record, when it is parsed, then quoted values are handled as CSV fields, including quoted values containing commas.
- Given all exact, case-sensitive required headers are present, when the file is checked, then row validation can proceed.
- Given one or more required headers are missing, when the file is checked, then the result identifies every missing header and no row-level results are produced.
- Given additional columns are present, when the file is checked, then they are ignored and do not cause failure.

### US-4: Find missing required values

As a user, I want missing provider values identified by row and field so that I can correct incomplete records.

**Acceptance criteria**

- Given a data row has an empty or whitespace-only `provider_id`, `provider_name`, or `effective_date`, when it is validated, then the row has an error naming each missing field.
- Given a required value contains non-whitespace characters, when required-value checks run, then it is not reported as missing.

### US-5: Validate effective dates

As a user, I want effective dates checked against the agreed format so that invalid dates are reported.

**Acceptance criteria**

- Given `effective_date` is a real calendar date in strict `YYYY-MM-DD` format, when it is validated, then it passes the date check.
- Given `effective_date` is malformed or does not represent a real calendar date, when it is validated, then the row has a date error.
- A date such as `2025-02-30` fails validation.

### US-6: Find duplicate provider IDs

As a user, I want duplicate provider IDs identified across the file so that every affected record can be corrected.

**Acceptance criteria**

- Given IDs differ only by surrounding whitespace, when uniqueness is checked, then their trimmed values are compared.
- Given two or more rows have the same trimmed ID with matching letter case, when the file is validated, then every row in that duplicate group has a duplicate-ID error.
- Given two IDs differ only by letter case, when uniqueness is checked, then they are treated as distinct.

## Epic 3: Understand validation results

Show the user a clear, consistent summary and actionable row-level error reasons.

### US-7: Calculate validation totals and row errors

As a user, I want a summary of valid and invalid rows with row-specific reasons so that I can assess the file quickly.

**Acceptance criteria**

- Given row validation completes, when the report is created, then it includes total data rows, valid rows, invalid rows, and each row's errors.
- Data row numbers start at 1 after the header; the header is not counted.
- A row with multiple errors is listed once with every applicable reason.
- A row with one or more errors counts as invalid once.
- Total rows equal valid rows plus invalid rows.
- When all rows are valid, the report shows zero invalid rows and no row errors.

### US-8: View the validation report

As a user, I want the report displayed on the upload page so that I can read the outcome without downloading or opening another tool.

**Acceptance criteria**

- Given a validation report is returned, when the page renders, then it displays total, valid, and invalid row counts.
- The page displays every row number with its reason or reasons.
- Given a file-level failure, when the page renders, then it displays the file-level message and does not display row-level results.
- The page remains available to submit another file after showing a result.

## Epic 4: Keep uploads transient

Honor the requirement not to retain uploaded data or reports.

### US-9: Process uploads without permanent storage

As a user, I want my uploaded data used only for validation so that it is not retained by the application.

**Acceptance criteria**

- The application does not write uploaded CSV contents or validation reports to a database or application-managed permanent file store.
- The report is created for the current request and no upload or report history is available after the response.
- CSV row values and file contents are not written to application logs.
- Automated tests exercise validation using in-memory or request-scoped input without a database.

## Open Decisions Before Implementation

These are listed in the architecture but are not specified by the approved product requirements; resolve them before implementation:

- Maximum accepted upload size.
- User-visible handling of malformed CSV syntax, an empty file, and blank data records.
- Whether file type is determined by the filename, parsed content, or both.