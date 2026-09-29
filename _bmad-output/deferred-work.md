- source_plan: `C:\dev\gitlab\csv-validator\_bmad-output\initiative-csv-validator\epic-validate-provider-csv-files\story-validate-csv-structure-and-required-values-plan.md`
  summary: Handle truncated CSV rows consistently across date validation and report aggregation.
  evidence: CsvDateValidationService still calls record.get("effective_date") without checking record.isSet, so a truncated row can become a generic read error after the required-value service reports the missing field; resolve with the date-validation or integrated edge-case story.
