package homemade.game.view.layers;

import homemade.game.Game;
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

        int value = state.getCellValue(cellCode);

        if (value == Game.CELL_EMPTY)
        {
            int time = effectManager.getFadeTimeRemaining(cellCode);

            if (time > 0)
                effects.renderFadingBlock(canvasX, canvasY, time, graphics);
        }
        else
        {
            Image sprite;

            if (value == Game.CELL_MARKED_FOR_SPAWN)
            {
                sprite = Assets.smallBlock;
            }
            else //if (value > 0) //condition is always true if codes stay unchanged
            {
                if (selectionState.isSelected(cellCode))
                    sprite = Assets.normalBlockSelected;
                else
                    sprite = Assets.normalBlock;
            }

            graphics.drawImage(sprite, canvasX, canvasY, null);
        }
    }
}
