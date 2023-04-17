package homemade.game.model.spawn;

import homemade.game.fieldstructure.CellCode;
import homemade.game.model.Cell;
import homemade.game.model.CellState;
import homemade.game.model.ComboEffect;
import homemade.game.model.cellmap.CellMapReader;
import homemade.game.model.cellstates.BlockState;
import homemade.game.model.cellstates.SimpleState;
import homemade.game.state.impl.BlockValuePool;

import java.util.*;

/**
 * Marks cells where blocks will spawn
 */
class CellMarker {
    private CellMapReader cellMap;
    private BlockValuePool blockValuePool;

    private Random random;

    CellMarker(CellMapReader cellMap, BlockValuePool blockValuePool) {
        this.cellMap = cellMap;
        this.blockValuePool = blockValuePool;

        random = new Random();
    }

    /**
     * Can mark more or less than given percentage of cells
     *
     * @param percentage 0..100
     */
    Map<CellCode, CellState> markAnyCell(Iterator<CellCode> iterator, Cell type, int percentage) {
        Map<CellCode, CellState> changes = new HashMap<>();

        CellState state = SimpleState.getSimpleState(type);

        if (state == null)
            throw new RuntimeException("not a simple type");

        while (iterator.hasNext()) {
            CellCode cellCode = iterator.next();

            if (random.nextInt(100) < percentage) {
                changes.put(cellCode, state);
            }
        }

        return changes;
    }

    Map<CellCode, CellState> markBlocks(Iterator<CellCode> iterator, LinkedList<ComboEffect> effects) {
        Map<CellCode, CellState> changes = new HashMap<>();

        LinkedList<CellCode> availableBlocks = new LinkedList<>();
        while (iterator.hasNext()) {
            CellCode cellCode = iterator.next();
            CellState cellState = cellMap.getCell(cellCode);

            if (cellState.isAliveBlock() && cellState.effect() == null)
                availableBlocks.add(cellCode);
        }

        while (!availableBlocks.isEmpty() && !effects.isEmpty()) {
            int position = random.nextInt(availableBlocks.size());

            CellCode cellCode = availableBlocks.remove(position);
            CellState oldState = cellMap.getCell(cellCode);
            CellState newCellState = new BlockState(oldState.value(), oldState.isMovableBlock(), effects.removeFirst());

            System.out.println("marked block: " + newCellState.value() + ", " + newCellState.effect().toString());

            changes.put(cellCode, newCellState);
        }

        return changes;
    }

    Map<CellCode, CellState> markForSpawn(Iterator<CellCode> iterator, int targetAmount) {
        Map<CellCode, CellState> changes = new HashMap<>();

        int canMark = blockValuePool.blocksAvailable();

        if (canMark > 0) {
            LinkedList<CellCode> freeCells = new LinkedList<CellCode>();

            while (iterator.hasNext()) {
                CellCode cellCode = iterator.next();

                if (cellMap.getCell(cellCode).isFreeForSpawn())
                    freeCells.add(cellCode);
            }

            int cellsToMark = Math.min(freeCells.size(), Math.min(targetAmount, canMark));

            CellState marked = SimpleState.getSimpleState(Cell.MARKED_FOR_SPAWN);
            for (int i = 0; i < cellsToMark; i++) {
                int position = random.nextInt(freeCells.size());

                changes.put(freeCells.remove(position), marked);
            }
        }

        return changes;
    }

    //TODO: try to reduce code duplication
}
