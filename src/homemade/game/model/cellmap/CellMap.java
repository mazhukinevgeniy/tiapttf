package homemade.game.model.cellmap;

import homemade.game.Cell;
import homemade.game.CellState;
import homemade.game.fieldstructure.CellCode;
import homemade.game.fieldstructure.Direction;
import homemade.game.fieldstructure.FieldStructure;
import homemade.game.fieldstructure.LinkCode;
import homemade.game.model.CellStates;

import java.util.*;

/**
 * Must be synchronized externally!
 */
public class CellMap implements CellMapReader
{
    private CellState cells[];
    private Link links[];

    private FieldStructure structure;

    public CellMap(FieldStructure structure, CellStates states)
    {
        this.structure = structure;

        cells = new CellState[structure.getFieldSize()];

        CellState emptyState = states.getState(Cell.EMPTY);

        for (int j = 0, height = structure.getHeight(); j < height; j++)
            for (int i = 0, width = structure.getWidth(); i < width; i++)
            {
                cells[structure.getCellCode(i, j).hashCode()] = emptyState;
            }

        int maxLinkCode = structure.getNumberOfLinks();
        links = new Link[maxLinkCode];

        for (Iterator<LinkCode> iterator = structure.getLinkCodeIterator(); iterator.hasNext();)
        {
            LinkCode link = iterator.next();
            links[link.hashCode()] = new Link();
        }
    }

    public CellState getCell(CellCode cellCode)
    {
        return cells[cellCode.hashCode()];
    }

    public Direction getLinkDirection(CellCode cellA, CellCode cellB)
    {
        return getLinkDirection(structure.getLinkCode(cellA, cellB));
    }

    public Direction getLinkDirection(LinkCode linkCode)
    {
        return links[linkCode.hashCode()].direction;
    }

    public int getChainLength(LinkCode linkCode)
    {
        return links[linkCode.hashCode()].chainLength;
    }

    public Set<CellCode> applyCascadeChanges(Map<CellCode, CellState> changes)
    {
        Set<CellCode> keys = changes.keySet();
        Set<CellCode> changedCells = new HashSet<CellCode>(keys);

        Set<LinkCode> linksToUpdate = new HashSet<>(keys.size() * 4);

        for (CellCode key : keys)
        {
            CellState newState = changes.get(key);
            CellState oldState = cells[key.hashCode()];

            if (newState != oldState)
            {
                cells[key.hashCode()] = newState;

                for (Direction direction : Direction.values())
                {
                    CellCode neighbour = key.neighbour(direction);

                    if (neighbour != null)
                    {
                        linksToUpdate.add(structure.getLinkCode(key, neighbour));
                    }
                }
            }
            else
                System.out.println("no change in cellmap");
        }

        for (LinkCode linkCode : linksToUpdate)
        {
            updateLinkValue(linkCode);
        }

        return changedCells;
    }

    private void updateLinkValue(LinkCode linkCode)
    {
        CellCode cell = linkCode.getLower();
        CellCode nextCell = linkCode.getHigher();

        Direction lowerToHigher = linkCode.getLowerToHigherDirection();

        Link link = links[linkCode.hashCode()];

        CellState cellA = cells[cell.hashCode()];
        CellState cellB = cells[nextCell.hashCode()];

        boolean bothAreOccupied = cellA.isNormalBlock() && cellB.isNormalBlock();

        Direction oldDirection = link.direction;
        Direction newDirection = bothAreOccupied ?
                    (cellA.value() > cellB.value() ? lowerToHigher : lowerToHigher.getOpposite()) :
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

                Link checkLink = links[structure.getLinkCode(checkA, checkB).hashCode()];

                while (checkA != null && checkB != null && checkLink.direction == newDirection)
                {
                    alignedLinks.add(checkLink);

                    checkA = checkA.neighbour(direction);
                    checkB = checkB.neighbour(direction);

                    if (checkA != null && checkB != null)
                    {
                        checkLink = links[structure.getLinkCode(checkA, checkB).hashCode()];
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
                            checkLink = links[structure.getLinkCode(checkA, checkB).hashCode()];
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

    private static class Link
    {
        private Direction direction;
        private int chainLength;

        private Link()
        {
            direction = null;
            chainLength = 0;
        }
    }
}
