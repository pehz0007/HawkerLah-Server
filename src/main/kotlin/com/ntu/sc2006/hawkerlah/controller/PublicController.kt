package com.ntu.sc2006.hawkerlah.controller

import com.ntu.sc2006.hawkerlah.service.HawkerCentreService
import com.ntu.sc2006.hawkerlah.service.SuccessResult
import com.ntu.sc2006.hawkerlah.service.toResponseEntity
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/public")
class PublicController(
    val hawkerCentreService: HawkerCentreService
) {
    @GetMapping("/hawker-centres")
    suspend fun getHawkerCentres(): ResponseEntity<String> {
        val hawkerCentres = hawkerCentreService.retrieveHawkerCentres()
        return SuccessResult(hawkerCentres).toResponseEntity()
    }

}