package homemade.game.controller;

import homemade.game.CellCode;
import homemade.game.Direction;
import homemade.game.Effect;
import homemade.game.Game;
import homemade.game.model.GameModel;
import homemade.game.view.GameView;
import homemade.utils.timer.QuickTimer;
import homemade.utils.timer.TimerTaskPerformer;

import java.awt.*;

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

    private QuickTimer timer;

    public GameController(Frame mainFrame)
    {
        frame = mainFrame;

        CellCode.initializeCellCodes();

        model = new GameModel(this);

        selectionManager = new SelectionManager(this);
        keyboard = new GameKeyboard(this);

        view = new GameView(this, mainFrame);

        long period = 1000 / Game.KEY_INPUT_CAP_PER_SECOND;
        timer = new QuickTimer(new TimedKeyboardInput(selectionManager, keyboard), period);
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

        view.getEffectManager().displayEffect(effect, atCell);
    }

    public void viewTimerUpdated()
    {
        this.view.renderNextFrame(model.copyGameState(), selectionManager.getSelectionState());
    }

    public void requestPauseToggle()
    {
        model.requestPauseToggle();
    }

    private static class TimedKeyboardInput implements TimerTaskPerformer
    {
        private SelectionManager selectionManager;
        private GameKeyboard keyboard;

        TimedKeyboardInput(SelectionManager selectionManager, GameKeyboard keyboard)
        {
            this.selectionManager = selectionManager;
            this.keyboard = keyboard;
        }

        @Override
        public void handleTimerTask()
        {
            Direction direction = keyboard.keyCodeToDirection(keyboard.extractKey());

            if (direction != null)
                selectionManager.tryToMoveSelectionIn(direction);
        }
    }
}
