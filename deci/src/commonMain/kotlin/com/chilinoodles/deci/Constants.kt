package com.chilinoodles.deci

internal object Constants {
    internal val DECIMAL_REGEX = Regex("""^[-+]?(?:\d+([.,]\d*)?|[.,]\d+)$""")
}