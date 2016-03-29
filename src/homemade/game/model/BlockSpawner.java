package homemade.game.model;

import homemade.game.Game;

import java.util.ArrayList;
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

    synchronized Map<Integer, Integer> spawnBlocks()
    {
        ArrayList<Cell> cells = cellMap.cells;
        Map<Integer, Integer> changes = GameModel.getCleanIntIntMap();

        for (int i = 0; i < Game.FIELD_WIDTH; i++)
            for (int j = 0; j < Game.FIELD_HEIGHT; j++)
            {
                int cellCode = i + j * Game.FIELD_WIDTH;

                if (cells.get(cellCode).getValue() == Game.CELL_MARKED_FOR_SPAWN)
                {
                    changes.put(cellCode, numberPool.takeNumber());

                    System.out.println("block spawned: " + i + ", " + j + " | " + changes.get(cellCode));
                }
            }

        return changes;
    }

    synchronized Map<Integer, Integer> markCells(int targetAmount)
    {
        Map<Integer, Integer> changes = GameModel.getCleanIntIntMap();

        int canMark = numberPool.numbersAvailable();

        if (canMark > 0)
        {
            LinkedList<Integer> freeCells = new LinkedList<Integer>();

            for (int i = 0; i < Game.FIELD_WIDTH; i++)
                for (int j = 0; j < Game.FIELD_HEIGHT; j++)
                {
                    int cellCode = i + j * Game.FIELD_WIDTH;

                    if (cellMap.cells.get(cellCode).getValue() == Game.CELL_EMPTY)
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
