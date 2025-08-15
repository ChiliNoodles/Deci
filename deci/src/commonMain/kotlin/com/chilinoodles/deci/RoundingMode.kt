package com.chilinoodles.deci

/**
 * Specifies how rounding should be performed when precision needs to be reduced.
 */
enum class RoundingMode {
    /**
     * Round away from zero. Always increments the digit prior to a discarded fraction.
     * Example: 2.5 → 3, -2.5 → -3
     */
    UP,

    /**
     * Round towards zero (truncate). Never increments the digit prior to a discarded fraction.
     * Example: 2.9 → 2, -2.9 → -2
     */
    DOWN,

    /**
     * Round towards positive infinity.
     * Example: 2.1 → 3, -2.1 → -2
     */
    CEILING,

    /**
     * Round towards negative infinity.
     * Example: 2.9 → 2, -2.1 → -3
     */
    FLOOR,

    /**
     * Round to nearest neighbor. Ties are rounded away from zero.
     * Example: 2.5 → 3, -2.5 → -3, 2.4 → 2
     */
    HALF_UP,

    /**
     * Round to nearest neighbor. Ties are rounded towards zero.
     * Example: 2.5 → 2, -2.5 → -2, 2.6 → 3
     */
    HALF_DOWN,

    /**
     * Round to nearest neighbor. Ties are rounded to the nearest even number (banker's rounding).
     * Example: 2.5 → 2, 3.5 → 4, 2.6 → 3
     */
    HALF_EVEN
}