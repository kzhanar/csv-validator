# Enhancement: Optional Notes Column

The CSV Validator currently accepts provider CSV files containing its
existing required columns.

Add support for an optional column named `notes`.

Requirements:

1. `notes` is optional.
2. Existing CSV files that do not contain `notes` must continue to work
   without any behavior change.
3. If the `notes` column is present, its value must be included in the
   validation report for that row.
4. An empty or whitespace-only `notes` value is valid.
5. `notes` does not affect whether a row is valid or invalid.
6. Existing validation rules for all other columns must remain unchanged.
7. Existing file-level validation behavior must remain unchanged.
8. Existing tests must continue to pass.
9. No new external dependencies should be introduced solely for this feature.
