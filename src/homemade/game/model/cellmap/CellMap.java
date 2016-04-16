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

    public Direction getLinkDirection(CellCode cellA, CellCode cellB)
    {
        return getLinkDirection(structure.getLinkCode(cellA, cellB));
    }

    public Direction getLinkDirection(LinkCode linkCode)
    {
        return links[linkCode.intCode()].direction;
    }

    public int getChainLength(LinkCode linkCode)
    {
        return links[linkCode.intCode()].chainLength;
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

        Set<LinkCode> linksToUpdate = new HashSet<>(keys.size() * 4);

        for (CellCode key : keys)
        {
            setCellValue(key, changes.get(key));

            for (Direction direction : Direction.values())
            {
                CellCode neighbour = key.neighbour(direction);

                if (neighbour != null)
                {
                    linksToUpdate.add(structure.getLinkCode(key, neighbour));
                }
            }
        }

        for (LinkCode linkCode : linksToUpdate)
        {
            updateLinkValue(linkCode);
        }

        return changedCells;
    }

    private void setCellValue(CellCode cell, int newValue)
    {
        Cell changedCell = cells[cell.intCode()];
        changedCell.value = newValue;
    }

    private void updateLinkValue(LinkCode linkCode)
    {
        CellCode cell = linkCode.getLower();
        CellCode nextCell = linkCode.getHigher();

        Direction lowerToHigher = linkCode.getLowerToHigherDirection();

        Link link = links[linkCode.intCode()];

        int valueA = cells[cell.intCode()].value;
        int valueB = cells[nextCell.intCode()].value;

        boolean bothAreOccupied = valueA > 0 && valueB > 0;

        Direction oldDirection = link.direction;
        Direction newDirection = bothAreOccupied ?
                    (valueA > valueB ? lowerToHigher : lowerToHigher.getOpposite()) :
                    null;

        if (oldDirection != newDirection)
        {
            link.direction = newDirection;

            Set<Link> alignedLinks = new HashSet<>(structure.getMaxDimension());

            for (Direction direction : EnumSet.of(lowerToHigher, lowerToHigher.getOpposite()))
            {
                Set<Link> brokenChainLinks = new HashSet<>(structure.getMaxDimension());

                CellCode checkA = cell;
                CellCode checkB = nextCell;

                Link checkLink = links[structure.getLinkCode(checkA, checkB).intCode()];

                while (checkA != null && checkB != null && checkLink.direction == newDirection)
                {
                    alignedLinks.add(checkLink);

                    checkA = checkA.neighbour(direction);
                    checkB = checkB.neighbour(direction);

                    if (checkA != null && checkB != null)
                    {
                        checkLink = links[structure.getLinkCode(checkA, checkB).intCode()];
                    }
                }

                if (checkA == cell.neighbour(direction) && checkB == nextCell.neighbour(direction))
                {
                    while (checkA != null && checkB != null && checkLink.direction == oldDirection)
                    {
                        brokenChainLinks.add(checkLink);

                        checkA = checkA.neighbour(direction);
                        checkB = checkB.neighbour(direction);

                        if (checkA != null && checkB != null)
                        {
                            checkLink = links[structure.getLinkCode(checkA, checkB).intCode()];
                        }
                    }
                }

                int brokenChainLength = oldDirection == null ? 0 : brokenChainLinks.size() + 1;
                for (Link brokenLink : brokenChainLinks)
                {
                    brokenLink.chainLength = brokenChainLength;
                }
            }

            int newChainLength = newDirection == null ? 0 : alignedLinks.size() + 1;
            for (Link alignedLink : alignedLinks)
            {
                alignedLink.chainLength = newChainLength;
            }
        }
        //TODO: seems like there must be an opportunity to reduce code duplication
    }
}
