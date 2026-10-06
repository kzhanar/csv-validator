---
tracker_id: ""
key: ""
type: epic
title: "Validate provider CSV files"
parent: initiative-csv-validator
covers: ["CAP-1", "CAP-2", "CAP-3", "CAP-4", "CAP-5"]
after: []
assignee: ""
risk: medium
---

# Validate provider CSV files

## Description

Verify and harden the existing provider CSV upload and validation workflow so it satisfies the canonical CSV Validator specification and remains covered by focused JUnit tests.

## Outcome

A user can upload a provider CSV file and receive a trustworthy row-level report; the specification's success signal is demonstrated by automated tests.

## Requirements

- CAP-1: A user can submit a CSV file containing provider data and receive a validation report.
- CAP-2: Required provider columns and values are validated, with errors for missing columns or values.
- CAP-3: Each effective_date value is validated as a date.
- CAP-4: provider_id values are unique within one uploaded file.
- CAP-5: The report contains total, valid, and invalid row counts plus row numbers and error reasons.

## Done when

1. A supported CSV upload produces a report with correct total, valid, and invalid row counts.
2. Missing required headers and values, invalid dates, and duplicate provider IDs are identified at the correct data rows.
3. Multiple errors on one row are retained without losing other row errors.
4. Uploaded data is processed transiently and no persistent storage is introduced.
5. Explicit edge-case tests cover malformed or boundary input behavior agreed during implementation.

## Boundaries

This epic owns the web upload flow, CSV parsing, validation services, report aggregation, template rendering, and their tests. It does not add persistent storage, non-CSV formats, or production-scale processing.

## References

- spec — _bmad-output/initiative-csv-validator/spec-csv-validator/spec-csv-validator.md, Why, Capabilities, Constraints, Non-goals, and Success signal
- requirements — requirements.md, Validation Rules, Output, and Constraints
- project — README.md, BMAD learning workflow

## Notes

- Decision: use one epic because all capabilities belong to one validation workflow and share the same implementation boundary.
- Decision: treat the existing implementation as verification and hardening work rather than assuming greenfield implementation.
- Observed current behavior: effective_date uses strict YYYY-MM-DD parsing; row numbers are 1-based for data rows; multiple errors are aggregated per row.
- Decision: CSV input policies are settled in the canonical spec; verify the approved size, filename/content, malformed/empty, invalid-UTF-8, blank-line, and delimiter-only cases from the initiative test catalog during the closing sweep.
