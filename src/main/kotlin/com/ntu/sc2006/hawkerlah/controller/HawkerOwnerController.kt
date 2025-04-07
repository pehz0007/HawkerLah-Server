package com.ntu.sc2006.hawkerlah.controller

import com.ntu.sc2006.hawkerlah.model.OrderTracking
import com.ntu.sc2006.hawkerlah.model.SalesData
import com.ntu.sc2006.hawkerlah.service.GenericErrorResult
import com.ntu.sc2006.hawkerlah.service.HawkerCentreService
import com.ntu.sc2006.hawkerlah.service.SuccessResult
import com.ntu.sc2006.hawkerlah.service.toResponseEntity
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
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
        return SuccessResult(hawkerStall).toResponseEntity()
    }

    @GetMapping("/hawker-dish")
    suspend fun getDishDetails(authentication: Authentication, @RequestParam dishId: String): ResponseEntity<String> {
        try{
            val userId = Uuid.parse(authentication.name)
            val hawkerStall = hawkerCentreService.retrieveHawkerStall(userId)
            val dishID = Uuid.parse(dishId)
            val dishDetails = hawkerCentreService.retrieveSpecificHawkerStallDish(dishID, hawkerStall.id)
            return SuccessResult(dishDetails).toResponseEntity()
        }catch (_: Exception){
            return GenericErrorResult("Unable to get Hawker's Stall Dish").toResponseEntity()
        }
    }

    //KCadd
    @GetMapping("/hawker-menu")
    suspend fun getHawkerMenu(authentication: Authentication): ResponseEntity<String> {
        val userId = Uuid.parse(authentication.name)
        val hawkerStall = hawkerCentreService.retrieveHawkerStall(userId)
        val menuItems = hawkerCentreService.retrieveHawkerStallDishes(hawkerStall.id)
        return SuccessResult(menuItems).toResponseEntity()
    }

    @PostMapping("/hawker-dish-add", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    suspend fun addNewDish(
        @RequestParam("file") file: MultipartFile, // Receives image
        @RequestParam("dishName") dishName: String,
        @RequestParam("description") description: String,
        @RequestParam("coldFoodStatus") coldFoodStatus: Boolean,
        @RequestParam("price") price: Double,
        @RequestParam("clearancePrice") clearancePrice: Double,
        authentication: Authentication
    ): ResponseEntity<String> {
        return try {
            val imgBytes = file.bytes
            val hawkerId = authentication.name

            if (dishName.isBlank()) {
                return GenericErrorResult("Dish name cannot be empty/must be filled").toResponseEntity()
            }

            if (description.isBlank()) {
                return GenericErrorResult("Description cannot be empty/must be filled").toResponseEntity()
            }

            if (price <= 0) {
                return GenericErrorResult("Price must be greater than 0/Cannot be negative").toResponseEntity()
            }

            if (clearancePrice <= 0) {
                return GenericErrorResult("Clearance price must be greater than 0/Cannot be negative")
                    .toResponseEntity()
            }

            if (price < clearancePrice) {
                return GenericErrorResult("Price must be greater than Clearance Price")
                    .toResponseEntity()
            }

            if (file.isEmpty) {
                return GenericErrorResult("An image file is needed").toResponseEntity()
            }

            hawkerCentreService.addNewDish(
                hawkerId,
                dishName,
                description,
                coldFoodStatus,
                price,
                clearancePrice,
                imgBytes
            )

            SuccessResult("Dish added successfully").toResponseEntity()
        } catch (e: Exception) {
            GenericErrorResult("Failed to add dish: ${e.message}").toResponseEntity()
        }
    }

    data class UpdateDishRequest(
        val dishId: String,
        val dishName: String,
        val description: String,
        val price: Double,
        val clearancePrice: Double,
        val coldFoodStatus: Boolean
    )

    @PutMapping("/hawker-dish-update")
    suspend fun updateDishDetails(@RequestBody updatedDish: UpdateDishRequest): ResponseEntity<String> {
        return try {
            if (updatedDish.dishName.isBlank()) {
                return GenericErrorResult("Dish name cannot be empty/must be filled").toResponseEntity()
            }

            if (updatedDish.description.isBlank()) {
                return GenericErrorResult("Description cannot be empty/must be filled").toResponseEntity()
            }

            if (updatedDish.price <= 0) {
                return GenericErrorResult("Price must be greater than 0/Cannot be negative").toResponseEntity()
            }

            if (updatedDish.clearancePrice <= 0) {
                return GenericErrorResult("Clearance price must be greater than 0/Cannot be negative")
                    .toResponseEntity()
            }

            if (updatedDish.price < updatedDish.clearancePrice) {
                return GenericErrorResult("Price must be greater than Clearance Price")
                    .toResponseEntity()
            }

            val dishId = Uuid.parse(updatedDish.dishId)
            hawkerCentreService.updateDishDetails(
                dishId, updatedDish.dishName, updatedDish.description, updatedDish.price, updatedDish.clearancePrice,
                updatedDish.coldFoodStatus
            )
            SuccessResult("Dish updated successfully").toResponseEntity()
        } catch (e: Exception) {
            e.printStackTrace()
            GenericErrorResult("Error updating dish").toResponseEntity()
        }
    }

    @PutMapping("/hawker-dish-updateImg")
    suspend fun updateImage(@RequestParam("dish_id") dishId: String,
                            @RequestParam("file") file: MultipartFile,
                            authentication: Authentication): ResponseEntity<String> {
        return try {
            val hawkerId = authentication.name
            val imgBytes = file.bytes
            hawkerCentreService.updateImage(
                dishId, imgBytes, hawkerId
            )

            SuccessResult("Dish image updated successfully").toResponseEntity()
        } catch (e: Exception) {
            e.printStackTrace()
            GenericErrorResult("Error updating dish image ${e.message}").toResponseEntity()
        }
    }

    data class SetClearanceParams(
        val dishId: String,
        val clearance: Boolean
    )

    @PutMapping("/set-clearance")
    suspend fun setClearance(@RequestBody updatedDish: SetClearanceParams): ResponseEntity<String> {
        return try {
            val dishID = Uuid.parse(updatedDish.dishId)
            hawkerCentreService.setClearance(
                dishID, updatedDish.clearance
            )
            SuccessResult("Clearance status successfully updated").toResponseEntity()
        } catch (e: Exception) {
            e.printStackTrace()
            GenericErrorResult("Error changing clearance status ${e.message}").toResponseEntity()
        }
    }


    @GetMapping("/order-tracking")
    suspend fun getOrderTracking(authentication: Authentication): ResponseEntity<String> {
        val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        val userId = Uuid.parse(authentication.name)
        val hawkerStall = hawkerCentreService.retrieveHawkerStall(userId)
        var menuItems = hawkerCentreService.retrieveHawkerStallDishesWithHawkerSales(hawkerStall.id, today)
        menuItems.filter { it.hawkerSales!!.isEmpty() }.forEach {
            hawkerCentreService.createHawkerSales(it.id, today)
        }
        //Create missing order tracking for today`
        menuItems = hawkerCentreService.retrieveHawkerStallDishesWithHawkerSales(hawkerStall.id, today)
        return SuccessResult(
            OrderTracking(
                menuItems,
                today,
            )
        ).toResponseEntity()
    }

    @GetMapping("/order-tracking-increment")
    suspend fun incrementOrderTracking(
        authentication: Authentication,
        @RequestParam dishId: String
    ): ResponseEntity<String> {
        val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        val userId = Uuid.parse(authentication.name)
        val dishId = Uuid.parse(dishId)
        val hawkerStall = hawkerCentreService.retrieveHawkerStall(userId)
        val dish = hawkerCentreService.getDishHawkerSales(dishId, hawkerStall.id, today)
        return if (dish != null) {
            hawkerCentreService.updateHawkerSalesQuantity(dish.id, today, dish.hawkerSales!!.first().quantity + 1)
            val updatedDish = hawkerCentreService.getDishHawkerSales(dishId, hawkerStall.id, today)
            SuccessResult(updatedDish).toResponseEntity()
        } else {
            GenericErrorResult("Order Tracking does not exist!").toResponseEntity()
        }
    }

    @GetMapping("/order-tracking-decrement")
    suspend fun decrementOrderTracking(
        authentication: Authentication,
        @RequestParam dishId: String
    ): ResponseEntity<String> {
        val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        val userId = Uuid.parse(authentication.name)
        val dishId = Uuid.parse(dishId)
        val hawkerStall = hawkerCentreService.retrieveHawkerStall(userId)
        val dish = hawkerCentreService.getDishHawkerSales(dishId, hawkerStall.id, today)
        return if (dish != null) {
            hawkerCentreService.updateHawkerSalesQuantity(dish.id, today, dish.hawkerSales!!.first().quantity - 1)
            val updatedDish = hawkerCentreService.getDishHawkerSales(dishId, hawkerStall.id, today)
            SuccessResult(updatedDish).toResponseEntity()
        } else {
            GenericErrorResult("Order Tracking does not exist!").toResponseEntity()
        }
    }

    @GetMapping("/past-sales")
    suspend fun getPastSales(
        authentication: Authentication,
        @RequestParam("startDate") startDateStr: String,
        @RequestParam("endDate") endDateStr: String
    ): ResponseEntity<String> {
        val userId = Uuid.parse(authentication.name)
        val hawkerStall = hawkerCentreService.retrieveHawkerStall(userId)
        val startDate = LocalDate.parse(startDateStr)
        val endDate = LocalDate.parse(endDateStr)
        val hawkerSales = hawkerCentreService.getPastSales(hawkerStall.id, startDate, endDate)
        return SuccessResult(SalesData(hawkerSales)).toResponseEntity()
    }

}