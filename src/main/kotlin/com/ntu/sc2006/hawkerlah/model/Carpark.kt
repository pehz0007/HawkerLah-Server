package com.ntu.sc2006.hawkerlah.model

import kotlinx.serialization.Serializable

@Serializable
data class Carpark(
    val name: String,
    val type: String,
    val address: String,
    val totalLots: String,
    val availableLots: String,
    val location: Location
)
