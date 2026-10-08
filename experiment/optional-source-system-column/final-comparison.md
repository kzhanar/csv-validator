# BMAD vs OpenSpec vs Superpowers

## Executive Summary

The repository contains three method-specific implementation histories and
run records for the optional `source_system` enhancement. All three report
passing project tests, but no evidence shows that the frozen independent
acceptance checklist was executed as an independent suite. Therefore no
methodology can be ranked for functional correctness from independent
acceptance evidence.

All three methods made similarly scoped production changes and added tests.
Their recorded full-suite counts are 38 for BMAD, 32 for OpenSpec, and 34 for
Superpowers; these are results from different branch states and test sets,
not a controlled head-to-head run. Their baselines also differ from each
other and from the frozen experiment-control baseline. Treat their file,
line, time, and interaction figures as method-reported evidence, not fully
normalized comparative measurements.

Exact token counts are unavailable for all three methods. Time and interaction
values below are reconstructed from available run-specific records where
possible. No weighted overall score is calculated.

## Experiment Controls

The frozen controls in
`experiment/optional-source-system-column/experiment-controls.md` specify
baseline `89d8999710e3cef3f0bd169b6ba970090e996013`, Microsoft OpenJDK
17.0.18, Maven 3.9.16, and VS Code agent (Copilot SDK). Model and reasoning
level are not recorded there.

The method metrics instead identify different working baselines:

| Method | Baseline cited in method metrics | Environment/model evidence |
|---|---|---|
| BMAD | `78aba0b4d78b516e5f75d0502e143d95ad6b8b35` | Initial tests used Temurin 21.0.12.1; a follow-up used Java 17.0.19. Model/reasoning not recorded. |
| OpenSpec | `3016750f3563bac83d2573bd6ee658b54fc25369` | Tests used Java 17.0.19. Model/reasoning not recorded. |
| Superpowers | `9368d1754179668829bb82eb7e382521f0421ed1` | Tests used Temurin 21.0.12.1; session metadata records `gpt-6-luna`. |

Because those baselines and runtime versions differ, common-baseline code
surface and environment equivalence cannot be assumed. The method-specific
feature histories are available at BMAD commit
`1182b230a425a83c9877b198a8b4593ba8d27dfa`, OpenSpec commit
`484848a32eef147b77f17a3b85f7b2decedfd6b3`, and Superpowers commit
`abc9196da78dad0882d6c506402c46e7c29238e4`. The frozen checklist is
`experiment/optional-source-system-column/independent-acceptance-checklist.md`.

## Independent Acceptance Results

The checklist is the primary correctness standard. Method-generated tests
are not treated as independent acceptance evidence. Each method's saved
metrics and result report describe feature tests and regression runs, but do
not identify an execution of the frozen IND-01–IND-10 checklist.

| Check | BMAD | OpenSpec | Superpowers |
|---|---|---|---|
| IND-01 | NOT VERIFIED | NOT VERIFIED | NOT VERIFIED |
| IND-02 | NOT VERIFIED | NOT VERIFIED | NOT VERIFIED |
| IND-03 | NOT VERIFIED | NOT VERIFIED | NOT VERIFIED |
| IND-04 | NOT VERIFIED | NOT VERIFIED | NOT VERIFIED |
| IND-05 | NOT VERIFIED | NOT VERIFIED | NOT VERIFIED |
| IND-06 | NOT VERIFIED | NOT VERIFIED | NOT VERIFIED |
| IND-07 | NOT VERIFIED | NOT VERIFIED | NOT VERIFIED |
| IND-08 | NOT VERIFIED | NOT VERIFIED | NOT VERIFIED |
| IND-09 | PASS | PASS | PASS |
| IND-10 | PASS | PASS | PASS |

**Evidence and interpretation:** IND-01–IND-08 require independent execution
of the corresponding behaviors; the records only show method-authored
tests/results, so they remain unverified under the evaluator rules. IND-09
is supported by each method's recorded successful full Maven test run. IND-10
is supported by the feature-commit diffs leaving `pom.xml` unchanged. This
does not constitute an independently frozen acceptance run.

For each method, independent checks passed: **2**, failed: **0**, not
verified: **8**. No regression failures are reported by the recorded Maven
runs. No known defect was reported in the run results; absence of a reported
defect is not independent proof that none exists. Functional requirements
IND-01–IND-08 remain unverified independently.

