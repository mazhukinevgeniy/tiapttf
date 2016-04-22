package homemade.game.model.selection;

import homemade.game.SelectionState;
import homemade.game.fieldstructure.CellCode;

import java.util.ArrayList;
import java.util.HashSet;

class SelectionStateProvider implements SelectionState
{
    private HashSet<CellCode> selection;
    private HashSet<CellCode> cellsToMove;

    SelectionStateProvider(ArrayList<CellCode> selectionData, HashSet<CellCode> cellsToMove)//TODO: do we have to accept selection data in the first place?
    {
        this.selection = new HashSet<>(selectionData);
        this.cellsToMove = cellsToMove;
    }

    @Override
    public boolean isSelected(CellCode cellCode)
    {
        return selection.contains(cellCode);
    }

    @Override
    public boolean canMoveTo(CellCode cellCode)
    {
        return cellsToMove.contains(cellCode);
    }
}
