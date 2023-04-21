package homemade.game.fieldstructure

import java.util.*

class FieldStructure @JvmOverloads constructor(@JvmField val width: Int = DEFAULT_FIELD_WIDTH,
                                               @JvmField val height: Int = DEFAULT_FIELD_HEIGHT) {
    private val cellCodes: ArrayList<CellCode>
    private val linkCodes: ArrayList<LinkCode>
    private val shifts = EnumMap(mapOf(
            Direction.BOTTOM to width,
            Direction.TOP to -width,
            Direction.LEFT to -1,
            Direction.RIGHT to 1
    ))

    @JvmField
    val fieldSize: Int = width * height

    @JvmField
    val numberOfLinks: Int = (width - 1) * height + (height - 1) * width

    init {
        cellCodes = createCellCodes()
        linkCodes = createLinkCodes()
    }

    private fun createCellCodes(): ArrayList<CellCode> {
        val cellCodes = ArrayList<CellCode>(fieldSize)
        for (i in 0 until width) {
            for (j in 0 until height) {
                cellCodes.add(CellCode(i, j, width, height, i + j * width))
            }
        }
        cellCodes.sortBy { it.hashCode() }
        for (i in 0 until fieldSize) {
            val cellCode = cellCodes[i]
            for (direction in Direction.values()) {
                if (!cellCode.onBorder(direction)) {
                    cellCode.neighbours[direction] = cellCodes[cellCode.hashCode() + shifts[direction]!!]
                }
            }
        }
        return cellCodes
    }

    private fun createLinkCodes(): ArrayList<LinkCode> {
        val codes = ArrayList<LinkCode>(numberOfLinks)
        for (j in 0 until height - 1) for (i in 0 until width) {
            val lower = getCellCode(i, j)
            val higher = lower.neighbour(Direction.BOTTOM)!!
            codes.add(LinkCode(Direction.BOTTOM, lower, higher, linkCodeAsInt(lower, higher)))
        }
        for (i in 0 until width - 1) for (j in 0 until height) {
            val lower = getCellCode(i, j)
            val higher = lower.neighbour(Direction.RIGHT)!!
            codes.add(LinkCode(Direction.RIGHT, lower, higher, linkCodeAsInt(lower, higher)))
        }
        codes.sortBy { it.hashCode() }
        return codes
    }

    fun getCellCode(x: Int, y: Int): CellCode {
        require(x in 0 until width && y in 0 until height) { "valid coordinates are 0..${width - 1}, 0..${height - 1}" }
        return cellCodes[x + y * width]
    }

    fun getLinkCode(cellCodePair: CellCodePair): LinkCode {
        return getLinkCode(cellCodePair.lower!!, cellCodePair.higher!!)
    }

    fun getLinkCode(cellA: CellCode, cellB: CellCode): LinkCode {
        require(cellA.distance(cellB) == 1) { "can't get linkCode: cells aren't neighbours" }
        return linkCodes[linkCodeAsInt(cellA, cellB)]
    }

    val cellCodeIterator: Iterator<CellCode>
        get() = cellCodes.listIterator()
    val linkCodeIterator: Iterator<LinkCode>
        get() = linkCodes.listIterator()
    val maxDimension: Int
        get() = width.coerceAtLeast(height)
    private val numberOfVerticalLinks: Int
        get() = width * (height - 1)

    /**
     * If we enumerate vertical links, it's easily done.
     *
     *
     * The idea of this enumeration is to numerate vertical links first,
     * and then horizontal, as if they were vertical.
     */
    private fun linkCodeAsInt(lower: CellCode, higher: CellCode): Int {
        if (lower.hashCode() > higher.hashCode()) {
            return linkCodeAsInt(higher, lower)
        }
        return when (higher) {
            lower.neighbour(Direction.RIGHT) -> {
                numberOfVerticalLinks + lower.rotatedCellCode
            }

            lower.neighbour(Direction.BOTTOM) -> {
                lower.hashCode()
            }

            else -> {
                throw RuntimeException("unresolvable linkCodeAsInt call")
            }
        }
    }

    companion object {
        private const val DEFAULT_FIELD_WIDTH = 9
        private const val DEFAULT_FIELD_HEIGHT = 9
    }
}
