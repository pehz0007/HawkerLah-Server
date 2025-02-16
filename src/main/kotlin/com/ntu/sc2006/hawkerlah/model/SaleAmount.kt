package com.ntu.sc2006.hawkerlah.model

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class SaleAmount(
    val food: Food,
    val quantitySold: Int,
    val date: LocalDate
)
