package com.cip.conwaylife3d
import org.junit.Assert.*
import org.junit.Test
class RendellTapeInputTest {
    @Test fun validatesOriginalInputAndAlphabet() {
        assertNull(RendellTapeInput.validate("11"))
        assertNull(RendellTapeInput.validate("0 1 2"))
        assertEquals("012", RendellTapeInput.normalize("0 1 2"))
    }
    @Test fun rejectsInvalidAndTooLongInputs() {
        assertNotNull(RendellTapeInput.validate(""))
        assertNotNull(RendellTapeInput.validate("1X1"))
        assertNotNull(RendellTapeInput.validate("1111111"))
    }
    @Test fun stagedDraftDoesNotModifyWorld() {
        val initial = setOf(Cell(0,0),Cell(1,0),Cell(0,1),Cell(1,1))
        val before = initial.toSet()
        RendellTapeInput.validate("212")
        assertEquals(before, initial)
    }
}
