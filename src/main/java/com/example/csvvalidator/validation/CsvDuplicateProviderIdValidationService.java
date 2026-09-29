package com.example.csvvalidator.validation;

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
public class CsvDuplicateProviderIdValidationService {

    public List<RowDuplicateIdError> findDuplicateProviderIds(InputStream csvInput) throws IOException {
        List<RowDuplicateIdError> errors = new ArrayList<>();
        Map<String, List<Integer>> rowNumbersByTrimmedId = new LinkedHashMap<>();

        try (Reader reader = new InputStreamReader(csvInput, StandardCharsets.UTF_8);
             CSVParser parser = CSVFormat.DEFAULT.builder().setHeader().setSkipHeaderRecord(true).build().parse(reader)) {

            int rowNumber = 1;
            for (CSVRecord record : parser) {
                String providerId = record.get("provider_id");
                if (!isBlank(providerId)) {
                    String trimmedId = providerId.trim();
                    rowNumbersByTrimmedId.computeIfAbsent(trimmedId, ignored -> new ArrayList<>()).add(rowNumber);
                }
                rowNumber++;
            }
        }

        for (List<Integer> duplicateRows : rowNumbersByTrimmedId.values()) {
            if (duplicateRows.size() > 1) {
                for (Integer rowNumber : duplicateRows) {
                    errors.add(new RowDuplicateIdError(rowNumber));
                }
            }
        }

        return errors;
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