## Metrics Comparison

| Metric | BMAD | OpenSpec | Superpowers |
|---|---:|---:|---:|
| Planning tokens | Unavailable — platform telemetry not exposed | Unavailable — platform telemetry not exposed | Unavailable — platform telemetry not exposed |
| Implementation tokens | Unavailable — platform telemetry not exposed | Unavailable — platform telemetry not exposed | Unavailable — platform telemetry not exposed |
| Verification tokens | Unavailable — platform telemetry not exposed | Unavailable — platform telemetry not exposed | Unavailable — platform telemetry not exposed |
| Total tokens | Unavailable — platform telemetry not exposed | Unavailable — platform telemetry not exposed | Unavailable — platform telemetry not exposed |
| Total elapsed time | 11m14s, reconstructed to feature commit | About 10m39s, reconstructed to result-file creation | 11m12s, reconstructed to final assistant message |
| Time before first code change | 3m21s, reconstructed | About 6m52s, reconstructed | 3m13s, reconstructed |
| AI interactions | 2 human inputs; 81 assistant tool invocations recorded | 3 user/assistant turn pairs, reconstructed | 48 assistant turn pairs, reconstructed |
| Method artifacts | BMad plan and result; metrics updated | Proposal, delta spec, design, tasks, generated metadata, result and metrics | No written plan/spec; result and metrics; skills recorded |
| Production files changed | 5 (method metrics) | 5 | 5 |
| Test files changed | 3 | 2 | 2 |
| Lines added | 184 source/test lines, per method metrics | 116 source/test lines, per method metrics | 117 source/test lines, per method metrics |
| Lines deleted | 4 source/test lines, per method metrics | 7 source/test lines, per method metrics | 4 source/test lines, per method metrics |
| Rework cycles | 0 code/test rework recorded | 0 code rework; 2 initial Maven startups failed due to JDK configuration | 1 test-fixture correction; no production rework |
| Independent checks passed | 2/10 (IND-09, IND-10 only) | 2/10 (IND-09, IND-10 only) | 2/10 (IND-09, IND-10 only) |
| Regression tests passed | 38/38 on follow-up full run | 32/32 | 34/34 |

Counts and durations above come from each branch's own
`experiment/optional-source-system-column/metrics.md` and
`result.md`. Baseline differences make change-surface counts and elapsed
times unsuitable as precise comparative rankings. Interaction definitions
also differ: BMAD records tool invocations and user inputs, while the other
records use turn pairs. These values are not comparable as model/API-call
counts. Cached/input/output tokens and rework tokens are unavailable.

## Token Usage

Planning, implementation, verification, rework, input, output, cached, and
total token counts are **Unavailable — platform telemetry not exposed** for
all three runs. No estimates or proxy conversions are used. Consequently,
token efficiency cannot be compared.

## Time and Process Overhead

The reported time-to-first-code-change and total elapsed values are
reconstructed from session timestamps or associated file/commit timestamps;
they are not active-work durations. BMAD's total is session start to feature
commit, OpenSpec's is session start to result-file creation, and Superpowers'
is session start to final assistant message. These end markers differ, so
the totals are not strictly comparable. Planning, implementation, and
verification phase durations are not consistently recorded.

Process evidence shows:

- **BMAD:** a written plan and result; one initial task request and one
  clarification choice; 81 recorded assistant tool invocations.
- **OpenSpec:** a proposal, capability delta spec, design, and task checklist
  plus generated change metadata; the user selected that all data rows,
  including valid rows, should be shown.
- **Superpowers:** the run record lists `using-superpowers`, `brainstorming`,
  `test-driven-development`, and `verification-before-completion`; no written
  implementation plan was recorded.

The interaction figures use different counting methods and are not a fair
rank ordering of conversational overhead.

## Code Quality

All three implementations separate per-row source metadata from validation
errors, preserve the validation counters/rules, and leave the Maven dependency
manifest unchanged.

- **BMAD and Superpowers:** each uses a dedicated row-source-system record and
  stores a separate list on the validation report. Their service changes
  include a distinct pass to extract source-system values. Their feature
  commits also contain a row-note record, reflecting the notes-capable source
  state used in those method histories.
