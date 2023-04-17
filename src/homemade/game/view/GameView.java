package homemade.game.view;

import homemade.game.controller.ViewListener;
import homemade.game.fieldstructure.FieldStructure;
import homemade.game.model.GameSettings;
import homemade.game.state.FieldState;
import homemade.game.state.SelectionState;
import homemade.game.view.layers.RenderingLayer;
import homemade.resources.Assets;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;

public class GameView {
    static final Integer CANVAS_LAYER = 0;
    static final Integer PANEL_LAYER = 1;

    public static final int CELL_WIDTH = 50;
    public static final int CELL_OFFSET = 1;

    public static final int GRID_OFFSET_X = 1;
    public static final int GRID_OFFSET_Y = 40;
    public static final int GRID_WIDTH = 460;
    public static final int GRID_HEIGHT = 460;

    private FieldStructure structure;

    private Container container;
    private BufferStrategy strategy;
    private EffectManager effectManager;

    private ArrayList<RenderingLayer> layers;

    private FpsTracker fpsTracker;


    public GameView(FieldStructure structure, GameSettings settings, ViewListener viewListener, Container container) {
        effectManager = new EffectManager();
        fpsTracker = new FpsTracker();

        this.structure = structure;
        this.container = container;

        final int CANVAS_WIDTH = GRID_OFFSET_X + GRID_WIDTH;
        final int CANVAS_HEIGHT = GRID_OFFSET_Y + GRID_HEIGHT;

        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));
        container.add(layeredPane);

        new GameInfoPanel(viewListener, layeredPane, CANVAS_WIDTH, GRID_OFFSET_Y);

        Canvas canvas = new Canvas();
        canvas.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));
        canvas.setBounds(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);
        layeredPane.add(canvas, CANVAS_LAYER);

        GameMouseAdapter mouseAdapter = new GameMouseAdapter(viewListener.mouseInputHandler());
        canvas.addMouseListener(mouseAdapter);
        canvas.addMouseMotionListener(mouseAdapter);
        canvas.addKeyListener(new ControlledKeyListener(viewListener.keyboardInputHandler()));

        canvas.createBufferStrategy(2);

        strategy = canvas.getBufferStrategy();

        layers = RenderingLayer.getRenderingLayers(structure, settings, effectManager);
    }

    public synchronized EffectManager getEffectManager() {
        return effectManager;
    }

    public synchronized void dispose() {
        if (strategy != null) {
            strategy.dispose();
            strategy = null;
        }

        effectManager.clearEffects();
    }

    /**
     * Would cause an exception if called after dispose()
     */
    public synchronized void renderNextFrame(FieldState state, SelectionState selection) {
        effectManager.measureTimePassed();
        fpsTracker.addTimestamp();

        if (strategy == null) {
            return;
        }

        container.setBackground(effectManager.getBackgroundColor());

        // Render single frame
        do {
            // The following loop ensures that the contents of the drawing buffer
            // are consistent in case the underlying surface was recreated
            do {
                // Get a new graphics context every time through the loop
                // to make sure the strategy is validated
                Graphics graphics = strategy.getDrawGraphics();

                graphics.clearRect(GRID_OFFSET_X, GRID_OFFSET_Y, GRID_WIDTH, GRID_HEIGHT);

                graphics.drawImage(Assets.getAssets().getField(), GRID_OFFSET_X, GRID_OFFSET_Y, null);

                for (RenderingLayer layer : layers) {
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
