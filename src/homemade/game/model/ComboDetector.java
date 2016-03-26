package homemade.game.model;

import homemade.game.Game;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by user3 on 26.03.2016.
 */
class ComboDetector
{
    private ArrayList<Integer> tmpStorage;
    //it's probably better than reallocating every time

    private int[] cellValues;

    ComboDetector(int[] cellValues)
    {
        int maxCellsPerLine = Math.max(Game.FIELD_WIDTH, Game.FIELD_HEIGHT);
        tmpStorage = new ArrayList<Integer>(maxCellsPerLine);

        this.cellValues = cellValues;
    }

    /**
     * @return Set of cellCodes in which blocks are bound to be removed because they were part of a combo
     */
    Set<Integer> findCellsToRemove(Set<Integer> starts)
    {
        int numberOfStarts = starts.size();

        Set<Integer> horizontals = new HashSet<Integer>(numberOfStarts);
        Set<Integer> verticals = new HashSet<Integer>(numberOfStarts);

        for (int cellCode : starts)
        {
            horizontals.add(cellCode / Game.FIELD_WIDTH);
            verticals.add(cellCode % Game.FIELD_WIDTH);
        }

        int hSize = horizontals.size();
        int vSize = verticals.size();

        int maxCellsToRemove = hSize * Game.FIELD_WIDTH + vSize * Game.FIELD_HEIGHT - hSize * vSize;

        HashSet<Integer> cellsToRemove = new HashSet<Integer>(maxCellsToRemove);

        for (int horizontal : horizontals)
        {
            iterateThroughTheLine(cellsToRemove, horizontal * Game.FIELD_WIDTH, 1, 0);
        }

        for (int vertical : verticals)
        {
            iterateThroughTheLine(cellsToRemove, vertical, 0, 1);
        }

        return cellsToRemove;
    }

    /**
     *
     * @param set storage for found cells
     * @param start cellCode of the beginning
     * @param stepX x component of line direction; assuming 1 or 0
     * @param stepY y component of line direction; assuming 0 or 1
     */
    private void iterateThroughTheLine(Set<Integer> set, int start, int stepX, int stepY)
    {
        tmpStorage.clear();

        int step = stepX + Game.FIELD_WIDTH * stepY;
        int cellsLeft = stepX * Game.FIELD_WIDTH + stepY * Game.FIELD_HEIGHT;

        int currentCell = start;
        int comboLength = 1;
        cellsLeft--;

        int lastValue = cellValues[start];
        int nextValue;

        while (cellsLeft > 0)
        {
            currentCell += step;
            cellsLeft--;

            nextValue = cellValues[currentCell];

            if (lastValue > 0 && nextValue > lastValue)
            {
                comboLength++;
            }
            else
            {
                if (comboLength >= Game.MIN_COMBO)
                {
                    //TODO: report combo

                    for (int i = 0; i < comboLength; i++)
                    {
                        tmpStorage.add(currentCell - (1 + i) * step);
                    }
                }

                comboLength = 1;

                if (cellsLeft + 1 < Game.MIN_COMBO)
                    cellsLeft = 0;
            }

            lastValue = nextValue;
        }

        set.addAll(tmpStorage);
    }
}
//TODO: check if EnumSet will improve performance