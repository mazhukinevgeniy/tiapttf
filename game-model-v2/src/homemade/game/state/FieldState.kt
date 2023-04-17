package homemade.game.state

import homemade.game.fieldstructure.CellCode
import homemade.game.fieldstructure.Direction
import homemade.game.fieldstructure.FieldStructure
import homemade.game.fieldstructure.LinkCode
import homemade.game.model.CellState

interface FieldState {
    val structure: FieldStructure

    fun getCellState(cellCode: CellCode): CellState?
    fun getLinkBetweenCells(linkCode: LinkCode): Direction?
    fun getChainLength(linkCode: LinkCode): Int

    fun getNumberOfBlocks(): Int {
        return structure.cellCodeIterator.asSequence().count { getCellState(it)?.isAnyBlock ?: false }
    }

    fun getNumberOfMovableBlocks(): Int {
        //TODO p sure the definition of 'movable' is wrong, i.e. it doesn't account the neighbours
        return structure.cellCodeIterator.asSequence().count() { getCellState(it)?.isMovableBlock ?: false }
    }
}
