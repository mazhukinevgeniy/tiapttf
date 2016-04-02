package homemade.game.view.layers;

import homemade.game.Game;
import homemade.game.view.GameView;
import homemade.resources.Assets;

/**
 * Created by user3 on 25.03.2016.
 */
class DigitMetadata
{
    int digitWidth[] = new int[10];
    int digitHeight = 22; //true while all assets share this quality

    private int offsetX[] = new int[Game.FIELD_WIDTH * Game.FIELD_HEIGHT];
    int offsetY = (GameView.CellWidth - digitHeight) / 2;

    DigitMetadata()
    {
        if (Game.FIELD_WIDTH * Game.FIELD_HEIGHT > 100)
            throw new Error("can't compose digit metadata");

        for (int i = 0; i < 10; i++)
        {
            this.digitWidth[i] = Assets.digit[i].getWidth(null);
        }

        //we don't need offset for number 0

        for (int i = 1; i < 10; i++)
        {
            this.offsetX[i - 1] = (GameView.CellWidth - digitWidth[i]) / 2;
        }

        for (int i = 10; i < Game.FIELD_WIDTH * Game.FIELD_HEIGHT + 1; i++)
        {
            int numberWidth = digitWidth[i / 10] + 1 + digitWidth[i % 10];

            this.offsetX[i - 1] = (GameView.CellWidth - numberWidth) / 2;
        }
    }

    int getOffsetXForNumber(int number) //number = 1..FIELD_SIZE
    {
        return offsetX[number - 1];
    }


}
