package homemade.game.view;

import homemade.game.Game;
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
    private GameController controller;

    private Canvas canvas;
    private BufferStrategy strategy;
    private Timer timer;


    public GameView(GameController controller, Frame mainFrame) //TODO: probably should reference interface instead
    {
        this.controller = controller;


        this.canvas = new Canvas();
        this.canvas.setPreferredSize(new Dimension(460, 460));//TODO: fix magic numbers
        mainFrame.add(this.canvas);

        canvas.createBufferStrategy(2);

        this.strategy = canvas.getBufferStrategy();

        this.timer = new Timer();
        long delay = 0; //time before the timertask is executed
        long period = 1000 / 30; //TODO: move to settings
        this.timer.schedule(new ViewTimerTask(controller), delay, period);
    }

    public void renderNextFrame(int[] data)
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

                int cellWidth = 50 + 1;

                graphics.drawImage(Assets.grid, 1, 1, null);

                for (int i = 0; i < Game.FIELD_WIDTH; i++)
                    for (int j = 0; j < Game.FIELD_HEIGHT; j++)
                    {
                        int value = data[i + j * Game.FIELD_WIDTH];

                        if (value == Game.CELL_EMPTY)
                        {
                            //?
                        }
                        else if (value == Game.CELL_MARKED_FOR_SPAWN)
                        {
                            graphics.drawImage(Assets.smallBlock, 1 + cellWidth * i, 1 + cellWidth * j, null);
                        }
                        else if (value > 0) //condition is always true if codes stay unchanged
                        {
                            graphics.drawImage(Assets.normalBlock, 1 + cellWidth * i, 1 + cellWidth * j, null);
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
            System.out.println("game view timer task executed");
        }
    }
}
