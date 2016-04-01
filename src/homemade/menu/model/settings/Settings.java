package homemade.menu.model.settings;

import homemade.game.Game;
import homemade.menu.model.save.Save;

import java.lang.*;

/**
 * Created by Marid on 27.03.2016.
 */
public class Settings
{
    public Parameter<Boolean> isRealTime = new Parameter<>("isRealTime");
    public Parameter<Integer> simultaneousSpawn = new Parameter("simultaneousSpawn");
    public Parameter<Integer> spawnPeriod = new Parameter<>("spawnPeriod");
    public Parameter<Integer> something = new Parameter<>("something");

    private Save save = null;

    public Settings()
    {
        setDefaultSettings();
    }

    public Settings(Save save)
    {
        setDefaultSettings();
        this.save = save;
        setSavedValue();
    }

    private void setDefaultSettings()
    {
        setDefaultRange();
        setDefaultValue();
    }

    private void setDefaultRange()
    {
        isRealTime.setEnum(new Enumeration<>(true, false));
        simultaneousSpawn.setRange(new IntRange(1, Game.FIELD_WIDTH * Game.FIELD_HEIGHT));
        spawnPeriod.setRange(new IntRange(1000, 1000 * 60 * 60));
        something.setEnum(new Enumeration<>(1, 2, 3));
    }

    public void setDefaultValue()
    {
        isRealTime.setValue(false);
        simultaneousSpawn.setValue(3);
        spawnPeriod.setValue(1000);
        something.setValue(2);
    }

    private void setSavedValue()
    {
        Boolean boolValue;
        Integer intValue;

        boolValue = save.getBooleanValue(isRealTime.getName());
        isRealTime.setValue(boolValue);

        intValue = save.getIntegerValue(simultaneousSpawn.getName());
        simultaneousSpawn.setValue(intValue);

        intValue = save.getIntegerValue(spawnPeriod.getName());
        spawnPeriod.setValue(intValue);

        intValue = save.getIntegerValue(something.getName());
        something.setValue(intValue);
    }
}
