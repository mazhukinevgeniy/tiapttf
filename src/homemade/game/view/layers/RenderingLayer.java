package homemade.game.view.layers;

import homemade.game.GameSettings;
import homemade.game.GameState;
import homemade.game.SelectionState;
import homemade.game.fieldstructure.CellCode;
import homemade.game.fieldstructure.FieldStructure;
import homemade.game.view.EffectManager;
import homemade.game.view.GameView;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * This class is meant to be extended.
 */
abstract public class RenderingLayer
{
    private static final int fullCellWidth = GameView.CELL_WIDTH + GameView.CELL_OFFSET;

    public static final ArrayList<RenderingLayer> getRenderingLayers(FieldStructure structure,
                                                                     GameSettings settings,
                                                                     EffectManager effectManager)
    {
        ArrayList<RenderingLayer> list = new ArrayList<RenderingLayer>();

        list.add(new BlockLayer(effectManager));
        list.add(new NumberLayer(structure));
        list.add(new LinkLayer(structure, settings));

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

    final public void renderLayer(Iterator<CellCode> cellCodeIterator, GameState state, SelectionState selection, Graphics graphics)
    {
        this.state = state;
        this.selectionState = selection;
        this.graphics = graphics;

        while (cellCodeIterator.hasNext())
        {
            CellCode next = cellCodeIterator.next();

            int x = next.x();
            int y = next.y();

            setCanvasCoordinates(x, y);
            renderForCell(next);
        }

        this.state = null;
        this.selectionState = null;
        this.graphics = null;
    }

    abstract void renderForCell(CellCode cellCode);

    final protected void setCanvasCoordinates(int i, int j)
    {
        canvasX = GameView.GRID_OFFSET + fullCellWidth * i;
        canvasY = GameView.GRID_OFFSET + fullCellWidth * j;
    }


}
