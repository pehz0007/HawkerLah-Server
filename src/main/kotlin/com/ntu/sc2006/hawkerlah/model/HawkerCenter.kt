package com.ntu.sc2006.hawkerlah.model

import kotlinx.serialization.Serializable

@Serializable
data class HawkerCenter(
    val name: String,
    val imageUrl: String,
    val rating: Int,
    val hawkerStalls: List<HawkerStall>,
    val location: Location
)

val sampleHawkerCenters = listOf(
    HawkerCenter(
        name = "Maxwell Food Centre",
        imageUrl = "https://example.com/maxwell.jpg",
        rating = 5,
        hawkerStalls = listOf(
            HawkerStall(
                name = "Tian Tian Hainanese Chicken Rice",
                image = "https://example.com/tian_tian.jpg",
                foodOption = listOf()
            ),
            HawkerStall(
                name = "Zhen Zhen Porridge",
                image = "https://example.com/zhen_zhen.jpg",
                foodOption = listOf()
            )
        ),
        location = Location(1.280094, 103.844173)
    ),
    HawkerCenter(
        name = "Old Airport Road Food Centre",
        imageUrl = "https://example.com/old_airport.jpg",
        rating = 4,
        hawkerStalls = listOf(
            HawkerStall(
                name = "Nam Sing Hokkien Fried Mee",
                image = "https://example.com/nam_sing.jpg",
                foodOption = listOf()
            ),
            HawkerStall(
                name = "To-Ricos Kway Chap",
                image = "https://example.com/to_ricos.jpg",
                foodOption = listOf()
            )
        ),
        location = Location(1.308598, 103.885546)
    ),
    HawkerCenter(
        name = "Tiong Bahru Market",
        imageUrl = "https://example.com/tiong_bahru.jpg",
        rating = 4,
        hawkerStalls = listOf(
            HawkerStall(
                name = "Tiong Bahru Hainanese Boneless Chicken Rice",
                image = "https://example.com/tbh_chicken_rice.jpg",
                foodOption = listOf()
            ),
            HawkerStall(
                name = "Min Nan Prawn Noodle",
                image = "https://example.com/min_nan.jpg",
                foodOption = listOf()
            )
        ),
        location = Location(1.285856, 103.832875)
    )
)