# Migration Guide

This guide helps you migrate from other decimal arithmetic libraries to Deci.

## From Java BigDecimal

If you're currently using Java's `BigDecimal` and want to adopt Deci for Kotlin Multiplatform support:

### Basic Replacements

| BigDecimal | Deci | Notes |
|------------|------|-------|
| `new BigDecimal("123.45")` | `Deci("123.45")` | Constructor syntax |
| `BigDecimal.ZERO` | `Deci.ZERO` | Zero constant |
| `BigDecimal.ONE` | `Deci.ONE` | One constant |
| `BigDecimal.TEN` | `Deci.TEN` | Ten constant |
| `bd1.add(bd2)` | `deci1 + deci2` | Addition |
| `bd1.subtract(bd2)` | `deci1 - deci2` | Subtraction |
| `bd1.multiply(bd2)` | `deci1 * deci2` | Multiplication |
| `bd1.divide(bd2, mc)` | `deci1 / deci2` | Division |

### Precision Control

**BigDecimal:**
```java
BigDecimal result = value.divide(divisor, 2, RoundingMode.HALF_UP);
BigDecimal scaled = value.setScale(2, RoundingMode.HALF_UP);
```

**Deci:**
```kotlin
val result = value.divide(divisor, scale = 2, roundingMode = RoundingMode.HALF_UP)
val scaled = value.setScale(scale = 2, roundingMode = RoundingMode.HALF_UP)
```

### Rounding Mode Mapping

| Java RoundingMode | Deci RoundingMode |
|-------------------|-------------------|
| `RoundingMode.UP` | `RoundingMode.UP` |
| `RoundingMode.DOWN` | `RoundingMode.DOWN` |
| `RoundingMode.CEILING` | `RoundingMode.CEILING` |
| `RoundingMode.FLOOR` | `RoundingMode.FLOOR` |
| `RoundingMode.HALF_UP` | `RoundingMode.HALF_UP` |
| `RoundingMode.HALF_DOWN` | `RoundingMode.HALF_DOWN` |
| `RoundingMode.HALF_EVEN` | `RoundingMode.HALF_EVEN` |

### Method Mappings

| BigDecimal Method | Deci Equivalent | Notes |
|-------------------|-----------------|-------|
| `compareTo(other)` | `compareTo(other)` | Same behavior |
| `equals(other)` | `==` or `equals(other)` | Kotlin operator |
| `signum()` | `isPositive()`, `isNegative()`, `isZero()` | More explicit |
| `abs()` | `abs()` | Same name |
| `negate()` | `negate()` | Same name |
| `max(other)` | `max(other)` | Same name |
| `min(other)` | `min(other)` | Same name |
| `toString()` | `toString()` | Same behavior |
| `doubleValue()` | `toDouble()` | Kotlin naming |

### Example Migration

**Before (Java BigDecimal):**
```java
BigDecimal price = new BigDecimal("99.99");
BigDecimal tax = new BigDecimal("0.08");
BigDecimal total = price.multiply(tax.add(BigDecimal.ONE));
BigDecimal rounded = total.setScale(2, RoundingMode.HALF_UP);
```

**After (Deci):**
```kotlin
val price = Deci("99.99")
val tax = Deci("0.08") 
val total = price * (tax + Deci.ONE)
val rounded = total.setScale(2, RoundingMode.HALF_UP)
```

## From Other Decimal Libraries

### From JavaScript decimal.js

| decimal.js | Deci | Notes |
|------------|------|-------|
| `new Decimal('123.45')` | `Deci("123.45")` | Constructor |
| `x.plus(y)` | `x + y` | Addition operator |
| `x.minus(y)` | `x - y` | Subtraction operator |
| `x.times(y)` | `x * y` | Multiplication operator |
| `x.div(y)` | `x / y` | Division operator |
| `x.toFixed(2)` | `x.setScale(2, RoundingMode.HALF_UP)` | Rounding |

### From Python decimal module

| Python decimal | Deci | Notes |
|----------------|------|-------|
| `Decimal('123.45')` | `Deci("123.45")` | Constructor |
| `x + y` | `x + y` | Same syntax |
| `x - y` | `x - y` | Same syntax |
| `x * y` | `x * y` | Same syntax |
| `x / y` | `x / y` | Same syntax |
| `x.quantize(Decimal('0.01'))` | `x.setScale(2, RoundingMode.HALF_EVEN)` | Quantization |

