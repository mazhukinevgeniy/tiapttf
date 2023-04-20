package homemade.game.state.immutable

import homemade.game.fieldstructure.CellCode
import homemade.game.fieldstructure.Direction
import homemade.game.fieldstructure.FieldStructure
import homemade.game.fieldstructure.LinkCode
import homemade.game.model.CellState
import homemade.game.state.FieldState
import homemade.game.state.MutableFieldState
import homemade.game.state.impl.BlockValuePool

class PlainFieldState(
        override val structure: FieldStructure,
        val cellStates: List<CellState>,
        val linkState: List<Direction?>,
        val lengths: List<Int>
) : FieldState {

    constructor(source: FieldState) : this(
            source.structure,
            source.structure.cellCodeIterator.asSequence().map { source.getCellState(it) }.toList(),
            source.structure.linkCodeIterator.asSequence().map { source.getLinkBetweenCells(it) }.toList(),
            source.structure.linkCodeIterator.asSequence().map { source.getChainLength(it) }.toList()
    )

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

    companion object {
        fun buildConsistent(fieldStructure: FieldStructure, cellStates: List<CellState>, maxBlock: Int): PlainFieldState {
            val tmp = MutableFieldState(fieldStructure, BlockValuePool(maxBlock, fieldStructure.fieldSize))
            tmp.applyCascadeChanges(fieldStructure.cellCodeIterator.asSequence().associateBy(
                    { it }, { cellStates[it.hashCode()] })
            )
            return PlainFieldState(tmp)
        }
    }
}
