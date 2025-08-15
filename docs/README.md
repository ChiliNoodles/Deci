# Deci Documentation

Welcome to the comprehensive documentation for the Deci Kotlin Multiplatform decimal arithmetic library.

## Documentation Structure

### [API Reference](API.md)
Complete API documentation for all classes, methods, and functions in the Deci library. Includes detailed parameter descriptions, return values, exceptions, and usage examples.

**Contents:**
- Deci class comprehensive API
- RoundingMode enum values and behavior
- DeciSerializer for kotlinx.serialization
- Extension functions and utilities
- Code examples for all major use cases

### [Architecture Guide](ARCHITECTURE.md)
Deep dive into the internal architecture and design decisions of the Deci library.

**Contents:**
- Platform-specific implementation details
- Design patterns and principles
- Error handling strategies
- Testing approach
- Performance considerations
- Future architecture plans

### [Migration Guide](MIGRATION.md)
Step-by-step guide for migrating from other decimal arithmetic libraries to Deci.

**Contents:**
- Migration from Java BigDecimal
- Migration from JavaScript decimal.js
- Migration from Python decimal module
- Common patterns and anti-patterns
- Platform-specific considerations
- Testing your migration

## Quick Navigation

### For New Users
1. Start with the main [README](../README.md) for installation and basic usage
2. Review the [API Reference](API.md) for detailed method documentation
3. Check out the sample app in `/sample/composeApp/` for working examples

### For Migrating Users
1. Read the [Migration Guide](MIGRATION.md) for your specific source library
2. Use the [API Reference](API.md) to find equivalent methods
3. Test your migration thoroughly across all target platforms

### For Contributors
1. Study the [Architecture Guide](ARCHITECTURE.md) to understand the codebase
2. Review existing tests in `/deci/src/commonTest/`
3. Follow the platform-specific implementation patterns

## Key Features Overview

### Cross-Platform Consistency
Deci provides identical API and behavior across:
- Android (using BigDecimal)
- iOS (using NSDecimalNumber) 
- JVM (using BigDecimal)
- WebAssembly (planned)

### Precision-First Design
- Exact decimal arithmetic without floating-point errors
- Configurable precision and rounding modes
- Safe string parsing with multiple error handling strategies

### Developer-Friendly API
- Natural operator overloading (`+`, `-`, `*`, `/`)
- Kotlinx.serialization support out of the box
- Comprehensive utility functions and extensions
- Immutable value objects for thread safety

### Production-Ready
- Extensive test coverage across all platforms
- Performance-optimized platform-specific implementations
- Clear error handling and validation
- MIT license for commercial use

## Examples by Use Case

### Financial Calculations
```kotlin
val price = Deci("99.99")
val taxRate = Deci("0.08")
val total = price * (Deci.ONE + taxRate)
val rounded = total.setScale(2, RoundingMode.HALF_UP)
```

### Scientific Calculations
```kotlin
val measurement = Deci("123.456789")
val precision = measurement.setScale(3, RoundingMode.HALF_EVEN)
val squared = measurement.pow(2)
```

### Data Processing
```kotlin
val values = listOf(Deci("1.1"), Deci("2.2"), Deci("3.3"))
val average = values.sumDeci() / Deci(values.size)
```

### Serialization
```kotlin
@Serializable
data class Product(val price: Deci, val name: String)

val product = Product(Deci("29.99"), "Widget")
val json = Json.encodeToString(product)
```

## Performance Notes

### Platform Optimizations
- **Android/JVM**: Uses highly optimized BigDecimal implementation
- **iOS**: Leverages native NSDecimalNumber for memory efficiency
- **All platforms**: Immutable objects enable compiler optimizations

### Best Practices
- Use string constructors for exact values: `Deci("0.1")` vs `Deci(0.1)`
- Cache frequently used values when possible
- Prefer `setScale()` over repeated division for formatting
- Use appropriate parsing methods for your error handling needs

## Version History

### Version 1.0.0
- Initial release with Android, iOS, and JVM support
- Complete API with operator overloading
- Kotlinx.serialization integration
- Comprehensive test suite
- MIT license

## Getting Support

### Documentation Issues
If you find errors or gaps in this documentation, please file an issue with:
- Specific page and section
- Description of the issue
- Suggested improvement

### Library Issues  
For bugs or feature requests:
- Include minimal reproduction case
- Specify target platforms affected
- Provide expected vs actual behavior

### Community
- Check existing issues before creating new ones
- Include platform information in bug reports
- Follow coding standards for contributions

## Contributing to Documentation

Documentation improvements are welcome! Please ensure:
- Examples are tested and work correctly
- Code follows the project's Kotlin style guide
- New content fits the existing documentation structure
- All platforms are considered in examples

Thank you for using Deci!
