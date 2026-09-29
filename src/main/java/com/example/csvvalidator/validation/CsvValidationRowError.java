package com.example.csvvalidator.validation;

import java.util.List;

public record CsvValidationRowError(int rowNumber, List<String> reasons) {

    public CsvValidationRowError {
        reasons = List.copyOf(reasons);
    }
}
