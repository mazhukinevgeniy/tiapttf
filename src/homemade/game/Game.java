package homemade.game;

/**
 * Created by user3 on 22.03.2016.
 */
public class Game
{
    public static final int CELL_EMPTY = 0;
    public static final int CELL_MARKED_FOR_SPAWN = -1;


    public static final int MIN_COMBO = 5;


    public static final int FIELD_WIDTH = 9;
    public static final int FIELD_HEIGHT = 9;

    public static final int FIELD_SIZE = FIELD_WIDTH * FIELD_HEIGHT;
    public static final int NUMBER_OF_LINKS = (FIELD_WIDTH - 1) * FIELD_HEIGHT + (FIELD_HEIGHT - 1) * FIELD_WIDTH;
}