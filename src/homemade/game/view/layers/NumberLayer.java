package homemade.game.view.layers;

import homemade.game.CellState;
import homemade.game.fieldstructure.CellCode;
import homemade.game.fieldstructure.FieldStructure;

class NumberLayer extends RenderingLayer.Cells {
    private DigitMetadata digitMetadata;


    NumberLayer(FieldStructure structure) {
        super();

        digitMetadata = new DigitMetadata(structure, assets);
    }

    @Override
    protected void renderForCell(CellCode cellCode) {
        CellState cell = state.getCellState(cellCode);
        if (cell.isNormalBlock()) {
            int value = cell.value();

            String numberToDraw = String.valueOf(value);

            int numberLength = numberToDraw.length();


            canvasX += digitMetadata.getOffsetXForNumber(value);
            canvasY += digitMetadata.offsetY;


            for (int k = 0; k < numberLength; k++) {
                int digit = Character.getNumericValue(numberToDraw.charAt(k));

                graphics.drawImage(assets.getDigit(digit), canvasX, canvasY, null);

                canvasX += 1 + digitMetadata.digitWidth[digit];
            }
        }
    }
}
