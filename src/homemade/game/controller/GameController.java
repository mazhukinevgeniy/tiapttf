package homemade.game.controller;

import homemade.game.Effect;
import homemade.game.GameState;
import homemade.game.fieldstructure.CellCode;
import homemade.game.fieldstructure.Direction;
import homemade.game.fieldstructure.FieldStructure;
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
    private static final int TARGET_FPS = 60;
    private static final int FRAMES_BEFORE_KEY_INPUT = 6;

    private int frameCounter;

    private Frame frame;

    private GameModel model;
    private GameView view;

    private SelectionManager selectionManager;
    private GameKeyboard keyboard;

    private QuickTimer timer;

    public GameController(Frame mainFrame)
    {
        frame = mainFrame;

        FieldStructure structure = new FieldStructure();

        model = new GameModel(this, structure);

        selectionManager = new SelectionManager(this, structure);
        keyboard = new GameKeyboard(this);

        ViewListener viewListener = new ViewListener(selectionManager, keyboard);

        view = new GameView(structure, viewListener, mainFrame);

        long period = 1000 / TARGET_FPS;
        frameCounter = 0;
        timer = new QuickTimer(new ControllerTimerTask(), period);
    }

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

    void requestPauseToggle()
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

    private class ControllerTimerTask implements TimerTaskPerformer
    {
        @Override
        public void handleTimerTask()
        {
            if (frameCounter == FRAMES_BEFORE_KEY_INPUT)
            {
                frameCounter = 0;
                Direction direction = keyboard.keyCodeToDirection(keyboard.extractKey());

                if (direction != null)
                    selectionManager.tryToMoveSelectionIn(direction);
            }
            else
                frameCounter++;

            view.renderNextFrame(model.copyGameState(), selectionManager.getSelectionState());
        }
    }
}
