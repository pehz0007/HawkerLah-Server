package com.ntu.sc2006.hawkerlah.model

import com.ntu.sc2006.hawkerlah.utils.SUUID
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Hawker(
    @SerialName("hawker_email") override val email: String, // Distinct name if needed
    @SerialName("hawker_username") override val username: String,
    @SerialName("hawker_number") override val number: String,
) : User(email, username, number)
