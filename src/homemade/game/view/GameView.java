package homemade.game.view;

import homemade.game.Game;
import homemade.game.GameState;
import homemade.game.SelectionState;
import homemade.game.controller.GameController;
import homemade.resources.Assets;

import java.awt.*;
import java.awt.image.BufferStrategy;
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
    private GameMouseAdapter mouseAdapter;

    private Timer timer;


    public GameView(GameController controller, Frame mainFrame) //TODO: probably should reference interface instead
    {
        this.canvas = new Canvas();
        this.canvas.setPreferredSize(new Dimension(GameView.CanvasWidth, GameView.CanvasHeight));
        mainFrame.add(this.canvas);

        this.mouseAdapter = new GameMouseAdapter(controller.mouseInputHandler());
        canvas.addMouseListener(this.mouseAdapter);
        canvas.addMouseMotionListener(this.mouseAdapter);

        canvas.createBufferStrategy(2);

        this.strategy = canvas.getBufferStrategy();

        this.timer = new Timer();
        long delay = 0; //time before the timertask is executed
        long period = 1000 / 30; //TODO: move to settings
        this.timer.schedule(new ViewTimerTask(controller), delay, period);
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

                int cellWidth = GameView.CellWidth + GameView.CellOffset;

                graphics.drawImage(Assets.grid, GameView.GridOffset, GameView.GridOffset, null);

                for (int i = 0; i < Game.FIELD_WIDTH; i++)
                    for (int j = 0; j < Game.FIELD_HEIGHT; j++)
                    {
                        int value = state.getCellValue(i, j);

                        if (value == Game.CELL_EMPTY)
                        {
                            //?
                        }
                        else
                        {
                            Image sprite;

                            if (value == Game.CELL_MARKED_FOR_SPAWN)
                            {
                                sprite = Assets.smallBlock;
                            }
                            else //if (value > 0) //condition is always true if codes stay unchanged
                            {
                                if (selection.isSelected(i, j))
                                    sprite = Assets.normalBlockSelected;
                                else
                                    sprite = Assets.normalBlock;
                            }

                            graphics.drawImage(sprite,
                                               GameView.GridOffset + cellWidth * i,
                                               GameView.GridOffset + cellWidth * j,
                                               null);
                        }

                    }

                // Render to graphics
                // ...

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

        ViewTimerTask(GameController controller)
        {
            this.controller = controller;
        }

        @Override
        public void run()
        {
            this.controller.viewTimerUpdated();
            //System.out.println("game view timer task executed");
        }
    }
}
