package homemade.game.controller;

import homemade.game.Game;
import homemade.game.GameState;
import homemade.game.SelectionState;
import homemade.game.view.GameView;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by user3 on 24.03.2016.
 */
class SelectionManager implements MouseInputHandler
{
    private ArrayList<Integer> selection;
    private GameController controller;

    private SelectionState state;

    SelectionManager(GameController controller)
    {
        this.selection = new ArrayList<Integer>(Math.max(Game.FIELD_WIDTH, Game.FIELD_HEIGHT));
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
            this.selection.clear();
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

                this.selection.clear();

                if (this.controller.model.copyGameState().getCellValue(cellX, cellY) > 0)
                {
                    this.selection.add(eventCell);
                }

                this.updateSelectionState();
            }
        }

        System.out.println("apparently, mouse released at " + cellX + ", " + cellY);
    }

    private void updateSelectionState()
    {
        GameState state = controller.model.copyGameState();

        int selectionSize = selection.size();
        int [] copy = new int[selectionSize];
        HashSet<Integer> cellsToMove = new HashSet<Integer>(4 * selectionSize);

        for (int i = 0; i < selectionSize; i++)
        {
            int cellCode = copy[i] = selection.get(i);

            int x = cellCode % Game.FIELD_WIDTH;
            int y = cellCode / Game.FIELD_WIDTH;

            if (x > 0 && state.getCellValue(x - 1, y) < 1)
                cellsToMove.add(cellCode - 1);
            if (x < Game.FIELD_WIDTH - 1 && state.getCellValue(x + 1, y) < 1)
                cellsToMove.add(cellCode + 1);
            if (y > 0 && state.getCellValue(x, y - 1) < 1)
                cellsToMove.add(cellCode - Game.FIELD_WIDTH);
            if (y < Game.FIELD_HEIGHT - 1 && state.getCellValue(x, y + 1) < 1)
                cellsToMove.add(cellCode + Game.FIELD_WIDTH);
        }

        this.state = new SelectionStateProvider(copy, cellsToMove);
    }

    SelectionState getSelectionState()
    {
        return this.state;
    }
}