package homemade.game.view.layers;

import homemade.game.Game;
import homemade.game.GameState;
import homemade.game.SelectionState;
import homemade.game.view.GameView;

import java.awt.*;
import java.util.ArrayList;

/**
 * This class is meant to be extended.
 */
abstract public class RenderingLayer
{
    public static final ArrayList<RenderingLayer> getRenderingLayers()
    {
        ArrayList<RenderingLayer> list = new ArrayList<RenderingLayer>();

        list.add(new BlockLayer());
        list.add(new LinkLayer());
        list.add(new NumberLayer());

        return list;
    }






    protected GameState state;
    protected SelectionState selectionState;
    protected Graphics graphics;
    protected int canvasX;
    protected int canvasY;

    private static final int fullCellWidth = GameView.CellWidth + GameView.CellOffset;

    RenderingLayer()
    {

    }

    final public void renderLayer(GameState state, SelectionState selection, Graphics graphics)
    {
        this.state = state;
        this.selectionState = selection;
        this.graphics = graphics;

        int cellWidth = GameView.CellWidth + GameView.CellOffset;

        for (int i = 0; i < Game.FIELD_WIDTH; i++) //render blocks
            for (int j = 0; j < Game.FIELD_HEIGHT; j++)
            {
                canvasX = GameView.GridOffset + fullCellWidth * i;
                canvasY = GameView.GridOffset + fullCellWidth * j;

                renderForCell(i, j);
            }

        this.state = null;
        this.selectionState = null;
        this.graphics = null;
    }

    abstract void renderForCell(int i, int j);
}
