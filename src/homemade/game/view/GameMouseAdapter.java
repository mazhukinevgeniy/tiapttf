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
        int cellX = e.getX() / (GameView.CellWidth + GameView.CellOffset);
        int cellY = e.getY() / (GameView.CellWidth + GameView.CellOffset);

        this.controller.handleMouseRelease(cellX, cellY);
    }

    //TODO: allow multiple block selection
}
