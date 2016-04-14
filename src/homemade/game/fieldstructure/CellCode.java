package homemade.game.fieldstructure;

/**
 * Created by user3 on 31.03.2016.
 */
public class CellCode
{

    static CellCode[] createCellCodes(int width, int height)
    {
        int cellCodeCap = width * height;
        CellCode[] cellCodes = new CellCode[cellCodeCap];

        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++)
            {
                int newCellCode = i + j * width;

                cellCodes[newCellCode] = new CellCode(i, j, width, height, newCellCode);
            }

        return cellCodes;
    }

    private int cCValue;

    private int cX, cY;

    private boolean[] isOnBorder;

    private CellCode(int x, int y, int width, int height, int value)
    {
        cCValue = value;
        cX = x;
        cY = y;

        isOnBorder = new boolean[4];

        for (int i = 0; i < 4; i++)
            isOnBorder[i] = false;

        if (x == 0)
            isOnBorder[Direction.LEFT.ordinal()] = true;
        else if (x == width - 1)
            isOnBorder[Direction.RIGHT.ordinal()] = true;

        if (y == 0)
            isOnBorder[Direction.TOP.ordinal()] = true;
        else if (y == height - 1)
            isOnBorder[Direction.BOTTOM.ordinal()] = true;
    }

    public int value() { return cCValue; }
    public int x() {return cX; }
    public int y() { return cY; }

    public boolean onBorder(Direction direction) { return isOnBorder[direction.ordinal()]; }

    public int distance(CellCode otherCell)
    {
        return Math.abs(cX - otherCell.cX) + Math.abs(cY - otherCell.cY);
    }


    //TODO: remove link-related methods below
    public int linkNumber(CellCode otherCell)
    {
        return linkNumber(cCValue, otherCell.cCValue);
    }

    public int linkNumber(Direction direction)
    {
        int otherCell = cCValue;

        if (direction == Direction.RIGHT)
            otherCell += 1;
        else if (direction == Direction.BOTTOM)
            otherCell += 9; //TODO: get rid of this bandaid ASAP
        else throw new Error("temporary code is temporary, right?");

        return linkNumber(cCValue, otherCell);
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

        int numberOfLinkInPair = (cellCodeB - cellCodeA == 1) ? 1 : 0;

        return cellCodeA * 2 + numberOfLinkInPair;
    }
    //TODO: remake it so that at every point of time we treat fake links as fake
    
}
