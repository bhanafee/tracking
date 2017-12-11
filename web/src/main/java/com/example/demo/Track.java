package com.example.demo;

import io.opentracing.ActiveSpan;
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
        try (ActiveSpan span = tracer.buildSpan("show").startActive()) {
            log.info("Getting track");
            return "track";
        }
    }

    @PostMapping("/track")
    public String add() {
        try (ActiveSpan span = tracer.buildSpan("track").startActive()) {
            log.info("Showing track");
            return "track";
        }
    }
}
