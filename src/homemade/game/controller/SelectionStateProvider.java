package homemade.game.controller;

import homemade.game.CellCode;
import homemade.game.Game;
import homemade.game.SelectionState;

import java.util.HashSet;

/**
 * Created by user3 on 25.03.2016.
 */
class SelectionStateProvider implements SelectionState
{
    private int[] selection;
    private HashSet<Integer> cellsToMove;

    SelectionStateProvider(int[] selectionData, HashSet<Integer> cellsToMove)
    {
        this.selection = selectionData;
        this.cellsToMove = cellsToMove;
    }

    @Override
    public boolean isSelected(CellCode cellCode)
    {
        boolean found = false;

        int cellCodeVal = cellCode.value();
        int i = 0, length = this.selection.length;

        while (!found && i < length)
        {
            if (this.selection[i] == cellCodeVal)
            {
                found = true;
            }

            i++;
        }

        return found;
    }

    @Override
    public boolean canMoveTo(CellCode cellCode)
    {
        return cellsToMove.contains(cellCode.value());
    }
}
