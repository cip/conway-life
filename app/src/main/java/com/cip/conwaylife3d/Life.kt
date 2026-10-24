package com.cip.conwaylife3d

import kotlin.random.Random

/** Toroidal Conway B3/S23 universe. Immutable transition results. */
class Life(val size: Int = 48) {
    init { require(size >= 3) }
    fun empty(): BooleanArray = BooleanArray(size * size)
    fun random(seed: Int = 42, density: Float = .22f): BooleanArray {
        val rng = Random(seed)
        return BooleanArray(size * size) { rng.nextFloat() < density }
    }
    fun next(current: BooleanArray): BooleanArray {
        require(current.size == size * size)
        return BooleanArray(current.size) { index ->
            val x = index % size
            val y = index / size
            var neighbors = 0
            for (dy in -1..1) for (dx in -1..1) {
                if (dx == 0 && dy == 0) continue
                val nx = (x + dx + size) % size
                val ny = (y + dy + size) % size
                if (current[ny * size + nx]) neighbors++
            }
            neighbors == 3 || (current[index] && neighbors == 2)
        }
    }
    fun glider(): BooleanArray = empty().also { cells ->
        val x = size / 2 - 1; val y = size / 2 - 1
        listOf(1 to 0, 2 to 1, 0 to 2, 1 to 2, 2 to 2).forEach { (dx, dy) -> cells[(y + dy) * size + x + dx] = true }
    }
}
