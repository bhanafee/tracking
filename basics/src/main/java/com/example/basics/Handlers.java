package com.example.basics;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.nio.charset.Charset;
import java.util.*;
import java.util.regex.Pattern;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.BodyInserters.fromObject;

@Component
public class Handlers {

    private static final MediaType JSON_UTF8 = new MediaType(APPLICATION_JSON, Charset.forName("UTF-8"));

    private static final Collection<Locale> AVAILABLE_LOCALES = Arrays.asList(Locale.getAvailableLocales());

    private static final Collection<java.util.Currency> AVAILABLE_CURRENCIES = java.util.Currency.getAvailableCurrencies();

    private static final Pattern Numeric = Pattern.compile("\\d{1,3}");

    private String extractCode(ServerRequest request) {
        return request.pathVariable("code");
    }

    private Locale extractLocalization(ServerRequest request) {
        final List<Locale.LanguageRange> requested = request.headers().acceptLanguage();
        final Locale matched = Locale.lookup(requested, AVAILABLE_LOCALES);
        return matched == null ? Locale.getDefault(Locale.Category.DISPLAY) : matched;
    }

    private <T> Mono<ServerResponse> respond(@SuppressWarnings("OptionalUsedAsFieldOrParameterType") Optional<T> info) {
        if (info.isPresent()) {
            return Mono.just(info)
                    .flatMap(i -> ServerResponse.ok().contentType(JSON_UTF8).body(fromObject(i)));

        } else {
            return ServerResponse.notFound().build();
        }
    }

    public Mono<ServerResponse> getCountry(ServerRequest request) {
        final Locale locale = extractLocalization(request);
        final String code = extractCode(request).toUpperCase(Locale.US);

        Optional<Country> country = Optional.empty();
        for (Locale c : AVAILABLE_LOCALES) {
            try {
                if (code.equalsIgnoreCase((c.getCountry()))) {
                    country = Optional.of(new CountryFromJRE(c, locale));
                    break;
                }
            } catch (MissingResourceException e) {
                // EMPTY
            }
            try {
                if (code.equalsIgnoreCase((c.getISO3Country()))) {
                    country = Optional.of(new CountryFromJRE(c, locale));
                    break;
                }
            } catch (MissingResourceException e) {
                // EMPTY
            }
        }

        return respond(country);
    }

    public Mono<ServerResponse> getCurrency(ServerRequest request) {
        final Locale locale = extractLocalization(request);
        final String code = extractCode(request).toUpperCase(Locale.US);

        Optional<Currency> currency = Optional.empty();
        if (Numeric.matcher(code).matches()) {
            int numeric = Integer.parseInt(code);
            for (java.util.Currency c : AVAILABLE_CURRENCIES) {
                if (c.getNumericCode() == numeric) {
                    currency = Optional.of(new CurrencyFromJRE(c, locale));
                    break;
                }
            }
        } else try {
            currency = Optional.of(new CurrencyFromJRE(java.util.Currency.getInstance(code), locale));
        } catch (final IllegalArgumentException e) {
            // EMPTY
        }

        return respond(currency);
    }

    public Mono<ServerResponse> getLanguage(ServerRequest request) {
        final Locale locale = extractLocalization(request);
        final String code = extractCode(request);

        java.util.Locale byTag = Locale.forLanguageTag(code);
        Optional<Language> language = Optional.ofNullable(byTag == null ? null : new LanguageFromJRE(byTag, locale));

        return respond(language);
    }

    private static class CountryFromJRE implements Country {
        private Locale wrapped;

        private Locale localized;

        CountryFromJRE(Locale wrapped, Locale localized) {
            this.wrapped = wrapped;
            this.localized = localized;
        }

        @Override
        public String getCode() {
            try {
                return wrapped.getCountry();
            } catch (MissingResourceException e) {
                return null;
            }
        }

        @Override
        public String getISO3Code() {
            try {
                return wrapped.getISO3Country();
            } catch (MissingResourceException e) {
                return null;
            }
        }

        @Override
        public String getName() {
            return wrapped.getDisplayCountry(localized);
        }

        @Override
        public Currency getCurrency() {
            try {
                final java.util.Currency fromJRE = java.util.Currency.getInstance(wrapped);
                return fromJRE == null ? null : new CurrencyFromJRE(fromJRE, localized);
            } catch (IllegalArgumentException e) {
                // java.util.Currency.getInstance blows up if it doesn't recognize getCountry() as ISO-3166
                return null;
            }
        }
    }

    private static class CurrencyFromJRE implements Currency {
        private java.util.Currency wrapped;

        private Locale localized;

        CurrencyFromJRE(java.util.Currency wrapped, Locale localized) {
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

    private static class LanguageFromJRE implements Language {
        private Locale wrapped;

        private Locale localized;

        LanguageFromJRE(Locale wrapped, Locale localized) {
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

}
