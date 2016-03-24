package homemade.game.view;

import homemade.game.controller.MouseInputHandler;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by user3 on 24.03.2016.
 */
class GameMouseAdapter extends MouseAdapter
{
    private MouseInputHandler inputHandler;

    GameMouseAdapter(MouseInputHandler inputHandler)
    {
        this.inputHandler = inputHandler;
    }


    @Override
    public void mouseReleased(MouseEvent e)
    {
        this.inputHandler.handleMouseRelease(e.getX(), e.getY());
    }

    //TODO: allow multiple block selection
}
