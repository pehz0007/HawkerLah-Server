package com.ntu.sc2006.hawkerlah.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HawkerCentre(
    val name: String,
    @SerialName("image_url")
    val imageUrl: String,
    val rating: Float,
//    val hawkerStalls: List<HawkerStall>,
    @SerialName("location_lat")
    val lat: Double,
    @SerialName("location_lng")
    val lng: Double,
)