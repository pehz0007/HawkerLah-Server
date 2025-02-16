package com.ntu.sc2006.hawkerlah.model

import kotlinx.serialization.Serializable

@Serializable
data class Carpark(
    val name: String,
    val type: String,
    val totalLots: Int,
    val availableLots: Int,
    val location: Location
)
