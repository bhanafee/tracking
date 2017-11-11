package com.example.basics;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.BodyInserters.fromObject;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;


@SpringBootApplication
public class Server {

    private static final Logger logger = LoggerFactory.getLogger(Server.class);

    private static final MediaType JSON_UTF8 = new MediaType(APPLICATION_JSON, Charset.forName("UTF-8"));

    public static void main(String[] args) {
        SpringApplication.run(Server.class, args);
    }

    @Bean
    public RouterFunction<ServerResponse> routes() {
        return route(GET("/countries/{code}"), this::getCountry)
                .andRoute(GET("/currencies/{code}"), this::getCurrency)
                .andRoute(GET("/languages/{code}"), this::getLanguage);
    }


    private <R> Function<ServerRequest, Mono<ServerResponse>> get(LocalizedFind<R> find) {
        return (request) -> {
            // Extract preferred Locale from headers, falling back to platform default
            final List<Locale.LanguageRange> requested = request.headers().acceptLanguage();
            final Locale matched = Locale.lookup(requested, BuiltIns.AVAILABLE_LOCALES);
            final Locale locale = matched == null ? Locale.getDefault(Locale.Category.DISPLAY) : matched;

            // Extract code from path
            final String code = request.pathVariable("code");

            Optional<R> result = find.apply(code, locale);

            logger.info("Request for {}", code);

            return result.isPresent()
                    ? Mono.just(result).flatMap((i -> ServerResponse.ok().contentType(JSON_UTF8).body(fromObject(i))))
                    : ServerResponse.notFound().build();
        };
    }

    private Mono<ServerResponse> getCountry(ServerRequest request) {
        return get(BuiltIns.findCountry()).apply(request);
    }

    private Mono<ServerResponse> getCurrency(ServerRequest request) {
        return get(BuiltIns.findCurrency()).apply(request);
    }

    private Mono<ServerResponse> getLanguage(ServerRequest request) {
        return get(BuiltIns.findLanguage()).apply(request);
    }
}
