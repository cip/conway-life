package com.cip.conwaylife3d

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Explain known Rendell machine timing without claiming to decode the glider tape.
 * User-visible observations come from the actual Life world passed in.
 */
object RendellTiming {
    const val GENERATIONS_PER_CYCLE = 11_040
    fun completedCycles(generation: Int) = generation / GENERATIONS_PER_CYCLE
    fun phase(generation: Int) = generation % GENERATIONS_PER_CYCLE
    fun untilNextCycle(generation: Int): Int =
        if (phase(generation) == 0) 0 else GENERATIONS_PER_CYCLE - phase(generation)
}

@Composable
fun RendellInspector(generation: Int, population: Int, originalPattern: Boolean) {
    ElevatedCard(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text("Rendell Turing Machine · Inspector", style = MaterialTheme.typography.titleMedium)
            Text("Life generation: $generation · live cells: $population")
            if (originalPattern) Text("Cycle estimate: ${RendellTiming.completedCycles(generation)} complete · phase ${RendellTiming.phase(generation)} / 11,040")
            else Text("Original program changed: machine-cycle estimate unavailable.")
            Text("Reference demo: two 1s on the right-hand tape become four after 16 cycles.",
                style = MaterialTheme.typography.bodySmall)
            Text("Input tape: not decoded · Output tape: not decoded",
                style = MaterialTheme.typography.bodyMedium)
            Text("This panel does not interpret the tape or machine state. Cycle counts assume the original Rendell preset and its start alignment; editing cells invalidates that assumption.",
                style = MaterialTheme.typography.bodySmall)
        }
    }
}
