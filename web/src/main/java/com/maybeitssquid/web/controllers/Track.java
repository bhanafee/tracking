package com.maybeitssquid.web.controllers;

import com.maybeitssquid.web.model.Position;
import com.maybeitssquid.web.model.Tourism;
import com.maybeitssquid.web.model.Tracker;
import com.maybeitssquid.web.model.Waypoint;
import io.opentracing.Span;
import io.opentracing.Tracer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.security.Principal;
import java.time.Instant;
import java.time.temporal.ChronoField;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

@Controller
@Slf4j
@RequiredArgsConstructor
public class Track {

    private final Tracer tracer;

    private final Tracker tracker;

    private final Tourism tourism;

    @GetMapping("/track")
    public String show(final Principal principal) {
        final Span span = trace("show");
        try {
            final String user = principal.getName();
            log.info("Getting track for {}", user);
            final Iterable<Waypoint> track = tracker.track(user);
            return "track";
        } catch (Exception e) {
            return error(e, span);
        } finally {
            span.finish();
        }
    }

    @PostMapping("/track")
    public String add(final Principal principal, final @Valid Position position, final BindingResult bindingResult) {
        // Get timestamp early
        final Instant timestamp = Instant.now();
        final Span span = trace("track");
        final String user = principal.getName();
        try {
            if (bindingResult.hasErrors()) {
                throw new IllegalArgumentException("Bad position received");
            } else {
                log.info("Posting waypoint for {}", user);
                final Map<String, String> tags = tracker.mark(principal.getName(), timestamp, position);
                final String blurb = !tags.containsKey("country") ? "" : tourism.info(
                        Locale.getDefault(),
                        tags.get("country"),
                        Optional.ofNullable(tags.get("postal")));
            }
            return "redirect:track";
        } catch (Exception e) {
            return error(e, span);
        } finally {
            span.finish();
        }
    }

    private Span trace(final String operation) {
        return tracer.buildSpan(operation).withTag("span.kind", "consumer").start();
    }

    private String error(final Exception e, final Span s) {
        final Instant now = Instant.now();
        final long secs = now.getLong(ChronoField.INSTANT_SECONDS);
        final long micros = now.getLong(ChronoField.MICRO_OF_SECOND);
        final long time = secs * 1000000L + micros;
        s.setTag("error", true);
        final Map<String, String> fields = new HashMap<String, String>();
        fields.put("event", "error");
        fields.put("error.object", e.getLocalizedMessage());
        s.log(time, fields);
        log.error("Controller exception", e);
        return "FAILURE";
    }
}
