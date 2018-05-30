package com.maybeitssquid.geolocation

import java.sql.Types.DOUBLE

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Component

/**
 * Implements [GeoQueries] using PostGIS TIGER database
 * @param template template wired to TIGER data
 */
@Component
class TigerDB(@Autowired val template: JdbcTemplate) : GeoQueries {
    companion object {
        const val AREAS_QUERY =
                "WITH point AS (SELECT ST_SetSRID(ST_MakePoint(?, ?),4326) AS location) " +
                        "SELECT tag, id, description " +
                        "FROM tiger_staging.areas, point " +
                        "WHERE ST_Intersects(point.location, geom)"
        val AREAS_PARAMETER_TYPES = intArrayOf(DOUBLE, DOUBLE)
    }

    override fun areas(point: Point): Array<Area> =
            template.query(AREAS_QUERY, arrayOf(point.longitude, point.latitude), AREAS_PARAMETER_TYPES)
            { rs, _ -> Area(rs.getString("tag"), rs.getString("id"), rs.getString("description")) }.toTypedArray()
}
