package homemade.game.view.layers;

import homemade.game.Cell;
import homemade.game.fieldstructure.CellCode;
import homemade.game.view.EffectManager;

import java.awt.*;

/**
 * Created by user3 on 02.04.2016.
 */
class BlockLayer extends RenderingLayer
{
    private EffectRenderer effects;
    private EffectManager effectManager;

    BlockLayer(EffectManager effectManager)
    {
        super();

        this.effectManager = effectManager;
        effects = new EffectRenderer();
    }

    @Override
    void renderForCell(CellCode cellCode)
    {
        if (selectionState.canMoveTo(cellCode))
        {
            graphics.drawImage(assets.getPlaceToMove(), canvasX, canvasY, null);
        }

        Cell type = state.getCellState(cellCode).type();

        if (type == Cell.EMPTY)
        {
            int time = effectManager.getFadeTimeRemaining(cellCode);

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
                sprite = assets.getBlock(selectionState.isSelected(cellCode));
            }
            else if (type == Cell.DEAD_BLOCK)
            {
                sprite = assets.getDeadBlock();
            }
            else
                throw new RuntimeException("unknown cell type");

            graphics.drawImage(sprite, canvasX, canvasY, null);
        }
    }
}
