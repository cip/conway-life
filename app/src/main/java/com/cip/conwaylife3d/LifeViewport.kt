package com.cip.conwaylife3d

/** Fits all currently living cells inside a viewport, including negative coordinates. */
data class LifeViewport(val centerX: Float, val centerY: Float, val pxPerCell: Float) {
    companion object {
        fun fit(cells: Set<Cell>, width: Int, height: Int): LifeViewport {
            if (cells.isEmpty()) return LifeViewport(0f, 0f, 12f)
            val minX = cells.minOf { it.x }.toLong()
            val maxX = cells.maxOf { it.x }.toLong()
            val minY = cells.minOf { it.y }.toLong()
            val maxY = cells.maxOf { it.y }.toLong()
            val cx = (minX + maxX + 1) / 2.0
            val cy = (minY + maxY + 1) / 2.0
            val spanX = (maxX - minX + 1).toDouble()
            val spanY = (maxY - minY + 1).toDouble()
            val scale = minOf(width.coerceAtLeast(1) * 0.92 / spanX,
                              height.coerceAtLeast(1) * 0.92 / spanY).coerceIn(0.00001, 60.0)
            return LifeViewport(cx.toFloat(), cy.toFloat(), scale.toFloat())
        }
    }
}