- **OpenSpec:** uses a per-row report record, keeps the error-only list
  separate, and tracks whether the header was present so the view can be
  conditional. It reads rows to populate metadata and derives total rows from
  that row collection.

These are observable design differences, not independent code-review
findings. The feature commits and method tests provide enough evidence to
describe the approaches, but there is no independent review record sufficient
to declare a quality winner.

## Change Surface

Method-reported source/test counts are close in production scope, while test
scope differs. BMAD added explicit integration coverage; Superpowers added
upload/transient-processing coverage; OpenSpec added report-service and MVC
coverage. The line totals are from different run baselines and are not
normalized to the frozen `89d899...` baseline.

Each feature commit leaves `pom.xml` unchanged: **zero direct dependency
changes**. The available method records do not establish a common-baseline
count of every unrelated file changed across the full experiment setup.

## Traceability

- **BMAD:** the run includes `_bmad-output/plan-optional-source-system-column.md`
  and a result report, linking planned work to implementation and tests.
- **OpenSpec:** the run includes a proposal, a `csv-validation-report` delta
  spec, design decisions, and three tracked tasks. The result and metrics
  report task verification. This provides the clearest explicit
  requirement-to-task structure in the available artifacts.
- **Superpowers:** session evidence records design/brainstorming and a
  test-first workflow; the result links behavior to tests, but there is no
  written plan/spec artifact in the run record.

No independent traceability audit was recorded. The observed artifact chains
are described without scoring their quality.

## Testing and Verification

| Method | Focused verification | Full regression | Integration / upload coverage | Recorded gaps |
|---|---|---|---|---|
| BMAD | 21/21 passed in focused run; later follow-up also reported 21/21 | 38/38 passed in the Java 17.0.19 follow-up | 2 integration tests included in focused/full counts | Initial run used JDK 21.0.12.1 rather than the frozen JDK 17.0.18; follow-up used 17.0.19 |
| OpenSpec | Report service 4/4; controller 11/11 | 32/32 passed | MVC upload/rendering coverage | No browser E2E run; full suite excludes stale integration report |
| Superpowers | 8/8 report-service and upload tests passed after implementation | 34/34 passed | Upload/transient-processing tests; the 2-test integration report was stale and excluded | Tests used JDK 21.0.12.1 rather than the frozen JDK 17.0.18 |

These are recorded project tests, not independent acceptance-suite results.
All three methods report passing regression tests, but differing source trees,
test selection, runtimes, and report freshness limit direct comparison.

## Rework

- **BMAD:** 0 code/test rework cycles recorded. It had setup/runtime variation
  between initial and follow-up test runs.
- **OpenSpec:** 0 code rework cycles recorded. Two Maven starts failed while
  locating a usable JDK; tests then passed using the supplied Java 17.0.19.
- **Superpowers:** one whitespace-fixture correction after Java text-block
  handling removed trailing spaces; no production-code rework. Expected
  test-first failures occurred before implementation/UI completion.

## BMAD

### Strengths

- A written implementation plan and result were retained.
- The records report focused, integration, and full-suite success, including
  a follow-up run on Java 17.0.19.

### Weaknesses

- The recorded experiment baseline differs from the frozen controls baseline.
- The initial run used JDK 21, and the session/tool invocation count is not
  comparable to the turn-pair counts in the other runs.

### Evidence

`bmad` history at `1182b230...`; `_bmad-output/plan-optional-source-system-column.md`;
`experiment/optional-source-system-column/metrics.md` and `result.md`; test
outputs recorded in the run artifacts.

## OpenSpec

### Strengths

- The change has explicit proposal/spec/design/task artifacts and an
  implementation checklist.
- The run records focused report-service and controller tests, a full suite,
  and OpenSpec validation.

### Weaknesses

- No browser end-to-end test was run.
- The run's recorded baseline differs from the frozen controls baseline, and
  the full-suite count is not identical to other branches' suites.

### Evidence

`openspec` history at `484848a...`; `openspec/changes/optional-source-system-column/`;
`experiment/optional-source-system-column/metrics.md` and `result.md`.

## Superpowers

### Strengths

- The session records a test-first implementation workflow, focused upload
  verification, and a full-suite pass.
