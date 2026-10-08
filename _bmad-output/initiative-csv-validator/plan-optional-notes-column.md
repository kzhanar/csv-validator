---
title: 'Add optional notes column to validation report'
type: 'feature'
ticket: ''
created: '2026-10-07'
status: 'built'
baseline_revision: '1be2745ca66419e20c42c0c8fcf426b6b42bca2e'
route: 'oneshot'
route_source: 'auto'
risk: 'medium'
review: 'quick'
review_source: 'pinned'
lenses_ran: ['quick']
review_loop_iteration: 0
context: []
---

<frozen-after-approval reason="human-owned intent — do not modify unless human renegotiates">

## Intent

**Problem:** Provider CSV files may contain useful row-level notes, but the validator currently omits them from its report.

**Approach:** Support an optional `notes` column and include its value in the report for every data row when present. Keep rows without `notes` behaviorally unchanged, and do not let notes affect validity.

**Constraints:** Preserve all validation rules and file-level behavior, keep empty and whitespace-only notes valid, and add no external dependency solely for this feature.

</frozen-after-approval>

## Implementation Notes

One-shot is appropriate for this bounded change: the report model, parser, controller, and template carry row notes separately from validation errors. The template shows notes only when the report contains row notes, preserving the existing report for files without the column. Empty and whitespace-only values are retained without being validated.

Changed five production files and two test files; no dependency was added. The focused report/controller tests pass (16 total), and the full regression suite passes (33 total). One focused run exposed a test-fixture issue because Java text blocks strip trailing whitespace; the fixture was changed to a regular string literal and the rerun passed.

The required quick review found no findings.

## Verification

**Commands:**
- `mvn test` -- passed: 33 tests, 0 failures or errors.
