package homemade.menu.controller;

import homemade.game.GameSettings;
import homemade.game.controller.GameController;
import homemade.menu.controller.settings.SettingsManager;
import homemade.menu.model.settings.Settings;
import homemade.menu.view.MenuPanel;
import homemade.menu.view.Window;
import homemade.menu.view.mainMenu.MainMenu;
import javafx.util.Pair;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Marid on 02.04.2016.
 */
public class MenuManager implements HandlerButtons
{
    public static final class Menu
    {
        public static final Pair<Integer, String> GAME = new Pair<> (0, "Game");
        public static final Pair<Integer, String> SETTINGS = new Pair<> (1, "Settings");
        public static final Pair<Integer, String> RECORDS = new Pair<> (2, "Records");
        public static final Pair<Integer, String> MAIN_MENU = new Pair<> (3, "Main menu");
    }

    private Window window;
    private Settings settings;

    private MenuPanel currentMenu;
    private Map<Integer, MenuPanel> menus = new HashMap<>();

    private GameController gameController;
    private ButtonActionListener actionListener;

    public MenuManager(Window window, Settings settings)
    {
        this.window = window;
        this.settings = settings;

        actionListener = new ButtonActionListener<>(this);

        Map<Integer, String> menuNames = createMenuNamesMap();
        MenuPanel mainMenu = new MainMenu(menuNames, actionListener);

        SettingsManager settingsManager = new SettingsManager(this, settings);
        MenuPanel settingsMenu = settingsManager.getSettingsMenu();

        menus.put(Menu.MAIN_MENU.getKey(), mainMenu);
        menus.put(Menu.SETTINGS.getKey(), settingsMenu);

        setCurrentMenu(Menu.MAIN_MENU.getKey());
        window.add(currentMenu);
    }

    private Map<Integer, String> createMenuNamesMap()
    {
        Map<Integer, String> menuNames = new HashMap<>();
        menuNames.put(Menu.GAME.getKey(), Menu.GAME.getValue());
        menuNames.put(Menu.SETTINGS.getKey(), Menu.SETTINGS.getValue());
        menuNames.put(Menu.RECORDS.getKey(), Menu.RECORDS.getValue());

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
        if (codeButton == Menu.GAME.getKey())
        {
            startGame();
        }
        else if (codeButton == Menu.SETTINGS.getKey())
        {
            toggleToMenu(Menu.SETTINGS.getKey());
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
