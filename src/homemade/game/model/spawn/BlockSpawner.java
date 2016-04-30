package homemade.game.model.spawn;

import homemade.game.Cell;
import homemade.game.CellState;
import homemade.game.fieldstructure.CellCode;
import homemade.game.model.BlockPool;
import homemade.game.model.cellmap.CellMapReader;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

class BlockSpawner
{
    private CellMapReader cellMap;
    private BlockPool blockPool;

    BlockSpawner(CellMapReader cellMap, BlockPool blockPool)
    {
        this.cellMap = cellMap;
        this.blockPool = blockPool;
    }

    Map<CellCode, CellState> spawnBlocks(Iterator<CellCode> iterator)
    {
        Map<CellCode, CellState> changes = new HashMap<>();

        while (iterator.hasNext())
        {
            CellCode cellCode = iterator.next();

            if (cellMap.getCell(cellCode).type() == Cell.MARKED_FOR_SPAWN)
            {
                changes.put(cellCode, blockPool.takeBlock());
            }
        }

        return changes;
    }
}
