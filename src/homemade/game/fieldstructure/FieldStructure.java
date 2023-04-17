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

        cellCodes = createCellCodes();
        linkCodes = createLinkCodes();
    }

    private CellCode[] createCellCodes() {
        CellCode[] cellCodes = new CellCode[getFieldSize()];

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int newCellCode = i + j * width;

                cellCodes[newCellCode] = new CellCode(i, j, width, height, newCellCode);
            }
        }

        for (int i = 0, size = getFieldSize(); i < size; i++) {
            CellCode cellCode = cellCodes[i];

            for (Direction direction : Direction.values()) {
                if (!cellCode.onBorder(direction)) {
                    cellCode.neighbours.put(direction, cellCodes[cellCode.hashCode() + shifts.get(direction)]);
                }
            }
        }

        return cellCodes;
    }

    private LinkCode[] createLinkCodes() {
        LinkCode codes[] = new LinkCode[getNumberOfLinks()];

        for (int j = 0; j < height - 1; j++)
            for (int i = 0; i < width; i++) {
                createLinkCode(getCellCode(i, j), Direction.BOTTOM, codes);
            }

        for (int i = 0; i < width - 1; i++)
            for (int j = 0; j < height; j++) {
                createLinkCode(getCellCode(i, j), Direction.RIGHT, codes);
            }

        return codes;
    }

    private void createLinkCode(CellCode lower, Direction direction, LinkCode codes[]) {
        CellCode higher = lower.neighbour(direction);
        int code = linkCodeAsInt(lower, higher);

        codes[code] = new LinkCode(direction, lower, higher, code);
    }

    public CellCode getCellCode(int x, int y) {
        assert x >= 0 && x < width && y >= 0 && y < height;

        return cellCodes[x + y * width];
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
    private int linkCodeAsInt(CellCode lower, CellCode higher) {
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
        } else {
            throw new RuntimeException("unresolvable linkCodeAsInt call");
        }

        return toReturn;
    }
}
