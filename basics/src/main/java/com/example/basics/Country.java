package com.example.basics;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public interface Country {
    String getCode();

    String getISO3Code();

    String getName();

    Currency getCurrency();
}
