package com.example.csvvalidator.validation;

import java.util.List;

public record RowMissingFieldError(int rowNumber, List<String> missingFields) {

    public RowMissingFieldError {
        missingFields = List.copyOf(missingFields);
    }
}