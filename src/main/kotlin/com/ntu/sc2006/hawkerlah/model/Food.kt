package com.ntu.sc2006.hawkerlah.model

import kotlinx.serialization.Serializable

@Serializable
enum class FoodStatus {
    SELLING_FAST, CLEARANCE, NORMAL
}

@Serializable
data class Food(
    val image: String,
    val name: String,
    val description: String,
    val price: Double,
    val clearancePrice: Double,
    val status: FoodStatus
)

