package homemade.game.model.selection;

import homemade.game.SelectionState;
import homemade.game.fieldstructure.CellCode;
import homemade.game.fieldstructure.Direction;
import homemade.game.model.GameModelLinker;
import homemade.game.model.cellmap.CellMapReader;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by user3 on 24.03.2016.
 */
public class BlockSelection
{
    private GameModelLinker linker;
    private CellMapReader cellMapReader;

    private ArrayList<CellCode> selection;

    private SelectionState state;

    public BlockSelection(GameModelLinker linker)
    {
        this.linker = linker;
        cellMapReader = linker.getMapReader();

        selection = new ArrayList<>(linker.getStructure().getMaxDimension());

        updateSelectionState();
    }

    public synchronized void activateCell(CellCode eventCell)
    {
        if (cellMapReader.getCellValue(eventCell) > 0)
        {
            selection.clear();
            selection.add(eventCell);

            updateSelectionState();
        }
        else if (selection.size() == 1) //ie we move single blocks and we can move them by clicking cells
        {
            CellCode selectedCell = selection.get(0);
            int distance = eventCell.distance(selectedCell);

            if (distance == 1)
            {
                tryMove(selectedCell, eventCell, true);
            }
        }

        System.out.println("apparently, mouse released at " + eventCell.x() + ", " + eventCell.y());
    }

    public synchronized void tryToMoveSelectionIn(Direction direction)
    {
        if (this.selection.size() == 1)
        {
            CellCode cellCode = selection.get(0);

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
            linker.tryCascadeChanges(selectedCell, eventCell);

            selection.clear();

            boolean selectedCellOccupied = cellMapReader.getCellValue(selectedCell) > 0;
            boolean eventCellOccupied = cellMapReader.getCellValue(eventCell) > 0;

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

                if (!cellCode.onBorder(direction) && cellMapReader.getCellValue(neighbour) < 1)
                    cellsToMove.add(neighbour);
            }
        }

        state = new SelectionStateProvider(copy, cellsToMove);
    }

    public SelectionState getSelectionState()
    {
        return state;
    } //TODO: return immutable copy
}