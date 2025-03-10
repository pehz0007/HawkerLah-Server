package com.ntu.sc2006.hawkerlah.service

import com.ntu.sc2006.hawkerlah.controller.SupabaseBean
import com.ntu.sc2006.hawkerlah.model.Food
import com.ntu.sc2006.hawkerlah.model.HawkerCentre
import com.ntu.sc2006.hawkerlah.model.HawkerStall
import com.ntu.sc2006.hawkerlah.utils.SUUID
import io.github.jan.supabase.postgrest.from
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import org.springframework.stereotype.Service

@Service
class HawkerCentreService(
    val supabaseBean: SupabaseBean
) {

    suspend fun retrieveHawkerStall(userId: SUUID): HawkerStall {
        val client = supabaseBean.supabaseClient()
        return client.from("hawker_stall").select() {
            filter {
                eq("id", userId)
            }
        }.decodeSingle<HawkerStall>()
    }

    suspend fun retrieveHawkerCentres(): List<HawkerCentre> {
        val client = supabaseBean.supabaseClient()
        return client.from("hawker_centre").select().decodeList<HawkerCentre>()
    }

    suspend fun retrieveSpecificHawkerStallDish(dishID: SUUID): Food {
        val client = supabaseBean.supabaseClient()
        return client.from("stall_dishes").select() {
            filter {
                eq("id", dishID)
            }
        }.decodeSingle<Food>()
    }

    suspend fun updateDishDetails(
        dishID: SUUID,
        dishName: String,
        description: String,
        price: Double,
        clearancePrice: Double) {

        try {
            val client = supabaseBean.supabaseClient()
            val response = client.from("stall_dishes")
                .update(
                    buildJsonObject {
                        put("dish_name", dishName)
                        put("description", description)
                        put("price", price)
                        put("clearance_price", clearancePrice)
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

}