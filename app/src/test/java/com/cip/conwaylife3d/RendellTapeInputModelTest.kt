package com.cip.conwaylife3d
import org.junit.Assert.*
import org.junit.Test

class RendellTapeInputModelTest {
    @Test fun publishedExampleMapsToRightStack() {
        val draft = RendellStackDraft("", RendellTapeInput.EXAMPLE_RIGHT)
        assertNull(RendellTapeInput.validate(draft))
        assertEquals("11", RendellTapeInput.normalize(draft.rightNearestFirst))
    }
    @Test fun bothStacksPreserveHeadFirstOrder() {
        val draft = RendellStackDraft("2 1 0", "1 2")
        assertNull(RendellTapeInput.validate(draft))
        assertEquals("210", RendellTapeInput.normalize(draft.leftNearestFirst))
        assertEquals("12", RendellTapeInput.normalize(draft.rightNearestFirst))
    }
    @Test fun rejectsInvalidAndEmptyStacks() {
        assertNotNull(RendellTapeInput.validate(RendellStackDraft("", "")))
        assertNotNull(RendellTapeInput.validate(RendellStackDraft("x", "11")))
        assertNotNull(RendellTapeInput.validate(RendellStackDraft("", "1111111")))
    }
}
