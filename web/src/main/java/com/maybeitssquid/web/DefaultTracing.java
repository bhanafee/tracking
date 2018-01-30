package com.maybeitssquid.web;

import io.opentracing.Tracer;
import io.opentracing.util.GlobalTracer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("!zipkin")
@Slf4j
public class DefaultTracing {

    @Bean
    public Tracer tracer() {
        log.info("Using {} global tracer", GlobalTracer.isRegistered() ? "registered" : "default");
        return GlobalTracer.get();
    }
}
