package com.ntu.sc2006.hawkerlah.model

import kotlinx.serialization.Serializable

@Serializable
data class BusStop(
    val id: String,
    val name: String,
    val location: String
)
