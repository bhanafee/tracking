package com.maybeitssquid.basics;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Describe the characteristics of a country.
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public interface Country {
    /**
     * Gets the ISO 3166-1 Alpha-2 code for this country.
     *
     * @return the alpha-2 code
     */
    String getCode();

    /**
     * Gets the ISO 3166-1 Alpha-3 code for this country.
     *
     * @return the alpha-3 code
     */
    String getISO3Code();

    /**
     * Gets the short name of this country.
     *
     * @return the name
     */
    String getName();

    /**
     * Gets information about the currency used by this country.
     *
     * @return the currency
     */
    Currency getCurrency();
}
