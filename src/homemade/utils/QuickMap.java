package homemade.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by user3 on 02.04.2016.
 */
public class QuickMap
{

    public static final Map<Integer, Integer> getCleanIntIntMap()
    {
        return new HashMap<Integer, Integer>();
    }

    public static final Map<Integer, Boolean> getCleanIntBoolMap()
    {
        return new HashMap<Integer, Boolean>();
    }

    public static final Map<Integer, String> getCleanIntStrMap()
    {
        return new HashMap<Integer, String>();
    }
}
