package com.example.basics;

import io.opentracing.Span;
import io.opentracing.SpanContext;
import io.opentracing.Tracer;
import io.opentracing.propagation.Format;
import io.opentracing.propagation.TextMap;
import io.opentracing.tag.Tags;
import io.opentracing.util.GlobalTracer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;

import java.util.Iterator;
import java.util.Map;

@Component
public class Tracing {

    private Logger log = LoggerFactory.getLogger(Tracing.class);

    private final Tracer tracer;

    /**
     * Create a tracing instance, registering the tracer {@link GlobalTracer#register(Tracer)} as needed.
     *
     * @param tracer the configured tracer. If this value is {@code null}, uses the tracer returned by
     *               {@link GlobalTracer#get()}, which may be a NO-OP.
     */
    public Tracing(@Autowired(required = false) final Tracer tracer) {
        if (tracer == null) {
            this.tracer = GlobalTracer.get();
            log.info("Using GlobalTracer");
        } else try {
            if (GlobalTracer.isRegistered() && !tracer.equals(GlobalTracer.get())) {
                log.warn("Configured with tracer other than GlobalTracer");
            } else {
                GlobalTracer.register(tracer);
                log.info("Registered configured tracer as GlobalTracer");
            }
        } finally {
            this.tracer = tracer;
            log.info("Using configured tracer");
        }
    }

    /**
     * Allow the logger to be configured to something other than based on the class name.
     *
     * @param log the logger to use.
     */
    @Autowired(required = false)
    public void setLogger(final Logger log) {
        this.log = log;
    }

    /**
     * Extracts the context from the server request, if there is one, and creates a new child span.
     *
     * @param request   the incoming server request.
     * @param operation the operation to assign to the new span.
     * @return a new child span.
     */
    public Span span(final ServerRequest request, final String operation) {
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

        this.log.info("{} tracing context for {}", ctx == null ? "No" : "Generated", operation);

        Tracer.SpanBuilder builder = this.tracer.buildSpan(operation)
                .withTag(Tags.SPAN_KIND.getKey(), Tags.SPAN_KIND_SERVER);

        // workaround for https://github.com/openzipkin-contrib/brave-opentracing/issues/63
        if (ctx != null) builder = builder.asChildOf(ctx);

        return builder.start();
    }

}
