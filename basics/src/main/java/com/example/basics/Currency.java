package com.example.basics;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public interface Currency {
    String getCode();

    String getNumericCode();

    String getName();

    String getSymbol();

    int getDigits();
}
