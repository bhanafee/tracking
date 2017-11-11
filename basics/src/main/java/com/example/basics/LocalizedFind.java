package com.example.basics;

import java.util.Locale;
import java.util.Optional;
import java.util.function.BiFunction;

/**
 * Performs a localized lookup.
 *
 * @param <R> the type value optionally returned by the function
 */
public interface LocalizedFind<R> extends BiFunction<String, Locale, Optional<R>> {
    // EMPTY
}
