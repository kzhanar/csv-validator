# CSV Validator Test Cases

## Scope

This catalog is derived from the canonical `SPEC-csv-validator`, the approved product acceptance criteria, and US-1 through US-9. It defines pre-implementation behavior; it does not add application code.

Unless otherwise stated, automated coverage uses JUnit 5. Test validation rules and CSV parsing at the service boundary; use MockMvc for upload, rendered file-level errors, and report presentation. Upload-size cases use the actual UTF-8 byte count. Each traceability entry names at least one capability and functional requirement; US and product acceptance-criterion references provide additional linkage.

`1 MiB` means 1,048,576 bytes. A file exactly at this size is accepted; larger files are rejected. `AC-1` through `AC-7` refer to the numbered acceptance criteria in `product-spec.md`. Story references point to the behavior in `epics-and-stories.md`; story criteria are not separately numbered there.

## Existing US-3 Cases

Detailed structure tests remain in `story-3-test-cases.md`. Their traceability is:

| Existing case | Coverage | Traceability |
|---|---|---|
| TC3-01 | Required headers permit row validation | CAP-2; FR-2; US-3 |
| TC3-02 | Extra columns are ignored | CAP-2; FR-2; US-3 |
| TC3-03 | Quoted commas parse as one field | CAP-1, CAP-2; FR-1; US-3 |
| TC3-04 | UTF-8 text parses correctly | CAP-1; FR-1; US-3 |
| TC3-05 | Missing `provider_id` header | CAP-2; FR-2; US-3; AC-5 |
| TC3-06 | Missing `provider_name` header | CAP-2; FR-2; US-3; AC-5 |
| TC3-07 | Missing `effective_date` header | CAP-2; FR-2; US-3; AC-5 |
| TC3-08 | Multiple missing headers | CAP-2; FR-2; US-3; AC-5 |
| TC3-09 | Header matching is case-sensitive | CAP-2; FR-2; US-3 |
| TC3-10 | Missing-header failure prevents row validation | CAP-2; FR-2; US-3; AC-5 |
| TC3-11 | Header-only input has zero data records | CAP-1, CAP-2; FR-2; US-3 |

## Upload and File-Type Cases

### TC-CV-001: Upload page exposes an accessible CSV selector

**Input/action:** Request the application home page.

**Expected:** The page renders a file selector with an accessible label, identifies CSV as the accepted format, and provides a submit control.

**Traceability:** CAP-1; FR-1; US-1.

### TC-CV-002: Valid CSV produces a successful report

**Input:**

```csv
provider_id,provider_name,effective_date
P-100,North Clinic,2025-01-31
P-101,South Center,2024-02-29
```

**Expected:** The rendered report shows 2 total, 2 valid, and 0 invalid data rows, with no row errors.

**Traceability:** CAP-1, CAP-5; FR-1, FR-6; US-2, US-7, US-8; AC-1, AC-7.

### TC-CV-003: Submission without a file is rejected

**Input/action:** Submit the upload form without selecting a file.

**Expected:** The page shows a clear file-level error and no row-level report.

**Traceability:** CAP-1; FR-1; US-2.

### TC-CV-004: Non-CSV filename is rejected even when its content is CSV

**Input:** A valid CSV payload named `providers.txt` with browser MIME type `text/csv`.

**Expected:** The server rejects the file based on the unsupported filename extension; no validation report is shown. The browser MIME type does not override the filename rule.

**Traceability:** CAP-1; FR-1; US-2; AC-6.

### TC-CV-005: Uppercase CSV extension and misleading MIME type are accepted

**Input:** A valid UTF-8 CSV named `providers.CSV` with browser MIME type `application/octet-stream`.

**Expected:** The case-insensitive extension is accepted, content is validated server-side, and the normal report is rendered.

**Traceability:** CAP-1, CAP-2; FR-1, FR-2; US-2, US-3.

### TC-CV-006: CSV extension does not bypass content validation

**Input:** A syntactically valid CSV named `providers.csv` whose header lacks one or more required columns.

**Expected:** The response reports each missing required header at file level and contains no row-level results.

**Traceability:** CAP-1, CAP-2; FR-2; US-3; AC-5.

### TC-CV-007: Upload exactly at the size limit is accepted

**Input:** A valid CSV payload padded in an ignored extra-column value to exactly 1,048,576 UTF-8 bytes.

**Expected:** The upload is accepted and receives a validation report; the padding column does not affect validation.

**Traceability:** CAP-1, CAP-2; FR-1, FR-2; US-2, US-3.

### TC-CV-008: Upload one byte over the size limit is rejected

**Input:** A valid CSV payload of 1,048,577 bytes.

**Expected:** The upload receives a clear file-level size error and no row-level report.

**Traceability:** CAP-1; FR-1; US-2.

## Required-Value and Date Cases

### TC-CV-009: Each empty required value is reported

**Input:** Parameterized rows with an empty `provider_id`, `provider_name`, or `effective_date`, with the other required values valid.

**Expected:** The affected row is invalid and names the corresponding missing field; each parameterized variant is reported separately.

**Traceability:** CAP-2; FR-3; US-4; AC-2.

### TC-CV-010: Each whitespace-only required value is reported

**Input:** Parameterized rows where one required field contains spaces or tabs only and the other required values are valid.

**Expected:** The affected row is invalid and identifies the whitespace-only field as missing.

**Traceability:** CAP-2; FR-3; US-4; AC-2.

### TC-CV-011: Delimiter-only record is treated as a data row

