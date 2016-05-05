package homemade.game.controller;

import homemade.game.Effect;
import homemade.game.GameSettings;
import homemade.game.fieldstructure.CellCode;
import homemade.game.fieldstructure.Direction;
import homemade.game.fieldstructure.FieldStructure;
import homemade.game.model.GameModel;
import homemade.game.view.GameView;
import homemade.menu.controller.MenuManager;
import homemade.utils.timer.QuickTimer;
import homemade.utils.timer.TimerTaskPerformer;

import java.awt.*;

public class GameController implements ScoreHandler, BlockRemovalHandler, MouseInputHandler
{
    private static final int TARGET_FPS = 60;

    private MenuManager menuManager;

    private Frame frame;
    private FieldStructure structure;
    private GameSettings settings;

    private GameModel model;
    private GameView view;

    private GameKeyboard keyboard;

    private QuickTimer mainTimer;

    public GameController(MenuManager menuManager, Frame mainFrame, Container container, GameSettings settings)
    {
        initialize(menuManager, mainFrame, container, settings);
    }

    private synchronized void initialize(MenuManager menuManager, Frame mainFrame, Container container, GameSettings settings)
    {
        this.menuManager = menuManager;

        frame = mainFrame;
        this.settings = settings;

        structure = new FieldStructure();

        keyboard = new GameKeyboard(this);
        ViewListener viewListener = new ViewListener(this, this, keyboard);

        view = new GameView(structure, settings, viewListener, container);
        model = new GameModel(this, structure, settings);
        //model must be initialized after view because there could be combos in initialization

        long period = 1000 / TARGET_FPS;
        mainTimer = new QuickTimer(new ControllerTimerTask(), period);
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

    synchronized void requestQuit()
    {
        model.forceStop();
    }

    public synchronized void gameOver()
    {
        new QuickTimer(new GameOverTimerTask(), GameOverTimerTask.PERIOD);
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

    private class GameOverTimerTask extends TimerTaskPerformer.TimerAwarePerformer
    {
        private static final int TASKS_BEFORE_QUIT = 5;
        private static final int PERIOD = 1000 / TASKS_BEFORE_QUIT;

        private int taskCounter = 0;

        @Override
        public void handleTimerTask()
        {
            if (taskCounter == TASKS_BEFORE_QUIT)
            {
                mainTimer.stop();
                timer.stop();
                view.dispose();

                menuManager.switchToMenu(MenuManager.MenuCode.MAIN_MENU);
            }
            else
            {
                model.killRandomBlocks();

                taskCounter++;
            }
        }
    }
}
