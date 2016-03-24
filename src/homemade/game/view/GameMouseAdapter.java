package homemade.game.view;

import homemade.game.controller.GameController;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by user3 on 24.03.2016.
 */
class GameMouseAdapter extends MouseAdapter
{
    private GameController controller;

    GameMouseAdapter(GameController controller)
    {
        this.controller = controller;
    }


    @Override
    public void mouseReleased(MouseEvent e)
    {
        this.controller.handleMouseRelease(e.getX(), e.getY());
    }

    //TODO: allow multiple block selection
}
