package homemade.game.model;

import homemade.game.Direction;
import homemade.game.Game;
import homemade.utils.QuickMap;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

/**
 * Created by user3 on 27.03.2016.
 */
class CellMap
{
    ArrayList<Cell> cells;
    ArrayList<Link> links;
    //TODO: figure if it's safe and needed to replace these with arrays

    private ArrayBasedGameState updatableState;
    private NumberPool numberPool;


    CellMap(ArrayBasedGameState updatableState, NumberPool numberPool)
    {
        this.updatableState = updatableState;
        this.numberPool = numberPool;

        cells = Cell.createLinkedCells();

        int maxCellCode = Game.FIELD_WIDTH * Game.FIELD_HEIGHT;
        int maxLinkCode = maxCellCode * 2;


        links = new ArrayList<Link>(maxLinkCode);

        //size and cycle below are both based on how Game.linkNumber works
        //theoretical minimum for size is width * height * 2 - width - height

        for (int i = 0; i < maxCellCode; i++)
        {
            Cell cell = cells.get(i);

            links.add(2 * i, cell.link(Direction.BOTTOM));
            links.add(2 * i + 1, cell.link(Direction.RIGHT));
        }
    }

    /**
     * Use this for handing user input: requested moves might be based on an outdated gameState
     * In this case, we should deny the attempt
     *
     * Could overload method for complicated movements
     */
    synchronized void tryCascadeChanges(int moveFromCell, int moveToCell)
    {
        int cellFromValue = cells.get(moveFromCell).getValue();
        int cellToValue = cells.get(moveToCell).getValue();

        if (cellToValue <= 0 && cellFromValue > 0)
        {
            Map<Integer, Integer> tmpMap = QuickMap.getCleanIntIntMap();
            tmpMap.put(moveFromCell, Game.CELL_EMPTY);
            tmpMap.put(moveToCell, cellFromValue);

            applyCascadeChanges(tmpMap);
        }
    }

    /**
     * Will apply the requested changes, check for reactions and update the mutable version of gameState
     * Seems to be excessive, but I'm not quite sure how to approach this issue
     */
    synchronized void applyCascadeChanges(Map<Integer, Integer> changes)
    {
        Set<Integer> keys = changes.keySet();

        for (int key : keys)
        {
            int value = changes.get(key);

            cells.get(key).setValue(value);
        }

        Map<Integer, Integer> updatedCells = QuickMap.getCleanIntIntMap();
        updatedCells.putAll(changes);

        if (Game.AUTOCOMPLETION)
        {
            checkCombos(updatedCells);
        }

        Map<Integer, Boolean> updatedLinks = QuickMap.getCleanIntBoolMap();

        keys = updatedCells.keySet();

        for (int key: keys)
        {
            Cell cell = cells.get(key);

            for (Direction direction : Direction.values())
            {
                Link link = cell.link(direction);

                if (link != null)
                {
                    updatedLinks.put(link.getNumber(), link.value);
                }
            }
        }

        updatableState.updateFieldSnapshot(updatedCells, updatedLinks);
    }

    private void checkCombos(Map<Integer, Integer> updatedChanges)
    {
        Set<Integer> cellsToRemove = new ComboDetector(this).findCellsToRemove(updatedChanges.keySet());

        for (int cellCode : cellsToRemove)
        {
            Cell cell = cells.get(cellCode);

            numberPool.freeNumber(cell.getValue());
            cell.setValue(Game.CELL_EMPTY);

            updatedChanges.put(cellCode, Game.CELL_EMPTY);
        }
    }
}
