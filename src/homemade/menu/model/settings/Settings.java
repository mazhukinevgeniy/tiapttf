package homemade.menu.model.settings;

import homemade.menu.model.save.SettingsSave;
import javafx.util.Pair;

import java.lang.reflect.Type;
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
    public static final class Name
    {
        public static final String isRealTime = "Real time";
        public static final String simultaneousSpawn = "Simultaneous spawn";
        public static final String spawnPeriod = "Max spawn period (ms)";
        public static final String comboLength = "Min combo length";
    }

    //2) add parameter to eligible list with names
    private List<String> nameListBool = Arrays.asList(Name.isRealTime);

    private List<String> nameListInt = Arrays.asList(Name.simultaneousSpawn,
                                                     Name.spawnPeriod,
                                                     Name.comboLength);

    //3) state default value and Range/Enum valid values
    private static Map<String, ValueChecker<?>> checkers = new HashMap<>();

    static
    {
        checkers.put(Name.isRealTime, new InSetChecker<>(true, true, false));
        checkers.put(Name.simultaneousSpawn, new RangeChecker<>(3, 1, 20));
        checkers.put(Name.spawnPeriod, new RangeChecker<>(2000, 100, 1000 * 60));
        checkers.put(Name.comboLength, new RangeChecker<>(5, 3, 9));
    }
    //4) everything should work (=

    private Map<String, Parameter<Boolean>> boolParameters = new HashMap<>();
    private Map<String, Parameter<Integer>> intParameters = new HashMap<>();

    private SettingsSave save;

    private Settings() {}

    public Settings(SettingsSave save)
    {
        this.save = save;

        prepareParameters(nameListBool, boolParameters);
        prepareParameters(nameListInt, intParameters);

        initializeValues();
        updateAllParametersInSave();
    }

    private <Type> void prepareParameters(List<String> settingNames, Map<String, Parameter<Type>> parametersMap)
    {
        for (String name : settingNames)
        {
            Parameter<Type> parameter = new Parameter<>(name);
            parametersMap.put(name, parameter);

            ValueChecker<Type> checker = (ValueChecker<Type>)checkers.get(name);
            parameter.setValueChecker(checker);
        }
    }

    private void initializeValues()
    {
        Function<String, Boolean> boolSettingsValue = name -> save.getBoolSettingsValue(name);
        Function<String, Integer> intSettingsValue = name -> save.getIntSettingsValue(name);

        setSavedValuesToMap(boolParameters, boolSettingsValue);
        setSavedValuesToMap(intParameters, intSettingsValue);
    }

    private <Type> void setSavedValuesToMap(Map<String, Parameter<Type>> parameters,
                                            Function<String, Type> saveGetSettingsValue)
    {
        for(Map.Entry<String, Parameter<Type>> param : parameters.entrySet())
        {
            Type savedValue = saveGetSettingsValue.apply(param.getKey());
            Parameter<Type> parameter = param.getValue();

            if (savedValue == null)
            {
                parameter.setDefaultValue();
            }
            else
            {
                parameter.setValue(savedValue);
            }
        }
    }

    //parameterName take from Settings.Name
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

    private void updateParameterInSave(Parameter<?> parameter)
    {
        save.setSettingsValue(parameter.getName(), parameter.getValue());
    }

    //parameterName take from Settings.Name
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

    //parameterName take from Settings.Name
    public <Type> void get(String parameterName, Type out)
    {
        out = get(parameterName);
    }

    //parameterName take from Settings.Name
    public Type getType(String parameterName)
    {
        Type type = null;
        if (boolParameters.containsKey(parameterName))
        {
            type = Boolean.TYPE;
        }
        else if (intParameters.containsKey(parameterName))
        {
            type = Integer.TYPE;
        }

        return type;
    }

    public Map<String, Type> getParameterNamesMap()
    {
        Map<String, Type> namesMap = new HashMap<>();
        for (String name : nameListBool)
        {
            namesMap.put(name, Boolean.TYPE);
        }
        for (String name : nameListInt)
        {
            namesMap.put(name, Integer.TYPE);
        }

        return namesMap;
    }

    public Map<String, Pair<Type, ?>> getAllParameters()
    {
        Map<String, Pair<Type, ?>> parameters = new HashMap<>();
        for (String name : nameListBool)
        {
            Boolean value = boolParameters.get(name).getValue();
            parameters.put(name, new Pair<>(Boolean.TYPE, value));
        }
        for (String name : nameListInt)
        {
            Integer value = intParameters.get(name).getValue();
            parameters.put(name, new Pair<>(Integer.TYPE, value));
        }

        return parameters;
    }

    public void setParameters(Map<String, Pair<Type, ?>> parameters)
    {
        for (String name : nameListBool)
        {
            if(parameters.containsKey(name))
            {
                Boolean value = (Boolean) parameters.get(name).getValue();
                set(name, value);
            }
        }
        for (String name : nameListInt)
        {
            if (parameters.containsKey(name))
            {
                Integer value = (Integer) parameters.get(name).getValue();
                set(name, value);
            }
        }
    }

    public void setDefaultSettings()
    {
        resetParameters(intParameters);
        resetParameters(boolParameters);

        updateAllParametersInSave();
    }

    private <Type> void resetParameters(Map<String, Parameter<Type>> parameters)
    {
        for(Map.Entry<String, Parameter<Type>> param : parameters.entrySet())
        {
            Parameter<Type> parameter = param.getValue();
            parameter.setDefaultValue();
        }
    }


    private void updateAllParametersInSave()
    {
        Parameter<?> parameter;
        for (String name : nameListBool)
        {
            parameter = boolParameters.get(name);
            updateParameterInSave(parameter);
        }
        for (String name : nameListInt)
        {
            parameter = intParameters.get(name);
            updateParameterInSave(parameter);
        }
    }
}
