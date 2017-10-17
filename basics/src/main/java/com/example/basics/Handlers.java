package com.example.basics;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.BodyInserters.fromObject;

@Component
public class Handlers {

    private static final MediaType JSON_UTF8 = new MediaType(APPLICATION_JSON, Charset.forName("UTF-8"));

    private String extractCode(ServerRequest request) {
        return request.pathVariable("code");
    }

    private Locale extractLocalization(ServerRequest request) {
        final List<Locale.LanguageRange> requested = request.headers().acceptLanguage();
        final Locale matched = Locale.lookup(requested, BuiltIns.AVAILABLE_LOCALES);
        return matched == null ? Locale.getDefault(Locale.Category.DISPLAY) : matched;
    }

    private <T> Mono<ServerResponse> respond(@SuppressWarnings("OptionalUsedAsFieldOrParameterType") Optional<T> info) {
        return info.isPresent()
                ? Mono.just(info).flatMap(i -> ServerResponse.ok().contentType(JSON_UTF8).body(fromObject(i)))
                : ServerResponse.notFound().build();
    }

    public Mono<ServerResponse> getCountry(ServerRequest request) {
        final Locale locale = extractLocalization(request);
        final String code = extractCode(request);
        Optional<Country> country = BuiltIns.findCountry(code, locale);
        return respond(country);
    }

    public Mono<ServerResponse> getCurrency(ServerRequest request) {
        final Locale locale = extractLocalization(request);
        final String code = extractCode(request);
        Optional<Currency> currency = BuiltIns.findCurrency(code, locale);
        return respond(currency);
    }

    public Mono<ServerResponse> getLanguage(ServerRequest request) {
        final Locale locale = extractLocalization(request);
        final String code = extractCode(request);
        Optional<Language> language = BuiltIns.findLanguage(code, locale);
        return respond(language);
    }


}
