package homemade.game.state;

import homemade.game.fieldstructure.CellCode;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;

public class MutableSelectionState implements SelectionState {
    @Nullable
    public CellCode selection;
    public HashSet<CellCode> cellsToMove;

    public MutableSelectionState(@Nullable CellCode selection, HashSet<CellCode> cellsToMove) {
        this.selection = selection;
        this.cellsToMove = cellsToMove;
    }

    @Override
    public boolean isSelected(CellCode cellCode) {
        return selection == cellCode;
    }

    @Override
    public boolean canMoveTo(CellCode cellCode) {
        return cellsToMove.contains(cellCode);
    }

    public SelectionState copySelectionState() {
        return new MutableSelectionState(selection, (HashSet<CellCode>) cellsToMove.clone());
    }
}
