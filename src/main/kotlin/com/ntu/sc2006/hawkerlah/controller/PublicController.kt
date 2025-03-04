package com.ntu.sc2006.hawkerlah.controller

import com.ntu.sc2006.hawkerlah.service.HawkerCentreService
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/public")
class PublicController(
    val hawkerCentreService: HawkerCentreService
) {
    @GetMapping("/hawkers-centres-name")
    suspend fun getHawkerCentresName(): ResponseEntity<String> {
        val responseJson = Json.encodeToString(hawkerCentreService.retrieveHawkerCentres().map { it.name })
        return ResponseEntity.ok(responseJson)
    }
}