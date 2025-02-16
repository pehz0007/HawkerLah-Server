package com.ntu.sc2006.hawkerlah.model

import kotlinx.serialization.Serializable

@Serializable
data class WeatherStatus(
    val area: String,
    val location: Location,
    val forecast: WeatherForecast
)

@Serializable
enum class WeatherForecast {
    Rainy, Cloudy, Thunderstorm, Sunny
}