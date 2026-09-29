package com.example.csvvalidator.validation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.junit.jupiter.api.Test;

class CsvDuplicateProviderIdValidationServiceTest {

    private final CsvDuplicateProviderIdValidationService service = new CsvDuplicateProviderIdValidationService();

    @Test
    void idsWithOnlySurroundingWhitespaceDifferencesAreComparedAsTrimmedValues() throws Exception {
        List<RowDuplicateIdError> errors = findErrors("""
                provider_id,provider_name,effective_date
                P-100,North Clinic,2025-01-31
                  P-100   ,South Clinic,2025-02-01
                """);

        assertEquals(List.of(1, 2), errors.stream().map(RowDuplicateIdError::rowNumber).toList());
    }

    @Test
    void everyRowInDuplicateGroupHasDuplicateErrorWhenCaseMatches() throws Exception {
        List<RowDuplicateIdError> errors = findErrors("""
                provider_id,provider_name,effective_date
                P-100,North Clinic,2025-01-31
                P-200,East Clinic,2025-01-31
                P-100,South Clinic,2025-02-01
                P-100,West Clinic,2025-03-01
                """);

        assertEquals(List.of(1, 3, 4), errors.stream().map(RowDuplicateIdError::rowNumber).toList());
    }

    @Test
    void idsThatDifferOnlyByLetterCaseAreTreatedAsDistinct() throws Exception {
        List<RowDuplicateIdError> errors = findErrors("""
                provider_id,provider_name,effective_date
                P-100,North Clinic,2025-01-31
                p-100,South Clinic,2025-02-01
                """);

        assertTrue(errors.isEmpty());
    }

    private List<RowDuplicateIdError> findErrors(String csv) throws Exception {
        return service.findDuplicateProviderIds(new ByteArrayInputStream(csv.getBytes(StandardCharsets.UTF_8)));
    }
}
