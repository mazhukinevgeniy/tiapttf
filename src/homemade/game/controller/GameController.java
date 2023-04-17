package homemade.game.controller;

import homemade.game.ExtendedGameState;
import homemade.game.GameSettings;
import homemade.game.fieldstructure.CellCode;
import homemade.game.fieldstructure.FieldStructure;
import homemade.game.loop.*;
import homemade.game.model.GameModelLinker;
import homemade.game.view.GameView;
import homemade.game.view.ShownEffect;
import homemade.menu.controller.MenuManager;
import homemade.menu.model.records.Records;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;

public class GameController implements MouseInputHandler, GameEventHandler<UIEvent> {
    private static final int TARGET_FPS = 60;

    private MenuManager menuManager;
    private Records records;
    private GameSettings settings;

    private Frame frame;
    private FieldStructure structure;

    private GameModelLinker model;
    private GameView view;
    private GameLoop gameLoop;

    private Timer mainTimer;

    private GameKeyboard keyboard;

    public GameController(MenuManager menuManager, Frame mainFrame, Container container, GameSettings settings, Records records) {
        this.menuManager = menuManager;
        this.records = records;
        this.settings = settings;

        frame = mainFrame;

        structure = new FieldStructure();

        gameLoop = new GameLoop(this);//TODO convert to kotlin to subscribe normally
        keyboard = new GameKeyboard(gameLoop.getModel());
        ViewListener viewListener = new ViewListener(this, this, keyboard);

        view = new GameView(structure, settings, viewListener, container);
        model = new GameModelLinker(structure, settings, gameLoop);
        //model must be initialized after view because there could be combos in initialization
        //TODO: if everything is done with eventLoop channels, this observation is invalid

        mainTimer = new Timer(5, new ActionListener() {
            long previousTime = 0;
            long sum = 0;

            @Override
            public void actionPerformed(ActionEvent e) {
                long newTime = System.currentTimeMillis();
                if (previousTime > 0 && newTime > previousTime) {
                    long diff = newTime - previousTime;
                    gameLoop.getModel().post(new TimeElapsed((int) diff));
                    sum += diff;
                    if (sum >= 1000 / TARGET_FPS) {
                        gameLoop.getModel().post(CreateSnapshot.INSTANCE);
                        sum = sum % (1000 / TARGET_FPS);
                    }
                }
                previousTime = newTime;
                gameLoop.getUi().tryPropagateEvents();
            }
        });
        mainTimer.start();
    }

    @Override
    public void handle(@NotNull UIEvent event) {
        if (event instanceof ShutDown) {
            view.dispose();
            mainTimer.stop();

            int score = model.lastGameState.getGameState().gameScore();

            records.add(score, settings.toString(), LocalDateTime.now());

            //need to post this on UI thread channel
            menuManager.switchToMenu(MenuManager.MenuCode.MAIN_MENU);
        } else if (event instanceof SnapshotReady) {
            ExtendedGameState state = model.lastGameState;

            frame.setTitle("score: " + state.getGameState().gameScore() + ", multiplier: " + state.getGameState().globalMultiplier());
            view.renderNextFrame(state.getGameState(), state.getSelectionState());
        } else if (event instanceof MultiplierChanged) {
            view.getEffectManager().blink(((MultiplierChanged) event).getDiff() > 0);
        } else if (event instanceof BlockRemoved) {
            view.getEffectManager().addEffect(((BlockRemoved) event).getCell(), ShownEffect.FADEAWAY);
        } else if (event instanceof BlockExploded) {
            view.getEffectManager().addEffect(((BlockExploded) event).getCell(), ShownEffect.EXPLOSION);
        } else {
            throw new RuntimeException("unexpected event " + event);
        }
    }

    @Override
    public void handleMouseRelease(int canvasX, int canvasY) {
        int gridX = canvasX - GameView.GRID_OFFSET_X;
        int gridY = canvasY - GameView.GRID_OFFSET_Y;

        int fullCell = GameView.CELL_WIDTH + GameView.CELL_OFFSET;

        int maxX = structure.width * fullCell;
        int maxY = structure.height * fullCell;

        if (gridX >= 0 && gridX < maxX && gridY >= 0 && gridY < maxY) {
            int cellX = gridX / (GameView.CELL_WIDTH + GameView.CELL_OFFSET);
            int cellY = gridY / (GameView.CELL_WIDTH + GameView.CELL_OFFSET);

            CellCode eventCell = structure.getCellCode(cellX, cellY);

            gameLoop.getModel().post(new UserClick(eventCell));
        }
    }

    void requestQuit() {
        gameLoop.getModel().post(new GameOver());
    }
}
