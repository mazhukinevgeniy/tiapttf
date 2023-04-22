package homemade.game.fieldstructure

import java.util.*
import kotlin.math.abs

class CellCode internal constructor(val x: Int, val y: Int, width: Int, height: Int, val cellCode: Int) {
    private val isOnBorder: BooleanArray = BooleanArray(4)
    var neighbours: EnumMap<Direction, CellCode>

    /**
     * Calculated as if the field was rotated at -PI/2;
     * width is oldheight
     * height is oldwidth
     * x is oldheight - 1 - oldy
     * y is oldx
     */
    val rotatedCellCode: Int = height - (y + 1) + height * x

    init {
        for (i in 0..3) isOnBorder[i] = false
        if (x == 0) isOnBorder[Direction.LEFT.ordinal] = true else if (x == width - 1) isOnBorder[Direction.RIGHT.ordinal] = true
        if (y == 0) isOnBorder[Direction.TOP.ordinal] = true else if (y == height - 1) isOnBorder[Direction.BOTTOM.ordinal] = true
        neighbours = EnumMap(Direction::class.java)
    }

    override fun hashCode(): Int {
        return cellCode
    }

    override fun equals(other: Any?): Boolean {
        return this === other
    }

    fun onBorder(direction: Direction): Boolean {
        return isOnBorder[direction.ordinal]
    }

    /**
     * @return null if there's no such neighbour
     */
    fun neighbour(direction: Direction): CellCode? {
        return neighbours[direction]
    }

    //TODO this probably shouldn't be a part of this class even
    val vicinity: Set<CellCode>
        /**
         * @return all non-null cellcodes from 3x3 square centered on the called cell
         */
        get() {
            val vicinity = HashSet<CellCode?>()
            val bot: CellCode? = neighbour(Direction.BOTTOM)
            val top: CellCode? = neighbour(Direction.TOP)
            val left: CellCode? = neighbour(Direction.LEFT)
            val right: CellCode? = neighbour(Direction.RIGHT)
            if (top != null) {
                vicinity.add(top)
                vicinity.add(top.neighbour(Direction.LEFT))
                vicinity.add(top.neighbour(Direction.RIGHT))
            }
            if (bot != null) {
                vicinity.add(bot)
                vicinity.add(bot.neighbour(Direction.LEFT))
                vicinity.add(bot.neighbour(Direction.RIGHT))
            }
            vicinity.add(left)
            vicinity.add(right)
            vicinity.remove(null)
            return vicinity as Set<CellCode>
        }

    fun distance(otherCell: CellCode): Int {
        return abs(x - otherCell.x) + abs(y - otherCell.y)
    }
}
