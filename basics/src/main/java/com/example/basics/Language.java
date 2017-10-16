package com.example.basics;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public interface Language {
    String getTag();

    String getCode();

    String getName();

    String getVariant();

    String getVariantName();

    String getScript();

    String getScriptName();
}
