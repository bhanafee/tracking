package com.maybeitssquid.web.model;

import lombok.Data;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;

@Data
public class Waypoint {

    /** The time at this waypoint. */
    private final Instant timestamp;

    /** The location of this waypoint, if known. */
    private final Optional<Position> position;

    /** The tags associated with this waypoint. */
    private final Map<String, String> tags;
}
