package com.cip.conwaylife3d
import org.junit.Assert.*
import org.junit.Test
import kotlin.random.Random
class HashLifeTest {
    private val slow=SparseLife()
    @Test fun gliderAndOscillators() {
        val examples=listOf(
          setOf(Cell(1,0),Cell(2,1),Cell(0,2),Cell(1,2),Cell(2,2)),
          setOf(Cell(47,47),Cell(48,47),Cell(49,47)),
          setOf(Cell(-2,-2),Cell(-1,-2),Cell(-2,-1),Cell(-1,-1)))
        for(p in examples)for(n in listOf(0,1,2,3,4,8,16,32,64)){
            var expected=p
            repeat(n){expected=slow.next(expected)}
            assertEquals("generation $n",expected,HashLife().advance(p,n))
        }
    }
    @Test fun seededRandomWorldsAgree() {
        val random=Random(12345)
        repeat(15){
            val input=buildSet{
                for(y in -5..5)for(x in -5..5)if(random.nextInt(5)==0)add(Cell(x,y))
            }
            val generations=random.nextInt(1,25)
            var expected=input
            repeat(generations){expected=slow.next(expected)}
            assertEquals("random scenario $it",expected,HashLife().advance(input,generations))
        }
    }
    @Test fun emptyAndZero() {
        assertTrue(HashLife().advance(emptySet(),1000).isEmpty())
        val c=setOf(Cell(0,0))
        assertEquals(c,HashLife().advance(c,0))
    }
}
