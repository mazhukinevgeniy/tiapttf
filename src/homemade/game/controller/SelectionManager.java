package homemade.game.controller;

import homemade.game.Game;
import homemade.game.SelectionState;
import homemade.game.view.GameView;

import java.util.Iterator;
import java.util.Vector;

/**
 * Created by user3 on 24.03.2016.
 */
class SelectionManager implements MouseInputHandler
{
    private Vector<Integer> selection;
    private GameController controller;

    private SelectionState state;

    SelectionManager(GameController controller)
    {
        this.selection = new Vector<Integer>(Math.max(Game.FIELD_WIDTH, Game.FIELD_HEIGHT));
        this.controller = controller;

        this.updateSelectionState();
    }



    public void handleMouseRelease(int canvasX, int canvasY)
    {
        int cellX = (canvasX - GameView.GridOffset) / (GameView.CellWidth + GameView.CellOffset);
        int cellY = (canvasY - GameView.GridOffset) / (GameView.CellWidth + GameView.CellOffset);

        if (this.controller.state().getCellValue(cellX, cellY) > 0)
        {
            this.selection.removeAllElements();
            this.selection.add(cellX + cellY * Game.FIELD_WIDTH);

            this.updateSelectionState();
        }

        //TODO: could use simple CellCoordinates class for arguments and simple manipulations, comparisons etc
        //TODO: add ability to move blocks by clicking at empty nearby cells

        System.out.println("apparently, mouse released at " + cellX + ", " + cellY);
    }

    private void updateSelectionState()
    {
        Iterator<Integer> iterator = this.selection.iterator();
        int [] copy = new int[this.selection.size()];

        for (int i = 0; iterator.hasNext(); i++)
        {
            copy[i] = iterator.next();
        }

        this.state = new SelectionStateProvider(copy);
    }

    SelectionState getSelectionState()
    {
        return this.state;
    }
}
