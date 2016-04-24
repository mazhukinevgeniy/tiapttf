package homemade.game.view.layers;

import homemade.game.Cell;
import homemade.game.CellState;
import homemade.game.fieldstructure.CellCode;
import homemade.game.view.EffectManager;
import homemade.resources.Assets;

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
            graphics.drawImage(Assets.placeToMove, canvasX, canvasY, null);
        }

        CellState cell = state.getCellState(cellCode);
        int value = state.getCellState(cellCode).value();
        //TODO: see how this method should be written with the new cellState

        if (cell.type() == Cell.EMPTY)
        {
            int time = effectManager.getFadeTimeRemaining(cellCode);

            if (time > 0)
                effects.renderFadingBlock(canvasX, canvasY, time, graphics);
        }
        else
        {
            Image sprite;

            if (cell.type() == Cell.MARKED_FOR_SPAWN)
            {
                sprite = Assets.smallBlock;
            }
            else if (cell.type() == Cell.OCCUPIED)
            {
                if (selectionState.isSelected(cellCode))
                    sprite = Assets.normalBlockSelected;
                else
                    sprite = Assets.normalBlock;
            }
            else if (cell.type() == Cell.DEAD_BLOCK)
            {
                sprite = Assets.deadBlock;
            }
            else
                throw new RuntimeException("unknown cell type");

            graphics.drawImage(sprite, canvasX, canvasY, null);
        }
    }
}
