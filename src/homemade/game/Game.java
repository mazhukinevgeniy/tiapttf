package homemade.game;

/**
 * Created by user3 on 22.03.2016.
 */
public class Game
{
    public static final int CELL_EMPTY = 0;
    public static final int CELL_MARKED_FOR_SPAWN = -1;

    public static final int FIELD_WIDTH = 9;
    public static final int FIELD_HEIGHT = 9;

    public static final boolean AUTOCOMPLETION = true;
    public static final int MIN_COMBO = 5;

    public static final int SIMULTANEOUS_SPAWN = 3;
    public static final int SPAWN_PERIOD = 1000;

    public static final int TARGET_FPS = 60;
    public static final int KEY_INPUT_CAP_PER_SECOND = 8;

    //TODO: move to settings everything we could and want

}
