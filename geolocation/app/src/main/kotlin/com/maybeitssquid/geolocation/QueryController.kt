package com.maybeitssquid.geolocation

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class QueryController {
    @GetMapping("/coordinate")
    fun greeting(@RequestParam(value = "longitude") longitude: Double,
                 @RequestParam(value = "latitude") latitude: Double) =
            Area("country", "Atlantis")

}
