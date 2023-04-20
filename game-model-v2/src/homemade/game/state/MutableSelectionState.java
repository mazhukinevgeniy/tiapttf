package homemade.game.state;

import homemade.game.fieldstructure.CellCode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class MutableSelectionState implements SelectionState {
    @Nullable
    public CellCode selection;
    public HashSet<CellCode> cellsToMove;

    public MutableSelectionState(@Nullable CellCode selection, HashSet<CellCode> cellsToMove) {
        this.selection = selection;
        this.cellsToMove = cellsToMove;
    }

    @Nullable
    @Override
    public CellCode getSelection() {
        return selection;
    }

    @NotNull
    @Override
    public Set<CellCode> getCellsToMove() {
        return Collections.unmodifiableSet(cellsToMove);
    }

    public SelectionState copySelectionState() {
        return new MutableSelectionState(selection, (HashSet<CellCode>) cellsToMove.clone());
    }
}
