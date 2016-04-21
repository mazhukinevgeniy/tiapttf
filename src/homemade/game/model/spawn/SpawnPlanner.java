package homemade.game.model.spawn;

import homemade.game.Game;
import homemade.game.fieldstructure.CellCode;
import homemade.game.model.NumberPool;
import homemade.game.model.cellmap.CellMapReader;

import java.util.*;

/**
 * Marks cells where blocks will spawn
 */
class SpawnPlanner
{
    private CellMapReader cellMap;
    private NumberPool numberPool;

    private Random random;

    SpawnPlanner(CellMapReader cellMap, NumberPool numberPool)
    {
        this.cellMap = cellMap;
        this.numberPool = numberPool;

        random = new Random();
    }


    Map<CellCode, Integer> markCells(Iterator<CellCode> iterator, int targetAmount)
    {
        Map<CellCode, Integer> changes = new HashMap<>();

        int canMark = numberPool.numbersAvailable();

        if (canMark > 0)
        {
            LinkedList<CellCode> freeCells = new LinkedList<CellCode>();

            while (iterator.hasNext())
            {
                CellCode cellCode = iterator.next();

                if (cellMap.getCellValue(cellCode) == Game.CELL_EMPTY)
                    freeCells.add(cellCode);
            }

            int cellsToMark = Math.min(freeCells.size(), Math.min(targetAmount, canMark));

            for (int i = 0; i < cellsToMark; i++)
            {
                int position = random.nextInt(freeCells.size());

                changes.put(freeCells.remove(position), Game.CELL_MARKED_FOR_SPAWN);
            }
        }

        return changes;
    }

}
