package homemade.game.model.cellmap;

import homemade.game.Game;
import homemade.game.fieldstructure.CellCode;
import homemade.game.fieldstructure.Direction;
import homemade.game.fieldstructure.FieldStructure;
import homemade.utils.QuickMap;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by user3 on 27.03.2016.
 */
public class CellMap
{
    ArrayList<Cell> cells;
    ArrayList<Link> links;
    //TODO: figure if it's safe and needed to replace these with arrays


    public CellMap(FieldStructure structure)
    {
        cells = Cell.createLinkedCells(structure);

        int maxCellCode = structure.getFieldSize();
        int maxLinkCode = maxCellCode * 2;


        links = new ArrayList<Link>(maxLinkCode);

        //size and cycle below are both based on how Game.linkNumber works
        //theoretical minimum for size is width * height * 2 - width - height
        //TODO: remove dependency //odds are, cellmap is going out anyway

        for (int i = 0; i < maxCellCode; i++)
        {
            Cell cell = cells.get(i);

            links.add(2 * i, cell.link(Direction.BOTTOM));
            links.add(2 * i + 1, cell.link(Direction.RIGHT));
        }
    }

    public Cell getCell(CellCode cellCode)
    {
        return cells.get(cellCode.value());
    }

    /**
     * Use this for handing user input: requested moves might be based on an outdated gameState
     * In this case, we should deny the attempt
     *
     * Could overload method for complicated movements
     */
    synchronized public Set<CellCode> tryCascadeChanges(CellCode moveFromCell, CellCode moveToCell)
    {
        int cellFromValue = cells.get(moveFromCell.value()).getValue();
        int cellToValue = cells.get(moveToCell.value()).getValue();

        Set<CellCode> changedCells = null;

        if (cellToValue <= 0 && cellFromValue > 0)
        {
            Map<CellCode, Integer> tmpMap = QuickMap.getCleanCellCodeIntMap();
            tmpMap.put(moveFromCell, Game.CELL_EMPTY);
            tmpMap.put(moveToCell, cellFromValue);

            changedCells = applyCascadeChanges(tmpMap);
        }

        return changedCells;
    }

    synchronized public Set<CellCode> applyCascadeChanges(Map<CellCode, Integer> changes)
    {
        Set<CellCode> keys = changes.keySet();
        Set<CellCode> changedCells = new HashSet<CellCode>(keys);

        for (CellCode key : keys)
        {
            setCellValue(key, changes.get(key));
        }

        return changedCells;
    }

    private void setCellValue(CellCode cell, int newValue)
    {
        Cell changedCell = cells.get(cell.value());
        changedCell.value = newValue;

        for (Direction direction : Direction.values())
        {
            CellCode neighbour = cell.neighbour(direction);

            if (neighbour != null)
            {
                int outerValue = cells.get(neighbour.value()).value;
                int multiplier = direction.getMultiplier();//TODO: probably should define direction multiplier on cellmap level

                changedCell.links[direction.ordinal()].value =
                        newValue > 0 &&
                        outerValue > 0 &&
                        multiplier * newValue > multiplier * outerValue;
            }
        }
    }
}
