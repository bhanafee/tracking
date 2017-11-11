package com.example.basics;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Describe the characteristics of a currency.
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public interface Currency {
    /**
     * Gets the ISO 4217 code for this currency.
     *
     * @return the code
     */
    String getCode();

    /**
     * Gets the ISO 4217 numeric code for this currency.
     *
     * @return the numeric code
     */
    String getNumericCode();

    /**
     * Gets the name of this currency.
     *
     * @return the name
     */
    String getName();

    /**
     * Gets the symbol for this currency.
     *
     * @return the symbol
     */
    String getSymbol();

    /**
     * Gets the default number of fraction digits used with this currency.
     *
     * @return the number of fraction digits
     */
    int getDigits();
}
