package com.example.csvvalidator.validation;

import java.util.List;

public record CsvValidationReport(int totalRows, int validRows, int invalidRows, List<CsvValidationRowError> rowErrors) {

    public CsvValidationReport {
        rowErrors = List.copyOf(rowErrors);
    }
}
