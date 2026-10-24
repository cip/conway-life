package com.cip.conwaylife3d

import org.junit.Assert.*
import org.junit.Test

class RendellTimingTest {
    @Test fun cycleBoundaries() {
        assertEquals(0, RendellTiming.completedCycles(0))
        assertEquals(0, RendellTiming.completedCycles(11_039))
        assertEquals(1, RendellTiming.completedCycles(11_040))
        assertEquals(16, RendellTiming.completedCycles(176_640))
        assertEquals(5_696, RendellTiming.phase(16_736))
        assertEquals(1, RendellTiming.untilNextCycle(11_039))
        assertEquals(0, RendellTiming.untilNextCycle(11_040))
    }
}
