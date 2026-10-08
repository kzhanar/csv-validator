# Design

## Context

`CsvValidationReportService` currently calculates totals and collects errors
for invalid rows. `CsvValidationRowError` contains only a row number and
reasons, so valid rows have no row-level report entry. The upload template
renders summary counts and those errors. Structure validation checks only the
existing required headers and already permits extra columns.

## Goals / Non-Goals

**Goals:**
- Carry the `source_system` value for every CSV data row into the report.
- Show a per-row source-system column only when the CSV header is present.
- Keep validation counts, errors, and existing file checks independent of this
  metadata.

**Non-Goals:**
- Add or change validation rules for `source_system`.
- Change behavior or presentation for CSV files without that column.
- Add a CSV parsing dependency.

## Decisions

- Keep the current error-only entries for validation reasons and add a
  separate per-row report collection for row number and optional source-system
  value. Extending only the error entries would omit valid rows; replacing the
  error collection would unnecessarily change existing error-report semantics.
- Track whether the header exists separately from its cell values, and render
  the source-system column only when present. This distinguishes an absent
  column from a present-but-empty value and preserves the old view for files
  without the column.
- Read cell values using the existing Commons CSV parser without trimming or
  making them part of validation. This preserves source text, including empty
  and whitespace-only values, and adds no dependency.

## Risks / Trade-offs

- [Risk] Report data is parsed alongside existing validation passes and could
  be misaligned by row numbering → use the same CSV record order and verify
  row-number correspondence with focused tests.
- [Risk] Rendering empty values could make a source value appear absent →
  retain the column when the header exists and test empty and whitespace-only
  cells explicitly.

## Migration Plan

No data migration is required. Deploy the application change normally. If
rollback is needed, revert the application change; CSV inputs and persisted
data are unaffected.

## Open Questions

None.
