package com.ntu.sc2006.hawkerlah.model

import com.ntu.sc2006.hawkerlah.utils.SUUID
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class Food(
    val id: SUUID,
    @SerialName("image_url")
    val image: String,
    @SerialName("dish_name")
    val dishName: String,
    val description: String,
    val price: Double,
    @SerialName("clearance_price")
    val clearancePrice: Double,
//    val status: FoodStatus,
    @SerialName("hawker_id")
    val hawkerStallId: SUUID,
    @SerialName("cold_food")
    val coldFood: Boolean,
    @SerialName("clearance")
    val clearance: Boolean,
    @SerialName("hawker_sales")
    val hawkerSales: List<HawkerSales>? = emptyList(),
    // KC ADD

)

