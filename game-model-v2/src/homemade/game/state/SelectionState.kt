package homemade.game.state

import homemade.game.fieldstructure.CellCode

interface SelectionState {
    val selection: CellCode?
    val cellsToMove: Set<CellCode>
    fun isSelected(cellCode: CellCode?): Boolean
    fun canMoveTo(cellCode: CellCode?): Boolean
}
