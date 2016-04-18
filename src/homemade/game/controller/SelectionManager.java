package homemade.game.controller;

import homemade.game.GameState;
import homemade.game.SelectionState;
import homemade.game.fieldstructure.CellCode;
import homemade.game.fieldstructure.Direction;
import homemade.game.fieldstructure.FieldStructure;
import homemade.game.view.GameView;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by user3 on 24.03.2016.
 */
class SelectionManager implements MouseInputHandler
{
    private FieldStructure structure;

    private ArrayList<CellCode> selection;
    private GameController controller;

    private SelectionState state;

    SelectionManager(GameController controller, FieldStructure fieldStructure)
    {
        this.controller = controller;

        structure = fieldStructure;

        createClearSelection();
    }

    void createClearSelection()
    {
        selection = new ArrayList<>(structure.getMaxDimension());

        updateSelectionState();
    }


    public synchronized void handleMouseRelease(int canvasX, int canvasY)
    {
        int cellX = (canvasX - GameView.GRID_OFFSET) / (GameView.CELL_WIDTH + GameView.CELL_OFFSET);
        int cellY = (canvasY - GameView.GRID_OFFSET) / (GameView.CELL_WIDTH + GameView.CELL_OFFSET);

        CellCode eventCell = structure.getCellCode(cellX, cellY);

        if (controller.copyGameState().getCellValue(eventCell) > 0)
        {
            this.selection.clear();
            this.selection.add(eventCell);

            this.updateSelectionState();
        }
        else if (this.selection.size() == 1) //ie we move single blocks and we can move them by clicking nearby cells
        {
            CellCode selectedCell = this.selection.get(0);
            int distance = eventCell.distance(selectedCell);

            if (distance == 1)
            {
                tryMove(selectedCell, eventCell, true);
            }
        }

        System.out.println("apparently, mouse released at " + cellX + ", " + cellY);
    }

    void tryToMoveSelectionIn(Direction direction)
    {
        if (this.selection.size() == 1)
        {
            CellCode cellCode = this.selection.get(0);

            if (!cellCode.onBorder(direction))
            {
                CellCode eventCell = cellCode.neighbour(direction);

                tryMove(cellCode, eventCell, false);
            }
        }
    }

    private void tryMove(CellCode selectedCell, CellCode eventCell, boolean moveSelectionOnFail)
    {
        if (eventCell != selectedCell)
        {
            controller.requestBlockMove(selectedCell, eventCell);

            selection.clear();

            GameState gameState = controller.copyGameState();

            boolean selectedCellOccupied = gameState.getCellValue(selectedCell) > 0;
            boolean eventCellOccupied = gameState.getCellValue(eventCell) > 0;

            if (selectedCellOccupied) //I think that means move failed
            {
                if (moveSelectionOnFail)
                    selection.add(eventCell);
                else
                    selection.add(selectedCell);
            }
            else if (eventCellOccupied)
                selection.add(eventCell);

            updateSelectionState();
        }
    }

    private void updateSelectionState()
    {
        GameState state = controller.copyGameState();

        int selectionSize = selection.size();
        ArrayList<CellCode> copy = new ArrayList<CellCode>(selectionSize);
        HashSet<CellCode> cellsToMove = new HashSet<CellCode>(4 * selectionSize);

        for (int i = 0; i < selectionSize; i++)
        {
            CellCode cellCode = selection.get(i);
            copy.add(cellCode);

            for (Direction direction : Direction.values())
            {
                CellCode neighbour = cellCode.neighbour(direction);

                if (!cellCode.onBorder(direction) && state.getCellValue(neighbour) < 1)
                    cellsToMove.add(neighbour);
            }
        }

        this.state = new SelectionStateProvider(copy, cellsToMove);
    }

    SelectionState getSelectionState()
    {
        return this.state;
    }
}