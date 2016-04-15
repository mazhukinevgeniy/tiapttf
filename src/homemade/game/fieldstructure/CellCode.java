package homemade.game.fieldstructure;

import java.util.EnumMap;

/**
 * Created by user3 on 31.03.2016.
 */
public class CellCode
{

    static CellCode[] createCellCodes(FieldStructure structure)
    {
        int width = structure.getWidth();
        int height = structure.getHeight();

        CellCode[] cellCodes = new CellCode[structure.getFieldSize()];

        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++)
            {
                int newCellCode = structure.cellCodeAsInt(i, j);

                cellCodes[newCellCode] = new CellCode(i, j, width, height, newCellCode);
            }

        for (int i = 0, size = structure.getFieldSize(); i < size; i++)
        {
            CellCode cellCode = cellCodes[i];

            for (Direction direction : Direction.values())
            {
                if (!cellCode.onBorder(direction))
                    cellCode.neighbours.put(direction, cellCodes[structure.cellCodeAsInt(cellCode, direction)]);
            }
        }

        return cellCodes;
    }

    private int cCValue;

    private int cX, cY;

    private boolean[] isOnBorder;
    private EnumMap<Direction, CellCode> neighbours;

    /**
     * Calculated as if the field was rotated at -PI/2;
     * width is oldheight
     * height is oldwidth
     * x is oldheight - 1 - oldy
     * y is oldx
     */
    int rotatedCellCode;

    private CellCode(int x, int y, int width, int height, int value)
    {
        cCValue = value;
        cX = x;
        cY = y;

        rotatedCellCode = height - (y + 1) + height * x;

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

    public int value() { return cCValue; }//TODO: why don't we rename it to intCode
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
}
