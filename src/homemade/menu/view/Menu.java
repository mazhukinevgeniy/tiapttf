package homemade.menu.view;

import homemade.menu.controller.MenuManager;

import javax.swing.*;

/**
 * Created by Marid on 02.04.2016.
 */
public class Menu extends JPanel
{
    protected MenuManager manager;

    public Menu() {}

    public Menu(MenuManager manager, Window window)
    {
        super();
        this.manager = manager;

        setVisible(false);
        window.add(this);
    }
}
