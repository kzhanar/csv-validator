package com.example.csvvalidator.validation;

import java.util.List;

public record CsvStructureValidationResult(boolean valid, List<String> missingRequiredHeaders) {

    public static CsvStructureValidationResult ok() {
        return new CsvStructureValidationResult(true, List.of());
    }

    public static CsvStructureValidationResult missingHeaders(List<String> missingRequiredHeaders) {
        return new CsvStructureValidationResult(false, List.copyOf(missingRequiredHeaders));
    }
}