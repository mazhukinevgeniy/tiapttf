package homemade.game.model.cellstates

import homemade.game.model.Cell
import homemade.game.model.CellState

object SimpleState {
    private class SimpleStateImpl(type: Cell) : CellState(type)

    private val simpleState = listOf(Cell.EMPTY, Cell.MARKED_FOR_SPAWN, Cell.DEAD_BLOCK).associateBy(
            { it }, { SimpleStateImpl(it) }
    )

    fun get(type: Cell): CellState {
        return simpleState[type] ?: throw RuntimeException("not a simple state $type")
    }
}
