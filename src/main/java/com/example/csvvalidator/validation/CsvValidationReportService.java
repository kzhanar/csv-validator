package com.example.csvvalidator.validation;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;

@Service
public class CsvValidationReportService {

    private static final String SOURCE_SYSTEM_HEADER = "source_system";

    private final CsvRequiredValueValidationService csvRequiredValueValidationService;
    private final CsvDateValidationService csvDateValidationService;
    private final CsvDuplicateProviderIdValidationService csvDuplicateProviderIdValidationService;

    public CsvValidationReportService(CsvRequiredValueValidationService csvRequiredValueValidationService,
                                      CsvDateValidationService csvDateValidationService,
                                      CsvDuplicateProviderIdValidationService csvDuplicateProviderIdValidationService) {
        this.csvRequiredValueValidationService = csvRequiredValueValidationService;
        this.csvDateValidationService = csvDateValidationService;
        this.csvDuplicateProviderIdValidationService = csvDuplicateProviderIdValidationService;
    }

    public CsvValidationReport createReport(InputStream csvInput) throws IOException {
        byte[] csvBytes = csvInput.readAllBytes();

        CsvRows csvRows = readRows(csvBytes);
        int totalRows = csvRows.rows().size();
        Map<Integer, List<String>> rowReasons = new LinkedHashMap<>();

        List<RowMissingFieldError> missingFieldErrors =
                csvRequiredValueValidationService.findMissingRequiredValues(new ByteArrayInputStream(csvBytes));
        for (RowMissingFieldError error : missingFieldErrors) {
            addReason(rowReasons, error.rowNumber(), "missing " + String.join(", ", error.missingFields()));
        }

        List<RowDateError> dateErrors =
                csvDateValidationService.findInvalidEffectiveDates(new ByteArrayInputStream(csvBytes));
        for (RowDateError error : dateErrors) {
            addReason(rowReasons, error.rowNumber(), "invalid effective_date (YYYY-MM-DD required).");
        }

        List<RowDuplicateIdError> duplicateIdErrors =
                csvDuplicateProviderIdValidationService.findDuplicateProviderIds(new ByteArrayInputStream(csvBytes));
        for (RowDuplicateIdError error : duplicateIdErrors) {
            addReason(rowReasons, error.rowNumber(), "duplicate provider_id.");
        }

        List<CsvValidationRowError> rowErrors = rowReasons.entrySet().stream()
                .map(entry -> new CsvValidationRowError(entry.getKey(), entry.getValue()))
                .toList();

        int invalidRows = rowErrors.size();
        int validRows = totalRows - invalidRows;

        return new CsvValidationReport(totalRows, validRows, invalidRows, rowErrors,
                csvRows.rows(), csvRows.sourceSystemPresent());
    }

    private CsvRows readRows(byte[] csvBytes) throws IOException {
        List<CsvValidationReportRow> rows = new ArrayList<>();
        try (Reader reader = new InputStreamReader(new ByteArrayInputStream(csvBytes), StandardCharsets.UTF_8);
             CSVParser parser = CSVFormat.DEFAULT.builder().setHeader().setSkipHeaderRecord(true).build().parse(reader)) {
            boolean sourceSystemPresent = parser.getHeaderMap().containsKey(SOURCE_SYSTEM_HEADER);
            int rowNumber = 1;
            for (CSVRecord ignored : parser) {
                String sourceSystem = sourceSystemPresent ? ignored.get(SOURCE_SYSTEM_HEADER) : null;
                rows.add(new CsvValidationReportRow(rowNumber, sourceSystem));
                rowNumber++;
            }
            return new CsvRows(rows, sourceSystemPresent);
        }
    }

    private void addReason(Map<Integer, List<String>> rowReasons, int rowNumber, String reason) {
        rowReasons.computeIfAbsent(rowNumber, ignored -> new ArrayList<>()).add(reason);
    }

    private record CsvRows(List<CsvValidationReportRow> rows, boolean sourceSystemPresent) {
    }
}
