package com.example.csvvalidator.validation;

import java.util.List;

public record CsvValidationReport(
        int totalRows,
        int validRows,
        int invalidRows,
        List<CsvValidationRowError> rowErrors,
        List<CsvValidationRowNote> rowNotes,
        List<CsvValidationRowSourceSystem> rowSourceSystems) {

    public CsvValidationReport(int totalRows, int validRows, int invalidRows, List<CsvValidationRowError> rowErrors) {
        this(totalRows, validRows, invalidRows, rowErrors, List.of(), List.of());
    }

    public CsvValidationReport(int totalRows, int validRows, int invalidRows, List<CsvValidationRowError> rowErrors,
                              List<CsvValidationRowNote> rowNotes) {
        this(totalRows, validRows, invalidRows, rowErrors, rowNotes, List.of());
    }

    public CsvValidationReport {
        rowErrors = List.copyOf(rowErrors);
        rowNotes = List.copyOf(rowNotes);
        rowSourceSystems = List.copyOf(rowSourceSystems);
    }
}
