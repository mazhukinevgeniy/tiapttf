package homemade.game.view.layers;

import homemade.game.fieldstructure.FieldStructure;
import homemade.game.view.GameView;
import homemade.resources.Assets;

import java.awt.*;

class DigitMetadata {
    int digitWidth[] = new int[10];
    int digitHeight[] = new int[10];

    private int offsetX[];
    private int offsetY[];

    DigitMetadata(FieldStructure structure, Assets assets) {
        int size = structure.getFieldSize();

        offsetX = new int[size];
        offsetY = new int[size];

        if (size > 100)
            throw new RuntimeException("can't compose digit metadata");

        for (int i = 0; i < 10; i++) {
            Image digit = assets.getDigit(i);

            digitWidth[i] = digit.getWidth(null);
            digitHeight[i] = digit.getHeight(null);
        }

        //we don't need offset for number 0

        for (int i = 1; i < 10; i++) {
            offsetX[i - 1] = (GameView.CELL_WIDTH - digitWidth[i]) / 2;
            offsetY[i - 1] = (GameView.CELL_WIDTH - digitHeight[i]) / 2;
        }

        for (int i = 10; i < size + 1; i++) {
            int numberWidth = digitWidth[i / 10] + 1 + digitWidth[i % 10];

            offsetX[i - 1] = (GameView.CELL_WIDTH - numberWidth) / 2;
            offsetY[i - 1] = Math.min(offsetY[i / 10], offsetY[i % 10]);
            //this offsetY doesn't work properly but it's not supposed to
            //I don't know yet what will we actually need
        }
    }

    int getOffsetXForNumber(int number) //number = 1..FIELD_SIZE
    {
        return offsetX[number - 1];
    }

    int getOffsetYForNumber(int number) //number = 1..FIELD_SIZE
    {
        return offsetY[number - 1];
    }

}
