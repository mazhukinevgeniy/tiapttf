package homemade.menu.model.save;

public interface SettingsSave {
    Integer getIntSettingsValue(String parameterName);

    Boolean getBoolSettingsValue(String parameterName);

    void setSettingsValue(String parameterName, Object value);
}
