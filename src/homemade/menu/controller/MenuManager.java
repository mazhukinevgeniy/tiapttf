package homemade.menu.controller;

import homemade.game.GameSettings;
import homemade.game.controller.GameController;
import homemade.menu.controller.settings.SettingsManager;
import homemade.menu.model.settings.Settings;
import homemade.menu.view.Menu;
import homemade.menu.view.Window;
import homemade.menu.view.mainMenu.MainMenu;

import java.util.EnumMap;
import java.util.Map;

public class MenuManager implements HandlerButtons
{
    public enum MenuCode
    {
        GAME, SETTINGS, MAIN_MENU
    }

    private Window window;
    private Settings settings;

    private Menu currentMenu;
    private Map<MenuCode, Menu> menus;

    private GameController gameController;
    private ButtonActionListener actionListener;

    public MenuManager(Window window, Settings settings)
    {
        this.window = window;
        this.settings = settings;

        actionListener = new ButtonActionListener<>(this);

        Map<MenuCode, String> menuNames = createMenuNamesMap();
        Menu mainMenu = new MainMenu(menuNames, actionListener);

        SettingsManager settingsManager = new SettingsManager(this, settings);
        Menu settingsMenu = settingsManager.getSettingsMenu();

        menus = new EnumMap<>(MenuCode.class);
        menus.put(MenuCode.MAIN_MENU, mainMenu);
        menus.put(MenuCode.SETTINGS, settingsMenu);
        menus.put(MenuCode.GAME, new Menu());

        setCurrentMenu(MenuCode.MAIN_MENU);
        window.add(currentMenu);
    }

    private Map<MenuCode, String> createMenuNamesMap()
    {
        Map<MenuCode, String> menuNames = new EnumMap<>(MenuCode.class);
        menuNames.put(MenuCode.GAME, "Game");
        menuNames.put(MenuCode.SETTINGS, "Settings");

        return menuNames;
    }

    private void setCurrentMenu(MenuCode code)
    {
        currentMenu = menus.get(code);
    }

    //TODO: tell me what is this method for
    public ButtonActionListener<MenuManager> getActionListener()
    {
        return actionListener;
    }

    @Override
    public void handleButtonClick(int code)
    {
        MenuCode menuCode = MenuCode.values()[code];

        switchToMenu(menuCode);
    }

    public void switchToMenu(MenuCode code)
    {
        window.remove(currentMenu);
        setCurrentMenu(code);
        window.add(currentMenu);

        if (code == MenuCode.GAME)
        {
            currentMenu.removeAll();
            gameController = new GameController(window, currentMenu, new GameSettings(settings));
        }

        currentMenu.updateUI();
    }
}