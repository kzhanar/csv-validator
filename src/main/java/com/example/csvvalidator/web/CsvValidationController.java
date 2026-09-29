package com.example.csvvalidator.web;

import java.util.Locale;
import java.util.StringJoiner;

import com.example.csvvalidator.validation.CsvValidationReport;
import com.example.csvvalidator.validation.CsvValidationReportService;
import com.example.csvvalidator.validation.CsvStructureValidationResult;
import com.example.csvvalidator.validation.CsvStructureValidationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class CsvValidationController {

    private final CsvStructureValidationService csvStructureValidationService;
    private final CsvValidationReportService csvValidationReportService;

    public CsvValidationController(CsvStructureValidationService csvStructureValidationService,
                                   CsvValidationReportService csvValidationReportService) {
        this.csvStructureValidationService = csvStructureValidationService;
        this.csvValidationReportService = csvValidationReportService;
    }

    @GetMapping("/")
    public String uploadPage() {
        return "index";
    }

    @PostMapping("/validate")
    public String submitFile(@RequestParam(value = "file", required = false) MultipartFile file, Model model) {
        if (file == null || file.isEmpty()) {
            model.addAttribute("fileError", "Select a non-empty CSV file to upload.");
            return "index";
        }

        String filename = file.getOriginalFilename();
        if (filename == null || !filename.toLowerCase(Locale.ROOT).endsWith(".csv")) {
            model.addAttribute("fileError", "Only files with a .csv filename extension are accepted.");
            return "index";
        }

        try {
            CsvStructureValidationResult structureResult = csvStructureValidationService.validate(file.getInputStream());
            if (!structureResult.valid()) {
                StringJoiner joiner = new StringJoiner(", ");
                structureResult.missingRequiredHeaders().forEach(joiner::add);
                model.addAttribute("fileError", "Missing required column(s): " + joiner);
                return "index";
            }

            CsvValidationReport report = csvValidationReportService.createReport(file.getInputStream());
            model.addAttribute("totalRows", report.totalRows());
            model.addAttribute("validRows", report.validRows());
            model.addAttribute("invalidRows", report.invalidRows());
            model.addAttribute("validationRowErrors", report.rowErrors());

            if (report.invalidRows() == 0) {
                model.addAttribute("successMessage", "CSV upload received.");
            }
        } catch (Exception ex) {
            model.addAttribute("fileError", "Unable to read CSV file.");
            return "index";
        }
        return "index";
    }
}