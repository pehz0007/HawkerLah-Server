package com.ntu.sc2006.hawkerlah.model

import com.ntu.sc2006.hawkerlah.utils.SUUID
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class FoodStatus {
    SELLING_FAST, CLEARANCE, NORMAL
}

@Serializable
data class Food(
    val id: SUUID,
    @SerialName("image_url")
    val image: String,
    @SerialName("dish_name")
    val name: String,
    val description: String,
    val price: Double,
    @SerialName("clearance_price")
    val clearancePrice: Double,
//    val status: FoodStatus,
    @SerialName("hawker_id")
    val hawkerStallId: SUUID
)

