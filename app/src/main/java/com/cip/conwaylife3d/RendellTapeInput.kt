package com.cip.conwaylife3d

/**
 * Rendell's tape consists of two stacks on either side of the read/write head.
 *
 * Logical model only: it is NOT a Life glider encoder or a software simulation
 * of the running machine. The symbol nearest the head is FIRST in each string.
 * The head symbol itself is in flight during the pop/push cycle and cannot be
 * read from a static stack snapshot.
 */
data class RendellStackDraft(val leftNearestFirst: String, val rightNearestFirst: String)

object RendellTapeInput {
    const val MAX_SYMBOLS_PER_STACK = 6
    const val EXAMPLE_RIGHT = "11"
    fun normalize(text: String) = text.filterNot(Char::isWhitespace)
    fun validateStack(text: String): String? {
        val value = normalize(text)
        if (value.length > MAX_SYMBOLS_PER_STACK)
            return "Up to six symbols per stack in this draft; physical capacity is not verified."
        if (value.any { it !in "012" })
            return "Only symbols 0, 1 and 2 are supported."
        return null
    }
    fun validate(draft: RendellStackDraft): String? {
        val leftError = validateStack(draft.leftNearestFirst)
        if (leftError != null) return "Left stack: $leftError"
        val rightError = validateStack(draft.rightNearestFirst)
        if (rightError != null) return "Right stack: $rightError"
        if (normalize(draft.leftNearestFirst).isEmpty() && normalize(draft.rightNearestFirst).isEmpty())
            return "At least one stack must contain a symbol."
        return null
    }
    // Preserve earlier simple validation callers: text describes the right stack.
    fun validate(text: String) = validate(RendellStackDraft("", text))
}
