package homemade.menu.model.save;

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
        return getIntValue(Block.SETTINGS, parameterName);
    }

    public Boolean getBoolSettingsValue(String parameterName)
    {
        return getBoolValue(Block.SETTINGS, parameterName);
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

    //TODO refactoring
    private Integer getIntValue(String blockName, String parameterName)
    {
        Integer parameterValue = null;
        String value = save.getParameterValue(blockName, parameterName);

        if(value != null)
        {
            parameterValue = Integer.valueOf(value);
        }

        return parameterValue;
    }

    private Boolean getBoolValue(String blockName, String parameterName)
    {
        Boolean parameterValue = null;
        String value = save.getParameterValue(blockName, parameterName);

        if(value != null)
        {
            parameterValue = Boolean.valueOf(value);
        }

        return parameterValue;
    }

    public final class Block
    {
        public static final String SETTINGS = "settings";
        public static final String RECORDS = "records";
    }
}
