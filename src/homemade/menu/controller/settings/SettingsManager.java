package homemade.menu.controller.settings;

import homemade.menu.model.settings.Settings;
import homemade.menu.view.Menu;
import homemade.menu.view.settings.SettingsMenu;
import javafx.util.Pair;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * Created by Marid on 18.04.2016.
 */
public class SettingsManager
{
    SettingsMenu settingsMenu;
    Settings settings;
    Map<String, Pair<Type, ?>> parameters;

    private SettingsManager() {}

    public SettingsManager(Settings settings)
    {
        this.settings = settings;
        parameters = settings.getAllParameters();
        settingsMenu = new SettingsMenu(parameters);
    }

    public Menu getSettingsMenu()
    {
        return this.settingsMenu;
    }
}
