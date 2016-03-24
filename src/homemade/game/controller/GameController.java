package homemade.game.controller;

import homemade.game.Game;
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

    private Selection selection;
    //TODO: add selection tracker which will proceed related updates from View

    public GameController(Frame mainFrame)
    {
        this.model = new GameModel();
        this.view = new GameView(this, mainFrame);

        this.selection = new Selection();
    }

    public void viewTimerUpdated()
    {
        this.view.renderNextFrame(this.model.getData(), this.selection);
    }

    public void handleMouseRelease(int canvasX, int canvasY)
    {
        int cellX = (canvasX - GameView.GridOffset) / (GameView.CellWidth + GameView.CellOffset);
        int cellY = (canvasY - GameView.GridOffset) / (GameView.CellWidth + GameView.CellOffset);

        if (this.model.getData()[cellX + Game.FIELD_WIDTH * cellY] > 0)
        {
            this.selection.active = true;
            this.selection.x = cellX;
            this.selection.y = cellY;
        }
        //TODO: could use simple CellCoordinates class for arguments and simple manipulations, comparisons etc
        //TODO: add ability to move blocks by clicking at empty nearby cells
        //TODO: ATM getData is rather expensive, redesign the access methods

        System.out.println("apparently, mouse released at " + cellX + ", " + cellY);
    }
}
