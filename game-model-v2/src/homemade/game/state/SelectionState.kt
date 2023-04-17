package homemade.game.state

import homemade.game.fieldstructure.CellCode

interface SelectionState {
    fun isSelected(cellCode: CellCode?): Boolean
    fun canMoveTo(cellCode: CellCode?): Boolean
}
