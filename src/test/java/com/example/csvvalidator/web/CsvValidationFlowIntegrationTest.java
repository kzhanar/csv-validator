package com.example.csvvalidator.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class CsvValidationFlowIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void validCsvFlowsFromUploadToRenderedValidationReport() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "providers.csv",
                MediaType.TEXT_PLAIN_VALUE,
                "provider_id,provider_name,effective_date\nP-100,North Clinic,2025-01-31"
                        .getBytes(StandardCharsets.UTF_8));

        mockMvc.perform(multipart("/validate").file(file))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Total rows: 1")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Valid rows: 1")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Invalid rows: 0")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("CSV upload received.")));
    }
}