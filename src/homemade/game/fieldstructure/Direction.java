package homemade.game.fieldstructure;

import java.util.EnumMap;

/**
 * Created by user3 on 31.03.2016.
 */
public enum Direction
{
    LEFT(), RIGHT(), TOP(), BOTTOM();

    private static EnumMap<Direction, Direction> opposites = new EnumMap<>(Direction.class);
    private static EnumMap<Direction, Integer> multipliers = new EnumMap<>(Direction.class);

    static
    {
        opposites.put(LEFT, RIGHT);
        opposites.put(RIGHT, LEFT);
        opposites.put(BOTTOM, TOP);
        opposites.put(TOP, BOTTOM);

        multipliers.put(TOP, 1);
        multipliers.put(BOTTOM, -1);
        multipliers.put(RIGHT, -1);
        multipliers.put(LEFT, 1);
    }

    public int getMultiplier()
    {
        return multipliers.get(this);
    }


    public Direction getOpposite()
    {
        return opposites.get(this);
    }
}
