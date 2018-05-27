package com.maybeitssquid.geolocation

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Component

/**
 * Implements [GeoQueries] using PostGIS TIGER database
 * @param template template wired to TIGER data
 */
@Component
class TigerDB (@Autowired template: JdbcTemplate) : GeoQueries {
    override fun areas(point: Point): Sequence<Area> {
        return emptySequence()
    }
}
