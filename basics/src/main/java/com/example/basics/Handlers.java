package com.example.basics;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.BodyInserters.fromObject;

@Component
public class Handlers {

    private static final Collection<Locale> AVAILABLE = Arrays.asList(Locale.getAvailableLocales());

    private String extractCode(ServerRequest request) {
        return request.pathVariable("code");
    }

    private Locale extractLocalization(ServerRequest request) {
        final List<Locale.LanguageRange> requested = request.headers().acceptLanguage();
        final Locale matched = Locale.lookup(requested, AVAILABLE);
        return matched == null ? Locale.getDefault(Locale.Category.DISPLAY) : matched;
    }

    private <T> Mono<ServerResponse> respond(T info) {
        return Mono
                .just(info)
                .flatMap(i -> ServerResponse.ok().contentType(APPLICATION_JSON).body(fromObject(i)))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> getCountry(ServerRequest request) {
        final Locale locale = extractLocalization(request);
        final String code = extractCode(request);

        // TODO: search locales for country by 2-letter, 3-letter, 4-letter
        final Country country = new CountryFromJRE(Locale.US, locale);

        return respond(country);
    }

    public Mono<ServerResponse> getCurrency(ServerRequest request) {
        final Locale locale = extractLocalization(request);
        final String code = extractCode(request);

        Currency currency;
        try {
            // TODO: handle searches by numeric code
            final java.util.Currency byAlpha = java.util.Currency.getInstance(code);
            currency = new CurrencyFromJRE(byAlpha, locale);
        } catch (final IllegalArgumentException e) {
            currency = null;
        }

        return respond(currency);
    }

    public Mono<ServerResponse> getLanguage(ServerRequest request) {
        final Locale locale = extractLocalization(request);
        final String code = extractCode(request);

        final java.util.Locale byTag = Locale.forLanguageTag(code);
        Language language = byTag == null ? null : new LanguageFromJRE(byTag, locale);

        return respond(language);
    }
}
