package homemade.game.fieldstructure;

import java.util.EnumMap;
import java.util.Iterator;

/**
 * Created by user3 on 14.04.2016.
 */
public class FieldStructure
{
    private static final int FIELD_WIDTH = 9;
    private static final int FIELD_HEIGHT = 9;

    private CellCode[] cellCodes;
    private LinkCode[] linkCodes;

    private EnumMap<Direction, Integer> shifts;

    private int width, height;
    private int fieldSize, numberOfLinks;

    public FieldStructure()
    {
        this(FIELD_WIDTH, FIELD_HEIGHT);
    }

    public FieldStructure(int width, int height)
    {
        this.width = width;
        this.height = height;

        fieldSize = width * height;
        numberOfLinks = (width - 1) * height + (height - 1) * width;

        shifts = new EnumMap<>(Direction.class);
        shifts.put(Direction.BOTTOM, width);
        shifts.put(Direction.TOP, -width);
        shifts.put(Direction.LEFT, -1);
        shifts.put(Direction.RIGHT, 1);

        cellCodes = CellCode.createCellCodes(width, height, shifts);
        linkCodes = LinkCode.createLinkCodes(width, height, numberOfLinks);
    }


    public CellCode getCellCode(int x, int y)
    {
        assert x > 0;
        assert x < width;
        assert y > 0;
        assert y < height;

        return cellCodes[x + y * width];
        //fun fact: can be calculated as x * rightshift + y * downshift
    }

    public LinkCode getLinkCode(CellCode cellA, CellCode cellB)
    {
        LinkCode toReturn = null;

        //TODO: implement
        if (cellA.distance(cellB) == 1)
        {

        }

        return toReturn;
    }

    public Iterator<CellCode> getCellCodeIterator()
    {
        return SafeIterator.getForImmutableArray(cellCodes);
    }

    public Iterator<LinkCode> getLinkCodeIterator()
    {
        return SafeIterator.getForImmutableArray(linkCodes);
    }

    public int getWidth()
    {
        return width;
    }

    public int getHeight()
    {
        return height;
    }

    public int getMaxDimension()
    {
        return Math.max(width, height);
    }

    public int getFieldSize()
    {
        return fieldSize;
    }

    public int getNumberOfLinks()
    {
        return numberOfLinks;
    }
}
