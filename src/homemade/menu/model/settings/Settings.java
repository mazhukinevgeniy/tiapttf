package homemade.menu.model.settings;

import homemade.menu.model.save.SettingsSave;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Marid on 27.03.2016.
 */
public class Settings
{
    //1) add name new parameter
    public final class Name
    {
        public static final String isRealTime = "isRealTime";
        public static final String simultaneousSpawn = "simultaneousSpawn";
        public static final String spawnPeriod = "spawnPeriod";
        public static final String something = "something";
    }

    //2) add parameter to eligible list with names
    private List<String> nameListBool = Arrays.asList(Name.isRealTime);

    private List<String> nameListInt = Arrays.asList(Name.simultaneousSpawn,
                                                     Name.spawnPeriod,
                                                     Name.something);

    //3) state default value and Range/Enum valid values
    public static class Default
    {
        public static Map<String, ValueChecker<?>> checkers = new HashMap<>();

        public static void initialize()
        {
            checkers.put(Name.isRealTime, new InSetChecker<>(true, true, false));
            checkers.put(Name.simultaneousSpawn, new RangeChecker<>(3, 1, 9));
            checkers.put(Name.spawnPeriod, new RangeChecker<>(1000, 1000, 1000 * 60 * 60));
            checkers.put(Name.something, new InSetChecker<>(2, 1, 2, 3));
        }
    }

    private Map<String, Parameter<Boolean>> boolParameters = new HashMap<>();
    private Map<String, Parameter<Integer>> intParameters = new HashMap<>();

    private SettingsSave save = null;

    public Settings()
    {
        Default.initialize();
        initializeMaps();
        setDefaultSettings();
    }

    public Settings(SettingsSave save)
    {
        Default.initialize();
        initializeMaps();
        setDefaultSettings();

        this.save = save;
        setSavedValues();
    }

    private void initializeMaps()
    {
        initializeMap(nameListBool, boolParameters);
        initializeMap(nameListInt, intParameters);
    }

    private <Type> void initializeMap(List<String> nameList, Map<String, Parameter<Type>> parameters)
    {
        for (String name : nameList)
        {
            Parameter<Type> parameter = new Parameter<>(name);
            parameters.put(name, parameter);
        }
    }

    private void setDefaultSettings()
    {
        setDefaultSettingToMap(nameListBool, boolParameters);
        setDefaultSettingToMap(nameListInt, intParameters);
    }

    private <Type> void setDefaultSettingToMap(List<String> nameList, Map<String, Parameter<Type>> parameters)
    {
        for (String name : nameList)
        {
            Parameter<Type> parameter = parameters.get(name);
            ValueChecker<Type> checker = (ValueChecker<Type>)Default.checkers.get(name);
            parameter.setValueChecker(checker);
            parameter.setDefaultValue();
        }
    }

    private void setSavedValues()
    {
        Boolean boolValue;
        for(String name : nameListBool)
        {
            boolValue = save.getBoolSettingsValue(name);
            Parameter<Boolean> parameter = boolParameters.get(name);
            if (boolValue == null)
            {
                parameter.setDefaultValue();
            }
            else
            {
                parameter.setValue(boolValue);
            }
        }

        Integer intValue;
        for(String name : nameListInt)
        {
            intValue = save.getIntSettingsValue(name);
            Parameter<Integer> parameter = intParameters.get(name);
            if (intValue == null)
            {
                parameter.setDefaultValue();
            }
            else
            {
                parameter.setValue(intValue);
            }
        }
    }

    public void set(String parameterName, Boolean value)
    {
        if (boolParameters.containsKey(parameterName))
        {
            Parameter<Boolean> parameter = boolParameters.get(parameterName);
            parameter.setValue(value);
            updateParameterInSave(parameter);
        }
    }

    public void set(String parameterName, Integer value)
    {
        if (intParameters.containsKey(parameterName))
        {
            Parameter<Integer> parameter = intParameters.get(parameterName);
            parameter.setValue(value);
            updateParameterInSave(parameter);
        }
    }

    public <Type> Type get(String parameterName)
    {
        Parameter<?> parameter = null;
        if (boolParameters.containsKey(parameterName))
        {
            parameter = boolParameters.get(parameterName);
        }
        else if (intParameters.containsKey(parameterName))
        {
            parameter = intParameters.get(parameterName);
        }

        Type value = null;
        if(parameter != null)
        {
            value = (Type)parameter.getValue();
        }

        return value;
    }

    private void updateParameterInSave(Parameter<?> parameter)
    {
        save.setSettingsValue(parameter.getName(), parameter.getValue());
    }
}
