package com.ntu.sc2006.hawkerlah.model

import com.ntu.sc2006.hawkerlah.utils.SUUID
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HawkerOwner(
    val id: SUUID,
    @SerialName("hawker_centre_id")
    val hawkerCentreId: SUUID,
)