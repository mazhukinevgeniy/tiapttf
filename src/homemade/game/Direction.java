package homemade.game;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by user3 on 31.03.2016.
 */
public class Direction
{
    private static final ArrayList<Integer> directionList = new ArrayList<Integer>(4);

    static
    {
        for (int i = 0; i < 4; i++)
        {
            directionList.add(i);
        }
    }

    public static final int NO_DIRECTION = -1;
    public static final int LEFT = 0;
    public static final int RIGHT = 1;
    public static final int TOP = 2;
    public static final int BOTTOM = 3;

    public static final Iterator<Integer> getIterator()
    {
        return directionList.iterator();
    }
}
