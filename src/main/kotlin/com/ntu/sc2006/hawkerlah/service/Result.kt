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
sealed class Result<out T>(
    val status: ResultStatus
)

@Serializable
data class SuccessResult<out T>(
    val response: T
) : Result<T>(ResultStatus.Ok)

@Serializable
data class ErrorResult<out T>(
    val message: String,
) : Result<T>(ResultStatus.Error)

typealias GenericErrorResult = ErrorResult<Nothing>

inline fun <reified T> T.toResponseEntity(): ResponseEntity<String> {
    val json = Json.encodeToString(this)
    return ResponseEntity.ok(json)
}