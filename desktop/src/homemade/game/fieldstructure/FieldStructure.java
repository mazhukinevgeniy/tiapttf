package homemade.game.fieldstructure;

import java.util.EnumMap;
import java.util.Iterator;

public class FieldStructure {
    private static final int FIELD_WIDTH = 9;
    private static final int FIELD_HEIGHT = 9;

    private CellCode[] cellCodes;
    private LinkCode[] linkCodes;

    private EnumMap<Direction, Integer> shifts;

    private int width, height;
    private int fieldSize, numberOfLinks;

    public FieldStructure() {
        this(FIELD_WIDTH, FIELD_HEIGHT);
    }

    public FieldStructure(int width, int height) {
        this.width = width;
        this.height = height;

        fieldSize = width * height;
        numberOfLinks = (width - 1) * height + (height - 1) * width;

        shifts = new EnumMap<>(Direction.class);
        shifts.put(Direction.BOTTOM, width);
        shifts.put(Direction.TOP, -width);
        shifts.put(Direction.LEFT, -1);
        shifts.put(Direction.RIGHT, 1);

        cellCodes = CellCode.createCellCodes(this);
        linkCodes = LinkCode.createLinkCodes(this);
    }

    public CellCode getCellCode(int x, int y) {
        assert x >= 0 && x < width && y >= 0 && y < height;

        return cellCodes[cellCodeAsInt(x, y)];
        //fun fact: can be calculated as x * rightshift + y * downshift
    }

    public LinkCode getLinkCode(CellCodePair cellCodePair) {
        return getLinkCode(cellCodePair.getLower(), cellCodePair.getHigher());
    }

    public LinkCode getLinkCode(CellCode cellA, CellCode cellB) {
        assert cellA.distance(cellB) == 1;

        return linkCodes[linkCodeAsInt(cellA, cellB)];
    }

    public Iterator<CellCode> getCellCodeIterator() {
        return SafeIterator.getForImmutableArray(cellCodes);
    }

    public Iterator<LinkCode> getLinkCodeIterator() {
        return SafeIterator.getForImmutableArray(linkCodes);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getMaxDimension() {
        return Math.max(width, height);
    }

    public int getFieldSize() {
        return fieldSize;
    }

    public int getNumberOfLinks() {
        return numberOfLinks;
    }

    /**
     * Package-internal data on the method of enumeration
     */

    /**
     * If we enumerate vertical links, it's easily done.
     * <p>
     * The idea of this enumeration is to numerate vertical links first,
     * and then horizontal, as if they were vertical.
     */
    int linkCodeAsInt(CellCode lower, CellCode higher) {
        if (lower.hashCode() > higher.hashCode()) {
            CellCode tmp = lower;
            lower = higher;
            higher = tmp;
        }

        int toReturn;

        if (lower.neighbour(Direction.RIGHT) == higher) {
            int numberOfVerticalLinks = width * (height - 1);

            toReturn = numberOfVerticalLinks + lower.rotatedCellCode;
        } else if (lower.neighbour(Direction.BOTTOM) == higher) {
            toReturn = lower.hashCode();
        } else
            throw new RuntimeException("unresolvable linkCodeAsInt call");

        return toReturn;
    }

    int cellCodeAsInt(int x, int y) {
        return x + y * width;
    }

    int cellCodeAsInt(CellCode cell, Direction direction) {
        return cell.hashCode() + shifts.get(direction);
    }
}
