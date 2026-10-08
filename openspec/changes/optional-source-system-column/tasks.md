# Tasks

## 1. Report data and validation coverage

- [x] 1.1 Add per-row report data and source-system extraction, preserving existing error and total calculations; extend report-service tests for files with and without the column, valid and invalid rows, empty and whitespace-only values, and unchanged validity, then run `mvn -Dtest=CsvValidationReportServiceTest test`.

## 2. User-visible report

- [x] 2.1 Render source-system values for every data row only when the column is present; add controller/view coverage for present and absent columns, update `requirements.md`, and run the focused controller tests.

## 3. Regression verification

- [x] 3.1 Run `mvn test` and `openspec validate optional-source-system-column`; confirm all tests pass and the change validates.
