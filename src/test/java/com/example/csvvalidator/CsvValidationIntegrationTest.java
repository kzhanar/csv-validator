package com.example.csvvalidator;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.StandardCharsets;
import java.util.List;

import com.example.csvvalidator.validation.CsvValidationRowSourceSystem;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class CsvValidationIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void validatesAndRendersSourceSystemValuesForEveryRow() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "providers.csv",
                MediaType.TEXT_PLAIN_VALUE,
                ("provider_id,provider_name,effective_date,source_system\n"
                        + "P-100,North Clinic,2025-01-31,legacy-import\n"
                        + ",South Clinic,2025-02-28,partner-feed")
                        .getBytes(StandardCharsets.UTF_8));

        mockMvc.perform(multipart("/validate").file(file))
                .andExpect(status().isOk())
                .andExpect(model().attribute("totalRows", 2))
                .andExpect(model().attribute("validRows", 1))
                .andExpect(model().attribute("invalidRows", 1))
                .andExpect(model().attribute("validationRowSourceSystems", List.of(
                        new CsvValidationRowSourceSystem(1, "legacy-import"),
                        new CsvValidationRowSourceSystem(2, "partner-feed"))))
                .andExpect(content().string(containsString("Source system by row")))
                .andExpect(content().string(containsString("legacy-import")))
                .andExpect(content().string(containsString("partner-feed")))
                .andExpect(content().string(containsString("Row 2: missing provider_id")));
    }

    @Test
    void keepsReportWithoutSourceSystemSectionWhenColumnIsAbsent() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "providers.csv",
                MediaType.TEXT_PLAIN_VALUE,
                "provider_id,provider_name,effective_date\nP-100,North Clinic,2025-01-31"
                        .getBytes(StandardCharsets.UTF_8));

        mockMvc.perform(multipart("/validate").file(file))
                .andExpect(status().isOk())
                .andExpect(model().attribute("totalRows", 1))
                .andExpect(model().attribute("validationRowSourceSystems", List.of()))
                .andExpect(content().string(not(containsString("Source system by row"))));
    }
}
