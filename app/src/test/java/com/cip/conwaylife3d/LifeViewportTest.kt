package com.cip.conwaylife3d
import org.junit.Assert.*
import org.junit.Test
class LifeViewportTest {
    @Test fun fitsWidePatternWithoutCropping() {
        val v = LifeViewport.fit(setOf(Cell(0,0),Cell(1713,1646)), 450, 800)
        assertEquals(857f,v.centerX,0.01f)
        assertEquals(823.5f,v.centerY,0.01f)
        assertTrue(1714*v.pxPerCell <= 450)
        assertTrue(1647*v.pxPerCell <= 800)
    }
    @Test fun negativeAndTinyPatterns() {
        val v = LifeViewport.fit(setOf(Cell(-10,-20),Cell(-8,-17)), 200, 300)
        assertEquals(-8.5f,v.centerX,0.01f)
        assertEquals(-18f,v.centerY,0.01f)
        assertEquals(60f,v.pxPerCell,0.01f)
    }
}
