# Proposal

## Why

Provider CSV files may contain a source-system value that users need to see
alongside validation results. The validator currently reports row errors and
summary counts but does not expose source-system data.

## What Changes

- Include the `source_system` value in the user-visible report for every data
  row when the optional column is present.
- Keep existing validation counts, row errors, and file-level validation
  behavior unchanged; the new field does not affect row validity.
- Continue accepting files that omit `source_system`.

## Capabilities

### New Capabilities

- `csv-validation-report`: Report per-row validation outcomes and optionally
  display source-system values without changing validation behavior.

### Modified Capabilities

None.

## Impact

- Affected application code: CSV report generation and the Thymeleaf report
  view.
- Affected tests: report-generation and upload/report integration coverage.
- No external dependency changes are expected.
