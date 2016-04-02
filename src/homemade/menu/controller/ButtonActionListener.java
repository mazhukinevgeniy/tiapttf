package homemade.menu.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Marid on 02.04.2016.
 */
public class ButtonActionListener implements ActionListener
{
    private MenuManager manager;

    public ButtonActionListener(MenuManager manager)
    {
        this.manager = manager;
    }

    public void actionPerformed(ActionEvent e) {
        manager.processClickButton(Integer.valueOf(e.getActionCommand()));
    }

}
