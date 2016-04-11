package homemade.game.view;

import homemade.game.GameState;
import homemade.game.SelectionState;
import homemade.game.controller.GameController;
import homemade.game.view.layers.RenderingLayer;
import homemade.resources.Assets;
import homemade.utils.timer.QuickTimer;
import homemade.utils.timer.TimerTaskPerformer;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;

/**
 * Created by user3 on 23.03.2016.
 */
public class GameView
{
    private static final int TARGET_FPS = 60;

    public static final int CELL_WIDTH = 50;
    public static final int CELL_OFFSET = 1;
    public static final int GRID_OFFSET = 1;
    public static final int CANVAS_WIDTH = 460;
    public static final int CANVAS_HEIGHT = 460;

    private Canvas canvas;

    private BufferStrategy strategy;
    private EffectManager effectManager;

    private GameController controller;

    private QuickTimer timer;

    private ArrayList<RenderingLayer> layers;


    public GameView(GameController controller, Frame mainFrame) //TODO: probably should reference interface instead
    {
        this.controller = controller;

        this.canvas = new Canvas();
        this.canvas.setPreferredSize(new Dimension(GameView.CANVAS_WIDTH, GameView.CANVAS_HEIGHT));
        mainFrame.add(this.canvas);

        GameMouseAdapter mouseAdapter = new GameMouseAdapter(controller.mouseInputHandler());
        canvas.addMouseListener(mouseAdapter);
        canvas.addMouseMotionListener(mouseAdapter);
        canvas.addKeyListener(new ControlledKeyListener(controller.keyboardInputHandler()));

        canvas.createBufferStrategy(2);

        this.strategy = canvas.getBufferStrategy();

        effectManager = new EffectManager();

        layers = RenderingLayer.getRenderingLayers(effectManager);

        timer = new QuickTimer(new ViewTimerTaskPerformer(), 1000 / TARGET_FPS);
    }

    public EffectManager getEffectManager()
    {
        return effectManager;
    }

    public void renderNextFrame(GameState state, SelectionState selection)
    {
        // Render single frame
        do
        {
            // The following loop ensures that the contents of the drawing buffer
            // are consistent in case the underlying surface was recreated
            do
            {
                // Get a new graphics context every time through the loop
                // to make sure the strategy is validated
                Graphics graphics = strategy.getDrawGraphics();

                graphics.clearRect(0, 0, GameView.CANVAS_WIDTH, GameView.CANVAS_HEIGHT);

                graphics.drawImage(Assets.field, GameView.GRID_OFFSET, GameView.GRID_OFFSET, null);

                for (RenderingLayer layer : layers)
                {
                    layer.renderLayer(state, selection, graphics);
                }


                // Dispose the graphics
                graphics.dispose();

                // Repeat the rendering if the drawing buffer contents
                // were restored
            } while (strategy.contentsRestored());

            // Display the buffer
            strategy.show();

            // Repeat the rendering if the drawing buffer was lost
        } while (strategy.contentsLost());
    }

    private class ViewTimerTaskPerformer implements TimerTaskPerformer
    {
        @Override
        public void handleTimerTask()
        {
            effectManager.measureTimePassed();
            controller.viewTimerUpdated();
        }
    }
}