package com.kttipay.common.deci

import com.chilinoodles.deci.Deci
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

/**
 * Kotlinx.serialization serializer for [Deci] that preserves precision by serializing as strings.
 * 
 * Serializes [Deci] values as JSON strings to maintain exact decimal precision.
 * Invalid strings during deserialization are converted to [Deci.ZERO].
 * 
 * Example JSON output: `{"amount": "123.45"}`
 */
object DeciSerializer : KSerializer<Deci> {
    override val descriptor = PrimitiveSerialDescriptor("Deci", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Deci) {
        encoder.encodeString(value.toString())
    }

    override fun deserialize(decoder: Decoder): Deci {
        return Deci.fromStringOrZero(decoder.decodeString())
    }
}