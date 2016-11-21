package homemade.menu.model.settings;

import homemade.menu.model.save.SettingsSave;

import java.util.*;

public class Settings {
    //1) add name new parameter
    public static final class Name {
        public static final String animatedLinks = "Animated links";
        public static final String isRealTime = "Real time";
        public static final String simultaneousSpawn = "Simultaneous spawn";
        public static final String spawnPeriod = "Max spawn period (ms)";
        public static final String comboLength = "Min combo length";
        public static final String maxBlockValue = "Max block value (9/27/81)";
    }

    //2) add parameter to list with names
    private List<String> nameList = Arrays.asList(
            Name.isRealTime,
            Name.animatedLinks,
            Name.simultaneousSpawn,
            Name.spawnPeriod,
            Name.comboLength,
            Name.maxBlockValue);

    //3) state default value and Range/Enum valid values
    private static Map<String, ValueChecker<?>> checkers = new HashMap<>();

    static {
        checkers.put(Name.animatedLinks, new InSetChecker<>(false, true, false));
        checkers.put(Name.isRealTime, new InSetChecker<>(true, true, false));
        checkers.put(Name.simultaneousSpawn, new RangeChecker<>(3, 1, 20));
        checkers.put(Name.spawnPeriod, new RangeChecker<>(2000, 100, 1000 * 60));
        checkers.put(Name.comboLength, new RangeChecker<>(5, 3, 9));
        checkers.put(Name.maxBlockValue, new InSetChecker<>(9, 9, 27, 81));
    }
    //4) everything should work (=

    private Map<String, ValidParameter<?>> parameters = new HashMap<>();

    private Presets presets = new Presets();
    private Presets.Mode currentMode;
    private Presets.Difficulty currentDifficutly;

    private SettingsSave save;

    public Settings(SettingsSave save) {
        this.save = save;

        prepareParameters(nameList, parameters);

        initializeValues();
        updateAllParametersInSave();
    }

    private void prepareParameters(List<String> settingNames, Map<String, ValidParameter<?>> parameters) {
        for (String name : settingNames) {
            ValueChecker<?> checker = checkers.get(name);
            ValidParameter<?> parameter = new ValidParameter<>(checker, name);
            parameters.put(name, parameter);
        }
    }

    private void initializeValues() {
        setSavedValuesToMap(parameters);
    }

    private void setSavedValuesToMap(Map<String, ValidParameter<?>> parameters) {
        for (Map.Entry<String, ValidParameter<?>> param : parameters.entrySet()) {
            ValidParameter<?> parameter = param.getValue();

            Object savedValue = save.getSettingsValue(param.getKey(), parameter.getType());

            if (savedValue == null) {
                parameter.setDefaultValue();
            } else {
                parameter.setValueWithCast(save.getSettingsValue(param.getKey(), parameter.getType()));
            }
        }
    }

    public <Type> void set(String parameterName, Type value) {
        if (parameters.containsKey(parameterName)) {
            ValidParameter<Type> parameter = (ValidParameter<Type>) parameters.get(parameterName);
            if (parameter != null) {
                parameter.setValue(value);
                updateParameterInSave(parameter);
            }
        }
    }

    private void updateParameterInSave(ValidParameter<?> parameter) {
        save.setSettingsValue(parameter.getName(), parameter.getValue());
    }

    public <Type> Type get(String parameterName) {
        Type value = null;
        if (parameters.containsKey(parameterName)) {
            ValidParameter<Type> parameter = (ValidParameter<Type>) parameters.get(parameterName);
            if (parameter != null) {
                value = parameter.getValue();
            }
        }

        return value;
    }

    //parameterName take from Settings.Name
    public <Type> Class<Type> getType(String parameterName) {
        Class<Type> type = null;
        if (parameters.containsKey(parameterName)) {
            type = (Class<Type>) parameters.get(parameterName).getType();
        }
        return type;
    }

    public List<Parameter<?>> getAllParameters() {
        List<Parameter<?>> listParameters = new ArrayList<>();
        for (String name : nameList) {
            listParameters.add(parameters.get(name));
        }

        return listParameters;
    }

    public void setPresetParameters(Presets.Mode mode, Presets.Difficulty difficulty) {
        List<Parameter<?>> parameters = presets.getPresets(mode, difficulty);
        parameters.add(new Parameter<>(Settings.Name.animatedLinks, false));
        setParameters(parameters);

        currentMode = mode;
        currentDifficutly = difficulty;
    }

    public void setParameters(List<Parameter<?>> parameters) {
        for (Parameter<?> param : parameters) {
            String name = param.getName();
            if (nameList.contains(name)) {
                set(name, param.getValue());
            }
        }
    }

    public void setDefaultSettings() {
        resetParameters(parameters);

        updateAllParametersInSave();
    }

    private void resetParameters(Map<String, ValidParameter<?>> parameters) {
        for (Map.Entry<String, ValidParameter<?>> param : parameters.entrySet()) {
            ValidParameter<?> parameter = param.getValue();
            parameter.setDefaultValue();
        }
    }

    private void updateAllParametersInSave() {
        for (String name : nameList) {
            ValidParameter<?> parameter = parameters.get(name);
            updateParameterInSave(parameter);
        }
    }
}
