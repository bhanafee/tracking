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

    public static void main(String[] args) {
        SpringApplication.run(Server.class, args);
    }

    @Bean
    public RouterFunction<ServerResponse> routes() {
        Handlers handlers = new Handlers();
        return route(GET("/countries/{code}"), handlers::getCountry)
                .andRoute(GET("/currencies/{code}"), handlers::getCurrency)
                .andRoute(GET("/languages/{code}"), handlers::getLanguage);
    }

}
