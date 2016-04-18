package homemade.menu.controller;

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
public class MenuManager
{
    private final class NameMenu
    {
        public static final String GAME = "Game";
        public static final String SETTINGS = "Settings";
        public static final String MAIN_MENU = "Main menu";
    }

    private final class CodeMenu
    {
        public static final int GAME = 0;
        public static final int SETTINGS = 1;
        public static final int MAIN_MENU = 2;
    }

    Window window;

    Menu currentMenu;
    Map<Integer, Menu> menus = new HashMap<>();

    GameController gameController;
    ButtonActionListener actionListener;

    public MenuManager(Window window, Settings settings)
    {
        this.window = window;
        this.actionListener = new ButtonActionListener(this);

        Map<Integer, String> menuNames = new HashMap<>();
        menuNames.put(CodeMenu.GAME, NameMenu.GAME);
        menuNames.put(CodeMenu.SETTINGS, NameMenu.SETTINGS);
        menuNames.put(CodeMenu.MAIN_MENU, NameMenu.MAIN_MENU);

        Menu mainMenu = new MainMenu(menuNames, actionListener);

        SettingsManager settingsManager = new SettingsManager(settings);
        Menu settingsMenu = settingsManager.getSettingsMenu();

        menus.put(CodeMenu.MAIN_MENU, mainMenu);
        menus.put(CodeMenu.SETTINGS, settingsMenu);

        setCurrentMenu(CodeMenu.MAIN_MENU);
        window.add(currentMenu);
    }

    private void setCurrentMenu(int codeMenu)
    {
        currentMenu = menus.get(codeMenu);
    }

    public ButtonActionListener getActionListener()
    {
        return actionListener;
    }

    public void processClickButton(int keyButton)
    {
        if (keyButton == CodeMenu.GAME)
        {
            startGame();
        }
        else if (keyButton == CodeMenu.SETTINGS)
        {
            toggleToMenu(CodeMenu.SETTINGS);
        }
    }

    private void startGame()
    {
        gameController = new GameController(window);
        currentMenu.setVisible(false);
    }

    private void toggleToMenu(int codeMenu)
    {
        window.remove(currentMenu);
        setCurrentMenu(codeMenu);
        window.add(currentMenu);
        currentMenu.updateUI();
    }
}
