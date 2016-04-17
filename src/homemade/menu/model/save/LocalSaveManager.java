package homemade.menu.model.save;

import java.lang.reflect.Type;

/**
 * Created by Marid on 16.04.2016.
 */
public class LocalSaveManager implements SettingsSave
{
    private static final String WRONG_PARAMETER = "Wrong parameter: ";

    private Save save = null;

    private LocalSaveManager () {}

    public LocalSaveManager(String pathToSave)
    {
        save = new Save(pathToSave);
    }

    boolean isValid()
    {
        return save != null;
    }

    public Integer getIntSettingsValue(String parameterName)
    {
        return getValue(Block.SETTINGS, parameterName, Integer.TYPE);
    }

    public Boolean getBoolSettingsValue(String parameterName)
    {
        return getValue(Block.SETTINGS, parameterName, Boolean.TYPE);
    }

    public <T> T getSettingsValue(String parameterName, Type type)
    {
        return getValue(Block.SETTINGS, parameterName, type);
    }

    public void setSettingsValue(String parameterName, Object value)
    {
        String stringValue = value.toString();
        save.setParameterValue(Block.SETTINGS, parameterName, stringValue);
    }

    public void setValue(String blockName, String parameterName, Object value)
    {
        String stringValue = value.toString();
        save.setParameterValue(blockName, parameterName, stringValue);
    }

    private <T> T getValue(String blockName, String parameterName, Type type)
    {
        String value = save.getParameterValue(blockName, parameterName);
        T parameterValue = convertStrValue(value, type);

        return parameterValue;
    }

    private <T> T convertStrValue(String value, Type type)
    {
        T convertedValue = null;
        if(value != null)
        {
            if (type.getTypeName().equals(Boolean.TYPE.getTypeName()))
            {
                convertedValue = (T)Boolean.valueOf(value);
            }
            else if (type.getTypeName().equals(Integer.TYPE.getTypeName()))
            {
                convertedValue = (T)Integer.valueOf(value);
            }
        }
        return convertedValue;
    }

    public final class Block
    {
        public static final String SETTINGS = "settings";
        public static final String RECORDS = "records";
    }
}
