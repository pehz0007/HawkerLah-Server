package com.ntu.sc2006.hawkerlah.model

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class OrderTracking(
    val dishes: List<Food>,
    val salesDate: LocalDate
)