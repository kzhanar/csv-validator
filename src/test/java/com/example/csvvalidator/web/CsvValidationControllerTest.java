package com.example.csvvalidator.web;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

import com.example.csvvalidator.validation.CsvValidationReport;
import com.example.csvvalidator.validation.CsvValidationReportService;
import com.example.csvvalidator.validation.CsvValidationRowError;
import com.example.csvvalidator.validation.CsvStructureValidationResult;
import com.example.csvvalidator.validation.CsvStructureValidationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(OutputCaptureExtension.class)
@WebMvcTest(CsvValidationController.class)
class CsvValidationControllerTest {

    @Autowired
    private MockMvc mockMvc;

        @MockBean
        private CsvStructureValidationService csvStructureValidationService;

        @MockBean
        private CsvValidationReportService csvValidationReportService;

    @Test
    void acceptsNonEmptyCsvWithoutRelyingOnBrowserContentType() throws Exception {
        when(csvStructureValidationService.validate(any(InputStream.class)))
                .thenReturn(CsvStructureValidationResult.ok());
        when(csvValidationReportService.createReport(any(InputStream.class)))
                .thenReturn(new CsvValidationReport(1, 1, 0, Collections.emptyList()));

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "providers.csv",
                MediaType.TEXT_PLAIN_VALUE,
                "provider upload".getBytes(StandardCharsets.UTF_8));

