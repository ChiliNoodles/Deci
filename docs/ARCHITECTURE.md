# Deci Architecture Documentation

This document describes the architecture and implementation details of the Deci library.

## Overview

Deci is a Kotlin Multiplatform library that provides consistent high-precision decimal arithmetic across multiple platforms. The library uses platform-specific implementations to ensure optimal performance while maintaining a unified API.

## Architecture Principles

1. **Platform Abstraction**: Uses Kotlin's `expect`/`actual` mechanism to provide platform-specific implementations
2. **Precision First**: Prioritizes mathematical precision over performance where trade-offs are necessary
3. **Immutability**: All operations return new instances, ensuring thread safety
4. **Error Safety**: Provides safe parsing methods alongside throwing variants

## Platform-Specific Implementations

### Android/JVM Implementation

**File**: `deci/src/androidMain/kotlin/com/chilinoodles/deci/Deci.android.kt`

Uses Java's `BigDecimal` class as the underlying implementation:

```kotlin
actual class Deci(private val internal: BigDecimal) : Comparable<Deci>
```

**Advantages:**
- Battle-tested implementation with well-defined precision semantics
- Extensive rounding mode support
- Optimal performance on JVM-based platforms
- Direct interoperability with existing Java codebases

**Key Features:**
- Uses `BigDecimal.stripTrailingZeros()` for consistent string representation
- Implements precision-aware division with `MathContext.DECIMAL128`
- Provides comprehensive rounding mode conversion from Deci's enum to Java's RoundingMode

### iOS Implementation  

**File**: `deci/src/iosMain/kotlin/com/chilinoodles/deci/Deci.ios.kt`

Uses Foundation's `NSDecimalNumber` as the underlying implementation:

```kotlin
actual class Deci private constructor(private val internal: NSDecimalNumber) : Comparable<Deci>
```

**Advantages:**
- Native iOS performance and memory management
- Direct integration with iOS Foundation framework
- Consistent with iOS app ecosystem expectations

**Key Features:**
- Custom rounding mode conversion to handle differences between Java and iOS semantics
- Uses `NSDecimalNumberHandler` for precision control
- Implements division-by-zero protection
- Provides proper handling of positive/negative zero

**Rounding Mode Mapping:**
The iOS implementation includes sophisticated logic to map Deci's rounding modes to NSRoundingMode:

```kotlin
private fun toNativeMode(mode: RoundingMode): NSRoundingMode = when (mode) {
    RoundingMode.UP -> if (isPositive()) NSRoundUp else NSRoundDown
    RoundingMode.DOWN -> if (isPositive()) NSRoundDown else NSRoundUp
    RoundingMode.CEILING -> NSRoundUp
    RoundingMode.FLOOR -> NSRoundDown
    RoundingMode.HALF_UP -> NSRoundPlain
    RoundingMode.HALF_DOWN -> NSRoundDown
    RoundingMode.HALF_EVEN -> NSRoundBankers
}
```

### JVM Implementation

**File**: `deci/src/jvmMain/kotlin/com/chilinoodles/deci/Deci.jvm.kt`

Currently shows placeholder implementation with `TODO()` statements. The Android implementation serves as the reference implementation for JVM targets.

### WebAssembly Implementation

**File**: `deci/src/wasmJsMain/kotlin/com/chilinoodles/deci/Deci.wasmJs.kt`

Currently shows placeholder implementation with `TODO()` statements, indicating future support for WebAssembly targets.

## Core Components

### 1. Common Interface

**File**: `deci/src/commonMain/kotlin/com/chilinoodles/deci/Deci.kt`

Defines the common API using Kotlin's `expect` class:

```kotlin
@Serializable(with = DeciSerializer::class)
expect class Deci : Comparable<Deci> {
    // Constructor definitions
    // Operator overloads
    // Utility methods
    // Companion object
}
```

### 2. Serialization Support

**File**: `deci/src/commonMain/kotlin/com/chilinoodles/deci/DeciSerializer.kt`

Provides kotlinx.serialization support:

```kotlin
object DeciSerializer : KSerializer<Deci> {
    override val descriptor = PrimitiveSerialDescriptor("Deci", PrimitiveKind.STRING)
    
    override fun serialize(encoder: Encoder, value: Deci) {
        encoder.encodeString(value.toString())
    }
    
    override fun deserialize(decoder: Decoder): Deci {
        return Deci.fromStringOrZero(decoder.decodeString())
    }
}
```

