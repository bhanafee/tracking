package com.maybeitssquid.web;

import brave.opentracing.BraveTracer;
import io.opentracing.Tracer;
import io.opentracing.util.GlobalTracer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import zipkin2.Span;
import zipkin2.reporter.AsyncReporter;
import zipkin2.reporter.Sender;
import zipkin2.reporter.okhttp3.OkHttpSender;

@Configuration
@Profile("zipkin")
@Slf4j
public class ZipkinTracing {

    private final Tracer tracer;

    public ZipkinTracing(@Value("${zipkin.host}") final String host, @Value("${zipkin.port}") final int port) {
        final String url = String.format("http://%1s:%2d/api/v2/spans", host, port);
        log.info("Tracing to Zipkin server at {}", url);
        final Sender sender = OkHttpSender.create(url);
        final AsyncReporter<Span> reporter = AsyncReporter.create(sender);
        final brave.Tracing tracing = brave.Tracing.newBuilder()
                .localServiceName("web")
                .spanReporter(reporter)
                .build();
        this.tracer = BraveTracer.create(tracing);
        GlobalTracer.register(tracer);
    }

    @Bean
    public Tracer tracer() {
        return this.tracer;
    }
}
