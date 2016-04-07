package homemade.game.model.spawn;

import homemade.game.CellCode;
import homemade.game.Game;
import homemade.game.model.NumberPool;
import homemade.game.model.cellmap.CellMap;
import homemade.utils.QuickMap;

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

    synchronized Map<CellCode, Integer> spawnBlocks()
    {
        Map<CellCode, Integer> changes = QuickMap.getCleanCellCodeIntMap();

        for (int i = 0; i < Game.FIELD_WIDTH; i++)
            for (int j = 0; j < Game.FIELD_HEIGHT; j++)
            {
                CellCode cellCode = CellCode.getFor(i, j);

                if (cellMap.getCell(cellCode).getValue() == Game.CELL_MARKED_FOR_SPAWN)
                {
                    changes.put(cellCode, numberPool.takeNumber());

                    System.out.println("block spawned: " + i + ", " + j + " | " + changes.get(cellCode.value()));
                }
            }

        return changes;
    }

    synchronized Map<CellCode, Integer> markCells(int targetAmount)
    {
        Map<CellCode, Integer> changes = QuickMap.getCleanCellCodeIntMap();

        int canMark = numberPool.numbersAvailable();

        if (canMark > 0)
        {
            LinkedList<CellCode> freeCells = new LinkedList<CellCode>();

            for (int i = 0; i < Game.FIELD_WIDTH; i++)
                for (int j = 0; j < Game.FIELD_HEIGHT; j++)
                {
                    CellCode cellCode = CellCode.getFor(i, j);


                    if (cellMap.getCell(cellCode).getValue() == Game.CELL_EMPTY)
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
