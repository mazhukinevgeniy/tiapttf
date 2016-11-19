package homemade.game.model.selection;

import homemade.game.SelectionState;
import homemade.game.fieldstructure.CellCode;
import homemade.game.fieldstructure.Direction;
import homemade.game.fieldstructure.FieldStructure;
import homemade.game.model.GameModelLinker;
import homemade.game.model.cellmap.CellMapReader;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

public class BlockSelection {
    private GameModelLinker linker;
    private CellMapReader cellMapReader;

    private int fieldSize;

    private ArrayList<CellCode> selection;

    private SelectionState state;

    public BlockSelection(GameModelLinker linker) {
        this.linker = linker;
        cellMapReader = linker.getMapReader();

        FieldStructure structure = linker.getStructure();
        fieldSize = structure.getFieldSize();

        selection = new ArrayList<>(structure.getMaxDimension());

        updateSelectionState();
    }

    public synchronized void activateCell(CellCode eventCell) {
        if (cellMapReader.getCell(eventCell).isMovable()) {
            selection.clear();
            selection.add(eventCell);

            updateSelectionState();
        } else if (selection.size() == 1 && state.canMoveTo(eventCell)) {
            CellCode selectedCell = selection.get(0);
            tryMove(selectedCell, eventCell, true);
        }

        System.out.println("apparently, mouse released at " + eventCell.x() + ", " + eventCell.y());
    }

    public synchronized void tryToMoveSelectionIn(Direction direction) {
        if (this.selection.size() == 1) {
            CellCode cellCode = selection.get(0);

            if (!cellCode.onBorder(direction)) {
                CellCode eventCell = cellCode.neighbour(direction);

                tryMove(cellCode, eventCell, false);
            }
        }
    }

    private void tryMove(CellCode selectedCell, CellCode eventCell, boolean moveSelectionOnFail) {
        if (eventCell != selectedCell) {
            linker.tryMove(selectedCell, eventCell);

            selection.clear();

            boolean selectedCellOccupied = cellMapReader.getCell(selectedCell).isNormalBlock();
            boolean eventCellOccupied = cellMapReader.getCell(eventCell).isNormalBlock();

            if (selectedCellOccupied) //I think that means move failed
            {
                if (moveSelectionOnFail)
                    selection.add(eventCell);
                else
                    selection.add(selectedCell);
            } else if (eventCellOccupied)
                selection.add(eventCell);

            updateSelectionState();
        }
    }

    public synchronized void updateSelectionState() {
        for (Iterator<CellCode> iterator = selection.iterator(); iterator.hasNext(); ) {
            CellCode next = iterator.next();

            if (!cellMapReader.getCell(next).isNormalBlock())
                iterator.remove();
        }

        HashSet<CellCode> copy = new HashSet<CellCode>(selection);

        HashSet<CellCode> cellsToMove = new HashSet<CellCode>(fieldSize);

        if (selection.size() > 1)
            throw new RuntimeException("accessable cells are undefined");
        else if (selection.size() == 1) {
            HashSet<CellCode> unaccessableCells = new HashSet<>(fieldSize);
            HashSet<CellCode> borderCells = new HashSet<>(1);
            Direction[] directions = Direction.values();

            borderCells.add(selection.get(0));

            while (!borderCells.isEmpty()) {
                HashSet<CellCode> newBorder = new HashSet<>(Math.min(borderCells.size() * 2, fieldSize / 2));

                for (CellCode borderCell : borderCells) {
                    unaccessableCells.add(borderCell);

                    for (Direction direction : directions) {
                        CellCode neighbour = borderCell.neighbour(direction);

                        if (neighbour != null && !unaccessableCells.contains(neighbour)) {
                            if (cellMapReader.getCell(neighbour).isFreeForMove()) {
                                newBorder.add(neighbour);
                                cellsToMove.add(neighbour);
                            } else {
                                unaccessableCells.add(neighbour);
                            }
                        }
                    }
                }

                borderCells = newBorder;
            }
        }

        state = new SelectionStateProvider(copy, cellsToMove);
    }

    public SelectionState getSelectionState() {
        return state;
    }
}