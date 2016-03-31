package homemade.game.controller;

import homemade.game.*;
import homemade.game.view.GameView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

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

        CellCode eventCell = CellCode.getFor(cellX, cellY);

        if (this.controller.model.copyGameState().getCellValue(eventCell) > 0)
        {
            this.selection.clear();
            this.selection.add(eventCell.value());

            this.updateSelectionState();
        }
        else if (this.selection.size() == 1) //ie we move single blocks and we can move them by clicking nearby cells
        {
            CellCode selectedCell = CellCode.getFor(this.selection.get(0));
            int distance = eventCell.distance(selectedCell);

            if (distance == 1)
            {
                tryMove(selectedCell, eventCell, true);
            }
        }

        System.out.println("apparently, mouse released at " + cellX + ", " + cellY);
    }

    void tryToMoveSelectionIn(int direction)
    {
        if (this.selection.size() == 1)
        {
            int selectedCell = this.selection.get(0);

            CellCode cellCode = CellCode.getFor(selectedCell);

            if (!cellCode.onBorder(direction))
            {
                CellCode eventCell = CellCode.getFor(selectedCell + CellCode.getShift(direction));

                tryMove(cellCode, eventCell, false);
            }
        }
    }

    private void tryMove(CellCode selectedCell, CellCode eventCell, boolean moveSelectionOnFail)
    {
        if (eventCell != selectedCell)
        {
            this.controller.model.blockMoveRequested(selectedCell, eventCell);

            this.selection.clear();

            if (this.controller.model.copyGameState().getCellValue(selectedCell) <= 0)
            {
                this.selection.add(eventCell.value());
            }
            else
            {
                this.selection.add(moveSelectionOnFail ? eventCell.value() : selectedCell.value());
            }

            this.updateSelectionState();
        }
    }

    private void updateSelectionState()
    {
        GameState state = controller.model.copyGameState();

        int selectionSize = selection.size();
        int [] copy = new int[selectionSize];
        HashSet<Integer> cellsToMove = new HashSet<Integer>(4 * selectionSize);

        for (int i = 0; i < selectionSize; i++)
        {
            CellCode cellCode = CellCode.getFor(copy[i] = selection.get(i));

            Iterator<Integer> iterator = Direction.getIterator();

            while (iterator.hasNext())
            {
                int direction = iterator.next();

                CellCode neighbour = cellCode.neighbour(direction);

                if (!cellCode.onBorder(direction) && state.getCellValue(neighbour) < 1)
                    cellsToMove.add(neighbour.value());
            }
        }

        this.state = new SelectionStateProvider(copy, cellsToMove);
    }

    SelectionState getSelectionState()
    {
        return this.state;
    }
}