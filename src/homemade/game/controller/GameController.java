package homemade.game.controller;

import homemade.game.Effect;
import homemade.game.GameState;
import homemade.game.fieldstructure.CellCode;
import homemade.game.fieldstructure.Direction;
import homemade.game.fieldstructure.FieldStructure;
import homemade.game.model.GameModel;
import homemade.game.model.spawn.SpawnManager;
import homemade.game.view.GameView;
import homemade.utils.timer.QuickTimer;
import homemade.utils.timer.TimerTaskPerformer;

import java.awt.*;

/**
 * Created by user3 on 23.03.2016.
 */
public class GameController implements ScoreHandler, BlockRemovalHandler
{
    private static final int KEY_INPUT_CAP_PER_SECOND = 8;

    private Frame frame;

    private GameModel model;
    private GameView view;

    private SelectionManager selectionManager;
    private GameKeyboard keyboard;

    private FieldStructure structure;

    private QuickTimer timer;

    public GameController(Frame mainFrame)
    {
        frame = mainFrame;

        structure = new FieldStructure();

        model = new GameModel(this);

        selectionManager = new SelectionManager(this);
        keyboard = new GameKeyboard(this);

        view = new GameView(this, mainFrame);

        long period = 1000 / KEY_INPUT_CAP_PER_SECOND;
        timer = new QuickTimer(new TimedKeyboardInput(), period);
    }

    public MouseInputHandler mouseInputHandler() { return selectionManager; }
    public KeyboardInputHandler keyboardInputHandler() { return keyboard; }
    public FieldStructure fieldStructure() { return structure; }

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
        model.getPauseToggler().toggleSpawnPause();
    }

    GameState copyGameState()
    {
        return model.copyGameState();
    }

    void requestBlockMove(CellCode from, CellCode to)
    {
        model.getMoveRequestHandler().requestBlockMove(from, to);
    }
    //TODO: shall we move such methods out of GameController? make a class for doing simple things with current model

    public void gameOver()
    {
        timer.setPeriod(1000);

        //TODO: render randomly destroyed field, then restart
    }

    private class TimedKeyboardInput implements TimerTaskPerformer
    {
        @Override
        public void handleTimerTask()
        {
            Direction direction = keyboard.keyCodeToDirection(keyboard.extractKey());

            if (direction != null)
                selectionManager.tryToMoveSelectionIn(direction);
        }
    }
}
