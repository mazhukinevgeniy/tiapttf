package homemade.game.view;

import homemade.game.controller.KeyboardInputHandler;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

class ControlledKeyListener implements KeyListener
{
    private KeyboardInputHandler handler;

    ControlledKeyListener(KeyboardInputHandler handler)
    {
        this.handler = handler;
    }

    @Override
    public void keyTyped(KeyEvent e)
    {

    }

    @Override
    public void keyPressed(KeyEvent e)
    {
        handler.keyPressed(e.getKeyCode());
    }

    @Override
    public void keyReleased(KeyEvent e)
    {
        handler.keyReleased(e.getKeyCode());
    }
}
