package homemade.game.view.layers;

import homemade.game.Cell;
import homemade.game.CellState;
import homemade.game.fieldstructure.CellCode;
import homemade.game.view.EffectManager;
import homemade.game.view.ShownEffect;
import homemade.resources.blocks.BlockAssets;

import java.awt.*;

class BlockLayer extends RenderingLayer.Cells
{
    private EffectRenderer effects;
    private EffectManager effectManager;

    private BlockAssets blockAssets;

    BlockLayer(EffectManager effectManager)
    {
        super();

        this.effectManager = effectManager;
        effects = new EffectRenderer();

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

        if (type == Cell.EMPTY)
        {
            float time = effectManager.getEffectTimeRemaining(cellCode, ShownEffect.FADEAWAY);

            if (time > 0)
                effects.renderFadingBlock(canvasX, canvasY, time, graphics);
        }
        else
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
