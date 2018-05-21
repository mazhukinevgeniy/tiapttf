package homemade.game.fieldstructure;

import java.util.EnumMap;

public enum Direction {
    LEFT(true), RIGHT(true), TOP(false), BOTTOM(false);

    private static EnumMap<Direction, Direction> opposites = new EnumMap<>(Direction.class);

    static {
        opposites.put(LEFT, RIGHT);
        opposites.put(RIGHT, LEFT);
        opposites.put(BOTTOM, TOP);
        opposites.put(TOP, BOTTOM);
    }

    private boolean horizontal;

    Direction(boolean isHorizontal) {
        horizontal = isHorizontal;
    }

    public Direction getOpposite() {
        return opposites.get(this);
    }

    public boolean isHorizontal() {
        return horizontal;
    }
}
