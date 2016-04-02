package homemade.game.view.layers;

import homemade.game.CellCode;
import homemade.game.Direction;
import homemade.game.view.GameView;
import homemade.resources.Assets;

/**
 * As of 2.04.2016, this layer will not work properly if we don't call renderForCell for each cell
 */
class LinkLayer extends RenderingLayer
{
    private int vertGlowOffsetX = (GameView.CellWidth - Assets.glowVertical.getWidth(null)) / 2;
    private int vertGlowOffsetY = (GameView.CellWidth * 2 + GameView.CellOffset - Assets.glowVertical.getHeight(null)) / 2;

    private int horGlowOffsetX = (GameView.CellWidth * 2 + GameView.CellOffset - Assets.glowHorizontal.getWidth(null)) / 2;
    private int horGlowOffsetY = (GameView.CellWidth - Assets.glowHorizontal.getHeight(null)) / 2;


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
