package com.example.basics;

import java.util.Locale;

public class Country {
    private Locale locale;

    private java.util.Currency currency;

    private Locale localized;

    public Country(final Locale locale) {
        this(locale, Locale.getDefault(Locale.Category.DISPLAY));
    }

    public Country (final Locale locale, final Locale localized) {
        this.locale = locale;
        this.currency = java.util.Currency.getInstance(locale);
        this.localized = localized;
    }

    public String getCode() {
        return locale.getCountry();
    }

    public String getName() {
        return locale.getDisplayCountry(localized);
    }

    public Currency getCurrency() {
        return new Currency() {

            @Override
            public String getCode() {
                return currency.getCurrencyCode();
            }

            @Override
            public String getNumericCode() {
                return String.format("%03d", currency.getNumericCode());
            }

            @Override
            public String getName() {
                return currency.getDisplayName(localized);
            }

            @Override
            public String getSymbol() {
                return currency.getSymbol(localized);
            }

            @Override
            public int getDigits() {
                return currency.getDefaultFractionDigits();
            }
        };
    }
}
