package homemade.game.controller;

import homemade.game.CellCode;
import homemade.game.Direction;
import homemade.game.Effect;
import homemade.game.Game;
import homemade.game.model.GameModel;
import homemade.game.view.GameView;

import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by user3 on 23.03.2016.
 */
public class GameController implements ScoreHandler, BlockRemovalHandler
{
    private Frame frame;

    GameModel model;
    private GameView view;

    private SelectionManager selectionManager;
    private GameKeyboard keyboard;

    public GameController(Frame mainFrame)
    {
        frame = mainFrame;

        CellCode.initializeCellCodes();

        model = new GameModel(this);

        selectionManager = new SelectionManager(this);
        keyboard = new GameKeyboard(this);

        view = new GameView(this, mainFrame);

        Timer timer = new Timer();
        long delay = 0;
        long period = 1000 / Game.KEY_INPUT_CAP_PER_SECOND;
        timer.schedule(new ControllerTimerTask(this), delay, period);
    }

    public MouseInputHandler mouseInputHandler() { return selectionManager; }
    public KeyboardInputHandler keyboardInputHandler() { return keyboard; }

    @Override
    public void scoreUpdated(int score)
    {
        frame.setTitle("score: " + String.valueOf(score));
    }

    public void blockRemoved(CellCode atCell)
    {
        Effect effect = Effect.FADING_BLOCK;

        view.getEffectManager().displayEffect(effect, atCell, effect.getFullDuration());
    }

    public void viewTimerUpdated()
    {
        this.view.renderNextFrame(model.copyGameState(), selectionManager.getSelectionState());
    }

    void controllerTimerUpdated()
    {
        Direction direction = keyboard.keyCodeToDirection(keyboard.extractKey());

        if (direction != null)
            selectionManager.tryToMoveSelectionIn(direction);
    }

    private class ControllerTimerTask extends TimerTask
    {
        private GameController controller;

        ControllerTimerTask(GameController controller)
        {
            this.controller = controller;
        }

        @Override
        public void run()
        {
            this.controller.controllerTimerUpdated();
        }
    }
    //TODO: think about this: now everyone has a timer, is it right?
}
