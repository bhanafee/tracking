package com.example.basics;

import java.util.Locale;
import java.util.Optional;
import java.util.function.BiFunction;

public interface LocalizedFind<R> extends BiFunction<String, Locale, Optional<R>> {
    // EMPTY
}
