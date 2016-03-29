package homemade.menu.model.settings;

import homemade.game.Game;

import java.lang.*;

/**
 * Created by Marid on 27.03.2016.
 */
public class Settings
{
    public static Parameter<Boolean> isRealTime = new Parameter<>("isRealTime", false);
    public static Parameter<Integer> simultaneousSpawn = new Parameter<>("simultaneousSpawn", 3, new IntRange(1, Game.FIELD_WIDTH * Game.FIELD_HEIGHT));
    public static Parameter<Integer> spawnPeriod = new Parameter<>("spawnPeriod", 1000, new IntRange(1000, 1000 * 60 * 60));
    public static Parameter<Integer> something = new Parameter<>("something", 3, new Enumeration<>(1, 2, 3));
}
