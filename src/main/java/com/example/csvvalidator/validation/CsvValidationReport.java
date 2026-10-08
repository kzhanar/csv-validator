package com.example.csvvalidator.validation;

import java.util.List;

public record CsvValidationReport(int totalRows, int validRows, int invalidRows,
                                  List<CsvValidationRowError> rowErrors,
                                  List<CsvValidationReportRow> rows,
                                  boolean sourceSystemPresent) {

    public CsvValidationReport {
        rowErrors = List.copyOf(rowErrors);
        rows = List.copyOf(rows);
    }

    public CsvValidationReport(int totalRows, int validRows, int invalidRows, List<CsvValidationRowError> rowErrors) {
        this(totalRows, validRows, invalidRows, rowErrors, List.of(), false);
    }
}
