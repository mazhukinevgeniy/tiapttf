package homemade.game.controller;

import homemade.game.GameSettings;
import homemade.game.GameState;
import homemade.game.fieldstructure.CellCode;
import homemade.game.fieldstructure.Direction;
import homemade.game.fieldstructure.FieldStructure;
import homemade.game.model.GameModel;
import homemade.game.view.GameView;
import homemade.game.view.ShownEffect;
import homemade.menu.controller.MenuManager;
import homemade.menu.model.records.Records;
import homemade.utils.timer.QuickTimer;
import homemade.utils.timer.TimerTaskPerformer;

import java.awt.*;
import java.time.LocalDateTime;

public class GameController implements BlockEventHandler, MouseInputHandler
{
    private static final int TARGET_FPS = 60;

    private MenuManager menuManager;
    private Records records;
    private GameSettings settings;

    private Frame frame;
    private FieldStructure structure;

    private GameModel model;
    private GameView view;

    private GameKeyboard keyboard;

    private QuickTimer mainTimer;

    public GameController(MenuManager menuManager, Frame mainFrame, Container container, GameSettings settings, Records records)
    {
        initialize(menuManager, mainFrame, container, settings, records);
    }

    private synchronized void initialize(MenuManager menuManager, Frame mainFrame, Container container, GameSettings settings, Records records)
    {
        this.menuManager = menuManager;
        this.records = records;
        this.settings = settings;

        frame = mainFrame;

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

    public synchronized void blockRemoved(CellCode atCell)
    {
        view.getEffectManager().addEffect(atCell, ShownEffect.FADEAWAY);
    }

    public synchronized void blockExploded(CellCode atCell)
    {
        view.getEffectManager().addEffect(atCell, ShownEffect.EXPLOSION);
    }

    public synchronized void multiplierChanged(int change)
    {
        view.getEffectManager().blink(change > 0);
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
                Direction direction = keyboard.extractDirection();

                if (direction != null)
                    model.tryMove(direction);
            }
            else
                frameCounter++;

            GameState state = model.copyGameState();

            frame.setTitle("score: " + state.gameScore() + ", multiplier: " + state.globalMultiplier());
            view.renderNextFrame(state, model.copySelectionState());
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

                int score = model.copyGameState().gameScore();
                String name = new StringBuilder()
                        .append("sp").append(settings.maxSpawn())
                        .append("c").append(settings.minCombo())
                        .append("per").append(settings.maxPeriod())
                        .append(settings.gameMode() == GameSettings.GameMode.TURN_BASED ? "tb" : "rt")
                        .toString();

                records.add(score, name, LocalDateTime.now());
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
