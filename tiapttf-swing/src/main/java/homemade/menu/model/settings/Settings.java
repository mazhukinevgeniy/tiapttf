package homemade.menu.model.settings;

import homemade.game.model.GameSettings;
import homemade.menu.model.save.SettingsSave;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Settings {
    //1) add nameMap new parameter
    public enum Code {
        ANIMATED_LINKS,
        IS_REALTIME,
        SIMULTANEOUS_SPAWN,
        SPAWN_PERIOD,
        COMBO_LENGTH,
        MAX_BLOCK_VALUE
    }

    private static final Map<Code, String> nameMap = new HashMap<>();

    static {
        nameMap.put(Code.ANIMATED_LINKS, "Animated links");
        nameMap.put(Code.IS_REALTIME, "Real time");
        nameMap.put(Code.SIMULTANEOUS_SPAWN, "Simultaneous spawn");
        nameMap.put(Code.SPAWN_PERIOD, "Max spawn period (ms)");
        nameMap.put(Code.COMBO_LENGTH, "Min combo length");
        nameMap.put(Code.MAX_BLOCK_VALUE, "Max block value (9/27/81)");
    }

    //3) state default value and Range/Enum valid values
    private static final Map<Code, ValueChecker<?>> checkers = new HashMap<>();

    static {
        checkers.put(Code.ANIMATED_LINKS, new InSetChecker<>(false, true, false));
        checkers.put(Code.IS_REALTIME, new InSetChecker<>(true, true, false));
        checkers.put(Code.SIMULTANEOUS_SPAWN, new RangeChecker<>(3, 1, 20));
        checkers.put(Code.SPAWN_PERIOD, new RangeChecker<>(2000, 100, 1000 * 60));
        checkers.put(Code.COMBO_LENGTH, new RangeChecker<>(5, 3, 9));
        checkers.put(Code.MAX_BLOCK_VALUE, new InSetChecker<>(9, 9, 27, 81));
    }
    //4) everything should work (=

    private Map<Code, ValidParameter<?>> parameters = new HashMap<>();

    private Presets presets = new Presets();
    private Presets.Mode currentMode;
    private Presets.Difficulty currentDifficutly;

    private SettingsSave save;

    public Settings(SettingsSave save) {
        this.save = save;

        prepareParameters(parameters);
        setSavedValuesToMap(parameters);
        updateAllParametersInSave();
    }

    private void prepareParameters(Map<Code, ValidParameter<?>> parameters) {
        for (Code code : Code.values()) {
            ValueChecker<?> checker = checkers.get(code);
            ValidParameter<?> parameter = new ValidParameter<>(checker, code);
            parameters.put(code, parameter);
        }
    }

    //TODO maybe better storage parameters in save also using parameter Codes
    private void setSavedValuesToMap(Map<Code, ValidParameter<?>> parameters) {
        for (Map.Entry<Code, ValidParameter<?>> param : parameters.entrySet()) {
            ValidParameter<?> parameter = param.getValue();

            Object savedValue = save.getSettingsValue(nameMap.get(param.getKey()), parameter.getType());

            if (savedValue == null) {
                parameter.setDefaultValue();
            } else {
                parameter.setValueWithCast(save.getSettingsValue(nameMap.get(param.getKey()), parameter.getType()));
            }
        }
    }

    public <Type> void set(Code parameterCode, Type value) {
        if (parameters.containsKey(parameterCode)) {
            ValidParameter<Type> parameter = (ValidParameter<Type>) parameters.get(parameterCode);
            if (parameter != null) {
                parameter.setValue(value);
                updateParameterInSave(parameter);
            }
        }
    }

    //TODO maybe better storage parameters in save also using parameter Codes
    private void updateParameterInSave(ValidParameter<?> parameter) {
        save.setSettingsValue(nameMap.get(parameter.getCode()), parameter.getValue());
    }

    public <Type> Type get(Code parameterCode) {
        Type value = null;
        if (parameters.containsKey(parameterCode)) {
            ValidParameter<Type> parameter = (ValidParameter<Type>) parameters.get(parameterCode);
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

    public Map<Code, String> getParameterNames() {
        return nameMap;
    }

    public List<Parameter<?>> getAllParameters() {
        List<Parameter<?>> listParameters = new ArrayList<>();
        for (Code code : Code.values()) {
            listParameters.add(parameters.get(code));
        }

        return listParameters;
    }

    public void setPresetParameters(Presets.Mode mode, Presets.Difficulty difficulty) {
        List<Parameter<?>> parameters = presets.getPresets(mode, difficulty);
        parameters.add(new Parameter<>(Code.ANIMATED_LINKS, false));
        setParameters(parameters);

        currentMode = mode;
        currentDifficutly = difficulty;
    }

    public void setParameters(List<Parameter<?>> parameters) {
        for (Parameter<?> param : parameters) {
            Code code = param.getCode();
            if (code != null) {
                set(code, param.getValue());
            }
        }
    }

    private void updateAllParametersInSave() {
        for (Code code : Code.values()) {
            ValidParameter<?> parameter = parameters.get(code);
            updateParameterInSave(parameter);
        }
    }

    public GameSettings generateSettings() {
        return new GameSettings(
                get(Settings.Code.IS_REALTIME) ? GameSettings.GameMode.REAL_TIME : GameSettings.GameMode.TURN_BASED,
                get(Settings.Code.COMBO_LENGTH),
                get(Settings.Code.SIMULTANEOUS_SPAWN),
                get(Settings.Code.SPAWN_PERIOD),
                get(Settings.Code.MAX_BLOCK_VALUE)
        );
    }
}
