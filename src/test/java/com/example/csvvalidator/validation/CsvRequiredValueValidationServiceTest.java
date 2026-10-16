package com.example.csvvalidator.validation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.junit.jupiter.api.Test;

class CsvRequiredValueValidationServiceTest {

    private final CsvRequiredValueValidationService service = new CsvRequiredValueValidationService();

    @Test
    void rowWithEmptyAndWhitespaceOnlyRequiredValuesHasNamedErrors() throws Exception {
        List<RowMissingFieldError> errors = findErrors("""
                provider_id,provider_name,effective_date
                ,North Clinic,
                P-100,   ,2025-01-31
                """);

        assertEquals(2, errors.size());
        assertEquals(1, errors.get(0).rowNumber());
        assertEquals(List.of("provider_id", "effective_date"), errors.get(0).missingFields());
        assertEquals(2, errors.get(1).rowNumber());
        assertEquals(List.of("provider_name"), errors.get(1).missingFields());
    }

    @Test
    void requiredValuesWithNonWhitespaceCharactersAreNotReportedMissing() throws Exception {
        List<RowMissingFieldError> errors = findErrors("""
                provider_id,provider_name,effective_date
                P-100,North Clinic,2025-01-31
                P-101,A,2025-12-01
                """);

        assertTrue(errors.isEmpty());
    }

    @Test
    void missingTrailingValuesAreReportedAsMissing() throws Exception {
        List<RowMissingFieldError> errors = findErrors("""
                provider_id,provider_name,effective_date
                P-100,North Clinic
                """);

        assertEquals(1, errors.size());
        assertEquals(1, errors.get(0).rowNumber());
        assertEquals(List.of("effective_date"), errors.get(0).missingFields());
    }

    private List<RowMissingFieldError> findErrors(String csv) throws Exception {
        return service.findMissingRequiredValues(new ByteArrayInputStream(csv.getBytes(StandardCharsets.UTF_8)));
    }
}