        mockMvc.perform(multipart("/validate").file(file))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attribute("totalRows", 1))
                .andExpect(model().attribute("validRows", 1))
                .andExpect(model().attribute("invalidRows", 0))
                .andExpect(model().attribute("successMessage", "CSV upload received."))
                .andExpect(content().string(containsString("Total rows: 1")))
                .andExpect(content().string(containsString("Valid rows: 1")))
                .andExpect(content().string(containsString("Invalid rows: 0")))
                .andExpect(content().string(containsString("CSV file")))
                .andExpect(content().string(containsString("Submit")))
                .andExpect(content().string(containsString("CSV upload received.")));
    }

    @Test
    void rejectsNonCsvFilenameEvenWhenBrowserClaimsCsvContentType() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "providers.txt",
                "text/csv",
                "provider upload".getBytes(StandardCharsets.UTF_8));

        mockMvc.perform(multipart("/validate").file(file))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attribute("fileError",
                        "Only files with a .csv filename extension are accepted."))
                .andExpect(content().string(containsString("Only files with a .csv filename extension are accepted.")));
    }

    @Test
    void rejectsRequestWithoutAFile() throws Exception {
        mockMvc.perform(multipart("/validate"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attribute("fileError", "Select a non-empty CSV file to upload."))
                .andExpect(content().string(containsString("Select a non-empty CSV file to upload.")));
    }

    @Test
    void rejectsEmptyCsvUpload() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "providers.csv", "text/csv", new byte[0]);

        mockMvc.perform(multipart("/validate").file(file))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attribute("fileError", "Select a non-empty CSV file to upload."));
    }

    @Test
    void acceptsUppercaseCsvExtension() throws Exception {
        when(csvStructureValidationService.validate(any(InputStream.class)))
                .thenReturn(CsvStructureValidationResult.ok());
        when(csvValidationReportService.createReport(any(InputStream.class)))
                .thenReturn(new CsvValidationReport(1, 1, 0, Collections.emptyList()));

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "providers.CSV",
                MediaType.TEXT_PLAIN_VALUE,
                "provider upload".getBytes(StandardCharsets.UTF_8));

        mockMvc.perform(multipart("/validate").file(file))
                .andExpect(status().isOk())
                .andExpect(model().attribute("totalRows", 1))
                .andExpect(model().attribute("validRows", 1))
                .andExpect(model().attribute("invalidRows", 0))
                .andExpect(model().attribute("successMessage", "CSV upload received."));
    }

    @Test
    void returnsValidationReportWithRowReasonsWhenRowsAreInvalid() throws Exception {
        when(csvStructureValidationService.validate(any(InputStream.class)))
                .thenReturn(CsvStructureValidationResult.ok());
        when(csvValidationReportService.createReport(any(InputStream.class)))
                .thenReturn(new CsvValidationReport(
                        3,
                        1,
                        2,
                        List.of(new CsvValidationRowError(1, List.of("missing provider_id", "duplicate provider_id.")),
                                new CsvValidationRowError(3, List.of("invalid effective_date (YYYY-MM-DD required).")))));

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "providers.csv",
                MediaType.TEXT_PLAIN_VALUE,
                "provider_id,provider_name,effective_date\n,North Clinic,".getBytes(StandardCharsets.UTF_8));

        mockMvc.perform(multipart("/validate").file(file))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attribute("totalRows", 3))
                .andExpect(model().attribute("validRows", 1))
                .andExpect(model().attribute("invalidRows", 2))
                .andExpect(model().attributeExists("validationRowErrors"))
                .andExpect(content().string(containsString("Total rows: 3")))
                .andExpect(content().string(containsString("Valid rows: 1")))
                .andExpect(content().string(containsString("Invalid rows: 2")))
                .andExpect(content().string(containsString("Row 1: missing provider_id; duplicate provider_id.")))
                .andExpect(content().string(containsString("Row 3: invalid effective_date (YYYY-MM-DD required).")));
    }

    @Test
    void rejectsCsvWithMissingRequiredHeaders() throws Exception {
        when(csvStructureValidationService.validate(any(InputStream.class))).thenReturn(
                CsvStructureValidationResult.missingHeaders(List.of("provider_name", "effective_date")));

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "providers.csv",
                MediaType.TEXT_PLAIN_VALUE,
                "provider_id\nP-100".getBytes(StandardCharsets.UTF_8));

        mockMvc.perform(multipart("/validate").file(file))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attribute("fileError",
                        "Missing required column(s): provider_name, effective_date"))
                .andExpect(model().attributeDoesNotExist("totalRows"))
                .andExpect(model().attributeDoesNotExist("validRows"))
                .andExpect(model().attributeDoesNotExist("invalidRows"))
                .andExpect(model().attributeDoesNotExist("validationRowErrors"))
                .andExpect(content().string(containsString("Missing required column(s): provider_name, effective_date")))
                .andExpect(content().string(org.hamcrest.Matchers.not(containsString("Validation Report"))))
                .andExpect(content().string(containsString("Missing required column(s): provider_name, effective_date")));
    }

    @Test
    void reportWithAllValidRowsHasZeroInvalidRowsAndNoRowErrors() throws Exception {
        when(csvStructureValidationService.validate(any(InputStream.class)))
                .thenReturn(CsvStructureValidationResult.ok());
        when(csvValidationReportService.createReport(any(InputStream.class)))
                .thenReturn(new CsvValidationReport(2, 2, 0, Collections.emptyList()));

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "providers.csv",
                MediaType.TEXT_PLAIN_VALUE,
                "provider_id,provider_name,effective_date\nP-100,North Clinic,2025-02-30"
                        .getBytes(StandardCharsets.UTF_8));

        mockMvc.perform(multipart("/validate").file(file))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attribute("totalRows", 2))
                .andExpect(model().attribute("validRows", 2))
                .andExpect(model().attribute("invalidRows", 0))
                .andExpect(model().attribute("validationRowErrors", Collections.emptyList()));
    }

    @Test
    void doesNotRetainValidationReportHistoryAcrossRequests() throws Exception {
        when(csvStructureValidationService.validate(any(InputStream.class)))
                .thenReturn(CsvStructureValidationResult.ok());
        when(csvValidationReportService.createReport(any(InputStream.class)))
                .thenReturn(new CsvValidationReport(
                        1,
                        0,
                        1,
                        List.of(new CsvValidationRowError(1, List.of("duplicate provider_id.")))))
                .thenReturn(new CsvValidationReport(1, 1, 0, Collections.emptyList()));

        MockMultipartFile firstFile = new MockMultipartFile(
                "file",
                "providers.csv",
                MediaType.TEXT_PLAIN_VALUE,
                "provider_id,provider_name,effective_date\nP-100,North Clinic,2025-01-31"
                        .getBytes(StandardCharsets.UTF_8));
        MockMultipartFile secondFile = new MockMultipartFile(
                "file",
                "providers.csv",
                MediaType.TEXT_PLAIN_VALUE,
                "provider_id,provider_name,effective_date\nP-200,West Clinic,2025-01-31"
                        .getBytes(StandardCharsets.UTF_8));

        mockMvc.perform(multipart("/validate").file(firstFile))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Row 1: duplicate provider_id.")));

        mockMvc.perform(multipart("/validate").file(secondFile))
                .andExpect(status().isOk())
                .andExpect(model().attribute("invalidRows", 0))
                .andExpect(content().string(org.hamcrest.Matchers.not(containsString("Row 1: duplicate provider_id."))));
    }

    @Test
    void doesNotLogUploadedCsvContents(CapturedOutput output) throws Exception {
        String sensitiveToken = "PRIVATE_PROVIDER_TOKEN_12345";
        when(csvStructureValidationService.validate(any(InputStream.class)))
                .thenReturn(CsvStructureValidationResult.ok());
        when(csvValidationReportService.createReport(any(InputStream.class)))
                .thenReturn(new CsvValidationReport(1, 1, 0, Collections.emptyList()));

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "providers.csv",
                MediaType.TEXT_PLAIN_VALUE,
                ("provider_id,provider_name,effective_date\n" + sensitiveToken + ",North Clinic,2025-01-31")
                        .getBytes(StandardCharsets.UTF_8));

        mockMvc.perform(multipart("/validate").file(file))
                .andExpect(status().isOk());

        assertThat(output.getOut()).doesNotContain(sensitiveToken);
        assertThat(output.getErr()).doesNotContain(sensitiveToken);
    }
}