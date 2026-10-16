---
title: 'Validate effective dates'
type: 'feature'
ticket: '3'
created: '2026-10-06'
baseline_revision: '10ff586ec4435e63fe185dd63a7e8d2d174a2ea3'
status: 'built'
route: 'oneshot'
route_source: 'auto'
review: 'quick'
review_source: 'pinned'
lenses_ran: ['quick']
review_loop_iteration: 0
context:
  - '{project-root}/_bmad-output/initiative-csv-validator/spec-csv-validator/spec-csv-validator.md'
  - '{project-root}/_bmad-output/initiative-csv-validator/test-cases-csv-validator.md'
---

<frozen-after-approval reason="human-owned intent — do not modify unless human renegotiates">

## Intent

**Problem:** CAP-3 requires `effective_date` to be a real calendar date in strict `YYYY-MM-DD` format, with invalid rows reported through the existing validation report.

**Approach:** Reuse the current date validator and report aggregation path. Add explicit regression coverage for the remaining TC-CV-013 datetime example and the TC-CV-018 missing-name-plus-invalid-date combination. Change production code only if the new tests expose a behavior gap.

</frozen-after-approval>

## Implementation Notes

Oneshot scope: the date and report services already implement the required behavior; the expected change is limited to focused test coverage. Do not alter other validation stories or refactor the report path.

The existing date validator uses strict `uuuu-MM-dd` parsing and the report service merges reasons by data-row number. Added the TC-CV-013 ISO timestamp example and the exact TC-CV-018 missing-name-plus-invalid-date case; both passed without a production-code change.

Focused verification passed: `CsvDateValidationServiceTest` (3), `CsvValidationReportServiceTest` (4), and `CsvValidationFlowIntegrationTest` (1), with 0 failures/errors. Quick review returned no findings.

## Verification

**Commands:**
- `mvn test "-Dtest=CsvDateValidationServiceTest,CsvValidationReportServiceTest,CsvValidationFlowIntegrationTest"` -- expected: strict date cases, combined row reasons, and the real upload-to-report regression all pass.