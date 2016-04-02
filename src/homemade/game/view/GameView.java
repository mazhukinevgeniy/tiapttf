package homemade.game.view;

import homemade.game.Game;
import homemade.game.GameState;
import homemade.game.SelectionState;
import homemade.game.controller.GameController;
import homemade.game.view.layers.RenderingLayer;
import homemade.resources.Assets;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by user3 on 23.03.2016.
 */
public class GameView
{
    public static final int CellWidth = 50;
    public static final int CellOffset = 1;
    public static final int GridOffset = 1;
    public static final int CanvasWidth = 460;
    public static final int CanvasHeight = 460;

    private Canvas canvas;

    private BufferStrategy strategy;
    private EffectManager effectManager;

    private Timer timer;

    private ArrayList<RenderingLayer> layers;


    public GameView(GameController controller, Frame mainFrame) //TODO: probably should reference interface instead
    {
        this.canvas = new Canvas();
        this.canvas.setPreferredSize(new Dimension(GameView.CanvasWidth, GameView.CanvasHeight));
        mainFrame.add(this.canvas);

        GameMouseAdapter mouseAdapter = new GameMouseAdapter(controller.mouseInputHandler());
        canvas.addMouseListener(mouseAdapter);
        canvas.addMouseMotionListener(mouseAdapter);
        canvas.addKeyListener(new ControlledKeyListener(controller.keyboardInputHandler()));

        canvas.createBufferStrategy(2);

        this.strategy = canvas.getBufferStrategy();

        effectManager = new EffectManager();

        layers = RenderingLayer.getRenderingLayers(effectManager);

        this.timer = new Timer();
        long delay = 0;
        long period = 1000 / Game.TARGET_FPS;
        this.timer.schedule(new ViewTimerTask(controller, effectManager), delay, period);
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

                graphics.clearRect(0, 0, GameView.CanvasWidth, GameView.CanvasHeight);

                graphics.drawImage(Assets.field, GameView.GridOffset, GameView.GridOffset, null);

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

    private class ViewTimerTask extends TimerTask
    {
        private GameController controller;
        private EffectManager effectManager;

        ViewTimerTask(GameController controller, EffectManager effectManager)
        {
            this.controller = controller;
            this.effectManager = effectManager;
        }

        @Override
        public void run()
        {
            this.effectManager.measureTimePassed();
            this.controller.viewTimerUpdated();
            //System.out.println("game view timer task executed");
        }
    }
}