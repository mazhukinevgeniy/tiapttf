package homemade.game;

import java.util.ArrayList;

/**
 * Created by user3 on 31.03.2016.
 */
public class CellCode
{
    private static CellCode[] cellCodes;
    private static int[] shifts;

    public static void initializeCellCodes()
    {
        int cellCodeCap = Game.FIELD_WIDTH * Game.FIELD_HEIGHT;
        CellCode.cellCodes = new CellCode[cellCodeCap];

        for (int i = 0; i < Game.FIELD_WIDTH; i++)
            for (int j = 0; j < Game.FIELD_HEIGHT; j++)
            {
                int newCellCode = i + j * Game.FIELD_WIDTH;

                CellCode.cellCodes[newCellCode] = new CellCode(i, j);
            }

        CellCode.shifts = new int[4];
        shifts[Direction.BOTTOM] = Game.FIELD_WIDTH;
        shifts[Direction.TOP] = -Game.FIELD_WIDTH;
        shifts[Direction.LEFT] = -1;
        shifts[Direction.RIGHT] = 1;
    }

    public static int getShift(int direction)
    {
        return CellCode.shifts[direction];
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
            isOnBorder[Direction.LEFT] = true;
        else if (x == Game.FIELD_WIDTH - 1)
            isOnBorder[Direction.RIGHT] = true;

        if (y == 0)
            isOnBorder[Direction.TOP] = true;
        else if (y == Game.FIELD_HEIGHT - 1)
            isOnBorder[Direction.BOTTOM] = true;
    }

    public int value() { return cCValue; }
    public int x() {return cX; }
    public int y() { return cY; }

    public boolean onBorder(int direction) { return isOnBorder[direction]; }

    public int distance(CellCode otherCell)
    {
        return Math.abs(cX - otherCell.cX + cY - otherCell.cY);
    }
}
