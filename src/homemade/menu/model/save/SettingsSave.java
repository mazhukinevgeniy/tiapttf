package homemade.menu.model.save;

public interface SettingsSave {
    <Type> Type getSettingsValue(String parameterName, Class<Type> type);

    void setSettingsValue(String parameterName, Object value);
}
