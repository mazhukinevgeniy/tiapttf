package homemade.game.fieldstructure;

/**
 * This class represents link as a part of the field structure.
 */
public final class LinkCode {

    private CellCode higher, lower;
    private Direction direction;

    private int code;

    LinkCode(Direction direction, CellCode lower, CellCode higher, int code) {
        this.direction = direction;

        this.lower = lower;
        this.higher = higher;

        this.code = code;
    }

    @Override
    public int hashCode() {
        return code;
    }

    public CellCode getLower() {
        return lower;
    }

    public CellCode getHigher() {
        return higher;
    }

    public Direction getLowerToHigherDirection() {
        return direction;
    }
}
