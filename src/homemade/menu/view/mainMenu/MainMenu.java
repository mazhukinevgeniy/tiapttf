package homemade.menu.view.mainMenu;

import homemade.menu.controller.MenuManager;
import homemade.menu.view.Menu;
import homemade.menu.view.Window;

import javax.swing.*;
import java.util.Map;
import java.util.Set;

/**
 * Created by Marid on 02.04.2016.
 */
public class MainMenu extends Menu
{
    private MenuManager manager;

    public MainMenu(MenuManager manager, Window window, Map<Integer, String> map)
    {
        super();
        this.manager = manager;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        Set<Integer> keys = map.keySet();

        for (int key : keys)
        {
            String nameButton = map.get(key);
            JButton button = MainMenuButtonFactory.createButton(nameButton, manager.getActionListener());
            button.setActionCommand(String.valueOf(key));
            add(button);
        }

        setVisible(false);
        window.add(this);
    }
}
