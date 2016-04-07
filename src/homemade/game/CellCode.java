package homemade.game;

/**
 * Created by user3 on 31.03.2016.
 */
public class CellCode
{
    private static CellCode[] cellCodes;
    private static int[] shifts;

    public static void initializeCellCodes()
    {
        int cellCodeCap = Game.FIELD_SIZE;
        CellCode.cellCodes = new CellCode[cellCodeCap];

        for (int i = 0; i < Game.FIELD_WIDTH; i++)
            for (int j = 0; j < Game.FIELD_HEIGHT; j++)
            {
                int newCellCode = i + j * Game.FIELD_WIDTH;

                CellCode.cellCodes[newCellCode] = new CellCode(i, j);
            }

        CellCode.shifts = new int[4];
        shifts[Direction.BOTTOM.ordinal()] = Game.FIELD_WIDTH;
        shifts[Direction.TOP.ordinal()] = -Game.FIELD_WIDTH;
        shifts[Direction.LEFT.ordinal()] = -1;
        shifts[Direction.RIGHT.ordinal()] = 1;
    }

    public static CellCode getFor(int cellCodeValue)
    {
        return CellCode.cellCodes[cellCodeValue];
    }

    public static CellCode getFor(int cellX, int cellY)
    {
        return CellCode.cellCodes[cellX + cellY * Game.FIELD_WIDTH];
    }


    private int cCValue;

    private int cX, cY;

    private boolean[] isOnBorder;

    private CellCode(int x, int y)
    {
        cCValue = x + y * Game.FIELD_WIDTH;
        cX = x;
        cY = y;

        isOnBorder = new boolean[4];

        for (int i = 0; i < 4; i++)
            isOnBorder[i] = false;

        if (x == 0)
            isOnBorder[Direction.LEFT.ordinal()] = true;
        else if (x == Game.FIELD_WIDTH - 1)
            isOnBorder[Direction.RIGHT.ordinal()] = true;

        if (y == 0)
            isOnBorder[Direction.TOP.ordinal()] = true;
        else if (y == Game.FIELD_HEIGHT - 1)
            isOnBorder[Direction.BOTTOM.ordinal()] = true;
    }

    public int value() { return cCValue; }
    public int x() {return cX; }
    public int y() { return cY; }

    public boolean onBorder(Direction direction) { return isOnBorder[direction.ordinal()]; }

    public int distance(CellCode otherCell)
    {
        return Math.abs(cX - otherCell.cX + cY - otherCell.cY);
    }

    public CellCode neighbour(Direction direction)
    {
        return isOnBorder[direction.ordinal()] ?
                null :
                CellCode.cellCodes[cCValue + CellCode.shifts[direction.ordinal()]];
    }

    /**
     * these methods are not supposed to get incorrect input atm
     * at least now it looks as ugly as it probably should!
     */
    public int linkNumber(Direction direction)
    {
        return linkNumber(cCValue, cCValue + shifts[direction.ordinal()]);
    }

    public int linkNumber(CellCode otherCell)
    {
        return linkNumber(cCValue, otherCell.cCValue);
    }


    /**
     * There are (2 * width * height - width - height) links total
     * But many things are easier if we numerate as if there're 2 * width * height of them
     */
    private final int linkNumber(int cellCodeA, int cellCodeB)
    {
        if (cellCodeB < cellCodeA)
        {
            int tmp = cellCodeA;
            cellCodeA = cellCodeB;
            cellCodeB = tmp;
        }

        //now we know A < B, let's assume both are valid (ie not out of bounds and not equal)

        int numberOfLinkInPair = (cellCodeB - cellCodeA == Game.FIELD_WIDTH) ? 0 : 1;

        return cellCodeA * 2 + numberOfLinkInPair;
    }
    //TODO: remake it so that at every point of time we treat fake links as fake
}
