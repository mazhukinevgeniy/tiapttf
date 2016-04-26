package homemade.game.view;

import homemade.game.GameSettings;
import homemade.game.GameState;
import homemade.game.SelectionState;
import homemade.game.controller.ViewListener;
import homemade.game.fieldstructure.FieldStructure;
import homemade.game.view.layers.RenderingLayer;
import homemade.resources.Assets;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;

/**
 * Created by user3 on 23.03.2016.
 */
public class GameView
{

    public static final int CELL_WIDTH = 50;
    public static final int CELL_OFFSET = 1;
    public static final int GRID_OFFSET = 1;
    public static final int CANVAS_WIDTH = 460;
    public static final int CANVAS_HEIGHT = 460;

    private FieldStructure structure;

    private Canvas canvas;

    private BufferStrategy strategy;
    private EffectManager effectManager;

    private ArrayList<RenderingLayer> layers;


    public GameView(FieldStructure structure, GameSettings settings, ViewListener viewListener, Frame mainFrame)
    {
        initialize(structure, settings, viewListener, mainFrame);
    }

    private synchronized void initialize(FieldStructure structure, GameSettings settings, ViewListener viewListener, Frame mainFrame)
    {
        effectManager = new EffectManager();

        this.structure = structure;

        canvas = new Canvas();
        canvas.setPreferredSize(new Dimension(GameView.CANVAS_WIDTH, GameView.CANVAS_HEIGHT));
        mainFrame.add(canvas);

        GameMouseAdapter mouseAdapter = new GameMouseAdapter(viewListener.mouseInputHandler());
        canvas.addMouseListener(mouseAdapter);
        canvas.addMouseMotionListener(mouseAdapter);
        canvas.addKeyListener(new ControlledKeyListener(viewListener.keyboardInputHandler()));

        canvas.createBufferStrategy(2);

        strategy = canvas.getBufferStrategy();

        layers = RenderingLayer.getRenderingLayers(structure, settings, effectManager);
    }

    public synchronized EffectManager getEffectManager()
    {
        return effectManager;
    }

    public synchronized void renderNextFrame(GameState state, SelectionState selection)
    {
        effectManager.measureTimePassed();

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

                graphics.drawImage(Assets.getAssets().getField(), GameView.GRID_OFFSET, GameView.GRID_OFFSET, null);

                for (RenderingLayer layer : layers)
                {
                    layer.renderLayer(structure, state, selection, graphics);
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

}