package com.cip.conwaylife3d

import org.junit.Assert.*
import org.junit.Test

class LifeTest {
    @Test fun emptyRemainsEmpty() {
        val life = Life(); assertTrue(life.next(life.empty()).none { it })
    }
    @Test fun blockStillLife() {
        val life = Life(); val a = life.empty()
        for (y in 10..11) for (x in 10..11) a[y * 48 + x] = true
        assertArrayEquals(a, life.next(a))
    }
    @Test fun blinkerPeriodTwo() {
        val life = Life(); val a = life.empty()
        for (x in 10..12) a[11 * 48 + x] = true
        assertArrayEquals(a, life.next(life.next(a)))
    }
    @Test fun wrappingAcrossEdges() {
        val life = Life(); val a = life.empty()
        a[0] = true; a[1] = true; a[47] = true
        assertTrue(life.next(a)[47 * 48])
    }
}
