package com.example.basics;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Locale;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.BodyInserters.fromObject;

@Component
public class CountryHandler {

    public Mono<ServerResponse> getCountry(ServerRequest request) {
        final String code = request.pathVariable("code");
        final Country country = new Country(Locale.US);
        Mono<Country> info = Mono.just(country);
        return info.flatMap(c -> ServerResponse.ok().contentType(APPLICATION_JSON).body(fromObject(c)));
    }
}
