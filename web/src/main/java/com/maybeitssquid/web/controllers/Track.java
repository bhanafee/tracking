package com.maybeitssquid.web.controllers;

import io.opentracing.Span;
import io.opentracing.Tracer;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;

@Controller
@Slf4j
@RequiredArgsConstructor
public class Track {

    private final Tracer tracer;

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
