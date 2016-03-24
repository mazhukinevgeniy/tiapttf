package homemade.game.controller;

import homemade.game.model.GameModel;
import homemade.game.view.GameView;

import java.awt.*;

/**
 * Created by user3 on 23.03.2016.
 */
public class GameController
{
    private GameModel model;
    private GameView view;

    public GameController(Frame mainFrame)
    {
        this.model = new GameModel();
        this.view = new GameView(this, mainFrame);
    }

    public void viewTimerUpdated()
    {
        this.view.renderNextFrame(this.model.getData());
    }

    public void handleMouseRelease(int cellX, int cellY)
    {
        System.out.println("apparently, mouse released at " + cellX + ", " + cellY);
    }
}
