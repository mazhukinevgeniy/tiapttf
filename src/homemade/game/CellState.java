package homemade.game;

import java.util.EnumSet;
import java.util.Set;

/**
 * Immutable
 */
public class CellState
{
    private static Set<Cell> cellFreeToMove = EnumSet.of(Cell.EMPTY, Cell.MARKED_FOR_SPAWN);
    private static Set<Cell> blocks = EnumSet.of(Cell.OCCUPIED, Cell.DEAD_BLOCK);

    private Cell cellType;
    private int cellValue;

    public CellState(Cell type, int code)
    {
        cellType = type;
        cellValue = code;

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

    public boolean isNormalBlock()
    {
        return cellType == Cell.OCCUPIED;
    }

    public boolean isAnyBlock()
    {
        return blocks.contains(cellType);
    }


    public boolean isFreeForMove()
    {
        return cellFreeToMove.contains(cellType);
    }

    public boolean isFreeForSpawn()
    {
        return cellType == Cell.EMPTY;
    }
}