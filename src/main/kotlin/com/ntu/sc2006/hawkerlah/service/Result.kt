package com.ntu.sc2006.hawkerlah.service

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.springframework.http.ResponseEntity

@Serializable
enum class ResultStatus {
    @SerialName("ok")
    Ok,
    @SerialName("error")
    Error
}

@Serializable
sealed class Result<T>(
    val status: ResultStatus
) {
    abstract val response: T?
}

@Serializable
data class SuccessResult<T>(
    override val response: T
) : Result<T>(ResultStatus.Ok)

@Serializable
data class ErrorResult<T>(
    val message: String,
    @Transient override val response: T? = null
) : Result<T>(ResultStatus.Error)

inline fun <reified T> T.toResponseEntity(): ResponseEntity<String> {
    val json = Json.encodeToString(this)
    return ResponseEntity.ok(json)
}