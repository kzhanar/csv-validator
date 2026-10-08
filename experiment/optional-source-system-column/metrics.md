# Experiment Metrics — Optional `source_system` Column

Only evidence-backed values are recorded. Reconstructed values are labelled
and include the records used to derive them. Token usage is not estimated.

| Metric | Value | Evidence source |
|---|---|---|
| Methodology | OpenSpec `spec-driven`; all 3 apply tasks complete | `openspec/config.yaml`; `openspec status --change optional-source-system-column`; apply instructions report 3/3 tasks done |
| Experiment baseline commit | `3016750f3563bac83d2573bd6ee658b54fc25369` | `git log` and `git status` captured for the implementation session |
| HEAD / final Git commit | HEAD remains at baseline; implementation changes are uncommitted; no final commit | `git log -1`; `git status --short`; implementation result |
| Planning tokens | Unavailable — platform telemetry not exposed | Session usage query returned no matching usage record |
| Implementation tokens | Unavailable — platform telemetry not exposed | Session usage query returned no matching usage record |
| Verification tokens | Unavailable — platform telemetry not exposed | Session usage query returned no matching usage record |
| Total tokens | Unavailable — platform telemetry not exposed | Session usage query returned no matching usage record |
| Time before first code change | **Reconstructed from session timestamps:** about 6m52s (session creation 13:49:12.062 EDT to first source-file write 13:56:04.382 EDT) | Copilot session metadata; filesystem creation/modified timestamps for `CsvValidationReportRow.java` and modified `CsvValidationReport.java` |
| Total elapsed time | **Reconstructed from session timestamps:** about 10m39s (session creation 13:49:12.062 EDT to run `result.md` creation 13:59:51.323 EDT) | Copilot session metadata; filesystem creation timestamp for `experiment/optional-source-system-column/result.md` |
| AI interactions | **Reconstructed from session records:** 3 user/assistant turn pairs for the implementation run, before the later metrics-recovery request; tool calls are not counted individually | Copilot session transcript: initial change request, `/opsx-apply` request, and JDK-path reply |
| OpenSpec artifacts | 4 tracked artifacts (proposal, delta spec, design, tasks) plus generated `.openspec.yaml` metadata | `openspec status`: 4/4 artifacts; change directory listing |
| Production files changed | 5 (4 Java files and 1 Thymeleaf template) | Baseline-to-worktree diff plus new untracked `CsvValidationReportRow.java`; listed in `result.md` |
| Test files changed | 2 | Baseline-to-worktree diff; listed in `result.md` |
| Source/test lines added | 116 | `git diff --numstat -- src` totals 113 tracked additions; add 3 lines from new untracked `CsvValidationReportRow.java` |
| Source/test lines deleted | 7 | `git diff --numstat -- src` |
| Tests added | 2 test methods (one report-service and one MVC/controller test) | Test diffs and `result.md` |
| Focused report-service tests | 4 passed, 0 failures, 0 errors, 0 skipped | Implementation-session Maven output for `mvn -Dtest=CsvValidationReportServiceTest test`; Surefire XML |
| Focused controller tests | 11 passed, 0 failures, 0 errors, 0 skipped | Implementation-session Maven output for `mvn -Dtest=CsvValidationControllerTest test`; Surefire XML |
| Full regression suite | 32 passed, 0 failures, 0 errors, 0 skipped | Implementation-session output for `mvn test`; current Surefire reports for the 7 test classes run. The `CsvValidationIntegrationTest` XML is dated 12:22 EDT, before this run, and was excluded as stale. |
| OpenSpec validation | PASS | `openspec validate optional-source-system-column`: change is valid |
| Rework / environment recovery | 0 code rework cycles; 2 Maven startup failures while locating a usable JDK, followed by successful runs with Java 17.0.19 | Implementation-session transcript and Maven outputs |
| Code quality | Pending independent evaluation | Not scored by implementer |
| Traceability quality | Pending independent evaluation | Not scored by implementer |
| Browser E2E test | Not run | Implementation result; verification records cover Maven/MVC tests only |
| Pre-existing unrelated worktree changes | Staged renames under `experiment/optional-notes-column/` and its prompt modification remained untouched | Initial and final `git status --short` records in the implementation session |