## Common Patterns

### Safe String Parsing

**Before:**
```java
// Java
try {
    BigDecimal value = new BigDecimal(userInput);
} catch (NumberFormatException e) {
    // Handle error
}
```

**After:**
```kotlin
// Deci - Multiple approaches
val value1 = Deci.fromStringOrNull(userInput) ?: defaultValue
val value2 = Deci.fromStringOrZero(userInput)
```

### Collection Operations

**Before:**
```java
// Java
BigDecimal sum = list.stream()
    .reduce(BigDecimal.ZERO, BigDecimal::add);
```

**After:**
```kotlin
// Deci
val sum = list.sumDeci()
```

### Serialization

**Before (manual JSON handling):**
```java
// Java - manual approach
public class Price {
    @JsonSerialize(using = BigDecimalSerializer.class)
    private BigDecimal amount;
}
```

**After:**
```kotlin
// Deci - automatic kotlinx.serialization
@Serializable
data class Price(val amount: Deci)
```

## Platform-Specific Considerations

### Android Migration

When migrating Android projects:

1. **Remove BigDecimal imports** in favor of Deci
2. **Update Proguard rules** if using code obfuscation
3. **Update serialization logic** to use kotlinx.serialization
4. **Test precision** in calculations to ensure consistency

### iOS Integration

When adding iOS support to existing JVM/Android projects:

1. **Verify rounding behavior** matches expectations
2. **Test edge cases** with very large or very small numbers
3. **Validate string parsing** with locale-specific inputs
4. **Check NSDecimalNumber limitations** if any

## Performance Considerations

### Memory Usage

- **Deci objects are immutable** - consider object reuse patterns
- **Platform implementations vary** - profile on target platforms
- **String parsing overhead** - cache parsed values when possible

### Computational Performance

- **Operator overloading** has minimal overhead
- **Platform-specific optimizations** are automatically applied
- **Precision vs. speed** - Deci prioritizes precision

## Testing Your Migration

### Regression Testing

1. **Create test cases** for all existing decimal calculations
2. **Verify precision** matches previous results
3. **Test edge cases** (zero, negative numbers, very large/small values)
4. **Validate rounding behavior** with your specific rounding needs

### Platform Testing

1. **Run tests on all target platforms** to ensure consistency
2. **Test serialization/deserialization** across platforms
3. **Verify UI formatting** displays correctly
4. **Check locale handling** for international users

### Example Test Structure

```kotlin
class MigrationTest {
    @Test
    fun `BigDecimal calculations match Deci results`() {
        // Compare results between BigDecimal and Deci
        val bdResult = JavaCalculation.calculate()
        val deciResult = DeciCalculation.calculate()
        assertEquals(bdResult.toString(), deciResult.toString())
    }
    
    @Test
    fun `precision is maintained across platforms`() {
        val testCases = listOf("123.456", "0.1", "99999.99999")
        testCases.forEach { input ->
            val deci = Deci(input)
            assertEquals(input, deci.toString())
        }
    }
}
```

## Common Pitfalls and Solutions

### 1. Floating Point Input

**Problem:**
```kotlin
// This may lose precision
val deci = Deci(0.1) // Not recommended
```

**Solution:**
```kotlin
// Use string constructor for exact values
val deci = Deci("0.1") // Recommended
```

### 2. Platform Differences

**Problem:** Assuming all platforms behave identically without testing.

**Solution:** Run comprehensive tests on all target platforms, especially around edge cases and rounding behavior.

### 3. Serialization Format

**Problem:** Expecting number serialization instead of string.

**Solution:** Deci serializes as strings to preserve precision. Adjust client expectations accordingly.

### 4. Exception Handling

**Problem:** Not handling the different parsing methods appropriately.

**Solution:**
- Use `Deci(string)` when input should always be valid
- Use `fromStringOrNull()` when you want to handle invalid input
- Use `fromStringOrZero()` when you want a default value

## Getting Help

If you encounter issues during migration:

1. **Check the API documentation** for method mappings
2. **Review test cases** for usage examples  
3. **Compare results** with your existing implementation
4. **Test on all target platforms** to identify platform-specific issues
5. **Create minimal reproduction cases** for any unexpected behavior
