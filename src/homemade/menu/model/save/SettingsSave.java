package homemade.menu.model.save;

public interface SettingsSave {
    Integer getIntSettingsValue(String parameterName);

    Boolean getBoolSettingsValue(String parameterName);

    <Type> Type getSettingsValue(String parameterName, Class<Type> type);

    void setSettingsValue(String parameterName, Object value);
}
