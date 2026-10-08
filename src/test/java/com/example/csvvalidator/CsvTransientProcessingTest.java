package com.example.csvvalidator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.sql.DataSource;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class CsvTransientProcessingTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void applicationHasNoDatabaseDataSourceBean() {
        assertThat(applicationContext.getBeanProvider(DataSource.class).getIfAvailable()).isNull();
    }

    @Test
    void reportsNotesForEveryRowWithoutChangingRowValidity() throws Exception {
        String csv = "provider_id,provider_name,effective_date,notes\n"
                + "P-100,North Clinic,2025-01-31,review soon\n"
                + "P-101,South Clinic,2025-02-28,\n"
                + "P-102,West Clinic,2025-03-01,   \n"
                + ",East Clinic,2025-03-02,invalid row note\n";
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "providers.csv",
                MediaType.TEXT_PLAIN_VALUE,
                csv.getBytes(StandardCharsets.UTF_8));

        String report = mockMvc.perform(multipart("/validate").file(file))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThat(report)
                .contains("Total rows: 4")
                .contains("Valid rows: 3")
                .contains("Invalid rows: 1")
                .contains("Row 4: missing provider_id")
                .contains("Notes by row")
                .contains("review soon")
                .contains("invalid row note")
                .doesNotContain("Source system by row");
        assertThat(report)
                .containsPattern("(?s)<td>2</td>\\s*<td></td>")
                .containsPattern("(?s)<td>3</td>\\s*<td>\\s{3}</td>");
    }

    @Test
    void reportsSourceSystemForEveryRowWithoutChangingRowValidity() throws Exception {
        String csv = "provider_id,provider_name,effective_date,source_system\n"
                + "P-100,North Clinic,2025-01-31,legacy\n"
                + "P-101,South Clinic,2025-02-28,   \n"
                + "P-102,,2025-03-01,\n";
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "providers.csv",
                MediaType.TEXT_PLAIN_VALUE,
                csv.getBytes(StandardCharsets.UTF_8));

        String report = mockMvc.perform(multipart("/validate").file(file))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThat(report)
                .contains("Total rows: 3")
                .contains("Valid rows: 2")
                .contains("Invalid rows: 1")
                .contains("Row 3: missing provider_name")
                .contains("Source system by row")
                .contains("legacy");
        assertThat(report)
                .containsPattern("(?s)<td>1</td>\\s*<td>legacy</td>")
                .containsPattern("(?s)<td>2</td>\\s*<td>\\s{3}</td>")
                .containsPattern("(?s)<td>3</td>\\s*<td></td>");
    }
}
