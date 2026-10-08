Read the existing CSV Validator requirements and experiment/change-request.md.
This is a small brownfield enhancement.
Use only the methodology configured in this branch and follow its normal workflow.
Inspect the existing implementation before changing code. Preserve existing behavior and backward compatibility. Implement the smallest reasonable change that satisfies the approved change request.
Add appropriate tests and run relevant regression/integration tests. Do not make unrelated changes.
Maintain experiment/results.md throughout the run. Record only measurements supported by available evidence. Do not estimate unavailable token, timing, or testing metrics; leave them as Not recorded.
At completion report methodology artifacts created, production and test files changed, tests executed and results, PASS / FAIL / GAP / RISK, rework cycles, and unresolved issues.
Do not score your own code quality, traceability quality, or overall methodology performance. Those will be assessed independently.
