package homemade.game.view.layers;

import homemade.game.fieldstructure.CellCode;
import homemade.game.fieldstructure.Direction;
import homemade.game.view.GameView;
import homemade.resources.Assets;

/**
 * As of 2.04.2016, this layer will not work properly if we don't call renderForCell for each cell
 */
class LinkLayer extends RenderingLayer
{
    private int vertGlowOffsetX = (GameView.CELL_WIDTH - Assets.glowVertical.getWidth(null)) / 2;
    private int vertGlowOffsetY = (GameView.CELL_WIDTH * 2 + GameView.CELL_OFFSET - Assets.glowVertical.getHeight(null)) / 2;

    private int horGlowOffsetX = (GameView.CELL_WIDTH * 2 + GameView.CELL_OFFSET - Assets.glowHorizontal.getWidth(null)) / 2;
    private int horGlowOffsetY = (GameView.CELL_WIDTH - Assets.glowHorizontal.getHeight(null)) / 2;


    LinkLayer()
    {
        super();
    }

    @Override
    void renderForCell(int i, int j)
    {
        CellCode cellCode = CellCode.getFor(i, j);
        //we can do it because we recognize 2*fieldSize links

        boolean rightLink = state.getLinkBetweenCells(cellCode.linkNumber(Direction.RIGHT));
        if (rightLink)
        {
            graphics.drawImage(Assets.glowHorizontal, canvasX + horGlowOffsetX, canvasY + horGlowOffsetY, null);
        }

        boolean bottomLink = state.getLinkBetweenCells(cellCode.linkNumber(Direction.BOTTOM));
        if (bottomLink)
        {
            graphics.drawImage(Assets.glowVertical, canvasX + vertGlowOffsetX, canvasY + vertGlowOffsetY, null);
        }
    }
}
