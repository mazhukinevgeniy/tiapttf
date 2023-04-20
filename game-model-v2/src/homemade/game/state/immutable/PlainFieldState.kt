package homemade.game.state.immutable

import homemade.game.fieldstructure.CellCode
import homemade.game.fieldstructure.Direction
import homemade.game.fieldstructure.FieldStructure
import homemade.game.fieldstructure.LinkCode
import homemade.game.model.CellState
import homemade.game.state.FieldState

class PlainFieldState(source: FieldState) : FieldState {
    override val structure: FieldStructure = source.structure
    val cellStates = structure.cellCodeIterator.asSequence().map { source.getCellState(it) }.toList()
    val linkState = structure.linkCodeIterator.asSequence().map { source.getLinkBetweenCells(it) }.toList()
    val lengths = structure.linkCodeIterator.asSequence().map { source.getChainLength(it) }.toList()

    override fun getCellState(cellCode: CellCode): CellState {
        check(cellCode.hashCode() in cellStates.indices) {
            "bad cell $cellCode in structure ${structure.width}x${structure.height}"
        }
        return cellStates[cellCode.hashCode()]
    }

    override fun getLinkBetweenCells(linkCode: LinkCode): Direction? {
        check(linkCode.hashCode() in linkState.indices) {
            "bad link $linkCode in structure ${structure.width}x${structure.height}"
        }
        return linkState[linkCode.hashCode()]
    }

    override fun getChainLength(linkCode: LinkCode): Int {
        check(linkCode.hashCode() in lengths.indices) {
            "bad link $linkCode in structure ${structure.width}x${structure.height}"
        }
        return lengths[linkCode.hashCode()]
    }
}
