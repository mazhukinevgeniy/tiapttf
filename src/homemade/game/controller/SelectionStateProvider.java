package homemade.game.controller;

import homemade.game.fieldstructure.CellCode;
import homemade.game.SelectionState;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by user3 on 25.03.2016.
 */
class SelectionStateProvider implements SelectionState
{
    private ArrayList<CellCode> selection;
    private HashSet<CellCode> cellsToMove;

    SelectionStateProvider(ArrayList<CellCode> selectionData, HashSet<CellCode> cellsToMove)
    {
        this.selection = selectionData;
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
