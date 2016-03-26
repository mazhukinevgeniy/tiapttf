package homemade.game.controller;

import homemade.game.model.GameModel;
import homemade.game.view.GameView;

import java.awt.*;

/**
 * Created by user3 on 23.03.2016.
 */
public class GameController
{
    GameModel model;
    private GameView view;

    private SelectionManager selectionManager;

    public GameController(Frame mainFrame)
    {
        this.model = new GameModel();

        this.selectionManager = new SelectionManager(this);

        this.view = new GameView(this, mainFrame);
    }

    public MouseInputHandler mouseInputHandler() { return this.selectionManager; }

    public void viewTimerUpdated()
    {
        this.view.renderNextFrame(this.model.copyGameState(), this.selectionManager.getSelectionState());
    }


}
