package com.ntu.sc2006.hawkerlah.controller

import com.ntu.sc2006.hawkerlah.service.CarparkService
import com.ntu.sc2006.hawkerlah.service.HawkerCentreService
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
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
        val responseJson = Json.encodeToString(hawkerCentreService.retrieveHawkerCentres())
        return ResponseEntity.ok(responseJson)
    }

    @GetMapping("/hawker-centre-food")
    suspend fun getAllMenuFromSpecificHawkerCentre(@RequestParam hawkerCentreId: String): ResponseEntity<String> {
        val responseJson = Json.encodeToString(hawkerCentreService.retrieveAllHawkerCentreFoodItems(hawkerCentreId))
        return ResponseEntity.ok(responseJson)

    }

    @GetMapping("/carpark")
    suspend fun getCarParkerCentres(): ResponseEntity<String> {
        val responseJson = Json.encodeToString(carparkService.fetchCarparkGeoJson())
        return ResponseEntity.ok(responseJson)
    }

}