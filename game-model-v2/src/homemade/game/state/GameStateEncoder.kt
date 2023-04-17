package homemade.game.state

import homemade.game.fieldstructure.CellCode
import homemade.game.fieldstructure.Direction
import homemade.game.fieldstructure.LinkCode
import homemade.game.model.CellState

class GameStateEncoder {
    fun encode(state: GameState): GameState {
        return PlainGameState(state)
    }

    fun encodeToString(state: GameState): String {
        TODO("impl")
    }

    /**
     * Uses assumption that cellstate/linkstate types are immutable
     */
    private class PlainGameState(source: GameState) : GameState(source.structure, source.spawnsDenied, source.gameScore, source.globalMultiplier) {
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
