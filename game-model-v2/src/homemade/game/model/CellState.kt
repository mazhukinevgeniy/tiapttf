package homemade.game.model

import homemade.game.model.combo.ComboEffect
import java.util.*

/**
 * Extensions of this class must be immutable
 */
abstract class CellState protected constructor(private val cellType: Cell) {
    open fun value(): Int {
        return UNDEFINED_VALUE
    }

    open fun effect(): ComboEffect? {
        return ComboEffect.UNDEFINED_COMBO_EFFECT
    }
    //TODO somethings wrong with isMovableBlock and isAliveBlock

    fun type(): Cell { //TODO why extra method
        return cellType
    }

    val isAliveBlock: Boolean
        get() = cellType == Cell.OCCUPIED
    val isAnyBlock: Boolean
        get() = blocks.contains(cellType)
    val isFreeForMove: Boolean
        get() = cellFreeToMove.contains(cellType)
    val isFreeForSpawn: Boolean
        get() = cellType == Cell.EMPTY
    open val isMovableBlock: Boolean
        get() = false

    companion object {
        private val cellFreeToMove: Set<Cell> = EnumSet.of(Cell.EMPTY, Cell.MARKED_FOR_SPAWN)
        private val blocks: Set<Cell> = EnumSet.of(Cell.OCCUPIED, Cell.DEAD_BLOCK)
        const val UNDEFINED_VALUE = -1
    }
}
