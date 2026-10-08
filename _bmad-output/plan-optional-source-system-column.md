---
title: 'Add optional source_system column to validation report'
type: 'feature'
ticket: ''
created: '2026-10-08'
status: 'built'
baseline_revision: '78aba0b4d78b516e5f75d0502e143d95ad6b8b35'
route: 'oneshot'
route_source: 'auto'
risk: 'low'
review: 'quick'
review_source: 'pinned'
lenses_ran: ['quick']
review_loop_iteration: 0
context: []
---

<frozen-after-approval reason="human-owned intent — do not modify unless human renegotiates">

## Intent

**Problem:** The validator currently ignores an optional `source_system` CSV column, so its value is absent from the per-row report.

**Approach:** Capture and display `source_system` per data row when the header is present. Keep files without the column behaviorally unchanged, accept empty and whitespace-only values, and leave validation results and all existing file-level validation rules unchanged.

## Boundaries & Constraints

**Always:** Preserve the three required headers and every existing row validation rule. Do not make the presence or contents of `source_system` affect validity. Add no dependency.

**Never:** Do not change file-level validation, required-column handling, or behavior for CSV files without `source_system`.

</frozen-after-approval>

## Implementation Notes

Used the existing optional `notes` report path as the implementation precedent. Report extraction remains separate from validation; existing four- and five-argument `CsvValidationReport` constructors remain available. Added unit/controller coverage and full-context MockMvc coverage for source-system display and unchanged reporting when the column is absent. The quick review reported no significant issues.

## Verification

**Commands:**
- `mvn -Dtest=CsvValidationReportServiceTest,CsvValidationControllerTest,CsvValidationIntegrationTest test` -- expected: focused report, rendered-controller, and full-context integration tests pass.
- `mvn test` -- expected: all regression tests pass.
