package homemade.game.model.cellmap;

import homemade.game.Game;
import homemade.game.fieldstructure.CellCode;
import homemade.game.fieldstructure.Direction;
import homemade.game.fieldstructure.FieldStructure;
import homemade.game.fieldstructure.LinkCode;
import homemade.utils.QuickMap;

import java.util.*;

/**
 * Created by user3 on 27.03.2016.
 */
public class CellMap
{
    Cell cells[];
    Link links[];

    FieldStructure structure;

    public CellMap(FieldStructure structure)
    {
        this.structure = structure;


        cells = Cell.createCells(structure);

        int maxLinkCode = structure.getNumberOfLinks();
        links = new Link[maxLinkCode];

        for (Iterator<LinkCode> iterator = structure.getLinkCodeIterator(); iterator.hasNext();)
        {
            LinkCode link = iterator.next();
            links[link.intCode()] = new Link(link);
        }
    }

    public int getCellValue(CellCode cellCode)
    {
        return cells[cellCode.intCode()].value;
    }

    public Direction getLinkDirection(LinkCode linkCode)
    {
        return links[linkCode.intCode()].direction;
    }

    /**
     * Use this for handing user input: requested moves might be based on an outdated gameState
     * In this case, we should deny the attempt
     *
     * Could overload method for complicated movements
     */
    synchronized public Set<CellCode> tryCascadeChanges(CellCode moveFromCell, CellCode moveToCell)
    {
        int cellFromValue = cells[moveFromCell.intCode()].value;
        int cellToValue = cells[moveToCell.intCode()].value;

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
        EnumSet<Direction> verticalDirectionSet = EnumSet.of(Direction.BOTTOM, Direction.TOP);

        Cell changedCell = cells[cell.intCode()];
        changedCell.value = newValue;

        for (Direction direction : Direction.values())
        {
            CellCode neighbour = cell.neighbour(direction);

            if (neighbour != null)
            {
                int outerValue = cells[neighbour.intCode()].value;
                int multiplier = direction.getMultiplier();//TODO: probably should define direction multiplier on cellmap level


                Direction linkDirection = verticalDirectionSet.contains(direction) ? Direction.BOTTOM : Direction.RIGHT;
                //TODO: this code wants to be temporary, so go ahead, remove it

                links[structure.getLinkCode(cell, neighbour).intCode()].direction =
                        newValue > 0 &&
                        outerValue > 0 &&
                        multiplier * newValue > multiplier * outerValue ? linkDirection : null;
            }
        }
    }
}