**Input:** A valid required-header record followed by `,,`.

**Expected:** The record is counted as data row 1, is not discarded as a blank physical line, and has missing-value errors for all three required fields. It counts as one invalid row.

**Traceability:** CAP-2, CAP-5; FR-3, FR-6; US-4, US-7; AC-2, AC-7.

### TC-CV-012: Leap-day date is accepted

**Input:** A row with `effective_date` equal to `2024-02-29`.

**Expected:** The date passes validation and the row is valid when its other values are valid and unique.

**Traceability:** CAP-3; FR-4; US-5.

### TC-CV-013: Invalid dates and non-strict formats are rejected

**Input:** Parameterized `effective_date` values: `2025-02-30`, `2025-1-02`, `2025/01/02`, and `2025-01-02T00:00:00`.

**Expected:** Each row is invalid with a date error. No value outside a real calendar date in strict `YYYY-MM-DD` format is accepted.

**Traceability:** CAP-3; FR-4; US-5; AC-3.

## Duplicate-ID and Report Cases

### TC-CV-014: Every row in an exact duplicate group is invalid

**Input:** Three data rows use `P-100`; one additional row has a unique ID.

**Expected:** All three rows in the duplicate group are identified as duplicates; the unique row is not marked duplicate.

**Traceability:** CAP-4, CAP-5; FR-5, FR-6; US-6, US-7; AC-4.

### TC-CV-015: Duplicate comparison trims surrounding whitespace

**Input:** Two rows use `P-100` and ` P-100 ` respectively.

**Expected:** Both rows are identified as duplicates because comparison uses trimmed IDs.

**Traceability:** CAP-4; FR-5; US-6; AC-4.

### TC-CV-016: Duplicate comparison preserves letter case

**Input:** Two rows use `P-100` and `p-100`.

**Expected:** The IDs are distinct; neither row receives a duplicate-ID error.

**Traceability:** CAP-4; FR-5; US-6.

### TC-CV-017: Data row numbering excludes the header

**Input:** A header followed by one valid row and one row with a missing provider name.

**Expected:** The first data row is numbered 1 and the second is numbered 2; the header is not counted.

**Traceability:** CAP-5; FR-6; US-7, US-8.

### TC-CV-018: Multiple errors on a row are retained and counted once

**Input:** One data row has a whitespace-only provider name and an invalid date.

**Expected:** The report lists the row once with both reasons; total rows is 1, valid rows is 0, and invalid rows is 1.

**Traceability:** CAP-2, CAP-3, CAP-5; FR-3, FR-4, FR-6; US-4, US-5, US-7, US-8; AC-2, AC-3, AC-7.

### TC-CV-019: Report totals satisfy the count invariant

**Input:** A file with four data rows: one valid row, one row with a missing required value, and two rows sharing a duplicate ID.

**Expected:** The report shows 4 total, 1 valid, and 3 invalid rows; each invalid row appears once with its reasons, and total equals valid plus invalid.

**Traceability:** CAP-5; FR-6; US-7, US-8; AC-7.

### TC-CV-020: Header-only CSV produces a zero-row report

**Input:** A `.csv` file containing only the three exact required headers, optionally followed by completely blank physical lines.

**Expected:** The file is accepted; the report shows 0 total, 0 valid, 0 invalid, and no row errors.

**Traceability:** CAP-1, CAP-5; FR-6; US-3, US-7, US-8.

## Empty and Malformed-File Cases

### TC-CV-021: Zero-byte file is rejected

**Input:** A zero-byte file named with a `.csv` extension.

**Expected:** A clear file-level empty-file error is shown, with no row-level results.

**Traceability:** CAP-1; FR-1, FR-6; US-2, US-8.

### TC-CV-022: Whitespace-only file is rejected

**Input:** A `.csv` file containing only spaces, tabs, or line breaks and no CSV header record.

**Expected:** A clear file-level empty-file error is shown, with no row-level results.

**Traceability:** CAP-1; FR-1, FR-6; US-2, US-8.

### TC-CV-023: Malformed CSV fails without a partial report

**Input:** A `.csv` file with required headers and a data record containing an unterminated quoted field.

**Expected:** Parsing fails with a clear file-level malformed-CSV error. No partial row report is rendered, even if earlier records were parseable.

**Traceability:** CAP-1; FR-1, FR-6; US-2, US-3, US-8.

### TC-CV-024: Invalid UTF-8 fails without a partial report

**Input:** A `.csv` byte stream containing an invalid UTF-8 byte sequence in an otherwise valid CSV record.

**Expected:** The file fails with a clear file-level encoding error and no partial row report.

**Traceability:** CAP-1; FR-1, FR-6; US-2, US-3, US-8.

### TC-CV-025: Completely blank physical lines are ignored

**Input:** A valid header and two valid data records separated by and followed by empty physical lines.

**Expected:** The report contains exactly 2 data rows, both valid; empty physical lines are not counted as data records.

**Traceability:** CAP-1, CAP-5; FR-6; US-3, US-7, US-8.

## Transient-Data Case

### TC-CV-026: Upload and report are not retained or logged

**Input/action:** Submit a valid CSV containing a unique sentinel provider name, receive the report, then inspect application-managed persistent storage, available history, and captured application logs.

**Expected:** No uploaded contents or report are retained after the request, no upload/report history is available, and neither the sentinel nor other row values appear in application logs. Validation uses request-scoped or in-memory input without a database.

**Traceability:** CAP-1; FR-7; US-9.
