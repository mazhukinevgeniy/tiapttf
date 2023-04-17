package homemade.game.view.layers;

import homemade.game.GameSettings;
import homemade.game.SelectionState;
import homemade.game.fieldstructure.CellCode;
import homemade.game.fieldstructure.FieldStructure;
import homemade.game.fieldstructure.LinkCode;
import homemade.game.state.GameState;
import homemade.game.view.EffectManager;
import homemade.game.view.GameView;
import homemade.resources.Assets;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * This class is meant to be extended.
 */
abstract public class RenderingLayer {
    private static final int fullCellWidth = GameView.CELL_WIDTH + GameView.CELL_OFFSET;

    public static final ArrayList<RenderingLayer> getRenderingLayers(FieldStructure structure,
                                                                     GameSettings settings,
                                                                     EffectManager effectManager) {
        ArrayList<RenderingLayer> list = new ArrayList<RenderingLayer>();

        list.add(new EffectLayer(effectManager));
        list.add(new BlockLayer());
        list.add(new NumberLayer(structure));
        list.add(new LinkLayer(settings));

        return list;
    }

    protected GameState state;
    protected SelectionState selectionState;
    protected Graphics graphics;
    protected int canvasX;
    protected int canvasY;

    protected Assets assets;

    RenderingLayer() {
        assets = Assets.getAssets();
    }

    final public void renderLayer(FieldStructure structure, GameState state, SelectionState selection, Graphics graphics) {
        this.state = state;
        this.selectionState = selection;
        this.graphics = graphics;

        iterate(structure);

        this.state = null;
        this.selectionState = null;
        this.graphics = null;
    }

    protected abstract void iterate(FieldStructure structure);

    protected void renderForCell(CellCode cellCode) {
    }

    protected void renderForLink(LinkCode linkCode) {
    }

    final protected void setCanvasCoordinates(int i, int j) {
        canvasX = GameView.GRID_OFFSET_X + fullCellWidth * i;
        canvasY = GameView.GRID_OFFSET_Y + fullCellWidth * j;
    }

    static class Cells extends RenderingLayer {
        @Override
        protected void iterate(FieldStructure structure) {
            Iterator<CellCode> cellCodeIterator = structure.getCellCodeIterator();

            while (cellCodeIterator.hasNext()) {
                CellCode next = cellCodeIterator.next();

                int x = next.getX();
                int y = next.getY();

                setCanvasCoordinates(x, y);
                renderForCell(next);
            }
        }
    }

    static class Links extends RenderingLayer {
        @Override
        protected void iterate(FieldStructure structure) {
            Iterator<LinkCode> linkCodeIterator = structure.getLinkCodeIterator();

            while (linkCodeIterator.hasNext()) {
                LinkCode next = linkCodeIterator.next();

                CellCode lower = next.lower;
                int x = lower.getX();
                int y = lower.getY();

                setCanvasCoordinates(x, y);
                renderForLink(next);
            }
        }
    }
}
