package homemade.game.state

import homemade.game.fieldstructure.CellCode

interface SelectionState {
    val selection: CellCode?
    val cellsToMove: Set<CellCode>
}

fun SelectionState.isSelected(cellCode: CellCode): Boolean {
    return selection == cellCode
}

fun SelectionState.canMoveTo(cellCode: CellCode): Boolean {
    return cellCode in cellsToMove
}
