package com.cip.conwaylife3d

/** Measurements from actual live Life cells, not guessed TM tape values. */
data class LifeCheckpoint(
    val generation: Int,
    val population: Int,
    val minX: Int?,
    val minY: Int?,
    val maxX: Int?,
    val maxY: Int?
) {
    companion object {
        fun from(generation: Int, cells: Set<Cell>): LifeCheckpoint =
            if (cells.isEmpty()) LifeCheckpoint(generation, 0, null, null, null, null)
            else LifeCheckpoint(
                generation, cells.size,
                cells.minOf { it.x }, cells.minOf { it.y },
                cells.maxOf { it.x }, cells.maxOf { it.y }
            )
    }
}
