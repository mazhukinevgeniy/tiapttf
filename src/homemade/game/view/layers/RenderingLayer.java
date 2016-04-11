package homemade.game.view.layers;

import homemade.game.Game;
import homemade.game.GameState;
import homemade.game.SelectionState;
import homemade.game.view.EffectManager;
import homemade.game.view.GameView;

import java.awt.*;
import java.util.ArrayList;

/**
 * This class is meant to be extended.
 */
abstract public class RenderingLayer
{
    private static final int fullCellWidth = GameView.CELL_WIDTH + GameView.CELL_OFFSET;

    public static final ArrayList<RenderingLayer> getRenderingLayers(EffectManager effectManager)
    {
        ArrayList<RenderingLayer> list = new ArrayList<RenderingLayer>();

        list.add(new BlockLayer(effectManager));
        list.add(new LinkLayer());
        list.add(new NumberLayer());

        return list;
    }

    protected GameState state;
    protected SelectionState selectionState;
    protected Graphics graphics;
    protected int canvasX;
    protected int canvasY;

    RenderingLayer()
    {
        super();
    }

    final public void renderLayer(GameState state, SelectionState selection, Graphics graphics)
    {
        this.state = state;
        this.selectionState = selection;
        this.graphics = graphics;

        int cellWidth = GameView.CELL_WIDTH + GameView.CELL_OFFSET;

        for (int i = 0; i < Game.FIELD_WIDTH; i++) //render blocks
            for (int j = 0; j < Game.FIELD_HEIGHT; j++)
            {
                setCanvasCoordinates(i, j);

                renderForCell(i, j);
            }

        this.state = null;
        this.selectionState = null;
        this.graphics = null;
    }

    abstract void renderForCell(int i, int j);

    final protected void setCanvasCoordinates(int i, int j)
    {
        canvasX = GameView.GRID_OFFSET + fullCellWidth * i;
        canvasY = GameView.GRID_OFFSET + fullCellWidth * j;
    }


}
