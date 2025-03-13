package com.ntu.sc2006.hawkerlah.controller

import com.ntu.sc2006.hawkerlah.service.HawkerCentreService
import com.ntu.sc2006.hawkerlah.utils.SUUID
import kotlinx.serialization.*
import kotlinx.serialization.json.*
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/customer")
class CustomerController(
    private val hawkerCentreService: HawkerCentreService
) {

    @GetMapping("/hawker-centres")
    suspend fun getHawkerCentres(): ResponseEntity<String> {
        val responseJson = Json.encodeToString(hawkerCentreService.retrieveHawkerCentres())
        return ResponseEntity.ok(responseJson)
    }
    @GetMapping("/hawker-centre-food")
    suspend fun getAllMenuFromSpecificHawkerCentre(@RequestParam hawkerCentreId:SUUID): ResponseEntity<String> {
        val responseJson = Json.encodeToString(hawkerCentreService.retrieveAllHawkerCentreFoodItems(hawkerCentreId.toString()))
        return ResponseEntity.ok(responseJson)

    }

}