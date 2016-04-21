package homemade.game.model.spawn;

import homemade.game.Game;
import homemade.game.fieldstructure.CellCode;
import homemade.game.model.NumberPool;
import homemade.game.model.cellmap.CellMapReader;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by user3 on 29.03.2016.
 */
class BlockSpawner
{
    private CellMapReader cellMap;
    private NumberPool numberPool;

    BlockSpawner(CellMapReader cellMap, NumberPool numberPool)
    {
        this.cellMap = cellMap;
        this.numberPool = numberPool;
    }

    Map<CellCode, Integer> spawnBlocks(Iterator<CellCode> iterator)
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
}
