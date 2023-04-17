package homemade.game.state

import homemade.game.fieldstructure.CellCode
import homemade.game.fieldstructure.Direction
import homemade.game.fieldstructure.FieldStructure
import homemade.game.fieldstructure.LinkCode
import homemade.game.model.CellState

abstract class GameState(sourceStructure: FieldStructure, denies: Int = 0, score: Int = 0, multiplier: Int = 1) {
    val structure: FieldStructure = sourceStructure

    var spawnsDenied: Int = denies
        protected set

    var gameScore: Int = score
        protected set

    var globalMultiplier: Int = multiplier
        protected set

    abstract fun getCellState(cellCode: CellCode): CellState?
    abstract fun getLinkBetweenCells(linkCode: LinkCode): Direction?
    abstract fun getChainLength(linkCode: LinkCode): Int

    fun getNumberOfBlocks(): Int {
        return structure.cellCodeIterator.asSequence().count { getCellState(it)?.isAnyBlock ?: false }
    }

    fun getNumberOfMovableBlocks(): Int {
        //TODO p sure the definition of 'movable' is wrong, i.e. it doesn't account the neighbours
        return structure.cellCodeIterator.asSequence().count() { getCellState(it)?.isMovableBlock ?: false }
    }
}