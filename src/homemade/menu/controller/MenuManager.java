package homemade.menu.controller;

import homemade.game.GameSettings;
import homemade.game.controller.GameController;
import homemade.menu.controller.settings.SettingsManager;
import homemade.menu.model.settings.Settings;
import homemade.menu.view.Menu;
import homemade.menu.view.Window;
import homemade.menu.view.mainMenu.MainMenu;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Marid on 02.04.2016.
 */
public class MenuManager implements HandlerButtons
{
    private final class NameMenu
    {
        private static final String GAME = "Game";
        private static final String SETTINGS = "Settings";
        private static final String MAIN_MENU = "Main menu";
    }

    public static final class CodeMenu
    {
        public static final int GAME = 0;
        public static final int SETTINGS = 1;
        public static final int MAIN_MENU = 2;
    }

    private Window window;
    private Settings settings;

    private Menu currentMenu;
    private Map<Integer, Menu> menus = new HashMap<>();

    private GameController gameController;
    private ButtonActionListener actionListener;

    public MenuManager(Window window, Settings settings)
    {
        this.window = window;
        this.settings = settings;

        actionListener = new ButtonActionListener<>(this);

        Map<Integer, String> menuNames = createMenuNamesMap();
        Menu mainMenu = new MainMenu(menuNames, actionListener);

        SettingsManager settingsManager = new SettingsManager(this, settings);
        Menu settingsMenu = settingsManager.getSettingsMenu();

        menus.put(CodeMenu.MAIN_MENU, mainMenu);
        menus.put(CodeMenu.SETTINGS, settingsMenu);

        setCurrentMenu(CodeMenu.MAIN_MENU);
        window.add(currentMenu);
    }

    private Map<Integer, String> createMenuNamesMap()
    {
        Map<Integer, String> menuNames = new HashMap<>();
        menuNames.put(CodeMenu.GAME, NameMenu.GAME);
        menuNames.put(CodeMenu.SETTINGS, NameMenu.SETTINGS);

        return menuNames;
    }

    private void setCurrentMenu(int codeMenu)
    {
        currentMenu = menus.get(codeMenu);
    }

    public ButtonActionListener<MenuManager> getActionListener()
    {
        return actionListener;
    }

    @Override
    public void handleButtonClick(int codeButton)
    {
        if (codeButton == CodeMenu.GAME)
        {
            startGame();
        }
        else if (codeButton == CodeMenu.SETTINGS)
        {
            toggleToMenu(CodeMenu.SETTINGS);
        }
    }

    private void startGame()
    {
        gameController = new GameController(window, new GameSettings(settings));
        currentMenu.setVisible(false);
    }

    public void toggleToMenu(int codeMenu)
    {
        window.remove(currentMenu);
        setCurrentMenu(codeMenu);
        window.add(currentMenu);
        currentMenu.updateUI();
    }
}
