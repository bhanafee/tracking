package com.example.basics;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;


@SpringBootApplication
public class Server {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Server.class, args);
    }

    @Bean
    public RouterFunction<ServerResponse> routes() {
        CountryHandler countries = new CountryHandler();
        return route(GET("/countries/{code}"), countries :: getCountry);
    }

}
