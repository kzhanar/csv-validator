# Enhancement: Optional `source_system` Column

Add support for an optional CSV column named `source_system`.

Requirements:

1. `source_system` is optional.
2. Existing CSV files without `source_system` must continue to work unchanged.
3. If `source_system` is present, its value must be included in the validation report for that row.
4. Empty or whitespace-only `source_system` values are valid.
5. `source_system` does not affect whether a row is valid or invalid.
6. Existing validation rules for all other columns must remain unchanged.
7. Existing file-level validation behavior must remain unchanged.
8. Existing tests must continue to pass.
9. No new external dependency should be introduced solely for this enhancement.
