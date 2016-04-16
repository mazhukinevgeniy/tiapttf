package homemade.game.fieldstructure;

/**
 * This class represents link as a part of the field structure.
 */
public class LinkCode
{

    static LinkCode[] createLinkCodes(FieldStructure structure)
    {
        LinkCode codes[] = new LinkCode[structure.getNumberOfLinks()];

        int width = structure.getWidth();
        int height = structure.getHeight();

        for (int j = 0; j < height - 1; j++)
            for (int i = 0; i < width; i++)
            {
                createLinkCode(structure, structure.getCellCode(i, j), Direction.BOTTOM, codes);
            }

        for (int i = 0; i < width - 1; i++)
            for (int j = 0; j < height; j++)
            {
                createLinkCode(structure, structure.getCellCode(i, j), Direction.RIGHT, codes);
            }

        return codes;
    }

    private static void createLinkCode(FieldStructure structure, CellCode lower, Direction direction, LinkCode codes[])
    {
        CellCode higher = lower.neighbour(direction);
        int code = structure.linkCodeAsInt(lower, higher);

        codes[code] = new LinkCode(direction, lower, higher, code);
    }

    private CellCode higher, lower;
    private Direction direction;

    private int code;

    private LinkCode(Direction direction, CellCode lower, CellCode higher, int code)
    {
        this.direction = direction;

        this.lower = lower;
        this.higher = higher;

        this.code = code;
    }

    public int intCode()
    {
        return code;
    }

    public CellCode getLower()
    {
        return lower;
    }

    public CellCode getHigher()
    {
        return higher;
    }

    public Direction getLowerToHigherDirection()
    {
        return direction;
    }
}
