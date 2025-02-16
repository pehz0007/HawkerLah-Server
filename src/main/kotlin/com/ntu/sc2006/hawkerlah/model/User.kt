package com.ntu.sc2006.hawkerlah.model

import kotlinx.serialization.Serializable

@Serializable
open class User(
    open val email: String,
    open val username: String,
    open val number: String
)
