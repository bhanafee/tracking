package com.maybeitssquid.geolocation

/**
 * A named geographic area.
 * @param tag the type of area
 * @param name the name of the area
 */
data class Area(val tag: String, val name: String)

/**
 * Longitude/Latitude coordinate
 */
data class Point(val longitude: Double, val latitude: Double)

interface GeoQueries {
    /**
     * Geographic that enclose a point.
     * @param the point
     * @return the areas that enclose the point
     */
    fun areas(point: Point): Sequence<Area>
}
