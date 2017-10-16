package com.example.basics;

import java.util.Locale;

public class CurrencyFromJRE implements Currency {
    private java.util.Currency wrapped;

    private Locale localized;

    public CurrencyFromJRE(java.util.Currency wrapped) {
        this(wrapped, Locale.getDefault(Locale.Category.DISPLAY));
    }

    public CurrencyFromJRE(java.util.Currency wrapped, Locale localized) {
        this.wrapped = wrapped;
        this.localized = localized;
    }

    @Override
    public String getCode() {
        return wrapped.getCurrencyCode();
    }

    @Override
    public String getNumericCode() {
        return wrapped.getNumericCodeAsString();
    }

    @Override
    public String getName() {
        return wrapped.getDisplayName(localized);
    }

    @Override
    public String getSymbol() {
        return wrapped.getSymbol(localized);
    }

    @Override
    public int getDigits() {
        return wrapped.getDefaultFractionDigits();
    }
}
