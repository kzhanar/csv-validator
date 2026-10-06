---
id: SPEC-csv-validator
companions:
  - ../../../product-spec.md
  - ../../../architecture.md
  - ../../../epics-and-stories.md
  - ../../../story-3-test-cases.md
  - ../test-cases-csv-validator.md
sources:
  - requirements.md
---

> **Canonical contract.** This SPEC is the complete, preservation-validated contract for what to build, test, and validate. The source documents listed in frontmatter are retained for traceability.

# CSV Validator

## Why

Users need to catch missing, invalid, and duplicate provider data before processing CSV files. This project builds a simple browser-based validator and serves as a Java, Spring Boot, Maven, and JUnit learning exercise for the BMAD path from requirements through verification.

## Capabilities

- **CAP-1**
  - **intent:** A user can submit one provider CSV file and see its validation outcome.
  - **success:** Supported CSV with required headers produces a row-level report; header-only CSV produces a zero-row report; empty, malformed, invalid-encoding, or unsupported files fail at file level without partial row results.

- **CAP-2**
  - **intent:** The validator checks the required `provider_id`, `provider_name`, and `effective_date` columns and values.
  - **success:** Missing headers are identified at file level; empty or whitespace-only required values are identified on their data rows.

- **CAP-3**
  - **intent:** The validator checks that each `effective_date` value is a valid date.
  - **success:** Dates must be real calendar dates in strict `YYYY-MM-DD` format, and invalid values are identified by row.

- **CAP-4**
  - **intent:** The validator checks that `provider_id` values are unique within one uploaded file.
  - **success:** IDs are compared after trimming surrounding whitespace, case-sensitively, and every row in a duplicate group is identified.

- **CAP-5**
  - **intent:** The validator presents a report that summarizes and explains row-level validation outcomes.
  - **success:** The report shows total, valid, and invalid data rows, numbers rows from 1 after the header, reports every reason, counts each invalid row once, and satisfies total = valid + invalid.

## Constraints

- Accept comma-delimited UTF-8 CSV content with exact, case-sensitive required headers; ignore extra columns.
- Require a case-insensitive `.csv` filename extension and validate content server-side; do not rely on browser-provided MIME type.
- Limit each upload to 1 MiB and process one upload at a time.
- Reject zero-byte or whitespace-only files, malformed CSV, and invalid UTF-8 at file level without partial results. Header-only files produce zero-row reports; ignore completely blank physical lines but treat delimiter-only records as data rows.
- Keep uploaded contents and reports request-scoped; do not retain them permanently, provide upload history, or log CSV row values.
- Keep the first version simple and use Java, Spring Boot, Maven, and JUnit.

## Non-goals

- Persistent storage or upload/report history.
- Support for non-CSV input formats.
- Expanded production-scale processing beyond the simple first version.

## Success signal

A user can submit a provider CSV and receive correct data-row totals plus every applicable required-value, invalid-date, and duplicate-ID reason by row. Missing required headers produce a file-level explanation without row results, and uploaded data is not retained after the request.

