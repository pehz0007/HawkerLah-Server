package com.ntu.sc2006.hawkerlah.model

import kotlinx.serialization.Serializable

@Serializable
data class SalesData(
    val hawkerSales: List<HawkerSales>
)