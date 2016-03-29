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

    //TODO: move to settings everything we could and want

    /**
     * There are (2 * width * height - width - height) links total
     * But many things are easier if we numerate as if there're 2 * width * height of them
     */
    public static final int linkNumber(int cellCodeA, int cellCodeB)
    {
        if (cellCodeB < cellCodeA)
        {
            int tmp = cellCodeA;
            cellCodeA = cellCodeB;
            cellCodeB = tmp;
        }

        //now we know A < B, let's assume both are valid (ie not out of bounds and not equal)

        int numberOfLinkInPair = (cellCodeB - cellCodeA == Game.FIELD_WIDTH) ? 0 : 1;

        return cellCodeA * 2 + numberOfLinkInPair;
    }
}
