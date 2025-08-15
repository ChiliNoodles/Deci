package com.chilinoodles.deci

internal val String.replaceCommaWithDot: String
    get() = this.replace(',', '.')