- The time-to-first-change and full session window have session-event
  timestamps supporting reconstruction.

### Weaknesses

- No written implementation plan/spec was recorded.
- One test-fixture correction was needed, and tests used JDK 21 rather than
  the frozen JDK 17.0.18.

### Evidence

`superpowers` history at `abc9196...`; `experiment/optional-source-system-column/metrics.md`
and `result.md`; first-run Copilot session events and Maven outputs referenced
there.

## Overhead vs Value

### BMAD

**OVERHEAD:** Written plan/result; 81 assistant tool invocations recorded;
reconstructed 11m14s session-to-commit; 184 added and 4 deleted source/test
lines per method metrics.

**VALUE:** 21/21 focused and 38/38 full follow-up test results recorded;
two independent checklist items verifiable from regression/dependency
evidence; no independent acceptance execution.

### OpenSpec

**OVERHEAD:** Four planning artifacts plus generated metadata; three recorded
user/assistant turn pairs; reconstructed 10m39s to result; 116 added and 7
deleted source/test lines per method metrics.

**VALUE:** 4/4 report-service, 11/11 controller, and 32/32 full test results;
OpenSpec validation passed; explicit requirement/spec/task links; no
independent acceptance execution.

### Superpowers

**OVERHEAD:** No written plan/spec; 48 assistant turn pairs; reconstructed
11m12s session window; 117 added and 4 deleted source/test lines per method
metrics; one test-fixture correction.

**VALUE:** 8/8 focused and 34/34 full test results; session evidence records
test-first work and no production-code rework; no independent acceptance
execution.

Because token counts are unavailable, baselines differ, interaction-count
definitions differ, and independent acceptance was not run, overhead cannot
be converted into a reliable efficiency ranking.

## Scoring

| Dimension | BMAD | OpenSpec | Superpowers |
|---|---|---|---|
| Correctness | Insufficient evidence | Insufficient evidence | Insufficient evidence |
| Token efficiency | Insufficient evidence | Insufficient evidence | Insufficient evidence |
| Code quality | Insufficient evidence for a comparative score | Insufficient evidence for a comparative score | Insufficient evidence for a comparative score |
| Traceability | Insufficient evidence for a comparative score | Insufficient evidence for a comparative score | Insufficient evidence for a comparative score |
| Change discipline | Insufficient evidence for a normalized score | Insufficient evidence for a normalized score | Insufficient evidence for a normalized score |
| Testing quality | Insufficient evidence for a comparative score | Insufficient evidence for a comparative score | Insufficient evidence for a comparative score |
| Verification quality | Insufficient evidence for a comparative score | Insufficient evidence for a comparative score | Insufficient evidence for a comparative score |
| Process efficiency | Insufficient evidence | Insufficient evidence | Insufficient evidence |

The records support describing observed artifacts, source changes, and tests,
but not assigning comparable 1–5 scores: independent correctness checks are
incomplete, baselines and runtimes differ, and token/interaction measures are
not consistently defined. Suggested weights are not applied and no overall
weighted score is calculated.

Suggested weights from the evaluator criteria:

| Dimension | Weight |
|---|---:|
| Correctness | 30% |
| Token efficiency | 20% |
| Code quality | 15% |
| Traceability | 15% |
| Change discipline | 10% |
| Verification quality | 5% |
| Process efficiency | 5% |

## Overall Findings

- **Correctness:** No correctness ranking is supported. The frozen functional
  acceptance checks IND-01–IND-08 were not evidenced as an independent run.
- **Regression evidence:** All three method result sets report successful
  full Maven suites, with different test totals and execution environments.
- **Traceability:** OpenSpec has the most explicit proposal-to-spec-to-task
  artifact chain in the inspected records; BMAD has a written plan; Superpowers
  records its workflow in session evidence and results.
- **Change surface:** Production file counts are reported as five for each
  method, but per-run baselines differ and test scope varies.
- **Token/process efficiency:** Exact token usage is unavailable; timing and
  interaction reconstructions use different endpoints and counting rules.
- **Best fit:** Insufficient evidence to declare a best methodology for this
  experiment. An independent acceptance run on a single pinned baseline and
  environment, plus comparable telemetry, would be needed for a defensible
  ranking.
