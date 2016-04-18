package homemade.menu.view.mainMenu;

import homemade.menu.controller.MenuManager;
import homemade.menu.view.Menu;

import javax.swing.*;
import java.util.Map;
import java.util.Set;

/**
 * Created by Marid on 02.04.2016.
 */
public class MainMenu extends Menu
{
    private MainMenu() {}

    public MainMenu(MenuManager manager, Map<Integer, String> menus)
    {
        super(manager);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        Set<Integer> keys = menus.keySet();

        for (int key : keys)
        {
            String nameButton = menus.get(key);
            JButton button = MainMenuButtonFactory.createButton(nameButton, manager.getActionListener());
            button.setActionCommand(String.valueOf(key));
            add(button);
        }
    }
}
