package homemade.game.controller;

import homemade.game.Game;
import homemade.game.SelectionState;

/**
 * Created by user3 on 25.03.2016.
 */
class SelectionStateProvider implements SelectionState
{
    private int[] selection;

    SelectionStateProvider(int[] selectionData)
    {
        this.selection = selectionData;

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
}
