package homemade.game.model;

import homemade.game.Direction;
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

    private CellMap cellMap;

    ComboDetector(CellMap cellMap)
    {
        int maxCellsPerLine = Math.max(Game.FIELD_WIDTH, Game.FIELD_HEIGHT);
        tmpStorage = new ArrayList<Integer>(maxCellsPerLine);

        this.cellMap = cellMap;
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
            iterateThroughTheLine(cellsToRemove, horizontal * Game.FIELD_WIDTH, Direction.RIGHT);
        }

        for (int vertical : verticals)
        {
            iterateThroughTheLine(cellsToRemove, vertical, Direction.BOTTOM);
        }

        return cellsToRemove;
    }

    /**
     *
     * @param set storage for found cells
     * @param start cellCode of the beginning
     * @param direction where to look for the next cell
     */
    private void iterateThroughTheLine(Set<Integer> set, int start, int direction)
    {
        tmpStorage.clear();

        System.out.println("start = " + start + ", direction = " + direction);

        Cell currentCell = cellMap.cells.get(start);
        Cell comboStartedAt = currentCell;

        int comboLength = 1;

        while (currentCell != null)
        {
            Link link = currentCell.link(direction);
            Cell tmpNext = currentCell.neighbour(direction);

            if (link != null && link.value)
            {
                comboLength++;
            }
            else
            {

                if (comboLength >= Game.MIN_COMBO)
                {
                    //TODO: actually report combo

                    String report = "";
                    Cell tmpCell = comboStartedAt;

                    while (tmpCell != tmpNext)
                    {
                        report = " " + tmpCell.getCode() + report;

                        tmpStorage.add(tmpCell.getCode());
                        tmpCell = tmpCell.neighbour(direction);
                    }

                    System.out.println("in terms of cell numbers combo is" + report);
                }

                comboStartedAt = tmpNext;
                comboLength = 1;
            }

            currentCell = tmpNext;
        }

        set.addAll(tmpStorage);
    }
}