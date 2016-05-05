package homemade.menu.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ButtonActionListener implements ActionListener
{
    private HandlerButtons handler;

    public ButtonActionListener(HandlerButtons handler)
    {
        this.handler = handler;
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        handler.handleButtonClick(Integer.valueOf(e.getActionCommand()));
    }
}
