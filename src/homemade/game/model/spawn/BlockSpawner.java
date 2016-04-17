package homemade.game.model.spawn;

import homemade.game.Game;
import homemade.game.fieldstructure.CellCode;
import homemade.game.model.NumberPool;
import homemade.game.model.cellmap.CellMap;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

/**
 * Created by user3 on 29.03.2016.
 */
class BlockSpawner
{
    private CellMap cellMap;
    private NumberPool numberPool;

    BlockSpawner(CellMap cellMap, NumberPool numberPool)
    {
        this.cellMap = cellMap;
        this.numberPool = numberPool;
    }

    synchronized Map<CellCode, Integer> spawnBlocks(Iterator<CellCode> iterator)
    {
        Map<CellCode, Integer> changes = new HashMap<>();

        while (iterator.hasNext())
        {
            CellCode cellCode = iterator.next();

            if (cellMap.getCellValue(cellCode) == Game.CELL_MARKED_FOR_SPAWN)
            {
                changes.put(cellCode, numberPool.takeNumber());

                System.out.println("block spawned: " + cellCode.x() + ", " + cellCode.y() + " | " + changes.get(cellCode));
            }
        }

        return changes;
    }

    synchronized Map<CellCode, Integer> markCells(Iterator<CellCode> iterator, int targetAmount)
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
                int position = (int) (Math.random() * (double) freeCells.size());

                changes.put(freeCells.get(position), Game.CELL_MARKED_FOR_SPAWN);

                freeCells.remove(position);
            }
        }
        else
        {
            //TODO: game over (:
        }

        return changes;
    }
}
