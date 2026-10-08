# Experiment Metrics

All metrics below are for the completed Optional Source System Column run. Timing and interaction values explicitly marked reconstructed are derived from available session records, transcript, and file/commit timestamps; they are not platform-reported durations or model-call counts.

| Metric | Value | Evidence source |
|---|---|---|
| Planning tokens | Unavailable — platform telemetry not exposed | Session-usage query returned no rows for the run; no token counts in the available transcript. |
| Implementation tokens | Unavailable — platform telemetry not exposed | Session-usage query returned no rows for the run; no token counts in the available transcript. |
| Verification tokens | Unavailable — platform telemetry not exposed | Session-usage query returned no rows for the run; no token counts in the available transcript. |
| Total tokens | Unavailable — platform telemetry not exposed | Session-usage query returned no rows for the run; no token counts in the available transcript. |
| Time before first production-code change | 3 min 21.483 sec — Reconstructed from session timestamps | Session created at 2026-10-08 11:54:17.984 -04:00; earliest changed production source file timestamp is 11:57:39.467 -04:00 (`CsvValidationReport.java`, filesystem LastWriteTime). |
| Total elapsed session window | 11 min 14.016 sec — Reconstructed from session timestamps | Session created at 11:54:17.984 -04:00; feature commit `1182b230a425a83c9877b198a8b4593ba8d27dfa` committed at 12:05:32 -04:00. Measures session-start-to-commit wall-clock window, not active working time. |
| Human-AI inputs recorded | 1 initial task request and 1 clarification choice | Original session transcript records the task request; the session's `ask_user` exchange records selection of loose work. |
| Assistant tool invocations recorded | 81 — reconstructed from the completed run's transcript; not an LLM/API call count | Original session transcript turn 1 contains 81 recorded tool calls. Individual call timestamps are not available. |
| Production files changed | 5 | `git diff --numstat` from baseline `78aba0b4d78b516e5f75d0502e143d95ad6b8b35` to feature commit; cross-checked against `result.md`. |
| Test files changed | 3 | Same baseline-to-commit Git diff; cross-checked against `result.md`. |
| Production lines added/deleted | 53 added / 3 deleted | Baseline-to-commit Git numstat for `src/main`. |
| Test lines added/deleted | 131 added / 1 deleted | Baseline-to-commit Git numstat for `src/test`. |
| Total committed lines added/deleted | 318 added / 17 deleted | `git diff --numstat` / `git show --stat` from experiment baseline to feature commit. |
| Files in baseline-to-commit diff | 11 | Git diff from the plan's recorded baseline to feature commit; includes production, tests, BMad plan, result, and metrics. |
| Methodology artifacts created | 2: BMad plan and experiment result | Git diff includes `_bmad-output/plan-optional-source-system-column.md` and `experiment/optional-source-system-column/result.md` as new files. `metrics.md` was updated, not created. |
| Newly added test methods | 5 | Test source diff: 2 service tests, 1 controller test, and 2 integration tests; names listed in `result.md`. |
| Focused test result | 21 run; 21 passed; 0 failures, 0 errors, 0 skipped | Original session's successful focused Maven output; 7 report-service, 12 controller, and 2 integration tests. |
| Initial focused Maven duration | 8.855 sec (run used JDK 21) | Maven output recorded in the original session transcript. |
| Initial full Maven duration | 6.784 sec (run used JDK 21) | Full `mvn test` output saved during the original session. |
| Initial test JVM / compiler target | Eclipse Temurin OpenJDK 21.0.12.1; Maven compiler `--release 17` | Original focused Maven output and `result.md`. |
| Follow-up focused verification | 21 run; 21 passed; 0 failures, 0 errors, 0 skipped | Maven output from 2026-10-08 follow-up using the user-supplied Java 17.0.19 runtime; saved output confirms `java version "17.0.19"` and `BUILD SUCCESS`. |
| Follow-up focused Maven duration | 6.906 sec | Maven `Total time` in the 2026-10-08 Java 17 follow-up output. |
| Integration test result | 2 run; 2 passed; 0 failures, 0 errors, 0 skipped | Latest `target/surefire-reports/com.example.csvvalidator.CsvValidationIntegrationTest.txt`, refreshed by the Java 17 full run. Included in focused and full totals; do not add to their counts. |
| Follow-up full regression | 38 run; 38 passed; 0 failures, 0 errors, 0 skipped | Maven output from the 2026-10-08 Java 17 follow-up; confirmed against latest `target/surefire-reports/*.txt`. |
| Follow-up full Maven duration | 7.061 sec | Maven `Total time` in the 2026-10-08 Java 17 follow-up output. |
| Follow-up test JVM | Oracle Java SE Runtime Environment 17.0.19+9-LTS-183 | Java version and Maven runtime details in the 2026-10-08 follow-up output using the user-supplied JDK. |
| Whitespace check | Passed | Original session's `git diff --check` output; final committed diff was staged after the check. |
| Code/test rework cycles | 0 recorded | Original session transcript and `result.md`: tests passed on first successful focused/full runs; no subsequent production or test edits. |
| Code quality | Pending independent evaluation | Evaluation state requested for this experiment; no independent score is recorded here. |
| Traceability quality | Pending independent evaluation | Evaluation state requested for this experiment; no independent score is recorded here. |
