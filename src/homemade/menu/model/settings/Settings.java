package homemade.menu.model.settings;

import homemade.menu.model.save.SettingsSave;

import java.lang.reflect.Type;
import java.util.*;
import java.util.function.Function;

public class Settings
{
    //1) add name new parameter
    public static final class Name
    {
        public static final String animatedLinks = "Animated links";
        public static final String isRealTime = "Real time";
        public static final String simultaneousSpawn = "Simultaneous spawn";
        public static final String spawnPeriod = "Max spawn period (ms)";
        public static final String comboLength = "Min combo length";
    }

    //2) add parameter to eligible list with names
    private List<String> nameListBool = Arrays.asList(Name.isRealTime, Name.animatedLinks);

    private List<String> nameListInt = Arrays.asList(Name.simultaneousSpawn,
                                                     Name.spawnPeriod,
                                                     Name.comboLength);

    //3) state default value and Range/Enum valid values
    private static Map<String, ValueChecker<?>> checkers = new HashMap<>();

    static
    {
        checkers.put(Name.animatedLinks, new InSetChecker<>(false, true, false));
        checkers.put(Name.isRealTime, new InSetChecker<>(true, true, false));
        checkers.put(Name.simultaneousSpawn, new RangeChecker<>(3, 1, 20));
        checkers.put(Name.spawnPeriod, new RangeChecker<>(2000, 100, 1000 * 60));
        checkers.put(Name.comboLength, new RangeChecker<>(5, 3, 9));
    }
    //4) everything should work (=

    private Map<String, ValidParameter<Boolean>> boolParameters = new HashMap<>();
    private Map<String, ValidParameter<Integer>> intParameters = new HashMap<>();

    private Presets presets = new Presets();
    private Presets.Mode currentMode;
    private Presets.Difficulty currentDifficutly;

    private SettingsSave save;

    public Settings(SettingsSave save)
    {
        this.save = save;

        prepareParameters(nameListBool, boolParameters, Boolean.class);
        prepareParameters(nameListInt, intParameters, Integer.class);

        initializeValues();
        updateAllParametersInSave();
    }

    private <Type> void prepareParameters(List<String> settingNames, Map<String, ValidParameter<Type>> parametersMap, Class<Type> type)
    {
        for (String name : settingNames)
        {
            ValidParameter<Type> parameter = new ValidParameter<>(type, name);
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

    private <Type> void setSavedValuesToMap(Map<String, ValidParameter<Type>> parameters,
                                            Function<String, Type> saveGetSettingsValue)
    {
        for(Map.Entry<String, ValidParameter<Type>> param : parameters.entrySet())
        {
            Type savedValue = saveGetSettingsValue.apply(param.getKey());
            ValidParameter<Type> parameter = param.getValue();

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
        ValidParameter<Type> parameter = null;
        if (boolParameters.containsKey(parameterName))
        {
            parameter = (ValidParameter<Type>)boolParameters.get(parameterName);
        }
        else if (intParameters.containsKey(parameterName))
        {
            parameter = (ValidParameter<Type>)intParameters.get(parameterName);
        }

        if (parameter != null)
        {
            parameter.setValue(value);
            updateParameterInSave(parameter);
        }
    }

    private void updateParameterInSave(ValidParameter<?> parameter)
    {
        save.setSettingsValue(parameter.getName(), parameter.getValue());
    }

    //parameterName take from Settings.Name
    public <Type> Type get(String parameterName)
    {
        ValidParameter<Type> parameter = null;
        if (boolParameters.containsKey(parameterName))
        {
            parameter = (ValidParameter<Type>)boolParameters.get(parameterName);
        }
        else if (intParameters.containsKey(parameterName))
        {
            parameter = (ValidParameter<Type>)intParameters.get(parameterName);
        }

        Type value = null;
        if(parameter != null)
        {
            value = parameter.getValue();
        }

        return value;
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

    public List<Parameter<?>> getAllParameters()
    {
        List<Parameter<?>> parameters = new ArrayList<>();
        for (String name : nameListBool)
        {
            parameters.add(boolParameters.get(name));
        }
        for (String name : nameListInt)
        {
            parameters.add(intParameters.get(name));
        }

        return parameters;
    }

    public void setPresetParameters(Presets.Mode mode, Presets.Difficulty difficulty)
    {
        List<Parameter<?>> parameters = presets.getPresets(mode, difficulty);
        parameters.add(new Parameter<>(Settings.Name.animatedLinks, false));
        setParameters(parameters);

        currentMode = mode;
        currentDifficutly = difficulty;
    }

    public void setParameters(List<Parameter<?>> parameters)
    {
        for (Parameter<?> param : parameters)
        {
            String name = param.getName();
            if (nameListBool.contains(name))
            {
                Boolean value = (Boolean) param.getValue();
                set(name, value);
            }
        }
        for (Parameter<?> param : parameters)
        {
            String name = param.getName();
            if (nameListInt.contains(name))
            {
                Integer value = (Integer) param.getValue();
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

    private <Type> void resetParameters(Map<String, ValidParameter<Type>> parameters)
    {
        for(Map.Entry<String, ValidParameter<Type>> param : parameters.entrySet())
        {
            ValidParameter<Type> parameter = param.getValue();
            parameter.setDefaultValue();
        }
    }


    private void updateAllParametersInSave()
    {
        ValidParameter<?> parameter;
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
