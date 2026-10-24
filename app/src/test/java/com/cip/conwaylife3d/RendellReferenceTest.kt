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
        // bgolly must emit absolute coordinates; otherwise fail instead of comparing
        // two independent but misaligned images and silently accepting translations.
        val position=Regex("""(?m)^#CXRLE\s+Pos=([-+]?\d+),([-+]?\d+)""").find(expectedText)
            ?: error("bgolly reference lacks #CXRLE Pos=...; cannot assert absolute coordinates")
        val dx=position.groupValues[1].toInt()
        val dy=position.groupValues[2].toInt()
        val absoluteExpected=expected.mapTo(HashSet(expected.size)){Cell(it.x+dx,it.y+dy)}
        val actual=HashLife().advance(original,16_384)
        assertEquals("population differs",absoluteExpected.size,actual.size)
        assertEquals("actual positions differ from independent bgolly",absoluteExpected,actual)
    }
}
