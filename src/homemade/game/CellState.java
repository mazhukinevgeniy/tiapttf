package homemade.game;

/**
 * Immutable
 */
public class CellState
{
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

    public Cell type()
    {
        return cellType;
    }

    public boolean isOccupied()
    {
        return cellType == Cell.OCCUPIED;
    }
}
