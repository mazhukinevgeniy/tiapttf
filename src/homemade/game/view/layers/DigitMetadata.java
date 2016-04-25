package homemade.game.view.layers;

import homemade.game.fieldstructure.FieldStructure;
import homemade.game.view.GameView;
import homemade.resources.Assets;

/**
 * Created by user3 on 25.03.2016.
 */
class DigitMetadata
{
    int digitWidth[] = new int[10];
    int digitHeight = 22; //true while all assets share this quality

    private int offsetX[];
    int offsetY = (GameView.CELL_WIDTH - digitHeight) / 2;

    DigitMetadata(FieldStructure structure, Assets assets)
    {
        int size = structure.getFieldSize();

        offsetX = new int[size];

        if (size > 100)
            throw new Error("can't compose digit metadata");

        for (int i = 0; i < 10; i++)
        {
            this.digitWidth[i] = assets.getDigit(i).getWidth(null);
        }

        //we don't need offset for number 0

        for (int i = 1; i < 10; i++)
        {
            this.offsetX[i - 1] = (GameView.CELL_WIDTH - digitWidth[i]) / 2;
        }

        for (int i = 10; i < size + 1; i++)
        {
            int numberWidth = digitWidth[i / 10] + 1 + digitWidth[i % 10];

            this.offsetX[i - 1] = (GameView.CELL_WIDTH - numberWidth) / 2;
        }
    }

    int getOffsetXForNumber(int number) //number = 1..FIELD_SIZE
    {
        return offsetX[number - 1];
    }


}
