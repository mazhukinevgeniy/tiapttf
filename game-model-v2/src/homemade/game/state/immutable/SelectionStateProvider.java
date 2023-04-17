package homemade.game.state.immutable;

import homemade.game.fieldstructure.CellCode;
import homemade.game.state.SelectionState;

import java.util.HashSet;

class SelectionStateProvider implements SelectionState {
    private HashSet<CellCode> selection;
    private HashSet<CellCode> cellsToMove;

    SelectionStateProvider(HashSet<CellCode> selectionData, HashSet<CellCode> cellsToMove) {
        this.selection = selectionData;
        this.cellsToMove = cellsToMove;
    }

    @Override
    public boolean isSelected(CellCode cellCode) {
        return selection.contains(cellCode);
    }

    @Override
    public boolean canMoveTo(CellCode cellCode) {
        return cellsToMove.contains(cellCode);
    }
}
