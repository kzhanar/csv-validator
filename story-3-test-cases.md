# US-3 Test Cases: Check the CSV Structure

These cases map to US-3 in [epics-and-stories.md](epics-and-stories.md). They verify CSV parsing and required-header behavior only; row-level field, date, and duplicate validation belong to later stories.

Unless noted otherwise, each case is automated with JUnit 5 by providing the CSV input to the CSV structure validation service and asserting its result. CSV fixtures are shown as UTF-8 text.

## TC3-01: Required headers allow row validation

**Precondition:** The CSV structure validation service is available.

**Input:**

```csv
provider_id,provider_name,effective_date
P-100,North Clinic,2025-01-31
```

**Steps:**

1. Supply the fixture as a CSV input.
2. Run CSV structure validation.

**Expected result:** All three required headers are recognized exactly, and the structure result permits row validation to proceed.

**Manual or automated:** Automated (JUnit 5)

## TC3-02: Extra columns are ignored

**Precondition:** The CSV structure validation service is available.

**Input:**

```csv
provider_id,provider_name,effective_date,source_system
P-100,North Clinic,2025-01-31,legacy
```

**Steps:**

1. Supply the fixture as a CSV input.
2. Run CSV structure validation.

**Expected result:** The structure check succeeds; `source_system` does not cause failure and is not treated as a required column.

**Manual or automated:** Automated (JUnit 5)

## TC3-03: Quoted values containing commas parse as one field

**Precondition:** The CSV structure validation service is available.

**Input:**

```csv
provider_id,provider_name,effective_date
P-100,"North Clinic, East",2025-01-31
```

**Steps:**

1. Supply the fixture as UTF-8 CSV input.
2. Run CSV structure validation.

**Expected result:** The CSV parses successfully; the quoted comma is part of `provider_name`, not a column separator, and the required headers are recognized.

**Manual or automated:** Automated (JUnit 5)

## TC3-04: UTF-8 field content is preserved by parsing

**Precondition:** The CSV structure validation service is available.

**Input:**

```csv
provider_id,provider_name,effective_date
P-100,München Health,2025-01-31
```

**Steps:**

1. Encode the fixture as UTF-8 and supply it as CSV input.
2. Run CSV structure validation.

**Expected result:** The file parses without a character-encoding error, and the required headers are recognized.

**Manual or automated:** Automated (JUnit 5)

## TC3-05: Missing provider_id header is reported

**Precondition:** The CSV structure validation service is available.

**Input:**

```csv
provider_name,effective_date
North Clinic,2025-01-31
```

**Steps:**

1. Supply the fixture as CSV input.
2. Run CSV structure validation.

**Expected result:** Validation fails at the file level and identifies `provider_id` as missing. No row-level results are produced.

**Manual or automated:** Automated (JUnit 5)

## TC3-06: Missing provider_name header is reported

**Precondition:** The CSV structure validation service is available.

**Input:**

```csv
provider_id,effective_date
P-100,2025-01-31
```

**Steps:**

1. Supply the fixture as CSV input.
2. Run CSV structure validation.

**Expected result:** Validation fails at the file level and identifies `provider_name` as missing. No row-level results are produced.

**Manual or automated:** Automated (JUnit 5)

## TC3-07: Missing effective_date header is reported

**Precondition:** The CSV structure validation service is available.

**Input:**

```csv
provider_id,provider_name
P-100,North Clinic
```

**Steps:**

1. Supply the fixture as CSV input.
2. Run CSV structure validation.

**Expected result:** Validation fails at the file level and identifies `effective_date` as missing. No row-level results are produced.

**Manual or automated:** Automated (JUnit 5)

## TC3-08: Multiple missing required headers are all reported

**Precondition:** The CSV structure validation service is available.

**Input:**

```csv
legacy_code
X-1
```

**Steps:**

1. Supply the fixture as CSV input.
2. Run CSV structure validation.

**Expected result:** Validation fails at the file level and identifies all three missing headers: `provider_id`, `provider_name`, and `effective_date`. No row-level results are produced.

**Manual or automated:** Automated (JUnit 5)

## TC3-09: Header matching is case-sensitive

**Precondition:** The CSV structure validation service is available.

**Input:**

```csv
Provider_ID,provider_name,effective_date
P-100,North Clinic,2025-01-31
```

**Steps:**

1. Supply the fixture as CSV input.
2. Run CSV structure validation.

**Expected result:** Validation fails at the file level and identifies the exact required header `provider_id` as missing. No row-level results are produced.

**Manual or automated:** Automated (JUnit 5)

## TC3-10: Missing-header failure prevents row-level validation

**Precondition:** The CSV structure validation service is available.

**Input:**

```csv
provider_id,effective_date
,,not-a-date
```

**Steps:**

1. Supply the fixture as CSV input.
2. Run CSV structure validation.
3. Inspect the result for file-level and row-level errors.

**Expected result:** The result identifies missing `provider_name` as a file-level error. It contains no row-level errors for empty values or the malformed date because row validation does not run.

**Manual or automated:** Automated (JUnit 5)

## TC3-11: Header-only CSV has no data records

**Precondition:** The CSV structure validation service is available.

**Input:**

```csv
provider_id,provider_name,effective_date
```

**Steps:**

1. Supply the fixture as CSV input.
2. Run CSV structure validation.

**Expected result:** All required headers are recognized and the structure check permits row validation to proceed with zero data records.

**Manual or automated:** Automated (JUnit 5)

## Full-Suite Reference

The approved upload-size, file-type, empty/malformed-input, validation, report, and transient-data cases are cataloged in [_bmad-output/initiative-csv-validator/test-cases-csv-validator.md](_bmad-output/initiative-csv-validator/test-cases-csv-validator.md). The traceability table there maps TC3-01 through TC3-11 to the canonical capabilities and functional requirements.