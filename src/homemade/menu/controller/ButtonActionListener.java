package homemade.menu.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Marid on 02.04.2016.
 */
public class ButtonActionListener<Type extends HandlerButtons> implements ActionListener
{
    private Type handler;

    public ButtonActionListener(Type handler)
    {
        this.handler = handler;
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        handler.handleButtonClick(Integer.valueOf(e.getActionCommand()));
    }
}
