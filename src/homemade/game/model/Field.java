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

    private int[] cells;

    private Vector<Integer> usedNumbers, availableNumbers;

    Field()
    {
        int numberOfCells = Game.FIELD_WIDTH * Game.FIELD_HEIGHT;

        this.cells = new int[numberOfCells];

        for (int i = 0; i < Game.FIELD_WIDTH; i++)
            for (int j = 0; j < Game.FIELD_HEIGHT; j++)
            {
                this.cells[i + j * Game.FIELD_WIDTH] = Game.CELL_EMPTY;
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
        for (int i = 0; i < Game.FIELD_WIDTH; i++)
            for (int j = 0; j < Game.FIELD_HEIGHT; j++)
            {
                if (this.cells[i + j * Game.FIELD_WIDTH] == Game.CELL_MARKED_FOR_SPAWN)
                {
                    this.cells[i + j * Game.FIELD_WIDTH] = this.getNumber();

                    System.out.println("block spawned: " + i + ", " + j + " | " + this.cells[i + j * Game.FIELD_WIDTH]);
                }
            }
    }

    void markCells(int targetAmount)
    {
        int cellsUsed = this.usedNumbers.size();
        //this depends on every powerup being removed at the stage of spawning blocks

        if (cellsUsed < Game.FIELD_WIDTH * Game.FIELD_HEIGHT)
        {
            Vector<Integer> freeCells = new Vector<Integer>(Game.FIELD_WIDTH * Game.FIELD_HEIGHT - cellsUsed);

            for (int i = 0; i < Game.FIELD_WIDTH; i++)
                for (int j = 0; j < Game.FIELD_HEIGHT; j++)
                {
                    if (this.cells[i + j * Game.FIELD_WIDTH] == Game.CELL_EMPTY)
                        freeCells.add(i + j * Game.FIELD_WIDTH);
                }

            for (int i = 0; i < Math.min(targetAmount, Game.FIELD_WIDTH * Game.FIELD_HEIGHT - cellsUsed); i++)
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

    int [] cloneToArray()
    {
        return this.cells.clone();
    }
}