package com.ntu.sc2006.hawkerlah.utils;

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlin.uuid.Uuid

object UUIDSerializer : KSerializer<Uuid> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("UUID", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Uuid) = encoder.encodeString(value.toString())

    override fun deserialize(decoder: Decoder): Uuid = Uuid.parse(decoder.decodeString())
}

typealias SUUID = @Serializable(with = UUIDSerializer::class) Uuid
