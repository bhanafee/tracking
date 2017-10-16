package com.example.basics;

import java.util.Locale;

public class CountryFromJRE implements Country {
    private Locale wrapped;

    private Locale localized;

    public CountryFromJRE(Locale wrapped) {
        this(wrapped, Locale.getDefault(Locale.Category.DISPLAY));
    }

    public CountryFromJRE(Locale wrapped, Locale localized) {
        this.wrapped = wrapped;
        this.localized = localized;
    }

    @Override
    public String getCode() {
        return wrapped.getCountry();
    }

    @Override
    public String getISO3Code() {
        return wrapped.getISO3Country();
    }

    @Override
    public String getName() {
        return wrapped.getDisplayCountry(localized);
    }

    @Override
    public Currency getCurrency() {
        final java.util.Currency fromJRE = java.util.Currency.getInstance(wrapped);
        return fromJRE == null ? null : new CurrencyFromJRE(fromJRE, localized);
    }
}
