# Optional `source_system` Column — Result

## Methodology

Used the branch-configured workspace-local Superpowers methodology. This was
handled as a bounded brownfield change: inspected the requirements and existing
implementation, presented the in-chat design for approval, then implemented
test-first. BMAD and OpenSpec workflows were not loaded.

## Approach

The report service now reads the optional `source_system` column as row metadata
without adding it to required headers or validation rules. Values are preserved
per data row, including empty and whitespace-only strings. The upload report
renders a separate “Source system by row” table only when the column is present.
The existing report constructors with four and five arguments remain available.
No dependency or file-level validation behavior changed.

## Methodology Artifacts

- [change-request.md](./change-request.md) — existing fixed input; not modified.
- [metrics.md](./metrics.md) — updated with run evidence and unavailable values
  marked `Not recorded`.
- [result.md](./result.md) — created for this run.
- No specification or implementation-plan file was created; the configured
  bounded Superpowers flow uses an approved in-chat design and does not require
  those artifacts.

## Files Changed

### Production files

- `src/main/java/com/example/csvvalidator/validation/CsvValidationReport.java`
- `src/main/java/com/example/csvvalidator/validation/CsvValidationReportService.java`
- `src/main/java/com/example/csvvalidator/validation/CsvValidationRowSourceSystem.java` (added)
- `src/main/java/com/example/csvvalidator/web/CsvValidationController.java`
- `src/main/resources/templates/index.html`

### Test files

- `src/test/java/com/example/csvvalidator/validation/CsvValidationReportServiceTest.java`
- `src/test/java/com/example/csvvalidator/CsvTransientProcessingTest.java`

## Tests Added

1. Report-service coverage for row-aligned values, blank and whitespace-only
   source-system values, and unchanged row validity.
2. Report-service coverage that the report metadata is empty when the optional
   column is absent.
3. Upload integration coverage for rendered values, blank and whitespace-only
   cells, preserved validation counts/errors, and hiding the section when the
   column is absent.

## Tests Executed

All Maven test commands used the workspace's bundled Temurin JDK
`21.0.12.1`; Maven compiled the project with `--release 17`.

| Command | Result |
|---|---|
| `mvn -Dtest=CsvValidationReportServiceTest test` before implementation | Expected RED: test compilation failed because `rowSourceSystems()` did not yet exist (8 missing-method errors). |
| `mvn -Dtest=CsvValidationReportServiceTest test` after report-service implementation, initial fixture | 5 tests run; 1 failed because Java text-block processing removed trailing spaces from the whitespace fixture. The fixture was corrected to use a literal concatenated string. |
| `mvn -Dtest=CsvValidationReportServiceTest test` after fixture correction | 5 tests run; 0 failures, 0 errors, 0 skipped. |
| `mvn -Dtest=CsvTransientProcessingTest test` before UI wiring | 3 tests run; 1 expected failure because the new report section was not yet rendered. |
| `mvn "-Dtest=CsvValidationReportServiceTest,CsvTransientProcessingTest" test` after UI wiring | 8 tests run; 0 failures, 0 errors, 0 skipped. |
| `mvn test` | 34 tests run; 0 failures, 0 errors, 0 skipped; `BUILD SUCCESS`. |

An initial Maven invocation could not start because the configured `JAVA_HOME`
pointed to a missing JDK 17 directory. The available bundled JDK 21 was used
instead. The project targets Java 17 bytecode, but execution on an actual JDK 17
runtime was not verified.

## Outcome

- **PASS:** All nine requirements in the change request are met by the
  implementation and test results. Existing CSVs without `source_system`
  continue through the previous validation flow; empty and whitespace-only
  source-system values remain metadata and do not affect row validity.
- **FAIL:** None in the final focused or full test runs.
- **GAP:** Tests ran on JDK 21 rather than a JDK 17 runtime because the configured
  JDK 17 path was unavailable. Maven compilation used `--release 17`.
- **RISK:** `CsvValidationReport` now has an additional record component.
  Existing four- and five-argument constructors are retained, but consumers
  relying on the record's exact component list or generated `equals`,
  `hashCode`, or `toString` representation may observe the added field.

## Rework Cycles

One test-fixture correction was needed after Java text-block indentation
handling removed the intended whitespace-only value. No production-code rework
was needed after focused and full-suite verification.

## Unresolved Issues

- No enhancement-specific issue remains.
- JDK 17 runtime execution remains unverified; see the verification gap above.
- Test output included existing `@MockBean` deprecation and Mockito dynamic
  Java-agent warnings under JDK 21; no dependency or unrelated cleanup was
  performed.

## Compatibility and Scope

- **Production code changed:** Yes.
- **Backward compatibility preserved:** Yes for existing CSV uploads and the
  prior four- and five-argument `CsvValidationReport` constructors. The report
  record's component structure is extended as noted under risk.
- **Unrelated changes made:** No.
- **Final Git commit:** None created. The current `HEAD` remains `9368d17`;
  implementation and experiment-result changes are uncommitted.
