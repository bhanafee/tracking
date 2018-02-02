package com.maybeitssquid.web.model;

import java.time.Instant;
import java.util.Map;

public interface Tracker {

    /**
     * Mark a user's current position
     *
     * @param user      the user
     * @param timestamp when the user was at position
     * @param position  the position of the user
     * @return tags associated with the marker
     */
    Map<String, String> mark(String user, Instant timestamp, Position position);

    /**
     * Retrieve a user's track
     *
     * @param user the user
     * @return the waypoints recorded for the user
     */
    Iterable<Waypoint> track(String user);
}
