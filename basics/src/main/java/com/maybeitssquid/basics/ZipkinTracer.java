package com.maybeitssquid.basics;

import brave.opentracing.BraveTracer;
import io.opentracing.Tracer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class ZipkinTracer {

    private final Tracer tracer;
    private Logger log = LoggerFactory.getLogger(ZipkinTracer.class);

    public ZipkinTracer(@Value("${zipkin.host}") final String host, @Value("${zipkin.port}") final int port) {
        final String url = String.format("http://%1s:%2d/api/v2/spans", host, port);
        log.info("Tracing to Zipkin server at {}", url);
        final Sender sender = OkHttpSender.create(url);
        final AsyncReporter<Span> reporter = AsyncReporter.create(sender);
        final brave.Tracing tracing = brave.Tracing.newBuilder()
                .localServiceName("basics")
                .spanReporter(reporter)
                .build();
        this.tracer = BraveTracer.create(tracing);
    }

    @Bean
    public Tracer tracer() {
        return this.tracer;
    }
}
