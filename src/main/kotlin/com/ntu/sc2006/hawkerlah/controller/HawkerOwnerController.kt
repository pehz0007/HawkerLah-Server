package com.ntu.sc2006.hawkerlah.controller

import com.ntu.sc2006.hawkerlah.service.HawkerCentreService
import kotlinx.serialization.*
import kotlinx.serialization.json.*
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import kotlin.uuid.Uuid

@RestController
@RequestMapping("/hawker-owner")
class HawkerOwnerController(
    private val hawkerCentreService: HawkerCentreService
) {

    @GetMapping("/hawker-stall")
    suspend fun getHawkerStall(authentication: Authentication): ResponseEntity<String> {
        val userId = Uuid.parse(authentication.name)
        val hawkerStall = hawkerCentreService.retrieveHawkerStall(userId)
        val responseJson = Json.encodeToString(hawkerStall)
        return ResponseEntity.ok(responseJson)
    }

    @GetMapping("/hawker-dish")
    suspend fun getDishDetails(@RequestParam dishId: String): ResponseEntity<String> {
        val dishID = Uuid.parse(dishId)
        val dishDetails = hawkerCentreService.retrieveSpecificHawkerStallDish(dishID)
        val responseJson = Json.encodeToString(dishDetails)
        return ResponseEntity.ok(responseJson)
    }

    @PutMapping("/hawker-dish-update")
    suspend fun updateDishDetails(@RequestBody updatedDish: UpdateDishRequest): ResponseEntity<String> {
        return try {
            val dishId = Uuid.parse(updatedDish.dishId)
            hawkerCentreService.updateDishDetails(
                dishId, updatedDish.dishName, updatedDish.description, updatedDish.price, updatedDish.clearancePrice
            )
            ResponseEntity.ok("Dish updated successfully")
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating dish ${e.message}")
        }
    }

    data class UpdateDishRequest(
        val dishId: String,
        val dishName: String,
        val description: String,
        val price: Double,
        val clearancePrice: Double
    )


}