package homemade.menu.model.settings;

import homemade.menu.model.save.Save;

/**
 * Created by Marid on 27.03.2016.
 */
public class Settings
{
    private Parameter<Boolean> isRealTime = new Parameter<>("isRealTime");
    private Parameter<Integer> simultaneousSpawn = new Parameter<>("simultaneousSpawn");
    private Parameter<Integer> spawnPeriod = new Parameter<>("spawnPeriod");
    private Parameter<Integer> something = new Parameter<>("something");

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
        simultaneousSpawn.setRange(new IntRange(1, 10));//just a prank//TODO: remove joke comments
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

    public boolean getIsRealTime()
    {
        return isRealTime.getValue();
    }

    public void setIsRealTime(boolean newValue)
    {
        isRealTime.setValue(newValue);
        updateParameterInSave(isRealTime);
    }

    public int getSimultaneousSpawn()
    {
        return simultaneousSpawn.getValue();
    }

    public void setSimultaneousSpawn(int newValue)
    {
        simultaneousSpawn.setValue(newValue);
        updateParameterInSave(simultaneousSpawn);
    }

    public int getSpawnPeriod()
    {
        return spawnPeriod.getValue();
    }

    public void setSpawnPeriod(int newValue)
    {
        spawnPeriod.setValue(newValue);
        updateParameterInSave(spawnPeriod);
    }

    public int getSomething()
    {
        return something.getValue();
    }

    public void setSomething(int newValue)
    {
        something.setValue(newValue);
        updateParameterInSave(something);
    }

    private void updateParameterInSave(Parameter<?> parameter)
    {
        save.setParameterValue(parameter.getName(), parameter.getValue());
    }
}
