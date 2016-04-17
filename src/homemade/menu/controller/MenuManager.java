package homemade.menu.controller;

import homemade.game.controller.GameController;
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
    private final class NameSubWindow
    {
        public static final String GAME = "Game";
        public static final String MAIN_MENU = "Main menu";
    }

    private final class CodeSubWindow
    {
        public static final int GAME = 0;
        public static final int MAIN_MENU = 1;
    }

    Window window;

    MainMenu mainMenu;
    Menu currentMenu;

    GameController gameController;
    ButtonActionListener actionListener;

    public MenuManager(Window window)
    {
        this.window = window;
        this.actionListener = new ButtonActionListener(this);

        Map<Integer, String> map = new HashMap<>();
        map.put(CodeSubWindow.GAME, NameSubWindow.GAME);
        map.put(CodeSubWindow.MAIN_MENU, NameSubWindow.MAIN_MENU);


        mainMenu = new MainMenu(this, window, map);

        currentMenu = mainMenu;
        currentMenu.setVisible(true);
    }

    private void startGame()
    {
        gameController = new GameController(window);
        currentMenu.setVisible(false);
    }

    public ButtonActionListener getActionListener()
    {
        return actionListener;
    }

    public void processClickButton(int keyButton)
    {
        if (keyButton == CodeSubWindow.GAME)
        {
            startGame();
        }
    }
}
