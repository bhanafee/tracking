package com.maybeitssquid.geolocation

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

/**
 * API to process geographic queries.
 * @param queries component that executes the queries
 */
@RestController
class QueryController(@Autowired val queries: GeoQueries) {
    @GetMapping("/coordinate")
    fun greeting(@RequestParam(value = "longitude") longitude: Double,
                 @RequestParam(value = "latitude") latitude: Double) =
            with(queries) {
                 areas(Point(longitude, latitude))
            }
}
