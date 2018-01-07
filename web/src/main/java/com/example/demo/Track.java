package com.example.demo;

import io.opentracing.Span;
import io.opentracing.Tracer;
import org.slf4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class Track {

    private final Logger log;

    private final Tracer tracer;

    public Track(final Logger log, final Tracer tracer) {
        this.log = log;
        this.tracer = tracer;
    }

    @GetMapping("/track")
    public String show() {
        final Span span = tracer.buildSpan("show").start();
        log.info("Getting track");
        span.finish();
        return "track";
    }

    @PostMapping("/track")
    public String add() {
        final Span span = tracer.buildSpan("track").start();
        log.info("Showing track");
        span.finish();
        return "track";
    }
}
