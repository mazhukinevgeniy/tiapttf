package homemade.menu.model.settings;

import homemade.game.Game;
import homemade.shell.model.save.Save;

import java.lang.*;

/**
 * Created by Marid on 27.03.2016.
 */
public class Settings
{
    public Parameter<Boolean> isRealTime;
    public Parameter<Integer> simultaneousSpawn;
    public Parameter<Integer> spawnPeriod;
    public Parameter<Integer> something;

    public Settings()
    {
        setDefaultValue();
    }

    public Settings(Save save)
    {
        setSavedValue(save);
    }

    public void setDefaultValue()
    {
        isRealTime = new Parameter<>("isRealTime", false);
        simultaneousSpawn = new Parameter<>("simultaneousSpawn", 3,
                                            new IntRange(1, Game.FIELD_WIDTH * Game.FIELD_HEIGHT));
        spawnPeriod = new Parameter<>("spawnPeriod", 1000, new IntRange(1000, 1000 * 60 * 60));
        something = new Parameter<>("something", 3, new Enumeration<>(1, 2, 3));
    }

    private void setSavedValue(Save save) { }
}
