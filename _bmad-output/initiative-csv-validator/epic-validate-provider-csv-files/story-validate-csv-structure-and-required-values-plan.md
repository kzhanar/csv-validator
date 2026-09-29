---
title: 'Validate CSV structure and required values'
type: 'feature'
ticket: '2'
created: '2026-09-29'
status: 'built'
route: 'oneshot'
route_source: 'auto'
baseline_revision: '889c61d1a0b433ad985e5755174aa44b38409b12'
review: 'quick'
review_source: 'pinned'
lenses_ran: ['quick']
review_loop_iteration: 0
context:
  - '{project-root}/_bmad-output/initiative-csv-validator/spec-csv-validator/spec-csv-validator.md'
  - '{project-root}/_bmad-output/initiative-csv-validator/epic-validate-provider-csv-files/epic-validate-provider-csv-files.md'
---

<frozen-after-approval reason="human-owned intent — do not modify unless human renegotiates">

## Intent

**Problem:** The CSV validation workflow must reject files with missing required headers and identify rows with missing required provider values without producing a misleading validation report.

**Approach:** Reuse the existing structure validator, required-value validator, controller flow, report aggregation, and focused JUnit tests; make only the smallest correction required if the ticket verification exposes a real gap.

</frozen-after-approval>

## Implementation Notes

This is an oneshot build because the ticket is a narrow CAP-2 verification slice and the repository already contains the relevant implementation and tests. Do not change date validation, duplicate detection, or malformed-input policy in this ticket.

The focused structure, required-value, and missing-header tests initially passed with the existing implementation. Review identified that truncated rows could throw instead of reporting missing trailing values, so `CsvRequiredValueValidationService` now guards unset fields and its regression test covers that case. The focused suite passes with 12 tests.

## Review Triage Log

- patch: A truncated row could throw from `CsvRequiredValueValidationService` instead of reporting an unset required field. Added an `isSet` guard and regression test; the focused suite passes.
- defer: `CsvDateValidationService` still reads an unset trailing `effective_date` directly during full report aggregation. This pre-existing cross-validator edge case is recorded in `deferred-work.md` for the date-validation or integrated edge-case story.

## Verification

**Commands:**
- `mvn test "-Dtest=CsvStructureValidationServiceTest,CsvRequiredValueValidationServiceTest,CsvValidationControllerTest#rejectsCsvWithMissingRequiredHeaders"` -- expected: all structure, required-value, and missing-header tests pass.
