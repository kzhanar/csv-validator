package com.example.csvvalidator.validation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;

class CsvStructureValidationServiceTest {

    private final CsvStructureValidationService service = new CsvStructureValidationService();

    @Test
    void requiredHeadersAllowRowValidation() throws Exception {
        CsvStructureValidationResult result = validate("""
                provider_id,provider_name,effective_date
                P-100,North Clinic,2025-01-31
                """);

        assertTrue(result.valid());
        assertTrue(result.missingRequiredHeaders().isEmpty());
    }

    @Test
    void extraColumnsAreIgnored() throws Exception {
        CsvStructureValidationResult result = validate("""
                provider_id,provider_name,effective_date,source_system
                P-100,North Clinic,2025-01-31,legacy
                """);

        assertTrue(result.valid());
    }

    @Test
    void quotedValuesWithCommasParseSuccessfully() throws Exception {
        CsvStructureValidationResult result = validate("""
                provider_id,provider_name,effective_date
                P-100,"North Clinic, East",2025-01-31
                """);

        assertTrue(result.valid());
    }

    @Test
    void utf8ValuesParseSuccessfully() throws Exception {
        CsvStructureValidationResult result = validate("""
                provider_id,provider_name,effective_date
                P-100,München Health,2025-01-31
                """);

        assertTrue(result.valid());
    }

    @Test
    void missingProviderIdIsReported() throws Exception {
        CsvStructureValidationResult result = validate("""
                provider_name,effective_date
                North Clinic,2025-01-31
                """);

        assertFalse(result.valid());
        assertEquals(1, result.missingRequiredHeaders().size());
        assertEquals("provider_id", result.missingRequiredHeaders().get(0));
    }

    @Test
    void multipleMissingHeadersAreReported() throws Exception {
        CsvStructureValidationResult result = validate("""
                legacy_code
                X-1
                """);

        assertFalse(result.valid());
        assertEquals(3, result.missingRequiredHeaders().size());
        assertTrue(result.missingRequiredHeaders().contains("provider_id"));
        assertTrue(result.missingRequiredHeaders().contains("provider_name"));
        assertTrue(result.missingRequiredHeaders().contains("effective_date"));
    }

    @Test
    void headerMatchingIsCaseSensitive() throws Exception {
        CsvStructureValidationResult result = validate("""
                Provider_ID,provider_name,effective_date
                P-100,North Clinic,2025-01-31
                """);

        assertFalse(result.valid());
        assertEquals(1, result.missingRequiredHeaders().size());
        assertEquals("provider_id", result.missingRequiredHeaders().get(0));
    }

    @Test
    void headerOnlyCsvIsStructurallyValid() throws Exception {
        CsvStructureValidationResult result = validate("""
                provider_id,provider_name,effective_date
                """);

        assertTrue(result.valid());
    }

    private CsvStructureValidationResult validate(String csv) throws Exception {
        return service.validate(new ByteArrayInputStream(csv.getBytes(StandardCharsets.UTF_8)));
    }
}