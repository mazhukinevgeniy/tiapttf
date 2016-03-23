package homemade.game.model;

import homemade.game.Game;

import java.util.Vector;

/**
 * Created by user3 on 22.03.2016.
 */
class Field
{
    //empty if value=0
    //has number if value>0
    //has special function if value<0

    private int width, height;

    private int[] cells;

    private Vector<Integer> usedNumbers, availableNumbers;

    Field(int width, int height)
    {
        this.width = width;
        this.height = height;

        int numberOfCells = width * height;

        this.cells = new int[numberOfCells];

        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++)
            {
                this.cells[i + j * width] = Game.CELL_EMPTY;
            }

        this.usedNumbers = new Vector<Integer>(numberOfCells);
        this.availableNumbers = new Vector<Integer>(numberOfCells);

        for (int i = 0; i < numberOfCells; i++)
        {
            this.availableNumbers.add(i + 1);
        }
    }

    void spawnBlocks()
    {
        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++)
            {
                if (this.cells[i + j * width] == Game.CELL_MARKED_FOR_SPAWN)
                {
                    this.cells[i + j * width] = this.getNumber();

                    System.out.println("block spawned: " + i + ", " + j + " | " + this.cells[i + j * width]);
                }
            }
    }

    void markCells(int targetAmount)
    {
        int cellsUsed = this.usedNumbers.size();
        //this depends on every powerup being removed at the stage of spawning blocks

        if (cellsUsed < width * height)
        {
            Vector<Integer> freeCells = new Vector<Integer>(width * height - cellsUsed);

            for (int i = 0; i < width; i++)
                for (int j = 0; j < height; j++)
                {
                    if (this.cells[i + j * width] == Game.CELL_EMPTY)
                        freeCells.add(i + j * width);
                }

            for (int i = 0; i < Math.min(targetAmount, width * height - cellsUsed); i++)
            {
                int position = (int)(Math.random() * (double)freeCells.size());

                this.cells[freeCells.get(position)] = Game.CELL_MARKED_FOR_SPAWN;

                freeCells.removeElementAt(position);
            }
        }
        else
        {
            //TODO: game over (:
        }
    }

    private int getNumber()
    {
        int length = this.availableNumbers.size();
        int position = (int)(Math.random() * (double)length);
        int number = this.availableNumbers.get(position);

        this.usedNumbers.add(number);
        this.availableNumbers.removeElementAt(position);

        return number;
    }

    public int [] toArray()
    {
        return this.cells.clone();
    }
}

//we cycle through the same stuff a lot, could optimize if needed