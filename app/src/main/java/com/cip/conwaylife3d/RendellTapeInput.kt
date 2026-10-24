package com.cip.conwaylife3d

/**
 * Logical input draft for the original three-symbol Rendell machine.
 * IMPORTANT: The Life preset is fixed. A draft cannot modify the loaded
 * glider-stack encoding until the real physical stack encoding is verified.
 */
object RendellTapeInput {
    const val MAX_SYMBOLS = 6
    const val EXAMPLE = "11"
    fun normalize(text: String) = text.filterNot(Char::isWhitespace)
    fun validate(text: String): String? {
        val value = normalize(text)
        if (value.isEmpty()) return "Enter at least one symbol."
        if (value.length > MAX_SYMBOLS) return "The preset has six stack cells; longer tapes are not supported yet."
        if (value.any { it !in "012" }) return "Use only 0, 1 or 2 (Rendell's three-symbol alphabet)."
        return null
    }
}
