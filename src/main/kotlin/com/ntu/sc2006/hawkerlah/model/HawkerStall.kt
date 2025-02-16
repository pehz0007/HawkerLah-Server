package com.ntu.sc2006.hawkerlah.model

import kotlinx.serialization.Serializable

@Serializable
data class HawkerStall(
    val name: String,
    val image: String,
    val foodOption: List<Food>,
)
