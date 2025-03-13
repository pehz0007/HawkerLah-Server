package com.ntu.sc2006.hawkerlah.model

import com.ntu.sc2006.hawkerlah.utils.SUUID
import kotlinx.datetime.LocalDate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OrderTracking(
    val id: SUUID,
    val quantity: Long,
    @SerialName("sales_date")
    val salesDate: LocalDate,
    @SerialName("stall_dish_id")
    val stallDishId: SUUID
)