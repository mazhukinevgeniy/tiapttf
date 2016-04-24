package homemade.game;

import java.util.EnumSet;
import java.util.Set;

/**
 * Immutable
 */
public class CellState
{
    private static Set<Cell> cellFreeToMove = EnumSet.of(Cell.EMPTY, Cell.MARKED_FOR_SPAWN);

    private Cell cellType;
    private int cellValue;

    private boolean freeToMove;

    public CellState(Cell type, int code)
    {
        cellType = type;
        cellValue = code;

        freeToMove = cellFreeToMove.contains(type);

        if (type == Cell.OCCUPIED && code == Cell.DEFAULT_VALUE)
            throw new RuntimeException("incorrect creation of cellState");
    }

    public int value()
    {
        return cellValue;
    }

    /**
     * I think we need this method for view
     */
    public Cell type()
    {
        return cellType;
    }

    public boolean isOccupiedByBlock()
    {
        return cellType == Cell.OCCUPIED;
    }

    public boolean isFreeForMove()
    {
        return freeToMove;
    }

    public boolean isFreeForSpawn()
    {
        return cellType == Cell.EMPTY;
    }
}