package homemade.game.fieldstructure;

import java.util.EnumMap;

public enum Direction {
    LEFT() {
        @Override
        public boolean isHorizontal() {
            return true;
        }
    },
    RIGHT() {
        @Override
        public boolean isHorizontal() {
            return true;
        }
    },
    TOP() {
        @Override
        public boolean isHorizontal() {
            return false;
        }
    },
    BOTTOM() {
        @Override
        public boolean isHorizontal() {
            return false;
        }
    };

    private static EnumMap<Direction, Direction> opposites = new EnumMap<>(Direction.class);

    static {
        opposites.put(LEFT, RIGHT);
        opposites.put(RIGHT, LEFT);
        opposites.put(BOTTOM, TOP);
        opposites.put(TOP, BOTTOM);
    }


    public Direction getOpposite() {
        return opposites.get(this);
    }

    abstract public boolean isHorizontal();
}
