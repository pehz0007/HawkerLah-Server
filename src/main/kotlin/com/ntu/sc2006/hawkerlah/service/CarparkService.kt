package com.ntu.sc2006.hawkerlah.service

import com.ntu.sc2006.hawkerlah.model.*
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.serialization.json.Json
import org.springframework.stereotype.Service

@Service
class CarparkService(val httpClient: HttpClient) {

    suspend fun fetchCarparkLocations(): List<CarparkRecord> {
        return try {
            val response: HttpResponse =
                httpClient.get("https://data.gov.sg/api/action/datastore_search?resource_id=d_23f946fa557947f93a8043bbef41dd09")
            val jsonString = response.body<String>()
            Json { ignoreUnknownKeys = true }.decodeFromString<CarparkResponse>(jsonString).result.records
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun fetchCarparkAvailability(): List<AvailabilityRecord> {
        return try {
            val response: HttpResponse = httpClient.get("https://api.data.gov.sg/v1/transport/carpark-availability")
            val jsonString = response.body<String>()
            val availabilityResponse =
                Json { ignoreUnknownKeys = true }.decodeFromString<AvailabilityResponse>(jsonString)
            println("Fetched Availability Response: $availabilityResponse")
            availabilityResponse.items.firstOrNull()?.carpark_data ?: emptyList()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun fetchAndMergeCarparkData(): List<Carpark> {
        val locations = fetchCarparkLocations()
        val availability = fetchCarparkAvailability()

        val availabilityMap = availability.associateBy { it.carpark_number }

        return locations.mapNotNull { location ->
            val lat = location.y_coord?.toDoubleOrNull()
            val lng = location.x_coord?.toDoubleOrNull()

            if (lat != null && lng != null) {
                val availabilityInfo = availabilityMap[location.car_park_no]

                val totalLots = availabilityInfo?.carpark_info?.firstOrNull()?.total_lots?.toIntOrNull() ?: 0
                val availableLots = availabilityInfo?.carpark_info?.firstOrNull()?.lots_available?.toIntOrNull() ?: 0
                Carpark(
                    name = location.car_park_no,
                    type = location.car_park_type,
                    address = location.address,
                    totalLots = totalLots.toString(),
                    availableLots = availableLots.toString(),
                    location = Location(lat, lng)
                )
            } else null
        }
    }

    suspend fun fetchCarparkGeoJson(): String {
        val carparks = fetchAndMergeCarparkData()

        val features = carparks.joinToString(",") { carpark ->
            """
        {
            "type": "Feature",
            "geometry": {
                "type": "Point",
                "coordinates": [${carpark.location.lng}, ${carpark.location.lat}]
            },
            "properties": {
                "name": "${carpark.name}",
                "type": "${carpark.type}",
                "address": "${carpark.address}",
                "totalLots": ${carpark.totalLots ?: 0},
                "availableLots": ${carpark.availableLots ?: 0}
            }
        }
        """.trimIndent()
        }

        return """
    {
        "type": "FeatureCollection",
        "features": [$features]
    }
    """.trimIndent()
    }
}