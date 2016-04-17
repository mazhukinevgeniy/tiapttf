package homemade.menu.model.settings;

import homemade.menu.model.save.SettingsSave;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

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
    //4) everything should work (=

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
        Function<String, Boolean> boolSettingsValue = name -> save.getBoolSettingsValue(name);
        Function<String, Integer> intSettingsValue = name -> save.getIntSettingsValue(name);

        setSavedValuesToMap(nameListBool, boolParameters, boolSettingsValue);
        setSavedValuesToMap(nameListInt, intParameters, intSettingsValue);
    }

    private <Type> void setSavedValuesToMap(List<String> nameList, Map<String, Parameter<Type>> parameters,
                                            Function<String, Type> saveGetSettingsValue)
    {
        for(String name : nameList)
        {
            Type value = saveGetSettingsValue.apply(name);
            Parameter<Type> parameter = parameters.get(name);
            if (value == null)
            {
                parameter.setDefaultValue();
            }
            else
            {
                parameter.setValue(value);
            }
        }
    }

    public <Type> void set(String parameterName, Type value)
    {
        Parameter<Type> parameter = null;
        if (boolParameters.containsKey(parameterName))
        {
            parameter = (Parameter<Type>)boolParameters.get(parameterName);
        }
        else if (intParameters.containsKey(parameterName))
        {
            parameter = (Parameter<Type>)intParameters.get(parameterName);
        }

        if (parameter != null)
        {
            parameter.setValue(value);
            updateParameterInSave(parameter);
        }
    }

    public <Type> Type get(String parameterName)
    {
        Parameter<Type> parameter = null;
        if (boolParameters.containsKey(parameterName))
        {
            parameter = (Parameter<Type>)boolParameters.get(parameterName);
        }
        else if (intParameters.containsKey(parameterName))
        {
            parameter = (Parameter<Type>)intParameters.get(parameterName);
        }

        Type value = null;
        if(parameter != null)
        {
            value = parameter.getValue();
        }

        return value;
    }

    public <Type> void get(String parameterName, Type out)
    {
        out = get(parameterName);
    }

    private void updateParameterInSave(Parameter<?> parameter)
    {
        save.setSettingsValue(parameter.getName(), parameter.getValue());
    }
}
