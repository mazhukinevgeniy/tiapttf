package homemade.game.model.spawn;

import homemade.game.Cell;
import homemade.game.CellState;
import homemade.game.fieldstructure.CellCode;
import homemade.game.model.BlockPool;
import homemade.game.model.CellStates;
import homemade.game.model.cellmap.CellMapReader;

import java.util.*;

/**
 * Marks cells where blocks will spawn
 */
class CellMarker
{
    private CellMapReader cellMap;
    private BlockPool blockPool;

    private CellStates cellStates;

    private Random random;

    CellMarker(CellMapReader cellMap, BlockPool blockPool, CellStates cellStates)
    {
        this.cellMap = cellMap;
        this.blockPool = blockPool;
        this.cellStates = cellStates;

        random = new Random();
    }

    /**
     * Can mark more or less than given percentage of cells
     * @param percentage 0..100
     */
    Map<CellCode, CellState> markAnyCell(Iterator<CellCode> iterator, Cell type, int percentage)
    {
        Map<CellCode, CellState> changes = new HashMap<>();

        CellState state = cellStates.getState(type);

        if (state == null)
            throw new RuntimeException("not a simple type");

        while (iterator.hasNext())
        {
            CellCode cellCode = iterator.next();

            if (random.nextInt(100) < percentage)
            {
                changes.put(cellCode, state);
            }
        }

        return changes;
    }

    Map<CellCode, CellState> markForSpawn(Iterator<CellCode> iterator, int targetAmount)
    {
        Map<CellCode, CellState> changes = new HashMap<>();

        int canMark = blockPool.blocksAvailable();

        if (canMark > 0)
        {
            LinkedList<CellCode> freeCells = new LinkedList<CellCode>();

            while (iterator.hasNext())
            {
                CellCode cellCode = iterator.next();

                if (cellMap.getCell(cellCode).isFreeForSpawn())
                    freeCells.add(cellCode);
            }

            int cellsToMark = Math.min(freeCells.size(), Math.min(targetAmount, canMark));

            for (int i = 0; i < cellsToMark; i++)
            {
                int position = random.nextInt(freeCells.size());

                changes.put(freeCells.remove(position), cellStates.getState(Cell.MARKED_FOR_SPAWN));
            }
        }

        return changes;
    }

}