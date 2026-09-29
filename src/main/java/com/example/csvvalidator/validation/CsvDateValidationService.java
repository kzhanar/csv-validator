package com.example.csvvalidator.validation;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;

@Service
public class CsvDateValidationService {

    private static final DateTimeFormatter STRICT_YYYY_MM_DD =
            DateTimeFormatter.ofPattern("uuuu-MM-dd").withResolverStyle(ResolverStyle.STRICT);

    public List<RowDateError> findInvalidEffectiveDates(InputStream csvInput) throws IOException {
        List<RowDateError> errors = new ArrayList<>();
        try (Reader reader = new InputStreamReader(csvInput, StandardCharsets.UTF_8);
             CSVParser parser = CSVFormat.DEFAULT.builder().setHeader().setSkipHeaderRecord(true).build().parse(reader)) {

            int rowNumber = 1;
            for (CSVRecord record : parser) {
                String effectiveDate = record.get("effective_date");
                if (!isBlank(effectiveDate) && !isStrictValidDate(effectiveDate)) {
                    errors.add(new RowDateError(rowNumber));
                }
                rowNumber++;
            }
        }
        return errors;
    }

    private boolean isStrictValidDate(String value) {
        try {
            LocalDate.parse(value, STRICT_YYYY_MM_DD);
            return true;
        } catch (DateTimeParseException ex) {
            return false;
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
