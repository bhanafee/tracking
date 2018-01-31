package com.maybeitssquid.basics;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Describe the characteristics of a language.
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public interface Language {
    /**
     * Gets the IETF BCP 47 tag for this language.
     *
     * @return the language tag
     */
    String getTag();

    /**
     * Gets the ISO-639 code for this language.
     *
     * @return the code.
     */
    String getCode();

    /**
     * Gets the name of this language.
     *
     * @return the name
     */
    String getName();

    /**
     * Gets the variant code for this locale.
     *
     * @return the variant code
     */
    String getVariant();

    /**
     * Gets the variant name for this locale.
     *
     * @return the variant name
     */
    String getVariantName();

    /**
     * Gets the ISO 15924 4-letter script code.
     *
     * @return the script code
     */
    String getScript();

    /**
     * Gets the name of the script for this language.
     *
     * @return the name of the script
     */
    String getScriptName();
}
