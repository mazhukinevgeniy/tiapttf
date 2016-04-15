package homemade.utils;

import homemade.game.fieldstructure.CellCode;
import homemade.game.fieldstructure.LinkCode;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by user3 on 02.04.2016.
 */
public class QuickMap
{

    public static final Map<LinkCode, Boolean> getCleanLinkCodeBoolMap()
    {
        return new HashMap<LinkCode, Boolean>();
    }

    public static final Map<CellCode, Integer> getCleanCellCodeIntMap()
    {
        return new HashMap<CellCode, Integer>();
    }

    public static final Map<Integer, String> getCleanIntStrMap()
    {
        return new HashMap<Integer, String>();
    }
}