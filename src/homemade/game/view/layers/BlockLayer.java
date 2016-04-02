package homemade.game.view.layers;

import homemade.game.CellCode;
import homemade.game.Game;
import homemade.resources.Assets;

import java.awt.*;

/**
 * Created by user3 on 02.04.2016.
 */
class BlockLayer extends RenderingLayer
{
    BlockLayer()
    {
        super();
    }

    @Override
    void renderForCell(int i, int j)
    {
        CellCode cellCode = CellCode.getFor(i, j);

        if (selectionState.canMoveTo(cellCode))
        {
            graphics.drawImage(Assets.placeToMove, canvasX, canvasY, null);
        }

        int value = state.getCellValue(cellCode);

        if (value == Game.CELL_EMPTY)
        {
            //?
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
