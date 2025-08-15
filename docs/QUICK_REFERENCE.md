# Deci Quick Reference

A quick reference guide for the most commonly used Deci operations.

## Installation

```kotlin
dependencies {
    implementation("com.chilinoodles.deci:deci:1.0.0")
}
```

## Import

```kotlin
import com.chilinoodles.deci.Deci
import com.chilinoodles.deci.RoundingMode
```

## Creation

```kotlin
// Recommended - exact precision
val d1 = Deci("123.45")
val d2 = Deci("1,234.56")  // Comma converted to dot

// From numbers
val d3 = Deci(42)
val d4 = Deci(42L)
val d5 = Deci(42.5)        // Use strings for precision

// Constants
val zero = Deci.ZERO       // "0"
val one = Deci.ONE         // "1"
val ten = Deci.TEN         // "10"
```

## Safe Parsing

```kotlin
val safe1 = Deci.fromStringOrNull("123.45")  // Deci or null
val safe2 = Deci.fromStringOrZero("invalid") // Deci.ZERO
```

## Basic Arithmetic

```kotlin
val a = Deci("10.5")
val b = Deci("2.25")

val sum = a + b           // 12.75
val diff = a - b          // 8.25
val product = a * b       // 23.625
val quotient = a / b      // 4.666...
```

## Precision Control

```kotlin
val value = Deci("10") / Deci("3")

// Set decimal places
val rounded = value.setScale(2, RoundingMode.HALF_UP)  // 3.33

// Divide with specific precision
val precise = Deci("10").divide(
    divisor = Deci("3"), 
    scale = 4, 
    roundingMode = RoundingMode.HALF_UP
)  // 3.3333
```

## Rounding Modes

```kotlin
val value = Deci("2.5")

value.setScale(0, RoundingMode.UP)         // 3
value.setScale(0, RoundingMode.DOWN)       // 2
value.setScale(0, RoundingMode.HALF_UP)    // 3
value.setScale(0, RoundingMode.HALF_DOWN)  // 2
value.setScale(0, RoundingMode.HALF_EVEN)  // 2 (banker's)
value.setScale(0, RoundingMode.CEILING)    // 3
value.setScale(0, RoundingMode.FLOOR)      // 2
```

## Comparison

```kotlin
val a = Deci("10.5")
val b = Deci("5.25")

// Operators
a > b           // true
a >= b          // true
a < b           // false
a <= b          // false
a == b          // false
a != b          // true

// Methods
a.compareTo(b)  // positive number
a.max(b)        // Deci("10.5")
a.min(b)        // Deci("5.25")
```

## Sign Operations

```kotlin
val positive = Deci("10.5")
val negative = Deci("-5.25")
val zero = Deci("0")

// Checking signs
positive.isPositive()  // true
negative.isNegative()  // true
zero.isZero()         // true

// Transformations
negative.abs()        // Deci("5.25")
positive.negate()     // Deci("-10.5")
```

## Collections

```kotlin
val decimals = listOf(Deci("1.5"), Deci("2.25"), Deci("3.75"))

// Sum all values
val total = decimals.sumDeci()  // Deci("7.5")

// Sort
val sorted = decimals.sorted()

// Find min/max
val min = decimals.minOrNull()
val max = decimals.maxOrNull()
```

## Extensions

```kotlin
// Power
val squared = Deci("5").pow(2)    // Deci("25")

// Convert to Long
val longVal = Deci("42.7").toLong()  // 42L

// Convert to Double
val doubleVal = Deci("42.5").toDouble()  // 42.5
```

## Serialization

```kotlin
@Serializable
data class Price(val amount: Deci, val currency: String)

val price = Price(Deci("99.99"), "USD")

// Serialize to JSON
val json = Json.encodeToString(price)
// {"amount":"99.99","currency":"USD"}

// Deserialize from JSON
val parsed = Json.decodeFromString<Price>(json)
```

## Common Patterns

### Financial Calculation
```kotlin
val subtotal = Deci("100.00")
val taxRate = Deci("0.08")
val total = subtotal * (Deci.ONE + taxRate)
val finalAmount = total.setScale(2, RoundingMode.HALF_UP)
```

### Percentage Calculation
```kotlin
val value = Deci("200")
val percentage = Deci("15")  // 15%
val result = value * percentage / Deci("100")  // 30
```

### Average Calculation
```kotlin
val values = listOf(Deci("10"), Deci("20"), Deci("30"))
val average = values.sumDeci() / Deci(values.size)  // 20
```

### Safe Division
```kotlin
fun safeDivide(dividend: Deci, divisor: Deci): Deci? {
    return if (divisor.isZero()) null else dividend / divisor
}
```

### Validation
```kotlin
fun validatePrice(input: String): Deci? {
    val price = Deci.fromStringOrNull(input) ?: return null
    return if (price.isPositive()) price else null
}
```

## Error Handling

```kotlin
// Division by zero
try {
    val result = Deci("10") / Deci.ZERO  // Throws ArithmeticException
} catch (e: ArithmeticException) {
    // Handle error
}

// Invalid scale
try {
    val result = Deci("10").setScale(-1, RoundingMode.UP)  // Throws IllegalArgumentException
} catch (e: IllegalArgumentException) {
    // Handle error
}

// Invalid string format
try {
    val result = Deci("invalid")  // Throws IllegalArgumentException
} catch (e: IllegalArgumentException) {
    // Handle error
}
```

## Performance Tips

1. **Use string constructors** for exact values: `Deci("0.1")` not `Deci(0.1)`
2. **Cache constants** when used frequently
3. **Reuse Deci instances** when possible (they're immutable)
4. **Use appropriate precision** - don't over-specify scales
5. **Choose parsing method** based on error handling needs

## Platform Support

- ✅ **Android** (API 21+)
- ✅ **iOS** (iOS 12+)  
- ✅ **JVM** (Java 17+)
- 🚧 **WebAssembly** (planned)

## Need More Help?

- [Full API Documentation](API.md)
- [Architecture Guide](ARCHITECTURE.md)
- [Migration Guide](MIGRATION.md)
- [Main README](../README.md)
