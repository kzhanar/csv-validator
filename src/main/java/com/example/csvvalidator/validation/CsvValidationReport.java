package com.example.csvvalidator.validation;

import java.util.List;

public record CsvValidationReport(
        int totalRows,
        int validRows,
        int invalidRows,
        List<CsvValidationRowError> rowErrors,
        List<CsvValidationRowNote> rowNotes) {

    public CsvValidationReport(int totalRows, int validRows, int invalidRows, List<CsvValidationRowError> rowErrors) {
        this(totalRows, validRows, invalidRows, rowErrors, List.of());
    }

    public CsvValidationReport {
        rowErrors = List.copyOf(rowErrors);
        rowNotes = List.copyOf(rowNotes);
    }
}
