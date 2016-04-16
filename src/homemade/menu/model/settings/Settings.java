package homemade.menu.model.settings;

import homemade.menu.model.save.ISettingsSave;

/**
 * Created by Marid on 27.03.2016.
 */
public class Settings
{
    /*private Map<String, Parameter> parameters = QuickMap.getCleanStrParameterMap();

    private final class Name
    {
        public static final String isRealTime = "isRealTime";
        public static final String simultaneousSpawn = "simultaneousSpawn";
        public static final String spawnPeriod = "spawnPeriod";
        public static final String something = "something";
    }

    private final class DefaultValue
    {
        public static final Boolean isRealTime = "isRealTime";
        public static final Integer simultaneousSpawn = "simultaneousSpawn";
        public static final Integer spawnPeriod = "spawnPeriod";
        public static final Integer something = "something";
    }*/

    private Parameter<Boolean> isRealTime = new Parameter<>("isRealTime");//ParameterName.isRealTime);
    private Parameter<Integer> simultaneousSpawn = new Parameter<>("simultaneousSpawn");
    private Parameter<Integer> spawnPeriod = new Parameter<>("spawnPeriod");
    private Parameter<Integer> something = new Parameter<>("something");

    private ISettingsSave save = null;

    public Settings()
    {
        //mapInitialize();
        setDefaultSettings();
    }

    public Settings(ISettingsSave save)
    {
        //mapInitialize();
        setDefaultSettings();
        this.save = save;
        setSavedValue();
    }

    /*private void mapInitialize()
    {
        List<String> nameList = Arrays.asList(Name.isRealTime, Name.simultaneousSpawn,
                                                    Name.spawnPeriod, Name.something);

        for (String name : nameList)
        {
            Parameter<?> parameter = new Parameter<>(name);
            parameters.put(Name.isRealTime, parameter);
        }
    }*/

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
