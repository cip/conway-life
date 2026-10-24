package com.cip.conwaylife3d

import org.junit.Assert.*
import org.junit.Test

class LifeCheckpointTest {
    @Test fun observesActualCoordinatesIncludingNegatives() {
        val cp = LifeCheckpoint.from(11_040, setOf(Cell(-8, 42), Cell(9, -3), Cell(3, 11)))
        assertEquals(11_040, cp.generation)
        assertEquals(3, cp.population)
        assertEquals(-8, cp.minX)
        assertEquals(-3, cp.minY)
        assertEquals(9, cp.maxX)
        assertEquals(42, cp.maxY)
    }
    @Test fun emptyWorldHasNoBounds() {
        val cp = LifeCheckpoint.from(0, emptySet())
        assertEquals(0, cp.population)
        assertNull(cp.minX)
        assertNull(cp.maxY)
    }
    @Test fun nextCycleUsesRealBoundary() {
        assertEquals(11_040, RendellTiming.GENERATIONS_PER_CYCLE)
        assertEquals(11_040, RendellTiming.untilNextCycle(0).let { if (it == 0) RendellTiming.GENERATIONS_PER_CYCLE else it })
        assertEquals(5_696, RendellTiming.untilNextCycle(16_384))
    }
}
