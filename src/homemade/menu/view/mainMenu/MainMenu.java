package homemade.menu.view.mainMenu;

import homemade.menu.controller.ButtonActionListener;
import homemade.menu.controller.MenuManager.MenuCode;
import homemade.menu.view.MenuPanel;

import javax.swing.*;
import java.util.Map;

public class MainMenu extends MenuPanel
{

    public MainMenu(Map<MenuCode, String> menuNames, ButtonActionListener actionListener)
    {
        super();

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        for (Map.Entry<MenuCode, String> entry : menuNames.entrySet())
        {
            String nameButton = entry.getValue();
            JButton button = MainMenuButtonFactory.createButton(nameButton, actionListener);
            button.setActionCommand(String.valueOf(entry.getKey().ordinal()));
            add(button);
        }
    }
}
