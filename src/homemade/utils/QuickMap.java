package homemade.utils;

import homemade.game.fieldstructure.CellCode;
import homemade.game.fieldstructure.Direction;
import homemade.game.fieldstructure.LinkCode;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by user3 on 02.04.2016.
 */
public class QuickMap
{
    //TODO: check if it is really really the best way to create maps

    public static final Map<LinkCode, Direction> getCleanLinkCodeDirectionMap()
    {
        return new HashMap<LinkCode, Direction>();
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