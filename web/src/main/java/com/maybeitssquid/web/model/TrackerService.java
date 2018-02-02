package com.maybeitssquid.web.model;

import io.opentracing.Tracer;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.context.annotation.Configuration;

import java.time.Instant;
import java.util.Collections;
import java.util.Map;

@RequiredArgsConstructor
@Configuration
public class TrackerService implements Tracker {

    @AutoConfigureOrder
    private final Tracer tracer;

    @Override
    public Map<String, String> mark(String user, Instant timestamp, Position position) {
        return Collections.emptyMap();
    }

    @Override
    public Iterable<Waypoint> track(String user) {
        return Collections.emptyList();
    }
}
