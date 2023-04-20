package homemade.game.model.cellstates

import homemade.game.model.Cell
import homemade.game.model.CellState

class SimpleState private constructor(type: Cell) : CellState(type) {
    companion object {
        val allStates = setOf(Cell.EMPTY, Cell.MARKED_FOR_SPAWN, Cell.DEAD_BLOCK)
        private val simpleState = allStates.associateBy({ it }, { SimpleState(it) })
        fun get(type: Cell): CellState {
            return simpleState[type] ?: throw RuntimeException("not a simple state $type")
        }
    }
}
