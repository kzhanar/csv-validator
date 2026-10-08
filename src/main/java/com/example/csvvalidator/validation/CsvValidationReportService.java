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

        int totalRows = countRows(csvBytes);
        List<CsvValidationRowNote> rowNotes = readRowNotes(csvBytes);
        List<CsvValidationRowSourceSystem> rowSourceSystems = readRowSourceSystems(csvBytes);
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

        return new CsvValidationReport(totalRows, validRows, invalidRows, rowErrors, rowNotes, rowSourceSystems);
    }

    private List<CsvValidationRowNote> readRowNotes(byte[] csvBytes) throws IOException {
        List<CsvValidationRowNote> rowNotes = new ArrayList<>();
        try (Reader reader = new InputStreamReader(new ByteArrayInputStream(csvBytes), StandardCharsets.UTF_8);
             CSVParser parser = CSVFormat.DEFAULT.builder().setHeader().setSkipHeaderRecord(true).build().parse(reader)) {
            Map<String, Integer> headers = parser.getHeaderMap();
            if (headers == null || !headers.containsKey("notes")) {
                return List.of();
            }

            int rowNumber = 1;
            int notesColumn = headers.get("notes");
            for (CSVRecord record : parser) {
                String notes = notesColumn < record.size() ? record.get(notesColumn) : "";
                rowNotes.add(new CsvValidationRowNote(rowNumber++, notes));
            }
        }
        return rowNotes;
    }

    private List<CsvValidationRowSourceSystem> readRowSourceSystems(byte[] csvBytes) throws IOException {
        List<CsvValidationRowSourceSystem> rowSourceSystems = new ArrayList<>();
        try (Reader reader = new InputStreamReader(new ByteArrayInputStream(csvBytes), StandardCharsets.UTF_8);
             CSVParser parser = CSVFormat.DEFAULT.builder().setHeader().setSkipHeaderRecord(true).build().parse(reader)) {
            Map<String, Integer> headers = parser.getHeaderMap();
            if (headers == null || !headers.containsKey("source_system")) {
                return List.of();
            }

            int rowNumber = 1;
            int sourceSystemColumn = headers.get("source_system");
            for (CSVRecord record : parser) {
                String sourceSystem = sourceSystemColumn < record.size() ? record.get(sourceSystemColumn) : "";
                rowSourceSystems.add(new CsvValidationRowSourceSystem(rowNumber++, sourceSystem));
            }
        }
        return rowSourceSystems;
    }

    private int countRows(byte[] csvBytes) throws IOException {
        int count = 0;
        try (Reader reader = new InputStreamReader(new ByteArrayInputStream(csvBytes), StandardCharsets.UTF_8);
             CSVParser parser = CSVFormat.DEFAULT.builder().setHeader().setSkipHeaderRecord(true).build().parse(reader)) {
            for (CSVRecord ignored : parser) {
                count++;
            }
        }
        return count;
    }

    private void addReason(Map<Integer, List<String>> rowReasons, int rowNumber, String reason) {
        rowReasons.computeIfAbsent(rowNumber, ignored -> new ArrayList<>()).add(reason);
    }
}
