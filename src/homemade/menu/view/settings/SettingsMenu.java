package homemade.menu.view.settings;

import homemade.menu.controller.MenuManager;
import homemade.menu.model.settings.Settings;
import homemade.menu.view.Menu;
import homemade.menu.view.Window;

/**
 * Created by Marid on 18.04.2016.
 */
public class SettingsMenu extends Menu
{
    private Settings settings;

    private SettingsMenu() {}

    public SettingsMenu(MenuManager manager, Window window, Settings settings)
    {
        super(manager, window);

        this.settings = settings;
    }
}
