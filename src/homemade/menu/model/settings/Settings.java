package homemade.menu.model.settings;

import homemade.menu.model.save.ISettingsSave;

/**
 * Created by Marid on 27.03.2016.
 */
public class Settings
{
    private Parameter<Boolean> isRealTime = new Parameter<>("isRealTime");
    private Parameter<Integer> simultaneousSpawn = new Parameter<>("simultaneousSpawn");
    private Parameter<Integer> spawnPeriod = new Parameter<>("spawnPeriod");
    private Parameter<Integer> something = new Parameter<>("something");

    private ISettingsSave save = null;

    public Settings()
    {
        setDefaultSettings();
    }

    public Settings(ISettingsSave save)
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
        //parameters.get(Name.isRealTime).setEnum(new Enumeration<>(true, false));
        isRealTime.setEnum(new Enumeration<>(true, false));
        simultaneousSpawn.setRange(new IntRange(1, 9));
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

        boolValue = save.getBoolSettingsValue(isRealTime.getName());
        if (boolValue == null)
        {
            boolValue = false;
        }
        isRealTime.setValue(boolValue);

        intValue = save.getIntSettingsValue(simultaneousSpawn.getName());
        if (intValue == null)
        {
            intValue = 3;
        }
        simultaneousSpawn.setValue(intValue);

        intValue = save.getIntSettingsValue(spawnPeriod.getName());
        if (intValue == null)
        {
            intValue = 1000;
        }
        spawnPeriod.setValue(intValue);

        intValue = save.getIntSettingsValue(something.getName());
        if (intValue == null)
        {
            intValue = 2;
        }
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
        save.setSettingsValue(parameter.getName(), parameter.getValue());
    }
}
