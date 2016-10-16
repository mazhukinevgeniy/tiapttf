package homemade.game.view.layers;

import homemade.game.Cell;
import homemade.game.CellState;
import homemade.game.fieldstructure.CellCode;
import homemade.resources.blocks.BlockAssets;

import java.awt.*;

class BlockLayer extends RenderingLayer.Cells
{
    private BlockAssets blockAssets;

    BlockLayer()
    {
        super();

        blockAssets = assets.getBlockAssets();
    }

    @Override
    protected void renderForCell(CellCode cellCode)
    {
        if (selectionState.canMoveTo(cellCode))
        {
            graphics.drawImage(assets.getPlaceToMove(), canvasX, canvasY, null);
        }

        CellState cellState = state.getCellState(cellCode);
        Cell type = cellState.type();

        if (type != Cell.EMPTY)
        {
            Image sprite;

            if (type == Cell.MARKED_FOR_SPAWN)
            {
                sprite = assets.getSmallBlock();
            }
            else if (type == Cell.OCCUPIED)
            {
                sprite = blockAssets.getBlock(selectionState.isSelected(cellCode), cellState.effect());
            }
            else if (type == Cell.DEAD_BLOCK)
            {
                sprite = blockAssets.getDeadBlock();
            }
            else
                throw new RuntimeException("unknown cell type");

            graphics.drawImage(sprite, canvasX, canvasY, null);
        }
    }
}
