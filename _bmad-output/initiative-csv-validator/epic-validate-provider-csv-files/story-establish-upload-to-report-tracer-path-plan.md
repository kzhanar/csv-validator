---
title: 'Establish upload-to-report tracer path'
type: 'feature'
ticket: '1'
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

**Problem:** The first story needs a verified tracer path from a supported provider CSV upload through validation orchestration to a rendered report.

**Approach:** Reuse the existing Spring MVC controller, report service, Thymeleaf template, and focused MockMvc test; make only the smallest correction required if the focused tracer check fails.

</frozen-after-approval>

## Implementation Notes

This is an oneshot build because the ticket is a narrow verification/hardening slice and the likely change is under 100 lines. The repository already contains the relevant implementation and test; do not refactor validation services or dependency configuration in this ticket.

The original focused controller test passed with the existing controller, report service, template, and MockMvc test. Review identified that it mocked the service boundary rather than proving the real path, so `CsvValidationFlowIntegrationTest` was added as the smallest test-only correction. The new integration test passes with the actual Spring services and template. Broader validation failures remain assigned to later stories in the epic.

## Review Triage Log

- false: The first review correctly identified that the original controller test mocked the service boundary. The finding was patched by adding `CsvValidationFlowIntegrationTest`; the combined focused tests and full Maven suite pass.
- defer: The invalid-date fixture in the existing controller test is pre-existing and belongs to CAP-3 validation work, not this CAP-1/CAP-5 tracer ticket.
- defer: The broad controller exception handler and malformed-input behavior are pre-existing and remain assigned to the epic's later edge-case verification story.
- false: The BMAD setup artifacts are expected planning deliverables for this workflow and do not represent an application-code scope violation.
- false: The reviewer's empty temporary diff was caused by Windows `NUL` handling in review-artifact generation; the directly named integration test exists and passed focused and full-suite verification.

## Verification

**Commands:**
- `mvn test -Dtest=CsvValidationControllerTest#acceptsNonEmptyCsvWithoutRelyingOnBrowserContentType,CsvValidationFlowIntegrationTest` -- expected: the controller boundary test and the real Spring upload-to-report integration test pass.
