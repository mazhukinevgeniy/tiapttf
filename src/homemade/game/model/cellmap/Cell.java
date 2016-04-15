package homemade.game.model.cellmap;

import homemade.game.Game;
import homemade.game.fieldstructure.FieldStructure;

class Cell
{

    static Cell[] createCells(FieldStructure structure)
    {
        int size = structure.getFieldSize();
        int width = structure.getWidth();
        int height = structure.getHeight();
        
        Cell[] cells = new Cell[size];

        for (int j = 0; j < height; j++)
            for (int i = 0; i < width; i++)
            {
                cells[structure.getCellCode(i, j).value()] = new Cell();
            }


        return cells;
    }

    int value;

    private Cell()
    {
        value = Game.CELL_EMPTY;
    }

}
