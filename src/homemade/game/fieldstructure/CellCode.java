package homemade.game.fieldstructure;

import java.util.EnumMap;

/**
 * Created by user3 on 31.03.2016.
 */
public class CellCode
{

    static CellCode[] createCellCodes(int width, int height, EnumMap<Direction, Integer> shifts)
    {
        int cellCodeCap = width * height;
        CellCode[] cellCodes = new CellCode[cellCodeCap];

        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++)
            {
                int newCellCode = i + j * width;

                cellCodes[newCellCode] = new CellCode(i, j, width, height, newCellCode);
            }

        for (int i = 0; i < cellCodeCap; i++)
        {
            CellCode cellCode = cellCodes[i];

            for (Direction direction : Direction.values())
            {
                if (!cellCode.onBorder(direction))
                    cellCode.neighbours.put(direction, cellCodes[cellCode.cCValue + shifts.get(direction)]);
            }
        }

        return cellCodes;
    }

    private int cCValue;

    private int cX, cY;

    private boolean[] isOnBorder;
    private EnumMap<Direction, CellCode> neighbours;

    private EnumMap<Direction, Integer> shifts;

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

        neighbours = new EnumMap<Direction, CellCode>(Direction.class);
    }

    public int value() { return cCValue; }
    public int x() {return cX; }
    public int y() { return cY; }

    public boolean onBorder(Direction direction) { return isOnBorder[direction.ordinal()]; }

    /**
     *
     * @return null if there's no such neighbour
     */
    public CellCode neighbour(Direction direction)
    {
        return neighbours.get(direction);
    }

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
        int toReturn;

        if (!onBorder(direction))
            toReturn = linkNumber(cCValue, neighbours.get(direction).cCValue);
        else
            throw new Error("imaginary link number requested");


        return toReturn;
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
