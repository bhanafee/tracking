package com.example.basics;

import java.util.Locale;

public class LanguageFromJRE implements Language {
    private Locale wrapped;

    private Locale localized;

    public LanguageFromJRE(Locale wrapped) {
        this(wrapped, Locale.getDefault(Locale.Category.DISPLAY));
    }

    public LanguageFromJRE(Locale wrapped, Locale localized) {
        this.wrapped = wrapped;
        this.localized = localized;
    }

    @Override
    public String getTag() {
        return wrapped.toLanguageTag();
    }

    @Override
    public String getCode() {
        return wrapped.getLanguage();
    }

    @Override
    public String getName() {
        return wrapped.getDisplayName(localized);
    }

    @Override
    public String getVariant() {
        return wrapped.getVariant();
    }

    @Override
    public String getVariantName() {
        return wrapped.getDisplayVariant(localized);
    }

    @Override
    public String getScript() {
        return wrapped.getScript();
    }

    @Override
    public String getScriptName() {
        return wrapped.getDisplayScript(localized);
    }
}
