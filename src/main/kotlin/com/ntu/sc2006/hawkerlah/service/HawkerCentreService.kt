package com.ntu.sc2006.hawkerlah.service

import com.ntu.sc2006.hawkerlah.controller.SupabaseBean
import com.ntu.sc2006.hawkerlah.model.Food
import com.ntu.sc2006.hawkerlah.model.HawkerCentre
import com.ntu.sc2006.hawkerlah.model.HawkerSales
import com.ntu.sc2006.hawkerlah.model.HawkerStall
import com.ntu.sc2006.hawkerlah.utils.SUUID
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.storage.storage
import io.github.jan.supabase.storage.BucketApi
import kotlinx.datetime.LocalDate
import kotlinx.serialization.json.*
import org.springframework.stereotype.Service
import java.util.stream.DoubleStream.DoubleMapMultiConsumer
import kotlin.random.Random
import kotlin.uuid.Uuid

@Service
class HawkerCentreService(
    val supabaseBean: SupabaseBean
) {

    suspend fun retrieveHawkerStall(userId: SUUID): HawkerStall {
        val client = supabaseBean.supabaseClient()
        return client.from("hawker_stall").select {
            filter {
                eq("id", userId)
            }
        }.decodeSingle<HawkerStall>()
    }

    suspend fun retrieveHawkerCentres(): List<HawkerCentre> {
        val client = supabaseBean.supabaseClient()
        return client.from("hawker_centre").select().decodeList<HawkerCentre>()
    }

    suspend fun retrieveSpecificHawkerStallDish(dishID: SUUID, hawkerStallId: SUUID): Food {
        val client = supabaseBean.supabaseClient()
        return client.from("stall_dishes").select {
            filter {
                eq("id", dishID)
                eq("hawker_id", hawkerStallId)
            }
        }.decodeSingle<Food>()
    }

    //kcADD
    suspend fun retrieveHawkerStallDishes(hawkerId: SUUID): List<Food> {
        val client = supabaseBean.supabaseClient()
        return client.from("stall_dishes").select {
            filter {
                eq("hawker_id", hawkerId)
            }
        }.decodeList<Food>()
    }

    suspend fun retrieveAllHawkerCentreFoodItems(hawkerCentreId: String): List<Food> {
        val client = supabaseBean.supabaseClient()
        return try {
            client.from("hawker_stall").select(
                Columns.raw(
                    """
            *,
            stall_dishes(
                id, dish_name, description, price, clearance_price, image_url, hawker_id, 
                cold_food, clearance
            )
            """
                )
            ) {
                filter {
                    eq("hawker_centre_id", hawkerCentreId)
                }
            }.decodeList<HawkerStall>().flatMap { it.stallDishes!! }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList() // Return an empty list in case of error
        }
    }

    //kcEnd

    suspend fun retrieveHawkerStallDishesWithHawkerSales(hawkerId: SUUID, saleDate: LocalDate): List<Food> {
        val client = supabaseBean.supabaseClient()
        return try {
            client.from("stall_dishes").select(
                Columns.raw(
                    """
                *,
                hawker_sales(*)
            """
                )
            ) {
                filter {
                    eq("hawker_id", hawkerId)
                    eq("hawker_sales.sales_date", saleDate.toString())
                }
            }.decodeList<Food>()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList() // Return an empty list in case of error
        }
    }

    suspend fun addNewDish(
        hawkerId: String,
        dishName: String,
        description: String,
        coldFoodStatus: Boolean,
        price: Double,
        clearancePrice: Double,
        imgBytes: ByteArray
    ) {
        try {
            val client = supabaseBean.supabaseClient()
            val bucketName = "dish-photos"
            val randomNo = Random.nextInt(1, 99)
            val filePath = "users/${hawkerId}_${randomNo}.jpg"

            client.storage.from(bucketName).upload(
                path = filePath,
                data = imgBytes,
            ) {
                upsert = true
            }

            val imageUrl = client.storage.from(bucketName).publicUrl(filePath)
            println("Image uploaded successfully: $imageUrl")

            val response = client.from("stall_dishes")
                .insert(buildJsonObject {
                    put("dish_name", dishName)
                    put("description", description)
                    put("cold_food", coldFoodStatus)
                    put("price", price)
                    put("clearance_price", clearancePrice)
                    put("hawker_id", hawkerId)
                    put("image_url", imageUrl)
                })

            println("Added Dish: $response")

        } catch (e: Exception) {
            println("Failed to add dish: ${e.message}")
            throw e
        }
    }

    suspend fun updateDishDetails(
        dishID: SUUID,
        dishName: String,
        description: String,
        price: Double,
        clearancePrice: Double,
        coldFoodStatus: Boolean) {

        try {
            val client = supabaseBean.supabaseClient()
            val response = client.from("stall_dishes")
                .update(
                    buildJsonObject {
                        put("dish_name", dishName)
                        put("description", description)
                        put("price", price)
                        put("clearance_price", clearancePrice)
                        put("cold_food", coldFoodStatus)
                    }
                ) {
                    filter {
                        eq("id", dishID)
                    }
                }

            println("Updated Dish: $response")
        } catch (e: Exception) {
            e.printStackTrace()
            println("Error updating dish: ${e.message}")
        }

    }

    suspend fun updateImage(
        dishId: String,
        newImage: ByteArray,
        hawkerId: String) {
        val client = supabaseBean.supabaseClient()
        val bucketName = "dish-photos"

        try {
            val response = client.from("stall_dishes").select(
                Columns.raw("image_url")
            ) {
                filter {
                    eq("id", dishId)
                }
            }.decodeSingle<JsonObject>()

            val existingImg = response["image_url"]?.jsonPrimitive?.contentOrNull

            val prevFilePath = existingImg
                ?.substringAfter("public/")
                ?.substringAfter("$bucketName/")

            val bucket = client.storage.from(bucketName)
            if (!prevFilePath.isNullOrBlank()) {
                bucket.delete(listOf(prevFilePath))
                println("Deleted old image: $prevFilePath")
            }

            val randomNo = Random.nextInt(1, 99)
            val newFilePath = "users/${hawkerId}_${randomNo}.jpg"

            client.storage.from(bucketName).upload(
                path = newFilePath,
                data = newImage
            ) {
                upsert = true
            }

            val imageUrl = client.storage.from(bucketName).publicUrl(newFilePath)
            println("New image uploaded: $imageUrl")

            client.from("stall_dishes").update(
                buildJsonObject {
                    put("image_url", imageUrl)
                }
            ) {
                filter {
                    eq("id", dishId)
                }
            }

            println("image is updated in supabase")
        } catch (e: Exception) {
            println("Error updating image: ${e.message}")
            throw e
        }
    }

    suspend fun setClearance(
        dishId: SUUID,
        chgedStatus: Boolean
    ) {

        try {
            val client = supabaseBean.supabaseClient()
            val response = client
                .from("stall_dishes")
                .update(
                    mapOf("clearance" to chgedStatus)
                ) {
                    filter {
                        eq("id", dishId)
                    }
                }

            println("Updated Clearance Status: $response")
        } catch (e: Exception) {
            e.printStackTrace()
            println("Error updating dish: ${e.message}")
        }
    }


    suspend fun createHawkerSales(dishId: SUUID, salesDate: LocalDate) {
        val client = supabaseBean.supabaseClient()
        client.from("hawker_sales").insert(
            HawkerSales(
                id = Uuid.random(),
                quantity = 0,
                salesDate = salesDate,
                stallDishId = dishId,
            )
        )
    }

    suspend fun getDishHawkerSales(dishId: SUUID, hawkerId: SUUID, salesDate: LocalDate): Food? {
        val client = supabaseBean.supabaseClient()
        return try {
            client.from("stall_dishes").select(
                Columns.raw(
                    """
                hawker_sales(*),
                *
                """
                )
            ) {
                filter {
                    eq("hawker_id", hawkerId)
                    eq("hawker_sales.stall_dish_id", dishId)
                    eq("hawker_sales.sales_date", salesDate)
                }
            }.decodeList<Food>().firstOrNull { it.hawkerSales!!.isNotEmpty() }
        } catch (e: Exception) {
            println("Failed to add dish: ${e.message}")
            throw e
        }
    }

    suspend fun updateHawkerSalesQuantity(dishId: SUUID, salesDate: LocalDate, quantity: Long) {
        val client = supabaseBean.supabaseClient()
        client.from("hawker_sales").update({
            HawkerSales::quantity setTo quantity
        }) {
            filter {
                eq("stall_dish_id", dishId)
                eq("sales_date", salesDate)
            }
        }
    }

    suspend fun getPastSales(hawkerId: SUUID, startDate: LocalDate, endDate: LocalDate): List<HawkerSales> {
        val client = supabaseBean.supabaseClient()
        return client.from("hawker_sales").select(
            Columns.raw(
                """
                stall_dishes(*),
                *
            """.trimIndent()
            )
        ) {
            filter {
                eq("stall_dishes.hawker_id", hawkerId)
                and {
                    gte("sales_date", startDate)
                    lte("sales_date", endDate)
                }
            }
        }.decodeList<HawkerSales>().filter { it.stallDishes != null }
    }


}