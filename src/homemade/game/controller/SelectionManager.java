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

        int eventCell = cellX + cellY * Game.FIELD_WIDTH;

        if (this.controller.model.copyGameState().getCellValue(cellX, cellY) > 0)
        {
            this.selection.removeAllElements();
            this.selection.add(eventCell);

            this.updateSelectionState();
        }
        else if (this.selection.size() == 1) //ie we move single blocks and we can move them by clicking nearby cells
        {
            int selectedCell = this.selection.get(0);
            int distance = Math.abs(eventCell - selectedCell);

            if (distance == 1 || distance == Game.FIELD_WIDTH) //ie cells are adjacent
            {
                this.controller.model.blockMoveRequested(selectedCell, eventCell);

                this.selection.removeAllElements();
                this.selection.add(eventCell);

                this.updateSelectionState();
            }
        }

        //TODO: could use simple CellCoordinates class for arguments and simple manipulations, comparisons etc

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
