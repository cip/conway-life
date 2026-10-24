package com.cip.conwaylife3d

/** Sparse, unbounded Conway B3/S23 engine for large Life constructions.
 * In contrast to the 48x48 mode, the edges DO NOT wrap around. */
data class Cell(val x: Int, val y: Int)

class SparseLife {
    fun next(current: Set<Cell>): Set<Cell> {
        val counts = HashMap<Cell, Int>(current.size * 5)
        for (cell in current) {
            for (dy in -1..1) for (dx in -1..1) {
                if (dx == 0 && dy == 0) continue
                val neighbor = Cell(cell.x + dx, cell.y + dy)
                counts[neighbor] = (counts[neighbor] ?: 0) + 1
            }
        }
        return counts.asSequence().filter { (cell, count) ->
            count == 3 || (count == 2 && cell in current)
        }.map { it.key }.toSet()
    }
}

/** Parses standard Life RLE (b/o/$/!) including comments and rule headers. */
object LifeRle {
    data class Pattern(val width: Int, val height: Int, val cells: Set<Cell>)

    fun decode(input: String): Pattern {
        val lines = input.lineSequence().map(String::trim).filter { it.isNotBlank() && !it.startsWith("#") }.toList()
        require(lines.isNotEmpty()) { "Missing RLE header" }
        val header = Regex("""x\s*=\s*(\d+)\s*,\s*y\s*=\s*(\d+)(?:\s*,\s*rule\s*=\s*([^,\s]+))?.*""", RegexOption.IGNORE_CASE)
            .matchEntire(lines.first()) ?: error("Invalid RLE header")
        val width = header.groupValues[1].toInt()
        val height = header.groupValues[2].toInt()
        require(width > 0 && height > 0) { "Invalid pattern dimensions" }
        val rule = header.groupValues[3]
        require(rule.isEmpty() || rule.equals("B3/S23", true) || rule.equals("23/3", true)) {
            "Only Conway B3/S23 patterns are supported"
        }
        val live = HashSet<Cell>()
        val body = lines.drop(1).joinToString("")
        var x = 0
        var y = 0
        var digits = ""
        var finished = false
        for (ch in body) {
            if (ch.isWhitespace()) continue
            if (ch.isDigit()) {
                digits += ch
                require(digits.length < 11) { "RLE run count too long" }
                continue
            }
            val n = if (digits.isEmpty()) 1 else digits.toInt()
            digits = ""
            require(n > 0)
            when (ch) {
                'b', 'B' -> { x += n; require(x <= width) { "RLE exceeds declared width" } }
                'o', 'O' -> {
                    require(x + n <= width && y < height) { "RLE exceeds dimensions" }
                    repeat(n) { live.add(Cell(x + it, y)) }
                    x += n
                }
                '$' -> { y += n; x = 0; require(y <= height) { "RLE exceeds declared height" } }
                '!' -> { finished = true; break }
                else -> error("Unknown RLE symbol: $ch")
            }
        }
        require(finished) { "RLE missing end marker" }
        return Pattern(width, height, live)
    }
}
