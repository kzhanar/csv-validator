# Experiment Metrics — Optional `source_system` Column

Only evidence-backed values are recorded. Durations and turn counts marked
reconstructed are calculated from the saved Copilot session event timestamps;
they are not estimates.

| Metric | Value | Evidence source |
|---|---|---|
| Configured methodology | Workspace-local Superpowers; bounded design approval followed by test-first implementation | `.github/copilot-instructions.md`; first-run session `events.jsonl` (`skill.invoked` and message events) |
| Baseline commit | `9368d1754179668829bb82eb7e382521f0421ed1` (`2026-10-08T13:13:01-04:00`) | `git show -s 9368d17`; session-start Git metadata |
| Baseline worktree | Clean at session start | First-run session `events.jsonl` / captured `git status --short` |
| Time before first code change | **00:03:12.544 — Reconstructed from session timestamps** (session start `17:13:37.056Z`; first recorded file edit `17:16:49.600Z`) | First-run `C:\Users\koi8\.copilot\session-state\0a74d031-d7cc-4fc0-a32c-b52b0ce799db\events.jsonl` (`session.start`, first `tool.execution_complete` with `fileEdits`) |
| Total elapsed session time | **00:11:12.468 — Reconstructed from session timestamps** (session start `17:13:37.056Z` to final assistant message `17:24:49.524Z`, before the later metrics-recovery request) | Same first-run `events.jsonl` (`session.start`, final `assistant.message`) |
| AI interaction count | **48 assistant turn pairs — Reconstructed from session records** (`assistant.turn_start` = 48; `assistant.turn_end` = 48). This is an event count, not a claim of 48 premium/API requests. | First-run `events.jsonl`, filtered to before the later metrics-recovery user message |
| Assistant message events | 75 — event records, not a count of unique user-facing responses | Same first-run `events.jsonl`, `assistant.message` events before the later request |
| Model recorded for run | `gpt-6-luna` (automatic selection resolved to this model) | Same first-run `events.jsonl` (`session.auto_mode_resolved`) |
| Premium requests in usage checkpoint | 1; this checkpoint field is not a count of all assistant turns | Same first-run `events.jsonl` (`session.usage_checkpoint`, `totalPremiumRequests`) |
| Non-token usage telemetry | `6,883,252,200` nano-AIU; **not tokens** | Same first-run `events.jsonl` (`session.usage_checkpoint`, `totalNanoAiu`) |
| Planning tokens | **Unavailable — platform telemetry not exposed** | Session usage checkpoint exposes nano-AIU and premium-request count, not token counts; local `assistant_usage_events` query returned no rows for this run |
| Implementation tokens | **Unavailable — platform telemetry not exposed** | Same evidence as planning tokens |
| Verification tokens | **Unavailable — platform telemetry not exposed** | Same evidence as planning tokens |
| Total tokens | **Unavailable — platform telemetry not exposed** | Same evidence as planning tokens |
| Production files changed | 5 (4 modified, 1 new) | `git diff --numstat 9368d17 -- src/main`; untracked `src/main/java/com/example/csvvalidator/validation/CsvValidationRowSourceSystem.java` |
| Test files changed | 2 | `git diff --numstat 9368d17 -- src/test` |
| Tests added | 3 (2 report-service tests, 1 upload integration test) | Test diff from `9368d17`; first-run session `events.jsonl` records the test-first edits |
| Application source/test lines | 117 added / 4 deleted, including the untracked source-system record | `git diff --numstat 9368d17 -- src/main src/test` plus line count of the untracked Java record |
| All working-tree lines at implementation-run completion | 220 added / 14 deleted, including untracked files; the tracked-only Git diff does not include untracked-file contents | First-run `events.jsonl` contains the captured PowerShell total (`+220 / -14`) calculated from `git diff --numstat 9368d17` plus then-untracked `result.md` and `CsvValidationRowSourceSystem.java` |
| Changed files total | 9 (7 tracked modifications, 2 untracked additions) | `git status --short`; tracked diff and untracked-file listing relative to `9368d17` |
| External dependencies added | 0 | No dependency-manifest changes in `git diff 9368d17`; `pom.xml` unchanged |
| Focused report and upload run | 8 tests passed; 0 failures, 0 errors, 0 skipped | First-run `events.jsonl` tool output for `mvn "-Dtest=CsvValidationReportServiceTest,CsvTransientProcessingTest" test`; Surefire class reports corroborate 5 + 3 |
| Final full Maven suite | 34 tests passed; 0 failures, 0 errors, 0 skipped; `BUILD SUCCESS` | First-run `events.jsonl` output for `mvn test`; final Surefire reports: `CsvTransientProcessingTest` 3, `CsvDateValidationServiceTest` 3, `CsvDuplicateProviderIdValidationServiceTest` 3, `CsvRequiredValueValidationServiceTest` 2, `CsvStructureValidationServiceTest` 8, `CsvValidationReportServiceTest` 5, and `CsvValidationControllerTest` 10 |
| Stale Surefire integration report | `CsvValidationIntegrationTest.txt` reports 2 passing tests, but is dated `2026-10-08 12:22:48 PM`; it predates the final full run reports (`1:23:50–1:23:51 PM`) and is **not included** in the 34-test final-suite count | File timestamps and contents under `target/surefire-reports` |
| Test runtime / compiler target | Temurin JDK `21.0.12.1`; Maven compiler target `--release 17` | First-run Maven output in `events.jsonl` |
| Rework cycles | 1 test-fixture correction; no production-code rework. Test-first verification also recorded expected pre-implementation and pre-UI failures. | First-run `events.jsonl` tool outputs; `result.md` |
| Superpowers skills invoked | `using-superpowers`, `brainstorming`, `test-driven-development`, `verification-before-completion`; no written implementation plan for this bounded change | First-run `events.jsonl` (`skill.invoked`); `.github/copilot-instructions.md`; `result.md` |
| Experiment/methodology artifacts | `change-request.md` pre-existed at baseline; `result.md` created during the run; `metrics.md` updated; no separate plan/spec artifact created | `git show 9368d17:experiment/optional-source-system-column/metrics.md`; baseline tree; current Git diff/status; `result.md`; branch instructions |
| Code quality | **Pending independent evaluation** | User instruction; not self-scored |
| Traceability quality | **Pending independent evaluation** | User instruction; not self-scored |
| Final Git commit | No implementation commit; `HEAD` remains the baseline commit `9368d17` | `git rev-parse HEAD`; `git status --short`; Git history |

## Evidence notes

- The session event export is
  `C:\Users\koi8\.copilot\session-state\0a74d031-d7cc-4fc0-a32c-b52b0ce799db\events.jsonl`.
  The interaction/timing rows are limited to events before the later metrics
  recovery request.
- Final Surefire text reports are under `target/surefire-reports`. The older
  `CsvValidationIntegrationTest.txt` is kept distinct from reports refreshed by
  the final `mvn test` invocation; its passing tests are not added to that
  invocation's count.
- `result.md` contains the implementation summary and exact recorded test
  command outcomes. No application source, test, methodology artifact, or Git
  history was changed for this metrics-recovery task.
