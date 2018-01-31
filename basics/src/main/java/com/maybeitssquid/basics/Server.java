package com.maybeitssquid.basics;

import io.opentracing.Scope;
import io.opentracing.Span;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.BodyInserters.fromObject;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

/**
 * Main class for the application.
 */
@SpringBootApplication
public class Server {

    private static final MediaType JSON_UTF8 = new MediaType(APPLICATION_JSON, Charset.forName("UTF-8"));

    private Logger log = LoggerFactory.getLogger(Server.class);

    private Tracing tracing;

    public static void main(String[] args) {
        SpringApplication.run(Server.class, args);
    }

    /**
     * Allows tracing to be configured.
     *
     * @param tracing the tracing configuration
     */
    @Autowired(required = false)
    public void setTracing(final Tracing tracing) {
        this.tracing = tracing;
    }

    /**
     * Provides the routes for countries, currencies and languages.
     *
     * @return routes for the server
     */
    @Bean
    public RouterFunction<ServerResponse> routes() {
        return route(GET("/countries/{code}"), this::getCountry)
                .andRoute(GET("/currencies/{code}"), this::getCurrency)
                .andRoute(GET("/languages/{code}"), this::getLanguage);
    }

    private <R> Function<ServerRequest, Mono<ServerResponse>> get(LocalizedFind<R> find, String operation) {
        if (find == null) throw new NullPointerException("Missing find function");
        if (operation == null || operation.isEmpty()) throw new IllegalArgumentException("Missing trace operation");

        return (request) -> {
            final Span span = this.tracing == null ? null : this.tracing.span(request, operation);
            log.info("span {}started for {}", span == null ? "not " : "", operation);

            // Extract preferred Locale from headers, falling back to platform default
            final List<Locale.LanguageRange> requested = request.headers().acceptLanguage();
            final Locale matched = Locale.lookup(requested, BuiltIns.AVAILABLE_LOCALES);
            final Locale locale = matched == null ? Locale.getDefault(Locale.Category.DISPLAY) : matched;

            // Extract code from path
            final String code = request.pathVariable("code");

            Optional<R> result = find.apply(code, locale);

            log.info("Request for {} code '{}'", operation, code);

            final Mono<ServerResponse> response = result.isPresent()
                    ? Mono.just(result).flatMap((i -> ServerResponse.ok().contentType(JSON_UTF8).body(fromObject(i))))
                    : ServerResponse.notFound().build();

            return span == null ? response : response.doOnTerminate(span::finish);
        };
    }

    private Mono<ServerResponse> getCountry(ServerRequest request) {
        final Function<ServerRequest, Mono<ServerResponse>> fn = get(BuiltIns.findCountry(), "country");
        return fn.apply(request);
    }

    private Mono<ServerResponse> getCurrency(ServerRequest request) {
        final Function<ServerRequest, Mono<ServerResponse>> fn = get(BuiltIns.findCurrency(), "currency");
        return fn.apply(request);
    }

    private Mono<ServerResponse> getLanguage(ServerRequest request) {
        final Function<ServerRequest, Mono<ServerResponse>> fn = get(BuiltIns.findLanguage(), "language");
        return fn.apply(request);
    }

}
