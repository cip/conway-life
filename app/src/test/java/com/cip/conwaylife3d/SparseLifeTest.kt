package com.cip.conwaylife3d

import org.junit.Assert.*
import org.junit.Test

class SparseLifeTest {
    @Test fun gliderTranslatesAfterFourSteps() {
        val engine = SparseLife()
        val original = setOf(Cell(1,0), Cell(2,1), Cell(0,2), Cell(1,2), Cell(2,2))
        var state = original
        repeat(4) { state = engine.next(state) }
        assertEquals(original.map { Cell(it.x+1,it.y+1) }.toSet(), state)
    }
    @Test fun patternsCanCrossLegacyBoardBoundary() {
        val engine = SparseLife()
        val current = setOf(Cell(47,47), Cell(48,47), Cell(49,47))
        val next = engine.next(current)
        assertTrue(Cell(48,46) in next)
        assertTrue(Cell(48,48) in next)
        assertEquals(3, next.size)
    }
    @Test fun rleDecodesGlider() {
        val pattern=LifeRle.decode("#N Glider\nx = 3, y = 3, rule = B3/S23\nbob\$2bo\$3o!")
        assertEquals(5,pattern.cells.size)
        assertTrue(Cell(1,0) in pattern.cells)
    }
    @Test fun parserRejectsOtherRules() {
        try {
            LifeRle.decode("x = 3, y = 3, rule = B36/S23\n3o!")
            fail("must reject non-Conway rule")
        } catch (expected: IllegalArgumentException) { }
    }
}
