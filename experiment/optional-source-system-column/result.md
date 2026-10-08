# Optional `source_system` Column — Result

## Methodology

Used the branch-configured BMad Build workflow (`bmad-build`), with its automatically selected oneshot route. The work was treated as loose experiment work because no active initiative was configured. The quick review lens reported no significant issues.

## Approach

Mirrored the existing optional `notes` report path: capture `source_system` per data row when present, carry the values through the report and controller, and render a conditional “Source system by row” table. Extraction does not participate in validation. Empty and whitespace-only values are retained as valid values. Existing four- and five-argument `CsvValidationReport` constructors remain available.

## Methodology Artifacts

- Created [`_bmad-output/plan-optional-source-system-column.md`](../../_bmad-output/plan-optional-source-system-column.md).
- Created this result file.
- Updated [`metrics.md`](./metrics.md) with measurements supported by test logs, Git, and available session telemetry.

## Files Changed

**Production files (5):**

- `src/main/java/com/example/csvvalidator/validation/CsvValidationReport.java`
- `src/main/java/com/example/csvvalidator/validation/CsvValidationReportService.java`
- `src/main/java/com/example/csvvalidator/validation/CsvValidationRowSourceSystem.java`
- `src/main/java/com/example/csvvalidator/web/CsvValidationController.java`
- `src/main/resources/templates/index.html`

**Test files (3):**

- `src/test/java/com/example/csvvalidator/validation/CsvValidationReportServiceTest.java`
- `src/test/java/com/example/csvvalidator/web/CsvValidationControllerTest.java`
- `src/test/java/com/example/csvvalidator/CsvValidationIntegrationTest.java`

## Tests Added

- `includesSourceSystemForEveryRowWithoutChangingValidationResults`
- `acceptsEmptyAndWhitespaceOnlySourceSystems`
- `rendersSourceSystemByRowWhenTheReportContainsSourceSystems`
- `validatesAndRendersSourceSystemValuesForEveryRow`
- `keepsReportWithoutSourceSystemSectionWhenColumnIsAbsent`

## Tests Executed and Exact Results

The project's configured `JAVA_HOME` points to a missing Java 17 directory. Tests were therefore run with the locally available Eclipse Temurin OpenJDK 21.0.12.1 bundled with VS Code; Maven compiled the project with `--release 17`.

- Focused: `mvn "-Dtest=CsvValidationReportServiceTest,CsvValidationControllerTest,CsvValidationIntegrationTest" test` — **BUILD SUCCESS**, 21 tests, 0 failures, 0 errors, 0 skipped; Maven reported 8.855 seconds.
- Full regression: `mvn test` — **BUILD SUCCESS**, 38 tests, 0 failures, 0 errors, 0 skipped; Maven reported 6.784 seconds.
- `git diff --check` — passed with no whitespace errors.

The first unquoted PowerShell invocation failed during argument parsing before Maven ran. The next invocation did not start Maven because the configured `JAVA_HOME` was invalid; the available JDK 21 was then used for both successful runs.

## PASS / FAIL / GAP / RISK

- **PASS:** Optional per-row `source_system` values are reported, including empty and whitespace-only values; values do not affect row validity.
- **PASS:** CSV files without the optional column continue to omit its report section; required-column and file-level validation behavior is unchanged.
- **PASS:** Focused and full regression test runs succeeded with no failures or errors; no dependency was added.
- **FAIL:** None.
- **GAP:** The application was not executed on Java 17 because the configured JDK path is unavailable. Compilation used `--release 17`, and tests ran on Java 21.
- **RISK:** Low. The enhancement only adds optional report metadata and does not alter validation decisions.

## Rework Cycles

0 code/test rework cycles.

## Unresolved Issues

No implementation or test failures remain. A test run on an actual Java 17 runtime remains unverified.

## Completion Checks

- Production code changed: **Yes**.
- Backward compatibility preserved: **Yes** — legacy CSVs omit the new section, existing validation behavior is unchanged, and previous report constructor arities remain supported.
- Unrelated changes made: **No**.
- Final Git commit: **To be reported in the completion message** (the hash cannot be embedded in the commit that contains this result without creating a self-referential follow-up commit).
