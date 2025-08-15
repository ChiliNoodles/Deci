package com.chilinoodles.deci

import com.kttipay.common.deci.DeciSerializer
import kotlinx.serialization.Serializable

@Serializable(with = DeciSerializer::class)
expect class Deci : Comparable<Deci> {
    constructor(value: String)
    constructor(value: Long)
    constructor(value: Int)
    constructor(value: Double)
    
    operator fun plus(other: Deci): Deci
    operator fun minus(other: Deci): Deci
    operator fun times(other: Deci): Deci
    operator fun div(other: Deci): Deci
    fun divide(divisor: Deci, scale: Int, roundingMode: RoundingMode): Deci
    fun setScale(scale: Int, roundingMode: RoundingMode): Deci
    override fun toString(): String
    fun toDouble(): Double
    fun isZero(): Boolean
    fun isNegative(): Boolean
    fun isPositive(): Boolean
    fun abs(): Deci
    fun negate(): Deci
    fun max(other: Deci): Deci
    fun min(other: Deci): Deci

    override fun compareTo(other: Deci): Int

    companion object {
        val ZERO: Deci
        val ONE: Deci
        val TEN: Deci

        @Deprecated("Use constructor instead", ReplaceWith("Deci(value)"))
        fun fromInt(value: Int): Deci
        @Deprecated("Use constructor instead", ReplaceWith("Deci(value)"))
        fun fromDouble(value: Double): Deci

        @Deprecated("Use constructor instead", ReplaceWith("Deci(value)"))
        fun fromStringOrThrow(value: String): Deci

        fun fromStringOrZero(value: String): Deci
        fun fromStringOrNull(value: String): Deci?
    }
}

@Deprecated("Use Deci.fromStringOrZero(value) instead", ReplaceWith("Deci.fromStringOrZero(value)"))
fun String.toSafeDeci(): Deci = Deci.fromStringOrZero(this)

/**
 * Sums all [Deci] values in this collection.
 * Returns [Deci.ZERO] for empty collections.
 */
fun Iterable<Deci>.sumDeci(): Deci = this.fold(Deci.ZERO) { acc, d -> acc + d }

/**
 * Raises this [Deci] to the given integer power.
 * 
 * @param exp The exponent (must be non-negative)
 * @throws IllegalArgumentException if [exp] is negative
 */
fun Deci.pow(exp: Int): Deci {
    require(exp >= 0) { "Negative exponents are not supported" }
    var result = Deci.ONE
    repeat(exp) {
        result *= this
    }
    return result
}

/**
 * Converts this [Deci] to [Long] by first converting to [Double].
 * The fractional part is truncated.
 */
fun Deci.toLong(): Long = this.toDouble().toLong()