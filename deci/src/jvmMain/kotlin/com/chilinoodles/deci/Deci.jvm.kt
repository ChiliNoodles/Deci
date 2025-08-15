package com.chilinoodles.deci

@kotlinx.serialization.Serializable(with = com.kttipay.common.deci.DeciSerializer::class)
actual class Deci : Comparable<Deci> {
    actual operator fun plus(other: Deci): Deci {
        TODO("Not yet implemented")
    }

    actual operator fun minus(other: Deci): Deci {
        TODO("Not yet implemented")
    }

    actual operator fun times(other: Deci): Deci {
        TODO("Not yet implemented")
    }

    actual operator fun div(other: Deci): Deci {
        TODO("Not yet implemented")
    }

    actual fun divide(
        divisor: Deci,
        scale: Int,
        roundingMode: RoundingMode
    ): Deci {
        TODO("Not yet implemented")
    }

    actual fun setScale(
        scale: Int,
        roundingMode: RoundingMode
    ): Deci {
        TODO("Not yet implemented")
    }

    actual override fun toString(): String {
        TODO("Not yet implemented")
    }

    actual fun toDouble(): Double {
        TODO("Not yet implemented")
    }

    actual fun isZero(): Boolean {
        TODO("Not yet implemented")
    }

    actual fun isNegative(): Boolean {
        TODO("Not yet implemented")
    }

    actual fun isPositive(): Boolean {
        TODO("Not yet implemented")
    }

    actual fun abs(): Deci {
        TODO("Not yet implemented")
    }

    actual fun negate(): Deci {
        TODO("Not yet implemented")
    }

    actual fun max(other: Deci): Deci {
        TODO("Not yet implemented")
    }

    actual fun min(other: Deci): Deci {
        TODO("Not yet implemented")
    }

    actual override operator fun compareTo(other: Deci): Int {
        TODO("Not yet implemented")
    }

    actual companion object {
        @Deprecated(
            message = "Use constructor instead",
            replaceWith = kotlin / ReplaceWith(expression = "Deci(value)")
        )
        actual fun fromInt(value: Int): Deci {
            TODO("Not yet implemented")
        }

        actual val TEN: Deci
            get() = TODO("Not yet implemented")

        @Deprecated(
            message = "Use constructor instead",
            replaceWith = kotlin / ReplaceWith(expression = "Deci(value)")
        )
        actual fun fromDouble(value: Double): Deci {
            TODO("Not yet implemented")
        }

        actual fun fromStringOrZero(value: String): Deci {
            TODO("Not yet implemented")
        }

        actual fun fromStringOrNull(value: String): Deci? {
            TODO("Not yet implemented")
        }

        @Deprecated(
            message = "Use constructor instead",
            replaceWith = kotlin / ReplaceWith(expression = "Deci(value)")
        )
        actual fun fromStringOrThrow(value: String): Deci {
            TODO("Not yet implemented")
        }

        actual val ZERO: Deci
            get() = TODO("Not yet implemented")

        actual fun serializer(): kotlinx.serialization.KSerializer<Deci> {
            TODO("Not yet implemented")
        }

        actual val ONE: Deci
            get() = TODO("Not yet implemented")
    }

    actual constructor(value: String) {
        TODO("Not yet implemented")
    }

    actual constructor(value: Long) {
        TODO("Not yet implemented")
    }

    actual constructor(value: Int) {
        TODO("Not yet implemented")
    }

    actual constructor(value: Double) {
        TODO("Not yet implemented")
    }
}