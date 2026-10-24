package com.cip.conwaylife3d

import org.junit.Assert.*
import org.junit.Test
import java.io.File

/** CI-only, exact cell-coordinate comparison against independently generated bgolly output. */
class RendellReferenceTest {
    @Test fun generation16384MatchesBgolly() {
        val originalFile=System.getProperty("rendell.input")?.let(::File)
            ?: error("Missing rendell.input: CI reference data is mandatory")
        val expectedFile=System.getProperty("rendell.reference")?.let(::File)
            ?: error("Missing rendell.reference: CI reference data is mandatory")
        val original=LifeRle.decode(originalFile.readText()).cells
        assertEquals("wrong source pattern", 36_549, original.size)
        val expectedText=expectedFile.readText()
        val expected=LifeRle.decode(expectedText).cells
        // bgolly's command-line RLE export does not include #CXRLE Pos.
        // Compare all cells in their bounding-box coordinates. This independently
        // detects phantom cells and wrong evolution, but not pure translations.
        fun normalize(cells: Set<Cell>): Set<Cell> {
            if (cells.isEmpty()) return cells
            val minX = cells.minOf { it.x }
            val minY = cells.minOf { it.y }
            return cells.mapTo(HashSet(cells.size)) { Cell(it.x - minX, it.y - minY) }
        }
        val actual=HashLife().advance(original,16_384)
        assertEquals("population differs",expected.size,actual.size)
        assertEquals("cell geometry differs from independent bgolly",
            normalize(expected),normalize(actual))

    }
}
