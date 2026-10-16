package com.example.csvvalidator.validation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.junit.jupiter.api.Test;

class CsvDateValidationServiceTest {

    private final CsvDateValidationService service = new CsvDateValidationService();

    @Test
    void realCalendarDatesInStrictYyyyMmDdFormatPassDateValidation() throws Exception {
        List<RowDateError> errors = findErrors("""
                provider_id,provider_name,effective_date
                P-100,North Clinic,2025-01-31
                P-101,South Clinic,2024-02-29
                """);

        assertTrue(errors.isEmpty());
    }

    @Test
    void malformedDatesAreReportedAsDateErrors() throws Exception {
        List<RowDateError> errors = findErrors("""
                provider_id,provider_name,effective_date
                P-100,North Clinic,2025/01/31
                P-101,South Clinic,2025-2-03
                P-102,East Clinic,2025-01-02T00:00:00
                """);

            assertEquals(3, errors.size());
        assertEquals(1, errors.get(0).rowNumber());
        assertEquals(2, errors.get(1).rowNumber());
            assertEquals(3, errors.get(2).rowNumber());
    }

    @Test
    void impossibleCalendarDatesAreReportedAsDateErrors() throws Exception {
        List<RowDateError> errors = findErrors("""
                provider_id,provider_name,effective_date
                P-100,North Clinic,2025-02-30
                """);

        assertEquals(1, errors.size());
        assertEquals(1, errors.get(0).rowNumber());
    }

    private List<RowDateError> findErrors(String csv) throws Exception {
        return service.findInvalidEffectiveDates(new ByteArrayInputStream(csv.getBytes(StandardCharsets.UTF_8)));
    }
}
