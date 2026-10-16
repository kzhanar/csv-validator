package com.example.csvvalidator.validation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.junit.jupiter.api.Test;

class CsvValidationReportServiceTest {

    private final CsvValidationReportService service = new CsvValidationReportService(
            new CsvRequiredValueValidationService(),
            new CsvDateValidationService(),
            new CsvDuplicateProviderIdValidationService());

    @Test
    void reportIncludesTotalsAndRowErrorsWithDataRowsStartingAtOneAfterHeader() throws Exception {
        CsvValidationReport report = createReport("""
                provider_id,provider_name,effective_date
                P-100,North Clinic,2025-01-31
                ,South Clinic,2025-02-28
                """);

        assertEquals(2, report.totalRows());
        assertEquals(1, report.validRows());
        assertEquals(1, report.invalidRows());
        assertEquals(1, report.rowErrors().size());
        assertEquals(2, report.rowErrors().get(0).rowNumber());
        assertEquals(List.of("missing provider_id"), report.rowErrors().get(0).reasons());
    }

    @Test
    void rowWithMultipleErrorsIsListedOnceWithEveryApplicableReason() throws Exception {
        CsvValidationReport report = createReport("""
                provider_id,provider_name,effective_date
                P-100,North Clinic,2025-01-31
                P-100,South Clinic,2025-02-30
                """);

        assertEquals(2, report.totalRows());
        assertEquals(0, report.validRows());
        assertEquals(2, report.invalidRows());

        CsvValidationRowError rowOne = report.rowErrors().stream()
                .filter(error -> error.rowNumber() == 1)
                .findFirst()
                .orElseThrow();
        CsvValidationRowError rowTwo = report.rowErrors().stream()
                .filter(error -> error.rowNumber() == 2)
                .findFirst()
                .orElseThrow();

        assertEquals(List.of("duplicate provider_id."), rowOne.reasons());
        assertEquals(List.of("invalid effective_date (YYYY-MM-DD required).", "duplicate provider_id."), rowTwo.reasons());
    }

        @Test
        void missingNameAndInvalidDateAreReportedTogetherOnOneRow() throws Exception {
                CsvValidationReport report = createReport("""
                                provider_id,provider_name,effective_date
                                P-100,   ,2025-02-30
                                """);

                assertEquals(1, report.totalRows());
                assertEquals(0, report.validRows());
                assertEquals(1, report.invalidRows());
                assertEquals(1, report.rowErrors().size());
                assertEquals(1, report.rowErrors().get(0).rowNumber());
                assertEquals(List.of("missing provider_name", "invalid effective_date (YYYY-MM-DD required)."),
                                report.rowErrors().get(0).reasons());
        }

    @Test
    void totalsAlwaysEqualValidPlusInvalidAndAllValidHasNoRowErrors() throws Exception {
        CsvValidationReport report = createReport("""
                provider_id,provider_name,effective_date
                P-100,North Clinic,2025-01-31
                P-101,South Clinic,2025-02-28
                """);

        assertEquals(report.totalRows(), report.validRows() + report.invalidRows());
        assertEquals(0, report.invalidRows());
        assertTrue(report.rowErrors().isEmpty());
    }

    private CsvValidationReport createReport(String csv) throws Exception {
        return service.createReport(new ByteArrayInputStream(csv.getBytes(StandardCharsets.UTF_8)));
    }
}
