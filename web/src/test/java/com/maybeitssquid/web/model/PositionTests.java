package com.maybeitssquid.web.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

import java.math.BigDecimal;

/**
 * Unit tests for Position data objects.
 */
public class PositionTests {

    @Test
    public void testSimplePosition() {
        final Position test = new Position(BigDecimal.ZERO, BigDecimal.ONE);
        assertEquals("Longitude is zero", BigDecimal.ZERO, test.getLongitude());
        assertEquals("Latitude is one", BigDecimal.ONE, test.getLatitude());
        assertFalse("Has no elevation", test.getElevation().isPresent());
    }

    @Test
    public void testPositionWithElevation() {
        final Position test = new Position(BigDecimal.ONE, BigDecimal.TEN, 100);
        assertEquals("Longitude is 1", BigDecimal.ONE, test.getLongitude());
        assertEquals("Latitude is 10", BigDecimal.TEN, test.getLatitude());
        assertEquals("Elevation is 100", 100, test.getElevation().getAsInt());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMaxLatitude() {
        new Position(BigDecimal.ZERO, BigDecimal.valueOf(91L));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMinLatitude() {
        new Position(BigDecimal.ZERO, BigDecimal.valueOf(-91L));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMaxLongitude() {
        new Position(BigDecimal.valueOf(181L), BigDecimal.ZERO);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMinLongitude() {
        new Position(BigDecimal.valueOf(-181L), BigDecimal.ZERO);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMaxElevation() {
        new Position(BigDecimal.ZERO, BigDecimal.ZERO, 9001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMinElevation() {
        new Position(BigDecimal.ZERO, BigDecimal.ZERO, -501);
    }
}
