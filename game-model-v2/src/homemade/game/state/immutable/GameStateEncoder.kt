package homemade.game.state.immutable

import homemade.game.fieldstructure.CellCode
import homemade.game.fieldstructure.Direction
import homemade.game.fieldstructure.LinkCode
import homemade.game.model.CellState
import homemade.game.state.FieldState

class GameStateEncoder {
    fun encode(state: FieldState): FieldState {
        return PlainFieldState(state)
    }

    fun encodeToString(state: FieldState): String {
        TODO("impl")
    }

    /**
     * Uses assumption that cellstate/linkstate types are immutable
     */
    private class PlainFieldState(source: FieldState) : FieldState(
            source.structure, source.spawnsDenied,
            source.gameScore, source.globalMultiplier,
            source.spawnPeriod
    ) {
        val cellStates = structure.cellCodeIterator.asSequence().map { source.getCellState(it) }.toList()
        val linkState = structure.linkCodeIterator.asSequence().map { source.getLinkBetweenCells(it) }.toList()
        val lengths = structure.linkCodeIterator.asSequence().map { source.getChainLength(it) }.toList()

        override fun getCellState(cellCode: CellCode): CellState? {
            if (cellCode.hashCode() in 0 until cellStates.size) {
                return cellStates[cellCode.hashCode()]
            }
            throw RuntimeException("bad cell $cellCode in structure ${structure.width}x${structure.height}")
        }

        override fun getLinkBetweenCells(linkCode: LinkCode): Direction? {
            if (linkCode.hashCode() in 0 until linkState.size) {
                return linkState[linkCode.hashCode()]
            }
            throw RuntimeException("bad link $linkCode in structure ${structure.width}x${structure.height}")
        }

        override fun getChainLength(linkCode: LinkCode): Int {
            if (linkCode.hashCode() in 0 until lengths.size) {
                return lengths[linkCode.hashCode()]
            }
            throw RuntimeException("bad link $linkCode in structure ${structure.width}x${structure.height}")
        }
    }
}
