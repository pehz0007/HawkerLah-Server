package com.ntu.sc2006.hawkerlah.controller

import com.ntu.sc2006.hawkerlah.service.CarparkService
import com.ntu.sc2006.hawkerlah.service.HawkerCentreService
import com.ntu.sc2006.hawkerlah.service.SuccessResult
import com.ntu.sc2006.hawkerlah.service.toResponseEntity
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/customer")
class CustomerController(
    private val hawkerCentreService: HawkerCentreService,
    private val carparkService: CarparkService,
) {

    @GetMapping("/hawker-centres")
    suspend fun getHawkerCentres(): ResponseEntity<String> {
        val hawkerCentres = hawkerCentreService.retrieveHawkerCentres()
        return SuccessResult(hawkerCentres).toResponseEntity()
    }

    @GetMapping("/hawker-centre-food")
    suspend fun getAllMenuFromSpecificHawkerCentre(@RequestParam hawkerCentreId: String): ResponseEntity<String> {
        val hawkerCentreFoodItems = hawkerCentreService.retrieveAllHawkerCentreFoodItems(hawkerCentreId)
        return SuccessResult(hawkerCentreFoodItems).toResponseEntity()

    }

    @GetMapping("/carpark")
    suspend fun getCarParkerCentres(): ResponseEntity<String> {
        val carparkGeoJson = carparkService.fetchCarparkGeoJson()
        return SuccessResult(carparkGeoJson).toResponseEntity()
    }

}