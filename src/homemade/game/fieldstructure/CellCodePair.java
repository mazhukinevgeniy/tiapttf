package homemade.game.fieldstructure;

public class CellCodePair {

    private CellCode lower;
    private CellCode higher;

    public CellCodePair(LinkCode link) {
        this.lower = link.getLower();
        this.higher = link.getHigher();
    }

    public void move(Direction direction) {
        lower = lower.neighbour(direction);
        higher = higher.neighbour(direction);
    }

    public boolean isValid() {
        return lower != null && higher != null;
    }

    public CellCode getLower() {
        return lower;
    }

    public CellCode getHigher() {
        return higher;
    }
}