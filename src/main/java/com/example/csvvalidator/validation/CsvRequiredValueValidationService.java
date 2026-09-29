package com.example.csvvalidator.validation;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;

@Service
public class CsvRequiredValueValidationService {

    private static final List<String> REQUIRED_HEADERS = List.of("provider_id", "provider_name", "effective_date");

    public List<RowMissingFieldError> findMissingRequiredValues(InputStream csvInput) throws IOException {
        List<RowMissingFieldError> errors = new ArrayList<>();
        try (Reader reader = new InputStreamReader(csvInput, StandardCharsets.UTF_8);
             CSVParser parser = CSVFormat.DEFAULT.builder().setHeader().setSkipHeaderRecord(true).build().parse(reader)) {

            int rowNumber = 1;
            for (CSVRecord record : parser) {
                List<String> missingFields = REQUIRED_HEADERS.stream()
                        .filter(header -> isBlank(record.get(header)))
                        .toList();

                if (!missingFields.isEmpty()) {
                    errors.add(new RowMissingFieldError(rowNumber, missingFields));
                }
                rowNumber++;
            }
        }
        return errors;
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}