**Design Decisions:**
- Serializes as strings to preserve exact precision
- Uses `fromStringOrZero` for fault-tolerant deserialization
- Integrates seamlessly with kotlinx.serialization ecosystem

### 3. Rounding Modes

**File**: `deci/src/commonMain/kotlin/com/chilinoodles/deci/RoundingMode.kt`

Provides platform-agnostic rounding mode definitions:

```kotlin
enum class RoundingMode {
    UP, DOWN, CEILING, FLOOR, HALF_UP, HALF_DOWN, HALF_EVEN
}
```

Maps to appropriate platform-specific rounding modes in each implementation.

### 4. Input Validation

**File**: `deci/src/commonMain/kotlin/com/chilinoodles/deci/Constants.kt`

Defines regex for decimal validation:

```kotlin
internal val DECIMAL_REGEX = Regex("""^[-+]?(?:\d+([.,]\d*)?|[.,]\d+)$""")
```

**Supported Formats:**
- Standard decimal notation: `123.45`, `-67.89`
- Leading decimal point: `.5`, `-.25`
- Trailing decimal point: `123.`, `-45.`
- Comma as decimal separator: `123,45` (converted to dot)
- Optional plus sign: `+123.45`

### 5. Utility Functions

**File**: `deci/src/commonMain/kotlin/com/chilinoodles/deci/Utils.kt`

Provides string manipulation utilities:

```kotlin
internal val String.replaceCommaWithDot: String
    get() = this.replace(',', '.')
```

## Design Patterns

### 1. Expect/Actual Pattern

The library extensively uses Kotlin Multiplatform's expect/actual mechanism:

- **Common module**: Defines the API contract
- **Platform modules**: Provide platform-specific implementations
- **Type safety**: Ensures consistent API across platforms

### 2. Immutable Value Objects

All Deci instances are immutable:

- Thread-safe by design
- Functional programming friendly
- Predictable behavior in concurrent environments

### 3. Operator Overloading

Natural mathematical syntax:

```kotlin
val result = deci1 + deci2 * deci3 / deci4
```

### 4. Safe Parsing Strategy

Multiple parsing approaches for different use cases:

- `Deci(string)`: Throws on invalid input (fail-fast)
- `fromStringOrNull(string)`: Returns null on invalid input (optional handling)
- `fromStringOrZero(string)`: Returns zero on invalid input (default value)

## Error Handling Strategy

### Input Validation
- String parsing validates against regex before platform-specific parsing
- Scale parameters validated to be non-negative
- Division by zero consistently throws `ArithmeticException`

### Platform Consistency
- All platforms throw the same exception types for the same conditions
- Error messages are consistent across platforms where possible
- Platform-specific errors are wrapped in common exception types

## Testing Strategy

### Common Tests
**File**: `deci/src/commonTest/kotlin/com/chilinoodles/deci/DeciTest.kt`

- Comprehensive test coverage of all public APIs
- Cross-platform behavior verification
- Edge case testing (zero, negative zero, precision boundaries)
- Serialization roundtrip testing

### Regex Validation Tests
**File**: `deci/src/commonTest/kotlin/com/chilinoodles/deci/DeciRegexTest.kt`

- Validates input format acceptance/rejection
- Ensures consistent parsing behavior across platforms

## Performance Considerations

### Memory Efficiency
- Immutable objects enable sharing and caching
- Platform-specific implementations use native memory management
- String parsing is optimized with regex pre-validation

### Computational Efficiency
- Leverages platform-optimized decimal arithmetic libraries
- Avoids unnecessary object creation in arithmetic operations
- Uses efficient comparison implementations

## Future Architecture Considerations

### WebAssembly Support
- Planned implementation using JavaScript's decimal arithmetic
- Will require careful precision management
- Should maintain API compatibility with other platforms

### Additional Platforms
- Kotlin/Native support for more platforms
- Server-side JavaScript support
- Maintaining consistent precision semantics across all targets

### API Evolution
- Backwards compatibility preservation
- Deprecation strategy for API changes
- Extension point design for future enhancements
