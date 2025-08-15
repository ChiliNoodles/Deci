# Deci API Documentation

This document provides comprehensive API documentation for the Deci library's core classes and functions.

## Table of Contents

- [Deci Class](#deci-class)
  - [Constructors](#constructors)
  - [Arithmetic Operations](#arithmetic-operations)
  - [Precision Control](#precision-control)
  - [Comparison Methods](#comparison-methods)
  - [Utility Methods](#utility-methods)
  - [Companion Object](#companion-object)
- [RoundingMode Enum](#roundingmode-enum)
- [DeciSerializer](#deciserializer)
- [Extension Functions](#extension-functions)

---

## Deci Class

The `Deci` class is the main class for high-precision decimal arithmetic operations. It implements `Comparable<Deci>` and supports kotlinx.serialization.

### Constructors

#### `Deci(value: String)`
Creates a Deci instance from a string representation.

**Parameters:**
- `value: String` - The string representation of the decimal number

**Throws:**
- `IllegalArgumentException` - If the string is blank or doesn't match the decimal format

**Examples:**
```kotlin
val decimal1 = Deci("123.456")      // Valid
val decimal2 = Deci("-0.789")       // Valid negative
val decimal3 = Deci("1,234.56")     // Valid with comma (converted to dot)
val decimal4 = Deci(".5")           // Valid leading decimal point
val decimal5 = Deci("100.")         // Valid trailing decimal point

// Invalid examples (throw IllegalArgumentException)
// Deci("")           // Blank string
// Deci("abc")        // Non-numeric
// Deci("1.2.3")      // Multiple decimal points
```

#### `Deci(value: Int)`
Creates a Deci instance from an integer.

**Parameters:**
- `value: Int` - The integer value

**Examples:**
```kotlin
val decimal = Deci(42)     // Creates Deci("42")
val negative = Deci(-100)  // Creates Deci("-100")
```

#### `Deci(value: Long)`
Creates a Deci instance from a long integer.

**Parameters:**
- `value: Long` - The long integer value

**Examples:**
```kotlin
val decimal = Deci(1234567890L)  // Creates Deci("1234567890")
```

#### `Deci(value: Double)`
Creates a Deci instance from a double.

**Parameters:**
- `value: Double` - The double value

**Note:** Use string constructor for exact decimal representation to avoid floating-point precision issues.

**Examples:**
```kotlin
val decimal = Deci(42.5)    // Creates Deci("42.5")
val precise = Deci("42.5")  // Recommended for precision
```

---

### Arithmetic Operations

#### `operator fun plus(other: Deci): Deci`
Adds two Deci values.

**Parameters:**
- `other: Deci` - The value to add

**Returns:** A new Deci instance with the sum

**Examples:**
```kotlin
val a = Deci("10.5")
val b = Deci("2.25")
val sum = a + b  // Deci("12.75")
```

#### `operator fun minus(other: Deci): Deci`
Subtracts one Deci value from another.

**Parameters:**
- `other: Deci` - The value to subtract

**Returns:** A new Deci instance with the difference

**Examples:**
```kotlin
val a = Deci("10.5")
val b = Deci("2.25")
val difference = a - b  // Deci("8.25")
```

#### `operator fun times(other: Deci): Deci`
Multiplies two Deci values.

**Parameters:**
- `other: Deci` - The value to multiply by

**Returns:** A new Deci instance with the product

**Examples:**
```kotlin
val a = Deci("10.5")
val b = Deci("2")
val product = a * b  // Deci("21")
```

#### `operator fun div(other: Deci): Deci`
Divides one Deci value by another with default precision.

**Parameters:**
- `other: Deci` - The divisor

**Returns:** A new Deci instance with the quotient

**Throws:**
- `ArithmeticException` - If dividing by zero

**Examples:**
```kotlin
val a = Deci("10")
val b = Deci("4")
val quotient = a / b  // Deci("2.5")

// Division by zero throws exception
// val error = a / Deci.ZERO  // ArithmeticException
```

---

### Precision Control

#### `fun divide(divisor: Deci, scale: Int, roundingMode: RoundingMode): Deci`
Divides with specified scale and rounding mode.

**Parameters:**
- `divisor: Deci` - The divisor
- `scale: Int` - The number of decimal places in the result (must be non-negative)
- `roundingMode: RoundingMode` - The rounding mode to apply

**Returns:** A new Deci instance with the quotient

**Throws:**
- `ArithmeticException` - If dividing by zero
- `IllegalArgumentException` - If scale is negative

**Examples:**
```kotlin
val result = Deci("10").divide(Deci("3"), scale = 2, roundingMode = RoundingMode.HALF_UP)
// Deci("3.33")

val precise = Deci("1").divide(Deci("3"), scale = 6, roundingMode = RoundingMode.HALF_EVEN)
// Deci("0.333333")
```

#### `fun setScale(scale: Int, roundingMode: RoundingMode): Deci`
Sets the scale (number of decimal places) of the Deci value.

**Parameters:**
- `scale: Int` - The number of decimal places (must be non-negative)
- `roundingMode: RoundingMode` - The rounding mode to apply

**Returns:** A new Deci instance with the specified scale

**Throws:**
- `IllegalArgumentException` - If scale is negative

**Examples:**
```kotlin
val value = Deci("123.456789")
val rounded = value.setScale(2, RoundingMode.HALF_UP)  // Deci("123.46")
val truncated = value.setScale(2, RoundingMode.DOWN)   // Deci("123.45")
```

---

### Comparison Methods

#### `override fun compareTo(other: Deci): Int`
Compares this Deci with another for ordering.

**Parameters:**
- `other: Deci` - The Deci to compare to

**Returns:** 
- Negative integer if this < other
- Zero if this == other  
- Positive integer if this > other

**Examples:**
```kotlin
val a = Deci("10.5")
val b = Deci("5.25")

println(a.compareTo(b))  // Positive number (a > b)
println(a > b)           // true
println(a == Deci("10.50"))  // true (trailing zeros ignored)
```

#### `fun max(other: Deci): Deci`
Returns the maximum of this Deci and another.

**Parameters:**
- `other: Deci` - The Deci to compare with

**Returns:** The larger of the two values

**Examples:**
```kotlin
val a = Deci("10.5")
val b = Deci("15.75")
val maximum = a.max(b)  // Deci("15.75")
```

#### `fun min(other: Deci): Deci`
Returns the minimum of this Deci and another.

**Parameters:**
- `other: Deci` - The Deci to compare with

**Returns:** The smaller of the two values

**Examples:**
```kotlin
val a = Deci("10.5")
val b = Deci("15.75")
val minimum = a.min(b)  // Deci("10.5")
```

---

### Utility Methods

#### `fun isZero(): Boolean`
Checks if this Deci represents zero.

**Returns:** `true` if the value is zero, `false` otherwise

**Examples:**
```kotlin
Deci("0").isZero()      // true
Deci("0.00").isZero()   // true
Deci("-0").isZero()     // true
Deci("0.01").isZero()   // false
```

#### `fun isPositive(): Boolean`
Checks if this Deci is positive (greater than zero).

**Returns:** `true` if the value is positive, `false` otherwise

**Examples:**
```kotlin
Deci("10.5").isPositive()   // true
Deci("0").isPositive()      // false
Deci("-5").isPositive()     // false
```

#### `fun isNegative(): Boolean`
Checks if this Deci is negative (less than zero).

**Returns:** `true` if the value is negative, `false` otherwise

**Examples:**
```kotlin
Deci("-10.5").isNegative()  // true
Deci("0").isNegative()      // false
Deci("5").isNegative()      // false
```

#### `fun abs(): Deci`
Returns the absolute value of this Deci.

**Returns:** A new Deci instance with the absolute value

**Examples:**
```kotlin
Deci("-10.5").abs()   // Deci("10.5")
Deci("10.5").abs()    // Deci("10.5")
Deci("0").abs()       // Deci("0")
```

#### `fun negate(): Deci`
Returns the negation of this Deci.

**Returns:** A new Deci instance with the negated value

**Examples:**
```kotlin
Deci("10.5").negate()   // Deci("-10.5")
Deci("-10.5").negate()  // Deci("10.5")
Deci("0").negate()      // Deci("0")
```

#### `override fun toString(): String`
Returns the string representation of this Deci.

**Returns:** String representation in plain decimal format

**Examples:**
```kotlin
Deci("123.456").toString()  // "123.456"
Deci("100").toString()      // "100"
```

#### `fun toDouble(): Double`
Converts this Deci to a Double.

**Returns:** Double representation (may lose precision)

**Note:** Use with caution as precision may be lost for very large or very precise numbers.

**Examples:**
```kotlin
Deci("123.456").toDouble()  // 123.456
```

---

### Companion Object

#### Constants

```kotlin
val ZERO: Deci    // Deci("0")
val ONE: Deci     // Deci("1") 
val TEN: Deci     // Deci("10")
```

#### `fun fromStringOrNull(value: String): Deci?`
Creates a Deci from a string, returning null if invalid.

**Parameters:**
- `value: String` - The string to parse

**Returns:** `Deci` instance if valid, `null` if invalid

**Examples:**
```kotlin
val valid = Deci.fromStringOrNull("123.45")     // Deci("123.45")
val invalid = Deci.fromStringOrNull("invalid")  // null
```

#### `fun fromStringOrZero(value: String): Deci`
Creates a Deci from a string, returning ZERO if invalid.

**Parameters:**
- `value: String` - The string to parse

**Returns:** `Deci` instance if valid, `Deci.ZERO` if invalid

**Examples:**
```kotlin
val valid = Deci.fromStringOrZero("123.45")     // Deci("123.45")
val invalid = Deci.fromStringOrZero("invalid")  // Deci.ZERO
```

#### Deprecated Methods

The following methods are deprecated in favor of constructors:

```kotlin
@Deprecated("Use constructor instead", ReplaceWith("Deci(value)"))
fun fromInt(value: Int): Deci

@Deprecated("Use constructor instead", ReplaceWith("Deci(value)"))
fun fromDouble(value: Double): Deci

@Deprecated("Use constructor instead", ReplaceWith("Deci(value)"))
fun fromStringOrThrow(value: String): Deci
```

---

## RoundingMode Enum

Defines how rounding should be performed when precision needs to be reduced.

### Values

#### `UP`
Round away from zero. Always increments the digit prior to a discarded fraction.

**Examples:**
```kotlin
Deci("2.5").setScale(0, RoundingMode.UP)   // "3"
Deci("-2.5").setScale(0, RoundingMode.UP)  // "-3"
```

#### `DOWN`
Round towards zero (truncate). Never increments the digit prior to a discarded fraction.

**Examples:**
```kotlin
Deci("2.9").setScale(0, RoundingMode.DOWN)   // "2"
Deci("-2.9").setScale(0, RoundingMode.DOWN)  // "-2"
```

#### `CEILING`
Round towards positive infinity.

**Examples:**
```kotlin
Deci("2.1").setScale(0, RoundingMode.CEILING)   // "3"
Deci("-2.1").setScale(0, RoundingMode.CEILING)  // "-2"
```

#### `FLOOR`
Round towards negative infinity.

**Examples:**
```kotlin
Deci("2.9").setScale(0, RoundingMode.FLOOR)   // "2"
Deci("-2.1").setScale(0, RoundingMode.FLOOR)  // "-3"
```

#### `HALF_UP`
Round to nearest neighbor. Ties are rounded away from zero.

**Examples:**
```kotlin
Deci("2.5").setScale(0, RoundingMode.HALF_UP)   // "3"
Deci("-2.5").setScale(0, RoundingMode.HALF_UP)  // "-3"
Deci("2.4").setScale(0, RoundingMode.HALF_UP)   // "2"
```

#### `HALF_DOWN`
Round to nearest neighbor. Ties are rounded towards zero.

**Examples:**
```kotlin
Deci("2.5").setScale(0, RoundingMode.HALF_DOWN)   // "2"
Deci("-2.5").setScale(0, RoundingMode.HALF_DOWN)  // "-2"
Deci("2.6").setScale(0, RoundingMode.HALF_DOWN)   // "3"
```

#### `HALF_EVEN`
Round to nearest neighbor. Ties are rounded to the nearest even number (banker's rounding).

**Examples:**
```kotlin
Deci("2.5").setScale(0, RoundingMode.HALF_EVEN)  // "2"
Deci("3.5").setScale(0, RoundingMode.HALF_EVEN)  // "4"
Deci("2.6").setScale(0, RoundingMode.HALF_EVEN)  // "3"
```

---

## DeciSerializer

A kotlinx.serialization serializer for the Deci class that serializes/deserializes as strings to preserve precision.

### Usage

The serializer is automatically applied when using `@Serializable` with Deci:

```kotlin
@Serializable
data class Price(val amount: Deci, val currency: String)

val price = Price(Deci("99.99"), "USD")
val json = Json.encodeToString(price)
// {"amount":"99.99","currency":"USD"}
```

### Manual Usage

```kotlin
val deci = Deci("123.45")
val json = Json.encodeToString(DeciSerializer, deci)  // "123.45"
val parsed = Json.decodeFromString(DeciSerializer, json)  // Deci("123.45")
```

**Note:** Invalid strings during deserialization are converted to `Deci.ZERO` using `fromStringOrZero`.

---

## Extension Functions

### `fun Deci.pow(exp: Int): Deci`
Raises a Deci to an integer power.

**Parameters:**
- `exp: Int` - The exponent (must be non-negative)

**Returns:** The result of raising this Deci to the given power

**Throws:**
- `IllegalArgumentException` - If exponent is negative

**Examples:**
```kotlin
Deci("2").pow(3)    // Deci("8")
Deci("10").pow(2)   // Deci("100")
Deci("5").pow(0)    // Deci("1")
```

### `fun Deci.toLong(): Long`
Converts a Deci to Long by first converting to Double.

**Returns:** Long representation (fractional part is truncated)

**Examples:**
```kotlin
Deci("42.7").toLong()   // 42L
Deci("-10.9").toLong()  // -10L
```

### `fun Iterable<Deci>.sumDeci(): Deci`
Sums a collection of Deci values.

**Returns:** The sum of all Deci values in the collection

**Examples:**
```kotlin
val decimals = listOf(Deci("1.5"), Deci("2.25"), Deci("3.75"))
val total = decimals.sumDeci()  // Deci("7.5")

val empty = emptyList<Deci>().sumDeci()  // Deci.ZERO
```

### Deprecated Extension

```kotlin
@Deprecated("Use Deci.fromStringOrZero(value) instead")
fun String.toSafeDeci(): Deci
```

This extension is deprecated in favor of the companion object method `Deci.fromStringOrZero()`.
