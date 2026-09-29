package com.example.csvvalidator.validation;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;

@Service
public class CsvStructureValidationService {

    private static final List<String> REQUIRED_HEADERS = List.of("provider_id", "provider_name", "effective_date");

    public CsvStructureValidationResult validate(InputStream csvInput) throws IOException {
        try (Reader reader = new InputStreamReader(csvInput, StandardCharsets.UTF_8);
             CSVParser parser = CSVFormat.DEFAULT.builder().setHeader().setSkipHeaderRecord(true).build().parse(reader)) {

            Map<String, Integer> headerMap = parser.getHeaderMap();
            List<String> missingHeaders = REQUIRED_HEADERS.stream()
                    .filter(requiredHeader -> !headerMap.containsKey(requiredHeader))
                    .toList();

            if (!missingHeaders.isEmpty()) {
                return CsvStructureValidationResult.missingHeaders(missingHeaders);
            }

            // Consume records so CSV syntax is checked while validating structure.
            for (CSVRecord ignored : parser) {
                // No-op.
            }

            return CsvStructureValidationResult.ok();
        }
    }
}