package homemade.menu.model.save;

/**
 * Created by Marid on 16.04.2016.
 */
public interface SettingsSave
{
    Integer getIntSettingsValue(String parameterName);
    Boolean getBoolSettingsValue(String parameterName);
    void setSettingsValue(String parameterName, Object value);

    void setValue(String blockName, String parameterName, Object value);
}
