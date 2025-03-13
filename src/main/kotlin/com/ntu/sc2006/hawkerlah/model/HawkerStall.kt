package com.ntu.sc2006.hawkerlah.model

import com.ntu.sc2006.hawkerlah.utils.SUUID
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HawkerStall(
    val id: SUUID,
    val name: String,
    @SerialName("image_url")
    val imageUrl: String?,
    @SerialName("hawker_centre_id")
    val hawkerCentreId: SUUID,
    val rating: Float,
    @SerialName("stall_dishes")
    val stallDishes: List<Food>? = emptyList()
)
