package com.ntu.sc2006.hawkerlah.model

import kotlinx.serialization.Serializable

@Serializable
data class CarparkRecord(
    val car_park_no: String,
    val address: String,
    val car_park_type: String,
    val x_coord: String?,
    val y_coord: String?
)

@Serializable
data class CarparkResult(val records: List<CarparkRecord>)

@Serializable
data class CarparkResponse(val result: CarparkResult)

@Serializable
data class AvailabilityRecord(
    val carpark_number: String,
    val update_datetime: String,
    val carpark_info: List<CarparkInfo>
)

@Serializable
data class AvailabilityData(val carpark_data: List<AvailabilityRecord>)

@Serializable
data class AvailabilityResponse(val items: List<AvailabilityData>)

@Serializable
data class CarparkInfo(
    val total_lots: String,
    val lots_available: String,
    val lot_type: String
)