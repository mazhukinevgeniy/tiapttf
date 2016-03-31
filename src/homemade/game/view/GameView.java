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

    private DigitMetadata digitMetadata = new DigitMetadata();

    private BufferStrategy strategy;

    private Timer timer;


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

        this.timer = new Timer();
        long delay = 0;
        long period = 1000 / Game.TARGET_FPS;
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

                graphics.clearRect(0, 0, GameView.CanvasWidth, GameView.CanvasHeight);

                int cellWidth = GameView.CellWidth + GameView.CellOffset;

                graphics.drawImage(Assets.field, GameView.GridOffset, GameView.GridOffset, null);

                for (int i = 0; i < Game.FIELD_WIDTH; i++) //render blocks
                    for (int j = 0; j < Game.FIELD_HEIGHT; j++)
                    {
                        int canvasX = GameView.GridOffset + cellWidth * i;
                        int canvasY = GameView.GridOffset + cellWidth * j;

                        if (selection.canMoveTo(i, j))
                        {
                            graphics.drawImage(Assets.placeToMove, canvasX, canvasY, null);
                        }

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

                            graphics.drawImage(sprite, canvasX, canvasY, null);
                        }
                    }

                int vertGlowOffsetX = (GameView.CellWidth - Assets.glowVertical.getWidth(null)) / 2;
                int vertGlowOffsetY = (GameView.CellWidth * 2 + GameView.CellOffset - Assets.glowVertical.getHeight(null)) / 2;

                int horGlowOffsetX = (GameView.CellWidth * 2 + GameView.CellOffset - Assets.glowHorizontal.getWidth(null)) / 2;
                int horGlowOffsetY = (GameView.CellWidth - Assets.glowHorizontal.getHeight(null)) / 2;

                for (int i = 0; i < Game.FIELD_WIDTH; i++) //render glow
                    for (int j = 0; j < Game.FIELD_HEIGHT; j++)
                    {
                        int cellCode = i + j * Game.FIELD_WIDTH;
                        int rightCellCode = cellCode + 1;
                        int bottomCellCode = cellCode + Game.FIELD_WIDTH;
                        //we can do it because we recognize 2*fieldSize links

                        boolean rightLink = state.getLinkBetweenCells(cellCode, rightCellCode);
                        if (rightLink)
                        {
                            int canvasX = GameView.GridOffset + cellWidth * i;
                            int canvasY = GameView.GridOffset + cellWidth * j;

                            canvasX += horGlowOffsetX;
                            canvasY += horGlowOffsetY;

                            graphics.drawImage(Assets.glowHorizontal, canvasX, canvasY, null);
                        }

                        boolean bottomLink = state.getLinkBetweenCells(cellCode, bottomCellCode);
                        if (bottomLink)
                        {
                            int canvasX = GameView.GridOffset + cellWidth * i;
                            int canvasY = GameView.GridOffset + cellWidth * j;

                            canvasX += vertGlowOffsetX;
                            canvasY += vertGlowOffsetY;

                            graphics.drawImage(Assets.glowVertical, canvasX, canvasY, null);
                        }
                    }

                for (int i = 0; i < Game.FIELD_WIDTH; i++) //render numbers
                    for (int j = 0; j < Game.FIELD_HEIGHT; j++)
                    {
                        int canvasX = GameView.GridOffset + cellWidth * i;
                        int canvasY = GameView.GridOffset + cellWidth * j;

                        int value = state.getCellValue(i, j);
                        if (value > 0)
                        {
                            String numberToDraw = String.valueOf(value);

                            int numberLength = numberToDraw.length();


                            canvasX += digitMetadata.getOffsetXForNumber(value);
                            canvasY += digitMetadata.offsetY;


                            for (int k = 0; k < numberLength; k++)
                            {
                                int digit = Character.getNumericValue(numberToDraw.charAt(k));

                                graphics.drawImage(Assets.digit[digit], canvasX, canvasY, null);

                                canvasX += 1 + digitMetadata.digitWidth[digit];
                            }
                        }
                    }

                //TODO: here is the great opportunity for splitting classes

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