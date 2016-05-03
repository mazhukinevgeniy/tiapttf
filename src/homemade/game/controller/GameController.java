package homemade.game.controller;

import homemade.game.Effect;
import homemade.game.GameSettings;
import homemade.game.fieldstructure.CellCode;
import homemade.game.fieldstructure.Direction;
import homemade.game.fieldstructure.FieldStructure;
import homemade.game.model.GameModel;
import homemade.game.view.GameView;
import homemade.utils.timer.QuickTimer;
import homemade.utils.timer.TimerTaskPerformer;

import java.awt.*;

public class GameController implements ScoreHandler, BlockRemovalHandler, MouseInputHandler
{
    private static final int TARGET_FPS = 60;

    private Frame frame;
    private FieldStructure structure;
    private GameSettings settings;

    private GameModel model;
    private GameView view;

    private GameKeyboard keyboard;

    private QuickTimer timer;

    public GameController(Frame mainFrame, Container container, GameSettings settings)
    {
        initialize(mainFrame, container, settings);
    }

    private synchronized void initialize(Frame mainFrame, Container container, GameSettings settings)
    {
        frame = mainFrame;
        this.settings = settings;

        structure = new FieldStructure();

        model = new GameModel(this, structure, settings);

        keyboard = new GameKeyboard(this);
        ViewListener viewListener = new ViewListener(this, keyboard);

        view = new GameView(structure, settings, viewListener, container);

        long period = 1000 / TARGET_FPS;
        timer = new QuickTimer(new ControllerTimerTask(), period);
    }

    @Override
    public synchronized void handleMouseRelease(int canvasX, int canvasY)
    {
        int gridX = canvasX - GameView.GRID_OFFSET_X;
        int gridY = canvasY - GameView.GRID_OFFSET_Y;

        int fullCell = GameView.CELL_WIDTH + GameView.CELL_OFFSET;

        int maxX = structure.getWidth() * fullCell;
        int maxY = structure.getHeight() * fullCell;

        if (gridX >= 0 && gridX < maxX && gridY >= 0 && gridY < maxY)
        {
            int cellX = gridX / (GameView.CELL_WIDTH + GameView.CELL_OFFSET);
            int cellY = gridY / (GameView.CELL_WIDTH + GameView.CELL_OFFSET);

            CellCode eventCell = structure.getCellCode(cellX, cellY);

            model.tryToActivateCell(eventCell);
        }
    }

    @Override
    public synchronized void scoreUpdated(int score)
    {
        frame.setTitle("score: " + String.valueOf(score));
    }

    public synchronized void blockRemoved(CellCode atCell)
    {
        Effect effect = Effect.FADING_BLOCK;

        view.getEffectManager().displayEffect(effect, atCell);
    }

    synchronized void requestPauseToggle()
    {
        model.toggleSpawnPause();
    }

    public synchronized void gameOver()
    {
        new QuickTimer(new GameOverTimerTask(this), GameOverTimerTask.PERIOD);
    }

    private class ControllerTimerTask implements TimerTaskPerformer
    {
        private static final int FRAMES_BEFORE_KEY_INPUT = 6;
        private int frameCounter = 0;

        @Override
        public void handleTimerTask()
        {
            if (frameCounter == FRAMES_BEFORE_KEY_INPUT)
            {
                frameCounter = 0;
                Direction direction = keyboard.keyCodeToDirection(keyboard.extractKey());

                if (direction != null)
                    model.tryMove(direction);
            }
            else
                frameCounter++;

            view.renderNextFrame(model.copyGameState(), model.copySelectionState());
        }
    }

    private class GameOverTimerTask implements TimerTaskPerformer
    {
        private static final int TASKS_BEFORE_RESTART = 10;
        private static final int PERIOD = 2 * 1000 / TASKS_BEFORE_RESTART;

        private int taskCounter = 0;

        private QuickTimer gameOverTimer;
        private GameController controller;

        GameOverTimerTask(GameController controller)
        {
            this.controller = controller;
        }

        @Override
        public void handleTimerTask()
        {
            if (taskCounter == TASKS_BEFORE_RESTART)
            {
                gameOverTimer.stop();
                view.getEffectManager().clearEffects();

                model = new GameModel(controller, structure, settings);
            }
            else
            {
                model.killRandomBlocks();

                taskCounter++;
            }
        }

        @Override
        public void setTimer(QuickTimer timer)
        {
            gameOverTimer = timer;
        }
    }
}
