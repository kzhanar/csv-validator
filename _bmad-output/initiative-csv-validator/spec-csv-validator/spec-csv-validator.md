---
id: SPEC-csv-validator
companions: []
sources:
  - README.md
  - requirements.md
---

> **Canonical contract.** This SPEC is the complete, preservation-validated contract for what to build, test, and validate. The source documents listed in frontmatter are retained for traceability.

# CSV Validator

## Why

Users receive provider CSV files containing missing, invalid, or duplicate data and need a simple way to validate a file before processing it. This project creates that validator while providing a small Java, Spring Boot, Maven, and JUnit learning exercise for practicing the BMAD path from requirements through verification.

## Capabilities

- **CAP-1**
  - **intent:** A user can submit a CSV file containing provider data and receive a validation report.
  - **success:** A supported CSV submission produces a validation report.

- **CAP-2**
  - **intent:** The validator checks that `provider_id`, `provider_name`, and `effective_date` columns exist and that each required value is populated.
  - **success:** Missing required columns or values produce errors that identify the validation failure.

- **CAP-3**
  - **intent:** The validator checks that each `effective_date` value is a valid date.
  - **success:** Rows containing invalid dates are identified in the validation report.

- **CAP-4**
  - **intent:** The validator checks that `provider_id` values are unique within one uploaded file.
  - **success:** Duplicate provider IDs are identified with their affected rows.

- **CAP-5**
  - **intent:** The validator presents a report that summarizes and explains row-level validation outcomes.
  - **success:** The report contains total rows, valid rows, invalid rows, each affected row number, and each error reason.

## Constraints

- Only CSV files are supported.
- Uploaded data must not be stored permanently.
- The first version must remain simple.
- The implementation is a Java, Spring Boot, Maven, and JUnit learning project.

## Non-goals

- Persistent storage of uploaded data.
- Support for non-CSV input formats.
- Expanded production-scale processing beyond the simple first version.

## Success signal

A user can upload a provider CSV file and receive a report that correctly counts total, valid, and invalid rows and explains every required-column, required-value, invalid-date, or duplicate-ID error by row.

## Open Questions

- Which date format or parser policy defines a valid `effective_date`?
- How should row numbers be defined relative to the header, and should one row report multiple validation errors?
- What behavior is required for malformed CSV syntax, unsupported encodings, empty files, and files with extra columns?
