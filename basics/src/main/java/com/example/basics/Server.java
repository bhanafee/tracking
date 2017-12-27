package com.example.basics;

import io.opentracing.ActiveSpan;
import io.opentracing.SpanContext;
import io.opentracing.Tracer;
import io.opentracing.propagation.Format;
import io.opentracing.propagation.TextMap;
import io.opentracing.tag.Tags;
import io.opentracing.util.GlobalTracer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.nio.charset.Charset;
import java.util.*;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private Logger logger = LoggerFactory.getLogger(Server.class);

    private Tracer tracer = GlobalTracer.get();

    public static void main(String[] args) {
        SpringApplication.run(Server.class, args);
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
        if (operation == null || operation.isEmpty()) throw new IllegalArgumentException("Mission trace operation");

        return (request) -> {
            final ActiveSpan span = span(request, operation);

            // Extract preferred Locale from headers, falling back to platform default
            final List<Locale.LanguageRange> requested = request.headers().acceptLanguage();
            final Locale matched = Locale.lookup(requested, BuiltIns.AVAILABLE_LOCALES);
            final Locale locale = matched == null ? Locale.getDefault(Locale.Category.DISPLAY) : matched;

            // Extract code from path
            final String code = request.pathVariable("code");

            Optional<R> result = find.apply(code, locale);

            logger.info("Request for {} code '{}'", operation, code);

            final Mono<ServerResponse> response = result.isPresent()
                    ? Mono.just(result).flatMap((i -> ServerResponse.ok().contentType(JSON_UTF8).body(fromObject(i))))
                    : ServerResponse.notFound().build();

            return span == null ? response : response.doOnTerminate(span::deactivate);
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
        final Function<ServerRequest, Mono<ServerResponse>> fn = get(BuiltIns.findLanguage(),"language");
        return fn.apply(request);
    }

    /**
     * Extracts the context from the server request, if there is one, and creates a new child span.
     * @param request the incoming server request.
     * @param operation the operation to assign to the new span.
     * @return a new child span.
     */
    private ActiveSpan span(final ServerRequest request, final String operation) {
        final SpanContext ctx = this.tracer.extract(Format.Builtin.HTTP_HEADERS, new TextMap() {
            @Override
            public Iterator<Map.Entry<String, String>> iterator() {
                return request.headers().asHttpHeaders().toSingleValueMap().entrySet().iterator();
            }

            @Override
            public void put(String key, String value) {
                throw new UnsupportedOperationException("Cannot put headers into immutable request");

            }
        });

        return this.tracer.buildSpan(operation)
                .asChildOf(ctx)
                .withTag(Tags.SPAN_KIND.getKey(), Tags.SPAN_KIND_SERVER)
                .startActive();
    }
}
