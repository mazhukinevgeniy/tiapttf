package homemade.game.view.layers;

import homemade.game.fieldstructure.CellCode;
import homemade.resources.Assets;

/**
 * Created by user3 on 02.04.2016.
 */
class NumberLayer extends RenderingLayer
{
    private DigitMetadata digitMetadata = new DigitMetadata();


    NumberLayer()
    {
        super();
    }

    @Override
    void renderForCell(int i, int j)
    {
        int value = state.getCellValue(CellCode.getFor(i, j));
        if (value > 0)
        {
            String numberToDraw = String.valueOf(value);

            int numberLength = numberToDraw.length();


            canvasX += digitMetadata.getOffsetXForNumber(value);
            canvasY += digitMetadata.offsetY;


            for (int k = 0; k < numberLength; k++)
            {
                int digit = Character.getNumericValue(numberToDraw.charAt(k));

                graphics.drawImage(Assets.digit[digit], canvasX, canvasY, null);

                canvasX += 1 + digitMetadata.digitWidth[digit];
            }
        }
    }
}
