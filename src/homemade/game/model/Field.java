package homemade.game.model;

import homemade.game.Game;

/**
 * Created by user3 on 22.03.2016.
 */
class Field
{
    //empty if value=0
    //has number if value>0
    //has special function if value<0

    protected int width, height;

    protected int[] cells;

    Field(int width, int height)
    {
        this.width = width;
        this.height = height;

        this.cells = new int[width * height];

        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++)
            {
                this.cells[i + j * width] = Game.CELL_EMPTY;
            }
    }

    void spawnBlocks()
    {

    }

    void markCells(int targetAmount)
    {

    }
}
