package homemade.game.model.selection;

import homemade.game.SelectionState;
import homemade.game.fieldstructure.CellCode;
import homemade.game.fieldstructure.Direction;
import homemade.game.fieldstructure.FieldStructure;
import homemade.game.loop.GameEvent;
import homemade.game.loop.GameEventHandler;
import homemade.game.loop.UserClick;
import homemade.game.model.GameModelLinker;
import homemade.game.model.cellmap.CellMapReader;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

public class BlockSelection implements GameEventHandler<GameEvent> {
    private GameModelLinker linker;
    private CellMapReader cellMapReader;

    private int fieldSize;

    private ArrayList<CellCode> selection;

    private SelectionState state;

    public BlockSelection(GameModelLinker linker) {
        this.linker = linker;
        cellMapReader = linker.getMapReader();

        FieldStructure structure = linker.getStructure();
        fieldSize = structure.fieldSize;

        selection = new ArrayList<>(structure.getMaxDimension());

        updateSelectionState();
    }

    @Override
    public void handle(GameEvent event) {
        if (!(event instanceof UserClick)) {
            return;
        }
        CellCode eventCell = ((UserClick) event).getCellCode();

        if (cellMapReader.getCell(eventCell).isMovableBlock()) {
            selection.clear();
            selection.add(eventCell);

            updateSelectionState();
        } else if (selection.size() == 1 && state.canMoveTo(eventCell)) {
            CellCode selectedCell = selection.get(0);
            tryMove(selectedCell, eventCell);
        }
    }

    private void tryMove(CellCode selectedCell, CellCode eventCell) {
        if (eventCell != selectedCell) {
            linker.tryMove(selectedCell, eventCell);

            selection.clear();

            boolean selectedCellOccupied = cellMapReader.getCell(selectedCell).isAliveBlock();
            boolean eventCellOccupied = cellMapReader.getCell(eventCell).isAliveBlock();

            if (selectedCellOccupied) {
                selection.add(eventCell);
            } else if (eventCellOccupied) {
                selection.add(eventCell);
            }

            updateSelectionState();
        }
    }

    public synchronized void updateSelectionState() {
        for (Iterator<CellCode> iterator = selection.iterator(); iterator.hasNext(); ) {
            CellCode next = iterator.next();

            if (!cellMapReader.getCell(next).isAliveBlock())
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

    public SelectionState copySelectionState() {
        return state;
    }
}
