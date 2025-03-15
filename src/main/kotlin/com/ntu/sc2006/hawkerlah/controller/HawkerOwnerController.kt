package com.ntu.sc2006.hawkerlah.controller

import com.ntu.sc2006.hawkerlah.service.HawkerCentreService
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
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

    //KCadd
    @GetMapping("/hawker-menu")
    suspend fun getHawkerMenu(authentication: Authentication): ResponseEntity<String> {
        val userId = Uuid.parse(authentication.name)
        val hawkerStall = hawkerCentreService.retrieveHawkerStall(userId)
        val menuItems = hawkerCentreService.retrieveHawkerStallDishes(hawkerStall.id)
        val responseJson = Json.encodeToString(menuItems)
        return ResponseEntity.ok(responseJson)
    }

    @PostMapping("/hawker-dish-add", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    suspend fun addNewDish(
        @RequestParam("file") file: MultipartFile, // Receives image
        @RequestParam("dishName") dishName: String,
        @RequestParam("description") description: String,
        @RequestParam("price") price: Double,
        @RequestParam("clearancePrice") clearancePrice: Double,
        authentication: Authentication
    ): ResponseEntity<String> {
        return try {
            val imgBytes = file.bytes
            val hawkerId = authentication.name

            hawkerCentreService.addNewDish(
                hawkerId,
                dishName,
                description,
                price,
                clearancePrice,
                imgBytes
            )

            ResponseEntity.ok("Dish added successfully")

        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Failed to add dish: ${e.message}")
        }
    }

    data class UpdateDishRequest(
        val dishId: String,
        val dishName: String,
        val description: String,
        val price: Double,
        val clearancePrice: Double
    )

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


    @GetMapping("/order-tracking")
    suspend fun getOrderTracking(authentication: Authentication): ResponseEntity<String> {
        val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        val userId = Uuid.parse(authentication.name)
        val hawkerStall = hawkerCentreService.retrieveHawkerStall(userId)
        var menuItems = hawkerCentreService.retrieveHawkerStallDishesWithOrderTracking(hawkerStall.id, today)
        menuItems.flatMap { it.orderTrackings!! }.ifEmpty {
            menuItems.forEach { hawkerCentreService.createOrderTracking(it.id, today) }
            menuItems = hawkerCentreService.retrieveHawkerStallDishesWithOrderTracking(hawkerStall.id, today)
        }
        val jsonResponse = Json.encodeToString(menuItems)
        return ResponseEntity.ok(jsonResponse)
    }


}