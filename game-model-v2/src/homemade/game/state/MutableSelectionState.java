package homemade.game.state;

import homemade.game.fieldstructure.CellCode;

import java.util.HashSet;

public class MutableSelectionState implements SelectionState {
    public HashSet<CellCode> selection;
    public HashSet<CellCode> cellsToMove;

    public MutableSelectionState(HashSet<CellCode> selectionData, HashSet<CellCode> cellsToMove) {
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

    public SelectionState copySelectionState() {
        return new MutableSelectionState((HashSet<CellCode>) selection.clone(), (HashSet<CellCode>) cellsToMove.clone());
    }
}
