package com.maybeitssquid.web.model;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.*;

/**
 * Unit tests for Position data objects.
 */
public class WaypointTests {

    @Test
    public void testSimpleWaypoint() {
        final Waypoint test = new Waypoint(Instant.EPOCH, Optional.empty(), Collections.emptyMap());
        assertNotNull(test.getTimestamp());
        assertFalse(test.getPosition().isPresent());
        assertTrue(test.getTags().isEmpty());
    }

    @Test
    public void testPositionWaypoint() {
        final Position position = new Position(BigDecimal.ONE, BigDecimal.TEN);
        final Waypoint test = new Waypoint(Instant.EPOCH, Optional.of(position), Collections.emptyMap());
        assertEquals(position, test.getPosition().get());
    }

    @Test
    public void testPositionTags() {
        final Map<String, String> tags = new HashMap<String, String>();
        tags.put("foo", "FOO");
        tags.put("bar", "BAR");
        final Waypoint test = new Waypoint(Instant.EPOCH, Optional.empty(), tags);
        assertEquals(2, test.getTags().size());
        assertEquals("FOO", test.getTags().get("foo"));
        assertEquals("BAR", test.getTags().get("bar"));
        assertFalse(test.getTags().containsKey("baz"));
    }
}
