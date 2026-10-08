# Independent Acceptance Checklist: Optional `source_system` Column

```text
IND-01  CSV without source_system behaves exactly as baseline
IND-02  CSV with populated source_system is accepted
IND-03  source_system value appears in the validation report for its row
IND-04  Empty source_system is accepted
IND-05  Whitespace-only source_system is accepted
IND-06  source_system does not make an otherwise invalid row valid
IND-07  Existing validation rules for all other columns remain unchanged
IND-08  Existing file-level validation behavior remains unchanged
IND-09  Existing tests continue to pass
IND-10  No new external dependency is introduced solely for this enhancement
```
