package com.maybeitssquid.web.model;

import lombok.Getter;

import java.math.BigDecimal;
import java.util.OptionalInt;

public class Position {

    public static final BigDecimal MAX_LONGITUDE = BigDecimal.valueOf(180L);
    public static final BigDecimal MAX_LATITUDE = BigDecimal.valueOf(90L);
    /**
     * The shore of the Dead Sea is -413m.
     */
    public static final int MIN_ELEVATION = -500;
    /**
     * The top of Mt. Everest is 8848m.
     */
    public static final int MAX_ELEVATION = 9000;

    /**
     * Gets the longitude in degrees.
     */
    @Getter
    private final BigDecimal longitude;

    /**
     * Gets the latitude in degrees.
     */
    @Getter
    private final BigDecimal latitude;

    /**
     * Gets the elevation in meters.
     */
    @Getter
    private final OptionalInt elevation;

    private Position(BigDecimal longitude, BigDecimal latitude, OptionalInt elevation) {
        if (longitude.abs().compareTo(MAX_LONGITUDE) > 0) {
            throw new IllegalArgumentException("Longitude out of range");
        }
        if (latitude.abs().compareTo(MAX_LATITUDE) > 0) {
            throw new IllegalArgumentException("Latitude out of range");
        }
        elevation.ifPresent(e -> {
            if (e < MIN_ELEVATION || e > MAX_ELEVATION)
                throw new IllegalArgumentException("Elevation out of range");
        });
        this.longitude = longitude;
        this.latitude = latitude;
        this.elevation = elevation;
    }

    public Position(BigDecimal longitude, BigDecimal latitude) {
        this(longitude, latitude, OptionalInt.empty());
    }

    public Position(BigDecimal longitude, BigDecimal latitude, int elevation) {
        this(longitude, latitude, OptionalInt.of(elevation));
    }

}
