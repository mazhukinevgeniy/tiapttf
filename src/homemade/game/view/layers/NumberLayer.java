package homemade.game.view.layers;

import homemade.game.fieldstructure.CellCode;
import homemade.game.fieldstructure.FieldStructure;
import homemade.resources.Assets;

/**
 * Created by user3 on 02.04.2016.
 */
class NumberLayer extends RenderingLayer
{
    private DigitMetadata digitMetadata;


    NumberLayer(FieldStructure structure)
    {
        super();

        digitMetadata = new DigitMetadata(structure);
    }

    @Override
    void renderForCell(CellCode cellCode)
    {
        int value = state.getCellValue(cellCode);
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
