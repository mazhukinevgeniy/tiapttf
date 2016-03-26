package homemade.game.controller;

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
    public boolean isSelected(int cellX, int cellY)
    {
        boolean found = false;

        int cellCode = cellX + cellY * Game.FIELD_WIDTH; //TODO: create a way to stop copying this code
        int i = 0, length = this.selection.length;

        while (!found && i < length)
        {
            if (this.selection[i] == cellCode)
            {
                found = true;
            }

            i++;
        }

        return found;
    }

    @Override
    public boolean canMoveTo(int cellX, int cellY)
    {
        return cellsToMove.contains(cellX + cellY * Game.FIELD_WIDTH);
    }
}
