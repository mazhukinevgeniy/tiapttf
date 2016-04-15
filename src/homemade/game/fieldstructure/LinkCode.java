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
                CellCode lower = structure.getCellCode(i, j);
                CellCode higher = lower.neighbour(Direction.BOTTOM);
                int code = structure.linkCodeAsInt(lower, higher);

                codes[code] = new LinkCode(lower, higher, code);
            }

        for (int i = 0; i < width - 1; i++)
            for (int j = 0; j < height; j++)
            {
                CellCode lower = structure.getCellCode(i, j);
                CellCode higher = lower.neighbour(Direction.RIGHT);
                int code = structure.linkCodeAsInt(lower, higher);

                codes[code] = new LinkCode(lower, higher, code);
            }

        return codes;
    }

    private CellCode higher, lower;

    private int code;

    private LinkCode(CellCode lower, CellCode higher, int code)
    {
        this.lower = lower;
        this.higher = higher;

        this.code = code;
    }

    public int intCode()
    {
        return code;
    }

}
