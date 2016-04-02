package homemade.menu.view;

import javax.swing.*;

/**
 * Created by Marid on 02.04.2016.
 */
public class MainMenu extends Menu
{
    public MainMenu()
    {
        super();

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        MainMenuButton gameButton = new MainMenuButton("Game");
        MainMenuButton someButton = new MainMenuButton("Some");

        add(gameButton);
        add(someButton);
    }
}
